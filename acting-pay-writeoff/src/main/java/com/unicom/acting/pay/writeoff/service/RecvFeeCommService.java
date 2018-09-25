package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 缴费交易信息
 *
 * @author Wangkh
 */
public interface RecvFeeCommService extends IBaseService {
    /**
     * 设置缴费金额
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setRecvfee(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 缴费入库方法
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     */
    void genRecvDBInfo(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo);
}
