package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.acting.pay.dao.BillDao;
import com.unicom.acting.pay.domain.Bill;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.pay.writeoff.service.BillPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;

/**
 * 对实时账单，往月账单和坏账账单等账单表的操作
 *
 * @author Administrators
 */
@Service
public class BillPayServiceImpl implements BillPayService {
    private Logger logger = LoggerFactory.getLogger(BillPayServiceImpl.class);
    @Autowired
    private BillDao billPayDao;

    @Override
    public boolean hasBadBillByAcctId(String acctId) {
        return billPayDao.hasBadBillByAcctId(acctId);
    }

    @Override
    public int updateBillBalance(Bill bill) {
        return billPayDao.updateBillBalance(bill);
    }

    @Override
    public int updateBadBillBalance(Bill bill) {
        return billPayDao.updateBadBillBalance(bill);
    }

    @Override
    public void updateBillInfo(List<FeeBill> bills, PayLog payLog, boolean hasBadBill) {
        if (CollectionUtils.isEmpty(bills)) {
            return;
        }
        //用于更新账单信息
        Bill tmpBill = new Bill();
        for (FeeBill bill : bills) {
            if (bill.getCanpayTag() != '2'
                    && (bill.getCurrWriteOffBalance() != 0 || bill.getCurrWriteOffLate() != 0
                    || bill.getPayTag() != bill.getOldPayTag()
                    || '9' == bill.getPayTag() || '5' == bill.getPayTag() || '1' == bill.getBillPayTag())) {
                tmpBill.init();
                tmpBill.setAcctId(bill.getAcctId());
                tmpBill.setBillId(bill.getBillId());
                tmpBill.setUserId(bill.getUserId());
                tmpBill.setIntegrateItemCode(bill.getIntegrateItemCode());
                tmpBill.setBalance(bill.getBalance() - bill.getCurrWriteOffBalance());
                tmpBill.setLateFee(bill.getLateFee() + bill.getNewLateFee() - bill.getDerateFee());
                tmpBill.setLateBalance(bill.getLateBalance() + bill.getNewLateFee() - bill.getDerateFee() - bill.getCurrWriteOffLate());
                if (!StringUtil.isEmpty(bill.getLateCalDate()) && bill.getLateCalDate().length() > 6) {
                    tmpBill.setLateCalDate(bill.getLateCalDate());
                }
                tmpBill.setNewPayTag(String.valueOf(bill.getPayTag()));
                tmpBill.setNewBillPayTag(String.valueOf(bill.getBillPayTag()));
                tmpBill.setWriteoffFee1(bill.getWriteoffFee1());
                tmpBill.setWriteoffFee2(bill.getWriteoffFee2());
                tmpBill.setWriteoffFee3(bill.getWriteoffFee3());
                tmpBill.setChargeId(bill.getChargeId());
                tmpBill.setVersionNo(bill.getVersionNo());
                tmpBill.setUpdateTime(payLog.getRecvTime());
                tmpBill.setUpdateDepartId(payLog.getRecvDepartId());
                tmpBill.setUpdateStaffId(payLog.getRecvStaffId());
                //更新账单表
                int updateBill = updateBillBalance(tmpBill);
                //更新坏账表
                if (hasBadBill && updateBill == 0) {
                    updateBill = updateBadBillBalance(tmpBill);
                }

                if (updateBill != 1) {
                    throw new SkyArkException(
                            MessageFormat.format(
                                    "帐单发生变化,更新失败!(acctId={0},billId={1},integrateItemCode={2},versionNo={3})",
                                    tmpBill.getAcctId(), tmpBill.getBillId(), String.valueOf(tmpBill.getIntegrateItemCode()),
                                    String.valueOf(tmpBill.getVersionNo())));
                }
            }
        }
    }

}
