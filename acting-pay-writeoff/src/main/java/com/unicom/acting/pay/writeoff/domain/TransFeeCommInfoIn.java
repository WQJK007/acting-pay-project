package com.unicom.acting.pay.writeoff.domain;


import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;

import java.util.List;

/**
 * 转账功能公共请求入参
 *
 * @author Wangkh
 */
public class TransFeeCommInfoIn extends TradeCommInfoIn {
    /**
     * @see #transTag 前台转账标识
     * 1 目标账本如果为私有，账本的用户标识是转入用户的
     */
    private String transTag;

    /**
     * @see #chgAcctTag 过户转账标识
     * 0 非过户转账
     * 1 过户转账
     */
    private char chgAcctTag;

    /**
     * @see #decuctOwefeeTag 清退是否扣除欠费
     * 0 扣往月费用
     * 1不扣往月费用
     */
    private char decuctOwefeeTag;

    /**
     * @see #transOutDedposits 转出账本信息
     */
    private List<TradeDepositInfo> transOutDedposits;

    /**
     * @see #tradeDepositInfos 按账本实例清退时账本清退明细信息
     * 主要包括待清退账本实例标识、清退金额、是否强制清退等信息
     */
    private List<TradeDepositInfo> tradeDepositInfos;

    public TransFeeCommInfoIn() {
        chgAcctTag = '0';
    }

    public String getTransTag() {
        return transTag;
    }

    public void setTransTag(String transTag) {
        this.transTag = transTag;
    }

    public char getChgAcctTag() {
        return chgAcctTag;
    }

    public void setChgAcctTag(char chgAcctTag) {
        this.chgAcctTag = chgAcctTag;
    }

    public char getDecuctOwefeeTag() {
        return decuctOwefeeTag;
    }

    public void setDecuctOwefeeTag(char decuctOwefeeTag) {
        this.decuctOwefeeTag = decuctOwefeeTag;
    }

    public List<TradeDepositInfo> getTransOutDedposits() {
        return transOutDedposits;
    }

    public void setTransOutDedposits(List<TradeDepositInfo> transOutDedposits) {
        this.transOutDedposits = transOutDedposits;
    }

    public List<TradeDepositInfo> getTradeDepositInfos() {
        return tradeDepositInfos;
    }

    public void setTradeDepositInfos(List<TradeDepositInfo> tradeDepositInfos) {
        this.tradeDepositInfos = tradeDepositInfos;
    }
}
