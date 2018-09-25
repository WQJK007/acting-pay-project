package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.TransLog;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * @author ducj
 */
public interface TransLogDao extends IBaseDao {
    /**
     * 根据acctId和chargeId获取transLog表中的记录
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    int getTransLogNumByCharegeIdAndAcctId(String chargeId, String acctId);

}
