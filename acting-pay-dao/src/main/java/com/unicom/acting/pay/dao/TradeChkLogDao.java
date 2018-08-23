package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.skyark.component.dao.IBaseDao;

/**
 * 账务交易对账日志表的增查改操作
 *
 * @author Wangkh
 */
public interface TradeChkLogDao extends IBaseDao {
    /**
     * 新增外围交易对账日志
     *
     * @param tradeHyLog
     * @param provinceCode
     * @return
     */
    int insertTradeHyLog(TradeHyLog tradeHyLog, String provinceCode);

}
