package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.WriteSnapLog;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * TF_B_WRITESNAP_LOG表增查改操作
 *
 * @author Wangkh
 */
public interface WriteSnapLogDao extends IBaseDao {
    /**
     * 新增销账快照日志数据
     *
     * @param writeSnapLog
     * @return
     */
    int insertWriteSnapLog(WriteSnapLog writeSnapLog);


    /**
     * 查询快照日志
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    int getWriteOffSnapLogByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 查询抵扣快照日志
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    int getWriteOffSnapLogDByChargeIdAndAcctId(String chargeId, String acctId);
    /**
     * 返销更新快照表
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    int updateWriteoffSnapCancelTag(String chargeId, String acctId);

    /**
     * 更新抵扣快照表
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    int updateWriteoffDSanpCancelTag(String chargeId, String acctId);
}
