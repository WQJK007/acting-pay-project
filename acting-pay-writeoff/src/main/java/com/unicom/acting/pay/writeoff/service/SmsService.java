package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.NoticeInfo;
import com.unicom.acting.pay.domain.SmsOrder;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 充值短信发送服务
 *
 * @author Administrators
 */
public interface SmsService extends IBaseService {
    /**
     * 加载充值后触发短信相关参数
     */
    void loadSmsParam();

    /**
     * 充值后短信发送
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     */
    void genSmsInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo);

    /**
     * 短信入库
     *
     * @param smsOrders
     */
    void insertSmsIO(List<SmsOrder> smsOrders);

}
