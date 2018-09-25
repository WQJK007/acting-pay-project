package com.unicom.acting.pay.transfee.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoOut;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommResultInfo;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 余额转账相关功能
 *
 * @author Wangkh
 */
public interface TransFeeService extends IBaseService {

    /**
     * 同账户余额转账
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     * @param transFeeCommResultInfo
     * @return
     */
    TransFeeCommInfoOut transferEncash(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo);

    /**
     * 不用账户余额转出
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     * @param transFeeCommResultInfo
     * @return
     */
    TransFeeCommInfoOut transFeeOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo);
}
