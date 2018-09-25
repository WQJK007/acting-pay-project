package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RecvFeeAsynAllMsg {
    private AsynWorkMQInfo asynWorkMQInfo;
    private PayLogDmnMQInfo payLogDmnMQInfo;
    private PayLogMQInfo payLogMQInfo;
    private List<AccessLogMQInfo> accessLogMQInfo;
    private List<DepositMQInfo> depositMQInfo;
    private RecvCreditMQInfo recvCreditMQInfo;
    private JFCreditMQInfo jfCreditMQInfo;
    private LeaveRealFeeMQInfo leaveRealFeeMQInfo;
    private List<SmsMQInfo> smsMQInfo;
    private RecvFeeAllMQKey recvfeeCheckInfo;

    public AsynWorkMQInfo getAsynWorkMQInfo() {
        return asynWorkMQInfo;
    }

    public void setAsynWorkMQInfo(AsynWorkMQInfo asynWorkMQInfo) {
        this.asynWorkMQInfo = asynWorkMQInfo;
    }

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

    public List<AccessLogMQInfo> getAccessLogMQInfo() {
        return accessLogMQInfo;
    }

    public void setAccessLogMQInfo(List<AccessLogMQInfo> accessLogMQInfo) {
        this.accessLogMQInfo = accessLogMQInfo;
    }

    public List<DepositMQInfo> getDepositMQInfo() {
        return depositMQInfo;
    }

    public void setDepositMQInfo(List<DepositMQInfo> depositMQInfo) {
        this.depositMQInfo = depositMQInfo;
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

    public List<SmsMQInfo> getSmsMQInfo() {
        return smsMQInfo;
    }

    public void setSmsMQInfo(List<SmsMQInfo> smsMQInfo) {
        this.smsMQInfo = smsMQInfo;
    }

    public RecvFeeAllMQKey getRecvfeeCheckInfo() {
        return recvfeeCheckInfo;
    }

    public void setRecvfeeCheckInfo(RecvFeeAllMQKey recvfeeCheckInfo) {
        this.recvfeeCheckInfo = recvfeeCheckInfo;
    }

    @Override
    public String toString() {
        return "RecvFeeAsynAllMsg{" +
                "asynWorkMQInfo=" + asynWorkMQInfo +
                ", payLogDmnMQInfo=" + payLogDmnMQInfo +
                ", payLogMQInfo=" + payLogMQInfo +
                ", accessLogMQInfo=" + accessLogMQInfo +
                ", depositMQInfo=" + depositMQInfo +
                ", recvCreditMQInfo=" + recvCreditMQInfo +
                ", jfCreditMQInfo=" + jfCreditMQInfo +
                ", leaveRealFeeMQInfo=" + leaveRealFeeMQInfo +
                ", smsMQInfo=" + smsMQInfo +
                ", recvfeeCheckInfo=" + recvfeeCheckInfo +
                '}';
    }
}
