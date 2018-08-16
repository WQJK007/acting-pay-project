package com.unicom.acting.pay.domain;

/**
 * 外围交易对账日志表
 * 临时放在这里，后期迁出到具体的微服务中,
 * 如果返销也需要，可以放在底层中
 *
 * @author Wangkh
 */
public class TradeHyLog {
    /**
     * 号码归属省份编码
     */
    private String provinceCode;
    /**
     * 工号归属地市编码
     */
    private String eparchyCode;
    /**
     * 工号编码
     */
    private String operId;
    /**
     * 工号归属部门编码
     */
    private String channelCode;
    /**
     * 第三方交费流水
     */
    private String TradeId;
    /**
     * 服务号码
     */
    private String serialNumber;
    /**
     * 第三方充值交易时间
     */
    private String outerTradeTime;
    /**
     * 渠道编码
     */
    private String channelId;
    /**
     * 储值方式
     */
    private int paymentId;
    /**
     * 交费方式
     */
    private int payFeeModeCode;
    /**
     * 交易金额
     */
    private String tradeFee;
    /**
     * 交费流水
     */
    private String chargerId;
    /**
     * 网别编码
     */
    private String netTypeCode;
    /**
     * 工号归属区县编码
     */
    private String cityCode;

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

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getTradeId() {
        return TradeId;
    }

    public void setTradeId(String tradeId) {
        TradeId = tradeId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOuterTradeTime() {
        return outerTradeTime;
    }

    public void setOuterTradeTime(String outerTradeTime) {
        this.outerTradeTime = outerTradeTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPayFeeModeCode() {
        return payFeeModeCode;
    }

    public void setPayFeeModeCode(int payFeeModeCode) {
        this.payFeeModeCode = payFeeModeCode;
    }

    public String getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(String tradeFee) {
        this.tradeFee = tradeFee;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "TradeHyLog{" +
                "provinceCode='" + provinceCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", operId='" + operId + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", TradeId='" + TradeId + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", outerTradeTime='" + outerTradeTime + '\'' +
                ", channelId='" + channelId + '\'' +
                ", paymentId=" + paymentId +
                ", payFeeModeCode=" + payFeeModeCode +
                ", tradeFee='" + tradeFee + '\'' +
                ", chargerId='" + chargerId + '\'' +
                ", netTypeCode='" + netTypeCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}
