package com.unicom.acting.pay.domain;

import java.util.List;

/**
 * 账务交易MQ对象，主要包含以下MQ对象内容：
 * 账务后台交易工单MQ
 * 缴费日志MQ
 * 存取款日志MQ
 * 账本MQ
 * 信控缴费解耦MQ
 * 用户实时结余MQ
 * 信控缴费接口MQ
 * 短信MQ
 *
 * @author Wangkh
 */
public class TradeCommResultInfo {
    /**
     * @see #payLog 缴费日志表
     */
    private PayLog payLog;

    /**
     * @see #clPayLogs 省份代收日志
     */
    private List<CLPayLog> clPayLogs;

    /**
     * @see #payOtherLog 缴费其他日志表
     */
    private PayOtherLog payOtherLog;

    /**
     * @see #accessLogs 存取款日志
     */
    private List<AccessLog> accessLogs;

    /**
     * @see #writeOffLogs 销账日志
     */
    private List<WriteOffLog> writeOffLogs;

    /**
     * @see #payLogDmn 账务后台交易日志表
     */
    private PayLogDmn payLogDmn;

    /**
     * @see #writeSnapLog 销账快照
     */
    private WriteSnapLog writeSnapLog;

    /**
     * @see #payLogDmnMQInfo 账务后台交易工单MQ
     */
    private PayLogDmnMQInfo payLogDmnMQInfo;

    /**
     * @see #jfCreditMQInfo  信控缴费接口MQ
     */
    private JFCreditMQInfo jfCreditMQInfo;

    /**
     * @see #leaveRealFeeMQInfo 用户实时结余MQ
     */
    private LeaveRealFeeMQInfo leaveRealFeeMQInfo;

    /**
     * @see #payLogMQInfo 缴费日志MQ
     */
    private PayLogMQInfo payLogMQInfo;

    /**
     * @see #accessLogMQInfos 缴费日志MQ
     */
    private List<AccessLogMQInfo> accessLogMQInfos;

    /**
     * @see #depositMQInfos 账本MQ
     */
    private List<DepositMQInfo> depositMQInfos;

    /**
     * @see #recvCreditMQInfo 信控缴费解耦
     */
    private RecvCreditMQInfo recvCreditMQInfo;

    /**
     * @see #smsOrders 短信工单
     */
    private List<SmsOrder> smsOrders;

    /**
     * @see #smsMQInfos 短信MQ
     */
    private List<SmsMQInfo> smsMQInfos;

    /**
     * @see #tradeHyLog 外围对账日志
     */
    private TradeHyLog tradeHyLog;

    /**
     * @see #asynWork 大合帐异步工单对象
     */
    private AsynWork asynWork;

    /**
     * @see #asynWorkMQInfo 大合帐异步工单MQ
     */
    private AsynWorkMQInfo asynWorkMQInfo;


    public PayLog getPayLog() {
        return payLog;
    }

    public void setPayLog(PayLog payLog) {
        this.payLog = payLog;
    }

    public List<CLPayLog> getClPayLogs() {
        return clPayLogs;
    }

    public void setClPayLogs(List<CLPayLog> clPayLogs) {
        this.clPayLogs = clPayLogs;
    }

    public PayOtherLog getPayOtherLog() {
        return payOtherLog;
    }

    public void setPayOtherLog(PayOtherLog payOtherLog) {
        this.payOtherLog = payOtherLog;
    }

    public List<AccessLog> getAccessLogs() {
        return accessLogs;
    }

    public void setAccessLogs(List<AccessLog> accessLogs) {
        this.accessLogs = accessLogs;
    }

    public List<WriteOffLog> getWriteOffLogs() {
        return writeOffLogs;
    }

    public void setWriteOffLogs(List<WriteOffLog> writeOffLogs) {
        this.writeOffLogs = writeOffLogs;
    }

    public PayLogDmn getPayLogDmn() {
        return payLogDmn;
    }

    public void setPayLogDmn(PayLogDmn payLogDmn) {
        this.payLogDmn = payLogDmn;
    }

    public WriteSnapLog getWriteSnapLog() {
        return writeSnapLog;
    }

    public void setWriteSnapLog(WriteSnapLog writeSnapLog) {
        this.writeSnapLog = writeSnapLog;
    }

