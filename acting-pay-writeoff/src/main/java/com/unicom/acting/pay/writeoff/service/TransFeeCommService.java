package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommResultInfo;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 余额转账公共服务
 *
 * @author Wangkh
 */
public interface TransFeeCommService extends IBaseService {
    /**
     * 同账户金额转出
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFerEnCashOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 同账户金额转入 TransferEncash
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setTransFerEnCashIn(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo);

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
     * 余额转出入库信息
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     * @param transFeeCommResultInfo
     */
    void genTransFeeDBInfo(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo);

}
