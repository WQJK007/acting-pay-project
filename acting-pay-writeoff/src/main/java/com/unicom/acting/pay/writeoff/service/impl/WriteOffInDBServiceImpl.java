package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.pay.domain.AccessLogMQInfo;
import com.unicom.acting.pay.domain.PayLogDmnMQInfo;
import com.unicom.acting.pay.domain.PayLogMQInfo;
import com.unicom.acting.pay.domain.TradeCommMQInfo;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.calc.service.DepositService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.writeoff.service.*;
import com.unicom.acts.pay.domain.Account;
import com.unicom.acts.pay.domain.AccountDeposit;
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
 * @author Administrators
 */
@Service
public class WriteOffInDBServiceImpl implements WriteOffInDBService {
    private static final Logger logger = LoggerFactory.getLogger(WriteOffInDBServiceImpl.class);
    @Autowired
    private DepositService depositService;
    @Autowired
    private DatumPayService datumPayService;
    @Autowired
    private PayOtherLogService payLogPayService;
    @Autowired
    private AcctDepositPayService acctDepositPayService;
    @Autowired
    private SysCommOperFeeService sysCommOperPayService;
    @Autowired
    private CommParaFeeService commParaPayService;
    @Autowired
    private WriteOffLogService writeOffLogService;

    @Override
    public void genLockAccount(String acctId, String provinceCode) {
        try {
            logger.info("lock acctId=" + acctId);
            datumPayService.genLockAccount(acctId, provinceCode);
        } catch (Exception ex) {
            throw new SkyArkException("账户锁定操作中，请稍候再试!acctId=" + acctId);
        }
    }


    @Override
    public void setRecvfee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //根据参数配置重新设置可打金额 按交费金额增加可打金额
        CommPara rCommPara = writeOffRuleInfo.getCommpara("ASM_ADD_INVOICE_FEE");
        if (rCommPara != null && !StringUtil.isEmptyCheckNullStr(rCommPara.getParaCode1())
                && "1".equals(rCommPara.getParaCode1())
                && !StringUtil.isEmptyCheckNullStr(rCommPara.getParaCode2())) {
            String channelPayment = "|" + tradeCommInfoIn.getChannelId() + ":" + tradeCommInfoIn.getPaymentId() + "|";
            if (rCommPara.getParaCode2().contains(channelPayment)) {
                tradeCommInfoIn.setInvoiceTag("1");
            }
        }
        AccountDeposit accountDeposit = null;
        if (tradeCommInfoIn.isDepositRecv()) {
            if (tradeCommInfo.isSpecialCycleStatus()) {
                throw new SkyArkException("月结销账期间不允指定帐本缴费!");
            }

            String acctBalanceId = tradeCommInfoIn.getAcctBalanceId();
            int depositCode = tradeCommInfoIn.getDepositCode();
            if (!StringUtil.isEmptyCheckNullStr(acctBalanceId) && acctBalanceId.length() > 4) {
                accountDeposit = depositService.getAcctDepositByAcctBalanceId(tradeCommInfo.getAccountDeposits(), acctBalanceId);
                accountDeposit.setRecvFee(tradeCommInfoIn.getTradeFee());
                accountDeposit.setIfInAccesslog('1');
            } else if (depositCode >= 0) {
                accountDeposit = acctDepositPayService.genAcctDepositByDepositCode(tradeCommInfoIn, tradeCommInfo, depositCode);
            } else {
                throw new SkyArkException("指定帐本缴费必须指定帐本标识或者帐本科目!");
            }
        } else {
            accountDeposit = acctDepositPayService.genAcctDeposit(tradeCommInfoIn, tradeCommInfo);
        }

        //更新账本列表
        depositService.accountDepositUpAndSort(tradeCommInfo.getWriteOffRuleInfo(), tradeCommInfo.getAccountDeposits(), accountDeposit);

