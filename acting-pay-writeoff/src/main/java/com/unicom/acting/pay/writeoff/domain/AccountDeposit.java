package com.unicom.acting.pay.writeoff.domain;


import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;

/**
 * 账户余额，主要映射TF_F_ACCOUNTDEPOSIT表字段
 *
 * @author ducj
 */
public class AccountDeposit implements Cloneable {
    private String acctBalanceId;
    private String acctId;
    private String userId;

    private String startDate;
    private String endDate;
    /**
     * @see #validTag
     * 账本有效标志
     * 0有效 1无效
     */
    private char validTag;
    /**
     * @see #privateTag
     * 余额共享标志
     * 0公有 1私有
     */
    private char privateTag;
    private String updateTime;
    private String rsrvInfo2;
    private String rsrvInfo1;
    private String provinceCode;
    private String eparchyCode;
    private String chargeId;
    private String backupInfo;
    private String rollBackInfo;

    private int depositCode;
    private long depositMoney;
    private long initMoney;
    private long money;
    /**
     * @see #limitMode
     * 限定方式
     * 0:无限定
     * 1:限额
     * 2:话费比例限定(LIMIT_MONEY对应的比例)
     */
    private char limitMode;
    private long limitMoney;
    /**
     * @see #limitLeft
     * 限额结存
     */
    private long limitLeft;
    /**
     * @see #invoiceFee
     * 帐户发票金额
     */
    private long invoiceFee;
    /**
     * @see #printFee
     * 打印金额
     */
    private long printFee;
    /**
     * @see #startCycleId
     * 可销帐单开始帐期
     */
    private int startCycleId;
    private int endCycleId;
    private long oweFee;
    /**
     * @see #freezeFee
     * 冻结金额
     */
    private long freezeFee;
    private long drecvFee1;
    private long drecvFee2;
    /**
     * @see #versionNo
     * 更新版本号
     */
    private int versionNo;
    /**
     * @see #actionCode
     * 活动编码根据不同的帐本类型
     */
    private int actionCode;
    /**
     * @see #openCycleId
     * 开帐帐期
     */
    private int openCycleId;
    private long rsrvFee2;
    private long rsrvFee1;
    /**
     * @see #itemPriorRuleId
     * 帐本优先规则id
     */
    private int itemPriorRuleId;
    /**
     * @see #depositTypeCode
     * 帐本类型的类型
     */
    private char depositTypeCode;
    /**
     * @see #cashTag
     * 是否现金流帐本
     */
    private char cashTag;
    /**
     * @see #cashType
     * 按现金非现金流比例销账标志
     */
    private char cashType;
    /**
     * @see #ifBalance
     * 是否计入结余
     */
    private char ifBalance;
    /**
     * @see #ifCalcOwe
     * 是否冲抵往月欠费
     */
    private char ifCalcOwe;
    /**
     * @see #creditMode
     * 信控方式
     */
    private char creditMode;
    /**
     * @see #newFlag
     * 是否新增
     */
    private char newFlag;
    /**
     * @see #virtualTag
     * 是否虚拟帐本（多个帐本按比例销帐需要虚拟帐本)
     */
    private char virtualTag;
    /**
     * @see #ifInAccesslog
     * 是否生成存取款日志记录
     */
    private char ifInAccesslog;
    /**
     * @see #impFee
     * 本次使用原预存金额(主要用来区分即有预存，又有欠费的情况)
     */
    private long impFee;
    /**
     * @see #recvFee
     * 本次收费金额
     */
    private long recvFee;
    /**
     * @see #useRecvFee
     * 本次使用收费金额(主要用来区分即有预存，又有欠费的情况)
     */
    private long useRecvFee;
    /**
     * @see #leftCanUse
     * 剩余的可使用金额
     */
    private long leftCanUse;
    /**
     * @see #impRealFee
     * 冲抵实时话费(包括原预存，现预存)
     */
    private long impRealFee;
    /**
     * @see #realFeeRecv
     * 本次缴费冲抵的实时话费
     */
    private long realFeeRecv;
    /**
     * @see #priority
     * 帐本类型优先级
     */
    private int priority;
    /**
     * @see #depositTypePriority
     * 帐本科目类型优先级
     */
    private int depositTypePriority;
    /**
     * @see #relPriority
     * 关联账本优先级
     */
    private int relPriority;
    /**
     * @see #drecvTimes
     * 抵扣次数
     */
    private int drecvTimes;
    /**
     * @see #writeOffOweFee
     * ture 该账本销过往月帐
     * false 该账本没有销往月帐
     */
    private boolean writeOffOweFee;

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

    public char getValidTag() {
        return validTag;
    }

