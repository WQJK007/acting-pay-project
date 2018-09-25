package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.Bill;
import com.unicom.skyark.component.dao.IBaseDao;

/**
 * 对TS_B_BILL,TS_B_BADBILL相关的一些操作
 *
 * @author Wangkh
 */
public interface BillDao extends IBaseDao {
    /**
     * 账户是否存在坏账账单
     *
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
     * 返销更新账单
     * @param bill
     * @return
     */
    int updBillRevert(Bill bill);

    /**
     * 拷贝历史账单
     * @param acctId
     * @param userId
     * @param billId
     * @return
     */
    int copyHisBillToBill(String acctId, String userId, String billId);

    /**
     * 删除历史账单
     * @param acctId
     * @param userId
     * @param billId
     * @return
     */
    int deleteHisBill(String acctId, String userId, String billId);


    /**
     * 返销更新坏账账单
     * @param bill
     * @return
     */
    int updBillBadRevert(Bill bill);


    /**
     * 返销更新账单的bill_pay_tag
     * @param acctId
     * @param billId
     * @return
     */
    int updBillRevertBillPayTag(String acctId, String billId);


    /**
     * 返销更新坏账账单的bill_pay_tag
     * @param acctId
     * @param billId
     * @return
     */
    int updBadBillRevertBillPayTag(String acctId, String billId);

    /**
     * 还原坏帐用户
     * @param acctId
     * @param actTag
     * @return
     */
    int updateBadbillUserInfo(String acctId, String actTag);

}
