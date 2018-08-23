package com.unicom.acting.pay.domain;

/**
 * 短信条件对象，映射TD_B_SMS_COND表主要对象
 */
public class SmsCond {
    private long smTempletId;
    private long tradeDefId;
    private long condId;
    private String smsType;
    private String provinceCode;

    public SmsCond() {
        smTempletId = 0;
        tradeDefId = 0;
        condId = 0;
        smsType = "";
        provinceCode = "";
    }

    public long getSmTempletId() {
        return smTempletId;
    }

    public void setSmTempletId(long smTempletId) {
        this.smTempletId = smTempletId;
    }

    public long getTradeDefId() {
        return tradeDefId;
    }

    public void setTradeDefId(long tradeDefId) {
        this.tradeDefId = tradeDefId;
    }

    public long getCondId() {
        return condId;
    }

    public void setCondId(long condId) {
        this.condId = condId;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "SmsCond{" +
                "smTempletId=" + smTempletId +
                ", tradeDefId=" + tradeDefId +
                ", condId=" + condId +
                ", smsType='" + smsType + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