    public void setValidTag(char validTag) {
        this.validTag = validTag;
    }

    public char getPrivateTag() {
        return privateTag;
    }

    public void setPrivateTag(char privateTag) {
        this.privateTag = privateTag;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRsrvInfo2() {
        return rsrvInfo2;
    }

    public void setRsrvInfo2(String rsrvInfo2) {
        this.rsrvInfo2 = rsrvInfo2;
    }

    public String getRsrvInfo1() {
        return rsrvInfo1;
    }

    public void setRsrvInfo1(String rsrvInfo1) {
        this.rsrvInfo1 = rsrvInfo1;
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

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
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

    public long getOweFee() {
        return oweFee;
    }

    public void setOweFee(long oweFee) {
        this.oweFee = oweFee;
    }

    public long getFreezeFee() {
        return freezeFee;
    }

    public void setFreezeFee(long freezeFee) {
        this.freezeFee = freezeFee;
    }

    public long getDrecvFee1() {
        return drecvFee1;
    }

    public void setDrecvFee1(long drecvFee1) {
        this.drecvFee1 = drecvFee1;
    }

    public long getDrecvFee2() {
        return drecvFee2;
    }

    public void setDrecvFee2(long drecvFee2) {
        this.drecvFee2 = drecvFee2;
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

    public long getRsrvFee2() {
        return rsrvFee2;
    }

    public void setRsrvFee2(long rsrvFee2) {
        this.rsrvFee2 = rsrvFee2;
    }

    public long getRsrvFee1() {
        return rsrvFee1;
    }

    public void setRsrvFee1(long rsrvFee1) {
        this.rsrvFee1 = rsrvFee1;
    }

    public int getItemPriorRuleId() {
        return itemPriorRuleId;
    }

    public void setItemPriorRuleId(int itemPriorRuleId) {
        this.itemPriorRuleId = itemPriorRuleId;
    }

    public char getDepositTypeCode() {
        return depositTypeCode;
    }

    public void setDepositTypeCode(char depositTypeCode) {
        this.depositTypeCode = depositTypeCode;
    }

    public char getCashTag() {
        return cashTag;
    }

    public void setCashTag(char cashTag) {
        this.cashTag = cashTag;
    }

    public char getCashType() {
        return cashType;
    }

    public void setCashType(char cashType) {
        this.cashType = cashType;
    }

    public char getIfBalance() {
        return ifBalance;
    }

    public void setIfBalance(char ifBalance) {
        this.ifBalance = ifBalance;
    }

    public char getIfCalcOwe() {
        return ifCalcOwe;
    }

    public void setIfCalcOwe(char ifCalcOwe) {
        this.ifCalcOwe = ifCalcOwe;
    }

    public char getCreditMode() {
        return creditMode;
    }

    public void setCreditMode(char creditMode) {
        this.creditMode = creditMode;
    }

    public char getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(char newFlag) {
        this.newFlag = newFlag;
    }

    public char getVirtualTag() {
        return virtualTag;
    }

    public void setVirtualTag(char virtualTag) {
        this.virtualTag = virtualTag;
    }

    public char getIfInAccesslog() {
        return ifInAccesslog;
    }

    public void setIfInAccesslog(char ifInAccesslog) {
        this.ifInAccesslog = ifInAccesslog;
    }

    public long getImpFee() {
        return impFee;
    }

    public void setImpFee(long impFee) {
        this.impFee = impFee;
    }

    public long getRecvFee() {
        return recvFee;
    }

    public void setRecvFee(long recvFee) {
        this.recvFee = recvFee;
    }

    public long getUseRecvFee() {
        return useRecvFee;
    }

    public void setUseRecvFee(long useRecvFee) {
        this.useRecvFee = useRecvFee;
    }

    public long getLeftCanUse() {
        return leftCanUse;
    }

    public void setLeftCanUse(long leftCanUse) {
        this.leftCanUse = leftCanUse;
    }

    public long getImpRealFee() {
        return impRealFee;
    }

    public void setImpRealFee(long impRealFee) {
        this.impRealFee = impRealFee;
    }

    public long getRealFeeRecv() {
        return realFeeRecv;
    }

    public void setRealFeeRecv(long realFeeRecv) {
        this.realFeeRecv = realFeeRecv;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDepositTypePriority() {
        return depositTypePriority;
    }

    public void setDepositTypePriority(int depositTypePriority) {
        this.depositTypePriority = depositTypePriority;
    }

    public int getRelPriority() {
        return relPriority;
    }

    public void setRelPriority(int relPriority) {
        this.relPriority = relPriority;
    }

    public int getDrecvTimes() {
        return drecvTimes;
    }

    public void setDrecvTimes(int drecvTimes) {
        this.drecvTimes = drecvTimes;
    }

    public boolean isWriteOffOweFee() {
        return writeOffOweFee;
    }

    public void setWriteOffOweFee(boolean writeOffOweFee) {
        this.writeOffOweFee = writeOffOweFee;
    }

    public AccountDeposit() {
        limitMode = '0';
        validTag = '0';
        privateTag = '0';
        depositCode = 0;
        depositMoney = 0;
        initMoney = 0;
        money = 0;
        limitMoney = 0;
        limitLeft = 0;
        invoiceFee = 0;
        printFee = 0;
        startCycleId = 0;
        endCycleId = 0;
        oweFee = 0;
        freezeFee = 0;
        drecvFee1 = 0;
        drecvFee2 = 0;
        versionNo = 0;
        actionCode = 0;
        openCycleId = 0;
        rsrvFee2 = 0;
        rsrvFee1 = 0;
        itemPriorRuleId = 0;
        depositTypeCode = '0';
        cashTag = '0';
        cashType = '0';
        ifBalance = '0';
        ifCalcOwe = '0';
        creditMode = '0';
        newFlag = '0';
        virtualTag = '0';
        ifInAccesslog = '0';
        impFee = 0;
        recvFee = 0;
        useRecvFee = 0;
        leftCanUse = 0;
        impRealFee = 0;
        realFeeRecv = 0;
        priority = 0;
        drecvTimes = 0;
        depositTypePriority = 0;
        relPriority = 0;
    }

    public void init() {
        limitMode = '0';
        validTag = '0';
        privateTag = '0';
        depositCode = 0;
        depositMoney = 0;
        initMoney = 0;
        money = 0;
        limitMoney = 0;
        limitLeft = 0;
        invoiceFee = 0;
        printFee = 0;
        startCycleId = 0;
        endCycleId = 0;
        oweFee = 0;
        freezeFee = 0;
        drecvFee1 = 0;
        drecvFee2 = 0;
        versionNo = 0;
        actionCode = 0;
        openCycleId = 0;
        rsrvFee2 = 0;
        rsrvFee1 = 0;
        itemPriorRuleId = 0;
        depositTypeCode = '0';
        cashTag = '0';
        cashType = '0';
        ifBalance = '0';
        ifCalcOwe = '0';
        creditMode = '0';
        newFlag = '0';
        virtualTag = '0';
        ifInAccesslog = '0';
        impFee = 0;
        recvFee = 0;
        useRecvFee = 0;
        leftCanUse = 0;
        impRealFee = 0;
        realFeeRecv = 0;
        priority = 0;
        drecvTimes = 0;
        depositTypePriority = 0;
        relPriority = 0;
    }

    @Override
    public AccountDeposit clone() {
        try {
            return (AccountDeposit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "AccountDeposit clone error");
        }
    }

    @Override
    public String toString() {
        return "AccountDeposit{" +
                "acctBalanceId='" + acctBalanceId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", validTag=" + validTag +
                ", privateTag=" + privateTag +
                ", updateTime='" + updateTime + '\'' +
                ", rsrvInfo2='" + rsrvInfo2 + '\'' +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", backupInfo='" + backupInfo + '\'' +
                ", rollBackInfo='" + rollBackInfo + '\'' +
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
                ", oweFee=" + oweFee +
                ", freezeFee=" + freezeFee +
                ", drecvFee1=" + drecvFee1 +
                ", drecvFee2=" + drecvFee2 +
                ", versionNo=" + versionNo +
                ", actionCode=" + actionCode +
                ", openCycleId=" + openCycleId +
                ", rsrvFee2=" + rsrvFee2 +
                ", rsrvFee1=" + rsrvFee1 +
                ", itemPriorRuleId=" + itemPriorRuleId +
                ", depositTypeCode=" + depositTypeCode +
                ", cashTag=" + cashTag +
                ", cashType=" + cashType +
                ", ifBalance=" + ifBalance +
                ", ifCalcOwe=" + ifCalcOwe +
                ", creditMode=" + creditMode +
                ", newFlag=" + newFlag +
                ", virtualTag=" + virtualTag +
                ", ifInAccesslog=" + ifInAccesslog +
                ", impFee=" + impFee +
                ", recvFee=" + recvFee +
                ", useRecvFee=" + useRecvFee +
                ", leftCanUse=" + leftCanUse +
                ", impRealFee=" + impRealFee +
                ", realFeeRecv=" + realFeeRecv +
                ", priority=" + priority +
                ", depositTypePriority=" + depositTypePriority +
                ", relPriority=" + relPriority +
                ", drecvTimes=" + drecvTimes +
                ", writeOffOweFee=" + writeOffOweFee +
                '}';
    }
}
