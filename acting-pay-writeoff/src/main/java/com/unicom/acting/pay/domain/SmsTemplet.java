package com.unicom.acting.pay.domain;

/**
 * 短信模板表对象，，映射TD_B_SMS_TEMPLET表主要字段
 */
public class SmsTemplet {
    private long smTempletId;
    private String smTempletName;
    private String smTempletContext;
    private char smTempletType;
    private String smKindCode;

    public SmsTemplet() {
        smTempletId = -1;
        smTempletName = "";
        smTempletContext = "";
        smTempletType = '0';
        smKindCode = "";
    }

    public long getSmTempletId() {
        return smTempletId;
    }

    public void setSmTempletId(int smTempletId) {
        this.smTempletId = smTempletId;
    }

    public String getSmTempletName() {
        return smTempletName;
    }

    public void setSmTempletName(String smTempletName) {
        this.smTempletName = smTempletName;
    }

    public String getSmTempletContext() {
        return smTempletContext;
    }

    public void setSmTempletContext(String smTempletContext) {
        this.smTempletContext = smTempletContext;
    }

    public char getSmTempletType() {
        return smTempletType;
    }

    public void setSmTempletType(char smTempletType) {
        this.smTempletType = smTempletType;
    }

    public void setSmTempletId(long smTempletId) {
        this.smTempletId = smTempletId;
    }

    public String getSmKindCode() {
        return smKindCode;
    }

    public void setSmKindCode(String smKindCode) {
        this.smKindCode = smKindCode;
    }


    @Override
    public String toString() {
        return "SmsTemplet{" +
                "smTempletId=" + smTempletId +
                ", smTempletName='" + smTempletName + '\'' +
                ", smTempletContext='" + smTempletContext + '\'' +
                ", smTempletType=" + smTempletType +
                ", smKindCode='" + smKindCode + '\'' +
                '}';
    }
}
