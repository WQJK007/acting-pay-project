package com.unicom.acting.pay.domain;

import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;

/**
 * 账单对象，主要用于更新TS_B_BILL表数据
 *
 * @author Wangkh
 */
public class Bill implements Cloneable {
    private String provinceCode;
    private String eparchyCode;
    private String netTypeCode;
    private String serialNumber;
    private String billId;
    private String acctId;
    private String userId;
    private int cycleId;
    private int integrateItemCode;
    private long fee;
    private long balance;
    private long printFee;
    private long bDiscnt;
    private long aDiscnt;
    private long adjustBefore;
    private long adjustAfter;
    private long lateFee;
    private long lateBalance;
    private String lateCalDate;
    private char canpayTag;
    /**
     * @see #payTag
     * 0：未销帐
     * 1：已销帐
     * 3：托收退单（可以继续托，也可现金交,未销帐）
     * 4：托收退单（必须现金交,未销帐）
     * 5：托收退单后现金销帐（已销帐)
     * 7: 托收在途(未销帐)
     * 8: 发票预打(类似托收在途)
     * 9: 托收回单(已销帐)
     */
    private char payTag;
    private char billPayTag;
    private String updateTime;
    private String updateDepartId;
    private String updateStaffId;
    private String chargeId;
    private long writeoffFee1;
    private long writeoffFee2;
    private long writeoffFee3;
    private long rsrvFee1;
    private long rsrvFee2;
    private long rsrvFee3;
    private int versionNo;
    private String rsrvInfo1;
    private String backupInfo;
    private String rollBackInfo;
    private String cityCode;

    //!!! 以下辅助标志
    /**
     * @see #noteItemCode
     * 该账目归属的发票项
     */
    private int noteItemCode;
    /**
     * @see #itemPriority
     * 账目优先
     */
    private int itemPriority;
    /**
     * @see #currWriteOffBalance
     * 本次销帐本金
     */
    private long currWriteOffBalance;
    /**
     * @see #currWriteOffLate
     * 本次销帐滞纳金
     */
    private long currWriteOffLate;
    /**
     * @see #impFee
     * 原预存款冲抵费用
     */
    private long impFee;
    /**
     * @see #genNoteTag
     * 生成票据标志
     * '0':不生成; 1:生成
     */
    private char genNoteTag;
    /**
     * @see #oldPayTag
     * 原始的付费标志
     */
    private char oldPayTag;
    /**
     * @see #negativeTag
     * 负账单标识
     * '0'表示负账单(没有对应正帐单),'1'表示负账单有对应的正帐单,'2'表示正帐单(默认)
     */
    private char negativeTag;
    /**
     * @see #negativeUser
     * 用户账期总欠费是负数的用户
     * 0 不是负欠费用户 1 负欠费用户
     */
    private char negativeUser;
    /**
     * @see #newLateFee
     * 本次新产生的滞纳金
     */
    private long newLateFee;
    /**
     * @see #derateFee
     * 本次减免滞纳金
     */
    private long derateFee;
    /**
     * @see #preDerateLateFee
     * 预减免滞纳金
     *
     */
    private long preDerateLateFee;
    /**
     * @see #prepayTag
     * 预付费标识
     */
    private char prepayTag;
    private int dealTag;
    /**
     * @see #newPayTag
     * 销账后账目项标识
     */
    private String newPayTag;
    /**
     * @see #newBillPayTag
     * 销账后账单标识
     */
    private String newBillPayTag;

    public Bill() {
        billId = "-1";
        cycleId = 0;
        integrateItemCode = 0;
        fee = 0;
        balance = 0;
        printFee = 0;
        bDiscnt = 0;
        aDiscnt = 0;
        adjustBefore = 0;
        adjustAfter = 0;
        lateFee = 0;
        lateBalance = 0;
        canpayTag = '0';
        payTag = '0';
        billPayTag = '0';
        versionNo = 0;
        writeoffFee1 = 0;
        writeoffFee2 = 0;
        writeoffFee3 = 0;
        rsrvFee1 = 0;
        rsrvFee2 = 0;
        rsrvFee3 = 0;
        itemPriority = 0;
        currWriteOffBalance = 0;
        currWriteOffLate = 0;
        lateCalDate = "";
        updateTime = "";
        impFee = 0;
        genNoteTag = '0';
        oldPayTag = '0';
        newLateFee = 0;
        derateFee = 0;
        preDerateLateFee = 0;
        negativeTag = '2';
        negativeUser = '1';
        prepayTag = '0';
        dealTag = 0;
        newPayTag = "0";
        newBillPayTag = "0";
    }

