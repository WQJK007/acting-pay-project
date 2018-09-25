package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 缴费MQ消息Key
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RecvFeeAllMQKey {
    /**
     * @see #acctId 账户标识
     */
    private String acctId;
    /**
     * @see #outerTradeId 外围交易流水
     */
    private String outerTradeId;
    /**
     * @see #chargeId 交易流水
     */
    private String chargeId;
    /**
     * @see #tradeCheckType 校验工单类型
     * 0 paylog 1 paylogdmn 2 asynwork 3 paylogasyn
     */
    private String tradeCheckType;
    /**
     * @see #provinceCode 账户归属省份编码，工单路由使用
     */
    private String provinceCode;

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

    public String getTradeCheckType() {
        return tradeCheckType;
    }

    public void setTradeCheckType(String tradeCheckType) {
        this.tradeCheckType = tradeCheckType;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "RecvFeeAllMQKey{" +
                "acctId='" + acctId + '\'' +
                ", outerTradeId='" + outerTradeId + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", tradeCheckType='" + tradeCheckType + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
