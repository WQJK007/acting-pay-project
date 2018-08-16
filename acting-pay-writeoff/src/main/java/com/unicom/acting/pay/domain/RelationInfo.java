package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SkyArkPropertyNamingStrategy.class)
public class RelationInfo {
    private String userId;
    private String netTypeCode;
    private String memberRoleCode;
    private String memberRoleType;
    private String memberRoleId;
    private String memberRoleNumber;
    private String startDate;
    private String endDate;
    private String discntPriorty;
    private String relationTypeCode;
    private String ItemId;
    private String provCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getMemberRoleCode() {
        return memberRoleCode;
    }

    public void setMemberRoleCode(String memberRoleCode) {
        this.memberRoleCode = memberRoleCode;
    }

    public String getMemberRoleType() {
        return memberRoleType;
    }

    public void setMemberRoleType(String memberRoleType) {
        this.memberRoleType = memberRoleType;
    }

    public String getMemberRoleId() {
        return memberRoleId;
    }

    public void setMemberRoleId(String memberRoleId) {
        this.memberRoleId = memberRoleId;
    }

    public String getMemberRoleNumber() {
        return memberRoleNumber;
    }

    public void setMemberRoleNumber(String memberRoleNumber) {
        this.memberRoleNumber = memberRoleNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDiscntPriorty() {
        return discntPriorty;
    }

    public void setDiscntPriorty(String discntPriorty) {
        this.discntPriorty = discntPriorty;
    }

    public String getRelationTypeCode() {
        return relationTypeCode;
    }

    public void setRelationTypeCode(String relationTypeCode) {
        this.relationTypeCode = relationTypeCode;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    @Override
    public String toString() {
        return "UserMember{" +
                "userId='" + userId + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", memberRoleCode='" + memberRoleCode + '\'' +
                ", memberRoleType='" + memberRoleType + '\'' +
                ", memberRoleId='" + memberRoleId + '\'' +
                ", memberRoleNumber='" + memberRoleNumber + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", discntPriorty='" + discntPriorty + '\'' +
                ", relationTypeCode='" + relationTypeCode + '\'' +
                ", ItemId='" + ItemId + '\'' +
                ", provCode='" + provCode + '\'' +
                '}';
    }
}