    //放在服务类中
    public void init() {
        cycleId = 0;
        integrateItemCode = 0;
        fee = 0;
        balance = 0;
        printFee = 0;
        bDiscnt = 0;
        aDiscnt = 0;
        adjustBefore = 0;
        adjustAfter = 0;
        lateFee = 0;
        lateBalance = 0;
        canpayTag = '0';
        payTag = '0';
        billPayTag = '0';
        versionNo = 0;
        writeoffFee1 = 0;
        writeoffFee2 = 0;
        writeoffFee3 = 0;
        rsrvFee1 = 0;
        rsrvFee2 = 0;
        rsrvFee3 = 0;
        itemPriority = 0;
        currWriteOffBalance = 0;
        currWriteOffLate = 0;
        lateCalDate = null;
        updateTime = null;
        impFee = 0;
        genNoteTag = '0';
        oldPayTag = '0';
        newLateFee = 0;
        derateFee = 0;
        preDerateLateFee = 0;
        negativeTag = '2';
        negativeUser = '1';
        prepayTag = '0';
        dealTag = 0;
        newPayTag = "0";
        newBillPayTag = "0";
    }


    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
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

    public int getCycleId() {
        return cycleId;
    }

    public void setCycleId(int cycleId) {
        this.cycleId = cycleId;
    }

    public int getIntegrateItemCode() {
        return integrateItemCode;
    }

