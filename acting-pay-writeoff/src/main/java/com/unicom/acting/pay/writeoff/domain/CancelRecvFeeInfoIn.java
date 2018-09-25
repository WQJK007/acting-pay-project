package com.unicom.acting.pay.writeoff.domain;

/**
 * 返销服务申请的请求所包含的信息
 * @author ducj
 */
public class CancelRecvFeeInfoIn {
    private String origOuterTradeId;
    private String origOuterTradeTime;
    private String currOuterTradeId;
    private long cancelFee;
    private String agentTag;
    private String tag;
    private String reqSrc;
    private String tradeStaffId;
    private String tradeDepartId;
    private String tradeCityCode;
    private String tradeEparchyCode;
    private String tradeProvinceCode;
    //crm
    private String crmDestoryTag;
    private String itemId;
    private String actionCode;

    private String userId;
    private String operateType;
    private String remark;
    private String channelId;
    private String provinceId;
    private String eparchyId;
    private String currChargeId;//ACTRADE_ID

    public CancelRecvFeeInfoIn(){
        origOuterTradeId = "";
        origOuterTradeTime = "";
        currOuterTradeId = "";
        cancelFee = 0;
        agentTag = "";
        tag = "";
        reqSrc = "";
        tradeStaffId = "";
        tradeDepartId = "";
        tradeCityCode = "";
        tradeEparchyCode = "";
        tradeProvinceCode = "";
        crmDestoryTag = "";
        itemId = "";
        actionCode = "";
        userId = "";
        operateType = "";
        remark = "";
        channelId = "";
        provinceId = "";
        eparchyId = "";
        currChargeId = "";
    }

    public String getCurrChargeId() {
        return currChargeId;
    }

    public void setCurrChargeId(String currChargeId) {
        this.currChargeId = currChargeId;
    }

    public String getCrmDestoryTag() {
        return crmDestoryTag;
    }

    public void setCrmDestoryTag(String crmDestoryTag) {
        this.crmDestoryTag = crmDestoryTag;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getEparchyId() {
        return eparchyId;
    }

    public void setEparchyId(String eparchyId) {
        this.eparchyId = eparchyId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrigOuterTradeId() {
        return origOuterTradeId;
    }

    public void setOrigOuterTradeId(String origOuterTradeId) {
        this.origOuterTradeId = origOuterTradeId;
    }

    public String getCurrOuterTradeId() {
        return currOuterTradeId;
    }

    public void setCurrOuterTradeId(String currOuterTradeId) {
        this.currOuterTradeId = currOuterTradeId;
    }

    public String getOrigOuterTradeTime() {
        return origOuterTradeTime;
    }

    public void setOrigOuterTradeTime(String origOuterTradeTime) {
        this.origOuterTradeTime = origOuterTradeTime;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTradeStaffId() {
        return tradeStaffId;
    }

    public void setTradeStaffId(String tradeStaffId) {
        this.tradeStaffId = tradeStaffId;
    }

    public String getTradeDepartId() {
        return tradeDepartId;
    }

    public void setTradeDepartId(String tradeDepartId) {
        this.tradeDepartId = tradeDepartId;
    }

    public String getTradeCityCode() {
        return tradeCityCode;
    }

    public void setTradeCityCode(String tradeCityCode) {
        this.tradeCityCode = tradeCityCode;
    }

    public String getTradeEparchyCode() {
        return tradeEparchyCode;
    }

    public void setTradeEparchyCode(String tradeEparchyCode) {
        this.tradeEparchyCode = tradeEparchyCode;
    }

    public String getTradeProvinceCode() {
        return tradeProvinceCode;
    }

    public void setTradeProvinceCode(String tradeProvinceCode) {
        this.tradeProvinceCode = tradeProvinceCode;
    }

    public long getCancelFee() {
        return cancelFee;
    }

    public void setCancelFee(long cancelFee) {
        this.cancelFee = cancelFee;
    }

    public String getAgentTag() {
        return agentTag;
    }

    public void setAgentTag(String agentTag) {
        this.agentTag = agentTag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getReqSrc() {
        return reqSrc;
    }

    public void setReqSrc(String reqSrc) {
        this.reqSrc = reqSrc;
    }

    @Override
    public String toString() {
        return "CancelRecvFeeInfoIn{" +
                "origOuterTradeId='" + origOuterTradeId + '\'' +
                ", origOuterTradeTime='" + origOuterTradeTime + '\'' +
                ", currOuterTradeId='" + currOuterTradeId + '\'' +
                ", cancelFee='" + cancelFee + '\'' +
                ", agentTag='" + agentTag + '\'' +
                ", tag='" + tag + '\'' +
                ", remark='" + remark + '\'' +
                ", tradeStaffId='" + tradeStaffId + '\'' +
                ", tradeDepartId='" + tradeDepartId + '\'' +
                ", tradeCityCode='" + tradeCityCode + '\'' +
                ", tradeEparchyCode='" + tradeEparchyCode + '\'' +
                ", tradeProvinceCode='" + tradeProvinceCode + '\'' +
                ", reqSrc='" + reqSrc + '\'' +
                '}';
    }
}
