package com.unicom.acting.pay.domain;

/**
 * 短信模板转换表，映射TD_B_SMS_CONVERT表主要字段
 */
public class SmsConvert {
    private long oriSmTempletId;
    private String smsType;
    private long convSmTempletId;
    private String provinceCode;

    public SmsConvert() {
        oriSmTempletId = 0;
        smsType = "";
        convSmTempletId = 0;
        provinceCode = "";
    }

    public long getOriSmTempletId() {
        return oriSmTempletId;
    }

    public void setOriSmTempletId(long oriSmTempletId) {
        this.oriSmTempletId = oriSmTempletId;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public long getConvSmTempletId() {
        return convSmTempletId;
    }

    public void setConvSmTempletId(long convSmTempletId) {
        this.convSmTempletId = convSmTempletId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "SmsConvert{" +
                "oriSmTempletId=" + oriSmTempletId +
                ", smsType='" + smsType + '\'' +
                ", convSmTempletId=" + convSmTempletId +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