    public void setIntegrateItemCode(int integrateItemCode) {
        this.integrateItemCode = integrateItemCode;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getPrintFee() {
        return printFee;
    }

    public void setPrintFee(long printFee) {
        this.printFee = printFee;
    }

    public long getbDiscnt() {
        return bDiscnt;
    }

    public void setbDiscnt(long bDiscnt) {
        this.bDiscnt = bDiscnt;
    }

    public long getaDiscnt() {
        return aDiscnt;
    }

    public void setaDiscnt(long aDiscnt) {
        this.aDiscnt = aDiscnt;
    }

    public long getAdjustBefore() {
        return adjustBefore;
    }

    public void setAdjustBefore(long adjustBefore) {
        this.adjustBefore = adjustBefore;
    }

    public long getAdjustAfter() {
        return adjustAfter;
    }

    public void setAdjustAfter(long adjustAfter) {
        this.adjustAfter = adjustAfter;
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

    public String getLateCalDate() {
        return lateCalDate;
    }

    public void setLateCalDate(String lateCalDate) {
        this.lateCalDate = lateCalDate;
    }

    public char getCanpayTag() {
        return canpayTag;
    }

    public void setCanpayTag(char canpayTag) {
        this.canpayTag = canpayTag;
    }

    public char getPayTag() {
        return payTag;
    }

    public void setPayTag(char payTag) {
        this.payTag = payTag;
    }

    public char getBillPayTag() {
        return billPayTag;
    }

    public void setBillPayTag(char billPayTag) {
        this.billPayTag = billPayTag;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateDepartId() {
        return updateDepartId;
    }

    public void setUpdateDepartId(String updateDepartId) {
        this.updateDepartId = updateDepartId;
    }

    public String getUpdateStaffId() {
        return updateStaffId;
    }

    public void setUpdateStaffId(String updateStaffId) {
        this.updateStaffId = updateStaffId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public long getWriteoffFee1() {
        return writeoffFee1;
    }

    public void setWriteoffFee1(long writeoffFee1) {
        this.writeoffFee1 = writeoffFee1;
    }

    public long getWriteoffFee2() {
        return writeoffFee2;
    }

    public void setWriteoffFee2(long writeoffFee2) {
        this.writeoffFee2 = writeoffFee2;
    }

    public long getWriteoffFee3() {
        return writeoffFee3;
    }

    public void setWriteoffFee3(long writeoffFee3) {
        this.writeoffFee3 = writeoffFee3;
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

    public long getRsrvFee3() {
        return rsrvFee3;
    }

    public void setRsrvFee3(long rsrvFee3) {
        this.rsrvFee3 = rsrvFee3;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getRsrvInfo1() {
        return rsrvInfo1;
    }

    public void setRsrvInfo1(String rsrvInfo1) {
        this.rsrvInfo1 = rsrvInfo1;
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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getNoteItemCode() {
        return noteItemCode;
    }

    public void setNoteItemCode(int noteItemCode) {
        this.noteItemCode = noteItemCode;
    }

    public int getItemPriority() {
        return itemPriority;
    }

    public void setItemPriority(int itemPriority) {
        this.itemPriority = itemPriority;
    }

    public long getCurrWriteOffBalance() {
        return currWriteOffBalance;
    }

    public void setCurrWriteOffBalance(long currWriteOffBalance) {
        this.currWriteOffBalance = currWriteOffBalance;
    }

    public long getCurrWriteOffLate() {
        return currWriteOffLate;
    }

    public void setCurrWriteOffLate(long currWriteOffLate) {
        this.currWriteOffLate = currWriteOffLate;
    }

    public long getImpFee() {
        return impFee;
    }

    public void setImpFee(long impFee) {
        this.impFee = impFee;
    }

    public char getGenNoteTag() {
        return genNoteTag;
    }

    public void setGenNoteTag(char genNoteTag) {
        this.genNoteTag = genNoteTag;
    }

    public char getOldPayTag() {
        return oldPayTag;
    }

    public void setOldPayTag(char oldPayTag) {
        this.oldPayTag = oldPayTag;
    }

    public char getNegativeTag() {
        return negativeTag;
    }

    public void setNegativeTag(char negativeTag) {
        this.negativeTag = negativeTag;
    }

    public char getNegativeUser() {
        return negativeUser;
    }

    public void setNegativeUser(char negativeUser) {
        this.negativeUser = negativeUser;
    }

    public long getNewLateFee() {
        return newLateFee;
    }

    public void setNewLateFee(long newLateFee) {
        this.newLateFee = newLateFee;
    }

    public long getDerateFee() {
        return derateFee;
    }

    public void setDerateFee(long derateFee) {
        this.derateFee = derateFee;
    }

    public long getPreDerateLateFee() {
        return preDerateLateFee;
    }

    public void setPreDerateLateFee(long preDerateLateFee) {
        this.preDerateLateFee = preDerateLateFee;
    }

    public char getPrepayTag() {
        return prepayTag;
    }

    public void setPrepayTag(char prepayTag) {
        this.prepayTag = prepayTag;
    }

    public int getDealTag() {
        return dealTag;
    }

    public void setDealTag(int dealTag) {
        this.dealTag = dealTag;
    }

    public String getNewPayTag() {
        return newPayTag;
    }

    public void setNewPayTag(String newPayTag) {
        this.newPayTag = newPayTag;
    }

    public String getNewBillPayTag() {
        return newBillPayTag;
    }

    public void setNewBillPayTag(String newBillPayTag) {
        this.newBillPayTag = newBillPayTag;
    }

    @Override
    public Bill clone() {
        try {
            return (Bill) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "Bill clone error");
        }
    }

    @Override
    public String toString() {
        return "Bill{" +
                "provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", billId='" + billId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", cycleId=" + cycleId +
                ", integrateItemCode=" + integrateItemCode +
                ", fee=" + fee +
                ", balance=" + balance +
                ", printFee=" + printFee +
                ", bDiscnt=" + bDiscnt +
                ", aDiscnt=" + aDiscnt +
                ", adjustBefore=" + adjustBefore +
                ", adjustAfter=" + adjustAfter +
                ", lateFee=" + lateFee +
                ", lateBalance=" + lateBalance +
                ", lateCalDate='" + lateCalDate + '\'' +
                ", canpayTag=" + canpayTag +
                ", payTag=" + payTag +
                ", billPayTag=" + billPayTag +
                ", updateTime='" + updateTime + '\'' +
                ", updateDepartId='" + updateDepartId + '\'' +
                ", updateStaffId='" + updateStaffId + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", writeoffFee1=" + writeoffFee1 +
                ", writeoffFee2=" + writeoffFee2 +
                ", writeoffFee3=" + writeoffFee3 +
                ", rsrvFee1=" + rsrvFee1 +
                ", rsrvFee2=" + rsrvFee2 +
                ", rsrvFee3=" + rsrvFee3 +
                ", versionNo=" + versionNo +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", backupInfo='" + backupInfo + '\'' +
                ", rollBackInfo='" + rollBackInfo + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", noteItemCode=" + noteItemCode +
                ", itemPriority=" + itemPriority +
                ", currWriteOffBalance=" + currWriteOffBalance +
                ", currWriteOffLate=" + currWriteOffLate +
                ", impFee=" + impFee +
                ", genNoteTag=" + genNoteTag +
                ", oldPayTag=" + oldPayTag +
                ", negativeTag=" + negativeTag +
                ", negativeUser=" + negativeUser +
                ", newLateFee=" + newLateFee +
                ", derateFee=" + derateFee +
                ", preDerateLateFee=" + preDerateLateFee +
                ", prepayTag=" + prepayTag +
                ", dealTag=" + dealTag +
                ", newPayTag='" + newPayTag + '\'' +
                ", newBillPayTag='" + newBillPayTag + '\'' +
                '}';
    }

}
