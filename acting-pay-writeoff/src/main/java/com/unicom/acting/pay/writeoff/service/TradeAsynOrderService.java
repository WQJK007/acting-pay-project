package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.FeePayLogDmn;
import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.acting.pay.domain.AsynWorkMQInfo;
import com.unicom.acting.pay.domain.PayLogDmnMQInfo;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 账务交易异步工单入库
 *
 * @author Wangkh
 */
public interface TradeAsynOrderService extends IBaseService {
    /**
     * 生成账务后台工单工单MQ信息
     *
     * @param payLogDmn
     * @return
     */
    PayLogDmnMQInfo genPayLogDmnMQInfo(FeePayLogDmn payLogDmn);

    /**
     * 生成账务后台工单表记录
     *
     * @param feePayLogDmn
     * @param dbType
     * @param provinceCode
     * @return
     */
    long insertPayLogDmn(FeePayLogDmn feePayLogDmn, String dbType, String provinceCode);

    /**
     * 新增异步缴费工单
     *
     * @param asynWork
     * @param dbType
     * @param provinceCode
     * @return
     */
    int insertAsynWork(AsynWork asynWork, String dbType, String provinceCode);

    /**
     * 生成异步工单工单MQ信息
     *
     * @param asynWork
     * @return
     */
    AsynWorkMQInfo genAsynWorkMQInfo(AsynWork asynWork);
}
