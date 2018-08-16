package com.unicom.acting.pay.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.AccessLog;
import com.unicom.acting.fee.domain.PayLog;
import com.unicom.acting.fee.domain.WriteOffLog;
import com.unicom.acting.fee.domain.WriteSnapLog;

import java.util.List;

/**
 * 缴费销账相关日志表CRUD操作，主要包括以下表
 * TF_B_WRITEOFFLOG，TF_B_ACCESSLOG，TF_B_WRITESNAP_LOG
 *
 * @author Wangkh
 */
public interface WriteOffLogPayDao extends IBaseDao {
    /**
     * 新增缴费日志记录
     *
     * @param payLog
     * @param provinceCode
     * @return
     */
    long insertPayLog(PayLog payLog, String provinceCode);

    /**
     * 新增销账日志数据
     *
     * @param logs
     * @param provinceCode
     */
    void insertWriteOffLog(List<WriteOffLog> logs, String provinceCode);

    /**
     * 新增存取款日志数据
     *
     * @param accessLogList
     * @param provinceCode
     */
    void insertAccessLog(List<AccessLog> accessLogList, String provinceCode);

    /**
     * 新增销账快照日志数据
     *
     * @param writeSnapLog
     * @param provinceCode
     * @return
     */
    long insertWriteSnapLog(WriteSnapLog writeSnapLog, String provinceCode);
}
