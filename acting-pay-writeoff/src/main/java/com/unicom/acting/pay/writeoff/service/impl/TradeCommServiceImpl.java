package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.ActPayPubDef;
import com.unicom.acting.pay.domain.PayLogDmnMQInfo;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.acting.pay.writeoff.domain.TradeDepositInfo;
import com.unicom.acting.pay.writeoff.domain.BackFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.calc.service.DepositCalcService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.writeoff.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 销账相关公共方法
 *
 * @author wangkh
 */
@Service
public class TradeCommServiceImpl implements TradeCommService {
    private static final Logger logger = LoggerFactory.getLogger(TradeCommServiceImpl.class);
    @Autowired
    private DepositCalcService depositCalcService;
    @Autowired
    private PayDatumService payDatumService;
    @Autowired
    private AcctDepositPayService acctDepositPayService;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;
    @Autowired
    private CommParaFeeService commParaFeeService;
    @Autowired
    private TradeCommLogService tradeCommLogService;


    @Override
    public void setRecvfee(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //根据参数配置重新设置可打金额 按交费金额增加可打金额
        CommPara rCommPara = writeOffRuleInfo.getCommpara("ASM_ADD_INVOICE_FEE");
        if (rCommPara != null && !StringUtil.isEmptyCheckNullStr(rCommPara.getParaCode1())
                && "1".equals(rCommPara.getParaCode1())
                && !StringUtil.isEmptyCheckNullStr(rCommPara.getParaCode2())) {
            String channelPayment = "|" + recvFeeCommInfoIn.getChannelId() + ":" + recvFeeCommInfoIn.getPaymentId() + "|";
            if (rCommPara.getParaCode2().contains(channelPayment)) {
                recvFeeCommInfoIn.setInvoiceTag("1");
            }
        }
        FeeAccountDeposit actDeposit = null;
        if (recvFeeCommInfoIn.isDepositRecv()) {
            if (tradeCommInfo.isSpecialCycleStatus()) {
                throw new SkyArkException("月结销账期间不允指定帐本缴费!");
            }

            String acctBalanceId = recvFeeCommInfoIn.getAcctBalanceId();
            int depositCode = recvFeeCommInfoIn.getDepositCode();
            if (!StringUtil.isEmptyCheckNullStr(acctBalanceId) && acctBalanceId.length() > 4) {
                actDeposit = depositCalcService.getAcctDepositByAcctBalanceId(tradeCommInfo.getFeeAccountDeposits(), acctBalanceId);
                actDeposit.setRecvFee(recvFeeCommInfoIn.getTradeFee());
                actDeposit.setIfInAccesslog('1');
            } else if (depositCode >= 0) {
                actDeposit = acctDepositPayService.genAcctDepositByDepositCode(recvFeeCommInfoIn, tradeCommInfo, depositCode);
            } else {
                throw new SkyArkException("指定帐本缴费必须指定帐本标识或者帐本科目!");
            }
        } else {
            actDeposit = acctDepositPayService.genAcctDeposit(recvFeeCommInfoIn, tradeCommInfo);
        }

        //更新账本列表
        depositCalcService.accountDepositUpAndSort(tradeCommInfo.getWriteOffRuleInfo(), tradeCommInfo.getFeeAccountDeposits(), actDeposit);

        //同步在线信控时使用
        recvFeeCommInfoIn.setAcctBalanceId(actDeposit.getAcctBalanceId());
        //生成缴费日志时使用
        recvFeeCommInfoIn.setPaymentOp(16000);
    }

