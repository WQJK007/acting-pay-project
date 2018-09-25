package com.unicom.acting.pay.transfee.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.fee.writeoff.service.CycleService;
import com.unicom.acting.fee.writeoff.service.FeeCommService;
import com.unicom.acting.pay.domain.ActingPayCommparaDef;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.acting.pay.domain.WriteOffLog;
import com.unicom.acting.pay.domain.WriteSnapLog;
import com.unicom.acting.pay.transfee.service.TransFeeService;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoOut;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommResultInfo;
import com.unicom.acting.pay.writeoff.service.CreditService;
import com.unicom.acting.pay.writeoff.service.SmsService;
import com.unicom.acting.pay.writeoff.service.TransFeeCommService;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TransFeeServiceImpl implements TransFeeService {
    private Logger logger = LoggerFactory.getLogger(TransFeeServiceImpl.class);

    @Autowired
    private FeeCommService feeCommService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private TransFeeCommService transFeeCommService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private CreditService creditService;
    @Autowired
    private CommParaFeeService commParaFeeService;
    @Autowired
    private CycleService cycleService;

    @Override
    public TransFeeCommInfoOut transferEncash(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo) {
        transFeeCommInfoIn.setPaymentId(100016);
        transFeeCommInfoIn.setPaymentOp(16004);
        transFeeCommInfoIn.setPayFeeModeCode(0);
        //余额转出
        transferOut(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
        //交易关联流水
        String relChargeId = transFeeCommResultInfo.getPayLogs().get(0).getChargeId();
        //原交易日志清理
        clearLog(tradeCommInfo, transFeeCommResultInfo);
        transFeeCommInfoIn.setPaymentId(100015);
        transFeeCommInfoIn.setPaymentOp(16005);
        transFeeCommInfoIn.setRelChargeId(relChargeId);
        transFeeCommInfoIn.setChargeId("");
        transFeeCommInfoIn.setTradeFee(-transFeeCommInfoIn.getTradeFee());
        //余额转入
        transferIn(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
        return genTransFeeCommInfoOut(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
    }

    @Override
    public TransFeeCommInfoOut transFeeOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo) {
        transOut(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
        return genTransFeeCommInfoOut(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
    }

    private void transOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo) {
        //查询用户资料
        feeCommService.getUserDatumInfo(transFeeCommInfoIn, tradeCommInfo);
        //查询账期信息
        feeCommService.getEparchyCycleInfo(tradeCommInfo, transFeeCommInfoIn.getEparchyCode(), transFeeCommInfoIn.getProvinceCode());

        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        if (writeOffRuleInfo.isSpecialRecvState(writeOffRuleInfo.getCurCycle())) {
            throw new SkyArkException("批量销账期间不允许余额转账操作!");
        }

        Account account = tradeCommInfo.getAccount();

        //获取销账规则
        feeCommService.getWriteOffRule(writeOffRuleInfo, account.getProvinceCode(),
                account.getEparchyCode(), account.getNetTypeCode());

        //查询账本
        feeCommService.getAcctBalance(transFeeCommInfoIn, tradeCommInfo);

        //查询账单
        feeCommService.getOweBill(transFeeCommInfoIn, tradeCommInfo);


        if (feeCommService.ifBillConsigning(transFeeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("托收在途不能转兑操作!");
        }

        //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
        if (!feeCommService.ifCalcLateFee(transFeeCommInfoIn, tradeCommInfo)) {
            //计算滞纳金
            tradeCommInfo.setCalcLateFee(true);
            //获取滞纳金减免工单
            feeCommService.getFeeDerateLateFeeLog(transFeeCommInfoIn, tradeCommInfo);
            //获取账户自定义缴费期
            feeCommService.getAcctPaymentCycle(tradeCommInfo, account.getAcctId());
        }

        logger.info("begin calc");
        //模拟销账计算
        calculateService.calc(tradeCommInfo);
        //设置账本转账金额
        transFeeCommService.setTransFeeOut(transFeeCommInfoIn, tradeCommInfo);
        logger.info("after setBackFee");
        //清退后销账计算
        calculateService.recvCalc(tradeCommInfo);
        //生成转账入库日志
        transFeeCommService.genTransFeeDBInfo(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
        //生成短信信息
        smsService.genSmsInfo(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
        //生成信控工单
        creditService.genCreditInfo(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
    }


    /**
     * 同账户余额转出
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     * @param transFeeCommResultInfo
     */
    private void transferOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo) {
        //查询用户资料
        feeCommService.getUserDatumInfo(transFeeCommInfoIn, tradeCommInfo);
        //查询账期信息
        feeCommService.getEparchyCycleInfo(tradeCommInfo, transFeeCommInfoIn.getEparchyCode(), transFeeCommInfoIn.getProvinceCode());

        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        if (writeOffRuleInfo.isSpecialRecvState(writeOffRuleInfo.getCurCycle())) {
            throw new SkyArkException("批量销账期间不允许余额转账操作!");
        }

        Account account = tradeCommInfo.getAccount();

        //获取销账规则
        feeCommService.getWriteOffRule(writeOffRuleInfo, account.getProvinceCode(),
                account.getEparchyCode(), account.getNetTypeCode());

        //查询账本
        feeCommService.getAcctBalance(transFeeCommInfoIn, tradeCommInfo);

        //查询账单
        feeCommService.getOweBill(transFeeCommInfoIn, tradeCommInfo);


        if (feeCommService.ifBillConsigning(transFeeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("托收在途不能转兑操作!");
        }

        //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
        if (!feeCommService.ifCalcLateFee(transFeeCommInfoIn, tradeCommInfo)) {
            //计算滞纳金
            tradeCommInfo.setCalcLateFee(true);
            //获取滞纳金减免工单
            feeCommService.getFeeDerateLateFeeLog(transFeeCommInfoIn, tradeCommInfo);
            //获取账户自定义缴费期
            feeCommService.getAcctPaymentCycle(tradeCommInfo, account.getAcctId());
        }

        logger.info("begin calc");
        //模拟销账计算
        calculateService.calc(tradeCommInfo);
        //设置账本转账金额
        transFeeCommService.setTransFerEnCashOut(transFeeCommInfoIn, tradeCommInfo);
        logger.info("after setBackFee");
        //清退后销账计算
        calculateService.recvCalc(tradeCommInfo);
        //生成转账入库日志
        transFeeCommService.genTransFeeDBInfo(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
    }

    /**
     * 同账户余额转入
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     * @param transFeeCommResultInfo
     */
    private void transferIn(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo) {
        Account account = tradeCommInfo.getAccount();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //查询账期信息
        checkCycleInfo(account.getEparchyCode(), account.getProvinceCode(), writeOffRuleInfo);
        logger.info("begin calc");
        //模拟销账计算
        calculateService.calc(tradeCommInfo);
        //设置账本转账金额
        transFeeCommService.setTransFerEnCashIn(transFeeCommInfoIn, tradeCommInfo);
        logger.info("after setBackFee");
        //清退后销账计算
        calculateService.recvCalc(tradeCommInfo);
        //生成转账入库日志
        transFeeCommService.genTransFeeDBInfo(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
        //生成短信信息
        transFeeCommResultInfo.setPayLog(transFeeCommResultInfo.getPayLogs().get(1));
        smsService.genSmsInfo(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
        //生成信控工单
        creditService.genCreditInfo(transFeeCommInfoIn, tradeCommInfo, transFeeCommResultInfo);
    }

    /**
     * 同账户转账信息返回
     *
     * @param transFeeCommInfoIn
     * @param tradeCommInfo
     * @param transFeeCommResultInfo
     * @return
     */
    TransFeeCommInfoOut genTransFeeCommInfoOut(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo) {
        TransFeeCommInfoOut transFeeCommInfoOut = new TransFeeCommInfoOut();
        //销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //设置账期信息
        transFeeCommInfoOut.setCurCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
        transFeeCommInfoOut.setMaxAcctCycleId(writeOffRuleInfo.getMaxAcctCycle().getCycleId());

        //设置用户信息
        User mainUser = tradeCommInfo.getMainUser();
        transFeeCommInfoOut.setSerialNumber(mainUser.getSerialNumber());
        transFeeCommInfoOut.setUserId(mainUser.getUserId());
        transFeeCommInfoOut.setBrandCode(mainUser.getBrandCode());

        //设置账户信息
        Account account = tradeCommInfo.getAccount();
        transFeeCommInfoOut.setAcctId(account.getAcctId());
        transFeeCommInfoOut.setPayName(account.getPayName());
        transFeeCommInfoOut.setPayModeCode(account.getPayModeCode());
        transFeeCommInfoOut.setEparchyCode(account.getEparchyCode());

        //缴费日志相关信息
        PayLog payLog = transFeeCommResultInfo.getPayLogs().get(1);
        transFeeCommInfoOut.setChargeId(payLog.getChargeId());
        transFeeCommInfoOut.setRelChargeId(transFeeCommInfoIn.getRelChargeId());
        transFeeCommInfoOut.setRecvFee(String.valueOf(payLog.getRecvFee()));
        transFeeCommInfoOut.setRecvTime(payLog.getRecvTime());
        transFeeCommInfoOut.setPaymentId(String.valueOf(payLog.getPaymentId()));

        //设置销账快照信息
        WriteSnapLog writeSnapLog = transFeeCommResultInfo.getWriteSnapLogs().get(1);
        transFeeCommInfoOut.setSpayFee(String.valueOf(writeSnapLog.getSpayFee()));
        transFeeCommInfoOut.setAllMoney(String.valueOf(writeSnapLog.getAllMoney()));
        transFeeCommInfoOut.setAllNewMoney(String.valueOf(writeSnapLog.getAllNewMoney()));
        transFeeCommInfoOut.setAllBalance(String.valueOf(writeSnapLog.getAllBalance()));
        transFeeCommInfoOut.setAllNewBalance(String.valueOf(writeSnapLog.getAllNewBalance()));
        transFeeCommInfoOut.setAllBOweFee(String.valueOf(writeSnapLog.getAllBOweFee()));
        transFeeCommInfoOut.setAimpFee(String.valueOf(writeSnapLog.getaImpFee()));
        transFeeCommInfoOut.setAllBImpFee(String.valueOf(writeSnapLog.getAllBOweFee()
                - writeSnapLog.getAllNewBOweFee()));
        transFeeCommInfoOut.setAllRImpFee(String.valueOf(writeSnapLog.getaImpFee()
                - (writeSnapLog.getAllBOweFee() - writeSnapLog.getAllNewBOweFee())));
        transFeeCommInfoOut.setAllNewBOweFee(String.valueOf(writeSnapLog.getAllNewBOweFee()));
        transFeeCommInfoOut.setPreRealFee(String.valueOf(writeSnapLog.getPreRealFee()));
        transFeeCommInfoOut.setCurRealFee(String.valueOf(writeSnapLog.getCurRealFee()));
        transFeeCommInfoOut.setAllROweFee(String.valueOf(writeSnapLog.getPreRealFee()
                + writeSnapLog.getCurRealFee()));
        return transFeeCommInfoOut;
    }

    /**
     * 交易日志清理
     *
     * @param tradeCommInfo
     * @param transFeeCommResultInfo
     */
    private void clearLog(TradeCommInfo tradeCommInfo, TransFeeCommResultInfo transFeeCommResultInfo) {
        tradeCommInfo.getFeeWriteSnapLog().init();
        //销账日志
        if (!CollectionUtils.isEmpty(tradeCommInfo.getFeeWriteOffLogs())) {
            tradeCommInfo.getFeeWriteOffLogs().clear();
        }
        //个性化销账日志
        if (!CollectionUtils.isEmpty(tradeCommInfo.getCurrLimitFeeDepositLog())) {
            tradeCommInfo.getCurrLimitFeeDepositLog().clear();
        }

        //账本可打金额信息
//        if (!CollectionUtils.isEmpty(tradeCommInfo.getInvoiceFeeMap())) {
//            tradeCommInfo.getInvoiceFeeMap().clear();
//        }

        //用户结余
        if (!CollectionUtils.isEmpty(tradeCommInfo.getUserBalance())) {
            tradeCommInfo.getUserBalance().clear();
        }

        //更新账单信息
        PayLog payLog = transFeeCommResultInfo.getPayLogs().get(0);
        updateBills(tradeCommInfo.getFeeBills(), payLog.getChargeId());

        //更新账本信息
        updateAcctDeposits(tradeCommInfo.getFeeAccountDeposits());

        //更新滞纳金信息
        if (!CollectionUtils.isEmpty(transFeeCommResultInfo.getWriteOffLogs())) {
            List<WriteOffLog> writeOffLogs = transFeeCommResultInfo.getWriteOffLogs();
            Set<Integer> writeOffCycle = new HashSet();
            for (WriteOffLog writeOffLog : writeOffLogs) {
                writeOffCycle.add(writeOffLog.getCycleId());
            }
            if (!CollectionUtils.isEmpty(writeOffCycle)) {
                updateDerateLogs(tradeCommInfo.getFeeDerateLateFeeLogs(), tradeCommInfo.getWriteOffRuleInfo(),
                        writeOffCycle, payLog.getChargeId());
            }
        }


    }


    /**
     * 当前账期查询
     *
     * @param eparchyCode
     * @param writeOffRuleInfo
     * @return
     */
    private void checkCycleInfo(String eparchyCode, String provinceCode, WriteOffRuleInfo writeOffRuleInfo) {
        String sysdate = writeOffRuleInfo.getSysdate();
        //当前当期
        Cycle curCycle = null;
        //当前最大开账账期
        Cycle maxCycle = null;

        CommPara commPara = commParaFeeService.getCommpara(ActingFeeCommparaDef.ASM_AUXACCTSTATUS_FROMCACHE,
                provinceCode, eparchyCode);
        if (commPara != null && "1".equals(commPara.getParaCode1())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode2())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode3())
                && sysdate.substring(8, 10).compareTo(commPara.getParaCode2()) >= 0
                && sysdate.substring(8, 10).compareTo(commPara.getParaCode3()) <= 0) {
            curCycle = cycleService.getCacheCurCycle(eparchyCode);
            maxCycle = cycleService.getCacheMaxAcctCycle(eparchyCode);
        } else {
            curCycle = cycleService.getCurCycle(eparchyCode);
            maxCycle = cycleService.getMaxAcctCycle(eparchyCode);
        }

        if (curCycle == null) {
            throw new SkyArkException("没有取到当前帐期!");
        }

        if (writeOffRuleInfo.isSpecialRecvState(writeOffRuleInfo.getCurCycle())) {
            throw new SkyArkException("批量销账期间不允许余额转账操作!");
        }

        if (maxCycle == null) {
            throw new SkyArkException("没有取到当前最大开帐帐期!");
        }

        writeOffRuleInfo.setCurCycle(curCycle);
        writeOffRuleInfo.setMaxAcctCycle(maxCycle);
    }


    /**
     * 同账户余额转账在转出时触发销账，更新账单欠费，滞纳金信息，其他销账产生的结果做初始化处理
     *
     * @param feeBills
     * @param chargeId
     */
    private void updateBills(List<FeeBill> feeBills, String chargeId) {
        if (CollectionUtils.isEmpty(feeBills)) {
            return;
        }

        for (FeeBill feeBill : feeBills) {
            if (feeBill.getCanpayTag() != '2'
                    && (feeBill.getCurrWriteOffBalance() != 0 || feeBill.getCurrWriteOffLate() != 0
                    || feeBill.getPayTag() != feeBill.getOldPayTag()
                    || '9' == feeBill.getPayTag() || '5' == feeBill.getPayTag() || '1' == feeBill.getBillPayTag())) {

                feeBill.setBalance(feeBill.getBalance() - feeBill.getCurrWriteOffBalance());
                feeBill.setLateFee(feeBill.getLateFee() + feeBill.getNewLateFee() - feeBill.getDerateFee());
                feeBill.setChargeId(chargeId);
            }
            feeBill.setImpFee(0);
            feeBill.setCurrWriteOffBalance(0);
            feeBill.setCurrWriteOffLate(0);
            feeBill.setRsrvFee1(0);
            feeBill.setRsrvFee2(0);
        }
    }

    /**
     * 同账户余额转账在转出时触发销账，账本销账过程产生的结果做初始化处理
     *
     * @param feeAccountDeposits
     */
    private void updateAcctDeposits(List<FeeAccountDeposit> feeAccountDeposits) {
        if (CollectionUtils.isEmpty(feeAccountDeposits)) {
            return;
        }

        for (FeeAccountDeposit feeAccountDeposit : feeAccountDeposits) {
            feeAccountDeposit.setImpFee(0);
            feeAccountDeposit.setUseRecvFee(0);
            feeAccountDeposit.setImpRealFee(0);
            //剩余可使用存折
            feeAccountDeposit.setLeftCanUse(0);
            feeAccountDeposit.setRealFeeRecv(0);
            feeAccountDeposit.setVirtualTag('0');
            //账本销往月账标识
            feeAccountDeposit.setWriteOffOweFee(false);
        }
    }


    /**
     * 同账户余额转账在转出时触发销账，记录滞纳金销账之后的金额信息
     *
     * @param feeDerateLateFeeLogs
     * @param writeOffRuleInfo
     * @param writeOffCycle
     * @param chargeId
     */
    private void updateDerateLogs(List<FeeDerateLateFeeLog> feeDerateLateFeeLogs, WriteOffRuleInfo writeOffRuleInfo, Set<Integer> writeOffCycle, String chargeId) {
        if (CollectionUtils.isEmpty(feeDerateLateFeeLogs)) {
            return;
        }

        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_LATEUSE_PERSIST);
        if (commPara == null) {
            throw new SkyArkException("ASM_LATEUSE_PERSIST参数没有配置!");
        }

        for (FeeDerateLateFeeLog feeDerateLateFeeLog : feeDerateLateFeeLogs) {
            if ('1' == feeDerateLateFeeLog.getUseTag()) {
                char newUseTag = '1';

                //是按金额才能持续使用
                if (0 == feeDerateLateFeeLog.getDerateRuleId() && "1".equals(commPara.getParaCode1())) {
                    if (feeDerateLateFeeLog.getUsedDerateFee() < feeDerateLateFeeLog.getDerateFee()) {
                        newUseTag = '2';
                    }
                }
                //本账期发生销账，滞纳金减免日志才更新标志
                if (writeOffCycle.contains(feeDerateLateFeeLog.getCycleId())) {
                    feeDerateLateFeeLog.setUseTag(newUseTag);
                    feeDerateLateFeeLog.setOperateId(chargeId);
                }
            }
        }
    }
}
