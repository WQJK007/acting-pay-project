package com.unicom.acting.pay.domain;


import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;

/**
 * 短信工单对象，主要映射TI_O_SMS和I_O_SMS_TMP表关键字段
 *
 * @author Wangkh
 */
public class SmsIO implements Cloneable {
    private String acctId;
    private String smsNoticeId;
    private String eparchyCode;
    private char inModeCode;
    private String smsChannelCode;
    private String sendObjectCode;
    private String sendTimeCode;
    private String sendCountCode;
    private String recvObjectType;
    private String recvObject;
    private String id;
    private String smsTypeCode;
    /**
     * 01:短信催缴
     * 02:业务办理通知
     * 03:缴费到帐通知
     * 04:预存转兑到帐通知
     * 05:验证码通知
     * 06:维系短信
     * 07:短信查询
     * 08:告警短信
     * 09:话费提醒
     */
    private String smsKindCode;
    private char noticeContentType;
    private String noticeContent;
    private String referedCount;
    private String forceReferCount;
    private String forceObject;
    private String forceStartTime;
    private String forceEndTime;
    private String smsPriority;
    private String referTime;
    private String referStaffId;
    private String referDepartId;
    private String dealTime;
    private String dealStaffId;
    private String dealDepartId;
    private char dealState;
    private String remark;
    private String revc1;
    private String revc2;
    private String revc3;
    private String revc4;
    /**
     * 省份短信发送类型
     */
    private String smsSendType;
    /**
     * 融合业务主号码
     */
    private String mainSN;
    /**
     * 融合业务短信发送方式
     */
    private String smsSendTypeFU;

