package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.acting.pay.domain.Bill;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 对实时账单，往月账单和坏账账单等账单表的操作
 *
 * @author Administrators
 */
public interface BillPayService extends IBaseService {
    /**
     * 账户是否还存在坏账
     * @param acctId
     
     * @return
     */
    boolean hasBadBillByAcctId(String acctId);

    /**
     * 更新账单信息
     *
     * @param bill
     
     * @return
     */
    int updateBillBalance(Bill bill);

    /**
     * 更新坏账账单信息
     *
     * @param bill
     
     * @return
     */
    int updateBadBillBalance(Bill bill);

    /**
     * 更新账单表信息
     *
     * @param bills
     * @param payLog
     * @param hasBadBill
     */
    void updateBillInfo(List<FeeBill> bills, PayLog payLog, boolean hasBadBill);
}
