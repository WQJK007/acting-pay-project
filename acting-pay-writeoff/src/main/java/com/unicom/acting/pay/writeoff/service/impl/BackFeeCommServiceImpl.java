package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.fee.calc.service.DepositCalcService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.domain.BackFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TradeDepositInfo;
import com.unicom.acting.pay.writeoff.service.AcctDepositPayService;
import com.unicom.acting.pay.writeoff.service.BackFeeCommService;
import com.unicom.acting.pay.writeoff.service.TradeCommService;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackFeeCommServiceImpl implements BackFeeCommService {
    private static final Logger logger = LoggerFactory.getLogger(BackFeeCommServiceImpl.class);
    @Autowired
    private DepositCalcService depositCalcService;
    @Autowired
    private CommParaFeeService commParaFeeService;
    @Autowired
    private TradeCommService tradeCommService;
    @Autowired
    private AcctDepositPayService acctDepositPayService;

    @Override
    public void setBackFee(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (StringUtils.isEmpty(backFeeCommInfoIn.getBackType())) {
            throw new SkyArkException("清退类型没有传入");
        }

        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        if (ActingPayPubDef.BACK_BY_ALLMONEY.equals(backFeeCommInfoIn.getBackType())) {
            tradeDepositInfos = getCanBackFee(tradeCommInfo, backFeeCommInfoIn.getTradeFee(), backFeeCommInfoIn.getDecuctOwefeeTag());
        } else if (ActingPayPubDef.BACK_BY_DEPOSITCODE.equals(backFeeCommInfoIn.getBackType())) {
            if (StringUtil.isEmptyCheckNullStr(backFeeCommInfoIn.getBackDepositCode())) {
                throw new SkyArkException("指定清退账本科目类型没有传入");
            }
            tradeDepositInfos = getCanBackFee(backFeeCommInfoIn, tradeCommInfo);
        } else if (ActingPayPubDef.BACK_BY_ACCTBALANCEID.equals(backFeeCommInfoIn.getBackType())) {
            tradeDepositInfos = getCanBackFee(tradeCommInfo, backFeeCommInfoIn.getTradeDepositInfos(),
                    backFeeCommInfoIn.getForceBackAcctTag(), backFeeCommInfoIn.getDecuctOwefeeTag());
        } else {
            throw new SkyArkException("不支持的清退类型 BACK_TYPE = " + backFeeCommInfoIn.getBackType());
        }

        if (CollectionUtils.isEmpty(tradeDepositInfos)) {
            throw new SkyArkException("没有可清退的帐本!");
        }

        List<FeeAccountDeposit> actDeposits = tradeCommInfo.getFeeAccountDeposits();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        long totalBackMoney = 0;
        Map depositInvMap = new HashMap();
        for (TradeDepositInfo tradeDepositInfo : tradeDepositInfos) {
            String acctBalanceId = tradeDepositInfo.getAcctBalanceId();
            long backMoney = tradeDepositInfo.getMoney();
            FeeAccountDeposit tmpDeposit = depositCalcService.getAcctDepositByAcctBalanceId(actDeposits, acctBalanceId);
            tmpDeposit.setRecvFee(-backMoney);
            if (0 != tmpDeposit.getRecvFee()) {
                //赠款
                if (2 == writeOffRuleInfo.depositTypeCode(tmpDeposit.getDepositCode())
                        || 3 == writeOffRuleInfo.depositTypeCode(tmpDeposit.getDepositCode())) {
                    backFeeCommInfoIn.setPayFeeModeCode(4);
                }
            }

            totalBackMoney += tmpDeposit.getRecvFee();

            if ('1' == backFeeCommInfoIn.getDecuctInvTag()) {
                long leftInvFee = tmpDeposit.getInvoiceFee() - tmpDeposit.getPrintFee();
                if (leftInvFee > backMoney) {
                    tmpDeposit.setInvoiceFee(tmpDeposit.getInvoiceFee() - backMoney);
                    depositInvMap.put(tmpDeposit.getAcctBalanceId(), -backMoney);
                } else {
                    if (leftInvFee > 0) {
                        tmpDeposit.setInvoiceFee(tmpDeposit.getPrintFee());
                        depositInvMap.put(tmpDeposit.getAcctBalanceId(), -tmpDeposit.getPrintFee());
                    }
                }
                tradeCommInfo.setInvoiceFeeMap(depositInvMap);
            }
        }

        backFeeCommInfoIn.setTradeFee(totalBackMoney);
        backFeeCommInfoIn.setPaymentOp(16001);
        backFeeCommInfoIn.setBackDepositInfos(tradeDepositInfos);
    }


    @Override
    public void genBackFeeDBInfo(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        //地市销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        //生成交费日志信息
        PayLog payLog = tradeCommService.genPayLog(backFeeCommInfoIn, tradeCommInfo);
        tradeCommResultInfo.setPayLog(payLog);

        //生成缴费日志MQ消息
        tradeCommResultInfo.setPayLogMQInfo(tradeCommService.genPayLogMQInfo(payLog));

        //更新交费快照信息
        WriteSnapLog writeSnapLog = tradeCommService.genWriteSnapLog(backFeeCommInfoIn,
                tradeCommInfo.getFeeWriteSnapLog(), payLog, writeOffRuleInfo.getCurCycle().getCycleId());
        tradeCommResultInfo.setWriteSnapLog(writeSnapLog);

        //生成销账日志数据
        List<WriteOffLog> writeOffLogs = tradeCommService.genWriteOffLog(tradeCommInfo.getFeeWriteOffLogs(), payLog, writeOffRuleInfo);
        //销账日志入库
        tradeCommResultInfo.setWriteOffLogs(writeOffLogs);


        //生成取款日志数据
        List<AccessLog> accessLogs = tradeCommService.genAccessLog(tradeCommInfo, payLog, writeOffLogs);
        tradeCommResultInfo.setAccessLogs(accessLogs);

        //生成取款日志MQ信息
        if (!CollectionUtils.isEmpty(accessLogs)) {
            tradeCommResultInfo.setAccessLogMQInfos(tradeCommService.genAccessLogMQInfo(accessLogs));
        }

        //生成代收费日志
        List<CLPayLog> clPayLogs = tradeCommService.genCLPaylog(writeOffLogs, payLog, backFeeCommInfoIn.getHeaderGray());
        tradeCommResultInfo.setClPayLogs(clPayLogs);

        //更新存折入库信息
        List<FeeAccountDeposit> deposits = tradeCommInfo.getFeeAccountDeposits();
        acctDepositPayService.updateDepositInfo(deposits, writeSnapLog.getAllNewBOweFee(),
                writeOffRuleInfo.getSysdate(), writeOffRuleInfo.getMaxAcctCycle().getCycleId());
        tradeCommResultInfo.setDepositMQInfos(acctDepositPayService.genDepositMQInfo(deposits));
    }


    /**
     * 针对总额清退时生成账户可清退账本明细信息
     *
     * @param tradeCommInfo
     * @param backFee         清退总费用
     * @param decuctOweFeeTag 扣减欠费标识
     * @return
     */
    private List<TradeDepositInfo> getCanBackFee(TradeCommInfo tradeCommInfo, long backFee, char decuctOweFeeTag) {
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        String sysdate = writeOffRuleInfo.getSysdate();
        List<FeeAccountDeposit> depositList = tradeCommInfo.getFeeAccountDeposits();
        if (CollectionUtils.isEmpty(depositList)) {
            return null;
        }

        //可清退账本信息
        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        for (FeeAccountDeposit deposit : depositList) {
            //冻结状态或已失效的账本不允许清退
            if ('0' != deposit.getValidTag() || sysdate.compareTo(deposit.getStartDate()) < 0
                    || sysdate.compareTo(deposit.getEndDate()) > 0) {
                continue;
            }

            //不可清退账本不允许清退 如果账本优先级不存在和C++逻辑不一样
            if (!writeOffRuleInfo.isBackDeposit(deposit.getDepositCode())) {
                continue;
            }

            //账本非冻结总余额
            long totalMoney = deposit.getMoney() - deposit.getFreezeFee();

            //账本没有非冻结总余额不允许清退
            if (totalMoney <= 0) {
                continue;
            }

            //账本销账后总余额
            long leftMoney = totalMoney - deposit.getImpFee() - deposit.getUseRecvFee();
            //账本实际可清退金额
            long depositBackMoney = 0;
            if ('1' == decuctOweFeeTag) {
                //不扣减欠费
                if (backFee > totalMoney) {
                    depositBackMoney = totalMoney;
                    backFee -= totalMoney;
                } else if (backFee > 0) {
                    depositBackMoney = backFee;
                    backFee = 0;
                }
            } else {
                //扣减欠费
                //账本无销账后总余额不允许清退
                if (leftMoney <= 0) {
                    continue;
                }
                if (backFee > leftMoney) {
                    depositBackMoney = leftMoney;
                    backFee -= leftMoney;
                } else if (backFee > 0) {
                    depositBackMoney = backFee;
                    backFee = 0;
                }
            }

            //设置可清退的可清退信息
            TradeDepositInfo tradeDepositInfo = new TradeDepositInfo();
            tradeDepositInfo.setAcctBalanceId(deposit.getAcctBalanceId());
            tradeDepositInfo.setMoney(depositBackMoney);
            tradeDepositInfos.add(tradeDepositInfo);
        }
        return tradeDepositInfos;
    }


    /**
     * 指定账本科目类型清退生成账户可清退账本明细信息
     *
     * @param backFeeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private List<TradeDepositInfo> getCanBackFee(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (StringUtils.isEmpty(backFeeCommInfoIn.getBackDepositCode())) {
            return null;
        }
        List<FeeAccountDeposit> depositList = tradeCommInfo.getFeeAccountDeposits();
        if (CollectionUtils.isEmpty(depositList)) {
            return null;
        }

        Account account = tradeCommInfo.getAccount();

        //获取清退账本科目编码
        CommPara commPara = commParaFeeService.getCommparaByLike(backFeeCommInfoIn.getBackDepositCode(),
                account.getProvinceCode(), account.getEparchyCode());
        if (commPara == null || StringUtils.isEmpty(commPara.getParaCode1())) {
            throw new SkyArkException("没有配置" + backFeeCommInfoIn.getBackDepositCode() + "参数");
        }

        List<FeeAccountDeposit> backDepositList = depositCalcService.getAcctDepositsByDepositCode(
                depositList, Integer.parseInt(commPara.getParaCode1()));
        if (CollectionUtils.isEmpty(backDepositList)) {
            throw new SkyArkException("没有指定的帐本，不能办理此业务" + backFeeCommInfoIn.getBackDepositCode());
        }

        //强制请退
        backFeeCommInfoIn.setForceBackAcctTag('1');
        long backFee = backFeeCommInfoIn.getTradeFee();
        List<TradeDepositInfo> tradeDepositInfos = new ArrayList<>();
        for (FeeAccountDeposit deposit : depositList) {
            //如果账本没有可用余额不允许清退
            if (deposit.getMoney() > 0) {
                continue;
            }
            long backDepositMoney = 0;
            if (backFee <= 0) {
                break;
            }

            if (deposit.getMoney() > backFee) {
                backDepositMoney = backFee;
            } else {
                backDepositMoney = deposit.getMoney();
            }
            backFee -= backDepositMoney;
            TradeDepositInfo tradeDepositInfo = new TradeDepositInfo();
            tradeDepositInfo.setAcctBalanceId(deposit.getAcctBalanceId());
            tradeDepositInfo.setMoney(backDepositMoney);
            tradeDepositInfos.add(tradeDepositInfo);
        }

        if (CollectionUtils.isEmpty(tradeDepositInfos)) {
            throw new SkyArkException("没有可清退的帐本");
        }

        if (backFee > 0) {
            throw new SkyArkException("没有足够金额可以清退" + backFee);
        }

        return tradeDepositInfos;
    }

    /**
     * 指定账本实例清退生成账户可清退账本明细信息
     *
     * @param tradeCommInfo
     * @param preTradeDepositInfos
     * @param forceBackAcctTag
     * @param decuctOweFeeTag
     * @return
     */
    private List<TradeDepositInfo> getCanBackFee(TradeCommInfo tradeCommInfo, List<TradeDepositInfo> preTradeDepositInfos, char forceBackAcctTag, char decuctOweFeeTag) {
        List<FeeAccountDeposit> depositList = tradeCommInfo.getFeeAccountDeposits();
        if (CollectionUtils.isEmpty(depositList)) {
            return null;
        }

        //账户实际可清退账本信息
        List<TradeDepositInfo> tmpTradeDepositInfos = new ArrayList();
        //强制指定帐本，不考虑可清退标志和结余
        if ('1' == forceBackAcctTag) {
            for (FeeAccountDeposit deposit : depositList) {
                TradeDepositInfo tradeDepositInfo = new TradeDepositInfo();
                tradeDepositInfo.setAcctBalanceId(deposit.getAcctBalanceId());
                tradeDepositInfo.setMoney(deposit.getMoney());
                tmpTradeDepositInfos.add(tradeDepositInfo);
            }
        } else {
            tmpTradeDepositInfos = getCanBackFee(tradeCommInfo, ActingPayPubDef.MAX_LIMIT_FEE, decuctOweFeeTag);
        }

        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        for (TradeDepositInfo tradeDepositInfo : preTradeDepositInfos) {
            String acctBalanceId = tradeDepositInfo.getAcctBalanceId();
            long backMoney = tradeDepositInfo.getMoney();
            int k = 0;
            for (; k < tmpTradeDepositInfos.size(); k++) {
                if (acctBalanceId.equals(tmpTradeDepositInfos.get(k).getAcctBalanceId())) {
                    break;
                }
            }
            if (k == tmpTradeDepositInfos.size()) {
                throw new SkyArkException("您选择的账本不存在或不可清退，请尝试强制清退!acctBalanceId=" + acctBalanceId);
            }

            if (backMoney > tmpTradeDepositInfos.get(k).getMoney()) {
                throw new SkyArkException("指定清退的帐本清退金额不合法!acctBalanceId=" + acctBalanceId);
            }

            logger.info("BACK MONEY = " + backMoney);

            TradeDepositInfo tradeDepositInfo1 = new TradeDepositInfo();
            tradeDepositInfo1.setAcctBalanceId(acctBalanceId);
            tradeDepositInfo1.setMoney(backMoney);
            tradeDepositInfos.add(tradeDepositInfo1);
        }
        return tradeDepositInfos;
    }


}
