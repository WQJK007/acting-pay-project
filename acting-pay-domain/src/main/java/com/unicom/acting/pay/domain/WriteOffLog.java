package com.unicom.acting.pay.domain;

/**
 * 销账日志对象，主要用于新增更新TF_B_WRITEOFFLOG表记录
 *
 * @author Wangkh
 */
public class WriteOffLog {
    private String writeoffId;
    private String chargeId;
    private String acctId;
    private String userId;
    private String netTypeCode;
    private String billId;
    private String acctBalanceId;
    private int integrateItemCode;
    private int depositCode;
    private int cycleId;
    private long writeoffFee;
    private long impFee;
    private long fee;
    private long oldBalance;
    private long newBalance;
    private long lateFee;
    private long lateBalance;
    private long oldLateBalance;
    private long newLateBalance;
    private long derateLateFee;
    private char oldPaytag;
    private char newPaytag;
    private char canPaytag;
    private char cancelTag;
    private String operateTime;
    private String eparchyCode;
    private String latecalDate;
    private int drecvTimes;
    private int depositLimitRuleid;
    private int depositPriorRuleid;
    private int itemPriorRuleid;
    private long newLateFee;
    private char depositTypeCode;
    private String provinceCode;
    private String areaCode;
    private String serialNumber;

    public WriteOffLog() {
        cycleId = -1;
        integrateItemCode = -1;
        depositCode = 0;
        writeoffFee = 0;
        impFee = 0;
        fee = 0;
        oldBalance = 0;
        newBalance = 0;
        lateFee = 0;
        lateBalance = 0;
        oldLateBalance = 0;
        newLateBalance = 0;
        derateLateFee = 0;
        oldPaytag = '\0';
        newPaytag = '\0';
        canPaytag = '\0';
        drecvTimes = 1;
        cancelTag = '0';
        depositLimitRuleid = -1;
        depositPriorRuleid = -1;
        itemPriorRuleid = -1;
        newLateFee = 0;
        depositTypeCode = '0';
    }

    public String getWriteoffId() {
        return writeoffId;
    }

    public void setWriteoffId(String writeoffId) {
        this.writeoffId = writeoffId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public int getIntegrateItemCode() {
        return integrateItemCode;
    }

    public void setIntegrateItemCode(int integrateItemCode) {
        this.integrateItemCode = integrateItemCode;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public long getWriteoffFee() {
        return writeoffFee;
    }

    public void setWriteoffFee(long writeoffFee) {
        this.writeoffFee = writeoffFee;
    }

    public long getImpFee() {
        return impFee;
    }

    public void setImpFee(long impFee) {
        this.impFee = impFee;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public long getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(long oldBalance) {
        this.oldBalance = oldBalance;
    }

    public long getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(long newBalance) {
        this.newBalance = newBalance;
    }

    public long getLateFee() {
        return lateFee;
    }

    public void setLateFee(long lateFee) {
        this.lateFee = lateFee;
    }

    public long getLateBalance() {
        return lateBalance;
    }

    public void setLateBalance(long lateBalance) {
        this.lateBalance = lateBalance;
    }

    public long getOldLateBalance() {
        return oldLateBalance;
    }

    public void setOldLateBalance(long oldLateBalance) {
        this.oldLateBalance = oldLateBalance;
    }

    public long getNewLateBalance() {
        return newLateBalance;
    }

    public void setNewLateBalance(long newLateBalance) {
        this.newLateBalance = newLateBalance;
    }

    public long getDerateLateFee() {
        return derateLateFee;
    }

    public void setDerateLateFee(long derateLateFee) {
        this.derateLateFee = derateLateFee;
    }

    public char getOldPaytag() {
        return oldPaytag;
    }

    public void setOldPaytag(char oldPaytag) {
        this.oldPaytag = oldPaytag;
    }

    public char getNewPaytag() {
        return newPaytag;
    }

    public void setNewPaytag(char newPaytag) {
        this.newPaytag = newPaytag;
    }

    public char getCanPaytag() {
        return canPaytag;
    }

    public void setCanPaytag(char canPaytag) {
        this.canPaytag = canPaytag;
    }

    public char getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(char cancelTag) {
        this.cancelTag = cancelTag;
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

    public String getLatecalDate() {
        return latecalDate;
    }

    public void setLatecalDate(String latecalDate) {
        this.latecalDate = latecalDate;
    }

    public int getDrecvTimes() {
        return drecvTimes;
    }

    public void setDrecvTimes(int drecvTimes) {
        this.drecvTimes = drecvTimes;
    }

    public int getDepositLimitRuleid() {
        return depositLimitRuleid;
    }

    public void setDepositLimitRuleid(int depositLimitRuleid) {
        this.depositLimitRuleid = depositLimitRuleid;
    }

    public int getDepositPriorRuleid() {
        return depositPriorRuleid;
    }

    public void setDepositPriorRuleid(int depositPriorRuleid) {
        this.depositPriorRuleid = depositPriorRuleid;
    }

    public int getItemPriorRuleid() {
        return itemPriorRuleid;
    }

    public void setItemPriorRuleid(int itemPriorRuleid) {
        this.itemPriorRuleid = itemPriorRuleid;
    }

    public long getNewLateFee() {
        return newLateFee;
    }

    public void setNewLateFee(long newLateFee) {
        this.newLateFee = newLateFee;
    }

    public char getDepositTypeCode() {
        return depositTypeCode;
    }

    public void setDepositTypeCode(char depositTypeCode) {
        this.depositTypeCode = depositTypeCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "WriteOffLog{" +
                "writeoffId='" + writeoffId + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", billId='" + billId + '\'' +
                ", acctBalanceId='" + acctBalanceId + '\'' +
                ", integrateItemCode=" + integrateItemCode +
                ", depositCode=" + depositCode +
                ", cycleId=" + cycleId +
                ", writeoffFee=" + writeoffFee +
                ", impFee=" + impFee +
                ", fee=" + fee +
                ", oldBalance=" + oldBalance +
                ", newBalance=" + newBalance +
                ", lateFee=" + lateFee +
                ", lateBalance=" + lateBalance +
                ", oldLateBalance=" + oldLateBalance +
                ", newLateBalance=" + newLateBalance +
                ", derateLateFee=" + derateLateFee +
                ", oldPaytag=" + oldPaytag +
                ", newPaytag=" + newPaytag +
                ", canPaytag=" + canPaytag +
                ", cancelTag=" + cancelTag +
                ", operateTime='" + operateTime + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", latecalDate='" + latecalDate + '\'' +
                ", drecvTimes=" + drecvTimes +
                ", depositLimitRuleid=" + depositLimitRuleid +
                ", depositPriorRuleid=" + depositPriorRuleid +
                ", itemPriorRuleid=" + itemPriorRuleid +
                ", newLateFee=" + newLateFee +
                ", depositTypeCode=" + depositTypeCode +
                ", provinceCode='" + provinceCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }
}
