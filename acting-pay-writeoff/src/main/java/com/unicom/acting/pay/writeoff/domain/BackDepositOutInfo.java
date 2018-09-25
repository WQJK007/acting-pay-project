package com.unicom.acting.pay.writeoff.domain;

/**
 * 清退账本余额信息
 *
 * @author Wangkh
 */
public class BackDepositOutInfo {
    /**
     * @see #acctBalanceId 账本实例标识
     */
    private String acctBalanceId;
    /**
     * @see #depositCode 账本科目编码
     */
    private String depositCode;
    /**
     * @see #depositName 账本科目名称
     */
    private String depositName;
    /**
     * @see #canUseValue 账本剩余可用金额
     */
    private String canUseValue;
    /**
     * @see #userId 用户标识
     */
    private String userId;
    /**
     * @see #serialNumber 服务号码
     */
    private String serialNumber;
    /**
     * @see #netTypeCode 用户网别
     */
    private String netTypeCode;

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public String getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(String depositCode) {
        this.depositCode = depositCode;
    }

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public String getCanUseValue() {
        return canUseValue;
    }

    public void setCanUseValue(String canUseValue) {
        this.canUseValue = canUseValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    @Override
    public String toString() {
        return "BackDepositOutInfo{" +
                "acctBalanceId='" + acctBalanceId + '\'' +
                ", depositCode='" + depositCode + '\'' +
                ", depositName='" + depositName + '\'' +
                ", canUseValue='" + canUseValue + '\'' +
                ", userId='" + userId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                '}';
    }
}
