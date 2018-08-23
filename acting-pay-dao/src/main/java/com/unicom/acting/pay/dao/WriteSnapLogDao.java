package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.WriteSnapLog;
import com.unicom.skyark.component.dao.IBaseDao;

/**
 * TF_B_WRITESNAP_LOG表增查改操作
 *
 * @author Wangkh
 */
public interface WriteSnapLogDao extends IBaseDao {
    /**
     * 新增销账快照日志数据
     *
     * @param writeSnapLog
     * @param provinceCode
     * @return
     */
    int insertWriteSnapLog(WriteSnapLog writeSnapLog, String provinceCode);
}
