package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.AccessLog;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * TF_B_ACCESSLOG表的增查改操作
 *
 * @author Wangkh
 */
public interface AccessLogDao extends IBaseDao {
    /**
     * 新增存取款日志数据
     *
     * @param accessLogs
     */
    void insertAccessLog(List<AccessLog> accessLogs);


    /**
     * 根据交费流水和账户标识校验原交费的存取款日志
     *
     * @param acctId
     * @param chargeId
     * @return
     */
    List<AccessLog> getOrigAccesslogsByAcctIdAndChargeId(String acctId, String chargeId);

    /**
     * 根据交费流水和账户标识获取存取款日志
     *
     * @param acctId
     * @param chargeId
     * @return
     */
    List<AccessLog> getAccesslogsByAcctIdAndChargeId(String acctId, String chargeId);

    /**
     * 根据交费流水和账户标识获取抵扣存取款日志
     *
     * @param acctId
     * @param chargeId
     * @return
     */
    List<AccessLog> getAccesslogDByAcctIdAndChargeId(String acctId, String chargeId);

    /**
     * 隔笔返销判断
     *
     * @param acctId
     * @param time
     * @return
     */
    int isLastAccesslog(String acctId, String time);


    /**
     * 隔笔返销判断
     * @param acctId
     * @param time
     * @return
     */
    int isLastAccesslogD(String acctId, String time);

    /**
     * 返销存取款日志
     *
     * @param chargeId
     * @param acctId
     * @return
     */
    int updateAccesslogByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 返销抵扣存取款日志
     *
     * @param chargeId
     * @param acctId
     * @return
     */
    int updateAccesslogDByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 存取款日志入库
     *
     * @param accessLog
     * @return
     */
    int insertAccesslog(AccessLog accessLog);

    /**
     * 抵扣存取款日志入库
     *
     * @param accessLog
     * @return
     */
    int insertAccesslogD(AccessLog accessLog);
}
