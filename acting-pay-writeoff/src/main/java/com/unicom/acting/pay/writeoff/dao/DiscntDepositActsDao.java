package com.unicom.acting.pay.writeoff.dao;

import com.unicom.acting.pay.writeoff.domain.DiscntDeposit;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * @author ducj
 */
public interface DiscntDepositActsDao extends IBaseDao {

    /**
     * 根据账户标识和用户标识获取活动账本数据
     * @param userId
     * @param acctId
     * @return
     */
    List<DiscntDeposit> getDiscntDepositsByUserIdAndAcctId(String userId, String acctId);

    List<DiscntDeposit> getDiscntDepositsByChargeIdAndAcctId(String userId, String acctId);

    List<DiscntDeposit> getDiscntDepositsByEventIdIdAndAcctId(String actionEventId, String acctId);
    /**
     * 用户活动实例
     *
     * @param acctId
     * @param userId
     * @return
     */
    List<DiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId);
}
