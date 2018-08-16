package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 用户实时结余MQ同步对象信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaveRealFeeMQInfo {
    private String userId;
    private long leaveRealFee;
    private long realFee;
    private char inTag;
    private String updateTime;

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

    public long getRealFee() {
        return realFee;
    }

    public void setRealFee(long realFee) {
        this.realFee = realFee;
    }

    public char getInTag() {
        return inTag;
    }

    public void setInTag(char inTag) {
        this.inTag = inTag;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "LeaveRealFeeMQInfo{" +
                "userId='" + userId + '\'' +
                ", leaveRealFee=" + leaveRealFee +
                ", realFee=" + realFee +
                ", inTag=" + inTag +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
