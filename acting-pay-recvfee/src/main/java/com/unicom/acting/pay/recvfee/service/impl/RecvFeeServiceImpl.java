package com.unicom.acting.pay.recvfee.service.impl;

import com.unicom.acting.pay.domain.TradeCommMQInfo;
import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.fee.writeoff.service.WriteOffFeeService;
import com.unicom.acting.pay.recvfee.service.RecvFeeService;
import com.unicom.acting.pay.writeoff.service.CreditPayService;
import com.unicom.acting.pay.writeoff.service.PayOtherLogService;
import com.unicom.acting.pay.writeoff.service.SmsPayService;
import com.unicom.acting.pay.writeoff.service.WriteOffInDBService;
import com.unicom.acts.pay.domain.Account;
import com.unicom.acts.pay.domain.AccountDeposit;
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
    private WriteOffFeeService writeOffService;
    @Autowired
    private WriteOffInDBService writeOffInDBService;
    @Autowired
    private SmsPayService smsService;
    @Autowired
    private CreditPayService creditService;
    @Autowired
    private SysCommOperFeeService sysCommOperService;
    @Autowired
    private PayOtherLogService payLogService;


    @Override
    public TradeCommInfoOut simpleRecvFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommMQInfo tradeCommMQInfo) {
        //查询用户资料
        writeOffService.genUserDatumInfo(tradeCommInfoIn, tradeCommInfo);
        //查询账期信息
        writeOffService.genEparchyCycleInfo(tradeCommInfo, tradeCommInfoIn.getEparchyCode(), tradeCommInfoIn.getProvinceCode());
        //非大合帐缴费
        if (!tradeCommInfoIn.isBigAcctRecvFee()) {
            return recvFeeSimple(tradeCommInfoIn, tradeCommInfo, tradeCommMQInfo);
        } else {
            return asynRecvFee(tradeCommInfoIn, tradeCommInfo);
        }
    }

    private TradeCommInfoOut recvFeeSimple(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommMQInfo tradeCommMQInfo) {
        Account account = tradeCommInfo.getAccount();
        //writeOffInDBService.genLockAccount(account.getAcctId(), tradeCommInfoIn.getProvinceCode());
        //查询账本
        writeOffService.getAcctBalance(tradeCommInfoIn, tradeCommInfo);
        //查询账单
        writeOffService.getOweBill(tradeCommInfoIn, tradeCommInfo);
        //特殊缴费校验
        writeOffService.specialTradeCheck(tradeCommInfoIn, tradeCommInfo);
        //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
        if (!writeOffService.ifCalcLateFee(tradeCommInfoIn, tradeCommInfo)) {
            //计算滞纳金
            tradeCommInfo.setCalcLateFee(true);
            //获取滞纳金减免工单
            writeOffService.getDerateFeeLog(tradeCommInfoIn, tradeCommInfo);
            //获取账户自定义缴费期
            writeOffService.getAcctPaymentCycle(tradeCommInfo, account.getAcctId(), ActPayPubDef.ACTS_DRDS_DBCONN);
        }
        logger.info("begin calc");
        //缴费前销账计算
        calculateService.calc(tradeCommInfo);
        //设置缴费金额
        writeOffInDBService.setRecvfee(tradeCommInfoIn, tradeCommInfo);
        logger.info("after setRecvfee");
        //缴费后销账计算
        calculateService.recvCalc(tradeCommInfo);
        //生成缴费入库信息
        writeOffInDBService.genInDBInfo(tradeCommInfoIn, tradeCommInfo, tradeCommMQInfo);
        //生成短信信息
        smsService.genSmsInfo(tradeCommInfoIn, tradeCommInfo, tradeCommMQInfo);
        //生成信控工单
        creditService.genCreditInfo(tradeCommInfoIn, tradeCommInfo, tradeCommMQInfo);
        //缴费结果整理返回
        return genTradeCommInfoOut(tradeCommInfoIn, tradeCommInfo);
    }


    private TradeCommInfoOut asynRecvFee(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        logger.info("生成异步缴费工单开始");
        addPayFeeWork(tradeCommInfoIn, tradeCommInfo);
        logger.info("生成异步缴费工单结束");
        //生成外围交易对账日志
        if ("1".equals(tradeCommInfoIn.getTradeHyLogFlag())) {
            addTradeHyLog(tradeCommInfoIn, tradeCommInfo.getTradeStaff(), tradeCommInfoIn.getChargeId(), tradeCommInfoIn.getProvinceCode());
            logger.info("外围对账交易工单生成结束");
        }
        //缴费结果整理返回
        return genTradeCommInfoOut(tradeCommInfoIn, tradeCommInfo);
    }

    private void addPayFeeWork(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        AsynWork asynWork = new AsynWork();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        Account account = tradeCommInfo.getAccount();
        User mainUser = tradeCommInfo.getMainUser();

        String tradeId = sysCommOperService.getSequence(account.getEparchyCode(), ActPayPubDef.SEQ_TRADE_ID, account.getProvinceCode());
        String chargeId = "";
        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getChargeId())) {
            chargeId = tradeCommInfoIn.getChargeId();
        } else {
            chargeId = sysCommOperService.getSequence(account.getEparchyCode(), ActPayPubDef.SEQ_CHARGE_ID, account.getProvinceCode());
        }
        tradeCommInfoIn.setChargeId(chargeId);
        asynWork.setWorkId(tradeId);
        asynWork.setChargeId(chargeId);
        asynWork.setTradeTime(writeOffRuleInfo.getSysdate());
        //外围缴费流水
        asynWork.setRsrvStr1(tradeCommInfoIn.getTradeId());
        asynWork.setRsrvStr3("1");
        asynWork.setDealTag("0");
        asynWork.setWorkTypeCode("2");
        asynWork.setStartCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
        asynWork.setEndCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
        asynWork.setTradeStaffId(tradeCommInfo.getTradeStaff().getStaffId());
        asynWork.setTradeDepartId(tradeCommInfo.getTradeStaff().getDepartId());
        asynWork.setTradeCityCode(tradeCommInfo.getTradeStaff().getCityCode());
        asynWork.setTradeEparchyCode(tradeCommInfo.getTradeStaff().getEparchyCode());
        asynWork.setChannelId(tradeCommInfoIn.getChannelId());
        asynWork.setPaymentId(String.valueOf(tradeCommInfoIn.getPaymentId()));
        asynWork.setPayFeeModeCode(String.valueOf(tradeCommInfoIn.getPayFeeModeCode()));
        asynWork.setPaymentOp("16000");
        asynWork.setWriteoffMode(tradeCommInfoIn.getWriteoffMode());
        asynWork.setRsrvFee1(String.valueOf(tradeCommInfoIn.getTradeFee()));
        asynWork.setAcctId(account.getAcctId());
        asynWork.setUserId(mainUser.getUserId());
        asynWork.setEparchyCode(account.getEparchyCode());
        asynWork.setNetTypeCode(mainUser.getNetTypeCode());
        asynWork.setSerialNumber(mainUser.getSerialNumber());
        //自然人
        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getNpFlag())) {
            asynWork.setRsrvStr2(tradeCommInfoIn.getNpFlag());
        }
        //98卡
        if (100006 == tradeCommInfoIn.getPaymentId()) {
            asynWork.setRsrvFee8(String.valueOf(tradeCommInfoIn.getInvoiceFee()));
        }
        payLogService.insertPayFeeWork(asynWork, account.getProvinceCode());
    }

    private TradeCommInfoOut genTradeCommInfoOut(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
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
        Account account = tradeCommInfo.getAccount();
        tradeCommInfoOut.setAcctId(account.getAcctId());
        tradeCommInfoOut.setPayName(account.getPayName());
        tradeCommInfoOut.setPayModeCode(account.getPayModeCode());
        tradeCommInfoOut.setEparchyCode(account.getEparchyCode());
        tradeCommInfoOut.setProvinceCode(tradeCommInfoIn.getProvinceCode());

        // 大合账优化，为防止初期启动时无返回值造成实时接口报错，所以临时把相关字段都赋值为0，并不代表真实值。
        // 如开关启动，需细化每一个缴费接口的处理模式，尤其是返回字段如何处理。
        if (tradeCommInfoIn.isBigAcctRecvFee()) {
            tradeCommInfoOut.setOuterTradeId(tradeCommInfoIn.getTradeId());
            tradeCommInfoOut.setChargeId(tradeCommInfoIn.getChargeId());
            tradeCommInfoOut.setPayChargeId(tradeCommInfoIn.getChargeId());
            tradeCommInfoOut.setRecvFee(String.valueOf(tradeCommInfoIn.getTradeFee()));
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
        PayLog payLog = tradeCommInfo.getPayLog();
        //如果是关联缴费需要主记录的流水
        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getRelChargeId())) {
            tradeCommInfoOut.setChargeId(tradeCommInfoIn.getRelChargeId());
        } else {
            tradeCommInfoOut.setChargeId(tradeCommInfoIn.getChargeId());
        }

        tradeCommInfoOut.setPayChargeId(payLog.getChargeId());
        tradeCommInfoOut.setOuterTradeId(payLog.getOuterTradeId());
        tradeCommInfoOut.setRecvFee(String.valueOf(payLog.getRecvFee()));
        tradeCommInfoOut.setExtendTag(String.valueOf(payLog.getExtendTag()));

        //设置销账快照信息
        WriteSnapLog writeSnapLog = tradeCommInfo.getWriteSnapLog();
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
        CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_SHOW_TYPE);
        if (commPara == null) {
            throw new SkyArkException("没有配置统一余额播报方案参数:ASM_SHOW_TYPE");
        }

        String contactType = "";
        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())) {
            contactType = commPara.getParaCode1();
        }
        tradeCommInfoOut.setConTactType(contactType);

        List<AccountDeposit> depositList = tradeCommInfo.getAccountDeposits();
        //账户当前可用余额
        long rsrvFee18 = 0;
        //储备账本余额
        long storeFee = 0;
        for (AccountDeposit accountDeposit : depositList) {
            if ("2".equals(tradeCommInfoIn.getWriteoffMode())
                    && ('0' == accountDeposit.getPrivateTag() || accountDeposit.getUserId().equals(mainUser.getUserId()))) {
                if ('0' == accountDeposit.getDepositTypeCode() || '2' == accountDeposit.getDepositTypeCode()) {
                    rsrvFee18 += accountDeposit.getLeftCanUse();
                }
            } else {
                if ('0' == accountDeposit.getDepositTypeCode() || '2' == accountDeposit.getDepositTypeCode()) {
                    rsrvFee18 += accountDeposit.getLeftCanUse();
                }
            }

            if ('1' == accountDeposit.getDepositTypeCode() || '3' == accountDeposit.getDepositTypeCode()) {
                storeFee += accountDeposit.getMoney() + accountDeposit.getRecvFee() - accountDeposit.getImpFee() - accountDeposit.getUseRecvFee();
            }

        }
        //账户当前可用余额
        tradeCommInfoOut.setRsrvStr18(String.valueOf(rsrvFee18));
        //储备金额
        tradeCommInfoOut.setResFee(String.valueOf(storeFee));
        //本次缴费的帐本标识返回
        tradeCommInfoOut.setAcctBalanceId1(tradeCommInfoIn.getAcctBalanceId());

        //交易时间
        if (!StringUtil.isEmpty(payLog.getRecvTime())) {
            tradeCommInfoOut.setRsrvDate(
                    payLog.getRecvTime().substring(0, 4) + payLog.getRecvTime().substring(5, 7)
                            + payLog.getRecvTime().substring(8, 10) + payLog.getRecvTime().substring(11, 13)
                            + payLog.getRecvTime().substring(14, 16) + payLog.getRecvTime().substring(17, 19));
        }
        return tradeCommInfoOut;
    }

    private void addTradeHyLog(TradeCommInfoIn tradeCommInfoIn, Staff staff, String chargeId, String provinceCode) {
        TradeHyLog tradeHyLog = new TradeHyLog();
        tradeHyLog.setOperId(staff.getStaffId());
        tradeHyLog.setChannelCode(staff.getDepartId());
        tradeHyLog.setEparchyCode(staff.getEparchyCode());
        tradeHyLog.setCityCode(staff.getCityCode());
        tradeHyLog.setProvinceCode(staff.getProvinceCode());
        tradeHyLog.setTradeId(tradeCommInfoIn.getTradeId());
        tradeHyLog.setOuterTradeTime(tradeCommInfoIn.getTradeTime());
        tradeHyLog.setSerialNumber(tradeCommInfoIn.getSerialNumber());
        tradeHyLog.setNetTypeCode(tradeCommInfoIn.getNetTypeCode());
        tradeHyLog.setChannelId(tradeCommInfoIn.getChannelId());
        tradeHyLog.setPaymentId(tradeCommInfoIn.getPaymentId());
        tradeHyLog.setPayFeeModeCode(tradeCommInfoIn.getPayFeeModeCode());
        tradeHyLog.setTradeFee(String.valueOf(tradeCommInfoIn.getTradeFee()));
        tradeHyLog.setChargerId(chargeId);
        payLogService.insertTradeHyLog(tradeHyLog, provinceCode);
    }

}
