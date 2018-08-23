package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.WriteOffLog;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * TF_B_WRITEOFFLOG表的增查改操作
 *
 * @author Wangkh
 */
public interface WriteOffLogDao extends IBaseDao {
    /**
     * 新增销账日志数据
     *
     * @param writeOffLogs
     * @param provinceCode
     */
    void insertWriteOffLog(List<WriteOffLog> writeOffLogs, String provinceCode);
}
