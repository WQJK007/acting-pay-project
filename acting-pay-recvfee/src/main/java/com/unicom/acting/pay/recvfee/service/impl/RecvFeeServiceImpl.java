package com.unicom.acting.pay.recvfee.service.impl;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoOut;
import com.unicom.acting.fee.writeoff.service.FeeCommService;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.acting.pay.domain.PayOtherLog;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.service.*;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
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
    private TradeCommService tradeCommService;
    @Autowired
    private TradeAsynOrderService tradeAsynOrderService;
    @Autowired
    private TradeCheckLogService tradeCheckLogService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private CreditService creditService;
    @Autowired
    private SysCommOperFeeService sysCommOperService;


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
            return asynRecvFee(recvFeeCommInfoIn, tradeCommInfo);
        }
    }

    private TradeCommInfoOut recvFeeSimple(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();
        //tradeCommService.genLockAccount(feeAccount.getAcctId(), recvFeeCommInfoIn.getProvinceCode());
        //查询账本
        feeCommService.getAcctBalance(recvFeeCommInfoIn, tradeCommInfo);
        //查询账单
        feeCommService.getOweBill(recvFeeCommInfoIn, tradeCommInfo);
        //特殊业务校验
        feeCommService.specialBusiCheck(recvFeeCommInfoIn, tradeCommInfo);
        //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
        if (!feeCommService.ifCalcLateFee(recvFeeCommInfoIn, tradeCommInfo)) {
            //计算滞纳金
            tradeCommInfo.setCalcLateFee(true);
            //获取滞纳金减免工单
            feeCommService.getFeeDerateLateFeeLog(recvFeeCommInfoIn, tradeCommInfo);
            //获取账户自定义缴费期  DbTypes.ACTS_DRDS
            feeCommService.getAcctPaymentCycle(tradeCommInfo, feeAccount.getAcctId(), DbTypes.ACTS_DRDS);
        }
        logger.info("begin calc");
        //缴费前销账计算
        calculateService.calc(tradeCommInfo);
        //设置缴费金额
        tradeCommService.setRecvfee(recvFeeCommInfoIn, tradeCommInfo);
        logger.info("after setRecvfee");
        //缴费后销账计算
        calculateService.recvCalc(tradeCommInfo);
        //生成缴费入库信息
        tradeCommService.genInDBInfo(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        //生成短信信息
        smsService.genSmsInfo(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        //生成信控工单
        creditService.genCreditInfo(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        //缴费结果整理返回
        return genTradeCommInfoOut(recvFeeCommInfoIn, tradeCommInfo);
    }



    private TradeCommInfoOut asynRecvFee(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        logger.info("生成异步缴费工单开始");
        addPayFeeWork(recvFeeCommInfoIn, tradeCommInfo);
        logger.info("生成异步缴费工单结束");
        //生成外围交易对账日志
        if ("1".equals(recvFeeCommInfoIn.getTradeHyLogFlag())) {
            addTradeHyLog(recvFeeCommInfoIn, tradeCommInfo.getTradeStaff(), recvFeeCommInfoIn.getChargeId(), recvFeeCommInfoIn.getProvinceCode());
            logger.info("外围对账交易工单生成结束");
        }
        //缴费结果整理返回
        return genTradeCommInfoOut(recvFeeCommInfoIn, tradeCommInfo);
    }

    private void addPayFeeWork(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        AsynWork asynWork = new AsynWork();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();
        User mainUser = tradeCommInfo.getMainUser();

        String tradeId = sysCommOperService.getSequence(feeAccount.getEparchyCode(), ActPayPubDef.SEQ_TRADE_ID, feeAccount.getProvinceCode());
        String chargeId = "";
        if (!StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getChargeId())) {
            chargeId = recvFeeCommInfoIn.getChargeId();
        } else {
            chargeId = sysCommOperService.getSequence(feeAccount.getEparchyCode(), ActPayPubDef.SEQ_CHARGE_ID, feeAccount.getProvinceCode());
        }
        recvFeeCommInfoIn.setChargeId(chargeId);
        asynWork.setWorkId(tradeId);
        asynWork.setChargeId(chargeId);
        asynWork.setTradeTime(writeOffRuleInfo.getSysdate());
        //外围缴费流水
        asynWork.setRsrvStr1(recvFeeCommInfoIn.getTradeId());
        asynWork.setRsrvStr3("1");
        asynWork.setDealTag("0");
        asynWork.setWorkTypeCode("2");
        asynWork.setStartCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
        asynWork.setEndCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
        asynWork.setTradeStaffId(tradeCommInfo.getTradeStaff().getStaffId());
        asynWork.setTradeDepartId(tradeCommInfo.getTradeStaff().getDepartId());
        asynWork.setTradeCityCode(tradeCommInfo.getTradeStaff().getCityCode());
        asynWork.setTradeEparchyCode(tradeCommInfo.getTradeStaff().getEparchyCode());
        asynWork.setChannelId(recvFeeCommInfoIn.getChannelId());
        asynWork.setPaymentId(String.valueOf(recvFeeCommInfoIn.getPaymentId()));
        asynWork.setPayFeeModeCode(String.valueOf(recvFeeCommInfoIn.getPayFeeModeCode()));
        asynWork.setPaymentOp("16000");
        asynWork.setWriteoffMode(recvFeeCommInfoIn.getWriteoffMode());
        asynWork.setRsrvFee1(String.valueOf(recvFeeCommInfoIn.getTradeFee()));
        asynWork.setAcctId(feeAccount.getAcctId());
        asynWork.setUserId(mainUser.getUserId());
        asynWork.setEparchyCode(feeAccount.getEparchyCode());
        asynWork.setNetTypeCode(mainUser.getNetTypeCode());
        asynWork.setSerialNumber(mainUser.getSerialNumber());
        //自然人
        if (!StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getNpFlag())) {
            asynWork.setRsrvStr2(recvFeeCommInfoIn.getNpFlag());
        }
        //98卡
        if (100006 == recvFeeCommInfoIn.getPaymentId()) {
            asynWork.setRsrvFee8(String.valueOf(recvFeeCommInfoIn.getInvoiceFee()));
        }
        tradeAsynOrderService.insertAsynWork(asynWork, DbTypes.ACT_ORDER_RDS, feeAccount.getProvinceCode());
    }

    private TradeCommInfoOut genTradeCommInfoOut(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
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
        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();
        tradeCommInfoOut.setAcctId(feeAccount.getAcctId());
        tradeCommInfoOut.setPayName(feeAccount.getPayName());
        tradeCommInfoOut.setPayModeCode(feeAccount.getPayModeCode());
        tradeCommInfoOut.setEparchyCode(feeAccount.getEparchyCode());
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
        FeePayLog payLog = tradeCommInfo.getFeePayLog();
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
        CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.ASM_SHOW_TYPE);
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

    private void addTradeHyLog(RecvFeeCommInfoIn recvFeeCommInfoIn, Staff staff, String chargeId, String provinceCode) {
        TradeHyLog tradeHyLog = new TradeHyLog();
        tradeHyLog.setOperId(staff.getStaffId());
        tradeHyLog.setChannelCode(staff.getDepartId());
        tradeHyLog.setEparchyCode(staff.getEparchyCode());
        tradeHyLog.setCityCode(staff.getCityCode());
        tradeHyLog.setProvinceCode(staff.getProvinceCode());
        tradeHyLog.setTradeId(recvFeeCommInfoIn.getTradeId());
        tradeHyLog.setOuterTradeTime(recvFeeCommInfoIn.getTradeTime());
        tradeHyLog.setSerialNumber(recvFeeCommInfoIn.getSerialNumber());
        tradeHyLog.setNetTypeCode(recvFeeCommInfoIn.getNetTypeCode());
        tradeHyLog.setChannelId(recvFeeCommInfoIn.getChannelId());
        tradeHyLog.setPaymentId(recvFeeCommInfoIn.getPaymentId());
        tradeHyLog.setPayFeeModeCode(recvFeeCommInfoIn.getPayFeeModeCode());
        tradeHyLog.setTradeFee(String.valueOf(recvFeeCommInfoIn.getTradeFee()));
        tradeHyLog.setChargerId(chargeId);
        tradeCheckLogService.insertTradeHyLog(tradeHyLog, provinceCode);
    }


    private PayOtherLog genPayOtherLog(CarrierInfo carrierInfo, PayLog payLog) {
        PayOtherLog payOtherLog = new PayOtherLog();
        payOtherLog.setChargeId(payLog.getChargeId());
        payOtherLog.setCancelTag('0');
        payOtherLog.setEparchyCode(payLog.getEparchyCode());
        payOtherLog.setProvinceCode(payLog.getProvinceCode());
        payOtherLog.setCarrierTime(payLog.getRecvTime());
        payOtherLog.setConfTimeLimit(0);
        payOtherLog.setCarrierId(carrierInfo.getCarrierId());
        payOtherLog.setCarrierCode(String.valueOf(carrierInfo.getCarrierCode()));
        payOtherLog.setBankCode(carrierInfo.getBankCode());
        payOtherLog.setCarrierUseName(carrierInfo.getCarrierUseName());
        return payOtherLog;
    }

}
