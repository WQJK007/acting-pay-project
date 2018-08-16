package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.pay.domain.SmsMQInfo;
import com.unicom.acting.pay.domain.TradeCommMQInfo;
import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.NoticeInfo;
import com.unicom.acting.fee.domain.TradeCommInfoIn;

import java.util.List;

/**
 * 充值短信发送服务
 *
 * @author Administrators
 */
public interface SmsPayService extends IBaseService {
    /**
     * 加载充值后触发短信相关参数
     *
     * @param provinceCode
     */
    void loadSmsParam(String provinceCode);

    /**
     * 充值后短信发送
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommMQInfo
     */
    void genSmsInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommMQInfo tradeCommMQInfo);

    /**
     * 短信入库
     * @param noticeInfoList
     * @param provinceCode
     */
    void insertSmsIO(List<NoticeInfo> noticeInfoList, String provinceCode);

}
