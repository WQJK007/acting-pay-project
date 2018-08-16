package com.unicom.acting.pay.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.pay.domain.*;

import java.util.List;

/**
 * 缴费日志表和帐务业务后台处理表资源操作
 *
 * @author Administrators
 */
public interface PayOtherLogService extends IBaseService {
    /**
     * 生成代收费日志
     *
     * @param cLPayLogList
     * @param provinceCode
     */
    void insertCLPayLog(List<CLPayLog> cLPayLogList, String provinceCode);

    /**
     * 生成收费其他信息日志
     *
     * @param carrierInfo
     * @param payLog
     * @return
     */
    PayOtherLog genPayOtherLog(CarrierInfo carrierInfo, PayLog payLog);

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