    public SmsIO() {
        inModeCode = '0';
        smsChannelCode = "25";
        sendObjectCode = "1";
        sendCountCode = "1";
        sendTimeCode = "0";
        recvObjectType = "00";
        smsTypeCode = "OA";
        smsKindCode = "03";
        noticeContentType = '0';
        referedCount = "0";
        forceReferCount = "0";
        smsPriority = "0";
        smsSendTypeFU = "0";
        dealState = '0';
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getSmsNoticeId() {
        return smsNoticeId;
    }

    public void setSmsNoticeId(String smsNoticeId) {
        this.smsNoticeId = smsNoticeId;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public char getInModeCode() {
        return inModeCode;
    }

    public void setInModeCode(char inModeCode) {
        this.inModeCode = inModeCode;
    }

    public String getSmsChannelCode() {
        return smsChannelCode;
    }

    public void setSmsChannelCode(String smsChannelCode) {
        this.smsChannelCode = smsChannelCode;
    }

    public String getSendObjectCode() {
        return sendObjectCode;
    }

    public void setSendObjectCode(String sendObjectCode) {
        this.sendObjectCode = sendObjectCode;
    }

    public String getSendTimeCode() {
        return sendTimeCode;
    }

    public void setSendTimeCode(String sendTimeCode) {
        this.sendTimeCode = sendTimeCode;
    }

    public String getSendCountCode() {
        return sendCountCode;
    }

    public void setSendCountCode(String sendCountCode) {
        this.sendCountCode = sendCountCode;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmsTypeCode() {
        return smsTypeCode;
    }

    public void setSmsTypeCode(String smsTypeCode) {
        this.smsTypeCode = smsTypeCode;
    }

    public String getSmsKindCode() {
        return smsKindCode;
    }

    public void setSmsKindCode(String smsKindCode) {
        this.smsKindCode = smsKindCode;
    }

    public char getNoticeContentType() {
        return noticeContentType;
    }

    public void setNoticeContentType(char noticeContentType) {
        this.noticeContentType = noticeContentType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getReferedCount() {
        return referedCount;
    }

    public void setReferedCount(String referedCount) {
        this.referedCount = referedCount;
    }

    public String getForceReferCount() {
        return forceReferCount;
    }

    public void setForceReferCount(String forceReferCount) {
        this.forceReferCount = forceReferCount;
    }

    public String getForceObject() {
        return forceObject;
    }

    public void setForceObject(String forceObject) {
        this.forceObject = forceObject;
    }

    public String getForceStartTime() {
        return forceStartTime;
    }

    public void setForceStartTime(String forceStartTime) {
        this.forceStartTime = forceStartTime;
    }

    public String getForceEndTime() {
        return forceEndTime;
    }

    public void setForceEndTime(String forceEndTime) {
        this.forceEndTime = forceEndTime;
    }

    public String getSmsPriority() {
        return smsPriority;
    }

    public void setSmsPriority(String smsPriority) {
        this.smsPriority = smsPriority;
    }

    public String getReferTime() {
        return referTime;
    }

    public void setReferTime(String referTime) {
        this.referTime = referTime;
    }

    public String getReferStaffId() {
        return referStaffId;
    }

    public void setReferStaffId(String referStaffId) {
        this.referStaffId = referStaffId;
    }

    public String getReferDepartId() {
        return referDepartId;
    }

    public void setReferDepartId(String referDepartId) {
        this.referDepartId = referDepartId;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealStaffId() {
        return dealStaffId;
    }

    public void setDealStaffId(String dealStaffId) {
        this.dealStaffId = dealStaffId;
    }

    public String getDealDepartId() {
        return dealDepartId;
    }

    public void setDealDepartId(String dealDepartId) {
        this.dealDepartId = dealDepartId;
    }

    public char getDealState() {
        return dealState;
    }

    public void setDealState(char dealState) {
        this.dealState = dealState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRevc1() {
        return revc1;
    }

    public void setRevc1(String revc1) {
        this.revc1 = revc1;
    }

    public String getRevc2() {
        return revc2;
    }

    public void setRevc2(String revc2) {
        this.revc2 = revc2;
    }

    public String getRevc3() {
        return revc3;
    }

    public void setRevc3(String revc3) {
        this.revc3 = revc3;
    }

    public String getRevc4() {
        return revc4;
    }

    public void setRevc4(String revc4) {
        this.revc4 = revc4;
    }

    public String getSmsSendType() {
        return smsSendType;
    }

    public void setSmsSendType(String smsSendType) {
        this.smsSendType = smsSendType;
    }

    public String getMainSN() {
        return mainSN;
    }

    public void setMainSN(String mainSN) {
        this.mainSN = mainSN;
    }

    public String getSmsSendTypeFU() {
        return smsSendTypeFU;
    }

    public void setSmsSendTypeFU(String smsSendTypeFU) {
        this.smsSendTypeFU = smsSendTypeFU;
    }

    @Override
    public SmsIO clone() {
        try {
            return (SmsIO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "SmsIO clone error");
        }
    }

    @Override
    public String toString() {
        return "SmsIO{" +
                "acctId='" + acctId + '\'' +
                ", smsNoticeId='" + smsNoticeId + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", inModeCode=" + inModeCode +
                ", smsChannelCode='" + smsChannelCode + '\'' +
                ", sendObjectCode='" + sendObjectCode + '\'' +
                ", sendTimeCode='" + sendTimeCode + '\'' +
                ", sendCountCode='" + sendCountCode + '\'' +
                ", recvObjectType='" + recvObjectType + '\'' +
                ", recvObject='" + recvObject + '\'' +
                ", id='" + id + '\'' +
                ", smsTypeCode='" + smsTypeCode + '\'' +
                ", smsKindCode='" + smsKindCode + '\'' +
                ", noticeContentType=" + noticeContentType +
                ", noticeContent='" + noticeContent + '\'' +
                ", referedCount='" + referedCount + '\'' +
                ", forceReferCount='" + forceReferCount + '\'' +
                ", forceObject='" + forceObject + '\'' +
                ", forceStartTime='" + forceStartTime + '\'' +
                ", forceEndTime='" + forceEndTime + '\'' +
                ", smsPriority='" + smsPriority + '\'' +
                ", referTime='" + referTime + '\'' +
                ", referStaffId='" + referStaffId + '\'' +
                ", referDepartId='" + referDepartId + '\'' +
                ", dealTime='" + dealTime + '\'' +
                ", dealStaffId='" + dealStaffId + '\'' +
                ", dealDepartId='" + dealDepartId + '\'' +
                ", dealState=" + dealState +
                ", remark='" + remark + '\'' +
                ", revc1='" + revc1 + '\'' +
                ", revc2='" + revc2 + '\'' +
                ", revc3='" + revc3 + '\'' +
                ", revc4='" + revc4 + '\'' +
                ", smsSendType='" + smsSendType + '\'' +
                ", mainSN='" + mainSN + '\'' +
                ", smsSendTypeFU='" + smsSendTypeFU + '\'' +
                '}';
    }
}
