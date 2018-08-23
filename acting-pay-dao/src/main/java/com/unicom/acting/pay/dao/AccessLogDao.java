package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.AccessLog;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * TF_B_ACCESSLOG表的增查改操作
 *
 * @author Wangkh
 */
public interface AccessLogDao extends IBaseDao {
    /**
     * 新增存取款日志数据
     *
     * @param accessLogs
     * @param provinceCode
     */
    void insertAccessLog(List<AccessLog> accessLogs, String provinceCode);
}
