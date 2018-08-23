package com.unicom.acting.pay.dao;


import com.unicom.acting.pay.domain.CLPayLog;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.acting.pay.domain.PayOtherLog;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * 对TF_B_PAYLOG，TF_B_PAYOTHER_LOG,表的增查改操作
 *
 * @author Wangkh
 */
public interface PayLogDao extends IBaseDao {
    /**
     * 新增缴费日志记录
     *
     * @param payLog
     * @param provinceCode
     * @return
     */
    int insertPayLog(PayLog payLog, String provinceCode);

    /**
     * 新增代收费日志
     *
     * @param clPayLogs
     * @param provinceCode
     */
    void insertCLPayLog(List<CLPayLog> clPayLogs, String provinceCode);

    /**
     * 新增收费其他信息日志
     *
     * @param payOtherLog
     * @param provinceCode
     */
    int insertPayOtherLog(PayOtherLog payOtherLog, String provinceCode);

}
