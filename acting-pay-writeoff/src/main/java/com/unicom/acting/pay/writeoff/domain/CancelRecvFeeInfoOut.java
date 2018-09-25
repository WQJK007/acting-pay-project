package com.unicom.acting.pay.writeoff.domain;


import com.unicom.acting.pay.domain.*;
import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ducj
 */
public class CancelRecvFeeInfoOut {
    private String currOuterTradeId;
    private String origChargeId;
    private String acctId;
    private String userId;
    private String serialNumber;
    private String chargeId;
    private String cancelChargeId;
    private int resultTag;
    private String respCode;
    private String respDesc;

    private int paymentId;
    private int payfeeModeCode;
    private String channelId;
//    private int actionCode;
//    private int cancelTag;
    private TradeStaff tradeTradeStaff;
    private List<PayLogDmn> paylogDmnList;
    private List<PayLog> cancelPayLogList;
    private Map<String, Boolean> drecvTagMap;
    private List<ChargeRelation> chargeRelationList;
    private Map<String, List<WriteOffLog>> writeOffLogListMap;
    private Map<String, List<AccessLog>> accesslogListMap;

    public CancelRecvFeeInfoOut(){
        this.currOuterTradeId =  "";
        this.origChargeId =  "";
        this.respCode =  "";
        this.respDesc =  "";
        this.acctId =  "";
        this.userId =  "";
        this.serialNumber = "";
        this.chargeId = "";
        this.cancelChargeId = "";
        this.resultTag = -1;
        this.paymentId = 0;
        this.payfeeModeCode = 0;
        this.channelId = "";
        paylogDmnList = new ArrayList<>();
        cancelPayLogList = new ArrayList<>();
        drecvTagMap = new HashedMap();
        chargeRelationList = new ArrayList<>();
        writeOffLogListMap =  new HashedMap();
        accesslogListMap =  new HashedMap();
    }

    public String getCurrOuterTradeId() {
        return currOuterTradeId;
    }

    public void setCurrOuterTradeId(String currOuterTradeId) {
        this.currOuterTradeId = currOuterTradeId;
    }

    public String getOrigChargeId() {
        return origChargeId;
    }

    public void setOrigChargeId(String origChargeId) {
        this.origChargeId = origChargeId;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
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

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getCancelChargeId() {
        return cancelChargeId;
    }

    public void setCancelChargeId(String cancelChargeId) {
        this.cancelChargeId = cancelChargeId;
    }

    public int getResultTag() {
        return resultTag;
    }

    public void setResultTag(int resultTag) {
        this.resultTag = resultTag;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPayfeeModeCode() {
        return payfeeModeCode;
    }

    public void setPayfeeModeCode(int payfeeModeCode) {
        this.payfeeModeCode = payfeeModeCode;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public TradeStaff getTradeTradeStaff() {
        return tradeTradeStaff;
    }

    public void setTradeTradeStaff(TradeStaff tradeTradeStaff) {
        this.tradeTradeStaff = tradeTradeStaff;
    }

    public List<PayLogDmn> getPaylogDmnList() {
        return paylogDmnList;
    }

    public void setPaylogDmnList(List<PayLogDmn> paylogDmnList) {
        this.paylogDmnList = paylogDmnList;
    }

    public List<PayLog> getCancelPayLogList() {
        return cancelPayLogList;
    }

    public void setCancelPayLogList(List<PayLog> cancelPayLogList) {
        this.cancelPayLogList = cancelPayLogList;
    }

    public Map<String, Boolean> getDrecvTagMap() {
        return drecvTagMap;
    }

    public void setDrecvTagMap(Map<String, Boolean> drecvTagMap) {
        this.drecvTagMap = drecvTagMap;
    }

    public List<ChargeRelation> getChargeRelationList() {
        return chargeRelationList;
    }

    public void setChargeRelationList(List<ChargeRelation> chargeRelationList) {
        this.chargeRelationList = chargeRelationList;
    }

    public Map<String, List<WriteOffLog>> getWriteOffLogListMap() {
        return writeOffLogListMap;
    }

    public void setWriteOffLogListMap(Map<String, List<WriteOffLog>> writeOffLogListMap) {
        this.writeOffLogListMap = writeOffLogListMap;
    }

    public Map<String, List<AccessLog>> getAccesslogListMap() {
        return accesslogListMap;
    }

    public void setAccesslogListMap(Map<String, List<AccessLog>> accesslogListMap) {
        this.accesslogListMap = accesslogListMap;
    }

    @Override
    public String toString() {
        return "CancelRecvFeeInfoOut{" +
                ", currOuterTradeId='" + currOuterTradeId + '\'' +
                ", origChargeId='" + origChargeId + '\'' +
                ", respCode='" + respCode + '\'' +
                ", respDesc='" + respDesc + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", cancelChargeId='" + cancelChargeId + '\'' +
                ", resultTag=" + resultTag +
                ", paymentId=" + paymentId +
                ", payfeeModeCode=" + payfeeModeCode +
                ", channelId=" + channelId +
                ", tradeTradeStaff=" + tradeTradeStaff +
                ", paylogDmnList=" + paylogDmnList +
                ", cancelPayLogList=" + cancelPayLogList +
                ", drecvTagMap=" + drecvTagMap +
                ", chargeRelationList=" + chargeRelationList +
                ", writeOffLogListMap=" + writeOffLogListMap +
                ", accesslogListMap=" + accesslogListMap +
                '}';
    }
}
