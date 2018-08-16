package com.unicom.acting.pay.dao;

import com.unicom.acting.fee.domain.Bill;
import com.unicom.skyark.component.dao.IBaseDao;

/**
 * 对TS_B_BILL,TS_B_BADBILL相关的一些操作
 *
 * @author Wangkh
 */
public interface BillPayDao extends IBaseDao {
    /**
     * 账户是否存在坏账账单
     *
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
}
