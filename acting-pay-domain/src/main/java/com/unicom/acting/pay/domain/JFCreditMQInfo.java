package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

import java.util.List;

/**
 * 缴费信控工单MQ同步对象信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JFCreditMQInfo {
    private String acctId;
    private String tradeTypeCode;
    private String tradeId;
    private String writeOffMode;
    private long realFee;
    private String recoverTag;
    private String cancelTag;
    private String batchTag;
    private String remark;
    private String updateStaffId;
    private String updateDepartId;
    private String updateTime;
    private String provinceCode;
    private String bigAcctTag;
    private List<JFUserLeaveFeeInfo> jfUserLeaveFeeInfos;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getTradeTypeCode() {
        return tradeTypeCode;
    }

    public void setTradeTypeCode(String tradeTypeCode) {
        this.tradeTypeCode = tradeTypeCode;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getWriteOffMode() {
        return writeOffMode;
    }

    public void setWriteOffMode(String writeOffMode) {
        this.writeOffMode = writeOffMode;
    }

    public long getRealFee() {
        return realFee;
    }

    public void setRealFee(long realFee) {
        this.realFee = realFee;
    }

    public String getRecoverTag() {
        return recoverTag;
    }

    public void setRecoverTag(String recoverTag) {
        this.recoverTag = recoverTag;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getBigAcctTag() {
        return bigAcctTag;
    }

    public void setBigAcctTag(String bigAcctTag) {
        this.bigAcctTag = bigAcctTag;
    }

    public List<JFUserLeaveFeeInfo> getJfUserLeaveFeeInfos() {
        return jfUserLeaveFeeInfos;
    }

    public void setJfUserLeaveFeeInfos(List<JFUserLeaveFeeInfo> jfUserLeaveFeeInfos) {
        this.jfUserLeaveFeeInfos = jfUserLeaveFeeInfos;
    }

    @Override
    public String toString() {
        return "JFCreditMQInfo{" +
                "acctId='" + acctId + '\'' +
                ", tradeTypeCode='" + tradeTypeCode + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", writeOffMode='" + writeOffMode + '\'' +
                ", realFee=" + realFee +
                ", recoverTag='" + recoverTag + '\'' +
                ", cancelTag='" + cancelTag + '\'' +
                ", batchTag='" + batchTag + '\'' +
                ", remark='" + remark + '\'' +
                ", updateStaffId='" + updateStaffId + '\'' +
                ", updateDepartId='" + updateDepartId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", bigAcctTag='" + bigAcctTag + '\'' +
                ", jfUserLeaveFeeInfos=" + jfUserLeaveFeeInfos +
                '}';
    }
}
