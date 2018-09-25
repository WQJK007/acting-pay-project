package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.acting.pay.domain.AsynWorkMQInfo;
import com.unicom.acting.pay.domain.PayLogDmn;
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
    PayLogDmnMQInfo genPayLogDmnMQInfo(PayLogDmn payLogDmn);

    /**
     * 生成账务后台工单表记录
     * @param payLogDmn
     * @param dbType
     * @param routeValue
     * @return
     */
    long insertPayLogDmn(PayLogDmn payLogDmn, String dbType, String routeValue);


    /**
     * 生成异步工单工单MQ信息
     *
     * @param asynWork
     * @return
     */
    AsynWorkMQInfo genAsynWorkMQInfo(AsynWork asynWork);


    /**
     * 新增异步缴费工单
     *
     * @param asynWork
     * @param dbType
     * @param routeValue
     * @return
     */
    int insertAsynWork(AsynWork asynWork, String dbType, String routeValue);


}
