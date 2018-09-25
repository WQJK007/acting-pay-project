package com.unicom.acting.pay.writeoff.dao;


import com.unicom.acting.fee.domain.Staff;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.acting.pay.writeoff.domain.DiscntDeposit;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * 缴费相关数据库操作
 *
 * @author ducj
 */
public interface PayLogActsDao extends IBaseDao {

    /**
     * 账户中心根据外围流水和账户标识判断缴费是否存在
     * @param tradeId
     * @param acctId
     * @return
     */
    boolean ifExistOuterTradeId(String tradeId, String acctId);
}
