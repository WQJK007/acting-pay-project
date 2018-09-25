package com.unicom.acting.pay.writeoff.domain;

import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;

import java.util.List;

/**
 * 清退功能公共入参
 *
 * @author Wangkh
 */
public class BackFeeCommInfoIn extends TradeCommInfoIn {
    /**
     * @see #batchId
     * 批次号
     */
    private String batchId;

    /**
     * @see #asynDealTag
     * 异步处理标识
     */
    private String asynDealTag;

    /**
     * @see #workTypeCode
     * 异步工单类型
     */
    private String workTypeCode;

    /**
     * @see #forckTag
     * 强制清退
     */
    private String forckTag;

    /**
     * @see #backType  清退类型
     * 0 按总额金额
     * 1按特定账本科目类型清退
     * 2按账本实例清退
     */
    private String backType;
    /**
     * @see #backDepositCode
     * 按特定账本科目类型清退的账本科目类型
     * 主要针对缴费站、小额包年功能按特定账本科目类型进行清退
     */
    private String backDepositCode;
    /**
     * @see #tradeDepositInfos 按账本实例清退时账本清退明细信息
     * 主要包括待清退账本实例标识、清退金额、是否强制清退等信息
     */
    private List<TradeDepositInfo> tradeDepositInfos;
    /**
     * @see #decuctOwefeeTag 清退是否扣除欠费
     * 0 扣往月费用
     * 1不扣往月费用
     */
    private char decuctOwefeeTag;

    /**
     * @see #forceBackAcctTag 是否强制清退账户余额
     * 0 不强制清退
     * 1 强制清退
     */
    private char forceBackAcctTag;

    /**
     * @see #decuctInvTag  扣减可打金额
     * 0 扣减可打金额
     * 1 扣减可打金额
     */
    private char decuctInvTag;

    private List<TradeDepositInfo> backDepositInfos;


    public BackFeeCommInfoIn() {
        backType = "2";
        decuctInvTag = '1';
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getAsynDealTag() {
        return asynDealTag;
    }


    public void setAsynDealTag(String asynDealTag) {
        this.asynDealTag = asynDealTag;
    }

    public String getWorkTypeCode() {
        return workTypeCode;
    }

    public void setWorkTypeCode(String workTypeCode) {
        this.workTypeCode = workTypeCode;
    }

    public String getForckTag() {
        return forckTag;
    }

    public void setForckTag(String forckTag) {
        this.forckTag = forckTag;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public String getBackDepositCode() {
        return backDepositCode;
    }

    public void setBackDepositCode(String backDepositCode) {
        this.backDepositCode = backDepositCode;
    }

    public List<TradeDepositInfo> getTradeDepositInfos() {
        return tradeDepositInfos;
    }

    public void setTradeDepositInfos(List<TradeDepositInfo> tradeDepositInfos) {
        this.tradeDepositInfos = tradeDepositInfos;
    }

    public char getDecuctOwefeeTag() {
        return decuctOwefeeTag;
    }

    public void setDecuctOwefeeTag(char decuctOwefeeTag) {
        this.decuctOwefeeTag = decuctOwefeeTag;
    }

    public char getForceBackAcctTag() {
        return forceBackAcctTag;
    }

    public void setForceBackAcctTag(char forceBackAcctTag) {
        this.forceBackAcctTag = forceBackAcctTag;
    }

    public char getDecuctInvTag() {
        return decuctInvTag;
    }

    public void setDecuctInvTag(char decuctInvTag) {
        this.decuctInvTag = decuctInvTag;
    }

    public List<TradeDepositInfo> getBackDepositInfos() {
        return backDepositInfos;
    }

    public void setBackDepositInfos(List<TradeDepositInfo> backDepositInfos) {
        this.backDepositInfos = backDepositInfos;
    }
}
