package com.unicom.acting.pay.writeoff.domain;

/**
 * 打印日志对象，映射TF_B_INVOICE_PRINTINFO、TF_B_INVOICE_PRINTLOG 表主要字段
 *
 * @author ducj
 */
public class PrintInfo {
    private String printInfoId;
    private String printLogId;
    private String custId;
    private String acctId;
    private long fee;
    private String outerTradeId;
    private String chargeId;
    private String recycleTag;
    private String provinceCode;

    public PrintInfo() {
        fee = 0;
    }

    public void init() {
        printInfoId = "";
        printLogId = "";
        custId = "";
        acctId = "";
        fee = 0;
        recycleTag = "";
        outerTradeId = "";
        chargeId = "";
        provinceCode = "";
    }

    public String getPrintInfoId() {
        return printInfoId;
    }

    public void setPrintInfoId(String printInfoId) {
        this.printInfoId = printInfoId;
    }

    public String getPrintLogId() {
        return printLogId;
    }

    public void setPrintLogId(String printLogId) {
        this.printLogId = printLogId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public String getRecycleTag() {
        return recycleTag;
    }

    public void setRecycleTag(String recycleTag) {
        this.recycleTag = recycleTag;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getOuterTradeId() {
        return outerTradeId;
    }

    public void setOuterTradeId(String outerTradeId) {
        this.outerTradeId = outerTradeId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }


    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }


    @Override
    public String toString() {
        return "PrintInfo{" +
                "printInfoId='" + printInfoId + '\'' +
                ", printLogId='" + printLogId + '\'' +
                ", custId='" + custId + '\'' +
                ", acctId='" + acctId + '\'' +
                ", fee='" + fee + '\'' +
                ", outerTradeId='" + outerTradeId + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", recycleTag='" + recycleTag + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
