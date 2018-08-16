package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.pay.domain.AccessLogMQInfo;
import com.unicom.acting.pay.domain.AsynWorkMQInfo;
import com.unicom.acting.pay.domain.PayLogDmnMQInfo;
import com.unicom.acting.pay.domain.PayLogMQInfo;
import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.*;

import java.util.List;

/**
 * 销账日志表资源操作
 *
 * @author Administrators
 */
public interface WriteOffLogService extends IBaseService {
    /**
     * 新增缴费日志记录
     *
     * @param payLog
     * @param provinceCode
     * @return
     */
    long insertPayLog(PayLog payLog, String provinceCode);

    /**
     * 提取销账日志信息
     *
     * @param writeOffLogs
     * @param payLog
     * @param writeOffRuleInfo
     * @return
     */
    List<WriteOffLog> genWriteOffLogInfo(List<WriteOffLog> writeOffLogs, PayLog payLog, WriteOffRuleInfo writeOffRuleInfo);

    /**
     * 销账日志入库
     *
     * @param writeOffLogs
     * @param provinceCode
     */
    void insertWriteOffLog(List<WriteOffLog> writeOffLogs, String provinceCode);

    /**
     * 提取入库的存取款记录
     *
     * @param accessLogs        存取款日志集
     * @param payLog            缴费日志
     * @param existsWriteOffLog 是否存在销账日志
     * @return
     */
    List<AccessLog> genAccessLogInfo(List<AccessLog> accessLogs, PayLog payLog, boolean existsWriteOffLog);

    /**
     * 新增存取款日志记录
     *
     * @param accessLogList 存取款记录
     * @param provinceCode  账户归属省份编码
     */
    void insertAccessLog(List<AccessLog> accessLogList, String provinceCode);

    /**
     * 新增销账快照表记录
     *
     * @param writeSnapLog
     * @param provinceCode
     */
    void insertWriteSnapLog(WriteSnapLog writeSnapLog, String provinceCode);

    /**
     * 根据缴费日志生成PayLogMQ对象信息
     *
     * @param paylog
     * @return
     */
    PayLogMQInfo genPayLogMQInfo(PayLog paylog);

    /**
     * 根据存取款日志生成AccessLogMQ对象信息
     *
     * @param accessLogList
     * @return
     */
    List<AccessLogMQInfo> genAccessLogMQInfo(List<AccessLog> accessLogList);

    /**
     * 生成账务后台工单工单MQ信息
     * @param payLogDmn
     * @return
     */
    PayLogDmnMQInfo genPayLogDmnMQInfo(PayLogDmn payLogDmn);

    /**
     * 生成异步工单工单MQ信息
     * @param asynWork
     * @return
     */
    AsynWorkMQInfo genAsynWorkMQInfo(AsynWork asynWork);

}
