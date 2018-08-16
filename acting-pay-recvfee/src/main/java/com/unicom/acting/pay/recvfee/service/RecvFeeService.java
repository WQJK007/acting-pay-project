package com.unicom.acting.pay.recvfee.service;


import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acting.fee.domain.TradeCommInfoOut;
import com.unicom.acting.pay.domain.TradeCommMQInfo;
import com.unicom.skyark.component.service.IBaseService;

public interface RecvFeeService extends IBaseService {

    /**
     * 缴费方法
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommMQInfo
     * @return
     */
    TradeCommInfoOut simpleRecvFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommMQInfo tradeCommMQInfo);

}
