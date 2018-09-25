package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.WriteOffLog;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * TF_B_WRITEOFFLOG表的增查改操作
 *
 * @author Wangkh
 */
public interface WriteOffLogDao extends IBaseDao {
    /**
     * 新增销账日志数据
     *
     * @param writeOffLogs
     */
    void insertWriteOffLog(List<WriteOffLog> writeOffLogs);


    /**
     * 查询销账日志
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return 销账日志
     */
    List<WriteOffLog> getWriteOffLogByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 查询抵扣销账日志
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return 抵扣销账日志
     */
    List<WriteOffLog> getWriteOffLogDByChargeIdAndAcctId(String chargeId, String acctId);



    /**
     * 返销更新销账日志表
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return 更新结果
     */
    int updateWriteoffCancelTag(String chargeId, String acctId);

    /**
     * 返销更新抵扣销账日志表
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return 更新结果
     */
    int updateWriteoffDCancelTag(String chargeId, String acctId);
}
