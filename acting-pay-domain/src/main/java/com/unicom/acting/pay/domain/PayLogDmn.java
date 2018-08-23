package com.unicom.acting.pay.domain;

/**
 * 帐务交易后台处理工单表,用于新增修改TF_B_PAYLOG_DMN表记录
 *
 * @author Wangkh
 */
public class PayLogDmn {
    private String eparchyCode;
    private String provinceCode;
    private String tradeId;
    private long tradeTypeCode;
    private String batchId;
    private long priority;
    private String chargeId;
    private String acctId;
    private String userId;
    private String serialNumber;
    private char writeoffMode;
    private char limitMode;
    private String channelId;
    private int paymentId;
    private long paymentOp;
    private int payFeeModeCode;
    private long recvFee;
    private String outerTradeId;
    private long billStartCycleId;
    private long billEndCycleId;
    private String startDate;
    private String endDate;
    private int months;
    private long limitMoney;
    private long paymentReasonCode;
    private char extendTag;
    private int actionCode;
    private String actionEventId;
    private String acctBalanceId;
    private long depositCode;
    private char privateTag;
    private String remark;
    private String inputNo;
    private long inputMode;
    private String acctId2;
    private String userId2;
    private long depositCode2;
    private String relChargeId;
    private String tradeTime;
    private String tradeEparchyCode;
    private String tradeCityCode;
    private String tradeDepartId;
    private String tradeStaffId;
    private char cancelTag;
    private char dealTag;
    private String dealTime;
    private long resultCode;
    private String resultInfo;
    private long rsrvFee1;
    private long rsrvFee2;
    private String rsrvInfo1;
    private int partitionId;
    private char printTag;
    private char forceBack;
    private String devCode;
    private String devName;
    private char npTag;
    private char agentTag;
    private char contractTag;

