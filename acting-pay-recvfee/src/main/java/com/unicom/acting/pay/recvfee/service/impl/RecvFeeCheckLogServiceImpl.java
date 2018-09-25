package com.unicom.acting.pay.recvfee.service.impl;

import com.unicom.acting.fee.domain.Staff;
import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.acting.pay.recvfee.service.RecvFeeCheckLogService;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.service.TradeCheckLogService;
import com.unicom.skyark.component.exception.SkyArkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecvFeeCheckLogServiceImpl implements RecvFeeCheckLogService {
    @Autowired
    private TradeCheckLogService tradeCheckLogService;

    @Override
    public TradeHyLog genTradeHyLog(RecvFeeCommInfoIn recvFeeCommInfoIn, Staff staff, String chargeId) {
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
        return tradeHyLog;
    }

    @Override
    public void insertTradeHyLog(TradeHyLog tradeHyLog, String dbType) {
        try {
            tradeCheckLogService.insertTradeHyLog(tradeHyLog);
        } catch (Exception ex) {
            throw new SkyArkException("新增TradeHyLog记录失败");
        }
    }
}
