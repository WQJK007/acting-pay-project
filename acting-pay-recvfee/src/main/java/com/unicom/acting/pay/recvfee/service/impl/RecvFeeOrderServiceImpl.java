package com.unicom.acting.pay.recvfee.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.domain.ActingPayPubDef;
import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.acting.pay.recvfee.service.RecvFeeOrderService;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecvFeeOrderServiceImpl implements RecvFeeOrderService {
    @Autowired
    private SysCommOperFeeService sysCommOperService;

    @Override
    public AsynWork genAsynWork(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        AsynWork asynWork = new AsynWork();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        Account account = tradeCommInfo.getAccount();
        User mainUser = tradeCommInfo.getMainUser();

        String tradeId = sysCommOperService.getActingSequence(ActingPayPubDef.SEQ_OUTERTRADEID_TANNAME,
                ActingPayPubDef.SEQ_OUTERTRADEID_COLUMNNAME, account.getProvinceCode());
        String chargeId = "";
        if (!StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getChargeId())) {
            chargeId = recvFeeCommInfoIn.getChargeId();
        } else {
            chargeId = sysCommOperService.getActingSequence(ActingPayPubDef.SEQ_CHARGEID_TANNAME,
                    ActingPayPubDef.SEQ_CHARGEID_COLUMNNAME, account.getProvinceCode());
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
        asynWork.setAcctId(account.getAcctId());
        asynWork.setUserId(mainUser.getUserId());
        asynWork.setEparchyCode(account.getEparchyCode());
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
        return asynWork;
    }
}
