package com.unicom.acting.pay.writeoff.service;


import com.unicom.acting.pay.domain.TradeCommMQInfo;
import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.TradeCommInfoIn;

/**
 * 销账相关公共方法
 *
 * @author Administrators
 */
public interface WriteOffInDBService extends IBaseService {
    /**
     * 获取账户锁
     *
     * @param acctId
     * @param provinceCode
     */
    void genLockAccount(String acctId, String provinceCode);

    /**
     * 设置缴费金额
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void setRecvfee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 设置清退金额
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void setBackFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);


    /**
     * 同账户金额转出
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFerOut(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 同账户金额转入
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFerIn(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 不同账户金额转出
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFeeOut(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 不同账户金额转入
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFeeIn(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 生成缴费入库对象信息，包含MQ发送信息
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommMQInfo
     */
    void genInDBInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommMQInfo tradeCommMQInfo);


}
