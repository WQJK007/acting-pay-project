package com.unicom.acting.pay.recvfee.service;

import com.unicom.acting.fee.domain.Staff;
import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

public interface RecvFeeCheckLogService extends IBaseService {
    TradeHyLog genTradeHyLog(RecvFeeCommInfoIn recvFeeCommInfoIn, Staff staff, String chargeId);

    void insertTradeHyLog(TradeHyLog tradeHyLog, String dbType);
}
