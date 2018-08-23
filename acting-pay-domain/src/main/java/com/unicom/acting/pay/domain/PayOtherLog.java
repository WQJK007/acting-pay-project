package com.unicom.acting.pay.domain;

/**
 * 收费其他信息日志表对象，映射TF_B_PAYOTHER_LOG表主要字段
 *
 * @author Wangkh
 */
public class PayOtherLog {
    private String chargeId;
    private String carrierId;
    private String carrierCode;
    private String carrierTime;
    private String eparchyCode;
    private String carrierVest;
    private String carrierUseName;
    private String carrierUsePhone;
    private String carrierUsePassId;
    private char carrierStatus;
    private long confTimeLimit;
    private String bankAcctNo;
    private String bankCode;
    private String bankName;
    private char cancelTag;
    private String rsrvInfo1;
    private String rsrvInfo2;
    private long rsrvFee1;
    private long rsrvFee2;
    private String provinceCode;

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierTime() {
        return carrierTime;
    }

    public void setCarrierTime(String carrierTime) {
        this.carrierTime = carrierTime;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getCarrierVest() {
        return carrierVest;
    }

    public void setCarrierVest(String carrierVest) {
        this.carrierVest = carrierVest;
    }

    public String getCarrierUseName() {
        return carrierUseName;
    }

    public void setCarrierUseName(String carrierUseName) {
        this.carrierUseName = carrierUseName;
    }

    public String getCarrierUsePhone() {
        return carrierUsePhone;
    }

    public void setCarrierUsePhone(String carrierUsePhone) {
        this.carrierUsePhone = carrierUsePhone;
    }

    public String getCarrierUsePassId() {
        return carrierUsePassId;
    }

    public void setCarrierUsePassId(String carrierUsePassId) {
        this.carrierUsePassId = carrierUsePassId;
    }

    public char getCarrierStatus() {
        return carrierStatus;
    }

    public void setCarrierStatus(char carrierStatus) {
        this.carrierStatus = carrierStatus;
    }

    public long getConfTimeLimit() {
        return confTimeLimit;
    }

    public void setConfTimeLimit(long confTimeLimit) {
        this.confTimeLimit = confTimeLimit;
    }

    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public char getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(char cancelTag) {
        this.cancelTag = cancelTag;
    }

    public String getRsrvInfo1() {
        return rsrvInfo1;
    }

    public void setRsrvInfo1(String rsrvInfo1) {
        this.rsrvInfo1 = rsrvInfo1;
    }

    public String getRsrvInfo2() {
        return rsrvInfo2;
    }

    public void setRsrvInfo2(String rsrvInfo2) {
        this.rsrvInfo2 = rsrvInfo2;
    }

    public long getRsrvFee1() {
        return rsrvFee1;
    }

    public void setRsrvFee1(long rsrvFee1) {
        this.rsrvFee1 = rsrvFee1;
    }

    public long getRsrvFee2() {
        return rsrvFee2;
    }

    public void setRsrvFee2(long rsrvFee2) {
        this.rsrvFee2 = rsrvFee2;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "PayOtherLog{" +
                "chargeId='" + chargeId + '\'' +
                ", carrierId='" + carrierId + '\'' +
                ", carrierCode='" + carrierCode + '\'' +
                ", carrierTime='" + carrierTime + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", carrierVest='" + carrierVest + '\'' +
                ", carrierUseName='" + carrierUseName + '\'' +
                ", carrierUsePhone='" + carrierUsePhone + '\'' +
                ", carrierUsePassId='" + carrierUsePassId + '\'' +
                ", carrierStatus=" + carrierStatus +
                ", confTimeLimit=" + confTimeLimit +
                ", bankAcctNo='" + bankAcctNo + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cancelTag=" + cancelTag +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", rsrvInfo2='" + rsrvInfo2 + '\'' +
                ", rsrvFee1=" + rsrvFee1 +
                ", rsrvFee2=" + rsrvFee2 +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
