package com.unicom.acting.pay.bwriteoff.domain;

import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;

public class BatchRecvFeeIn extends RecvFeeCommInfoIn {
    private long originRecvFee;
    private int resultCode;
    private String resultInfo;
    private String batchId;
    private String originTradeId;
    private int tradeTypeCode;
    private String dealtag;
    private String actTag;
    private String originDealtag;
    private String originActTag;
    public BatchRecvFeeIn()
    {
        super();
        originRecvFee = 0;
        resultCode = 0;
        resultInfo = "";
        batchId= "";
        originTradeId="";
    }
    public long getOriginRecvFee() {
        return originRecvFee;
    }

    public void setOriginRecvFee(long originRecvFee) {
        this.originRecvFee = originRecvFee;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getOriginTradeId() {
        return originTradeId;
    }

    public void setOriginTradeId(String originTradeId) {
        this.originTradeId = originTradeId;
    }

    public int getTradeTypeCode() {
        return tradeTypeCode;
    }

    public void setTradeTypeCode(int tradeTypeCode) {
        this.tradeTypeCode = tradeTypeCode;
    }

    public String getDealtag() {
        return dealtag;
    }

    public void setDealtag(String dealtag) {
        this.dealtag = dealtag;
    }

    public String getActTag() {
        return actTag;
    }

    public void setActTag(String actTag) {
        this.actTag = actTag;
    }

    public String getOriginDealtag() {
        return originDealtag;
    }

    public void setOriginDealtag(String originDealtag) {
        this.originDealtag = originDealtag;
    }

    public String getOriginActTag() {
        return originActTag;
    }

    public void setOriginActTag(String originActTag) {
        this.originActTag = originActTag;
    }
}
