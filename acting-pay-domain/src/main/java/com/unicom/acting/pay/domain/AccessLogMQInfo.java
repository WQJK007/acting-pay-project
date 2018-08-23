package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 存取款日志MQ同步对象信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessLogMQInfo {
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
        return "AccessLogMQInfo{" +
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
