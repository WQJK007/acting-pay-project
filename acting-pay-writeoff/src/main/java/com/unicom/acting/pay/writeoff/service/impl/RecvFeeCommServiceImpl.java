package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.calc.service.DepositCalcService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.domain.PayOtherLog;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.service.*;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * 缴费交易信息
 *
 * @author Wangkh
 */

@Service
public class RecvFeeCommServiceImpl implements RecvFeeCommService {
    private static final Logger logger = LoggerFactory.getLogger(RecvFeeCommServiceImpl.class);
    @Autowired
    private DepositCalcService depositCalcService;
    @Autowired
    private AcctDepositPayService acctDepositPayService;
    @Autowired
    private TradeCommService tradeCommService;


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

        //生成缴费日志时使用
        recvFeeCommInfoIn.setPaymentOp(16000);
    }

    @Override
    public void genRecvDBInfo(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        //地市销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //生成交费日志信息
        PayLog payLog = genRecvPayLog(recvFeeCommInfoIn, tradeCommInfo);
        tradeCommResultInfo.setPayLog(payLog);

        //生成缴费其他日志表
        if (recvFeeCommInfoIn.getCarrierInfo() != null
                && !StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getCarrierInfo().getCarrierId())) {
            tradeCommResultInfo.setPayOtherLog(genPayOtherLog(recvFeeCommInfoIn.getCarrierInfo(), payLog));
        }

        //抵扣或者补收期间并且对应临时表中有数据
        if (tradeCommInfo.isSpecialCycleStatus()) {
            if (payLog.getExtendTag() != '0') {
                throw new SkyArkException("月结期间不允许进行异地缴费!");
            }
            if (recvFeeCommInfoIn.isTradeCheckFlag()) {
                //涉及第三方对账只能发送MQ消息
                tradeCommResultInfo.setPayLogDmnMQInfo(genPayLogDmnMQInfo(recvFeeCommInfoIn, payLog));
            } else {
                PayLogDmn payLogDmn = genPayLogDmn(recvFeeCommInfoIn, payLog);
                tradeCommResultInfo.setPayLogDmn(payLogDmn);
            }
            return;
        }

        //生成缴费日志MQ消息
        tradeCommResultInfo.setPayLogMQInfo(tradeCommService.genPayLogMQInfo(payLog));

        //更新交费快照信息
        WriteSnapLog writeSnapLog = tradeCommService.genWriteSnapLog(recvFeeCommInfoIn,
                tradeCommInfo.getFeeWriteSnapLog(),payLog, writeOffRuleInfo.getCurCycle().getCycleId());
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
        List<CLPayLog> clPayLogs = tradeCommService.genCLPaylog(writeOffLogs, payLog, recvFeeCommInfoIn.getHeaderGray());
        tradeCommResultInfo.setClPayLogs(clPayLogs);

        //更新存折入库信息
        List<FeeAccountDeposit> deposits = tradeCommInfo.getFeeAccountDeposits();
        acctDepositPayService.updateDepositInfo(deposits, writeSnapLog.getAllNewBOweFee(),
                writeOffRuleInfo.getSysdate(), writeOffRuleInfo.getMaxAcctCycle().getCycleId());
        tradeCommResultInfo.setDepositMQInfos(acctDepositPayService.genDepositMQInfo(deposits));
    }

    /**
     * 缴费日志
     *
     * @param recvFeeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private PayLog genRecvPayLog(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        PayLog payLog = tradeCommService.genPayLog(recvFeeCommInfoIn, tradeCommInfo);
        if ("02".equals(recvFeeCommInfoIn.getNpFlag())) {
            payLog.setNpTag(recvFeeCommInfoIn.getNpFlag());
        }
        return payLog;
    }

    /**
     * 缴费其他日志表
     *
     * @param carrierInfo
     * @param payLog
     * @return
     */
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



    /**
     * 生成账务后台交易工单
     *
     * @param recvFeeCommInfoIn
     * @param payLog
     * @return
     */
    private PayLogDmn genPayLogDmn(RecvFeeCommInfoIn recvFeeCommInfoIn, PayLog payLog) {
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
        payLogDmn.setWriteoffMode(StringUtil.firstOfString(recvFeeCommInfoIn.getWriteoffMode()));
        payLogDmn.setLimitMode(StringUtil.firstOfString(recvFeeCommInfoIn.getLimitMode()));
        payLogDmn.setChannelId(payLog.getChannelId());
        payLogDmn.setPaymentId(payLog.getPaymentId());
        payLogDmn.setPaymentOp(payLog.getPaymentOp());
        payLogDmn.setPayFeeModeCode(payLog.getPayFeeModeCode());
        payLogDmn.setRecvFee(payLog.getRecvFee());
        payLogDmn.setOuterTradeId(payLog.getOuterTradeId());
        payLogDmn.setBillStartCycleId(recvFeeCommInfoIn.getBillStartCycleId());
        payLogDmn.setBillEndCycleId(recvFeeCommInfoIn.getBillEndCycleId());
        payLogDmn.setStartDate(recvFeeCommInfoIn.getDepositStartDate());
        payLogDmn.setMonths(recvFeeCommInfoIn.getMonths());
        payLogDmn.setLimitMoney(recvFeeCommInfoIn.getLimitMoney());
        payLogDmn.setPaymentReasonCode(StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getReasonCode()) ? 0 : Long.parseLong(recvFeeCommInfoIn.getReasonCode()));
        payLogDmn.setExtendTag(payLog.getExtendTag());
        payLogDmn.setAcctBalanceId(recvFeeCommInfoIn.getAcctBalanceId());
        payLogDmn.setDepositCode(recvFeeCommInfoIn.getDepositCode());
        payLogDmn.setPrivateTag(StringUtil.firstOfString(recvFeeCommInfoIn.getPrivateTag()));
        payLogDmn.setRemark(recvFeeCommInfoIn.getRemark());
        payLogDmn.setTradeTime(payLog.getRecvTime());
        payLogDmn.setTradeStaffId(payLog.getRecvStaffId());
        payLogDmn.setTradeDepartId(payLog.getRecvDepartId());
        payLogDmn.setTradeEparchyCode(payLog.getRecvEparchyCode());
        payLogDmn.setTradeCityCode(payLog.getRecvCityCode());

        // 一卡充缴费可打发票金额放入备用字段rsrvInfo1
        if (100006 == payLog.getPaymentId()) {
            payLogDmn.setRsrvInfo1(String.valueOf(recvFeeCommInfoIn.getInvoiceFee()));
        }
        payLogDmn.setRelChargeId(recvFeeCommInfoIn.getRelChargeId());
        return payLogDmn;
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