        //同步在线信控时使用
        tradeCommInfoIn.setAcctBalanceId(accountDeposit.getAcctBalanceId());
        //生成缴费日志时使用
        tradeCommInfoIn.setPaymentOp(16000);
    }

    @Override
    public void setBackFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (StringUtils.isEmpty(tradeCommInfoIn.getBackType())) {
            throw new SkyArkException("清退类型没有传入");
        }

        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        if (ActPayPubDef.BACK_BY_ALLMONEY.equals(tradeCommInfoIn.getBackType())) {
            tradeDepositInfos = getCanBackFee(tradeCommInfo, tradeCommInfoIn.getTradeFee(), tradeCommInfoIn.getDecuctOwefeeTag());
        } else if ("1".equals(tradeCommInfoIn.getBackType())) {
            if (StringUtils.isEmpty(tradeCommInfoIn.getBackDepositType())) {
                throw new SkyArkException("指定清退账本科目类型没有传入");
            }
            tradeDepositInfos = getCanBackFee(tradeCommInfoIn, tradeCommInfo);
        } else if ("2".equals(tradeCommInfoIn.getBackType())) {
            tradeDepositInfos = getCanBackFee(tradeCommInfo, tradeCommInfoIn.getTradeDepositInfos(),
                    tradeCommInfoIn.getForceBackAcctTag(), tradeCommInfoIn.getDecuctOwefeeTag());
        } else {
            throw new SkyArkException("不支持的清退类型 BACK_TYPE = " + tradeCommInfoIn.getBackDepositType());
        }

        if (CollectionUtils.isEmpty(tradeDepositInfos)) {
            throw new SkyArkException("没有清退的帐本!");
        }

        List<AccountDeposit> accountDeposits = tradeCommInfo.getAccountDeposits();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        long totalBackMoney = 0;
        Map depositInvMap = new HashMap();
        for (TradeDepositInfo tradeDepositInfo : tradeDepositInfos) {
            String acctBalanceId = tradeDepositInfo.getAcctBalanceId();
            long backMoney = tradeDepositInfo.getMoney();
            AccountDeposit tmpDeposit = depositService.getAcctDepositByAcctBalanceId(accountDeposits, acctBalanceId);
            tmpDeposit.setRecvFee(-backMoney);
            if (0 != tmpDeposit.getRecvFee()) {
                //赠款
                if (2 == writeOffRuleInfo.depositTypeCode(tmpDeposit.getDepositCode())
                        || 3 == writeOffRuleInfo.depositTypeCode(tmpDeposit.getDepositCode())) {
                    tradeCommInfoIn.setPayFeeModeCode(4);
                }
            }

            totalBackMoney += tmpDeposit.getRecvFee();

            if ('1' == tradeCommInfoIn.getDecuctInvTag()) {
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

        tradeCommInfoIn.setTradeFee(totalBackMoney);
        tradeCommInfoIn.setPaymentOp(16001);


    }

    @Override
    public void setTransFerOut(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //转出账本实例标识
        String acctBalanceId = tradeCommInfoIn.getAcctBalanceId();
        List<AccountDeposit> accountDeposits = tradeCommInfo.getAccountDeposits();
        long transFee = tradeCommInfoIn.getTradeFee();
        //实际转出账本
        AccountDeposit transOutDeposit = depositService.getAcctDepositByAcctBalanceId(accountDeposits, acctBalanceId);
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
                tradeCommInfoIn.setPayFeeModeCode(4);
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
        tradeCommInfoIn.setTradeFee(-transFee);
    }

    @Override
    public void setTransFerIn(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //转出账本实例标识
        String acctBalanceId = tradeCommInfoIn.getAcctBalanceId();
        List<AccountDeposit> accountDeposits = tradeCommInfo.getAccountDeposits();
        long transFee = tradeCommInfoIn.getTradeFee();
        //实际转出账本
        AccountDeposit transOutDeposit = depositService.getAcctDepositByAcctBalanceId(accountDeposits, acctBalanceId);
        if (transOutDeposit.getDepositCode() == tradeCommInfoIn.getDepositCode()) {
            throw new SkyArkException("转出帐本科目不能和目的帐本科目相同!"
                    + "TransInDepositCode = " + tradeCommInfoIn.getDepositCode()
                    + ",TransOutDepositCode = " + transOutDeposit.getDepositCode());
        }

        //获取转入账本
        AccountDeposit transInDeposit = acctDepositPayService.genAcctDepositByTransFer(tradeCommInfoIn, tradeCommInfo, transOutDeposit);
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        if (transFee != 0) {
            if ('2' == writeOffRuleInfo.depositTypeCode(transInDeposit.getDepositCode())
                    || '3' == writeOffRuleInfo.depositTypeCode(transInDeposit.getDepositCode())) {
                //赠款
                tradeCommInfoIn.setPayFeeModeCode(4);
            }
        }
        depositService.accountDepositUpAndSort(writeOffRuleInfo, accountDeposits, transInDeposit);
    }

    @Override
    public void setTransFeeOut(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        //转出账本不存在不做任何处理
        if (CollectionUtils.isEmpty(tradeCommInfoIn.getTradeDepositInfos())) {
            return;
        }

        List<TradeDepositInfo> tradeDepositInfos = getCanTransferFee(tradeCommInfoIn, tradeCommInfo, ActPayPubDef.MAX_LIMIT_FEE);
        if (CollectionUtils.isEmpty(tradeDepositInfos)) {
            throw new SkyArkException("该账户无账本可转");
        }

        List<AccountDeposit> accountDeposits = tradeCommInfo.getAccountDeposits();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        long tradeFee = 0;
        for (TradeDepositInfo transOutDeposit : tradeCommInfoIn.getTradeDepositInfos()) {
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
            if ('1' == tradeCommInfoIn.getChgAcctTag()) {
                transOutDeposit.setMoney(tradeDepositInfos.get(k).getMoney());
            } else {
                //非过户转账校验实际转账额度
                if (transOutDeposit.getMoney() > tradeDepositInfos.get(k).getMoney()) {
                    throw new SkyArkException("转出的帐本金额不合法!transMoney = " + transOutDeposit.getMoney()
                            + ",acctBalanceId = " + transOutDeposit.getAcctBalanceId());
                }
            }

            AccountDeposit deposit = depositService.getAcctDepositByAcctBalanceId(
                    accountDeposits, transOutDeposit.getAcctBalanceId());
            deposit.setRecvFee(-transOutDeposit.getMoney());
            tradeFee += deposit.getRecvFee();

            if (deposit.getRecvFee() != 0) {
                if ('2' == writeOffRuleInfo.depositTypeCode(deposit.getDepositCode())
                        || '3' == writeOffRuleInfo.depositTypeCode(deposit.getDepositCode())) {
                    //赠款
                    tradeCommInfoIn.setPayFeeModeCode(4);
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
            depositService.accountDepositUpAndSort(writeOffRuleInfo, accountDeposits, deposit);
        }
        //更新实际转账金额
        tradeCommInfoIn.setTradeFee(tradeFee);
    }

    @Override
    public void setTransFeeIn(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (CollectionUtils.isEmpty(tradeCommInfoIn.getTransOutDedposits())) {
            return;
        }

        List<AccountDeposit> accountDeposits = tradeCommInfo.getAccountDeposits();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        List<TradeDepositInfo> desDeposits = new ArrayList();
        long tradeFee = 0;
        for (TradeDepositInfo depositInfo : tradeCommInfoIn.getTradeDepositInfos()) {
            AccountDeposit transOutDeposit = depositService.getAcctDepositByAcctBalanceId(
                    accountDeposits, depositInfo.getAcctBalanceId());
            AccountDeposit desDeposit = acctDepositPayService.genAcctDepositByTransFer(tradeCommInfoIn, tradeCommInfo, transOutDeposit);
            tradeFee += depositInfo.getMoney();
            if (depositInfo.getMoney() != 0) {
                if ('2' == writeOffRuleInfo.depositTypeCode(desDeposit.getDepositCode())
                        || '3' == writeOffRuleInfo.depositTypeCode(desDeposit.getDepositCode())) {
                    tradeCommInfoIn.setPayFeeModeCode(4);
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
            depositService.accountDepositUpAndSort(writeOffRuleInfo, accountDeposits, desDeposit);
        }

        tradeCommInfoIn.getTradeDepositInfos().addAll(desDeposits);
        tradeCommInfoIn.setTradeFee(tradeFee);
    }

    @Override
    public void genInDBInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommMQInfo tradeCommMQInfo) {
        //地市销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //生成交费日志信息
        PayLog payLog = genPayLog(tradeCommInfoIn, tradeCommInfo);
        tradeCommInfo.setPayLog(payLog);

        //生成缴费其他日志信息
        PayOtherLog payOtherLog = null;
        if ((tradeCommInfoIn.getCarrierInfo() != null
                && !StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getCarrierInfo().getCarrierId()))) {
            payOtherLog = payLogPayService.genPayOtherLog(tradeCommInfoIn.getCarrierInfo(), payLog);
            tradeCommInfo.setPayOtherLog(payOtherLog);
        }

        //抵扣或者补收期间并且对应临时表中有数据
        if (tradeCommInfo.isSpecialCycleStatus()) {
            if (payLog.getExtendTag() != '0') {
                throw new SkyArkException("月结期间不允许进行异地缴费!");
            }

            if (tradeCommInfo.isExistsTradeCheck()) {
                //涉及第三方对账只能发送MQ消息
                tradeCommMQInfo.setPayLogDmnMQInfo(genPayLogDmnMQInfo(tradeCommInfoIn, payLog));
            } else {
                PayLogDmn payLogDmn = genPayLogDmn(tradeCommInfoIn, payLog);
                tradeCommInfo.setPayLogDmn(payLogDmn);
            }
            return;
        }

        //更新交费快照信息
        WriteSnapLog writeSnapLog = tradeCommInfo.getWriteSnapLog();
        genWriteSnapLogInfo(tradeCommInfoIn, writeSnapLog, payLog, writeOffRuleInfo.getCurCycle().getCycleId());
        tradeCommInfo.setWriteSnapLog(writeSnapLog);

        //生成销账日志数据
        List<WriteOffLog> writeOffLogs = writeOffLogService.genWriteOffLogInfo(tradeCommInfo.getWriteOffLogs(), payLog, writeOffRuleInfo);
        //销账日志入库
        tradeCommInfo.setWriteOffLogs(writeOffLogs);


        //生成取款日志数据
        List<AccessLog> accessLogs = writeOffLogService.genAccessLogInfo(tradeCommInfo.getAccesslogs(), payLog, CollectionUtils.isEmpty(writeOffLogs));
        tradeCommInfo.setAccesslogs(accessLogs);
        tradeCommInfo.setAccesslogs(accessLogs);

        //生成取款日志MQ信息
        if (!CollectionUtils.isEmpty(accessLogs)) {
            tradeCommMQInfo.setAccessLogMQInfos(writeOffLogService.genAccessLogMQInfo(accessLogs));
        }

        //生成代收费日志
        List<CLPayLog> clPayLogList = genCLPaylog(writeOffLogs, payLog);
        tradeCommInfo.setClPayLogs(clPayLogList);

        //更新存折入库信息
        List<AccountDeposit> deposits = tradeCommInfo.getAccountDeposits();
        acctDepositPayService.updateDepositInfo(deposits, writeSnapLog.getAllNewBOweFee(),
                writeOffRuleInfo.getSysdate(), writeOffRuleInfo.getMaxAcctCycle().getCycleId());
        tradeCommMQInfo.setDepositMQInfos(acctDepositPayService.genDepositMQInfo(deposits));

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
                || CollectionUtils.isEmpty(tradeCommInfo.getBills())) {
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
            List<Bill> bills = tradeCommInfo.getBills();
            for (Bill bill : bills) {
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
                || CollectionUtils.isEmpty(tradeCommInfo.getBills())
                || tradeCommInfo.getAccount() == null) {
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
            Account account = tradeCommInfo.getAccount();
            for (String payFeeMode : consignPayMode) {
                if (payFeeMode.equals(account.getPayModeCode())) {
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
                || CollectionUtils.isEmpty(tradeCommInfo.getBills())) {
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
            List<Bill> bills = tradeCommInfo.getBills();
            for (Bill bill : bills) {
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
    private PayLog genPayLog(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        PayLog payLog = new PayLog();
        Account account = tradeCommInfo.getAccount();
        User mainUser = tradeCommInfo.getMainUser();
        Staff staff = tradeCommInfo.getTradeStaff();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        payLog.setProvinceCode(account.getProvinceCode());
        payLog.setEparchyCode(account.getEparchyCode());
        payLog.setCityCode(account.getCityCode());
        payLog.setCustId(account.getCustId());
        payLog.setAcctId(account.getAcctId());
        payLog.setUserId(mainUser.getUserId());
        payLog.setSerialNumber(mainUser.getSerialNumber());
        payLog.setNetTypeCode(mainUser.getNetTypeCode());
        //帐户缴费，没有输入用户信息，需要填写帐户的网别
        if (StringUtil.isEmptyCheckNullStr(payLog.getNetTypeCode())) {
            payLog.setNetTypeCode(account.getNetTypeCode());
        }
        if (StringUtil.isEmptyCheckNullStr(payLog.getUserId())) {
            payLog.setUserId("-1");
        }
        if (StringUtil.isEmptyCheckNullStr(payLog.getSerialNumber())) {
            payLog.setSerialNumber("-1");
        }
        //外围没有传入交费流水，系统会重新生成
        if (StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getChargeId())) {
            String tmpChargeId = sysCommOperPayService.getSequence(payLog.getEparchyCode(),
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
            String provinceCode = commParaPayService.getProvCodeByEparchyCode(
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
        if ("02".equals(tradeCommInfoIn.getNpFlag())) {
            payLog.setNpTag(tradeCommInfoIn.getNpFlag());
        }
        return payLog;
    }

    /**
     * 生成账务后台工单表信息
     *
     * @param tradeCommInfoIn
     * @param payLog
     * @return
     */
    private PayLogDmn genPayLogDmn(TradeCommInfoIn tradeCommInfoIn, PayLog payLog) {
        PayLogDmn payLogDmn = new PayLogDmn();
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
    private List<CLPayLog> genCLPaylog(List<WriteOffLog> writeOffLogList, PayLog payLog) {
        if (CollectionUtils.isEmpty(writeOffLogList)) {
            return Collections.EMPTY_LIST;
        }

        List<CLPayLog> clPayLogList = new ArrayList<>();
        for (WriteOffLog writeOffLog : writeOffLogList) {
            if (writeOffLog.getCanPaytag() != '4' || writeOffLog.getWriteoffFee() == 0)
                continue;
            int k = 0;
            for (; k < clPayLogList.size(); k++) {
                if (clPayLogList.get(k).getUserId().equals(writeOffLog.getUserId()))
                    break;
            }

            if (k == clPayLogList.size()) {
                String clPaylogId = sysCommOperPayService.getSequence(payLog.getEparchyCode(), ActPayPubDef.SEQ_CHARGE_ID, payLog.getProvinceCode());
                CLPayLog clPayLog = new CLPayLog();
                clPayLog.setClPaylogId(clPaylogId);
                clPayLog.setProvinceCode(writeOffLog.getProvinceCode());
                clPayLog.setEparchyCode(writeOffLog.getEparchyCode());
                clPayLog.setAreaCode(writeOffLog.getAreaCode());
                clPayLog.setNetTypeCode(writeOffLog.getNetTypeCode());
                clPayLog.setAcctId(writeOffLog.getAcctId());
                clPayLog.setUserId(writeOffLog.getUserId());

                String oldUserId = datumPayService.getOldUserIdOf2G3G(writeOffLog.getUserId(), payLog.getProvinceCode());
                String oldAcctId = datumPayService.getOldAcctIdOf2G3G(writeOffLog.getUserId(), payLog.getProvinceCode());

                if ("".equals(oldUserId) || "".equals(oldAcctId))
                    throw new SkyArkException("未找到迁转前省份OLD_ACCT_ID或OLD_USER_ID!(userId =" + writeOffLog.getUserId() + ")");
                clPayLog.setOldAcctId(oldAcctId);
                clPayLog.setOldUserId(oldUserId);
                clPayLog.setSerialNumber(writeOffLog.getSerialNumber());
                clPayLog.setPaymentId(payLog.getPaymentId());
                clPayLog.setRecvFee(writeOffLog.getWriteoffFee());
                clPayLog.setChargeId(payLog.getChargeId());
                clPayLog.setOuterTradeId(payLog.getOuterTradeId());
                clPayLog.setRecvTime(payLog.getRecvTime());
                clPayLog.setRecvStaffId(payLog.getRecvStaffId());
                clPayLog.setRecvDepartId(payLog.getRecvDepartId());
                clPayLog.setEparchyCode(payLog.getRecvEparchyCode());
                clPayLog.setRecvCityCode(payLog.getRecvCityCode());
                clPayLogList.add(clPayLog);
            } else {
                clPayLogList.get(k).setRecvFee(clPayLogList.get(k).getRecvFee() + writeOffLog.getWriteoffFee());
            }
        }
        return clPayLogList;
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
    private void genWriteSnapLogInfo(TradeCommInfoIn tradeCommInfoIn, WriteSnapLog writeSnapLog, PayLog payLog, int CycleId) {
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
        List<AccountDeposit> depositList = tradeCommInfo.getAccountDeposits();
        if (CollectionUtils.isEmpty(depositList)) {
            return null;
        }

        //可清退账本信息
        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        for (AccountDeposit deposit : depositList) {
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
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private List<TradeDepositInfo> getCanBackFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (StringUtils.isEmpty(tradeCommInfoIn.getBackDepositType())) {
            return null;
        }
        List<AccountDeposit> depositList = tradeCommInfo.getAccountDeposits();
        if (CollectionUtils.isEmpty(depositList)) {
            return null;
        }

        Account account = tradeCommInfo.getAccount();

        //获取清退账本科目编码
        CommPara commPara = commParaPayService.getCommparaByLike(tradeCommInfoIn.getBackDepositType(),
                account.getProvinceCode(), account.getEparchyCode(), ActPayPubDef.ACT_RDS_DBCONN);
        if (commPara == null || StringUtils.isEmpty(commPara.getParaCode1())) {
            throw new SkyArkException("没有配置" + tradeCommInfoIn.getBackDepositType() + "参数");
        }

        List<AccountDeposit> backDepositList = depositService.getAcctDepositsByDepositCode(
                depositList, Integer.parseInt(commPara.getParaCode1()));
        if (CollectionUtils.isEmpty(backDepositList)) {
            throw new SkyArkException("没有指定的帐本，不能办理此业务" + tradeCommInfoIn.getBackDepositType());
        }

        //强制请退
        tradeCommInfoIn.setForceBackAcctTag('1');
        long backFee = tradeCommInfoIn.getTradeFee();
        List<TradeDepositInfo> tradeDepositInfos = new ArrayList<>();
        for (AccountDeposit deposit : depositList) {
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
        List<AccountDeposit> depositList = tradeCommInfo.getAccountDeposits();
        if (CollectionUtils.isEmpty(depositList)) {
            return null;
        }

        //账户实际可清退账本信息
        List<TradeDepositInfo> tmpTradeDepositInfos = new ArrayList();
        //强制指定帐本，不考虑可清退标志和结余
        if ('1' == forceBackAcctTag) {
            for (AccountDeposit deposit : depositList) {
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
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @param maxTransferFee
     * @return
     */
    private List<TradeDepositInfo> getCanTransferFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, long maxTransferFee) {
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //账本销当月费用金额
        Map<String, Long> writeOffRealFee = new HashMap();
        List<WriteOffLog> writeOffLogs = tradeCommInfo.getWriteOffLogs();
        if (!CollectionUtils.isEmpty(writeOffLogs)) {
            for (WriteOffLog writeOffLog : writeOffLogs) {
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

        List<AccountDeposit> accountDeposits = tradeCommInfo.getAccountDeposits();
        if (CollectionUtils.isEmpty(accountDeposits)) {
            return null;
        }
        List<TradeDepositInfo> tradeDepositInfos = new ArrayList();
        for (AccountDeposit deposit : accountDeposits) {
            //冻结或失效账本不做转出处理
            if (deposit.getValidTag() != 1
                    || writeOffRuleInfo.getSysdate().compareTo(deposit.getEndDate()) > 0) {
                continue;
            }

            //过户转账不校验账本可转属性 余额转账需要校验可转属性
            if ('1' != tradeCommInfoIn.getChgAcctTag()
                    && !writeOffRuleInfo.isTransDeposit(deposit.getDepositCode())) {
                continue;
            }

            long totalMoney = deposit.getMoney() - deposit.getFreezeFee();
            long leftMoney = totalMoney - deposit.getImpFee() - deposit.getUseRecvFee();
            long transFee = 0;

            //不扣减欠费但必须扣减截止到上月的欠费
            if (tradeCommInfoIn.getDecuctOwefeeTag() == '1'
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
    private PayLogDmnMQInfo genPayLogDmnMQInfo(TradeCommInfoIn tradeCommInfoIn, PayLog payLog) {
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
