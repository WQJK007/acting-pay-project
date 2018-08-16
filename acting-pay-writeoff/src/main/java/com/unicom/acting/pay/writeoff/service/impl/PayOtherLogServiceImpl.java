package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.pay.dao.PayOtherLogDao;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.service.PayOtherLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 缴费日志表和帐务业务后台处理表通过JDBC方式操作
 *
 * @author Administrators
 */
@Service
public class PayOtherLogServiceImpl implements PayOtherLogService {
    @Autowired
    private PayOtherLogDao payLogPayDao;

    @Override
    public long insertPayLogDmn(PayLogDmn payLogDmn, String provinceCode) {
        return payLogPayDao.insertPayLogDmn(payLogDmn, provinceCode);
    }


    @Override
    public void insertCLPayLog(List<CLPayLog> cLPayLogList, String provinceCode) {
        if (CollectionUtils.isEmpty(cLPayLogList)) {
            return;
        }
        payLogPayDao.insertCLPayLog(cLPayLogList, provinceCode);
    }

    @Override
    public PayOtherLog genPayOtherLog(CarrierInfo carrierInfo, PayLog payLog) {
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

    @Override
    public long insertPayOtherLog(PayOtherLog payOtherLog, String provinceCode) {
        return payLogPayDao.insertPayOtherLog(payOtherLog, provinceCode);
    }


    @Override
    public int insertTradeHyLog(TradeHyLog tradeHyLog, String provinceCode) {
        return payLogPayDao.insertTradeHyLog(tradeHyLog, provinceCode);
    }

    @Override
    public int insertPayFeeWork(AsynWork asynWork, String provinceCode) {
        return payLogPayDao.insertPayFeeWork(asynWork, provinceCode);
    }

}
