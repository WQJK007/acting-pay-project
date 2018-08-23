package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

import java.util.List;

/**
 * 缴费全量同步MQ消息
 * 主要包含
 * 缴费临时日志MQ
 * 缴费日志MQ
 * 存取款日志MQ
 * 账本MQ
 * 信控解耦工单MQ
 * 用户结余MQ
 * 缴费信控工单MQ
 * 短信工单MQ
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecvFeeAsynAllMsg {
    private PayLogDmnMQInfo payLogDmnMQInfo;
    private PayLogMQInfo payLogMQInfo;
    private List<AccessLogMQInfo> accessLogMQInfoList;
    private List<DepositMQInfo> depositMQInfoList;
    private RecvCreditMQInfo recvCreditMQInfo;
    private JFCreditMQInfo jfCreditMQInfo;
    private LeaveRealFeeMQInfo leaveRealFeeMQInfo;
    private List<SmsMQInfo> smsMQInfoList;

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

    public List<AccessLogMQInfo> getAccessLogMQInfoList() {
        return accessLogMQInfoList;
    }

    public void setAccessLogMQInfoList(List<AccessLogMQInfo> accessLogMQInfoList) {
        this.accessLogMQInfoList = accessLogMQInfoList;
    }

    public List<DepositMQInfo> getDepositMQInfoList() {
        return depositMQInfoList;
    }

    public void setDepositMQInfoList(List<DepositMQInfo> depositMQInfoList) {
        this.depositMQInfoList = depositMQInfoList;
    }

    public RecvCreditMQInfo getRecvCreditMQInfo() {
        return recvCreditMQInfo;
    }

    public void setRecvCreditMQInfo(RecvCreditMQInfo recvCreditMQInfo) {
        this.recvCreditMQInfo = recvCreditMQInfo;
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

    public List<SmsMQInfo> getSmsMQInfoList() {
        return smsMQInfoList;
    }

    public void setSmsMQInfoList(List<SmsMQInfo> smsMQInfoList) {
        this.smsMQInfoList = smsMQInfoList;
    }

    @Override
    public String toString() {
        return "RecvFeeAsynAllMsg{" +
                "payLogDmnMQInfo=" + payLogDmnMQInfo +
                ", payLogMQInfo=" + payLogMQInfo +
                ", accessLogMQInfoList=" + accessLogMQInfoList +
                ", depositMQInfoList=" + depositMQInfoList +
                ", recvCreditMQInfo=" + recvCreditMQInfo +
                ", jfCreditMQInfo=" + jfCreditMQInfo +
                ", leaveRealFeeMQInfo=" + leaveRealFeeMQInfo +
                ", smsMQInfoList=" + smsMQInfoList +
                '}';
    }
}
