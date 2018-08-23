package com.unicom.acting.pay.domain;

/**
 * 存取款日志对象，主要更新TF_B_ACCESSLOG表记录使用
 *
 * @author Wangkh
 */
public class AccessLog {
    private String accessId;
    private String chargeId;
    private String acctId;
    private String acctBalanceId;
    private char depositTypeCode;
    private int depositCode;
    private long oldBalance;
    private long money;
    private long newBalance;
    private long invoiceFee;
    private String operateTime;
    private String eparchyCode;
    private char cancelTag;
    private char accessTag;
    private char curTag;
    private String provinceCode;

    public AccessLog() {
        depositTypeCode = ' ';
        depositCode = -1;
        oldBalance = 0;
        money = 0;
        newBalance = 0;
        invoiceFee = 0;
        cancelTag = '0';
        accessTag = '0';
        curTag = '0';
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public char getDepositTypeCode() {
        return depositTypeCode;
    }

    public void setDepositTypeCode(char depositTypeCode) {
        this.depositTypeCode = depositTypeCode;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public long getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(long oldBalance) {
        this.oldBalance = oldBalance;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(long newBalance) {
        this.newBalance = newBalance;
    }

    public long getInvoiceFee() {
        return invoiceFee;
    }

    public void setInvoiceFee(long invoiceFee) {
        this.invoiceFee = invoiceFee;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public char getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(char cancelTag) {
        this.cancelTag = cancelTag;
    }

    public char getAccessTag() {
        return accessTag;
    }

    public void setAccessTag(char accessTag) {
        this.accessTag = accessTag;
    }

    public char getCurTag() {
        return curTag;
    }

    public void setCurTag(char curTag) {
        this.curTag = curTag;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "AccessLog{" +
                "accessId='" + accessId + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", acctBalanceId='" + acctBalanceId + '\'' +
                ", depositTypeCode=" + depositTypeCode +
                ", depositCode=" + depositCode +
                ", oldBalance=" + oldBalance +
                ", money=" + money +
                ", newBalance=" + newBalance +
                ", invoiceFee=" + invoiceFee +
                ", operateTime='" + operateTime + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", cancelTag=" + cancelTag +
                ", accessTag=" + accessTag +
                ", curTag=" + curTag +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
