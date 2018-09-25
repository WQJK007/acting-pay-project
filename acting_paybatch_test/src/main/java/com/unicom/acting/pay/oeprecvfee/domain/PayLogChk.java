package com.unicom.acting.pay.oeprecvfee.domain;

public class PayLogChk {
    private String  tradeId;
    private int  tradeTypeCode;
    private String  batchId;
    private int  priority;
    private String  chargeId;
    private String  acctId;
    private String  userId;
    private String  serialNumber;
    private String  writeoffMode;
    private String  channelId;
    private int  paymentId;
    private int  payFeeModeCode;
    private int  paymentOp;
    private String  recvFee;
    private String  recvTime;
    private String  tradeEparchyCode;
    private String  tradeCityCode;
    private String  tradeDepartId;
    private String  tradeStaffId;
    private int  paymentReasonCode;
    private String  actTag;
    private String  actionCode;
    private String  remark;
    private String  outerTradeId;
    private String  inputNo;
    private int  inputMode;
    private String  cancelTag;
    private String  dealTag;
    private String  dealTime;
    private String  resultCode;
    private String  resultInfo;
    private int  startCycleId;
    private int  endCycleId;
    private String  startDate;
    private String  endDate;
    private String  limitMoney;
    private String  allboweFee;
    private String  allroweFee;
    private String  allNewBalance;
    private String  spayFee;
    private String  rsrvFee1;
    private String  rsrvFee2;
    private String  rsrvInfo1;
    private int  months;
    private char  extendTag;
    private String actionEventId;
    private String acctBalanceId;
    private int  depositCode;
    private char  privateTag;
    private String    limitMode;
    private String acctId2;
    private String userId2;
    private int  depositCode2;
    private String relChargeId;
    private String tradeTime;
    private char  recoverTag;
    private  String provinceCode;

    public void init()
    {
        tradeId = "";
        tradeTypeCode = 0;
        batchId = "";
        priority = 0;
        chargeId = "";
        acctId = "";
        userId = "";
        serialNumber = "";
        writeoffMode = "1";
        channelId = "";
        paymentId = 0;
        payFeeModeCode = 0;
        paymentOp = 0;
        recvFee = "0";
        tradeEparchyCode = "";
        tradeCityCode = "";
        tradeDepartId = "";
        tradeStaffId = "";
        paymentReasonCode = 0;
        //actTag = '';
        actionCode = "0";
        acctBalanceId = "";
        remark = "";
        outerTradeId = "";
        inputNo = "";
        inputMode = 0;
        //cancelTag = '';
        dealTag = "0";
        dealTime = "";
        resultCode = "0";
        resultInfo = "";
        limitMoney = "99999999999";
        extendTag = '0';
        privateTag = '0';
        limitMode = "0";
        recoverTag = '0';
        provinceCode = "";
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public int getTradeTypeCode() {
        return tradeTypeCode;
    }

    public void setTradeTypeCode(int tradeTypeCode) {
        this.tradeTypeCode = tradeTypeCode;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getWriteoffMode() {
        return writeoffMode;
    }

    public void setWriteoffMode(String writeoffMode) {
        this.writeoffMode = writeoffMode;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPayFeeModeCode() {
        return payFeeModeCode;
    }

    public void setPayFeeModeCode(int payFeeModeCode) {
        this.payFeeModeCode = payFeeModeCode;
    }

    public int getPaymentOp() {
        return paymentOp;
    }

    public void setPaymentOp(int paymentOp) {
        this.paymentOp = paymentOp;
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

    public String getTradeEparchyCode() {
        return tradeEparchyCode;
    }

    public void setTradeEparchyCode(String tradeEparchyCode) {
        this.tradeEparchyCode = tradeEparchyCode;
    }

    public String getTradeCityCode() {
        return tradeCityCode;
    }

    public void setTradeCityCode(String tradeCityCode) {
        this.tradeCityCode = tradeCityCode;
    }

    public String getTradeDepartId() {
        return tradeDepartId;
    }

    public void setTradeDepartId(String tradeDepartId) {
        this.tradeDepartId = tradeDepartId;
    }

    public String getTradeStaffId() {
        return tradeStaffId;
    }

    public void setTradeStaffId(String tradeStaffId) {
        this.tradeStaffId = tradeStaffId;
    }

    public int getPaymentReasonCode() {
        return paymentReasonCode;
    }

    public void setPaymentReasonCode(int paymentReasonCode) {
        this.paymentReasonCode = paymentReasonCode;
    }

    public String getActTag() {
        return actTag;
    }

    public void setActTag(String actTag) {
        this.actTag = actTag;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOuterTradeId() {
        return outerTradeId;
    }

    public void setOuterTradeId(String outerTradeId) {
        this.outerTradeId = outerTradeId;
    }

    public String getInputNo() {
        return inputNo;
    }

    public void setInputNo(String inputNo) {
        this.inputNo = inputNo;
    }

    public int getInputMode() {
        return inputMode;
    }

    public void setInputMode(int inputMode) {
        this.inputMode = inputMode;
    }

    public String getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(String cancelTag) {
        this.cancelTag = cancelTag;
    }

    public String getDealTag() {
        return dealTag;
    }

    public void setDealTag(String dealTag) {
        this.dealTag = dealTag;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
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

    public String getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(String limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getAllboweFee() {
        return allboweFee;
    }

    public void setAllboweFee(String allboweFee) {
        this.allboweFee = allboweFee;
    }

    public String getAllroweFee() {
        return allroweFee;
    }

    public void setAllroweFee(String allroweFee) {
        this.allroweFee = allroweFee;
    }

    public String getAllNewBalance() {
        return allNewBalance;
    }

    public void setAllNewBalance(String allNewBalance) {
        this.allNewBalance = allNewBalance;
    }

    public String getSpayFee() {
        return spayFee;
    }

    public void setSpayFee(String spayFee) {
        this.spayFee = spayFee;
    }

    public String getRsrvFee1() {
        return rsrvFee1;
    }

    public void setRsrvFee1(String rsrvFee1) {
        this.rsrvFee1 = rsrvFee1;
    }

    public String getRsrvFee2() {
        return rsrvFee2;
    }

    public void setRsrvFee2(String rsrvFee2) {
        this.rsrvFee2 = rsrvFee2;
    }

    public String getRsrvInfo1() {
        return rsrvInfo1;
    }

    public void setRsrvInfo1(String rsrvInfo1) {
        this.rsrvInfo1 = rsrvInfo1;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public char getExtendTag() {
        return extendTag;
    }

    public void setExtendTag(char extendTag) {
        this.extendTag = extendTag;
    }

    public String getActionEventId() {
        return actionEventId;
    }

    public void setActionEventId(String actionEventId) {
        this.actionEventId = actionEventId;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public int getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(int depositCode) {
        this.depositCode = depositCode;
    }

    public char getPrivateTag() {
        return privateTag;
    }

    public void setPrivateTag(char privateTag) {
        this.privateTag = privateTag;
    }

    public String getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(String limitMode) {
        this.limitMode = limitMode;
    }

    public String getAcctId2() {
        return acctId2;
    }

    public void setAcctId2(String acctId2) {
        this.acctId2 = acctId2;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public int getDepositCode2() {
        return depositCode2;
    }

    public void setDepositCode2(int depositCode2) {
        this.depositCode2 = depositCode2;
    }

    public String getRelChargeId() {
        return relChargeId;
    }

    public void setRelChargeId(String relChargeId) {
        this.relChargeId = relChargeId;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public char getRecoverTag() {
        return recoverTag;
    }

    public void setRecoverTag(char recoverTag) {
        this.recoverTag = recoverTag;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
