package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;

/**
 * 缴费信控工单MQ同步对象信息
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JFCreditMQInfo implements Cloneable {
    private String userId;
    private String acctId;
    private String tradeTypeCode;
    private String tradeId;
    private String writeOffMode;
    private long realFee;
    private long leaveRealFee;
    private String recoverTag;
    private String cancelTag;
    private String batchTag;
    private String processTag;
    private String remark;
    private String updateStaffId;
    private String updateDepartId;
    private String updateTime;
    private String provinceCode;
    private String bigAcctTag;

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

    public long getLeaveRealFee() {
        return leaveRealFee;
    }

    public void setLeaveRealFee(long leaveRealFee) {
        this.leaveRealFee = leaveRealFee;
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

    public String getProcessTag() {
        return processTag;
    }

    public void setProcessTag(String processTag) {
        this.processTag = processTag;
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

    @Override
    public JFCreditMQInfo clone() {
        try {
            return (JFCreditMQInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "JFCreditMQInfo clone error");
        }
    }

    @Override
    public String toString() {
        return "JFCreditMQInfo{" +
                "userId='" + userId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", tradeTypeCode='" + tradeTypeCode + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", writeOffMode='" + writeOffMode + '\'' +
                ", realFee=" + realFee +
                ", leaveRealFee=" + leaveRealFee +
                ", recoverTag='" + recoverTag + '\'' +
                ", cancelTag='" + cancelTag + '\'' +
                ", batchTag='" + batchTag + '\'' +
                ", processTag='" + processTag + '\'' +
                ", remark='" + remark + '\'' +
                ", updateStaffId='" + updateStaffId + '\'' +
                ", updateDepartId='" + updateDepartId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", bigAcctTag='" + bigAcctTag + '\'' +
                '}';
    }
}
