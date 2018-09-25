package com.unicom.acting.pay.domain;


import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;

/**
 * 通知中心工单信息
 *
 * @author Wangkh
 */
public class NoticeInfo implements Cloneable{
    private String smsNoticeId;
    private String accessCode;
    private String sendTimeCode;
    private String recvObjectType;
    private String recvObject;
    private String acctId;
    private String eparchyCode;
    private String provinceCode;
    private String productId;
    private String noticeContent;
    private String templetId;
    private String sysdate;
    private String sysdate2;
    private String recvfee;
    private String currentavlfee;
    private String allnewbalance;
    private String allnewrowefee;
    private String allnewmoney;
    private String allnewbowefee;
    private String allrealfee;
    private String month;
    private String serialnumberOut;
    private String generateTime;
    private String sendTimeStart;
    private String endTime;
    private String reviewFlag;
    private String remark;

    public String getSmsNoticeId() {
        return smsNoticeId;
    }

    public void setSmsNoticeId(String smsNoticeId) {
        this.smsNoticeId = smsNoticeId;
    }

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

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
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

    public String getSysdate() {
        return sysdate;
    }

    public void setSysdate(String sysdate) {
        this.sysdate = sysdate;
    }

    public String getSysdate2() {
        return sysdate2;
    }

    public void setSysdate2(String sysdate2) {
        this.sysdate2 = sysdate2;
    }

    public String getRecvfee() {
        return recvfee;
    }

    public void setRecvfee(String recvfee) {
        this.recvfee = recvfee;
    }

    public String getCurrentavlfee() {
        return currentavlfee;
    }

    public void setCurrentavlfee(String currentavlfee) {
        this.currentavlfee = currentavlfee;
    }

    public String getAllnewbalance() {
        return allnewbalance;
    }

    public void setAllnewbalance(String allnewbalance) {
        this.allnewbalance = allnewbalance;
    }

    public String getAllnewrowefee() {
        return allnewrowefee;
    }

    public void setAllnewrowefee(String allnewrowefee) {
        this.allnewrowefee = allnewrowefee;
    }

    public String getAllnewmoney() {
        return allnewmoney;
    }

    public void setAllnewmoney(String allnewmoney) {
        this.allnewmoney = allnewmoney;
    }

    public String getAllnewbowefee() {
        return allnewbowefee;
    }

    public void setAllnewbowefee(String allnewbowefee) {
        this.allnewbowefee = allnewbowefee;
    }

    public String getAllrealfee() {
        return allrealfee;
    }

    public void setAllrealfee(String allrealfee) {
        this.allrealfee = allrealfee;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSerialnumberOut() {
        return serialnumberOut;
    }

    public void setSerialnumberOut(String serialnumberOut) {
        this.serialnumberOut = serialnumberOut;
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
    public NoticeInfo clone() {
        try {
            return (NoticeInfo)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "NoticeInfo clone error");
        }
    }

    @Override
    public String toString() {
        return "NoticeInfo{" +
                "smsNoticeId='" + smsNoticeId + '\'' +
                ", accessCode='" + accessCode + '\'' +
                ", sendTimeCode='" + sendTimeCode + '\'' +
                ", recvObjectType='" + recvObjectType + '\'' +
                ", recvObject='" + recvObject + '\'' +
                ", acctId='" + acctId + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", productId='" + productId + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", templetId='" + templetId + '\'' +
                ", sysdate='" + sysdate + '\'' +
                ", sysdate2='" + sysdate2 + '\'' +
                ", recvfee='" + recvfee + '\'' +
                ", currentavlfee='" + currentavlfee + '\'' +
                ", allnewbalance='" + allnewbalance + '\'' +
                ", allnewrowefee='" + allnewrowefee + '\'' +
                ", allnewmoney='" + allnewmoney + '\'' +
                ", allnewbowefee='" + allnewbowefee + '\'' +
                ", allrealfee='" + allrealfee + '\'' +
                ", month='" + month + '\'' +
                ", serialnumberOut='" + serialnumberOut + '\'' +
                ", generateTime='" + generateTime + '\'' +
                ", sendTimeStart='" + sendTimeStart + '\'' +
                ", endTime='" + endTime + '\'' +
                ", reviewFlag='" + reviewFlag + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
