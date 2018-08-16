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
public class TradeCommMQInfo {
    /**
     * @see #payLogDmnMQInfo 账务后台交易工单MQ
     */
    private PayLogDmnMQInfo payLogDmnMQInfo;

    /**
     * @see #jfCreditMqInfos  信控缴费接口MQ
     */
    private List<JFCreditMQInfo> jfCreditMqInfos;

    /**
     * @see #leaveRealFeeMQInfos 用户实时结余MQ
     */
    private List<LeaveRealFeeMQInfo> leaveRealFeeMQInfos;

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
     * @see #smsMQInfos 短信MQ
     */
    private List<SmsMQInfo> smsMQInfos;

    public PayLogDmnMQInfo getPayLogDmnMQInfo() {
        return payLogDmnMQInfo;
    }

    public void setPayLogDmnMQInfo(PayLogDmnMQInfo payLogDmnMQInfo) {
        this.payLogDmnMQInfo = payLogDmnMQInfo;
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

    public List<JFCreditMQInfo> getJfCreditMqInfos() {
        return jfCreditMqInfos;
    }

    public void setJfCreditMqInfos(List<JFCreditMQInfo> jfCreditMqInfos) {
        this.jfCreditMqInfos = jfCreditMqInfos;
    }

    public List<LeaveRealFeeMQInfo> getLeaveRealFeeMQInfos() {
        return leaveRealFeeMQInfos;
    }

    public void setLeaveRealFeeMQInfos(List<LeaveRealFeeMQInfo> leaveRealFeeMQInfos) {
        this.leaveRealFeeMQInfos = leaveRealFeeMQInfos;
    }

    public List<SmsMQInfo> getSmsMQInfos() {
        return smsMQInfos;
    }

    public void setSmsMQInfos(List<SmsMQInfo> smsMQInfos) {
        this.smsMQInfos = smsMQInfos;
    }

    @Override
    public String toString() {
        return "RecvFeeAsynAllMsg{" +
                "payLogDmnMQInfo=" + payLogDmnMQInfo +
                ", payLogMQInfo=" + payLogMQInfo +
                ", accessLogMQInfos=" + accessLogMQInfos +
                ", depositMQInfos=" + depositMQInfos +
                ", recvCreditMQInfo=" + recvCreditMQInfo +
                ", jfCreditMqInfos=" + jfCreditMqInfos +
                ", leaveRealFeeMQInfos=" + leaveRealFeeMQInfos +
                ", smsMQInfos=" + smsMQInfos +
                '}';
    }


}

