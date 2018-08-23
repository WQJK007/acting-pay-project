package com.unicom.acting.pay.writeoff.service;


import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.acting.pay.writeoff.domain.BackFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 账务交易入库公共方法
 *
 * @author Administrators
 */
public interface TradeCommService extends IBaseService {
    /**
     * 设置缴费金额
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setRecvfee(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 设置清退金额
     *
     * @param backFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setBackFee(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo);


    /**
     * 同账户金额转出
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFerOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 同账户金额转入
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFerIn(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 不同账户金额转出
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFeeOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 不同账户金额转入
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFeeIn(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 生成缴费入库对象信息，包含MQ发送信息
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     */
    void genInDBInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo);


}
