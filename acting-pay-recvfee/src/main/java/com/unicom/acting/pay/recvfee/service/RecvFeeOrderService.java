package com.unicom.acting.pay.recvfee.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 缴费异步工单表
 *
 * @author Wangkh
 */
public interface RecvFeeOrderService extends IBaseService {
    AsynWork genAsynWork(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo);
}
