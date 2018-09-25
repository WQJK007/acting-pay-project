package com.unicom.acting.pay.writeoff.domain;

import com.unicom.acting.fee.domain.Cycle;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.acting.pay.domain.TradeStaff;

/**
 * 交费返销交易数据，需要自行加工
 * @author ducj
 */
public class CancelRecvFeeInfo {
    private String acctId;
    private String provinceId;
    private TradeStaff tradeTradeStaff;   //交易员工对象
    private Cycle curCycle;
    private String sysDate;
    private String eparchyid;
    private String origChargeId;
    private PayLog payLog;
    //CancelRecvFeeInfoIn
    private String origOuterTradeId;
    private String origOuterTradeTime;
    private String currOuterTradeId;
    private long cancelFee;
    private String agentTag;
    private String reqSrc;
    private String userId;
    private String operateType;
    private String remark;
    private String channelId;
    private String tag;
    //crm返销使用字段
    private String crmDestoryTag;
    private String itemId;
    private String actionCode;
    private String specialDoFlag;
    //沃受理代理商缴费返销使用字段对应ACTRADE_ID
    private String currChargeId;

    public CancelRecvFeeInfo(){
        acctId = "";
        provinceId = "";
        eparchyid = "";
        origChargeId = "";
        sysDate = "";
        origOuterTradeId = "";
        origOuterTradeTime = "";
        currOuterTradeId = "";
        cancelFee = 0;
        agentTag = "";
        reqSrc = "";
        userId = "";
        operateType = "";
        remark = "";
        channelId = "";
        tag = "";
        crmDestoryTag = "";
        itemId = "";
        actionCode = "";
        specialDoFlag = "";
        currChargeId = "";
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getEparchyid() {
        return eparchyid;
    }

    public void setEparchyid(String eparchyid) {
        this.eparchyid = eparchyid;
    }

    public String getOrigChargeId() {
        return origChargeId;
    }

    public void setOrigChargeId(String origChargeId) {
        this.origChargeId = origChargeId;
    }

    public PayLog getPayLog() {
        return payLog;
    }

    public void setPayLog(PayLog payLog) {
        this.payLog = payLog;
    }

    public Cycle getCurCycle() {
        return curCycle;
    }

    public void setCurCycle(Cycle curCycle) {
        this.curCycle = curCycle;
    }

    public String getSysDate() {
        return sysDate;
    }

    public void setSysDate(String sysDate) {
        this.sysDate = sysDate;
    }

    public String getOrigOuterTradeId() {
        return origOuterTradeId;
    }

    public void setOrigOuterTradeId(String origOuterTradeId) {
        this.origOuterTradeId = origOuterTradeId;
    }

    public String getOrigOuterTradeTime() {
        return origOuterTradeTime;
    }

    public void setOrigOuterTradeTime(String origOuterTradeTime) {
        this.origOuterTradeTime = origOuterTradeTime;
    }

    public String getCurrOuterTradeId() {
        return currOuterTradeId;
    }

    public void setCurrOuterTradeId(String currOuterTradeId) {
        this.currOuterTradeId = currOuterTradeId;
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

    public String getReqSrc() {
        return reqSrc;
    }

    public void setReqSrc(String reqSrc) {
        this.reqSrc = reqSrc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TradeStaff getTradeTradeStaff() {
        return tradeTradeStaff;
    }

    public void setTradeTradeStaff(TradeStaff tradeTradeStaff) {
        this.tradeTradeStaff = tradeTradeStaff;
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

    public String getSpecialDoFlag() {
        return specialDoFlag;
    }

    public void setSpecialDoFlag(String specialDoFlag) {
        this.specialDoFlag = specialDoFlag;
    }

    public String getCurrChargeId() {
        return currChargeId;
    }

    public void setCurrChargeId(String currChargeId) {
        this.currChargeId = currChargeId;
    }

    @Override
    public String toString() {
        return "CancelRecvFeeInfo{" +
                "acctId='" + acctId + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", eparchyid='" + eparchyid + '\'' +
                ", origChargeId='" + origChargeId + '\'' +
                ", payLog=" + payLog +
                ", curCycle=" + curCycle +
                ", sysDate='" + sysDate + '\'' +
                ", origOuterTradeId='" + origOuterTradeId + '\'' +
                ", origOuterTradeTime='" + origOuterTradeTime + '\'' +
                ", currOuterTradeId='" + currOuterTradeId + '\'' +
                ", cancelFee=" + cancelFee +
                ", agentTag='" + agentTag + '\'' +
                ", reqSrc='" + reqSrc + '\'' +
                ", userId='" + userId + '\'' +
                ", operateType='" + operateType + '\'' +
                ", remark='" + remark + '\'' +
                ", channelId='" + channelId + '\'' +
                ", tag='" + tag + '\'' +
                ", tradeTradeStaff=" + tradeTradeStaff +
                ", crmDestoryTag='" + crmDestoryTag + '\'' +
                ", itemId='" + itemId + '\'' +
                ", actionCode='" + actionCode + '\'' +
                ", specialDoFlag='" + specialDoFlag + '\'' +
                ", currChargeId='" + currChargeId + '\'' +
                '}';
    }
}
