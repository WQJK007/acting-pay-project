package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 信控工单用户实时结余
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JFUserLeaveFeeInfo {
    private String userId;
    private long leaveRealFee;
    private String processTag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getLeaveRealFee() {
        return leaveRealFee;
    }

    public void setLeaveRealFee(long leaveRealFee) {
        this.leaveRealFee = leaveRealFee;
    }

    public String getProcessTag() {
        return processTag;
    }

    public void setProcessTag(String processTag) {
        this.processTag = processTag;
    }

    @Override
    public String toString() {
        return "JFUserLeaveFeeInfo{" +
                "userId='" + userId + '\'' +
                ", leaveRealFee=" + leaveRealFee +
                ", processTag='" + processTag + '\'' +
                '}';
    }
}
