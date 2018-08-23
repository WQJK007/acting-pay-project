package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 缴费触发信控服务
 *
 * @author Administrators
 */
public interface CreditService extends IBaseService {
    /**
     * 充值后触发信控
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     */
    void genCreditInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo);
}
