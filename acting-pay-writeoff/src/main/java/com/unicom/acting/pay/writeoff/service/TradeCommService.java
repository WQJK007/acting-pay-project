package com.unicom.acting.pay.writeoff.service;


import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.domain.BackFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 账务交易入库公共方法
 *
 * @author Administrators
 */
public interface TradeCommService extends IBaseService {
    /**
     * 生成缴费入库对象信息，包含MQ发送信息
     * 可以删除
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     */
    void genInDBInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo);

    /**
     * 生成缴费日志公共信息
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    PayLog genPayLog(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 生成销账快照入库对象
     *
     * @param tradeCommInfoIn
     * @param feeWriteSnapLog
     * @param payLog
     * @param CycleId
     * @return
     */
    WriteSnapLog genWriteSnapLog(TradeCommInfoIn tradeCommInfoIn, FeeWriteSnapLog feeWriteSnapLog, PayLog payLog, int CycleId);

    /**
     * 生成销账日志入库对象
     *
     * @param feeWriteOffLogs
     * @param payLog
     * @param writeOffRuleInfo
     * @return
     */
    List<WriteOffLog> genWriteOffLog(List<FeeWriteOffLog> feeWriteOffLogs, PayLog payLog, WriteOffRuleInfo writeOffRuleInfo);

    /**
     * 生成存取款日志对象
     *
     * @param tradeCommInfo
     * @param payLog
     * @param writeOffLogs
     * @return
     */
    List<AccessLog> genAccessLog(TradeCommInfo tradeCommInfo, PayLog payLog, List<WriteOffLog> writeOffLogs);

    /**
     * 根据存取款日志生成AccessLogMQ对象信息
     *
     * @param accessLogs
     * @return
     */
    List<AccessLogMQInfo> genAccessLogMQInfo(List<AccessLog> accessLogs);

    /**
     * 生成缴费日志MQ消息
     *
     * @param Paylog
     * @return
     */
    PayLogMQInfo genPayLogMQInfo(PayLog Paylog);

    /**
     * 生成省份代收费日志
     *
     * @param writeOffLogs
     * @param payLog
     * @return
     */
    List<CLPayLog> genCLPaylog(List<WriteOffLog> writeOffLogs, PayLog payLog, String headerGray);
}
