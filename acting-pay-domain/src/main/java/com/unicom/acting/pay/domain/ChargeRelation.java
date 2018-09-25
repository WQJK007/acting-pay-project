package com.unicom.acting.pay.domain;

/**
 * 交易关联日志表
 *
 * @author Wangkh
 */
public class ChargeRelation {
    private String acctId;
    private String id;
    private String operateId1;
    private String operateId2;
    private String operateType;
    private String debutyCode;
    private String operateStaffId;
    private String operateDepartId;
    private String operateCityCode;
    private String operateEparchyCode;
    private String operateTime;
    private String eparchyCode;
    private String provinceCode;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperateId1() {
        return operateId1;
    }

    public void setOperateId1(String operateId1) {
        this.operateId1 = operateId1;
    }

    public String getOperateId2() {
        return operateId2;
    }

    public void setOperateId2(String operateId2) {
        this.operateId2 = operateId2;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getDebutyCode() {
        return debutyCode;
    }

    public void setDebutyCode(String debutyCode) {
        this.debutyCode = debutyCode;
    }

    public String getOperateStaffId() {
        return operateStaffId;
    }

    public void setOperateStaffId(String operateStaffId) {
        this.operateStaffId = operateStaffId;
    }

    public String getOperateDepartId() {
        return operateDepartId;
    }

    public void setOperateDepartId(String operateDepartId) {
        this.operateDepartId = operateDepartId;
    }

    public String getOperateCityCode() {
        return operateCityCode;
    }

    public void setOperateCityCode(String operateCityCode) {
        this.operateCityCode = operateCityCode;
    }

    public String getOperateEparchyCode() {
        return operateEparchyCode;
    }

    public void setOperateEparchyCode(String operateEparchyCode) {
        this.operateEparchyCode = operateEparchyCode;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
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

    @Override
    public String toString() {
        return "ChargeRelation{" +
                "acctId='" + acctId + '\'' +
                ", id='" + id + '\'' +
                ", operateId1='" + operateId1 + '\'' +
                ", operateId2='" + operateId2 + '\'' +
                ", operateType='" + operateType + '\'' +
                ", debutyCode='" + debutyCode + '\'' +
                ", operateStaffId='" + operateStaffId + '\'' +
                ", operateDepartId='" + operateDepartId + '\'' +
                ", operateCityCode='" + operateCityCode + '\'' +
                ", operateEparchyCode='" + operateEparchyCode + '\'' +
                ", operateTime='" + operateTime + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
