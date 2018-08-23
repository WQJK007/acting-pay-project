package com.unicom.acting.pay.domain;

/**
 * 营业厅前厅通过现金支票，电汇，信用卡等支付方式缴费时传递的特殊信息
 */
public class CarrierInfo {
    private String carrierId;
    /**
     * 支付类型，4现金支票，0其他非现金支付方式
     */
    private int carrierCode;

    private String carrierVest;
    /**
     * 付方单位名称
     */
    private String carrierUseName;
    private String carrierUsePhone;
    private String carrierUsePassId;
    private String carrierStatus;
    private String carrierTimeLimit;
    private String bankName;
    private String bankAcct;
    /**
     * 银行编码
     */
    private String bankCode;

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public int getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(int carrierCode) {
        this.carrierCode = carrierCode;
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

    public String getCarrierStatus() {
        return carrierStatus;
    }

    public void setCarrierStatus(String carrierStatus) {
        this.carrierStatus = carrierStatus;
    }

    public String getCarrierTimeLimit() {
        return carrierTimeLimit;
    }

    public void setCarrierTimeLimit(String carrierTimeLimit) {
        this.carrierTimeLimit = carrierTimeLimit;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAcct() {
        return bankAcct;
    }

    public void setBankAcct(String bankAcct) {
        this.bankAcct = bankAcct;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public String toString() {
        return "CarrierInfo{" +
                "carrierId='" + carrierId + '\'' +
                ", carrierCode=" + carrierCode +
                ", carrierVest='" + carrierVest + '\'' +
                ", carrierUseName='" + carrierUseName + '\'' +
                ", carrierUsePhone='" + carrierUsePhone + '\'' +
                ", carrierUsePassId='" + carrierUsePassId + '\'' +
                ", carrierStatus='" + carrierStatus + '\'' +
                ", carrierTimeLimit='" + carrierTimeLimit + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAcct='" + bankAcct + '\'' +
                ", bankCode='" + bankCode + '\'' +
                '}';
    }
}
