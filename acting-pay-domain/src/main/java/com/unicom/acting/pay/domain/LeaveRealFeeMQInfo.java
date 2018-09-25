package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

import java.util.List;

/**
 * 用户实时结余MQ同步对象信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LeaveRealFeeMQInfo {
    private List<UserLeaveFeeInfo> userLeaveFeeInfos;
    private long realFee;
    private char inTag;
    private String updateTime;

    public List<UserLeaveFeeInfo> getUserLeaveFeeInfos() {
        return userLeaveFeeInfos;
    }

    public void setUserLeaveFeeInfos(List<UserLeaveFeeInfo> userLeaveFeeInfos) {
        this.userLeaveFeeInfos = userLeaveFeeInfos;
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
                "userLeaveFeeInfos=" + userLeaveFeeInfos +
                ", realFee=" + realFee +
                ", inTag=" + inTag +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
