package com.unicom.acting.pay.domain;

import java.util.List;

/**
 * 信控MQ对象
 *
 * @author Wangkh
 */
public class CreditMQInfo {
    private List<JFCreditMQInfo> jfCreditMQInfos;
    private List<LeaveRealFeeMQInfo> leaveRealFeeMQInfos;
    private RecvCreditMQInfo recvCreditMQInfo;

    public List<JFCreditMQInfo> getJfCreditMQInfos() {
        return jfCreditMQInfos;
    }

    public void setJfCreditMQInfos(List<JFCreditMQInfo> jfCreditMQInfos) {
        this.jfCreditMQInfos = jfCreditMQInfos;
    }

    public List<LeaveRealFeeMQInfo> getLeaveRealFeeMQInfos() {
        return leaveRealFeeMQInfos;
    }

    public void setLeaveRealFeeMQInfos(List<LeaveRealFeeMQInfo> leaveRealFeeMQInfos) {
        this.leaveRealFeeMQInfos = leaveRealFeeMQInfos;
    }

    public RecvCreditMQInfo getRecvCreditMQInfo() {
        return recvCreditMQInfo;
    }

    public void setRecvCreditMQInfo(RecvCreditMQInfo recvCreditMQInfo) {
        this.recvCreditMQInfo = recvCreditMQInfo;
    }

    @Override
    public String toString() {
        return "CreditMQInfo{" +
                "jfCreditMQInfos=" + jfCreditMQInfos +
                ", leaveRealFeeMQInfos=" + leaveRealFeeMQInfos +
                ", recvCreditMQInfo=" + recvCreditMQInfo +
                '}';
    }
}
