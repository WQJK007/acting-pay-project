package com.unicom.acting.pay.recvfee.service;


import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoOut;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

public interface RecvFeeService extends IBaseService {

    /**
     * 缴费方法
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     * @return
     */
    TradeCommInfoOut simpleRecvFee(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo);

}
