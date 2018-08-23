package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.DepositMQInfo;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
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
     * @param feeAccountDeposits
     * @return
     */
    List<DepositMQInfo> genDepositMQInfo(List<FeeAccountDeposit> feeAccountDeposits);


    /**
     * 根据交易信息生成账本
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    FeeAccountDeposit genAcctDeposit(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo);

    /**
     * 根据账本类型生成账本
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     * @param depositCode
     * @return
     */
    FeeAccountDeposit genAcctDepositByDepositCode(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, int depositCode);


    FeeAccountDeposit genAcctDepositByTransFer(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, FeeAccountDeposit transOutDeposit);

    /**
     * 更新账本表往月欠费和开张账期
     *
     * @param feeAccountDeposits
     * @param writeSnapLog
     * @param maxAcctCycleId
     */
    void updateAcctDepositOweFee(List<FeeAccountDeposit> feeAccountDeposits, FeeWriteSnapLog writeSnapLog, int maxAcctCycleId);

    /**
     * 缴费后更新账本信息
     *
     * @param feeAccountDeposits
     * @param writeOffRuleInfo
     * @param provinceCode
     */
    void updateAcctDepositInfo(List<FeeAccountDeposit> feeAccountDeposits, WriteOffRuleInfo writeOffRuleInfo, String provinceCode);

    void updateDepositInfo(List<FeeAccountDeposit> feeAccountDeposits, long acctOweFee, String sysdate, int maxAcctCycleId);

}
