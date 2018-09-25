package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

import java.util.List;

/**
 * 余额转账消息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TransFeeMQMsg {
    private List<PayLogMQInfo> payLogMQInfo;
    private List<AccessLogMQInfo> accessLogMQInfo;
    private List<DepositMQInfo> depositMQInfo;
    private RecvCreditMQInfo recvCreditMQInfo;
    private List<SmsMQInfo> smsMQInfo;
    private TransFeeMQMsgKey transfeeCheckInfo;

    public List<PayLogMQInfo> getPayLogMQInfo() {
        return payLogMQInfo;
    }

    public void setPayLogMQInfo(List<PayLogMQInfo> payLogMQInfo) {
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

    public List<SmsMQInfo> getSmsMQInfo() {
        return smsMQInfo;
    }

    public void setSmsMQInfo(List<SmsMQInfo> smsMQInfo) {
        this.smsMQInfo = smsMQInfo;
    }

    public TransFeeMQMsgKey getTransfeeCheckInfo() {
        return transfeeCheckInfo;
    }

    public void setTransfeeCheckInfo(TransFeeMQMsgKey transfeeCheckInfo) {
        this.transfeeCheckInfo = transfeeCheckInfo;
    }

    @Override
    public String toString() {
        return "TransFeeMQMsg{" +
                "payLogMQInfo=" + payLogMQInfo +
                ", accessLogMQInfo=" + accessLogMQInfo +
                ", depositMQInfo=" + depositMQInfo +
                ", recvCreditMQInfo=" + recvCreditMQInfo +
                ", smsMQInfo=" + smsMQInfo +
                ", transfeeCheckInfo=" + transfeeCheckInfo +
                '}';
    }
}
