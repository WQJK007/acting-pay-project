package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

import java.util.List;

/**
 * 预存清退账户中心MQ消息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BackFeeMQMsg {
    private PayLogMQInfo payLogMQInfo;
    private List<AccessLogMQInfo> accessLogMQInfos;
    private List<DepositMQInfo> depositMQInfos;
    private RecvCreditMQInfo recvCreditMQInfo;
    private List<SmsMQInfo> smsMQInfos;
    private BackFeeMQMsgKey backfeeCheckinfo;

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

    public BackFeeMQMsgKey getBackfeeCheckinfo() {
        return backfeeCheckinfo;
    }

    public void setBackfeeCheckinfo(BackFeeMQMsgKey backfeeCheckinfo) {
        this.backfeeCheckinfo = backfeeCheckinfo;
    }

    @Override
    public String toString() {
        return "BackFeeMQMsg{" +
                "payLogMQInfo=" + payLogMQInfo +
                ", accessLogMQInfos=" + accessLogMQInfos +
                ", depositMQInfos=" + depositMQInfos +
                ", recvCreditMQInfo=" + recvCreditMQInfo +
                ", smsMQInfos=" + smsMQInfos +
                ", backfeeCheckinfo=" + backfeeCheckinfo +
                '}';
    }
}
