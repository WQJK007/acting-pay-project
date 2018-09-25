package com.unicom.acting.pay.writeoff.domain;

/**
 * 余额转账公共输出参数
 *
 * @author Wangkh
 */
public class TransFeeCommInfoOut {
    /**
     * @see #serialNumber 交易服务号码
     */
    private String serialNumber;

    /**
     * @see #netTypeCode 网别
     */
    private String netTypeCode;
    /**
     * @see #userId 交易用户标识
     */
    private String userId;
    /**
     * @see #acctId 交易账户标识
     */
    private String acctId;
    /**
     * @see #payName 交易账户名称
     */
    private String payName;
    /**
     * @see #payModeCode 账户付费方式
     */
    private String payModeCode;
    /**
     * @see #eparchyCode 账户归属地市
     */
    private String eparchyCode;
    /**
     * @see #brandCode 用户品牌编码
     */
    private String brandCode;
    /**
     * @see #curCycleId 当前账期
     */
    private int curCycleId;
    /**
     * @see #maxAcctCycleId  当前最大开账账期
     */
    private int maxAcctCycleId;
    /**
     * @see #chargeId 交易流水
     */
    private String chargeId;
    /**
     * @see #relChargeId 关联流水
     */
    private String relChargeId;
    /**
     * @see #recvFee 交易金额
     */
    private String recvFee;
    /**
     * @see #recvTime 交易时间
     */
    private String recvTime;
    /**
     * @see #paymentId 储值方式
     */
    private String paymentId;
    /**
     * @see #acctBalaceId 本次最后操作的账本实例
     */
    private String acctBalaceId;
    /**
     * @see #spayFee 交易后应缴金额
     */
    private String spayFee;
    /**
     * @see #allMoney 原预存款
     */
    private String allMoney;
    /**
     * @see #allNewMoney 现预存款
     */
    private String allNewMoney;
    /**
     * @see #allBalance 原实时结余
     */
    private String allBalance;
    /**
     * @see #allNewBalance 现实时结余
     */
    private String allNewBalance;
    /**
     * @see #aimpFee 原预存款销账金额
     */
    private String aimpFee;
    /**
     * @see #allBOweFee 原欠费金额
     */
    private String allBOweFee;
    /**
     * @see #allBImpFee 往月欠费销账金额
     */
    private String allBImpFee;
    /**
     * @see #allRImpFee 原预存款销往月欠费金额
     */
    private String allRImpFee;
    /**
     * @see #allNewBOweFee 现欠费金额
     */
    private String allNewBOweFee;
    /**
     * @see #preRealFee 上个账期实时费用
     */
    private String preRealFee;
    /**
     * @see #curRealFee 当前账期实时费用
     */
    private String curRealFee;
    /**
     * @see #allROweFee 实时费用
     */
    private String allROweFee;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayModeCode() {
        return payModeCode;
    }

    public void setPayModeCode(String payModeCode) {
        this.payModeCode = payModeCode;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public int getCurCycleId() {
        return curCycleId;
    }

    public void setCurCycleId(int curCycleId) {
        this.curCycleId = curCycleId;
    }

    public int getMaxAcctCycleId() {
        return maxAcctCycleId;
    }

    public void setMaxAcctCycleId(int maxAcctCycleId) {
        this.maxAcctCycleId = maxAcctCycleId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getRelChargeId() {
        return relChargeId;
    }

    public void setRelChargeId(String relChargeId) {
        this.relChargeId = relChargeId;
    }

    public String getRecvFee() {
        return recvFee;
    }

    public void setRecvFee(String recvFee) {
        this.recvFee = recvFee;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAcctBalaceId() {
        return acctBalaceId;
    }

    public void setAcctBalaceId(String acctBalaceId) {
        this.acctBalaceId = acctBalaceId;
    }

    public String getSpayFee() {
        return spayFee;
    }

    public void setSpayFee(String spayFee) {
        this.spayFee = spayFee;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    public String getAllNewMoney() {
        return allNewMoney;
    }

    public void setAllNewMoney(String allNewMoney) {
        this.allNewMoney = allNewMoney;
    }

    public String getAllBalance() {
        return allBalance;
    }

    public void setAllBalance(String allBalance) {
        this.allBalance = allBalance;
    }

    public String getAllNewBalance() {
        return allNewBalance;
    }

    public void setAllNewBalance(String allNewBalance) {
        this.allNewBalance = allNewBalance;
    }

    public String getAimpFee() {
        return aimpFee;
    }

    public void setAimpFee(String aimpFee) {
        this.aimpFee = aimpFee;
    }

    public String getAllBOweFee() {
        return allBOweFee;
    }

    public void setAllBOweFee(String allBOweFee) {
        this.allBOweFee = allBOweFee;
    }

    public String getAllBImpFee() {
        return allBImpFee;
    }

    public void setAllBImpFee(String allBImpFee) {
        this.allBImpFee = allBImpFee;
    }

    public String getAllRImpFee() {
        return allRImpFee;
    }

    public void setAllRImpFee(String allRImpFee) {
        this.allRImpFee = allRImpFee;
    }

    public String getAllNewBOweFee() {
        return allNewBOweFee;
    }

    public void setAllNewBOweFee(String allNewBOweFee) {
        this.allNewBOweFee = allNewBOweFee;
    }

    public String getPreRealFee() {
        return preRealFee;
    }

    public void setPreRealFee(String preRealFee) {
        this.preRealFee = preRealFee;
    }

    public String getCurRealFee() {
        return curRealFee;
    }

    public void setCurRealFee(String curRealFee) {
        this.curRealFee = curRealFee;
    }

    public String getAllROweFee() {
        return allROweFee;
    }

    public void setAllROweFee(String allROweFee) {
        this.allROweFee = allROweFee;
    }

    @Override
    public String toString() {
        return "TransFeeCommInfoOut{" +
                "serialNumber='" + serialNumber + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", userId='" + userId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", payName='" + payName + '\'' +
                ", payModeCode='" + payModeCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", brandCode='" + brandCode + '\'' +
                ", curCycleId=" + curCycleId +
                ", maxAcctCycleId=" + maxAcctCycleId +
                ", chargeId='" + chargeId + '\'' +
                ", relChargeId='" + relChargeId + '\'' +
                ", recvFee='" + recvFee + '\'' +
                ", recvTime='" + recvTime + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", acctBalaceId='" + acctBalaceId + '\'' +
                ", spayFee='" + spayFee + '\'' +
                ", allMoney='" + allMoney + '\'' +
                ", allNewMoney='" + allNewMoney + '\'' +
                ", allBalance='" + allBalance + '\'' +
                ", allNewBalance='" + allNewBalance + '\'' +
                ", aimpFee='" + aimpFee + '\'' +
                ", allBOweFee='" + allBOweFee + '\'' +
                ", allBImpFee='" + allBImpFee + '\'' +
                ", allRImpFee='" + allRImpFee + '\'' +
                ", allNewBOweFee='" + allNewBOweFee + '\'' +
                ", preRealFee='" + preRealFee + '\'' +
                ", curRealFee='" + curRealFee + '\'' +
                ", allROweFee='" + allROweFee + '\'' +
                '}';
    }
}
