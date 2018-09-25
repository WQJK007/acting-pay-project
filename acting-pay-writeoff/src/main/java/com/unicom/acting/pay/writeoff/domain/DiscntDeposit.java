package com.unicom.acting.pay.writeoff.domain;


import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;

/**
 * 活动实例表，主要映射TF_B_DISCNT_DEPOSIT表字段
 *
 * @author ducj
 */
public class DiscntDeposit implements Cloneable {
    private String actionEventId;
    private String actionRuleId;
    private String chargeId;
    private String eparchyCode;
    private String acctBalanceId;
    private int actionCode;
    private int condId;
    private String outerTradeId;
    private String acctId;
    private String userId;
    private int partitionId;
    private char discntType;
    private int discntItemId;
    private char discntMode;
    private long money;
    private long leftMoney;
    private int months;
    private char limitMode;
    private String splitMethod;
    private long limitMoney;
    private char delayTag;
    private int startCycleId;
    private int endCycleId;
    private char timerType;
    private int itemGrpId;
    private int desDepositCode;
    private int perSmsId;
    private int lastSmsId;
    private int fireDay;
    private int firedMonth;
    private char ifTrans;
    private String relActionEventId;
    private String recvTime;
    private String recvEparchyCode;
    private String recvCityCode;
    private String recvDepartId;
    private String recvStaffId;
    private char canUseTag;
    private String rsrvInfo;
    private long rsrvFee;
    private char cancelTag;
    private int versionNo;
    private long sumConsume;
    private String resultInfo;
    private char canPreTrans;
    private String netTypeCode;
    private int depositCode;
    private String discountCode;
    private String rsrvDate1;
    private String activeTime;
    private String cancelStaffId;
    private String cancelDepartId;
    private String cancelCityCode;
    private String cancelEparchyCode;
    private String cancelTime;
    private String nextDate;
    private int pDepositCode;
    private char ifStopTrans;
    private long consumeThreshold;
    private String batchId;
    private String transFormula;
    private String execDate;
    private int transMonths;
    private String recvUserId;
    private String provinceCode;
    private long productMoney;
    private int productStartCycle;
    private int productEndCycle;

    public String getActionEventId() {
        return actionEventId;
    }

    public void setActionEventId(String actionEventId) {
        this.actionEventId = actionEventId;
    }

    public String getActionRuleId() {
        return actionRuleId;
    }

