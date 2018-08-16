package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 信控接口工单MQ同步对象信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecvCreditMQInfo {
    private String tradeId;
    private String acctId;
    private String userId;
    private String recoveTag;
    private String cancelTag;
    private String batchTag;
    private String writeOffMode;
    private String recvTime;
    private String remark;
    private String updateStaffId;
    private String updateDepartId;
    private String provinceCode;
    private String rsrvStr1;
    private String rsrvStr2;
    private String rsrvStr3;
    private String bigAcctTag;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
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

    public String getRecoveTag() {
        return recoveTag;
    }

    public void setRecoveTag(String recoveTag) {
        this.recoveTag = recoveTag;
    }

    public String getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(String cancelTag) {
        this.cancelTag = cancelTag;
    }

    public String getBatchTag() {
        return batchTag;
    }

    public void setBatchTag(String batchTag) {
        this.batchTag = batchTag;
    }

    public String getWriteOffMode() {
        return writeOffMode;
    }

    public void setWriteOffMode(String writeOffMode) {
        this.writeOffMode = writeOffMode;
    }

    public String getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(String recvTime) {
        this.recvTime = recvTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateStaffId() {
        return updateStaffId;
    }

    public void setUpdateStaffId(String updateStaffId) {
        this.updateStaffId = updateStaffId;
    }

    public String getUpdateDepartId() {
        return updateDepartId;
    }

    public void setUpdateDepartId(String updateDepartId) {
        this.updateDepartId = updateDepartId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getRsrvStr1() {
        return rsrvStr1;
    }

    public void setRsrvStr1(String rsrvStr1) {
        this.rsrvStr1 = rsrvStr1;
    }

    public String getRsrvStr2() {
        return rsrvStr2;
    }

    public void setRsrvStr2(String rsrvStr2) {
        this.rsrvStr2 = rsrvStr2;
    }

    public String getRsrvStr3() {
        return rsrvStr3;
    }

    public void setRsrvStr3(String rsrvStr3) {
        this.rsrvStr3 = rsrvStr3;
    }

    public String getBigAcctTag() {
        return bigAcctTag;
    }

    public void setBigAcctTag(String bigAcctTag) {
        this.bigAcctTag = bigAcctTag;
    }

    @Override
    public String toString() {
        return "RecvCreditMQInfo{" +
                "tradeId='" + tradeId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", userId='" + userId + '\'' +
                ", recoveTag='" + recoveTag + '\'' +
                ", cancelTag='" + cancelTag + '\'' +
                ", batchTag='" + batchTag + '\'' +
                ", writeOffMode='" + writeOffMode + '\'' +
                ", recvTime='" + recvTime + '\'' +
                ", remark='" + remark + '\'' +
                ", updateStaffId='" + updateStaffId + '\'' +
                ", updateDepartId='" + updateDepartId + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", rsrvStr1='" + rsrvStr1 + '\'' +
                ", rsrvStr2='" + rsrvStr2 + '\'' +
                ", rsrvStr3='" + rsrvStr3 + '\'' +
                ", bigAcctTag='" + bigAcctTag + '\'' +
                '}';
    }
}
