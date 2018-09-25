package com.unicom.acting.pay.writeoff.dao;


import com.unicom.acting.pay.writeoff.domain.PrintInfo;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * @author ducj
 */
public interface PrintInfoActsDao extends IBaseDao {

    /**
     * 根据缴费流水和账户标识捞取打印日志
     * @param chargeId
     * @param acctId
     * @return
     */
    List<PrintInfo> getPrintInfoByChargeId(String chargeId, String acctId);
}