    public PayLogDmnMQInfo getPayLogDmnMQInfo() {
        return payLogDmnMQInfo;
    }

    public void setPayLogDmnMQInfo(PayLogDmnMQInfo payLogDmnMQInfo) {
        this.payLogDmnMQInfo = payLogDmnMQInfo;
    }

    public JFCreditMQInfo getJfCreditMQInfo() {
        return jfCreditMQInfo;
    }

    public void setJfCreditMQInfo(JFCreditMQInfo jfCreditMQInfo) {
        this.jfCreditMQInfo = jfCreditMQInfo;
    }

    public LeaveRealFeeMQInfo getLeaveRealFeeMQInfo() {
        return leaveRealFeeMQInfo;
    }

    public void setLeaveRealFeeMQInfo(LeaveRealFeeMQInfo leaveRealFeeMQInfo) {
        this.leaveRealFeeMQInfo = leaveRealFeeMQInfo;
    }

    public PayLogMQInfo getPayLogMQInfo() {
        return payLogMQInfo;
    }

    public void setPayLogMQInfo(PayLogMQInfo payLogMQInfo) {
        this.payLogMQInfo = payLogMQInfo;
    }

    public List<AccessLogMQInfo> getAccessLogMQInfos() {
        return accessLogMQInfos;
    }

    public void setAccessLogMQInfos(List<AccessLogMQInfo> accessLogMQInfos) {
        this.accessLogMQInfos = accessLogMQInfos;
    }

    public List<DepositMQInfo> getDepositMQInfos() {
        return depositMQInfos;
    }

    public void setDepositMQInfos(List<DepositMQInfo> depositMQInfos) {
        this.depositMQInfos = depositMQInfos;
    }

    public RecvCreditMQInfo getRecvCreditMQInfo() {
        return recvCreditMQInfo;
    }

    public void setRecvCreditMQInfo(RecvCreditMQInfo recvCreditMQInfo) {
        this.recvCreditMQInfo = recvCreditMQInfo;
    }

    public List<SmsOrder> getSmsOrders() {
        return smsOrders;
    }

    public void setSmsOrders(List<SmsOrder> smsOrders) {
        this.smsOrders = smsOrders;
    }

    public List<SmsMQInfo> getSmsMQInfos() {
        return smsMQInfos;
    }

    public void setSmsMQInfos(List<SmsMQInfo> smsMQInfos) {
        this.smsMQInfos = smsMQInfos;
    }

    public TradeHyLog getTradeHyLog() {
        return tradeHyLog;
    }

    public void setTradeHyLog(TradeHyLog tradeHyLog) {
        this.tradeHyLog = tradeHyLog;
    }

    public AsynWork getAsynWork() {
        return asynWork;
    }

    public void setAsynWork(AsynWork asynWork) {
        this.asynWork = asynWork;
    }

    public AsynWorkMQInfo getAsynWorkMQInfo() {
        return asynWorkMQInfo;
    }

    public void setAsynWorkMQInfo(AsynWorkMQInfo asynWorkMQInfo) {
        this.asynWorkMQInfo = asynWorkMQInfo;
    }

    @Override
    public String toString() {
        return "TradeCommResultInfo{" +
                "payLog=" + payLog +
                ", clPayLogs=" + clPayLogs +
                ", payOtherLog=" + payOtherLog +
                ", accessLogs=" + accessLogs +
                ", writeOffLogs=" + writeOffLogs +
                ", payLogDmn=" + payLogDmn +
                ", writeSnapLog=" + writeSnapLog +
                ", payLogDmnMQInfo=" + payLogDmnMQInfo +
                ", jfCreditMQInfo=" + jfCreditMQInfo +
                ", leaveRealFeeMQInfo=" + leaveRealFeeMQInfo +
                ", payLogMQInfo=" + payLogMQInfo +
                ", accessLogMQInfos=" + accessLogMQInfos +
                ", depositMQInfos=" + depositMQInfos +
                ", recvCreditMQInfo=" + recvCreditMQInfo +
                ", smsOrders=" + smsOrders +
                ", smsMQInfos=" + smsMQInfos +
                ", tradeHyLog=" + tradeHyLog +
                ", asynWork=" + asynWork +
                ", asynWorkMQInfo=" + asynWorkMQInfo +
                '}';
    }
}

