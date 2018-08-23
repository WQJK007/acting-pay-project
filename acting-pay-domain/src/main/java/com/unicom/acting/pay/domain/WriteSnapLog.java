package com.unicom.acting.pay.domain;

/**
 * 销账快照对象，主要用于新增更新TF_B_WRITESNAP_LOG表记录
 *
 * @author Wangkh
 */
public class WriteSnapLog {
    private String chargeId;
    private String acctId;
    private long spayFee;
    private long allMoney;
    private long allNewMoney;
    private long allBalance;
    private long allNewBalance;
    private long allBOweFee;
    private long aImpFee;
    private long allNewBOweFee;
    private long preRealFee;
    private long curRealFee;
    private long protocolBalance;
    private long oldRoundFee;
    private long newRoundFee;
    private String operateTime;
    private String eparchyCode;
    private int cycleId;
    private String remark;
    private long rsrvFee1;
    private long rsrvFee2;
    private String rsrvInfo1;
    private char cancelTag;
    private char recoverTag;
    private char writeoffMode;
    private String provinceCode;
    private long currentAvlFee;

    public WriteSnapLog() {
        writeoffMode = '1';
        spayFee = 0;
        allMoney = 0;
        allNewMoney = 0;
        allBalance = 0;
        allNewBalance = 0;
        allBOweFee = 0;
        aImpFee = 0;
        allNewBOweFee = 0;
        preRealFee = 0;
        curRealFee = 0;
        protocolBalance = 0;
        oldRoundFee = 0;
        newRoundFee = 0;
        recoverTag = '1';
        cycleId = -1;
        cancelTag = '0';
        rsrvFee1 = 0;
        rsrvFee2 = 0;
        currentAvlFee = 0;
    }

    public void init() {
        writeoffMode = '1';
        spayFee = 0;
        allMoney = 0;
        allNewMoney = 0;
        allBalance = 0;
        allNewBalance = 0;
        allBOweFee = 0;
        aImpFee = 0;
        allNewBOweFee = 0;
        preRealFee = 0;
        curRealFee = 0;
        protocolBalance = 0;
        oldRoundFee = 0;
        newRoundFee = 0;
        recoverTag = '1';
        cycleId = -1;
        cancelTag = '0';
        rsrvFee1 = 0;
        rsrvFee2 = 0;
        currentAvlFee = 0;
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

    public long getSpayFee() {
        return spayFee;
    }

    public void setSpayFee(long spayFee) {
        this.spayFee = spayFee;
    }

    public long getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(long allMoney) {
        this.allMoney = allMoney;
    }

    public long getAllNewMoney() {
        return allNewMoney;
    }

    public void setAllNewMoney(long allNewMoney) {
        this.allNewMoney = allNewMoney;
    }

    public long getAllBalance() {
        return allBalance;
    }

    public void setAllBalance(long allBalance) {
        this.allBalance = allBalance;
    }

    public long getAllNewBalance() {
        return allNewBalance;
    }

    public void setAllNewBalance(long allNewBalance) {
        this.allNewBalance = allNewBalance;
    }

    public long getAllBOweFee() {
        return allBOweFee;
    }

    public void setAllBOweFee(long allBOweFee) {
        this.allBOweFee = allBOweFee;
    }

    public long getaImpFee() {
        return aImpFee;
    }

    public void setaImpFee(long aImpFee) {
        this.aImpFee = aImpFee;
    }

    public long getAllNewBOweFee() {
        return allNewBOweFee;
    }

    public void setAllNewBOweFee(long allNewBOweFee) {
        this.allNewBOweFee = allNewBOweFee;
    }

    public long getPreRealFee() {
        return preRealFee;
    }

    public void setPreRealFee(long preRealFee) {
        this.preRealFee = preRealFee;
    }

    public long getCurRealFee() {
        return curRealFee;
    }

    public void setCurRealFee(long curRealFee) {
        this.curRealFee = curRealFee;
    }

    public long getProtocolBalance() {
        return protocolBalance;
    }

    public void setProtocolBalance(long protocolBalance) {
        this.protocolBalance = protocolBalance;
    }

    public long getOldRoundFee() {
        return oldRoundFee;
    }

    public void setOldRoundFee(long oldRoundFee) {
        this.oldRoundFee = oldRoundFee;
    }

    public long getNewRoundFee() {
        return newRoundFee;
    }

    public void setNewRoundFee(long newRoundFee) {
        this.newRoundFee = newRoundFee;
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

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public char getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(char cancelTag) {
        this.cancelTag = cancelTag;
    }

    public char getRecoverTag() {
        return recoverTag;
    }

    public void setRecoverTag(char recoverTag) {
        this.recoverTag = recoverTag;
    }

    public char getWriteoffMode() {
        return writeoffMode;
    }

    public void setWriteoffMode(char writeoffMode) {
        this.writeoffMode = writeoffMode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public long getCurrentAvlFee() {
        return currentAvlFee;
    }

    public void setCurrentAvlFee(long currentAvlFee) {
        this.currentAvlFee = currentAvlFee;
    }

    @Override
    public String toString() {
        return "WriteSnapLogDao{" +
                "chargeId='" + chargeId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", spayFee=" + spayFee +
                ", allMoney=" + allMoney +
                ", allNewMoney=" + allNewMoney +
                ", allBalance=" + allBalance +
                ", allNewBalance=" + allNewBalance +
                ", allBOweFee=" + allBOweFee +
                ", aImpFee=" + aImpFee +
                ", allNewBOweFee=" + allNewBOweFee +
                ", preRealFee=" + preRealFee +
                ", curRealFee=" + curRealFee +
                ", protocolBalance=" + protocolBalance +
                ", oldRoundFee=" + oldRoundFee +
                ", newRoundFee=" + newRoundFee +
                ", operateTime='" + operateTime + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", cycleId=" + cycleId +
                ", remark='" + remark + '\'' +
                ", rsrvFee1=" + rsrvFee1 +
                ", rsrvFee2=" + rsrvFee2 +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", cancelTag=" + cancelTag +
                ", recoverTag=" + recoverTag +
                ", writeoffMode=" + writeoffMode +
                ", provinceCode='" + provinceCode + '\'' +
                ", currentAvlFee=" + currentAvlFee +
                '}';
    }
}
