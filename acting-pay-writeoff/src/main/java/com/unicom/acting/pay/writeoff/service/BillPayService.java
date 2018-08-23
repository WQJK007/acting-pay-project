package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.acting.fee.domain.FeePayLog;
import com.unicom.acting.pay.domain.Bill;
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
     * @param provinceCode
     * @return
     */
    boolean hasBadBillByAcctId(String acctId, String provinceCode);

    /**
     * 更新账单信息
     *
     * @param bill
     * @param provinceCode
     * @return
     */
    int updateBillBalance(Bill bill, String provinceCode);

    /**
     * 更新坏账账单信息
     *
     * @param bill
     * @param provinceCode
     * @return
     */
    int updateBadBillBalance(Bill bill, String provinceCode);

    /**
     * 更新账单表信息
     *
     * @param bills
     * @param payLog
     * @param hasBadBill
     */
    void updateBillInfo(List<FeeBill> bills, FeePayLog payLog, boolean hasBadBill);
}
