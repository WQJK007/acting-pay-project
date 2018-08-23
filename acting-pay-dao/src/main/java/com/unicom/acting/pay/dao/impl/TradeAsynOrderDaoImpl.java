package com.unicom.acting.pay.dao.impl;


import com.unicom.acting.pay.dao.TradeAsynOrderDao;
import com.unicom.acting.pay.domain.PayLogDmn;
import com.unicom.acting.pay.domain.AsynWork;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TradeAsynOrderDaoImpl extends JdbcBaseDao implements TradeAsynOrderDao {
    @Override
    public int insertPayLogDmn(PayLogDmn payLogDmn, String dbType, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_PAYLOG_DMN(EPARCHY_CODE,PROVINCE_CODE,TRADE_ID,");
        sql.append("TRADE_TYPE_CODE,BATCH_ID,PRIORITY,CHARGE_ID,ACCT_ID,USER_ID,");
        sql.append("SERIAL_NUMBER,WRITEOFF_MODE,CHANNEL_ID,PAYMENT_ID,PAYMENT_OP,");
        sql.append("PAY_FEE_MODE_CODE,RECV_FEE,OUTER_TRADE_ID,BILL_START_CYCLE_ID,");
        sql.append("BILL_END_CYCLE_ID,START_DATE,END_DATE,MONTHS,LIMIT_MONEY,");
        sql.append("PAYMENT_REASON_CODE,EXTEND_TAG,ACTION_CODE,ACTION_EVENT_ID,");
        sql.append("ACCT_BALANCE_ID,DEPOSIT_CODE,PRIVATE_TAG,REMARK,INPUT_NO,INPUT_MODE,");
        sql.append("ACCT_ID2,USER_ID2,DEPOSIT_CODE2,REL_CHARGE_ID,TRADE_TIME,");
        sql.append("TRADE_EPARCHY_CODE,TRADE_CITY_CODE,TRADE_DEPART_ID,TRADE_STAFF_ID,");
        sql.append("CANCEL_TAG,DEAL_TAG,DEAL_TIME,RESULT_CODE,RESULT_INFO,");
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
        return this.getJdbcTemplate(dbType, provinceCode).update(sql.toString(), param);
    }

    @Override
    public int insertAsynWork(AsynWork asynWork, String dbType, String provinceCode) {
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
        return this.getJdbcTemplate(dbType, provinceCode).update(sql.toString(), param);
    }
}
