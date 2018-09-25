package com.unicom.acting.pay.domain;

/**
 * 缴费日志对象，映射TF_B_TRANSLOG表主要字段
 *
 * @author ducj
 */
public class TransLog {
    private String userId;
    private String acctId;
    private String desUserId;
    private String desAcctId;
    private String chargeId;
    private String desChargeId;
    private long transFee;
    private String acctBalanceId;
    private String acctBalanceIdT;
    private String depositCodeT;
    private String transTime;
    private String eparchyCode;
    private String provoiceCode;


    public TransLog() {
        userId = "";
        acctId = "";
        desUserId = "";
        desAcctId = "";
        chargeId = "";
        desChargeId = "";
        transFee = 0;
        acctBalanceId = "";
        acctBalanceIdT = "";
        depositCodeT = "";
        transTime = "";
        eparchyCode = "";
        provoiceCode = "";
    }

    public void init() {
        userId = "";
        acctId = "";
        desUserId = "";
        desAcctId = "";
        chargeId = "";
        desChargeId = "";
        transFee = 0;
        acctBalanceId = "";
        acctBalanceIdT = "";
        depositCodeT = "";
        transTime = "";
        eparchyCode = "";
        provoiceCode = "";
    }

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

    public String getDesUserId() {
        return desUserId;
    }

    public void setDesUserId(String desUserId) {
        this.desUserId = desUserId;
    }

    public String getDesAcctId() {
        return desAcctId;
    }

    public void setDesAcctId(String desAcctId) {
        this.desAcctId = desAcctId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getDesChargeId() {
        return desChargeId;
    }

    public void setDesChargeId(String desChargeId) {
        this.desChargeId = desChargeId;
    }

    public long getTransFee() {
        return transFee;
    }

    public void setTransFee(long transFee) {
        this.transFee = transFee;
    }

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public String getAcctBalanceIdT() {
        return acctBalanceIdT;
    }

    public void setAcctBalanceIdT(String acctBalanceIdT) {
        this.acctBalanceIdT = acctBalanceIdT;
    }

    public String getDepositCodeT() {
        return depositCodeT;
    }

    public void setDepositCodeT(String depositCodeT) {
        this.depositCodeT = depositCodeT;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getProvoiceCode() {
        return provoiceCode;
    }

    public void setProvoiceCode(String provoiceCode) {
        this.provoiceCode = provoiceCode;
    }
}