    public PayLogDmn() {
        writeoffMode = '1';
        paymentId = -1;
        billStartCycleId = 198001;
        billEndCycleId = 203001;
        months = 240;
        limitMoney = 99999999999L;
        extendTag = '0';
        actionCode = -1;
        depositCode = -1;
        privateTag = '0';
        depositCode2 = -1;
        cancelTag = '0';
        dealTag = '0';
        limitMode = '0';
        printTag = '0';
        forceBack = '0';
        resultCode = 0;
        priority = 0;
        devCode = "";
        devName = "";
        npTag = '0';
        agentTag = '0';
        contractTag = '0';
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public long getTradeTypeCode() {
        return tradeTypeCode;
    }

    public void setTradeTypeCode(long tradeTypeCode) {
        this.tradeTypeCode = tradeTypeCode;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
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

    public char getWriteoffMode() {
        return writeoffMode;
    }

    public void setWriteoffMode(char writeoffMode) {
        this.writeoffMode = writeoffMode;
    }

    public char getLimitMode() {
        return limitMode;
    }

    public void setLimitMode(char limitMode) {
        this.limitMode = limitMode;
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

    public long getPaymentOp() {
        return paymentOp;
    }

    public void setPaymentOp(long paymentOp) {
        this.paymentOp = paymentOp;
    }

    public int getPayFeeModeCode() {
        return payFeeModeCode;
    }

    public void setPayFeeModeCode(int payFeeModeCode) {
        this.payFeeModeCode = payFeeModeCode;
    }

    public long getRecvFee() {
        return recvFee;
    }

    public void setRecvFee(long recvFee) {
        this.recvFee = recvFee;
    }

    public String getOuterTradeId() {
        return outerTradeId;
    }

    public void setOuterTradeId(String outerTradeId) {
        this.outerTradeId = outerTradeId;
    }

    public long getBillStartCycleId() {
        return billStartCycleId;
    }

    public void setBillStartCycleId(long billStartCycleId) {
        this.billStartCycleId = billStartCycleId;
    }

    public long getBillEndCycleId() {
        return billEndCycleId;
    }

    public void setBillEndCycleId(long billEndCycleId) {
        this.billEndCycleId = billEndCycleId;
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

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public long getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(long limitMoney) {
        this.limitMoney = limitMoney;
    }

    public long getPaymentReasonCode() {
        return paymentReasonCode;
    }

    public void setPaymentReasonCode(long paymentReasonCode) {
        this.paymentReasonCode = paymentReasonCode;
    }

    public char getExtendTag() {
        return extendTag;
    }

    public void setExtendTag(char extendTag) {
        this.extendTag = extendTag;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
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

    public long getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(long depositCode) {
        this.depositCode = depositCode;
    }

    public char getPrivateTag() {
        return privateTag;
    }

    public void setPrivateTag(char privateTag) {
        this.privateTag = privateTag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInputNo() {
        return inputNo;
    }

    public void setInputNo(String inputNo) {
        this.inputNo = inputNo;
    }

    public long getInputMode() {
        return inputMode;
    }

    public void setInputMode(long inputMode) {
        this.inputMode = inputMode;
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

    public long getDepositCode2() {
        return depositCode2;
    }

    public void setDepositCode2(long depositCode2) {
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

    public char getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(char cancelTag) {
        this.cancelTag = cancelTag;
    }

    public char getDealTag() {
        return dealTag;
    }

    public void setDealTag(char dealTag) {
        this.dealTag = dealTag;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public long getResultCode() {
        return resultCode;
    }

    public void setResultCode(long resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
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

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public char getPrintTag() {
        return printTag;
    }

    public void setPrintTag(char printTag) {
        this.printTag = printTag;
    }

    public char getForceBack() {
        return forceBack;
    }

    public void setForceBack(char forceBack) {
        this.forceBack = forceBack;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public char getNpTag() {
        return npTag;
    }

    public void setNpTag(char npTag) {
        this.npTag = npTag;
    }

    public char getAgentTag() {
        return agentTag;
    }

    public void setAgentTag(char agentTag) {
        this.agentTag = agentTag;
    }

    public char getContractTag() {
        return contractTag;
    }

    public void setContractTag(char contractTag) {
        this.contractTag = contractTag;
    }

    @Override
    public String toString() {
        return "PayLogDmn{" +
                "eparchyCode='" + eparchyCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", tradeTypeCode=" + tradeTypeCode +
                ", batchId='" + batchId + '\'' +
                ", priority=" + priority +
                ", chargeId='" + chargeId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", writeoffMode=" + writeoffMode +
                ", limitMode=" + limitMode +
                ", channelId='" + channelId + '\'' +
                ", paymentId=" + paymentId +
                ", paymentOp=" + paymentOp +
                ", payFeeModeCode=" + payFeeModeCode +
                ", recvFee=" + recvFee +
                ", outerTradeId='" + outerTradeId + '\'' +
                ", billStartCycleId=" + billStartCycleId +
                ", billEndCycleId=" + billEndCycleId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", months=" + months +
                ", limitMoney=" + limitMoney +
                ", paymentReasonCode=" + paymentReasonCode +
                ", extendTag=" + extendTag +
                ", actionCode=" + actionCode +
                ", actionEventId='" + actionEventId + '\'' +
                ", acctBalanceId='" + acctBalanceId + '\'' +
                ", depositCode=" + depositCode +
                ", privateTag=" + privateTag +
                ", remark='" + remark + '\'' +
                ", inputNo='" + inputNo + '\'' +
                ", inputMode=" + inputMode +
                ", acctId2='" + acctId2 + '\'' +
                ", userId2='" + userId2 + '\'' +
                ", depositCode2=" + depositCode2 +
                ", relChargeId='" + relChargeId + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", tradeEparchyCode='" + tradeEparchyCode + '\'' +
                ", tradeCityCode='" + tradeCityCode + '\'' +
                ", tradeDepartId='" + tradeDepartId + '\'' +
                ", tradeStaffId='" + tradeStaffId + '\'' +
                ", cancelTag=" + cancelTag +
                ", dealTag=" + dealTag +
                ", dealTime='" + dealTime + '\'' +
                ", resultCode=" + resultCode +
                ", resultInfo='" + resultInfo + '\'' +
                ", rsrvFee1=" + rsrvFee1 +
                ", rsrvFee2=" + rsrvFee2 +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", partitionId=" + partitionId +
                ", printTag=" + printTag +
                ", forceBack=" + forceBack +
                ", devCode='" + devCode + '\'' +
                ", devName='" + devName + '\'' +
                ", npTag=" + npTag +
                ", agentTag=" + agentTag +
                ", contractTag=" + contractTag +
                '}';
    }
}
