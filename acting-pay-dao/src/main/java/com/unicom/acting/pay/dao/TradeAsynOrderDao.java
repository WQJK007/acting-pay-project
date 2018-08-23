package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.PayLogDmn;
import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.skyark.component.dao.IBaseDao;

/**
 * 账务交易异步工单表增改操作
 *
 * @author Wangkh
 */
public interface TradeAsynOrderDao extends IBaseDao {
    /**
     * 生成账务后台工单表记录
     *
     * @param payLogDmn
     * @param dbType
     * @param provinceCode
     * @return
     */
    int insertPayLogDmn(PayLogDmn payLogDmn, String dbType, String provinceCode);

    /**
     * 新增异步缴费工单
     *
     * @param asynWork
     * @param dbType
     * @param provinceCode
     * @return
     */
    int insertAsynWork(AsynWork asynWork, String dbType, String provinceCode);
}
