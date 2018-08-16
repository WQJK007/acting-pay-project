package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.AsynWork;
import com.unicom.acting.fee.domain.CLPayLog;
import com.unicom.acting.fee.domain.PayLogDmn;
import com.unicom.acting.fee.domain.PayOtherLog;

import java.util.List;

public interface PayOtherLogDao extends IBaseDao {
    /**
     * 生成代收费日志
     *
     * @param cLPayLogList
     * @param provinceCode
     */
    void insertCLPayLog(List<CLPayLog> cLPayLogList, String provinceCode);

    /**
     * 收费其他信息日志
     *
     * @param payOtherLog
     * @param provinceCode
     */
    long insertPayOtherLog(PayOtherLog payOtherLog, String provinceCode);


    /**
     * 生成账务后台工单表记录
     *
     * @param payLogDmn
     * @param provinceCode
     * @return
     */
    long insertPayLogDmn(PayLogDmn payLogDmn, String provinceCode);

    /**
     * 新增外围交易对账日志
     *
     * @param tradeHyLog
     * @param provinceCode
     * @return
     */
    int insertTradeHyLog(TradeHyLog tradeHyLog, String provinceCode);

    /**
     * 新增异步缴费工单
     *
     * @param asynWork
     * @param provinceCode
     * @return
     */
    int insertPayFeeWork(AsynWork asynWork, String provinceCode);
}