    @Override
    public void setBackFee(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (StringUtils.isEmpty(backFeeCommInfoIn.getBackType())) {
            throw new SkyArkException("清退类型没有传入");
        }

        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        if (ActPayPubDef.BACK_BY_ALLMONEY.equals(backFeeCommInfoIn.getBackType())) {
            tradeDepositInfos = getCanBackFee(tradeCommInfo, backFeeCommInfoIn.getTradeFee(), backFeeCommInfoIn.getDecuctOwefeeTag());
        } else if ("1".equals(backFeeCommInfoIn.getBackType())) {
            if (StringUtils.isEmpty(backFeeCommInfoIn.getBackDepositType())) {
                throw new SkyArkException("指定清退账本科目类型没有传入");
            }
            tradeDepositInfos = getCanBackFee(backFeeCommInfoIn, tradeCommInfo);
        } else if ("2".equals(backFeeCommInfoIn.getBackType())) {
            tradeDepositInfos = getCanBackFee(tradeCommInfo, backFeeCommInfoIn.getTradeDepositInfos(),
                    backFeeCommInfoIn.getForceBackAcctTag(), backFeeCommInfoIn.getDecuctOwefeeTag());
        } else {
            throw new SkyArkException("不支持的清退类型 BACK_TYPE = " + backFeeCommInfoIn.getBackDepositType());
        }

        if (CollectionUtils.isEmpty(tradeDepositInfos)) {
            throw new SkyArkException("没有清退的帐本!");
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
    }

    @Override
    public void setTransFerOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //转出账本实例标识
        String acctBalanceId = transFeeCommInfoIn.getAcctBalanceId();
        List<FeeAccountDeposit> actDeposits = tradeCommInfo.getFeeAccountDeposits();
        long transFee = transFeeCommInfoIn.getTradeFee();
        //实际转出账本
        FeeAccountDeposit transOutDeposit = depositCalcService.getAcctDepositByAcctBalanceId(actDeposits, acctBalanceId);
        long totalFee = transOutDeposit.getMoney() - transOutDeposit.getFreezeFee();
        if (totalFee <= 0 || totalFee < transFee) {
            throw new SkyArkException("没有足够的金额可转!acctBalanceId=" + acctBalanceId);
        }
        transOutDeposit.setRecvFee(-transFee);
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        if (transFee != 0) {
            if ('2' == writeOffRuleInfo.depositTypeCode(transOutDeposit.getDepositCode())
                    || '3' == writeOffRuleInfo.depositTypeCode(transOutDeposit.getDepositCode())) {
                //赠款
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
        transFeeCommInfoIn.setTradeFee(-transFee);
    }

    @Override
    public void setTransFerIn(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
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
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        if (transFee != 0) {
            if ('2' == writeOffRuleInfo.depositTypeCode(transInDeposit.getDepositCode())
                    || '3' == writeOffRuleInfo.depositTypeCode(transInDeposit.getDepositCode())) {
                //赠款
                transFeeCommInfoIn.setPayFeeModeCode(4);
            }
        }
        depositCalcService.accountDepositUpAndSort(writeOffRuleInfo, actDeposits, transInDeposit);
    }

    @Override
    public void setTransFeeOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //转出账本不存在不做任何处理
        if (CollectionUtils.isEmpty(transFeeCommInfoIn.getTradeDepositInfos())) {
            return;
        }

        List<TradeDepositInfo> tradeDepositInfos = getCanTransferFee(transFeeCommInfoIn, tradeCommInfo, ActPayPubDef.MAX_LIMIT_FEE);
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
                if ('2' == writeOffRuleInfo.depositTypeCode(deposit.getDepositCode())
                        || '3' == writeOffRuleInfo.depositTypeCode(deposit.getDepositCode())) {
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
                if ('2' == writeOffRuleInfo.depositTypeCode(desDeposit.getDepositCode())
                        || '3' == writeOffRuleInfo.depositTypeCode(desDeposit.getDepositCode())) {
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
    public void genInDBInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        //地市销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //生成交费日志信息
        FeePayLog payLog = genPayLog(tradeCommInfoIn, tradeCommInfo);
        tradeCommInfo.setFeePayLog(payLog);

        //抵扣或者补收期间并且对应临时表中有数据
        if (tradeCommInfo.isSpecialCycleStatus()) {
            if (payLog.getExtendTag() != '0') {
                throw new SkyArkException("月结期间不允许进行异地缴费!");
            }
            if (tradeCommInfo.isExistsTradeCheck()) {
                //涉及第三方对账只能发送MQ消息
                tradeCommResultInfo.setPayLogDmnMQInfo(genPayLogDmnMQInfo(tradeCommInfoIn, payLog));
            } else {
                FeePayLogDmn payLogDmn = genPayLogDmn(tradeCommInfoIn, payLog);
                tradeCommInfo.setFeePayLogDmn(payLogDmn);
            }
            return;
        }

        //更新交费快照信息
        FeeWriteSnapLog writeSnapLog = tradeCommInfo.getFeeWriteSnapLog();
        genWriteSnapLogInfo(tradeCommInfoIn, writeSnapLog, payLog, writeOffRuleInfo.getCurCycle().getCycleId());
        tradeCommInfo.setFeeWriteSnapLog(writeSnapLog);

        //生成销账日志数据
        List<FeeWriteOffLog> writeOffLogs = tradeCommLogService.genWriteOffLogInfo(tradeCommInfo.getFeeWriteOffLogs(), payLog, writeOffRuleInfo);
        //销账日志入库
        tradeCommInfo.setFeeWriteOffLogs(writeOffLogs);


        //生成取款日志数据
        List<FeeAccessLog> accessLogs = tradeCommLogService.genAccessLogInfo(tradeCommInfo.getAccesslogs(), payLog, CollectionUtils.isEmpty(writeOffLogs));
        tradeCommInfo.setAccesslogs(accessLogs);

        //生成取款日志MQ信息
        if (!CollectionUtils.isEmpty(accessLogs)) {
            tradeCommResultInfo.setAccessLogMQInfos(tradeCommLogService.genAccessLogMQInfo(accessLogs));
        }

        //生成代收费日志
        List<FeeCLPayLog> feeClPayLogList = genCLPaylog(writeOffLogs, payLog);
        tradeCommInfo.setFeeClPayLogs(feeClPayLogList);

        //更新存折入库信息
        List<FeeAccountDeposit> deposits = tradeCommInfo.getFeeAccountDeposits();
        acctDepositPayService.updateDepositInfo(deposits, writeSnapLog.getAllNewBOweFee(),
                writeOffRuleInfo.getSysdate(), writeOffRuleInfo.getMaxAcctCycle().getCycleId());
        tradeCommResultInfo.setDepositMQInfos(acctDepositPayService.genDepositMQInfo(deposits));

    }

    /**
     * 托收在途
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private boolean ifBillConsigning(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getFeeBills())) {
            return false;
        }

        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(PubCommParaDef.ASM_CONSIGN_CAN_RECV);
        if (commPara == null) {
            throw new SkyArkException("ASM_CONSIGN_CAN_RECV参数没有配置!");
        }

        //1000041银行代扣和托收一样的处理
        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && "0".compareTo(commPara.getParaCode1()) < 0
                && (tradeCommInfoIn.getPaymentId() != 100004
                && tradeCommInfoIn.getPaymentId() != 1000041
                && tradeCommInfoIn.getPaymentId() != 1000044)) {
            //托收在途判断
            List<FeeBill> bills = tradeCommInfo.getFeeBills();
            for (FeeBill bill : bills) {
                if ('7' == bill.getPayTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 托收账户
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private boolean ifConsignPayMode(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getFeeBills())
                || tradeCommInfo.getFeeAccount() == null) {
            return false;
        }

        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(PubCommParaDef.ASM_CONSIGN_CAN_RECV);
        if (commPara == null) {
            throw new SkyArkException("ASM_CONSIGN_CAN_RECV参数没有配置!");
        }

        //1000041银行代扣和托收一样的处理
        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && "2".equals(commPara.getParaCode1())
                && (tradeCommInfoIn.getPaymentId() != 100004
                && tradeCommInfoIn.getPaymentId() != 1000041
                && tradeCommInfoIn.getPaymentId() != 1000044)) {
            CommPara rCommPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(PubCommParaDef.ASM_CONSIGN_PAY_MODE);
            if (rCommPara == null) {
                throw new SkyArkException("ASM_CONSIGN_PAY_MODE参数没有配置!");
            }
            String[] consignPayMode = rCommPara.getParaCode1().split("\\|");
            //托收在途判断
            FeeAccount feeAccount = tradeCommInfo.getFeeAccount();
            for (String payFeeMode : consignPayMode) {
                if (payFeeMode.equals(feeAccount.getPayModeCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 代理商预打发票
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private boolean ifPrePrintInvoice(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if ("15000".equals(tradeCommInfoIn.getChannelId())) {
            return false;
        }

        //操作对象不能为空
        if (tradeCommInfo.getWriteOffRuleInfo() == null
                || CollectionUtils.isEmpty(tradeCommInfo.getFeeBills())) {
            return false;
        }
        CommPara commPara = tradeCommInfo.getWriteOffRuleInfo().getCommpara(PubCommParaDef.ASM_PRE_PRINTINVOICE_CAN_RECV);
        if (commPara == null) {
            throw new SkyArkException("ASM_PRE_PRINTINVOICE_CAN_RECV参数没有配置!");
        }

        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && "1".equals(commPara.getParaCode1())
                && !tradeCommInfoIn.getChannelId().equals(commPara.getParaCode2())) {
            //发票预打在途,(只有后台进程才能缴费)
            List<FeeBill> bills = tradeCommInfo.getFeeBills();
            for (FeeBill bill : bills) {
                if ('8' == bill.getPayTag()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 电子赠款停机状态不能赠送
     *
     * @param mainUser
     * @param paymentId
     * @param writeOffRuleInfo
     */
    private void elecPresentLimit(User mainUser, int paymentId, WriteOffRuleInfo writeOffRuleInfo) {
        String userStateCode = mainUser.getServiceStateCode();
        if (!StringUtil.isEmptyCheckNullStr(mainUser.getServiceStateCode())
                && !"0".equals(userStateCode) && !"N".equals(userStateCode)) {
            CommPara commPara1 = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_PRESENT_LIMITPAYMENT);
            if (commPara1 == null) {
                throw new SkyArkException("ASM_PRESENT_LIMITPAYMENT参数没有配置!");
            }

            //没有配置限制的储值方式不再处理
            if (StringUtil.isEmptyCheckNullStr(commPara1.getParaCode1())) {
                return;
            }

            String regexStr = "|" + paymentId + "|";
            if (commPara1.getParaCode1().contains(regexStr)) {
                throw new SkyArkException("停机状态,限制此业务!userStateCode=" + userStateCode + ",paymentId=" + paymentId);
            }
        }
    }

    /**
     * 生成缴费日志信息
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private FeePayLog genPayLog(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        FeePayLog payLog = new FeePayLog();
        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();
        User mainUser = tradeCommInfo.getMainUser();
        Staff staff = tradeCommInfo.getTradeStaff();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        payLog.setProvinceCode(feeAccount.getProvinceCode());
        payLog.setEparchyCode(feeAccount.getEparchyCode());
        payLog.setCityCode(feeAccount.getCityCode());
        payLog.setCustId(feeAccount.getCustId());
        payLog.setAcctId(feeAccount.getAcctId());
        payLog.setUserId(mainUser.getUserId());
        payLog.setSerialNumber(mainUser.getSerialNumber());
        payLog.setNetTypeCode(mainUser.getNetTypeCode());
        //帐户缴费，没有输入用户信息，需要填写帐户的网别
        if (StringUtil.isEmptyCheckNullStr(payLog.getNetTypeCode())) {
            payLog.setNetTypeCode(feeAccount.getNetTypeCode());
        }
        if (StringUtil.isEmptyCheckNullStr(payLog.getUserId())) {
            payLog.setUserId("-1");
        }
        if (StringUtil.isEmptyCheckNullStr(payLog.getSerialNumber())) {
            payLog.setSerialNumber("-1");
        }
        //外围没有传入交费流水，系统会重新生成
        if (StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getChargeId())) {
            String tmpChargeId = sysCommOperFeeService.getSequence(payLog.getEparchyCode(),
                    ActPayPubDef.SEQ_CHARGE_ID, payLog.getProvinceCode());
            tradeCommInfoIn.setChargeId(tmpChargeId);
        }

        //异地缴费参数校验,默认不是异地缴费
        boolean isExtendFee = false;
        CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_NONLOCAL_RECVFEE);
        if (commPara != null && "1".equals(commPara.getParaCode1())) {
            String channelPayment = "|" + tradeCommInfoIn.getChannelId() + ":" + tradeCommInfoIn.getPaymentId() + "|";
            if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode2())
                    && commPara.getParaCode2().contains(channelPayment)) {
                isExtendFee = true;
            }
        }

        if (isExtendFee
                && isEffectiveEparchy(staff.getEparchyCode())
                && isEffectiveEparchy(payLog.getEparchyCode())
                && !payLog.getEparchyCode().equals(staff.getEparchyCode())) {
            //以工号归属地市编码查询工号归属省份，外围传递的省份编码可能不是工号归属省份编码
            String provinceCode = commParaFeeService.getProvCodeByEparchyCode(
                    staff.getEparchyCode(), tradeCommInfoIn.getProvinceCode());
            if (StringUtil.isEmptyCheckNullStr(provinceCode)) {
                throw new SkyArkException("未找到地市编码对应的省份编码!, TRADE_EPARCHY_CODE = " + staff.getEparchyCode());
            }
            staff.setProvinceCode(provinceCode);
            if (!payLog.getProvinceCode().equals(staff.getProvinceCode())) {
                //跨省异地缴费
                payLog.setExtendTag('2');
            } else {
                //本省异地缴费
                payLog.setExtendTag('1');
            }
        } else {
            //非异地缴费
            payLog.setExtendTag('0');
        }

        payLog.setChargeId(tradeCommInfoIn.getChargeId());
        payLog.setOuterTradeId(tradeCommInfoIn.getTradeId());
        payLog.setPaymentId(tradeCommInfoIn.getPaymentId());
        payLog.setPaymentOp(tradeCommInfoIn.getPaymentOp());
        payLog.setPayFeeModeCode(tradeCommInfoIn.getPayFeeModeCode());
        payLog.setRecvTime(writeOffRuleInfo.getSysdate());
        payLog.setRecvFee(tradeCommInfoIn.getTradeFee());
        payLog.setRecvProvinceCode(staff.getProvinceCode());
        payLog.setRecvEparchyCode(staff.getEparchyCode());
        payLog.setRecvCityCode(staff.getCityCode());
        payLog.setRecvDepartId(staff.getDepartId());
        payLog.setRecvStaffId(staff.getStaffId());
        payLog.setCancelTag('0');

        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getRemark())) {
            if (tradeCommInfoIn.getRemark().length() > 150) {
                payLog.setRemark(tradeCommInfoIn.getRemark().substring(0, 149));
            } else {
                payLog.setRemark(tradeCommInfoIn.getRemark());
            }
        }

        payLog.setPaymentRuleId(writeOffRuleInfo.getPaymentDepositRuleId());
        payLog.setActionCode(0);
        payLog.setPaymentReasonCode(0);
        payLog.setChannelId(tradeCommInfoIn.getChannelId());
        payLog.setLimitMoney(tradeCommInfoIn.getLimitMoney());
        payLog.setActionEventId(null);
//        if ("02".equals(tradeCommInfoIn.getNpFlag())) {
//            payLog.setNpTag(tradeCommInfoIn.getNpFlag());
//        }
        return payLog;
    }

    /**
     * 生成账务后台工单表信息
     *
     * @param tradeCommInfoIn
     * @param payLog
     * @return
     */
    private FeePayLogDmn genPayLogDmn(TradeCommInfoIn tradeCommInfoIn, FeePayLog payLog) {
        FeePayLogDmn payLogDmn = new FeePayLogDmn();
        payLogDmn.setTradeId(payLog.getChargeId());
        payLogDmn.setTradeTypeCode(0);
        payLogDmn.setEparchyCode(payLog.getEparchyCode());
        payLogDmn.setProvinceCode(payLog.getProvinceCode());
        payLogDmn.setBatchId(payLog.getChargeId());
        payLogDmn.setChargeId(payLog.getChargeId());
        payLogDmn.setAcctId(payLog.getAcctId());
        payLogDmn.setUserId(payLog.getUserId());
        payLogDmn.setSerialNumber(payLog.getSerialNumber());
        payLogDmn.setWriteoffMode(StringUtil.firstOfString(tradeCommInfoIn.getWriteoffMode()));
        payLogDmn.setLimitMode(StringUtil.firstOfString(tradeCommInfoIn.getLimitMode()));
        payLogDmn.setChannelId(payLog.getChannelId());
        payLogDmn.setPaymentId(payLog.getPaymentId());
        payLogDmn.setPaymentOp(payLog.getPaymentOp());
        payLogDmn.setPayFeeModeCode(payLog.getPayFeeModeCode());
        payLogDmn.setRecvFee(payLog.getRecvFee());
        payLogDmn.setOuterTradeId(payLog.getOuterTradeId());
        payLogDmn.setBillStartCycleId(tradeCommInfoIn.getBillStartCycleId());
        payLogDmn.setBillEndCycleId(tradeCommInfoIn.getBillEndCycleId());
        payLogDmn.setStartDate(tradeCommInfoIn.getDepositStartDate());
        payLogDmn.setMonths(tradeCommInfoIn.getMonths());
        payLogDmn.setLimitMoney(tradeCommInfoIn.getLimitMoney());
        payLogDmn.setPaymentReasonCode(StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getReasonCode()) ? 0 : Long.parseLong(tradeCommInfoIn.getReasonCode()));
        payLogDmn.setExtendTag(payLog.getExtendTag());
        payLogDmn.setAcctBalanceId(tradeCommInfoIn.getAcctBalanceId());
        payLogDmn.setDepositCode(tradeCommInfoIn.getDepositCode());
        payLogDmn.setPrivateTag(StringUtil.firstOfString(tradeCommInfoIn.getPrivateTag()));
        payLogDmn.setRemark(tradeCommInfoIn.getRemark());
        payLogDmn.setTradeTime(payLog.getRecvTime());
        payLogDmn.setTradeStaffId(payLog.getRecvStaffId());
        payLogDmn.setTradeDepartId(payLog.getRecvDepartId());
        payLogDmn.setTradeEparchyCode(payLog.getRecvEparchyCode());
        payLogDmn.setTradeCityCode(payLog.getRecvCityCode());

        // 一卡充缴费可打发票金额放入备用字段rsrvInfo1
        if (100006 == payLog.getPaymentId()) {
            payLogDmn.setRsrvInfo1(String.valueOf(tradeCommInfoIn.getInvoiceFee()));
        }
        payLogDmn.setRelChargeId(tradeCommInfoIn.getRelChargeId());
        return payLogDmn;
    }

    /**
     * 省份代收费
     *
     * @param writeOffLogList
     * @param payLog
     * @return
     */
    private List<FeeCLPayLog> genCLPaylog(List<FeeWriteOffLog> writeOffLogList, FeePayLog payLog) {
        if (CollectionUtils.isEmpty(writeOffLogList)) {
            return Collections.EMPTY_LIST;
        }

        List<FeeCLPayLog> feeClPayLogList = new ArrayList<>();
        for (FeeWriteOffLog writeOffLog : writeOffLogList) {
            if (writeOffLog.getCanPaytag() != '4' || writeOffLog.getWriteoffFee() == 0) {
                continue;
            }
            int k = 0;
            for (; k < feeClPayLogList.size(); k++) {
                if (feeClPayLogList.get(k).getUserId().equals(writeOffLog.getUserId())) {
                    break;
                }
            }

            if (k == feeClPayLogList.size()) {
                String clPaylogId = sysCommOperFeeService.getSequence(payLog.getEparchyCode(), ActPayPubDef.SEQ_CHARGE_ID, payLog.getProvinceCode());
                FeeCLPayLog feeClPayLog = new FeeCLPayLog();
                feeClPayLog.setClPaylogId(clPaylogId);
                feeClPayLog.setProvinceCode(writeOffLog.getProvinceCode());
                feeClPayLog.setEparchyCode(writeOffLog.getEparchyCode());
                feeClPayLog.setAreaCode(writeOffLog.getAreaCode());
                feeClPayLog.setNetTypeCode(writeOffLog.getNetTypeCode());
                feeClPayLog.setAcctId(writeOffLog.getAcctId());
                feeClPayLog.setUserId(writeOffLog.getUserId());

                String oldUserId = payDatumService.getOldUserIdOf2G3G(writeOffLog.getUserId(), payLog.getProvinceCode());
                String oldAcctId = payDatumService.getOldAcctIdOf2G3G(writeOffLog.getUserId(), payLog.getProvinceCode());

                if ("".equals(oldUserId) || "".equals(oldAcctId)) {
                    throw new SkyArkException("未找到迁转前省份OLD_ACCT_ID或OLD_USER_ID!(userId =" + writeOffLog.getUserId() + ")");
                }
                feeClPayLog.setOldAcctId(oldAcctId);
                feeClPayLog.setOldUserId(oldUserId);
                feeClPayLog.setSerialNumber(writeOffLog.getSerialNumber());
                feeClPayLog.setPaymentId(payLog.getPaymentId());
                feeClPayLog.setRecvFee(writeOffLog.getWriteoffFee());
                feeClPayLog.setChargeId(payLog.getChargeId());
                feeClPayLog.setOuterTradeId(payLog.getOuterTradeId());
                feeClPayLog.setRecvTime(payLog.getRecvTime());
                feeClPayLog.setRecvStaffId(payLog.getRecvStaffId());
                feeClPayLog.setRecvDepartId(payLog.getRecvDepartId());
                feeClPayLog.setEparchyCode(payLog.getRecvEparchyCode());
                feeClPayLog.setRecvCityCode(payLog.getRecvCityCode());
                feeClPayLogList.add(feeClPayLog);
            } else {
                feeClPayLogList.get(k).setRecvFee(feeClPayLogList.get(k).getRecvFee() + writeOffLog.getWriteoffFee());
            }
        }
        return feeClPayLogList;
    }

    /**
     * 校验地市编码是否有效
     *
     * @param eparchyCode
     * @return
     */
    private boolean isEffectiveEparchy(String eparchyCode) {
        if (StringUtil.isEmptyCheckNullStr(eparchyCode)
                || 4 != eparchyCode.length()
                || ActPayPubDef.DEFAULT_EPARCHY_CODE.equalsIgnoreCase(eparchyCode)) {
            return false;
        }
        return true;
    }

    /**
     * 销账快照表字段更新
     *
     * @param tradeCommInfoIn
     * @param writeSnapLog
     * @param payLog
     * @param CycleId
     */
    private void genWriteSnapLogInfo(TradeCommInfoIn tradeCommInfoIn, FeeWriteSnapLog writeSnapLog, FeePayLog payLog, int CycleId) {
        writeSnapLog.setChargeId(payLog.getChargeId());
        writeSnapLog.setOperateTime(payLog.getRecvTime());
        writeSnapLog.setRecoverTag(StringUtil.firstOfString(tradeCommInfoIn.getRecoverTag()));
        writeSnapLog.setWriteoffMode(StringUtil.firstOfString(tradeCommInfoIn.getWriteoffMode()));

        if (tradeCommInfoIn.getBillEndCycleId() == ActPayPubDef.MAX_CYCLE_ID) {
            writeSnapLog.setCycleId(CycleId);
        } else {
            writeSnapLog.setCycleId(tradeCommInfoIn.getBillEndCycleId());
        }
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

            //不可清退账本不允许清退
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
        if (StringUtils.isEmpty(backFeeCommInfoIn.getBackDepositType())) {
            return null;
        }
        List<FeeAccountDeposit> depositList = tradeCommInfo.getFeeAccountDeposits();
        if (CollectionUtils.isEmpty(depositList)) {
            return null;
        }

        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();

        //获取清退账本科目编码
        CommPara commPara = commParaFeeService.getCommparaByLike(backFeeCommInfoIn.getBackDepositType(),
                feeAccount.getProvinceCode(), feeAccount.getEparchyCode(), ActPayPubDef.ACT_RDS_DBCONN);
        if (commPara == null || StringUtils.isEmpty(commPara.getParaCode1())) {
            throw new SkyArkException("没有配置" + backFeeCommInfoIn.getBackDepositType() + "参数");
        }

        List<FeeAccountDeposit> backDepositList = depositCalcService.getAcctDepositsByDepositCode(
                depositList, Integer.parseInt(commPara.getParaCode1()));
        if (CollectionUtils.isEmpty(backDepositList)) {
            throw new SkyArkException("没有指定的帐本，不能办理此业务" + backFeeCommInfoIn.getBackDepositType());
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
            tmpTradeDepositInfos = getCanBackFee(tradeCommInfo, ActPayPubDef.MAX_LIMIT_FEE, decuctOweFeeTag);
        }

        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        for (TradeDepositInfo tradeDepositInfo : preTradeDepositInfos) {
            String acctBalanceId = tradeDepositInfo.getAcctBalanceId();
            long backMoney = tradeDepositInfo.getMoney();
            int k = 0;
            for (; k < tmpTradeDepositInfos.size(); k++) {
                if (acctBalanceId.equals(tmpTradeDepositInfos.get(k))) {
                    break;
                }
            }
            if (k == tmpTradeDepositInfos.size()) {
                throw new SkyArkException("您选择的账本不存在或不可清退，请尝试强制清退!acctBalanceId=%s" + acctBalanceId);
            }

            if (backMoney > tmpTradeDepositInfos.get(k).getMoney()) {
                throw new SkyArkException("指定清退的帐本清退金额不合法!acctBalanceId=%s" + acctBalanceId);
            }

            logger.info("BACK MONEY = " + backMoney);

            TradeDepositInfo tradeDepositInfo1 = new TradeDepositInfo();
            tradeDepositInfo1.setAcctBalanceId(acctBalanceId);
            tradeDepositInfo1.setMoney(backMoney);
            tradeDepositInfos.add(tradeDepositInfo1);
        }
        return tradeDepositInfos;
    }


    /**
     * 账本可转金额
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

    /**
     * 账务后台交易工单MQ信息
     *
     * @param tradeCommInfoIn
     * @param payLog
     * @return
     */
    private PayLogDmnMQInfo genPayLogDmnMQInfo(TradeCommInfoIn tradeCommInfoIn, FeePayLog payLog) {
        PayLogDmnMQInfo payLogDmnMQInfo = new PayLogDmnMQInfo();
        payLogDmnMQInfo.setTradeId(payLog.getChargeId());
        payLogDmnMQInfo.setTradeTypeCode(0);
        payLogDmnMQInfo.setEparchyCode(payLog.getEparchyCode());
        payLogDmnMQInfo.setProvinceCode(payLog.getProvinceCode());
        payLogDmnMQInfo.setBatchId(payLog.getChargeId());
        payLogDmnMQInfo.setChargeId(payLog.getChargeId());
        payLogDmnMQInfo.setAcctId(payLog.getAcctId());
        payLogDmnMQInfo.setUserId(payLog.getUserId());
        payLogDmnMQInfo.setSerialNumber(payLog.getSerialNumber());
        payLogDmnMQInfo.setWriteoffMode(StringUtil.firstOfString(tradeCommInfoIn.getWriteoffMode()));
        payLogDmnMQInfo.setLimitMode(StringUtil.firstOfString(tradeCommInfoIn.getLimitMode()));
        payLogDmnMQInfo.setChannelId(payLog.getChannelId());
        payLogDmnMQInfo.setPaymentId(payLog.getPaymentId());
        payLogDmnMQInfo.setPaymentOp(payLog.getPaymentOp());
        payLogDmnMQInfo.setPayFeeModeCode(payLog.getPayFeeModeCode());
        payLogDmnMQInfo.setRecvFee(payLog.getRecvFee());
        payLogDmnMQInfo.setOuterTradeId(payLog.getOuterTradeId());
        payLogDmnMQInfo.setBillStartCycleId(tradeCommInfoIn.getBillStartCycleId());
        payLogDmnMQInfo.setBillEndCycleId(tradeCommInfoIn.getBillEndCycleId());
        payLogDmnMQInfo.setStartDate(tradeCommInfoIn.getDepositStartDate());
        payLogDmnMQInfo.setMonths(tradeCommInfoIn.getMonths());
        payLogDmnMQInfo.setLimitMoney(tradeCommInfoIn.getLimitMoney());
        payLogDmnMQInfo.setPaymentReasonCode(StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getReasonCode()) ? 0 : Long.parseLong(tradeCommInfoIn.getReasonCode()));
        payLogDmnMQInfo.setExtendTag(payLog.getExtendTag());
        payLogDmnMQInfo.setAcctBalanceId(tradeCommInfoIn.getAcctBalanceId());
        payLogDmnMQInfo.setDepositCode(tradeCommInfoIn.getDepositCode());
        payLogDmnMQInfo.setPrivateTag(StringUtil.firstOfString(tradeCommInfoIn.getPrivateTag()));
        payLogDmnMQInfo.setRemark(tradeCommInfoIn.getRemark());
        payLogDmnMQInfo.setTradeTime(payLog.getRecvTime());
        payLogDmnMQInfo.setTradeStaffId(payLog.getRecvStaffId());
        payLogDmnMQInfo.setTradeDepartId(payLog.getRecvDepartId());
        payLogDmnMQInfo.setTradeEparchyCode(payLog.getRecvEparchyCode());
        payLogDmnMQInfo.setTradeCityCode(payLog.getRecvCityCode());

        // 一卡充缴费可打发票金额放入备用字段rsrvInfo1
        if (100006 == payLog.getPaymentId()) {
            payLogDmnMQInfo.setRsrvInfo1(String.valueOf(tradeCommInfoIn.getInvoiceFee()));
        }
        payLogDmnMQInfo.setRelChargeId(tradeCommInfoIn.getRelChargeId());
        return payLogDmnMQInfo;
    }
}
