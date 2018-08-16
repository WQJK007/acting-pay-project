package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.TradeCommInfoIn;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.acting.fee.domain.WriteSnapLog;
import com.unicom.acting.pay.domain.DepositMQInfo;
import com.unicom.acts.pay.domain.AccountDeposit;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 账本表和账本销账关系表资源操作
 *
 * @author Administrators
 */
public interface AcctDepositPayService extends IBaseService {

    /**
     * 根据账本信息生成DepositMQ对象信息
     *
     * @param accountDepositList
     * @return
     */
    List<DepositMQInfo> genDepositMQInfo(List<AccountDeposit> accountDepositList);


    /**
     * 根据交易信息生成账本
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    AccountDeposit genAcctDeposit(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 根据账本类型生成账本
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @param depositCode
     * @return
     */
    AccountDeposit genAcctDepositByDepositCode(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, int depositCode);


    AccountDeposit genAcctDepositByTransFer(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, AccountDeposit transOutDeposit);

    /**
     * 更新账本表往月欠费和开张账期
     *
     * @param accountDeposits
     * @param writeSnapLog
     * @param maxAcctCycleId
     */
    void updateAcctDepositOweFee(List<AccountDeposit> accountDeposits, WriteSnapLog writeSnapLog, int maxAcctCycleId);

    /**
     * 缴费后更新账本信息
     *
     * @param accountDeposits
     * @param writeOffRuleInfo
     * @param provinceCode
     */
    void updateAcctDepositInfo(List<AccountDeposit> accountDeposits, WriteOffRuleInfo writeOffRuleInfo, String provinceCode);

    void updateDepositInfo(List<AccountDeposit> accountDeposits, long acctOweFee, String sysdate, int maxAcctCycleId);

}
