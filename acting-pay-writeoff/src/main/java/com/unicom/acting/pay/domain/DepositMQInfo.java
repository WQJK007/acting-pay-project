package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 账本MQ同步对象信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositMQInfo {
    private String acctBalanceId;
    private String acctId;
    private String userId;
    private int depositCode;
    private long depositMoney;
    private long initMoney;
    private long money;
    private char limitMode;
    private long limitMoney;
    private long limitLeft;
    private long invoiceFee;
    private long printFee;
    private int startCycleId;
    private int endCycleId;
    private String startDate;
    private String endDate;
    private long oweFee;
    private char validTag;
    private long freezeFee;
    private char privateTag;
    private String eparchyCode;
    private String backupInfo;
    private String rollBackInfo;
    private int versionNo;
    private int actionCode;
    private int openCycleId;
    private String updateTime;
    private long rsrvFee1;
    private long rsrvFee2;
    private String rsrvInfo1;
    private String rsrvInfo2;
    private String provinceCode;
    private char newFlag;

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public long getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(long depositMoney) {
        this.depositMoney = depositMoney;
    }

    public long getInitMoney() {
        return initMoney;
    }

    public void setInitMoney(long initMoney) {
        this.initMoney = initMoney;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public char getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(char limitMode) {
        this.limitMode = limitMode;
    }

    public long getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(long limitMoney) {
        this.limitMoney = limitMoney;
    }

    public long getLimitLeft() {
        return limitLeft;
    }

    public void setLimitLeft(long limitLeft) {
        this.limitLeft = limitLeft;
    }

    public long getInvoiceFee() {
        return invoiceFee;
    }

    public void setInvoiceFee(long invoiceFee) {
        this.invoiceFee = invoiceFee;
    }

    public long getPrintFee() {
        return printFee;
    }

    public void setPrintFee(long printFee) {
        this.printFee = printFee;
    }

    public int getStartCycleId() {
        return startCycleId;
    }

    public void setStartCycleId(int startCycleId) {
        this.startCycleId = startCycleId;
    }

    public int getEndCycleId() {
        return endCycleId;
    }

    public void setEndCycleId(int endCycleId) {
        this.endCycleId = endCycleId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getOweFee() {
        return oweFee;
    }

    public void setOweFee(long oweFee) {
        this.oweFee = oweFee;
    }

    public char getValidTag() {
        return validTag;
    }

    public void setValidTag(char validTag) {
        this.validTag = validTag;
    }

    public long getFreezeFee() {
        return freezeFee;
    }

    public void setFreezeFee(long freezeFee) {
        this.freezeFee = freezeFee;
    }

    public char getPrivateTag() {
        return privateTag;
    }

    public void setPrivateTag(char privateTag) {
        this.privateTag = privateTag;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getBackupInfo() {
        return backupInfo;
    }

    public void setBackupInfo(String backupInfo) {
        this.backupInfo = backupInfo;
    }

    public String getRollBackInfo() {
        return rollBackInfo;
    }

    public void setRollBackInfo(String rollBackInfo) {
        this.rollBackInfo = rollBackInfo;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public int getOpenCycleId() {
        return openCycleId;
    }

    public void setOpenCycleId(int openCycleId) {
        this.openCycleId = openCycleId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public char getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(char newFlag) {
        this.newFlag = newFlag;
    }

    @Override
    public String toString() {
        return "DepositMQInfo{" +
                "acctBalanceId='" + acctBalanceId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", depositCode=" + depositCode +
                ", depositMoney=" + depositMoney +
                ", initMoney=" + initMoney +
                ", money=" + money +
                ", limitMode=" + limitMode +
                ", limitMoney=" + limitMoney +
                ", limitLeft=" + limitLeft +
                ", invoiceFee=" + invoiceFee +
                ", printFee=" + printFee +
                ", startCycleId=" + startCycleId +
                ", endCycleId=" + endCycleId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", oweFee=" + oweFee +
                ", validTag=" + validTag +
                ", freezeFee=" + freezeFee +
                ", privateTag=" + privateTag +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", backupInfo='" + backupInfo + '\'' +
                ", rollBackInfo='" + rollBackInfo + '\'' +
                ", versionNo=" + versionNo +
                ", actionCode=" + actionCode +
                ", openCycleId=" + openCycleId +
                ", updateTime='" + updateTime + '\'' +
                ", rsrvFee1=" + rsrvFee1 +
                ", rsrvFee2=" + rsrvFee2 +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", rsrvInfo2='" + rsrvInfo2 + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", newFlag=" + newFlag +
                '}';
    }
}
