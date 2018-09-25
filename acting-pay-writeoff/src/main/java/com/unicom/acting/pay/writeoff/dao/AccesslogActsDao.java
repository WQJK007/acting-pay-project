package com.unicom.acting.pay.writeoff.dao;


import com.unicom.skyark.component.dao.IBaseDao;

/**
 * @author ducj
 */
public interface AccesslogActsDao extends IBaseDao {

    /**
     * 账户中心- 根据acctId和chargeId判断存取款日志是否存在
     * @param acctId
     * @param chargeId
     * @return
     */
    boolean ifExistAccesslogByAcctIdAndChargeId(String acctId, String chargeId);
}
