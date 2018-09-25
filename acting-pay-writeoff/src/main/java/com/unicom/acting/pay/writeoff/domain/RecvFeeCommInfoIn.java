package com.unicom.acting.pay.writeoff.domain;

import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.CarrierInfo;

import java.util.Set;

/**
 * 收费功能公共请求入参
 *
 * @author Wangkh
 */
public class RecvFeeCommInfoIn extends TradeCommInfoIn {
    //支票，行用卡，转账等支付方式请求入参
    private CarrierInfo carrierInfo;
    //####积分+现金缴费信息####
    /**
     * 支付方式
     */
    private String feePayMode;
    /**
     * 积分金额
     */
    private long userScore;
    /**
     * 23转4剩余代收费欠费
     */
    private String feeBalance;


    //####一卡充缴费增加可打金额####
    /**
     * 一卡充缴费标识 0 代表一卡充缴费
     */
    private String cardTypeCode;
    /**
     * 是否增加可打金额 0代表增加可打金额
     */
    private String printFlag;
    /**
     * 增加可打金额
     */
    private long printFee;

    /**
     * 自然人缴费标识,02代表自然人缴费
     */
    private String npFlag;

    /**
     * 指定账目项缴费
     */
    private Set<Integer> payItemCode;
    /**
     * 指定账期缴费
     */
    private Set<Integer> selCycleId;
    /**
     * 是否增加外围交易日志
     */
    private String tradeHyLogFlag;

    /**
     * 涉及外围交易对账
     */
    private boolean tradeCheckFlag;


    public CarrierInfo getCarrierInfo() {
        return carrierInfo;
    }

    public void setCarrierInfo(CarrierInfo carrierInfo) {
        this.carrierInfo = carrierInfo;
    }

    public String getFeePayMode() {
        return feePayMode;
    }

    public void setFeePayMode(String feePayMode) {
        this.feePayMode = feePayMode;
    }

    public long getUserScore() {
        return userScore;
    }

    public void setUserScore(long userScore) {
        this.userScore = userScore;
    }

    public String getFeeBalance() {
        return feeBalance;
    }

    public void setFeeBalance(String feeBalance) {
        this.feeBalance = feeBalance;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    public long getPrintFee() {
        return printFee;
    }

    public void setPrintFee(long printFee) {
        this.printFee = printFee;
    }

    public String getNpFlag() {
        return npFlag;
    }

    public void setNpFlag(String npFlag) {
        this.npFlag = npFlag;
    }

    public Set<Integer> getPayItemCode() {
        return payItemCode;
    }

    public void setPayItemCode(Set<Integer> payItemCode) {
        this.payItemCode = payItemCode;
    }

    public Set<Integer> getSelCycleId() {
        return selCycleId;
    }

    public void setSelCycleId(Set<Integer> selCycleId) {
        this.selCycleId = selCycleId;
    }

    public String getTradeHyLogFlag() {
        return tradeHyLogFlag;
    }

    public void setTradeHyLogFlag(String tradeHyLogFlag) {
        this.tradeHyLogFlag = tradeHyLogFlag;
    }

    public boolean isTradeCheckFlag() {
        return tradeCheckFlag;
    }

    public void setTradeCheckFlag(boolean tradeCheckFlag) {
        this.tradeCheckFlag = tradeCheckFlag;
    }
}
