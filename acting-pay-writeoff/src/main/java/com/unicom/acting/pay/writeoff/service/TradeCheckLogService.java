package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 账务交易对账日志入库服务
 *
 * @author Wangkh
 */
public interface TradeCheckLogService extends IBaseService {
    /**
     * 新增外围交易对账日志
     *
     * @param tradeHyLog
     * @param provinceCode
     * @return
     */
    int insertTradeHyLog(TradeHyLog tradeHyLog, String provinceCode);

}
