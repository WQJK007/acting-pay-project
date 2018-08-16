package com.unicom.acting.pay.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 短信工单MQ同步对象信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsMQInfo {
    private String accessCode;
    private String sendTimeCode;
    private String recvObjectType;
    private String recvObject;
    private String eparchyCode;
    private String provinceCode;
    private String productId;
    private String noticeContent;
    private String templetId;
    private TempletValue templetValue;
    private String generateTime;
    private String sendTimeStart;
    private String endTime;
    private String reviewFlag;
    private String remark;

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getSendTimeCode() {
        return sendTimeCode;
    }

    public void setSendTimeCode(String sendTimeCode) {
        this.sendTimeCode = sendTimeCode;
    }

    public String getRecvObjectType() {
        return recvObjectType;
    }

    public void setRecvObjectType(String recvObjectType) {
        this.recvObjectType = recvObjectType;
    }

    public String getRecvObject() {
        return recvObject;
    }

    public void setRecvObject(String recvObject) {
        this.recvObject = recvObject;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getTempletId() {
        return templetId;
    }

    public void setTempletId(String templetId) {
        this.templetId = templetId;
    }

    public TempletValue getTempletValue() {
        return templetValue;
    }

    public void setTempletValue(TempletValue templetValue) {
        this.templetValue = templetValue;
    }

    public String getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(String generateTime) {
        this.generateTime = generateTime;
    }

    public String getSendTimeStart() {
        return sendTimeStart;
    }

    public void setSendTimeStart(String sendTimeStart) {
        this.sendTimeStart = sendTimeStart;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(String reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SmsMQInfo{" +
                "accessCode='" + accessCode + '\'' +
                ", sendTimeCode='" + sendTimeCode + '\'' +
                ", recvObjectType='" + recvObjectType + '\'' +
                ", recvObject='" + recvObject + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", productId='" + productId + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", templetId='" + templetId + '\'' +
                ", templetValue=" + templetValue +
                ", generateTime='" + generateTime + '\'' +
                ", sendTimeStart='" + sendTimeStart + '\'' +
                ", endTime='" + endTime + '\'' +
                ", reviewFlag='" + reviewFlag + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
