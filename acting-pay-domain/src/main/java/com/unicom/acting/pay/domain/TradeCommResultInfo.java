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
     * @see #smsMQInfos 短信MQ
     */
    private List<SmsMQInfo> smsMQInfos;

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

    public List<SmsMQInfo> getSmsMQInfos() {
        return smsMQInfos;
    }

    public void setSmsMQInfos(List<SmsMQInfo> smsMQInfos) {
        this.smsMQInfos = smsMQInfos;
    }

    @Override
    public String toString() {
        return "TradeCommResultInfo{" +
                "payLogDmnMQInfo=" + payLogDmnMQInfo +
                ", jfCreditMQInfo=" + jfCreditMQInfo +
                ", leaveRealFeeMQInfo=" + leaveRealFeeMQInfo +
                ", payLogMQInfo=" + payLogMQInfo +
                ", accessLogMQInfos=" + accessLogMQInfos +
                ", depositMQInfos=" + depositMQInfos +
                ", recvCreditMQInfo=" + recvCreditMQInfo +
                ", smsMQInfos=" + smsMQInfos +
                '}';
    }
}

