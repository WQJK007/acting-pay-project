package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.pay.dao.TradeChkLogDao;
import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.acting.pay.writeoff.service.TradeCheckLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeCheckLogServiceImpl implements TradeCheckLogService {
    @Autowired
    private TradeChkLogDao tradeChkLogDao;


    @Override
    public int insertTradeHyLog(TradeHyLog tradeHyLog) {
        return tradeChkLogDao.insertTradeHyLog(tradeHyLog);
    }
}