    public void setActionRuleId(String actionRuleId) {
        this.actionRuleId = actionRuleId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public int getCondId() {
        return condId;
    }

    public void setCondId(int condId) {
        this.condId = condId;
    }

    public String getOuterTradeId() {
        return outerTradeId;
    }

    public void setOuterTradeId(String outerTradeId) {
        this.outerTradeId = outerTradeId;
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

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public char getDiscntType() {
        return discntType;
    }

    public void setDiscntType(char discntType) {
        this.discntType = discntType;
    }

    public int getDiscntItemId() {
        return discntItemId;
    }

    public void setDiscntItemId(int discntItemId) {
        this.discntItemId = discntItemId;
    }

    public char getDiscntMode() {
        return discntMode;
    }

    public void setDiscntMode(char discntMode) {
        this.discntMode = discntMode;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getLeftMoney() {
        return leftMoney;
    }

    public void setLeftMoney(long leftMoney) {
        this.leftMoney = leftMoney;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public char getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(char limitMode) {
        this.limitMode = limitMode;
    }

    public String getSplitMethod() {
        return splitMethod;
    }

    public void setSplitMethod(String splitMethod) {
        this.splitMethod = splitMethod;
    }

    public long getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(long limitMoney) {
        this.limitMoney = limitMoney;
    }

    public char getDelayTag() {
        return delayTag;
    }

    public void setDelayTag(char delayTag) {
        this.delayTag = delayTag;
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

    public char getTimerType() {
        return timerType;
    }

    public void setTimerType(char timerType) {
        this.timerType = timerType;
    }

    public int getItemGrpId() {
        return itemGrpId;
    }

    public void setItemGrpId(int itemGrpId) {
        this.itemGrpId = itemGrpId;
    }

    public int getDesDepositCode() {
        return desDepositCode;
    }

    public void setDesDepositCode(int desDepositCode) {
        this.desDepositCode = desDepositCode;
    }

    public int getPerSmsId() {
        return perSmsId;
    }

    public void setPerSmsId(int perSmsId) {
        this.perSmsId = perSmsId;
    }

    public int getLastSmsId() {
        return lastSmsId;
    }

    public void setLastSmsId(int lastSmsId) {
        this.lastSmsId = lastSmsId;
    }

    public int getFireDay() {
        return fireDay;
    }

    public void setFireDay(int fireDay) {
        this.fireDay = fireDay;
    }

    public int getFiredMonth() {
        return firedMonth;
    }

    public void setFiredMonth(int firedMonth) {
        this.firedMonth = firedMonth;
    }

    public char getIfTrans() {
        return ifTrans;
    }

    public void setIfTrans(char ifTrans) {
        this.ifTrans = ifTrans;
    }

    public String getRelActionEventId() {
        return relActionEventId;
    }

    public void setRelActionEventId(String relActionEventId) {
        this.relActionEventId = relActionEventId;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }

    public String getRecvEparchyCode() {
        return recvEparchyCode;
    }

    public void setRecvEparchyCode(String recvEparchyCode) {
        this.recvEparchyCode = recvEparchyCode;
    }

    public String getRecvCityCode() {
        return recvCityCode;
    }

    public void setRecvCityCode(String recvCityCode) {
        this.recvCityCode = recvCityCode;
    }

    public String getRecvDepartId() {
        return recvDepartId;
    }

    public void setRecvDepartId(String recvDepartId) {
        this.recvDepartId = recvDepartId;
    }

    public String getRecvStaffId() {
        return recvStaffId;
    }

    public void setRecvStaffId(String recvStaffId) {
        this.recvStaffId = recvStaffId;
    }

    public char getCanUseTag() {
        return canUseTag;
    }

    public void setCanUseTag(char canUseTag) {
        this.canUseTag = canUseTag;
    }

    public String getRsrvInfo() {
        return rsrvInfo;
    }

    public void setRsrvInfo(String rsrvInfo) {
        this.rsrvInfo = rsrvInfo;
    }

    public long getRsrvFee() {
        return rsrvFee;
    }

    public void setRsrvFee(long rsrvFee) {
        this.rsrvFee = rsrvFee;
    }

    public char getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(char cancelTag) {
        this.cancelTag = cancelTag;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public long getSumConsume() {
        return sumConsume;
    }

    public void setSumConsume(long sumConsume) {
        this.sumConsume = sumConsume;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public char getCanPreTrans() {
        return canPreTrans;
    }

    public void setCanPreTrans(char canPreTrans) {
        this.canPreTrans = canPreTrans;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getRsrvDate1() {
        return rsrvDate1;
    }

    public void setRsrvDate1(String rsrvDate1) {
        this.rsrvDate1 = rsrvDate1;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getCancelStaffId() {
        return cancelStaffId;
    }

    public void setCancelStaffId(String cancelStaffId) {
        this.cancelStaffId = cancelStaffId;
    }

    public String getCancelDepartId() {
        return cancelDepartId;
    }

    public void setCancelDepartId(String cancelDepartId) {
        this.cancelDepartId = cancelDepartId;
    }

    public String getCancelCityCode() {
        return cancelCityCode;
    }

    public void setCancelCityCode(String cancelCityCode) {
        this.cancelCityCode = cancelCityCode;
    }

    public String getCancelEparchyCode() {
        return cancelEparchyCode;
    }

    public void setCancelEparchyCode(String cancelEparchyCode) {
        this.cancelEparchyCode = cancelEparchyCode;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public int getpDepositCode() {
        return pDepositCode;
    }

    public void setpDepositCode(int pDepositCode) {
        this.pDepositCode = pDepositCode;
    }

    public char getIfStopTrans() {
        return ifStopTrans;
    }

    public void setIfStopTrans(char ifStopTrans) {
        this.ifStopTrans = ifStopTrans;
    }

    public long getConsumeThreshold() {
        return consumeThreshold;
    }

    public void setConsumeThreshold(long consumeThreshold) {
        this.consumeThreshold = consumeThreshold;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getTransFormula() {
        return transFormula;
    }

    public void setTransFormula(String transFormula) {
        this.transFormula = transFormula;
    }

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    public int getTransMonths() {
        return transMonths;
    }

    public void setTransMonths(int transMonths) {
        this.transMonths = transMonths;
    }

    public String getRecvUserId() {
        return recvUserId;
    }

    public void setRecvUserId(String recvUserId) {
        this.recvUserId = recvUserId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public long getProductMoney() {
        return productMoney;
    }

    public void setProductMoney(long productMoney) {
        this.productMoney = productMoney;
    }

    public int getProductStartCycle() {
        return productStartCycle;
    }

    public void setProductStartCycle(int productStartCycle) {
        this.productStartCycle = productStartCycle;
    }

    public int getProductEndCycle() {
        return productEndCycle;
    }

    public void setProductEndCycle(int productEndCycle) {
        this.productEndCycle = productEndCycle;
    }

    @Override
    public DiscntDeposit clone() {
        try {
            return (DiscntDeposit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "AccountDeposit clone error");
        }
    }

    @Override
    public String toString() {
        return "DiscntDeposit{" +
                "actionEventId='" + actionEventId + '\'' +
                ", actionRuleId='" + actionRuleId + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", acctBalanceId='" + acctBalanceId + '\'' +
                ", actionCode=" + actionCode +
                ", condId=" + condId +
                ", outerTradeId='" + outerTradeId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", partitionId=" + partitionId +
                ", discntType=" + discntType +
                ", discntItemId=" + discntItemId +
                ", discntMode=" + discntMode +
                ", money=" + money +
                ", leftMoney=" + leftMoney +
                ", months=" + months +
                ", limitMode=" + limitMode +
                ", splitMethod='" + splitMethod + '\'' +
                ", limitMoney=" + limitMoney +
                ", delayTag=" + delayTag +
                ", startCycleId=" + startCycleId +
                ", endCycleId=" + endCycleId +
                ", timerType=" + timerType +
                ", itemGrpId=" + itemGrpId +
                ", desDepositCode=" + desDepositCode +
                ", perSmsId=" + perSmsId +
                ", lastSmsId=" + lastSmsId +
                ", fireDay=" + fireDay +
                ", firedMonth=" + firedMonth +
                ", ifTrans=" + ifTrans +
                ", relActionEventId='" + relActionEventId + '\'' +
                ", recvTime='" + recvTime + '\'' +
                ", recvEparchyCode='" + recvEparchyCode + '\'' +
                ", recvCityCode='" + recvCityCode + '\'' +
                ", recvDepartId='" + recvDepartId + '\'' +
                ", recvStaffId='" + recvStaffId + '\'' +
                ", canUseTag=" + canUseTag +
                ", rsrvInfo='" + rsrvInfo + '\'' +
                ", rsrvFee=" + rsrvFee +
                ", cancelTag=" + cancelTag +
                ", versionNo=" + versionNo +
                ", sumConsume=" + sumConsume +
                ", resultInfo='" + resultInfo + '\'' +
                ", canPreTrans=" + canPreTrans +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", depositCode=" + depositCode +
                ", discountCode='" + discountCode + '\'' +
                ", rsrvDate1='" + rsrvDate1 + '\'' +
                ", activeTime='" + activeTime + '\'' +
                ", cancelStaffId='" + cancelStaffId + '\'' +
                ", cancelDepartId='" + cancelDepartId + '\'' +
                ", cancelCityCode='" + cancelCityCode + '\'' +
                ", cancelEparchyCode='" + cancelEparchyCode + '\'' +
                ", cancelTime='" + cancelTime + '\'' +
                ", nextDate='" + nextDate + '\'' +
                ", pDepositCode=" + pDepositCode +
                ", ifStopTrans=" + ifStopTrans +
                ", consumeThreshold=" + consumeThreshold +
                ", batchId='" + batchId + '\'' +
                ", transFormula='" + transFormula + '\'' +
                ", execDate='" + execDate + '\'' +
                ", transMonths=" + transMonths +
                ", recvUserId='" + recvUserId + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", productMoney=" + productMoney +
                ", productStartCycle=" + productStartCycle +
                ", productEndCycle=" + productEndCycle +
                '}';
    }
}