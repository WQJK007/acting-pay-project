package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.acting.fee.domain.AsynWork;
import com.unicom.acting.fee.domain.CLPayLog;
import com.unicom.acting.fee.domain.PayLogDmn;
import com.unicom.acting.fee.domain.PayOtherLog;
import com.unicom.acting.pay.dao.PayOtherLogDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PayOtherLogDaoImpl extends JdbcBaseDao implements PayOtherLogDao {
    @Override
    public void insertCLPayLog(List<CLPayLog> cLPayLogList, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_CLPAYLOG (CHARGE_ID,CL_PAYLOG_ID,PROV_CODE,EPARCHY_CODE,");
        sql.append("NET_TYPE_CODE,AREA_CODE,ACCT_ID,USER_ID,OLD_ACCT_ID,OLD_USER_ID,");
        sql.append("SERIAL_NUMBER,RECV_FEE,CANCEL_TAG,DOWN_TAG,OUTER_TRADE_ID,OPERATE_TIME,");
        sql.append("RECV_TIME,RECV_EPARCHY_CODE,RECV_CITY_CODE,RECV_DEPART_ID,RECV_STAFF_ID,");
        sql.append("CANCEL_STAFF_ID,CANCEL_DEPART_ID,CANCEL_CITY_CODE,CANCEL_EPARCHY_CODE,");
        sql.append("CANCEL_TIME,CANCEL_CHARGE_ID,DEAL_TYPE,PAYMENT_ID,RSRV_INFO1,RSRV_INFO2,");
        sql.append("RSRV_INFO3) VALUES (:VCHARGE_ID,:VCL_PAYLOG_ID,:VPROV_CODE,:VEPARCHY_CODE,");
        sql.append(":VNET_TYPE_CODE,:VAREA_CODE,:VACCT_ID,:VUSER_ID,:VOLD_ACCT_ID,:VOLD_USER_ID,");
        sql.append(":VSERIAL_NUMBER,:VRECV_FEE,:VCANCEL_TAG,:VDOWN_TAG,:VOUTER_TRADE_ID,");
        sql.append("SYSDATE,SYSDATE,:VRECV_EPARCHY_CODE,:VRECV_CITY_CODE,:VRECV_DEPART_ID,");
        sql.append(":VRECV_STAFF_ID,'','','','',NULL,'','0',:VPAYMENT_ID,'','','') ");
        List params = new ArrayList();
        for (CLPayLog pCLPayLog : cLPayLogList) {
            Map<String, String> param = new HashMap<>();
            param.put("VCHARGE_ID", pCLPayLog.getChargeId());
            param.put("VCL_PAYLOG_ID", pCLPayLog.getClPaylogId());
            param.put("VPROV_CODE", pCLPayLog.getProvinceCode());
            param.put("VEPARCHY_CODE", pCLPayLog.getEparchyCode());
            param.put("VNET_TYPE_CODE", pCLPayLog.getNetTypeCode());
            param.put("VAREA_CODE", pCLPayLog.getAreaCode());
            param.put("VACCT_ID", pCLPayLog.getAcctId());
            param.put("VUSER_ID", pCLPayLog.getUserId());
            param.put("VOLD_ACCT_ID", pCLPayLog.getOldAcctId());
            param.put("VOLD_USER_ID", pCLPayLog.getOldUserId());
            param.put("VSERIAL_NUMBER", pCLPayLog.getSerialNumber());
            param.put("VRECV_FEE", String.valueOf(pCLPayLog.getRecvFee()));
            param.put("VCANCEL_TAG", String.valueOf(pCLPayLog.getCancelTag()));
            param.put("VDOWN_TAG", String.valueOf(pCLPayLog.getDownTag()));
            param.put("VOUTER_TRADE_ID", pCLPayLog.getOuterTradeId());
            param.put("VRECV_EPARCHY_CODE", pCLPayLog.getRecvEparchyCode());
            param.put("VRECV_CITY_CODE", pCLPayLog.getRecvCityCode());
            param.put("VRECV_DEPART_ID", pCLPayLog.getRecvDepartId());
            param.put("VRECV_STAFF_ID", pCLPayLog.getRecvStaffId());
            param.put("VPAYMENT_ID", String.valueOf(pCLPayLog.getPaymentId()));
            params.add(param);
        }
        this.getJdbcTemplate(provinceCode).batchUpdate(sql.toString(), (Map<String, String>[]) params.toArray(new Map[params.size()]));
    }

    @Override
    public long insertPayOtherLog(PayOtherLog payOtherLog, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_PAYOTHER_LOG(CHARGE_ID,CARRIER_ID,");
        sql.append("CARRIER_CODE,OPERATE_TIME,PROVINCE_CODE,EPARCHY_CODE,CARRIER_VEST,");
        sql.append("CARRIER_USE_NAME,CARRIER_USE_PHONE,CARRIER_USE_PASSID,CARRIER_STATUS,");
        sql.append("CONF_TIME_LIMIT,BANK_ACCT_NO,BANK_CODE,BANK_NAME,CANCEL_TAG,RSRV_INFO2,");
        sql.append("RSRV_INFO1,RSRV_FEE2,RSRV_FEE1) VALUES (:VCHARGE_ID,:VCARRIER_ID,");
        sql.append(":VCARRIER_CODE,STR_STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),");
        sql.append(":VPROVINCE_CODE,:VEPARCHY_CODE,:VCARRIER_VEST,:VCARRIER_USE_NAME,");
        sql.append(":VCARRIER_USE_PHONE,:VCARRIER_USE_PASSID,:VCARRIER_STATUS,");
        sql.append(":VCONF_TIME_LIMIT,:VBANK_ACCT_NO,:VBANK_CODE,:VBANK_NAME,");
        sql.append(":VCANCEL_TAG,:VRSRV_INFO2,:VRSRV_INFO1,:VRSRV_FEE2,:VRSRV_FEE1");
        Map<String, String> param = new HashMap<>();
        param.put("VCHARGE_ID", payOtherLog.getChargeId());
        param.put("VCARRIER_ID", payOtherLog.getCarrierId());
        param.put("VCARRIER_CODE", payOtherLog.getCarrierCode());
        param.put("VOPERATE_TIME", payOtherLog.getCarrierTime());
        param.put("VPROVINCE_CODE", payOtherLog.getProvinceCode());
        param.put("VEPARCHY_CODE", payOtherLog.getEparchyCode());
        param.put("VCARRIER_VEST", payOtherLog.getCarrierVest());
        param.put("VCARRIER_USE_NAME", payOtherLog.getCarrierUseName());
        param.put("VCARRIER_USE_PHONE", payOtherLog.getCarrierUsePhone());
        param.put("VCARRIER_USE_PASSID", payOtherLog.getCarrierUsePassId());
        param.put("VCARRIER_STATUS", String.valueOf(payOtherLog.getCarrierStatus()));
        param.put("VCONF_TIME_LIMIT", String.valueOf(payOtherLog.getConfTimeLimit()));
        param.put("VBANK_ACCT_NO", payOtherLog.getBankAcctNo());
        param.put("VBANK_CODE", payOtherLog.getBankCode());
        param.put("VBANK_NAME", payOtherLog.getBankName());
        param.put("VCANCEL_TAG", String.valueOf(payOtherLog.getCancelTag()));
        param.put("VRSRV_INFO2", payOtherLog.getRsrvInfo2());
        param.put("VRSRV_INFO1", payOtherLog.getRsrvInfo1());
        param.put("VRSRV_FEE2", String.valueOf(payOtherLog.getRsrvFee2()));
        param.put("VRSRV_FEE1", String.valueOf(payOtherLog.getRsrvFee1()));
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }

    @Override
    public long insertPayLogDmn(PayLogDmn payLogDmn, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_PAYLOG_DMN(EPARCHY_CODE,PROVINCE_CODE,TRADE_ID,");
        sql.append("TRADE_TYPE_CODE,BATCH_ID,PRIORITY,CHARGE_ID,ACCT_ID,USER_ID,");
        sql.append("SERIAL_NUMBER,WRITEOFF_MODE,CHANNEL_ID,PAYMENT_ID,PAYMENT_OP,");
        sql.append("PAY_FEE_MODE_CODE,RECV_FEE,OUTER_TRADE_ID,BILL_START_CYCLE_ID,");
        sql.append("BILL_END_CYCLE_ID,START_DATE,END_DATE,MONTHS,LIMIT_MONEY,PAYMENT_REASON_CODE,");
        sql.append("EXTEND_TAG,ACTION_CODE,ACTION_EVENT_ID,ACCT_BALANCE_ID,DEPOSIT_CODE,");
        sql.append("PRIVATE_TAG,REMARK,INPUT_NO,INPUT_MODE,ACCT_ID2,USER_ID2,DEPOSIT_CODE2,");
        sql.append("REL_CHARGE_ID,TRADE_TIME,TRADE_EPARCHY_CODE,TRADE_CITY_CODE,TRADE_DEPART_ID,");
        sql.append("TRADE_STAFF_ID,CANCEL_TAG,DEAL_TAG,DEAL_TIME,RESULT_CODE,RESULT_INFO,");
        sql.append("RSRV_FEE1,RSRV_FEE2,RSRV_INFO1,LIMIT_MODE,FORCE_BACK) VALUES(");
        sql.append(":VEPARCHY_CODE,:VPROVINCE_CODE,:VTRADE_ID,:VTRADE_TYPE_CODE,");
        sql.append(":VBATCH_ID,:VPRIORITY,:VCHARGE_ID,:VACCT_ID,:VUSER_ID,:VSERIAL_NUMBER,");
        sql.append(":VWRITEOFF_MODE,:VCHANNEL_ID,:VPAYMENT_ID,:VPAYMENT_OP,:VPAY_FEE_MODE_CODE,");
        sql.append(":VRECV_FEE,:VOUTER_TRADE_ID,:VBILL_START_CYCLE_ID,");
        sql.append(":VBILL_END_CYCLE_ID,");
        sql.append("STR_TO_DATE(:VSTART_DATE,'%Y-%m-%d %T'),");
        sql.append("STR_TO_DATE(:VEND_DATE,'%Y-%m-%d %T'),");
        sql.append(":VMONTHS,:VLIMIT_MONEY,:VPAYMENT_REASON_CODE,");
        sql.append(":VEXTEND_TAG,:VACTION_CODE,:VACTION_EVENT_ID,");
        sql.append(":VACCT_BALANCE_ID,:VDEPOSIT_CODE,:VPRIVATE_TAG,:VREMARK,");
        sql.append(":VINPUT_NO,:VINPUT_MODE,:VACCT_ID2,:VUSER_ID2,");
        sql.append(":VDEPOSIT_CODE2,:VREL_CHARGE_ID,");
        sql.append("STR_TO_DATE(:VTRADE_TIME,'%Y-%m-%d %T'),");
        sql.append(":VTRADE_EPARCHY_CODE,:VTRADE_CITY_CODE,:VTRADE_DEPART_ID,:VTRADE_STAFF_ID,");
        sql.append(":VCANCEL_TAG,:VDEAL_TAG,STR_TO_DATE(:VDEAL_TIME,'%Y-%m-%d %T'),");
        sql.append(":VRESULT_CODE,:VRESULT_INFO,:VRSRV_FEE1,");
        sql.append(":VRSRV_FEE2,:VRSRV_INFO1,:VLIMIT_MODE,:VFORCE_BACK)");
        Map<String, String> param = new HashMap<>();
        param.put("VTRADE_ID", payLogDmn.getTradeId());
        param.put("VTRADE_TYPE_CODE", String.valueOf(payLogDmn.getTradeTypeCode()));
        param.put("VBATCH_ID", payLogDmn.getBatchId());
        param.put("VPRIORITY", String.valueOf(payLogDmn.getPriority()));
        param.put("VCHARGE_ID", payLogDmn.getChargeId());
        param.put("VACCT_ID", payLogDmn.getAcctId());
        param.put("VUSER_ID", payLogDmn.getUserId());
        param.put("VSERIAL_NUMBER", payLogDmn.getSerialNumber());
        param.put("VEPARCHY_CODE", payLogDmn.getEparchyCode());
        param.put("VPROVINCE_CODE", payLogDmn.getProvinceCode());
        param.put("VWRITEOFF_MODE", String.valueOf(payLogDmn.getWriteoffMode()));
        param.put("VLIMIT_MODE", String.valueOf(payLogDmn.getLimitMode()));
        param.put("VCHANNEL_ID", String.valueOf(payLogDmn.getChannelId()));
        param.put("VPAYMENT_ID", String.valueOf(payLogDmn.getPaymentId()));
        param.put("VPAYMENT_OP", String.valueOf(payLogDmn.getPaymentOp()));
        param.put("VPAY_FEE_MODE_CODE", String.valueOf(payLogDmn.getPayFeeModeCode()));
        param.put("VRECV_FEE", String.valueOf(payLogDmn.getRecvFee()));
        param.put("VOUTER_TRADE_ID", payLogDmn.getOuterTradeId());
        param.put("VBILL_START_CYCLE_ID", String.valueOf(payLogDmn.getBillStartCycleId()));
        param.put("VBILL_END_CYCLE_ID", String.valueOf(payLogDmn.getBillEndCycleId()));
        param.put("VSTART_DATE", payLogDmn.getStartDate());
        param.put("VEND_DATE", payLogDmn.getEndDate());
        param.put("VMONTHS", String.valueOf(payLogDmn.getMonths()));
        param.put("VLIMIT_MONEY", String.valueOf(payLogDmn.getLimitMoney()));
        param.put("VPAYMENT_REASON_CODE", String.valueOf(payLogDmn.getPaymentReasonCode()));
        param.put("VEXTEND_TAG", String.valueOf(payLogDmn.getExtendTag()));
        param.put("VACTION_CODE", String.valueOf(payLogDmn.getActionCode()));
        param.put("VACTION_EVENT_ID", payLogDmn.getActionEventId());
        param.put("VACCT_BALANCE_ID", payLogDmn.getAcctBalanceId());
        param.put("VDEPOSIT_CODE", String.valueOf(payLogDmn.getDepositCode()));
        param.put("VPRIVATE_TAG", String.valueOf(payLogDmn.getPrivateTag()));
        param.put("VREMARK", payLogDmn.getRemark());
        param.put("VINPUT_NO", payLogDmn.getInputNo());
        param.put("VINPUT_MODE", String.valueOf(payLogDmn.getInputMode()));
        param.put("VACCT_ID2", payLogDmn.getAcctId2());
        param.put("VUSER_ID2", payLogDmn.getUserId2());
        param.put("VDEPOSIT_CODE2", String.valueOf(payLogDmn.getDepositCode2()));
        param.put("VREL_CHARGE_ID", payLogDmn.getRelChargeId());
        param.put("VTRADE_TIME", payLogDmn.getTradeTime());
        param.put("VTRADE_EPARCHY_CODE", payLogDmn.getTradeEparchyCode());
        param.put("VTRADE_CITY_CODE", payLogDmn.getTradeCityCode());
        param.put("VTRADE_DEPART_ID", payLogDmn.getTradeDepartId());
        param.put("VTRADE_STAFF_ID", payLogDmn.getTradeStaffId());
        param.put("VCANCEL_TAG", String.valueOf(payLogDmn.getCancelTag()));
        param.put("VDEAL_TAG", String.valueOf(payLogDmn.getDealTag()));
        param.put("VDEAL_TIME", payLogDmn.getDealTime());
        param.put("VRESULT_CODE", String.valueOf(payLogDmn.getResultCode()));
        param.put("VRESULT_INFO", payLogDmn.getResultInfo());
        param.put("VRSRV_FEE1", String.valueOf(payLogDmn.getRsrvFee1()));
        param.put("VRSRV_FEE2", String.valueOf(payLogDmn.getRsrvFee2()));
        param.put("VRSRV_INFO1", payLogDmn.getRsrvInfo1());
        param.put("VFORCE_BACK", String.valueOf(payLogDmn.getForceBack()));
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }

    @Override
    public int insertTradeHyLog(TradeHyLog tradeHyLog, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TL_B_TRADELOG_HY (PROVINCE_CODE,EPARCHY_CODE,OPER_ID,");
        sql.append("CHANNEL_CODE,TRADE_ID,SERIAL_NUMBER,OUTER_TRADE_TIME,CHANNEL_ID,");
        sql.append("PAYMENT_ID,PAY_FEE_MODE_CODE,TRADE_FEE,CHARGE_ID,NET_TYPE_CODE,CITY_CODE) ");
        sql.append(" VALUES (:VPROVINCE_CODE,:VEPARCHY_CODE,:VOPER_ID,:VCHANNEL_CODE,");
        sql.append(":VTRADE_ID,:VSERIAL_NUMBER,STR_TO_DATE(:VOUTER_TRADE_TIME, 'YYYYMMDDHH24MISS'),");
        sql.append(":VCHANNEL_ID,:VPAYMENT_ID,:VPAY_FEE_MODE_CODE,");
        sql.append(":VTRADE_FEE,:VCHARGE_ID,:VNET_TYPE_CODE,:VCITY_CODE)");
        Map param = new HashMap<>();
        param.put("VPROVINCE_CODE", tradeHyLog.getProvinceCode());
        param.put("VEPARCHY_CODE", tradeHyLog.getEparchyCode());
        param.put("VOPER_ID", tradeHyLog.getOperId());
        param.put("VCHANNEL_CODE", tradeHyLog.getChannelCode());
        param.put("VTRADE_ID", tradeHyLog.getTradeId());
        param.put("VSERIAL_NUMBER", tradeHyLog.getSerialNumber());
        param.put("VOUTER_TRADE_TIME", tradeHyLog.getOuterTradeTime());
        param.put("VCHANNEL_ID", tradeHyLog.getChannelId());
        param.put("VPAYMENT_ID", tradeHyLog.getPaymentId());
        param.put("VPAY_FEE_MODE_CODE", tradeHyLog.getPayFeeModeCode());
        param.put("VTRADE_FEE", tradeHyLog.getTradeFee());
        param.put("VCHARGE_ID", tradeHyLog.getChargerId());
        param.put("VNET_TYPE_CODE", tradeHyLog.getNetTypeCode());
        param.put("VCITY_CODE", tradeHyLog.getCityCode());
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }

    @Override
    public int insertPayFeeWork(AsynWork asynWork, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_ASYN_WORK (WORK_ID,WORK_TYPE_CODE,EPARCHY_CODE,");
        sql.append("NET_TYPE_CODE,CHARGE_ID,ACCT_ID,USER_ID,SERIAL_NUMBER,WRITEOFF_MODE,");
        sql.append("PAYMENT_ID,PAY_FEE_MODE_CODE,PAYMENT_OP,CHANNEL_ID,START_CYCLE_ID,");
        sql.append("END_CYCLE_ID,IF_PRINT,REMARK,TRADE_TIME,TRADE_EPARCHY_CODE,");
        sql.append("TRADE_CITY_CODE,TRADE_DEPART_ID,TRADE_STAFF_ID,DEAL_TAG,DEAL_TIME,");
        sql.append("RESULT_CODE,RESULT_INFO,RSRV_FEE1,RSRV_FEE2,RSRV_FEE3,RSRV_FEE4,");
        sql.append("RSRV_FEE5,RSRV_FEE6,RSRV_FEE7,RSRV_FEE8,RSRV_STR1,RSRV_STR2,RSRV_STR3) ");
        sql.append("VALUES (:VWORK_ID,:VWORK_TYPE_CODE,:VEPARCHY_CODE,:VNET_TYPE_CODE,");
        sql.append(":VCHARGE_ID,:VACCT_ID,:VUSER_ID,:VSERIAL_NUMBER,:VWRITEOFF_MODE,");
        sql.append(":VPAYMENT_ID,:VPAY_FEE_MODE_CODE,:VPAYMENT_OP,:VCHANNEL_ID,");
        sql.append(":VSTART_CYCLE_ID,:VEND_CYCLE_ID,'','',");
        sql.append("STR_TO_DATE(:VTRADE_TIME, '%Y-%m-%d %T'),");
        sql.append(":VTRADE_EPARCHY_CODE,:VTRADE_CITY_CODE,:VTRADE_DEPART_ID,");
        sql.append(":VTRADE_STAFF_ID,:VDEAL_TAG,'','','',:VRSRV_FEE1,'','','','','','',");
        sql.append(":VRSRV_FEE8,:VRSRV_STR1,:VRSRV_STR2,:VRSRV_STR3)");
        Map<String, String> param = new HashMap<>();
        param.put("VWORK_ID", asynWork.getWorkId());
        param.put("VCHARGE_ID", asynWork.getChargeId());
        param.put("VWORK_TYPE_CODE", asynWork.getWorkTypeCode());
        param.put("VEPARCHY_CODE", asynWork.getEparchyCode());
        param.put("VNET_TYPE_CODE", asynWork.getNetTypeCode());
        param.put("VSERIAL_NUMBER", asynWork.getSerialNumber());
        param.put("VWRITEOFF_MODE", asynWork.getWriteoffMode());
        param.put("VPAYMENT_ID", asynWork.getPaymentId());
        param.put("VPAY_FEE_MODE_CODE", asynWork.getPayFeeModeCode());
        param.put("VPAYMENT_OP", asynWork.getPaymentOp());
        param.put("VCHANNEL_ID", asynWork.getChannelId());
        param.put("VSTART_CYCLE_ID", String.valueOf(asynWork.getStartCycleId()));
        param.put("VEND_CYCLE_ID", String.valueOf(asynWork.getEndCycleId()));
        param.put("VTRADE_TIME", asynWork.getTradeTime());
        param.put("VTRADE_EPARCHY_CODE", asynWork.getTradeEparchyCode());
        param.put("VTRADE_CITY_CODE", asynWork.getTradeCityCode());
        param.put("VTRADE_DEPART_ID", asynWork.getTradeDepartId());
        param.put("VTRADE_STAFF_ID", asynWork.getTradeStaffId());
        param.put("VDEAL_TAG", asynWork.getDealTag());
        param.put("VRSRV_FEE1", asynWork.getRsrvFee1());
        param.put("VRSRV_FEE8", asynWork.getRsrvFee8());
        param.put("VRSRV_STR1", asynWork.getRsrvStr1());
        param.put("VRSRV_STR2", asynWork.getRsrvStr2());
        param.put("VRSRV_STR3", asynWork.getRsrvStr3());
        param.put("VACCT_ID", asynWork.getAcctId());
        param.put("VUSER_ID", asynWork.getUserId());
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }

}
