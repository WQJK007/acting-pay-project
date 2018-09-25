package com.unicom.acting.pay.writeoff.dao;


import com.unicom.acting.pay.writeoff.domain.AccountDeposit;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * 账管相关数据库操作
 *
 * @author Wangkh
 */
public interface DepositActsDao extends IBaseDao {

    /**
     * 账户中心根据acctBalance和acctId获取账本可打金额
     * @param acctBalanceId
     * @param acctId
     * @return
     */
    List<AccountDeposit> getDepositCanPrintFeeByAcctBalanceIdAndAcctId(String acctBalanceId, String acctId);

}
