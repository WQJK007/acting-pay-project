package com.unicom.acting.pay.domain;

/**
 * 代收费日志表，主要用于修改TF_B_CLPAYLOG表记录
 *
 * @author Wangkh
 */
public class CLPayLog {
    private String chargeId;
    private String clPaylogId;
    private String provinceCode;
    private String eparchyCode;
    private String areaCode;
    private String netTypeCode;
    private String acctId;
    private String userId;
    private String oldAcctId;
    private String oldUserId;
    private String serialNumber;
    private long paymentId;
    private long recvFee;
    private char cancelTag;
    private char downTag;
    private String outerTradeId;
    private String recvEparchyCode;
    private String recvCityCode;
    private String recvDepartId;
    private String recvStaffId;
    private String recvTime;
    private String cancelStaffId;
    private String cancelDepartId;
    private String cancelCityCode;
    private String cancelEparchyCode;
    private String cancelTime;
    private String cancelChargeId;
    private String rsrvInfo1;
    private String rsrvInfo2;
    private String rsrvInfo3;

    public CLPayLog() {
        chargeId = "";
        clPaylogId = "";
        provinceCode = "";
        eparchyCode = "";
        areaCode = "";
        netTypeCode = "";
        acctId = "";
        userId = "";
        oldAcctId = "";
        oldUserId = "";
        serialNumber = "";
        paymentId = -1;
        recvFee = 0;
        cancelTag = '0';
        downTag = '0';
        outerTradeId = "";
        recvEparchyCode = "";
        recvCityCode = "";
        recvDepartId = "";
        recvStaffId = "";
        recvTime = "";
        cancelStaffId = "";
        cancelDepartId = "";
        cancelCityCode = "";
        cancelEparchyCode = "";
        cancelTime = "";
        cancelChargeId = "";
        rsrvInfo1 = "";
        rsrvInfo2 = "";
        rsrvInfo3 = "";
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getClPaylogId() {
        return clPaylogId;
    }

    public void setClPaylogId(String clPaylogId) {
        this.clPaylogId = clPaylogId;
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

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldAcctId() {
        return oldAcctId;
    }

    public void setOldAcctId(String oldAcctId) {
        this.oldAcctId = oldAcctId;
    }

    public String getOldUserId() {
        return oldUserId;
    }

    public void setOldUserId(String oldUserId) {
        this.oldUserId = oldUserId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getRecvFee() {
        return recvFee;
    }

    public void setRecvFee(long recvFee) {
        this.recvFee = recvFee;
    }

    public char getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(char cancelTag) {
        this.cancelTag = cancelTag;
    }

    public char getDownTag() {
        return downTag;
    }

    public void setDownTag(char downTag) {
        this.downTag = downTag;
    }

    public String getOuterTradeId() {
        return outerTradeId;
    }

    public void setOuterTradeId(String outerTradeId) {
        this.outerTradeId = outerTradeId;
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

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
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

    public String getRsrvInfo1() {
        return rsrvInfo1;
    }

    public void setRsrvInfo1(String rsrvInfo1) {
        this.rsrvInfo1 = rsrvInfo1;
    }

    public String getRsrvInfo2() {
        return rsrvInfo2;
    }

    public void setRsrvInfo2(String rsrvInfo2) {
        this.rsrvInfo2 = rsrvInfo2;
    }

    public String getRsrvInfo3() {
        return rsrvInfo3;
    }

    public void setRsrvInfo3(String rsrvInfo3) {
        this.rsrvInfo3 = rsrvInfo3;
    }

    @Override
    public String toString() {
        return "CLPayLog{" +
                "chargeId='" + chargeId + '\'' +
                ", clPaylogId='" + clPaylogId + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", oldAcctId='" + oldAcctId + '\'' +
                ", oldUserId='" + oldUserId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", paymentId=" + paymentId +
                ", recvFee=" + recvFee +
                ", cancelTag=" + cancelTag +
                ", downTag=" + downTag +
                ", outerTradeId='" + outerTradeId + '\'' +
                ", recvEparchyCode='" + recvEparchyCode + '\'' +
                ", recvCityCode='" + recvCityCode + '\'' +
                ", recvDepartId='" + recvDepartId + '\'' +
                ", recvStaffId='" + recvStaffId + '\'' +
                ", recvTime='" + recvTime + '\'' +
                ", cancelStaffId='" + cancelStaffId + '\'' +
                ", cancelDepartId='" + cancelDepartId + '\'' +
                ", cancelCityCode='" + cancelCityCode + '\'' +
                ", cancelEparchyCode='" + cancelEparchyCode + '\'' +
                ", cancelTime='" + cancelTime + '\'' +
                ", cancelChargeId='" + cancelChargeId + '\'' +
                ", rsrvInfo1='" + rsrvInfo1 + '\'' +
                ", rsrvInfo2='" + rsrvInfo2 + '\'' +
                ", rsrvInfo3='" + rsrvInfo3 + '\'' +
                '}';
    }
}
