package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.pay.dao.TradeAsynOrderDao;
import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.acting.pay.domain.AsynWorkMQInfo;
import com.unicom.acting.pay.domain.PayLogDmn;
import com.unicom.acting.pay.domain.PayLogDmnMQInfo;
import com.unicom.acting.pay.writeoff.service.TradeAsynOrderService;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeAsynOrderServiceImpl implements TradeAsynOrderService {
    @Autowired
    private TradeAsynOrderDao tradeAsynOrderDao;

    @Override
    public PayLogDmnMQInfo genPayLogDmnMQInfo(PayLogDmn payLogDmn) {
        PayLogDmnMQInfo payLogDmnMQInfo = new PayLogDmnMQInfo();
        payLogDmnMQInfo.setTradeId(payLogDmn.getChargeId());
        payLogDmnMQInfo.setTradeTypeCode(payLogDmn.getTradeTypeCode());
        payLogDmnMQInfo.setEparchyCode(payLogDmn.getEparchyCode());
        payLogDmnMQInfo.setProvinceCode(payLogDmn.getProvinceCode());
        payLogDmnMQInfo.setBatchId(payLogDmn.getChargeId());
        payLogDmnMQInfo.setChargeId(payLogDmn.getChargeId());
        payLogDmnMQInfo.setAcctId(payLogDmn.getAcctId());
        payLogDmnMQInfo.setUserId(payLogDmn.getUserId());
        payLogDmnMQInfo.setSerialNumber(payLogDmn.getSerialNumber());
        payLogDmnMQInfo.setWriteoffMode(payLogDmn.getWriteoffMode());
        payLogDmnMQInfo.setLimitMode(payLogDmn.getLimitMode());
        payLogDmnMQInfo.setChannelId(payLogDmn.getChannelId());
        payLogDmnMQInfo.setPaymentId(payLogDmn.getPaymentId());
        payLogDmnMQInfo.setPaymentOp(payLogDmn.getPaymentOp());
        payLogDmnMQInfo.setPayFeeModeCode(payLogDmn.getPayFeeModeCode());
        payLogDmnMQInfo.setRecvFee(payLogDmn.getRecvFee());
        payLogDmnMQInfo.setOuterTradeId(payLogDmn.getOuterTradeId());
        payLogDmnMQInfo.setBillStartCycleId(payLogDmn.getBillStartCycleId());
        payLogDmnMQInfo.setBillEndCycleId(payLogDmn.getBillEndCycleId());
        payLogDmnMQInfo.setStartDate(payLogDmn.getStartDate());
        payLogDmnMQInfo.setMonths(payLogDmn.getMonths());
        payLogDmnMQInfo.setLimitMoney(payLogDmn.getLimitMoney());
        payLogDmnMQInfo.setPaymentReasonCode(payLogDmn.getPaymentReasonCode());
        payLogDmnMQInfo.setExtendTag(payLogDmn.getExtendTag());
        payLogDmnMQInfo.setAcctBalanceId(payLogDmn.getAcctBalanceId());
        payLogDmnMQInfo.setDepositCode(payLogDmn.getDepositCode());
        payLogDmnMQInfo.setPrivateTag(payLogDmn.getPrivateTag());
        payLogDmnMQInfo.setRemark(payLogDmn.getRemark());
        payLogDmnMQInfo.setTradeTime(payLogDmn.getTradeTime());
        payLogDmnMQInfo.setTradeStaffId(payLogDmn.getTradeStaffId());
        payLogDmnMQInfo.setTradeDepartId(payLogDmn.getTradeDepartId());
        payLogDmnMQInfo.setTradeEparchyCode(payLogDmn.getTradeEparchyCode());
        payLogDmnMQInfo.setTradeCityCode(payLogDmn.getTradeCityCode());

        // 一卡充缴费可打发票金额放入备用字段rsrvInfo1
        if (!StringUtil.isEmptyCheckNullStr(payLogDmn.getRsrvInfo1())) {
            payLogDmnMQInfo.setRsrvInfo1(payLogDmn.getRsrvInfo1());
        }

        if (!StringUtil.isEmptyCheckNullStr(payLogDmn.getRelChargeId())) {
            payLogDmnMQInfo.setRelChargeId(payLogDmn.getRelChargeId());
        }

        return payLogDmnMQInfo;
    }

    @Override
    public long insertPayLogDmn(PayLogDmn payLogDmn, String dbType, String routeValue) {
        return tradeAsynOrderDao.insertPayLogDmn(payLogDmn, dbType, routeValue);
    }

    @Override
    public AsynWorkMQInfo genAsynWorkMQInfo(AsynWork asynWork) {
        AsynWorkMQInfo asynWorkMQInfo = new AsynWorkMQInfo();
        asynWorkMQInfo.setWorkId(asynWork.getWorkId());
        asynWorkMQInfo.setChargeId(asynWork.getChargeId());
        asynWorkMQInfo.setTradeTime(asynWork.getTradeTime());
        //外围缴费流水
        asynWorkMQInfo.setRsrvStr1(asynWork.getRsrvStr1());
        asynWorkMQInfo.setRsrvStr3(asynWork.getRsrvStr3());
        asynWorkMQInfo.setDealTag(asynWork.getDealTag());
        asynWorkMQInfo.setWorkTypeCode(asynWork.getWorkTypeCode());
        asynWorkMQInfo.setStartCycleId(asynWork.getStartCycleId());
        asynWorkMQInfo.setEndCycleId(asynWork.getEndCycleId());
        asynWorkMQInfo.setTradeStaffId(asynWork.getTradeStaffId());
        asynWorkMQInfo.setTradeDepartId(asynWork.getTradeDepartId());
        asynWorkMQInfo.setTradeCityCode(asynWork.getTradeCityCode());
        asynWorkMQInfo.setTradeEparchyCode(asynWork.getTradeEparchyCode());
        asynWorkMQInfo.setChannelId(asynWork.getChannelId());
        asynWorkMQInfo.setPaymentId(asynWork.getPaymentId());
        asynWorkMQInfo.setPayFeeModeCode(asynWork.getPayFeeModeCode());
        asynWorkMQInfo.setPaymentOp(asynWork.getPaymentOp());
        asynWorkMQInfo.setWriteoffMode(asynWork.getWriteoffMode());
        asynWorkMQInfo.setRsrvFee1(asynWork.getRsrvFee1());
        asynWorkMQInfo.setAcctId(asynWork.getAcctId());
        asynWorkMQInfo.setUserId(asynWork.getUserId());
        asynWorkMQInfo.setEparchyCode(asynWork.getEparchyCode());
        asynWorkMQInfo.setNetTypeCode(asynWork.getNetTypeCode());
        asynWorkMQInfo.setSerialNumber(asynWork.getSerialNumber());
        asynWorkMQInfo.setRsrvStr2(asynWork.getRsrvStr2());
        asynWorkMQInfo.setRsrvFee8(asynWork.getRsrvFee8());
        return asynWorkMQInfo;
    }

    @Override
    public int insertAsynWork(AsynWork asynWork, String dbType, String routeValue) {
        return tradeAsynOrderDao.insertAsynWork(asynWork, dbType, routeValue);
    }
}
