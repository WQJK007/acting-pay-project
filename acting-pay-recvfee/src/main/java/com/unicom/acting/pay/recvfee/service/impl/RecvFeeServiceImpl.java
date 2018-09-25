package com.unicom.acting.pay.recvfee.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoOut;
import com.unicom.acting.fee.writeoff.service.FeeCommService;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.acting.pay.recvfee.service.RecvFeeCheckLogService;
import com.unicom.acting.pay.recvfee.service.RecvFeeOrderService;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.service.*;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.pay.recvfee.service.RecvFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecvFeeServiceImpl implements RecvFeeService {
    private Logger logger = LoggerFactory.getLogger(RecvFeeServiceImpl.class);
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private FeeCommService feeCommService;
    @Autowired
    private RecvFeeCommService recvFeeCommService;
    @Autowired
    private TradeCommService tradeCommService;
    @Autowired
    private TradeAsynOrderService tradeAsynOrderService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private CreditService creditService;
    @Autowired
    private RecvFeeOrderService recvFeeOrderService;
    @Autowired
    private RecvFeeCheckLogService recvFeeCheckLogService;


    @Override
    public TradeCommInfoOut simpleRecvFee(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        //查询用户资料
        feeCommService.getUserDatumInfo(recvFeeCommInfoIn, tradeCommInfo);
        //查询账期信息
        feeCommService.getEparchyCycleInfo(tradeCommInfo, recvFeeCommInfoIn.getEparchyCode(), recvFeeCommInfoIn.getProvinceCode());
        //非大合帐缴费
        if (!recvFeeCommInfoIn.isBigAcctRecvFee()) {
            return recvFeeSimple(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        } else {
            return asynRecvFee(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        }
    }

    private TradeCommInfoOut recvFeeSimple(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        Account account= tradeCommInfo.getAccount();
        //tradeCommService.genLockAccount(feeAccount.getAcctId(), recvFeeCommInfoIn.getProvinceCode());
        //获取省份地市销账规则
        feeCommService.getWriteOffRule(tradeCommInfo.getWriteOffRuleInfo(), account.getProvinceCode(), account.getEparchyCode(), account.getNetTypeCode());
        //查询账本
        feeCommService.getAcctBalance(recvFeeCommInfoIn, tradeCommInfo);
        //查询账单
        feeCommService.getOweBill(recvFeeCommInfoIn, tradeCommInfo);
        //缴费特殊校验
        specialRecvFeeCheck(recvFeeCommInfoIn, tradeCommInfo);
        //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
        if (!feeCommService.ifCalcLateFee(recvFeeCommInfoIn, tradeCommInfo)) {
            //计算滞纳金
            tradeCommInfo.setCalcLateFee(true);
            //获取滞纳金减免工单
            feeCommService.getFeeDerateLateFeeLog(recvFeeCommInfoIn, tradeCommInfo);
            //获取账户自定义缴费期  DbTypes.ACTS_DRDS
            feeCommService.getAcctPaymentCycle(tradeCommInfo, account.getAcctId());
        }
        logger.info("begin calc");
        //缴费前销账计算
        calculateService.calc(tradeCommInfo);
        //设置缴费金额
        recvFeeCommService.setRecvfee(recvFeeCommInfoIn, tradeCommInfo);
        logger.info("after setRecvfee");
        //缴费后销账计算
        calculateService.recvCalc(tradeCommInfo);
        //生成缴费入库信息
        recvFeeCommService.genRecvDBInfo(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        //生成短信信息
        smsService.genSmsInfo(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        //生成信控工单
        creditService.genCreditInfo(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        //缴费结果整理返回
        return genTradeCommInfoOut(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
    }

    /**
     * 大合帐缴费异步处理
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     * @return
     */
    private TradeCommInfoOut asynRecvFee(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        logger.info("生成异步缴费工单开始");
        AsynWork asynWork = recvFeeOrderService.genAsynWork(recvFeeCommInfoIn, tradeCommInfo);
        logger.info("生成异步缴费工单结束");

        if (recvFeeCommInfoIn.isTradeCheckFlag()) {
            logger.info("外围对账交易工单生成开始");
            if ("1".equals(recvFeeCommInfoIn.getTradeHyLogFlag())) {
                TradeHyLog tradeHyLog = recvFeeCheckLogService.genTradeHyLog(recvFeeCommInfoIn,
                        tradeCommInfo.getTradeStaff(), recvFeeCommInfoIn.getChargeId());
                tradeCommResultInfo.setTradeHyLog(tradeHyLog);
            }
            logger.info("外围对账交易工单生成结束");
            tradeCommResultInfo.setAsynWorkMQInfo(tradeAsynOrderService.genAsynWorkMQInfo(asynWork));
        } else {
            tradeCommResultInfo.setAsynWork(asynWork);
        }
        //缴费结果整理返回
        return genTradeCommInfoOut(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
    }


    /**
     * 缴费特殊校验
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     */
    private void specialRecvFeeCheck(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (feeCommService.ifBillConsigning(recvFeeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("托收在途不能前台缴费!");
        }

        if (feeCommService.ifConsignPayMode(recvFeeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("托收用户不能前台缴费!");
        }
        if (feeCommService.ifPrePrintInvoice(recvFeeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("代理商预打发票只能通过预打回款缴费!");
        }

        //电子券充值用户服务状态校验
        feeCommService.elecPresentLimit(tradeCommInfo.getMainUser(), recvFeeCommInfoIn.getPaymentId(),
                tradeCommInfo.getWriteOffRuleInfo());

    }


    private TradeCommInfoOut genTradeCommInfoOut(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        TradeCommInfoOut tradeCommInfoOut = new TradeCommInfoOut();
        //销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        //设置账期信息
        tradeCommInfoOut.setCurCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
        tradeCommInfoOut.setMaxAcctCycleId(writeOffRuleInfo.getMaxAcctCycle().getCycleId());

        //设置用户信息
        User mainUser = tradeCommInfo.getMainUser();
        tradeCommInfoOut.setSerialNumber(mainUser.getSerialNumber());
        tradeCommInfoOut.setNetTypeCode(mainUser.getNetTypeCode());
        tradeCommInfoOut.setUserId(mainUser.getUserId());
        tradeCommInfoOut.setBrandCode(mainUser.getBrandCode());

        //设置账户信息
        Account account= tradeCommInfo.getAccount();
        tradeCommInfoOut.setAcctId(account.getAcctId());
        tradeCommInfoOut.setPayName(account.getPayName());
        tradeCommInfoOut.setPayModeCode(account.getPayModeCode());
        tradeCommInfoOut.setEparchyCode(account.getEparchyCode());
        tradeCommInfoOut.setProvinceCode(recvFeeCommInfoIn.getProvinceCode());

        // 大合账优化，为防止初期启动时无返回值造成实时接口报错，所以临时把相关字段都赋值为0，并不代表真实值。
        // 如开关启动，需细化每一个缴费接口的处理模式，尤其是返回字段如何处理。
        if (recvFeeCommInfoIn.isBigAcctRecvFee()) {
            tradeCommInfoOut.setOuterTradeId(recvFeeCommInfoIn.getTradeId());
            tradeCommInfoOut.setChargeId(recvFeeCommInfoIn.getChargeId());
            tradeCommInfoOut.setPayChargeId(recvFeeCommInfoIn.getChargeId());
            tradeCommInfoOut.setRecvFee(String.valueOf(recvFeeCommInfoIn.getTradeFee()));
            tradeCommInfoOut.setExtendTag("0");
            tradeCommInfoOut.setSpayFee("0");
            tradeCommInfoOut.setAllMoney("0");
            tradeCommInfoOut.setAllNewMoney("0");
            tradeCommInfoOut.setAllBalance("0");
            tradeCommInfoOut.setAllNewBalance("0");
            tradeCommInfoOut.setAllBOweFee("0");
            tradeCommInfoOut.setAimpFee("0");
            tradeCommInfoOut.setAllNewBOweFee("0");
            tradeCommInfoOut.setPreRealFee("0");
            tradeCommInfoOut.setCurRealFee("0");
            tradeCommInfoOut.setAllROweFee("0");
            tradeCommInfoOut.setRsrvStr18("0");
            tradeCommInfoOut.setRsrvDate("00000000000000");
            tradeCommInfoOut.setAcctBalanceId1("0");
            tradeCommInfoOut.setResFee("0");
            return tradeCommInfoOut;
        }

        //缴费日志相关信息
        PayLog payLog = tradeCommResultInfo.getPayLog();
        //如果是关联缴费需要主记录的流水
        if (!StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getRelChargeId())) {
            tradeCommInfoOut.setChargeId(recvFeeCommInfoIn.getRelChargeId());
        } else {
            tradeCommInfoOut.setChargeId(recvFeeCommInfoIn.getChargeId());
        }

        tradeCommInfoOut.setPayChargeId(payLog.getChargeId());
        tradeCommInfoOut.setOuterTradeId(payLog.getOuterTradeId());
        tradeCommInfoOut.setRecvFee(String.valueOf(payLog.getRecvFee()));
        tradeCommInfoOut.setExtendTag(String.valueOf(payLog.getExtendTag()));

        //设置销账快照信息
        FeeWriteSnapLog writeSnapLog = tradeCommInfo.getFeeWriteSnapLog();
        tradeCommInfoOut.setSpayFee(String.valueOf(writeSnapLog.getSpayFee()));
        tradeCommInfoOut.setAllMoney(String.valueOf(writeSnapLog.getAllMoney()));
        tradeCommInfoOut.setAllNewMoney(String.valueOf(writeSnapLog.getAllNewMoney()));
        tradeCommInfoOut.setAllBalance(String.valueOf(writeSnapLog.getAllBalance()));
        tradeCommInfoOut.setAllNewBalance(String.valueOf(writeSnapLog.getAllNewBalance()));
        tradeCommInfoOut.setAllBOweFee(String.valueOf(writeSnapLog.getAllBOweFee()));
        tradeCommInfoOut.setAimpFee(String.valueOf(writeSnapLog.getaImpFee()));
        tradeCommInfoOut.setAllNewBOweFee(String.valueOf(writeSnapLog.getAllNewBOweFee()));
        tradeCommInfoOut.setPreRealFee(String.valueOf(writeSnapLog.getPreRealFee()));
        tradeCommInfoOut.setCurRealFee(String.valueOf(writeSnapLog.getCurRealFee()));
        tradeCommInfoOut.setAllROweFee(String.valueOf(writeSnapLog.getPreRealFee() + writeSnapLog.getCurRealFee()));

        //统一余额播报
        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_SHOW_TYPE);
        if (commPara == null) {
            throw new SkyArkException("没有配置统一余额播报方案参数:ASM_SHOW_TYPE");
        }

        String contactType = "";
        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())) {
            contactType = commPara.getParaCode1();
        }
        tradeCommInfoOut.setConTactType(contactType);

        List<FeeAccountDeposit> depositList = tradeCommInfo.getFeeAccountDeposits();
        //账户当前可用余额
        long rsrvFee18 = 0;
        //储备账本余额
        long storeFee = 0;
        for (FeeAccountDeposit actDeposit : depositList) {
            if ("2".equals(recvFeeCommInfoIn.getWriteoffMode())
                    && ('0' == actDeposit.getPrivateTag() || actDeposit.getUserId().equals(mainUser.getUserId()))) {
                if ('0' == actDeposit.getDepositTypeCode() || '2' == actDeposit.getDepositTypeCode()) {
                    rsrvFee18 += actDeposit.getLeftCanUse();
                }
            } else {
                if ('0' == actDeposit.getDepositTypeCode() || '2' == actDeposit.getDepositTypeCode()) {
                    rsrvFee18 += actDeposit.getLeftCanUse();
                }
            }

            if ('1' == actDeposit.getDepositTypeCode() || '3' == actDeposit.getDepositTypeCode()) {
                storeFee += actDeposit.getMoney() + actDeposit.getRecvFee() - actDeposit.getImpFee() - actDeposit.getUseRecvFee();
            }

        }
        //账户当前可用余额
        tradeCommInfoOut.setRsrvStr18(String.valueOf(rsrvFee18));
        //储备金额
        tradeCommInfoOut.setResFee(String.valueOf(storeFee));
        //本次缴费的帐本标识返回
        tradeCommInfoOut.setAcctBalanceId1(recvFeeCommInfoIn.getAcctBalanceId());

        //交易时间
        if (!StringUtil.isEmpty(payLog.getRecvTime())) {
            tradeCommInfoOut.setRsrvDate(
                    payLog.getRecvTime().substring(0, 4) + payLog.getRecvTime().substring(5, 7)
                            + payLog.getRecvTime().substring(8, 10) + payLog.getRecvTime().substring(11, 13)
                            + payLog.getRecvTime().substring(14, 16) + payLog.getRecvTime().substring(17, 19));
        }
        return tradeCommInfoOut;
    }
}
