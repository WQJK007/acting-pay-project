package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.calc.service.DepositCalcService;
import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.domain.FeeWriteOffLog;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.domain.TradeDepositInfo;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommResultInfo;
import com.unicom.acting.pay.writeoff.service.TradeCommService;
import com.unicom.acting.pay.writeoff.service.TransFeeCommService;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransFeeCommServiceImpl implements TransFeeCommService {
    private static final Logger logger = LoggerFactory.getLogger(TransFeeCommServiceImpl.class);
    @Autowired
    private DepositCalcService depositCalcService;
    @Autowired
    private AcctDepositPayServiceImpl acctDepositPayService;
    @Autowired
    private TradeCommService tradeCommService;

    @Override
    public void setTransFerEnCashOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //转出账本实例标识
        String acctBalanceId = transFeeCommInfoIn.getAcctBalanceId();
        List<FeeAccountDeposit> actDeposits = tradeCommInfo.getFeeAccountDeposits();
        long transFee = transFeeCommInfoIn.getTradeFee();
        //实际转出账本
        FeeAccountDeposit transOutDeposit = depositCalcService.getAcctDepositByAcctBalanceId(actDeposits, acctBalanceId);
        if (transOutDeposit == null) {
            throw new SkyArkException("转出帐本不存在,ACCT_BALANCE_ID=" + acctBalanceId);
        }
        long totalFee = transOutDeposit.getMoney() - transOutDeposit.getFreezeFee();
        if (totalFee <= 0 || totalFee < transFee) {
            throw new SkyArkException("没有足够的金额可转!acctBalanceId=" + acctBalanceId);
        }
        transOutDeposit.setRecvFee(-transFee);
        if (transFee != 0) {
            //赠款
            if ('2' == transOutDeposit.getDepositTypeCode() || '3' == transOutDeposit.getDepositTypeCode()) {
                transFeeCommInfoIn.setPayFeeModeCode(4);
            }

            //更新可打金额
            long leftInvFee = transOutDeposit.getInvoiceFee() - transOutDeposit.getPrintFee();
            Map invoiceFeeMap = new HashMap();
            if (leftInvFee > transFee) {
                transOutDeposit.setInvoiceFee(transOutDeposit.getInvoiceFee() - transFee);
                invoiceFeeMap.put(transOutDeposit.getAcctBalanceId(), transFee);
            } else {
                transOutDeposit.setInvoiceFee(transOutDeposit.getPrintFee());
                invoiceFeeMap.put(transOutDeposit.getAcctBalanceId(), leftInvFee);
            }
            tradeCommInfo.setInvoiceFeeMap(invoiceFeeMap);
        }
        depositCalcService.accountDepositUpAndSort(tradeCommInfo.getWriteOffRuleInfo(), actDeposits, transOutDeposit);
        transFeeCommInfoIn.setAcctBalanceId(acctBalanceId);
        transFeeCommInfoIn.setTradeFee(-transFee);
    }

    @Override
    public void setTransFerEnCashIn(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //转出账本实例标识
        String acctBalanceId = transFeeCommInfoIn.getAcctBalanceId();
        List<FeeAccountDeposit> actDeposits = tradeCommInfo.getFeeAccountDeposits();
        long transFee = transFeeCommInfoIn.getTradeFee();
        //实际转出账本
        FeeAccountDeposit transOutDeposit = depositCalcService.getAcctDepositByAcctBalanceId(actDeposits, acctBalanceId);
        if (transOutDeposit.getDepositCode() == transFeeCommInfoIn.getDepositCode()) {
            throw new SkyArkException("转出帐本科目不能和目的帐本科目相同!"
                    + "TransInDepositCode = " + transFeeCommInfoIn.getDepositCode()
                    + ",TransOutDepositCode = " + transOutDeposit.getDepositCode());
        }

        //获取转入账本
        FeeAccountDeposit transInDeposit = acctDepositPayService.genAcctDepositByTransFer(transFeeCommInfoIn, tradeCommInfo, transOutDeposit);
        //赠款
        if (transFee != 0) {
            if ('2' == transOutDeposit.getDepositTypeCode() || '3' == transOutDeposit.getDepositTypeCode()) {
                transFeeCommInfoIn.setPayFeeModeCode(4);
            }
        }

        depositCalcService.accountDepositUpAndSort(tradeCommInfo.getWriteOffRuleInfo(), actDeposits, transInDeposit);
        transFeeCommInfoIn.setAcctBalanceId(transInDeposit.getAcctBalanceId());

    }

    @Override
    public void setTransFeeOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //转出账本不存在不做任何处理
        if (CollectionUtils.isEmpty(transFeeCommInfoIn.getTradeDepositInfos())) {
            return;
        }

        List<TradeDepositInfo> tradeDepositInfos = getCanTransferFee(transFeeCommInfoIn, tradeCommInfo, ActingPayPubDef.MAX_LIMIT_FEE);
        if (CollectionUtils.isEmpty(tradeDepositInfos)) {
            throw new SkyArkException("该账户无账本可转");
        }

        List<FeeAccountDeposit> actDeposits = tradeCommInfo.getFeeAccountDeposits();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        long tradeFee = 0;
        for (TradeDepositInfo transOutDeposit : transFeeCommInfoIn.getTradeDepositInfos()) {
            int k = 0;
            for (; k < tradeDepositInfos.size(); k++) {
                if (transOutDeposit.getAcctBalanceId().equals(tradeDepositInfos.get(k).getAcctBalanceId())) {
                    break;
                }
            }
            if (k == tradeDepositInfos.size()) {
                throw new SkyArkException("帐本不允许转帐或者不存在!acctBalanceId = " + transOutDeposit.getAcctBalanceId());
            }


            //过户转账以实际可转金额为准
            if ('1' == transFeeCommInfoIn.getChgAcctTag()) {
                transOutDeposit.setMoney(tradeDepositInfos.get(k).getMoney());
            } else {
                //非过户转账校验实际转账额度
                if (transOutDeposit.getMoney() > tradeDepositInfos.get(k).getMoney()) {
                    throw new SkyArkException("转出的帐本金额不合法!transMoney = " + transOutDeposit.getMoney()
                            + ",acctBalanceId = " + transOutDeposit.getAcctBalanceId());
                }
            }

            FeeAccountDeposit deposit = depositCalcService.getAcctDepositByAcctBalanceId(
                    actDeposits, transOutDeposit.getAcctBalanceId());
            deposit.setRecvFee(-transOutDeposit.getMoney());
            tradeFee += deposit.getRecvFee();

            if (deposit.getRecvFee() != 0) {
                if ('2' == deposit.getDepositTypeCode()
                        || '3' == deposit.getDepositTypeCode()) {
                    //赠款
                    transFeeCommInfoIn.setPayFeeModeCode(4);
                }
                //更新可打金额
                long leftInvFee = deposit.getInvoiceFee() - deposit.getPrintFee();
                Map invoiceFeeMap = new HashMap();
                if (leftInvFee > transOutDeposit.getMoney()) {
                    deposit.setInvoiceFee(deposit.getInvoiceFee() - transOutDeposit.getMoney());
                    invoiceFeeMap.put(deposit.getAcctBalanceId(), transOutDeposit.getMoney());
                } else {
                    deposit.setInvoiceFee(deposit.getPrintFee());
                    invoiceFeeMap.put(deposit.getAcctBalanceId(), leftInvFee);
                }
                tradeCommInfo.setInvoiceFeeMap(invoiceFeeMap);
            }
            depositCalcService.accountDepositUpAndSort(writeOffRuleInfo, actDeposits, deposit);
        }
        //更新实际转账金额
        transFeeCommInfoIn.setTradeFee(tradeFee);
    }

    @Override
    public void setTransFeeIn(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (CollectionUtils.isEmpty(transFeeCommInfoIn.getTransOutDedposits())) {
            return;
        }

        List<FeeAccountDeposit> actDeposits = tradeCommInfo.getFeeAccountDeposits();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        List<TradeDepositInfo> desDeposits = new ArrayList();
        long tradeFee = 0;
        for (TradeDepositInfo depositInfo : transFeeCommInfoIn.getTradeDepositInfos()) {
            FeeAccountDeposit transOutDeposit = depositCalcService.getAcctDepositByAcctBalanceId(
                    actDeposits, depositInfo.getAcctBalanceId());
            FeeAccountDeposit desDeposit = acctDepositPayService.genAcctDepositByTransFer(transFeeCommInfoIn, tradeCommInfo, transOutDeposit);
            tradeFee += depositInfo.getMoney();
            if (depositInfo.getMoney() != 0) {
                if ('2' == desDeposit.getDepositTypeCode() || '3' == desDeposit.getDepositTypeCode()) {
                    transFeeCommInfoIn.setPayFeeModeCode(4);
                }
            }

            if (!CollectionUtils.isEmpty(desDeposits)) {
                for (TradeDepositInfo tmpDepositInfo : desDeposits) {
                    if (tmpDepositInfo.getAcctBalanceId().equals(desDeposit.getAcctBalanceId())) {
                        tmpDepositInfo.setMoney(tmpDepositInfo.getMoney() + desDeposit.getMoney());
                    } else {
                        TradeDepositInfo desDepositInfo = new TradeDepositInfo();
                        desDepositInfo.setAcctBalanceId(desDeposit.getAcctBalanceId());
                        desDepositInfo.setMoney(desDeposit.getMoney());
                        desDeposits.add(desDepositInfo);
                    }
                }
            } else {
                TradeDepositInfo desDepositInfo = new TradeDepositInfo();
                desDepositInfo.setAcctBalanceId(desDeposit.getAcctBalanceId());
                desDepositInfo.setMoney(desDeposit.getMoney());
                desDeposits.add(desDepositInfo);
            }
            depositCalcService.accountDepositUpAndSort(writeOffRuleInfo, actDeposits, desDeposit);
        }

        transFeeCommInfoIn.getTradeDepositInfos().addAll(desDeposits);
        transFeeCommInfoIn.setTradeFee(tradeFee);
    }

    @Override
    public void genTransFeeDBInfo(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo) {
        //地市销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        //生成交费日志信息
        PayLog payLog = tradeCommService.genPayLog(transFeeCommInfoIn, tradeCommInfo);
        if (CollectionUtils.isEmpty(transFeeCommResultInfo.getPayLogs())) {
            //初始化数组大小为1
            transFeeCommResultInfo.setPayLogs(new ArrayList(1));
        }
        transFeeCommResultInfo.getPayLogs().add(payLog);

        //生成缴费日志MQ消息
        if (CollectionUtils.isEmpty(transFeeCommResultInfo.getPayLogMQInfos())) {
            //初始化数组大小为1
            transFeeCommResultInfo.setPayLogMQInfos(new ArrayList(1));
        }
        transFeeCommResultInfo.getPayLogMQInfos().add(tradeCommService.genPayLogMQInfo(payLog));

        //更新交费快照信息
        WriteSnapLog writeSnapLog = tradeCommService.genWriteSnapLog(transFeeCommInfoIn,
                tradeCommInfo.getFeeWriteSnapLog(), payLog, writeOffRuleInfo.getCurCycle().getCycleId());
        if (CollectionUtils.isEmpty(transFeeCommResultInfo.getWriteSnapLogs())) {
            //初始化数组大小为1
            transFeeCommResultInfo.setWriteSnapLogs(new ArrayList(1));
        }
        transFeeCommResultInfo.getWriteSnapLogs().add(writeSnapLog);

        //生成销账日志数据
        List<WriteOffLog> writeOffLogs = tradeCommService.genWriteOffLog(tradeCommInfo.getFeeWriteOffLogs(), payLog, writeOffRuleInfo);
        if (CollectionUtils.isEmpty(transFeeCommResultInfo.getWriteOffLogs())) {
            transFeeCommResultInfo.setTransOutWriteOffLogs(writeOffLogs);
        } else {
            transFeeCommResultInfo.getWriteOffLogs().addAll(writeOffLogs);
        }


        //生成取款日志数据
        List<AccessLog> accessLogs = tradeCommService.genAccessLog(tradeCommInfo, payLog, writeOffLogs);
        if (CollectionUtils.isEmpty(transFeeCommResultInfo.getAccessLogs())) {
            transFeeCommResultInfo.setAccessLogs(accessLogs);
        } else {
            transFeeCommResultInfo.getAccessLogs().addAll(accessLogs);
        }

        //生成取款日志MQ信息
        if (CollectionUtils.isEmpty(transFeeCommResultInfo.getAccessLogMQInfos())) {
            transFeeCommResultInfo.setAccessLogMQInfos(tradeCommService.genAccessLogMQInfo(accessLogs));
        } else {
            transFeeCommResultInfo.getAccessLogMQInfos().addAll(tradeCommService.genAccessLogMQInfo(accessLogs));
        }

        //生成代收费日志
        List<CLPayLog> clPayLogs = tradeCommService.genCLPaylog(writeOffLogs, payLog, transFeeCommInfoIn.getHeaderGray());
        if (CollectionUtils.isEmpty(transFeeCommResultInfo.getClPayLogs())) {
            transFeeCommResultInfo.setTransOutCLPayLogs(clPayLogs);
        } else {
            transFeeCommResultInfo.getClPayLogs().addAll(clPayLogs);
        }

        //更新存折入库信息
        List<FeeAccountDeposit> deposits = tradeCommInfo.getFeeAccountDeposits();
        acctDepositPayService.updateDepositInfo(deposits, writeSnapLog.getAllNewBOweFee(),
                writeOffRuleInfo.getSysdate(), writeOffRuleInfo.getMaxAcctCycle().getCycleId());
        transFeeCommResultInfo.setDepositMQInfos(acctDepositPayService.genDepositMQInfo(deposits));

        //存在关联信息需要生成关联交易日志
        if (!StringUtil.isEmptyCheckNullStr(transFeeCommInfoIn.getRelChargeId())) {
            ChargeRelation chargeRelation = new ChargeRelation();
            chargeRelation.setAcctId(payLog.getAcctId());
            chargeRelation.setEparchyCode(payLog.getEparchyCode());
            chargeRelation.setProvinceCode(payLog.getProvinceCode());
            chargeRelation.setId(payLog.getChargeId());
            chargeRelation.setOperateId1(payLog.getChargeId());
            chargeRelation.setOperateId2(transFeeCommInfoIn.getRelChargeId());
            if (transFeeCommInfoIn.getPaymentOp() == 16005) {
                chargeRelation.setOperateType("2");
            }

            chargeRelation.setDebutyCode("");
            chargeRelation.setOperateTime(payLog.getRecvTime());
            chargeRelation.setOperateStaffId(payLog.getRecvStaffId());
            chargeRelation.setOperateDepartId(payLog.getRecvDepartId());
            chargeRelation.setOperateEparchyCode(payLog.getRecvEparchyCode());
            chargeRelation.setOperateCityCode(payLog.getRecvCityCode());
            transFeeCommResultInfo.setChargeRelation(chargeRelation);
        }
    }

    /**
     * 获取可转账本信息
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     * @param maxTransferFee
     * @return
     */
    private List<TradeDepositInfo> getCanTransferFee(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, long maxTransferFee) {
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //账本销当月费用金额
        Map<String, Long> writeOffRealFee = new HashMap();
        List<FeeWriteOffLog> writeOffLogs = tradeCommInfo.getFeeWriteOffLogs();
        if (!CollectionUtils.isEmpty(writeOffLogs)) {
            for (FeeWriteOffLog writeOffLog : writeOffLogs) {
                if (writeOffLog.getCycleId() == writeOffRuleInfo.getCurCycle().getCycleId()) {
                    if (!writeOffRealFee.containsKey(writeOffLog.getAcctBalanceId())) {
                        writeOffRealFee.put(writeOffLog.getAcctBalanceId(), writeOffLog.getWriteoffFee());
                    } else {
                        long writeOffFee = writeOffRealFee.get(writeOffLog.getAcctBalanceId()) + writeOffLog.getWriteoffFee();
                        writeOffRealFee.put(writeOffLog.getAcctBalanceId(), writeOffFee);
                    }
                }
            }
        }

        List<FeeAccountDeposit> actDeposits = tradeCommInfo.getFeeAccountDeposits();
        if (CollectionUtils.isEmpty(actDeposits)) {
            return null;
        }
        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        for (FeeAccountDeposit deposit : actDeposits) {
            //冻结或失效账本不做转出处理
            if (deposit.getValidTag() != 1
                    || writeOffRuleInfo.getSysdate().compareTo(deposit.getEndDate()) > 0) {
                continue;
            }

            //过户转账不校验账本可转属性 余额转账需要校验可转属性
            if ('1' != transFeeCommInfoIn.getChgAcctTag()
                    && !writeOffRuleInfo.isTransDeposit(deposit.getDepositCode())) {
                continue;
            }

            long totalMoney = deposit.getMoney() - deposit.getFreezeFee();
            long leftMoney = totalMoney - deposit.getImpFee() - deposit.getUseRecvFee();
            long transFee = 0;

            //不扣减欠费但必须扣减截止到上月的欠费
            if (transFeeCommInfoIn.getDecuctOwefeeTag() == '1'
                    && !CollectionUtils.isEmpty(writeOffRealFee)
                    && writeOffRealFee.containsKey(deposit.getAcctBalanceId())) {
                leftMoney += writeOffRealFee.get(deposit.getAcctBalanceId());
            }

            if (leftMoney < 0) {
                continue;
            }

            if (maxTransferFee > leftMoney) {
                transFee = leftMoney;
                maxTransferFee -= leftMoney;
            } else if (maxTransferFee > 0) {
                transFee = maxTransferFee;
                maxTransferFee = 0;
            }

            if (transFee >= 0) {
                TradeDepositInfo tradeDepositInfo = new TradeDepositInfo();
                tradeDepositInfo.setAcctBalanceId(deposit.getAcctBalanceId());
                tradeDepositInfo.setMoney(transFee);
                tradeDepositInfos.add(tradeDepositInfo);
            }
        }
        return tradeDepositInfos;
    }

}
