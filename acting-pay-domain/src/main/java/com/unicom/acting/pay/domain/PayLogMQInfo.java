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
public class PayLogMQInfo {
    private String chargeId;
    private String eparchyCode;
    private String cityCode;
    private String custId;
    private String userId;
    private String serialNumber;
    private String netTypeCode;
    private String acctId;
    private String channelId;
    private int paymentId;
    private int payFeeModeCode;
    private int paymentOp;
    private long recvFee;
    private long limitMoney;
    private String recvTime;
    private String recvProvinceCode;
    private String recvEparchyCode;
    private String recvCityCode;
    private String recvDepartId;
    private String recvStaffId;
    private int paymentReasonCode;
    private String inputNo;
    private int inputMode;
    private String outerTradeId;
    private int actionCode;
    private String actionEventId;
    private int paymentRuleId;
    private String remark;
    private String cancelStaffId;
    private String cancelDepartId;
    private String cancelCityCode;
    private String cancelEparchyCode;
    private String cancelTime;
    private String cancelChargeId;
    private long rsrvFee1;
    private long rsrvFee2;
    private String rsrvInfo1;
    private char cancelTag;
    private char actTag;
    private char extendTag;
    private String provinceCode;
    private String devCode;
    private String devName;
    private String npTag;
    private char agentTag;
    private char contractTag;

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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
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

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
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

    public long getRecvFee() {
        return recvFee;
    }

    public void setRecvFee(long recvFee) {
        this.recvFee = recvFee;
    }

    public long getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(long limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }

    public String getRecvProvinceCode() {
        return recvProvinceCode;
    }

    public void setRecvProvinceCode(String recvProvinceCode) {
        this.recvProvinceCode = recvProvinceCode;
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

    public int getPaymentReasonCode() {
        return paymentReasonCode;
    }

    public void setPaymentReasonCode(int paymentReasonCode) {
        this.paymentReasonCode = paymentReasonCode;
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

    public String getOuterTradeId() {
        return outerTradeId;
    }

    public void setOuterTradeId(String outerTradeId) {
        this.outerTradeId = outerTradeId;
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

    public int getPaymentRuleId() {
        return paymentRuleId;
    }

    public void setPaymentRuleId(int paymentRuleId) {
        this.paymentRuleId = paymentRuleId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCancelChargeId() {
        return cancelChargeId;
    }

    public void setCancelChargeId(String cancelChargeId) {
        this.cancelChargeId = cancelChargeId;
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

    public char getActTag() {
        return actTag;
    }

    public void setActTag(char actTag) {
        this.actTag = actTag;
    }

    public char getExtendTag() {
        return extendTag;
    }

    public void setExtendTag(char extendTag) {
        this.extendTag = extendTag;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
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

    public String getNpTag() {
        return npTag;
    }

    public void setNpTag(String npTag) {
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
        return "PayLogMQInfo{" +
                "chargeId='" + chargeId + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", custId='" + custId + '\'' +
                ", userId='" + userId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", acctId='" + acctId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", paymentId=" + paymentId +
                ", payFeeModeCode=" + payFeeModeCode +
                ", paymentOp=" + paymentOp +
                ", recvFee=" + recvFee +
                ", limitMoney=" + limitMoney +
                ", recvTime='" + recvTime + '\'' +
                ", recvProvinceCode='" + recvProvinceCode + '\'' +
                ", recvEparchyCode='" + recvEparchyCode + '\'' +
                ", recvCityCode='" + recvCityCode + '\'' +
                ", recvDepartId='" + recvDepartId + '\'' +
                ", recvStaffId='" + recvStaffId + '\'' +
                ", paymentReasonCode=" + paymentReasonCode +
                ", inputNo='" + inputNo + '\'' +
                ", inputMode=" + inputMode +
                ", outerTradeId='" + outerTradeId + '\'' +
                ", actionCode=" + actionCode +
                ", actionEventId='" + actionEventId + '\'' +
                ", paymentRuleId=" + paymentRuleId +
                ", remark='" + remark + '\'' +
                ", cancelStaffId='" + cancelStaffId + '\'' +
                ", cancelDepartId='" + cancelDepartId + '\'' +
                ", cancelCityCode='" + cancelCityCode + '\'' +
                ", cancelEparchyCode='" + cancelEparchyCode + '\'' +
                ", cancelTime='" + cancelTime + '\'' +
                ", cancelChargeId='" + cancelChargeId + '\'' +
                ", rsrvFee1=" + rsrvFee1 +
                ", rsrvFee2=" + rsrvFee2 +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", cancelTag=" + cancelTag +
                ", actTag=" + actTag +
                ", extendTag=" + extendTag +
                ", provinceCode='" + provinceCode + '\'' +
                ", devCode='" + devCode + '\'' +
                ", devName='" + devName + '\'' +
                ", npTag=" + npTag +
                ", agentTag=" + agentTag +
                ", contractTag=" + contractTag +
                '}';
    }
}
