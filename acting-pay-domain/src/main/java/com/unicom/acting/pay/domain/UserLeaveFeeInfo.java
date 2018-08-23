package com.unicom.acting.pay.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 用户实时结余
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLeaveFeeInfo {
    private String userId;
    private long leaveRealFee;

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

    @Override
    public String toString() {
        return "UserLeaveFeeInfo{" +
                "userId='" + userId + '\'' +
                ", leaveRealFee=" + leaveRealFee +
                '}';
    }
}
