package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.domain.PayOtherLog;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 账务交易核心日志入库服务
 * 包含对TF_B_PAYLOG，TF_B_ACCESSlOG，TF_B_WRITEOFF_LOG，
 * TF_B_WRITESNAP_LOG等表的入库以及MQ信息生成功能
 *
 * @author Wangkh
 */
public interface TradeCommLogService extends IBaseService {
    /**
     * 账务费用交易日志公共入库方法
     *
     * @param tradeCommResultInfo
     */
    void tradeFeeCommInDB(TradeCommResultInfo tradeCommResultInfo);

    /**
     * 新增缴费日志记录
     *
     * @param payLog
     */
    void insertPayLog(PayLog payLog);

    /**
     * 生成代收费日志
     *
     * @param clPayLogs
     */
    void insertCLPayLog(List<CLPayLog> clPayLogs);

    /**
     * 收费其他信息日志
     *
     * @param payOtherLog
     */
    void insertPayOtherLog(PayOtherLog payOtherLog);

    /**
     * 交易关联日志
     *
     * @param chargeRelation
     */
    void insertChargerelation(ChargeRelation chargeRelation);

    /**
     * 提取销账日志信息
     *
     * @param writeOffLogs
     * @param payLog
     * @param writeOffRuleInfo
     * @return
     */
    List<FeeWriteOffLog> genWriteOffLogInfo(List<FeeWriteOffLog> writeOffLogs,
                                            FeePayLog payLog, WriteOffRuleInfo writeOffRuleInfo);

    /**
     * 销账日志入库
     *
     * @param writeOffLogs
     */
    void insertWriteOffLog(List<WriteOffLog> writeOffLogs);

    /**
     * 提取入库的存取款记录
     *
     * @param feeAccessLogs     存取款日志集
     * @param feePayLog         缴费日志
     * @param existsWriteOffLog 是否存在销账日志
     * @return
     */
    List<FeeAccessLog> genAccessLogInfo(List<FeeAccessLog> feeAccessLogs,
                                        FeePayLog feePayLog, boolean existsWriteOffLog);

    /**
     * 新增存取款日志记录
     *
     * @param accessLogs 存取款记录
     */
    void insertAccessLog(List<AccessLog> accessLogs);

    /**
     * 新增销账快照表记录
     *
     * @param writeSnapLog
     */
    void insertWriteSnapLog(WriteSnapLog writeSnapLog);

    /**
     * 根据缴费日志生成PayLogMQ对象信息
     *
     * @param Paylog
     * @return
     */
    PayLogMQInfo genPayLogMQInfo(PayLog Paylog);

    /**
     * 根据存取款日志生成AccessLogMQ对象信息
     *
     * @param accessLogs
     * @return
     */
    List<AccessLogMQInfo> genAccessLogMQInfo(List<AccessLog> accessLogs);
}
