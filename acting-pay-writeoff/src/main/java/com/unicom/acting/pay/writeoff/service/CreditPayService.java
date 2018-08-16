package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.pay.domain.CreditMQInfo;
import com.unicom.acting.pay.domain.TradeCommMQInfo;
import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.TradeCommInfoIn;

/**
 * 缴费触发信控服务
 *
 * @author Administrators
 */
public interface CreditPayService extends IBaseService {
    /**
     * 充值后触发信控
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void genCreditInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommMQInfo tradeCommMQInfo);
}
