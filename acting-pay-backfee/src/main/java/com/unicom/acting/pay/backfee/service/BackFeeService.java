package com.unicom.acting.pay.backfee.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.domain.BackFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.BackFeeCommInfoOut;
import com.unicom.skyark.component.service.IBaseService;

public interface BackFeeService extends IBaseService {
    /**
     * 账管前台预存清退功能
     *
     * @param backFeeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     * @return
     */
    BackFeeCommInfoOut backFeeEss(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo);

}
