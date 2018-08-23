package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.pay.domain.AccessLogMQInfo;
import com.unicom.acting.pay.domain.PayLogMQInfo;
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
     * 新增缴费日志记录
     *
     * @param feePayLog
     * @param provinceCode
     */
    void insertPayLog(FeePayLog feePayLog, String provinceCode);

    /**
     * 生成代收费日志
     *
     * @param feeCLPayLogs
     * @param provinceCode
     */
    void insertCLPayLog(List<FeeCLPayLog> feeCLPayLogs, String provinceCode);

    /**
     * 收费其他信息日志
     *
     * @param payOtherLog
     * @param provinceCode
     */
    long insertPayOtherLog(PayOtherLog payOtherLog, String provinceCode);


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
     * @param feeWriteOffLogs
     * @param provinceCode
     */
    void insertWriteOffLog(List<FeeWriteOffLog> feeWriteOffLogs, String provinceCode);

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
     * @param feeAccessLogs 存取款记录
     * @param provinceCode  账户归属省份编码
     */
    void insertAccessLog(List<FeeAccessLog> feeAccessLogs, String provinceCode);

    /**
     * 新增销账快照表记录
     *
     * @param feeWriteSnapLog
     * @param provinceCode
     */
    void insertWriteSnapLog(FeeWriteSnapLog feeWriteSnapLog, String provinceCode);

    /**
     * 根据缴费日志生成PayLogMQ对象信息
     *
     * @param feePaylog
     * @return
     */
    PayLogMQInfo genPayLogMQInfo(FeePayLog feePaylog);

    /**
     * 根据存取款日志生成AccessLogMQ对象信息
     *
     * @param feeAccessLogs
     * @return
     */
    List<AccessLogMQInfo> genAccessLogMQInfo(List<FeeAccessLog> feeAccessLogs);
}
