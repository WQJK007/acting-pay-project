package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.PayLogDao;
import com.unicom.acting.pay.domain.*;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PayLogDaoImpl extends JdbcBaseDao implements PayLogDao {
    @Override
    public int insertPayLog(PayLog payLog) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_PAYLOG(CHARGE_ID,PROVINCE_CODE,EPARCHY_CODE,");
        sql.append("CITY_CODE,CUST_ID,USER_ID,SERIAL_NUMBER,NET_TYPE_CODE,ACCT_ID,CHANNEL_ID,");
        sql.append("PAYMENT_ID,PAY_FEE_MODE_CODE,PAYMENT_OP,RECV_FEE,LIMIT_MONEY,RECV_TIME,");
        sql.append("RECV_EPARCHY_CODE,RECV_CITY_CODE,RECV_DEPART_ID,RECV_STAFF_ID,");
        sql.append("PAYMENT_REASON_CODE,INPUT_NO,INPUT_MODE,OUTER_TRADE_ID,ACT_TAG,EXTEND_TAG,");
        sql.append("ACTION_CODE,ACTION_EVENT_ID,PAYMENT_RULE_ID,REMARK,CANCEL_TAG,");
        sql.append("CANCEL_STAFF_ID,CANCEL_DEPART_ID,CANCEL_CITY_CODE,CANCEL_EPARCHY_CODE,");
        sql.append("CANCEL_TIME,CANCEL_CHARGE_ID,RSRV_FEE1,RSRV_FEE2,RSRV_INFO1) ");
        sql.append("VALUES(:VCHARGE_ID,:VPROVINCE_CODE,:VEPARCHY_CODE,");
        sql.append(":VCITY_CODE,:VCUST_ID,:VUSER_ID,:VSERIAL_NUMBER,");
        sql.append(":VNET_TYPE_CODE,:VACCT_ID,:VCHANNEL_ID,:VPAYMENT_ID,");
        sql.append(":VPAY_FEE_MODE_CODE,:VPAYMENT_OP,:VRECV_FEE,:VLIMIT_MONEY,");
        sql.append("STR_TO_DATE(:VRECV_TIME,'%Y-%m-%d %T'),:VRECV_EPARCHY_CODE,");
        sql.append(":VRECV_CITY_CODE,:VRECV_DEPART_ID,:VRECV_STAFF_ID,:VPAYMENT_REASON_CODE,");
        sql.append(":VINPUT_NO,:VINPUT_MODE,:VOUTER_TRADE_ID,:VACT_TAG,:VEXTEND_TAG,");
        sql.append(":VACTION_CODE,:VACTION_EVENT_ID,:VPAYMENT_RULE_ID,:VREMARK,");
        sql.append(":VCANCEL_TAG,:VCANCEL_STAFF_ID,:VCANCEL_DEPART_ID,:VCANCEL_CITY_CODE,");
        sql.append(":VCANCEL_EPARCHY_CODE,STR_TO_DATE(:VCANCEL_TIME,'%Y-%m-%d %T'),");
        sql.append(":VCANCEL_CHARGE_ID,:VRSRV_FEE1,:VRSRV_FEE2,:VRSRV_INFO1)");
        Map<String, String> param = new HashMap();
        param.put("VCHARGE_ID", payLog.getChargeId());
        param.put("VPROVINCE_CODE", payLog.getProvinceCode());
        param.put("VEPARCHY_CODE", payLog.getEparchyCode());
        param.put("VCITY_CODE", payLog.getCityCode());
        param.put("VCUST_ID", payLog.getCustId());
        param.put("VUSER_ID", payLog.getUserId());
        param.put("VSERIAL_NUMBER", payLog.getSerialNumber());
        param.put("VNET_TYPE_CODE", payLog.getNetTypeCode());
        param.put("VACCT_ID", payLog.getAcctId());
        param.put("VCHANNEL_ID", payLog.getChannelId());
        param.put("VPAYMENT_ID", String.valueOf(payLog.getPaymentId()));
        param.put("VPAY_FEE_MODE_CODE", String.valueOf(payLog.getPayFeeModeCode()));
        param.put("VPAYMENT_OP", String.valueOf(payLog.getPaymentOp()));
        param.put("VRECV_FEE", String.valueOf(payLog.getRecvFee()));
        param.put("VLIMIT_MONEY", String.valueOf(payLog.getLimitMoney()));
        param.put("VRECV_TIME", payLog.getRecvTime());
        param.put("VRECV_EPARCHY_CODE", payLog.getRecvEparchyCode());
        param.put("VRECV_CITY_CODE", payLog.getRecvCityCode());
        param.put("VRECV_DEPART_ID", payLog.getRecvDepartId());
        param.put("VRECV_STAFF_ID", payLog.getRecvStaffId());
        param.put("VPAYMENT_REASON_CODE", String.valueOf(payLog.getPaymentReasonCode()));
        param.put("VINPUT_NO", payLog.getInputNo());
        param.put("VINPUT_MODE", String.valueOf(payLog.getInputMode()));
        param.put("VOUTER_TRADE_ID", payLog.getOuterTradeId());
        param.put("VACT_TAG", String.valueOf(payLog.getActTag()));
        param.put("VEXTEND_TAG", String.valueOf(payLog.getExtendTag()));
        param.put("VACTION_CODE", String.valueOf(payLog.getActionCode()));
        param.put("VACTION_EVENT_ID", payLog.getActionEventId());
        param.put("VPAYMENT_RULE_ID", String.valueOf(payLog.getPaymentRuleId()));
        param.put("VREMARK", payLog.getRemark());
        param.put("VCANCEL_TAG", String.valueOf(payLog.getCancelTag()));
        param.put("VCANCEL_STAFF_ID", payLog.getCancelStaffId());
        param.put("VCANCEL_DEPART_ID", payLog.getCancelDepartId());
        param.put("VCANCEL_CITY_CODE", payLog.getCancelCityCode());
        param.put("VCANCEL_EPARCHY_CODE", payLog.getCancelEparchyCode());
        param.put("VCANCEL_TIME", payLog.getCancelTime());
        param.put("VCANCEL_CHARGE_ID", payLog.getCancelChargeId());
        param.put("VRSRV_FEE1", String.valueOf(payLog.getRsrvFee1()));
        param.put("VRSRV_FEE2", String.valueOf(payLog.getRsrvFee2()));
        param.put("VRSRV_INFO1", payLog.getRsrvInfo1());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    @Override
    public void insertCLPayLog(List<CLPayLog> clPayLogs) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_CLPAYLOG (CHARGE_ID,CL_PAYLOG_ID,PROVINCE_CODE,EPARCHY_CODE,");
        sql.append("NET_TYPE_CODE,AREA_CODE,ACCT_ID,USER_ID,OLD_ACCT_ID,OLD_USER_ID,");
        sql.append("SERIAL_NUMBER,RECV_FEE,CANCEL_TAG,DOWN_TAG,OUTER_TRADE_ID,OPERATE_TIME,");
        sql.append("RECV_TIME,RECV_EPARCHY_CODE,RECV_CITY_CODE,RECV_DEPART_ID,RECV_STAFF_ID,");
        sql.append("CANCEL_STAFF_ID,CANCEL_DEPART_ID,CANCEL_CITY_CODE,CANCEL_EPARCHY_CODE,");
        sql.append("CANCEL_TIME,CANCEL_CHARGE_ID,DEAL_TYPE,PAYMENT_ID,RSRV_INFO1,RSRV_INFO2,");
        sql.append("RSRV_INFO3) VALUES (:VCHARGE_ID,:VCL_PAYLOG_ID,:VPROV_CODE,:VEPARCHY_CODE,");
        sql.append(":VNET_TYPE_CODE,:VAREA_CODE,:VACCT_ID,:VUSER_ID,:VOLD_ACCT_ID,:VOLD_USER_ID,");
        sql.append(":VSERIAL_NUMBER,:VRECV_FEE,:VCANCEL_TAG,:VDOWN_TAG,:VOUTER_TRADE_ID,");
        sql.append("SYSDATE(),SYSDATE(),:VRECV_EPARCHY_CODE,:VRECV_CITY_CODE,:VRECV_DEPART_ID,");
        sql.append(":VRECV_STAFF_ID,'','','','',NULL,NULL,'0',:VPAYMENT_ID,'','','') ");
        List params = new ArrayList(clPayLogs.size());
        for (CLPayLog pCLPayLog : clPayLogs) {
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
        this.getJdbcTemplate(DbTypes.ACTING_DRDS).batchUpdate(sql.toString(),
                (Map<String, String>[]) params.toArray(new Map[params.size()]));


    }

    @Override
    public int insertPayOtherLog(PayOtherLog payOtherLog) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_PAYOTHER_LOG(CHARGE_ID,CARRIER_ID,");
        sql.append("CARRIER_CODE,OPERATE_TIME,PROVINCE_CODE,EPARCHY_CODE,CARRIER_VEST,");
        sql.append("CARRIER_USE_NAME,CARRIER_USE_PHONE,CARRIER_USE_PASSID,CARRIER_STATUS,");
        sql.append("CONF_TIME_LIMIT,BANK_ACCT_NO,BANK_CODE,BANK_NAME,CANCEL_TAG,RSRV_INFO2,");
        sql.append("RSRV_INFO1,RSRV_FEE2,RSRV_FEE1,ACCT_ID,USER_ID) VALUES (:VCHARGE_ID,:VCARRIER_ID,");
        sql.append(":VCARRIER_CODE,STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),");
        sql.append(":VPROVINCE_CODE,:VEPARCHY_CODE,:VCARRIER_VEST,:VCARRIER_USE_NAME,");
        sql.append(":VCARRIER_USE_PHONE,:VCARRIER_USE_PASSID,:VCARRIER_STATUS,");
        sql.append(":VCONF_TIME_LIMIT,:VBANK_ACCT_NO,:VBANK_CODE,:VBANK_NAME,");
        sql.append(":VCANCEL_TAG,:VRSRV_INFO2,:VRSRV_INFO1,:VRSRV_FEE2,:VRSRV_FEE1,:VACCT_ID,:VUSER_ID)");
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
        param.put("VACCT_ID", payOtherLog.getAcctId());
        param.put("VUSER_ID", payOtherLog.getUserId());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    @Override
    public int insertChargerelation(ChargeRelation chargeRelation) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_CHARGERELATION(ID,OPERATE_ID1,OPERATE_ID2,OPERATE_TYPE,");
        sql.append("DEBUTY_CODE,OPERATE_STAFF_ID,OPERATE_DEPART_ID,OPERATE_CITY_CODE,");
        sql.append("OPERATE_EPARCHY_CODE,OPERATE_TIME,ACCT_ID,EPARCHY_CODE,PROVINCE_CODE) ");
        sql.append("VALUES(:VID,:VOPERATE_ID1,:VOPERATE_ID2,:VOPERATE_TYPE,:VDEBUTY_CODE,");
        sql.append(":VOPERATE_STAFF_ID,:VOPERATE_DEPART_ID,:VOPERATE_CITY_CODE,");
        sql.append(":VOPERATE_EPARCHY_CODE,STR_STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T')");
        sql.append(":VACCT_ID,:VEPARCHY_CODE,:VPROVINCE_CODE)");
        Map<String, String> param = new HashMap();
        param.put("VID", chargeRelation.getId());
        param.put("VOPERATE_ID1", chargeRelation.getOperateId1());
        param.put("VOPERATE_ID2", chargeRelation.getOperateId2());
        param.put("VOPERATE_TYPE", chargeRelation.getOperateType());
        param.put("VDEBUTY_CODE", chargeRelation.getDebutyCode());
        param.put("VOPERATE_STAFF_ID", chargeRelation.getOperateStaffId());
        param.put("VOPERATE_DEPART_ID", chargeRelation.getOperateDepartId());
        param.put("VOPERATE_CITY_CODE", chargeRelation.getOperateCityCode());
        param.put("VOPERATE_EPARCHY_CODE", chargeRelation.getOperateEparchyCode());
        param.put("VOPERATE_TIME", chargeRelation.getOperateTime());
        param.put("VACCT_ID", chargeRelation.getAcctId());
        param.put("VEPARCHY_CODE", chargeRelation.getEparchyCode());
        param.put("VPROVINCE_CODE", chargeRelation.getProvinceCode());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    @Override
    public boolean ifExistOuterTradeId(String tradeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TF_B_PAYLOG WHERE OUTER_TRADE_ID=:VOUTER_TRADE_ID ");
        Map<String, String> param = new HashMap<>();
        param.put("VOUTER_TRADE_ID", tradeId);
        List<String> result = this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForList(sql.toString(), param, String.class);
        if (!CollectionUtils.isEmpty(result)) {
            return true;
        }
        return false;
    }



    @Override
    public List<PayLog> getPaylogByOuterTradeId(String outerTradeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CHARGE_ID, EPARCHY_CODE, CITY_CODE, CUST_ID, USER_ID, SERIAL_NUMBER,");
        sql.append(" NET_TYPE_CODE, ACCT_ID, CHANNEL_ID, PAYMENT_ID, PAY_FEE_MODE_CODE, PAYMENT_OP,");
        sql.append(" RECV_FEE, LIMIT_MONEY, DATE_FORMAT(RECV_TIME,'%Y-%m-%d %T') RECV_TIME, RECV_EPARCHY_CODE,");
        sql.append(" RECV_CITY_CODE, RECV_DEPART_ID,RECV_STAFF_ID, PAYMENT_REASON_CODE, INPUT_NO, INPUT_MODE,");
        sql.append(" OUTER_TRADE_ID, ACT_TAG,EXTEND_TAG, ACTION_CODE, ACTION_EVENT_ID, PAYMENT_RULE_ID, REMARK,");
        sql.append(" CANCEL_TAG, CANCEL_STAFF_ID, CANCEL_DEPART_ID, CANCEL_CITY_CODE, CANCEL_EPARCHY_CODE,");
        sql.append(" DATE_FORMAT(CANCEL_TIME,'%Y-%m-%d %T') CANCEL_TIME, CANCEL_CHARGE_ID, DEV_CODE, DEV_NAME,");
        sql.append(" NP_TAG, AGENT_TAG, CONTRACT_TAG, RSRV_FEE1, RSRV_FEE2, RSRV_INFO1, RSRV_INFO2,");
        sql.append(" PROVINCE_CODE, STANDARD_KIND_CODE FROM TF_B_PAYLOG ");
        sql.append(" WHERE OUTER_TRADE_ID=:VOUTER_TRADE_ID AND  ACT_TAG <> '9'");
        Map<String, String> param = new HashMap<>();
        param.put("VOUTER_TRADE_ID", outerTradeId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new PaylogRowMapper());
    }


    @Override
    public List<PayLog> getPaylogByChargeIdAndAcctId(String chargeId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CHARGE_ID, EPARCHY_CODE, CITY_CODE, CUST_ID, USER_ID, SERIAL_NUMBER,");
        sql.append(" NET_TYPE_CODE, ACCT_ID, CHANNEL_ID, PAYMENT_ID, PAY_FEE_MODE_CODE, PAYMENT_OP,");
        sql.append(" RECV_FEE, LIMIT_MONEY, DATE_FORMAT(RECV_TIME,'%Y-%m-%d %T') RECV_TIME, RECV_EPARCHY_CODE,");
        sql.append(" RECV_CITY_CODE, RECV_DEPART_ID,RECV_STAFF_ID, PAYMENT_REASON_CODE, INPUT_NO, INPUT_MODE,");
        sql.append(" OUTER_TRADE_ID, ACT_TAG,EXTEND_TAG, ACTION_CODE, ACTION_EVENT_ID, PAYMENT_RULE_ID, REMARK,");
        sql.append(" CANCEL_TAG, CANCEL_STAFF_ID, CANCEL_DEPART_ID, CANCEL_CITY_CODE, CANCEL_EPARCHY_CODE,");
        sql.append(" DATE_FORMAT(CANCEL_TIME,'%Y-%m-%d %T') CANCEL_TIME, CANCEL_CHARGE_ID, DEV_CODE, DEV_NAME,");
        sql.append(" NP_TAG, AGENT_TAG, CONTRACT_TAG, RSRV_FEE1, RSRV_FEE2, RSRV_INFO1, RSRV_INFO2,");
        sql.append(" PROVINCE_CODE, STANDARD_KIND_CODE FROM TF_B_PAYLOG ");
        sql.append(" WHERE CHARGE_ID=:VCHARGE_ID AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>();
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new PaylogRowMapper());
    }



    @Override
    public List<PayLog> getPaylogByOuterTradeIdAndAcctId(String outerTradeId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CHARGE_ID, EPARCHY_CODE, CITY_CODE, CUST_ID, USER_ID, SERIAL_NUMBER,");
        sql.append(" NET_TYPE_CODE, ACCT_ID, CHANNEL_ID, PAYMENT_ID, PAY_FEE_MODE_CODE, PAYMENT_OP,");
        sql.append(" RECV_FEE, LIMIT_MONEY, DATE_FORMAT(RECV_TIME,'%Y-%m-%d %T') RECV_TIME, RECV_EPARCHY_CODE,");
        sql.append(" RECV_CITY_CODE, RECV_DEPART_ID,RECV_STAFF_ID, PAYMENT_REASON_CODE, INPUT_NO, INPUT_MODE,");
        sql.append(" OUTER_TRADE_ID, ACT_TAG,EXTEND_TAG, ACTION_CODE, ACTION_EVENT_ID, PAYMENT_RULE_ID, REMARK,");
        sql.append(" CANCEL_TAG, CANCEL_STAFF_ID, CANCEL_DEPART_ID, CANCEL_CITY_CODE, CANCEL_EPARCHY_CODE,");
        sql.append(" DATE_FORMAT(CANCEL_TIME,'%Y-%m-%d %T') CANCEL_TIME, CANCEL_CHARGE_ID, DEV_CODE, DEV_NAME,");
        sql.append(" NP_TAG, AGENT_TAG, CONTRACT_TAG, RSRV_FEE1, RSRV_FEE2, RSRV_INFO1, RSRV_INFO2,");
        sql.append(" PROVINCE_CODE, STANDARD_KIND_CODE FROM TF_B_PAYLOG ");
        sql.append(" WHERE OUTER_TRADE_ID=:VOUTER_TRADE_ID AND  ACCT_ID = :VACCT_ID AND CANCEL_TAG='0'");
        Map<String, String> param = new HashMap<>();
        param.put("VOUTER_TRADE_ID", outerTradeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new PaylogRowMapper());
    }



    @Override
    public List<PayLogDmn> getPaylogDmnByChargeIdAndAcctId(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TRADE_ID, TRADE_TYPE_CODE, BATCH_ID, PRIORITY, CHARGE_ID, ACCT_ID, USER_ID, SERIAL_NUMBER,");
        sql.append(" WRITEOFF_MODE, CHANNEL_ID, PAYMENT_ID, PAYMENT_OP, PAY_FEE_MODE_CODE, RECV_FEE, OUTER_TRADE_ID, ");
        sql.append(" BILL_START_CYCLE_ID, BILL_END_CYCLE_ID, DATE_FORMAT(START_DATE,'%Y-%m-%d %T') START_DATE, ");
        sql.append(" DATE_FORMAT(END_DATE,'%Y-%m-%d %T') END_DATE, MONTHS, LIMIT_MODE, LIMIT_MONEY, PAYMENT_REASON_CODE, ");
        sql.append(" EXTEND_TAG, ACTION_CODE, ACTION_EVENT_ID, ACCT_BALANCE_ID, DEPOSIT_CODE, PRIVATE_TAG, REMARK, ");
        sql.append(" INPUT_NO, INPUT_MODE, ACCT_ID2, USER_ID2, DEPOSIT_CODE2, REL_CHARGE_ID, ");
        sql.append(" DATE_FORMAT(TRADE_TIME,'%Y-%m-%d %T') TRADE_TIME, TRADE_EPARCHY_CODE, TRADE_CITY_CODE, ");
        sql.append(" TRADE_DEPART_ID, TRADE_STAFF_ID, CANCEL_TAG, DEV_CODE, DEV_NAME, NP_TAG, AGENT_TAG, ");
        sql.append(" CONTRACT_FLAG, DEAL_TAG, DATE_FORMAT(DEAL_TIME,'%Y-%m-%d %T') DEAL_TIME, RESULT_CODE, ");
        sql.append(" RESULT_INFO, RSRV_FEE1, RSRV_FEE2, RSRV_INFO1, EPARCHY_CODE, PROVINCE_CODE, PRINT_TAG, FORCE_BACK ");
        sql.append(" FROM TF_B_PAYLOG_DMN WHERE CHARGE_ID=:VCHARGE_ID AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>();
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new PaylogDmnRowMapper());
    }


    @Override
    public int insertPaylogDmn(PayLogDmn payLogDmn){
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
        sql.append(":VWRITEOFF_MODE,:VCHANNEL_ID,:VPAYMENT_ID,:VPAYMENT_OP,:VRECV_FEE,");
        sql.append(":VPAY_FEE_MODE_CODE,:VOUTER_TRADE_ID,:VBILL_START_CYCLE_ID,");
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
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }



    @Override
    public int updatePayOtherlogByChargeIdAndAcctId(String chargeId, String acctid){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_PAYOTHER_LOG SET CANCEL_TAG='1'");
        sql.append(" WHERE ACCT_ID=:VACCT_ID");
        sql.append(" AND CHARGE_ID=:VCHARGE_ID ");
        sql.append(" AND CANCEL_TAG='0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctid);
        param.put("VCHARGE_ID", chargeId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    @Override
    public int updatePayLogByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelTime, String acctid) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_PAYLOG  SET CANCEL_TAG='1', CANCEL_STAFF_ID= :VCANCEL_STAFF_ID,");
        sql.append(" CANCEL_DEPART_ID= :VCANCEL_DEPART_ID, CANCEL_CITY_CODE= :VCANCEL_CITY_CODE,");
        sql.append(" CANCEL_EPARCHY_CODE = :VCANCEL_EPARCHY_CODE,");
        sql.append(" CANCEL_TIME= STR_TO_DATE(:VCANCEL_TIME,'%Y-%m-%d %T')");
        sql.append(" WHERE CHARGE_ID = :VCHARGE_ID AND CANCEL_TAG = '0' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(7);
        param.put("VCANCEL_STAFF_ID", tradeTradeStaff.getStaffId());
        param.put("VCANCEL_DEPART_ID", tradeTradeStaff.getDepartId());
        param.put("VCANCEL_CITY_CODE", tradeTradeStaff.getCityCode());
        param.put("VCANCEL_EPARCHY_CODE", tradeTradeStaff.getEparchyCode());
        param.put("VCANCEL_TIME", cancelTime);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctid);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    @Override
    public int updatePayLogDByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelTime, String acctid) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_PAYLOG_D  SET CANCEL_TAG='1', CANCEL_STAFF_ID= :VCANCEL_STAFF_ID,");
        sql.append(" CANCEL_DEPART_ID= :VCANCEL_DEPART_ID, CANCEL_CITY_CODE= :VCANCEL_CITY_CODE,");
        sql.append(" CANCEL_EPARCHY_CODE = :VCANCEL_EPARCHY_CODE,");
        sql.append(" CANCEL_TIME= STR_TO_DATE(:VCANCEL_TIME,'%Y-%m-%d %T')");
        sql.append(" WHERE CHARGE_ID = :VCHARGE_ID AND CANCEL_TAG = '0' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(7);
        param.put("VCANCEL_STAFF_ID", tradeTradeStaff.getStaffId());
        param.put("VCANCEL_DEPART_ID", tradeTradeStaff.getDepartId());
        param.put("VCANCEL_CITY_CODE", tradeTradeStaff.getCityCode());
        param.put("VCANCEL_EPARCHY_CODE", tradeTradeStaff.getEparchyCode());
        param.put("VCANCEL_TIME", cancelTime);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctid);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    @Override
    public int insertClPaylog(CLPayLog clPaylog) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_CLPAYLOG (CHARGE_ID,CL_PAYLOG_ID,PROVINCE_CODE,EPARCHY_CODE,NET_TYPE_CODE,");
        sql.append(" AREA_CODE,ACCT_ID,USER_ID,OLD_ACCT_ID,OLD_USER_ID,SERIAL_NUMBER,RECV_FEE,CANCEL_TAG,DOWN_TAG,");
        sql.append(" OUTER_TRADE_ID,OPERATE_TIME,RECV_TIME,RECV_EPARCHY_CODE,RECV_CITY_CODE,RECV_DEPART_ID,");
        sql.append(" RECV_STAFF_ID,CANCEL_STAFF_ID,CANCEL_DEPART_ID,CANCEL_CITY_CODE,CANCEL_EPARCHY_CODE,");
        sql.append(" CANCEL_TIME,CANCEL_CHARGE_ID,CLCANCEL_PAY_ID,PAYMENT_ID,RSRV_INFO1,RSRV_INFO2,RSRV_INFO3)");
        sql.append(" VALUES(:VCHARGE_ID, :VCL_PAYLOG_ID, :VPROV_CODE, :VEPARCHY_CODE, :VNET_TYPE_CODE, :VAREA_CODE,");
        sql.append(" :VACCT_ID, :VUSER_ID, :VOLD_ACCT_ID, :VOLD_USER_ID, :VSERIAL_NUMBER, :VRECV_FEE, :VCANCEL_TAG, ");
        sql.append(" :VDOWN_TAG, :VOUTER_TRADE_ID, STR_TO_DATE(SYSDATE(),'%Y-%m-%d %T'), STR_TO_DATE(SYSDATE(),'%Y-%m-%d %T'), ");
        sql.append(" :VRECV_EPARCHY_CODE, :VRECV_CITY_CODE, :VRECV_DEPART_ID, :VRECV_STAFF_ID, :VCANCEL_STAFF_ID, ");
        sql.append(" :VCANCEL_DEPART_ID, :VCANCEL_CITY_CODE, :VCANCEL_EPARCHY_CODE, STR_TO_DATE(:VCANCEL_TIME,'%Y-%m-%d %T'), ");
        sql.append(" :VCANCEL_CHARGE_ID, :VCLCANCEL_PAY_ID, :VPAYMENT_ID, :VRSRV_INFO1, :VRSRV_INFO2, :VRSRV_INFO3)");
        Map<String, String> param = new HashMap<>(9);
        param.put("VCHARGE_ID",clPaylog.getChargeId());
        param.put("VCL_PAYLOG_ID",clPaylog.getClPaylogId());
        param.put("VPROV_CODE",clPaylog.getProvinceCode());
        param.put("VEPARCHY_CODE",clPaylog.getEparchyCode());
        param.put("VNET_TYPE_CODE",clPaylog.getNetTypeCode());
        param.put("VAREA_CODE",clPaylog.getAreaCode());
        param.put("VACCT_ID",clPaylog.getAcctId());
        param.put("VUSER_ID",clPaylog.getUserId());
        param.put("VOLD_ACCT_ID",clPaylog.getOldAcctId());
        param.put("VOLD_USER_ID",clPaylog.getOldUserId());
        param.put("VSERIAL_NUMBER",clPaylog.getSerialNumber());
        param.put("VRECV_FEE",String.valueOf(clPaylog.getRecvFee()));
        param.put("VCANCEL_TAG",String.valueOf(clPaylog.getCancelTag()));
        param.put("VDOWN_TAG",String.valueOf(clPaylog.getDownTag()));
        param.put("VOUTER_TRADE_ID",clPaylog.getOuterTradeId());
        param.put("VRECV_EPARCHY_CODE",clPaylog.getRecvEparchyCode());
        param.put("VRECV_CITY_CODE",clPaylog.getRecvCityCode());
        param.put("VRECV_DEPART_ID",clPaylog.getRecvDepartId());
        param.put("VRECV_STAFF_ID",clPaylog.getRecvStaffId());
        param.put("VCANCEL_STAFF_ID",clPaylog.getCancelStaffId());
        param.put("VCANCEL_DEPART_ID",clPaylog.getCancelDepartId());
        param.put("VCANCEL_CITY_CODE",clPaylog.getCancelCityCode());
        param.put("VCANCEL_EPARCHY_CODE",clPaylog.getCancelEparchyCode());
        param.put("VCANCEL_TIME",clPaylog.getCancelTime());
        param.put("VCANCEL_CHARGE_ID",clPaylog.getCancelChargeId());
        param.put("VCLCANCEL_PAY_ID",clPaylog.getClCancelPayId());
        param.put("VPAYMENT_ID",String.valueOf(clPaylog.getPaymentId()));
        param.put("VRSRV_INFO1",clPaylog.getRsrvInfo1());
        param.put("VRSRV_INFO2",clPaylog.getRsrvInfo2());
        param.put("VRSRV_INFO3",clPaylog.getRsrvInfo3());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    @Override
    public List<CLPayLog> getClPaylogByChargeIdAndAcctId(String chargeId, String acctid){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CHARGE_ID,CL_PAYLOG_ID,PROVINCE_CODE,EPARCHY_CODE,NET_TYPE_CODE,");
        sql.append(" AREA_CODE,ACCT_ID,USER_ID,OLD_ACCT_ID,OLD_USER_ID,SERIAL_NUMBER,RECV_FEE,CANCEL_TAG,DOWN_TAG,");
        sql.append(" OUTER_TRADE_ID,OPERATE_TIME,RECV_TIME,RECV_EPARCHY_CODE,RECV_CITY_CODE,RECV_DEPART_ID,");
        sql.append(" RECV_STAFF_ID,CANCEL_STAFF_ID,CANCEL_DEPART_ID,CANCEL_CITY_CODE,CANCEL_EPARCHY_CODE,");
        sql.append(" CANCEL_TIME,CANCEL_CHARGE_ID,CLCANCEL_PAY_ID,PAYMENT_ID,RSRV_INFO1,RSRV_INFO2,RSRV_INFO3");
        sql.append(" FROM TF_B_CLPAYLOG WHERE CHARGE_ID=:VCHARGE_ID AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(3);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctid);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new ClPaylogRowMapper());
    }


    @Override
    public int updateOrigClPaylog(CLPayLog clPayLog, String dealTag) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_CLPAYLOG A SET CANCEL_TAG='1',CANCEL_STAFF_ID=:VRECV_STAFF_ID,");
        sql.append(" CANCEL_DEPART_ID=:VRECV_DEPART_ID,CANCEL_CITY_CODE= :VRECV_CITY_CODE,CANCEL_EPARCHY_CODE = :VRECV_EPARCHY_CODE,");
        sql.append(" CANCEL_TIME=STR_TO_DATE(SYSDATE(),'%Y-%m-%d %T'),CANCEL_CHARGE_ID=:VCHARGE_ID,");
        sql.append(" CLCANCEL_PAY_ID=:VCL_PAYLOG_ID , DOWN_TAG='1' WHERE USER_ID=:VUSER_ID AND ");
        sql.append(" CHARGE_ID=:VCANCEL_CHARGE_ID AND CANCEL_TAG='0' AND DOWN_TAG=:VDOWN_TAG AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(10);
        param.put("VRECV_STAFF_ID", clPayLog.getRecvStaffId());
        param.put("VRECV_DEPART_ID", clPayLog.getRecvDepartId());
        param.put("VRECV_CITY_CODE", clPayLog.getRecvCityCode());
        param.put("VRECV_EPARCHY_CODE", clPayLog.getRecvEparchyCode());
        param.put("VCHARGE_ID", clPayLog.getChargeId());
        param.put("VCL_PAYLOG_ID", clPayLog.getClPaylogId());
        param.put("VCANCEL_CHARGE_ID", clPayLog.getCancelChargeId());
        param.put("VDOWN_TAG", dealTag);
        param.put("VACCT_ID", clPayLog.getAcctId());
        param.put("VUSER_ID", clPayLog.getUserId());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    @Override
    public int updateOrigClPaylogByChargeIdAndAcctId(String chargeId, String acctid) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_CLPAYLOG SET DOWN_TAG='0' WHERE CHARGE_ID=:VCHARGE_ID");
        sql.append("  AND CANCEL_TAG='2' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctid);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }



    @Override
    public int insertPaylogD(PayLog payLog){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_PAYLOG_D(CHARGE_ID,PROVINCE_CODE,EPARCHY_CODE,");
        sql.append("CITY_CODE,CUST_ID,USER_ID,SERIAL_NUMBER,NET_TYPE_CODE,ACCT_ID,CHANNEL_ID,");
        sql.append("PAYMENT_ID,PAY_FEE_MODE_CODE,PAYMENT_OP,RECV_FEE,LIMIT_MONEY,RECV_TIME,");
        sql.append("RECV_EPARCHY_CODE,RECV_CITY_CODE,RECV_DEPART_ID,RECV_STAFF_ID,");
        sql.append("PAYMENT_REASON_CODE,INPUT_NO,INPUT_MODE,OUTER_TRADE_ID,ACT_TAG,EXTEND_TAG,");
        sql.append("ACTION_CODE,ACTION_EVENT_ID,PAYMENT_RULE_ID,REMARK,CANCEL_TAG,CANCEL_STAFF_ID,");
        sql.append("CANCEL_DEPART_ID,CANCEL_CITY_CODE,CANCEL_EPARCHY_CODE,CANCEL_TIME,");
        sql.append("CANCEL_CHARGE_ID,RSRV_FEE1,RSRV_FEE2,RSRV_INFO1) VALUES( ");
        sql.append(":VCHARGE_ID,:VPROVINCE_CODE,:VEPARCHY_CODE,");
        sql.append(":VCITY_CODE,:VCUST_ID,:VUSER_ID,:VSERIAL_NUMBER,");
        sql.append(":VNET_TYPE_CODE,:VACCT_ID,:VCHANNEL_ID,:VPAYMENT_ID,");
        sql.append(":VPAY_FEE_MODE_CODE,:VPAYMENT_OP,:VRECV_FEE,:VLIMIT_MONEY,");
        sql.append("STR_TO_DATE(:VRECV_TIME,'%Y-%m-%d %T'),:VRECV_EPARCHY_CODE,");
        sql.append(":VRECV_CITY_CODE,:VRECV_DEPART_ID,:VRECV_STAFF_ID,:VPAYMENT_REASON_CODE,");
        sql.append(":VINPUT_NO,:VINPUT_MODE,:VOUTER_TRADE_ID,:VACT_TAG,:VEXTEND_TAG,");
        sql.append(":VACTION_CODE,:VACTION_EVENT_ID,:VPAYMENT_RULE_ID,:VREMARK,");
        sql.append(":VCANCEL_TAG,:VCANCEL_STAFF_ID,:VCANCEL_DEPART_ID,:VCANCEL_CITY_CODE,");
        sql.append(":VCANCEL_EPARCHY_CODE,STR_TO_DATE(:VCANCEL_TIME,'%Y-%m-%d %T'),");
        sql.append(":VCANCEL_CHARGE_ID,:VRSRV_FEE1,:VRSRV_FEE2,:VRSRV_INFO1)");
        Map<String, String> param = new HashMap<>();
        param.put("VCHARGE_ID", payLog.getChargeId());
        param.put("VPROVINCE_CODE", payLog.getProvinceCode());
        param.put("VEPARCHY_CODE", payLog.getEparchyCode());
        param.put("VCITY_CODE", payLog.getCityCode());
        param.put("VCUST_ID", payLog.getCustId());
        param.put("VUSER_ID", payLog.getUserId());
        param.put("VSERIAL_NUMBER", payLog.getSerialNumber());
        param.put("VNET_TYPE_CODE", payLog.getNetTypeCode());
        param.put("VACCT_ID", payLog.getAcctId());
        param.put("VCHANNEL_ID", payLog.getChannelId());
        param.put("VPAYMENT_ID", String.valueOf(payLog.getPaymentId()));
        param.put("VPAY_FEE_MODE_CODE", String.valueOf(payLog.getPayFeeModeCode()));
        param.put("VPAYMENT_OP", String.valueOf(payLog.getPaymentOp()));
        param.put("VRECV_FEE", String.valueOf(payLog.getRecvFee()));
        param.put("VLIMIT_MONEY", String.valueOf(payLog.getLimitMoney()));
        param.put("VRECV_TIME", payLog.getRecvTime());
        param.put("VRECV_EPARCHY_CODE", payLog.getRecvEparchyCode());
        param.put("VRECV_CITY_CODE", payLog.getRecvCityCode());
        param.put("VRECV_DEPART_ID", payLog.getRecvDepartId());
        param.put("VRECV_STAFF_ID", payLog.getRecvStaffId());
        param.put("VPAYMENT_REASON_CODE", String.valueOf(payLog.getPaymentReasonCode()));
        param.put("VINPUT_NO", payLog.getInputNo());
        param.put("VINPUT_MODE", String.valueOf(payLog.getInputMode()));
        param.put("VOUTER_TRADE_ID", payLog.getOuterTradeId());
        param.put("VACT_TAG", String.valueOf(payLog.getActTag()));
        param.put("VEXTEND_TAG", String.valueOf(payLog.getExtendTag()));
        param.put("VACTION_CODE", String.valueOf(payLog.getActionCode()));
        param.put("VACTION_EVENT_ID", payLog.getActionEventId());
        param.put("VPAYMENT_RULE_ID", String.valueOf(payLog.getPaymentRuleId()));
        param.put("VREMARK", payLog.getRemark());
        param.put("VCANCEL_TAG", String.valueOf(payLog.getCancelTag()));
        param.put("VCANCEL_STAFF_ID", payLog.getCancelStaffId());
        param.put("VCANCEL_DEPART_ID", payLog.getCancelDepartId());
        param.put("VCANCEL_CITY_CODE", payLog.getCancelCityCode());
        param.put("VCANCEL_EPARCHY_CODE", payLog.getCancelEparchyCode());
        param.put("VCANCEL_TIME", payLog.getCancelTime());
        param.put("VCANCEL_CHARGE_ID", payLog.getCancelChargeId());
        param.put("VRSRV_FEE1", String.valueOf(payLog.getRsrvFee1()));
        param.put("VRSRV_FEE2", String.valueOf(payLog.getRsrvFee2()));
        param.put("VRSRV_INFO1", payLog.getRsrvInfo1());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    @Override
    public boolean ifExistPaylogByChargeId(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TF_B_PAYLOG WHERE CHARGE_ID=:VCHARGE_ID AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        List<String> result = this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForList(sql.toString(), param, String.class);
        if (!CollectionUtils.isEmpty(result)) {
            return true;
        }
        return false;
    }



    class PaylogDmnRowMapper implements RowMapper<PayLogDmn> {
        @Override
        public PayLogDmn mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            PayLogDmn payLogDmn = new PayLogDmn();
            payLogDmn.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            payLogDmn.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            payLogDmn.setTradeId(resultSet.getString("TRADE_ID"));
            payLogDmn.setTradeTypeCode(resultSet.getInt("TRADE_TYPE_CODE"));
            payLogDmn.setBatchId(resultSet.getString("BATCH_ID"));
            payLogDmn.setPriority(resultSet.getInt("PRIORITY"));
            payLogDmn.setChargeId(resultSet.getString("CHARGE_ID"));
            payLogDmn.setAcctId(resultSet.getString("ACCT_ID"));
            payLogDmn.setUserId(resultSet.getString("USER_ID"));
            payLogDmn.setSerialNumber(resultSet.getString("SERIAL_NUMBER"));
            payLogDmn.setWriteoffMode(StringUtil.firstOfString(resultSet.getString("WRITEOFF_MODE")));
            payLogDmn.setLimitMode(StringUtil.firstOfString(resultSet.getString("LIMIT_MODE")));
            payLogDmn.setChannelId(resultSet.getString("CHANNEL_ID"));
            payLogDmn.setPaymentId(resultSet.getInt("PAYMENT_ID"));
            payLogDmn.setPaymentOp(resultSet.getInt("PAYMENT_OP"));
            payLogDmn.setPayFeeModeCode(resultSet.getInt("PAY_FEE_MODE_CODE"));
            payLogDmn.setRecvFee(resultSet.getInt("RECV_FEE"));
            payLogDmn.setOuterTradeId(resultSet.getString("OUTER_TRADE_ID"));
            payLogDmn.setBillStartCycleId(resultSet.getInt("BILL_START_CYCLE_ID"));
            payLogDmn.setBillEndCycleId(resultSet.getInt("BILL_END_CYCLE_ID"));
            payLogDmn.setStartDate(resultSet.getString("START_DATE"));
            payLogDmn.setEndDate(resultSet.getString("END_DATE"));
            payLogDmn.setMonths(resultSet.getInt("MONTHS"));
            payLogDmn.setLimitMoney(resultSet.getInt("LIMIT_MONEY"));
            payLogDmn.setPaymentReasonCode(resultSet.getInt("PAYMENT_REASON_CODE"));
            payLogDmn.setExtendTag(StringUtil.firstOfString(resultSet.getString("EXTEND_TAG")));
            payLogDmn.setActionCode(resultSet.getInt("ACTION_CODE"));
            payLogDmn.setActionEventId(resultSet.getString("ACTION_EVENT_ID"));
            payLogDmn.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            payLogDmn.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            payLogDmn.setPrivateTag(StringUtil.firstOfString(resultSet.getString("PRIVATE_TAG")));
            payLogDmn.setRemark(resultSet.getString("REMARK"));
            payLogDmn.setInputNo(resultSet.getString("INPUT_NO"));
            payLogDmn.setInputMode(resultSet.getInt("INPUT_MODE"));
            payLogDmn.setAcctId2(resultSet.getString("ACCT_ID2"));
            payLogDmn.setUserId2(resultSet.getString("USER_ID2"));
            payLogDmn.setDepositCode2(resultSet.getInt("DEPOSIT_CODE2"));
            payLogDmn.setRelChargeId(resultSet.getString("REL_CHARGE_ID"));
            payLogDmn.setTradeTime(resultSet.getString("TRADE_TIME"));
            payLogDmn.setTradeEparchyCode(resultSet.getString("TRADE_EPARCHY_CODE"));
            payLogDmn.setTradeCityCode(resultSet.getString("TRADE_CITY_CODE"));
            payLogDmn.setTradeDepartId(resultSet.getString("TRADE_DEPART_ID"));
            payLogDmn.setTradeStaffId(resultSet.getString("TRADE_STAFF_ID"));
            payLogDmn.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            payLogDmn.setDealTag(StringUtil.firstOfString(resultSet.getString("DEAL_TAG")));
            payLogDmn.setDealTime(resultSet.getString("DEAL_TIME"));
            payLogDmn.setResultCode(resultSet.getInt("RESULT_CODE"));
            payLogDmn.setResultInfo(resultSet.getString("RESULT_INFO"));
            payLogDmn.setRsrvFee1(resultSet.getInt("RSRV_FEE1"));
            payLogDmn.setRsrvFee2(resultSet.getInt("RSRV_FEE2"));
            payLogDmn.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            payLogDmn.setPrintTag(StringUtil.firstOfString(resultSet.getString("PRINT_TAG")));
            return payLogDmn;
        }
    }

    class PaylogRowMapper implements RowMapper<PayLog> {
        @Override
        public PayLog mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            PayLog paylog = new PayLog();
            paylog.setChargeId(resultSet.getString("CHARGE_ID"));
            paylog.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            paylog.setCityCode(resultSet.getString("CITY_CODE"));
            paylog.setCustId(resultSet.getString("CUST_ID"));
            paylog.setUserId(resultSet.getString("USER_ID"));
            paylog.setSerialNumber(resultSet.getString("SERIAL_NUMBER"));
            paylog.setNetTypeCode(resultSet.getString("NET_TYPE_CODE"));
            paylog.setAcctId(resultSet.getString("ACCT_ID"));
            paylog.setChannelId(resultSet.getString("CHANNEL_ID"));
            paylog.setPaymentId(resultSet.getInt("PAYMENT_ID"));
            paylog.setPayFeeModeCode(resultSet.getInt("PAY_FEE_MODE_CODE"));
            paylog.setPaymentOp(resultSet.getInt("PAYMENT_OP"));
            paylog.setRecvFee(resultSet.getLong("RECV_FEE"));
            paylog.setLimitMoney(resultSet.getLong("LIMIT_MONEY"));
            paylog.setRecvTime(resultSet.getString("RECV_TIME"));
            paylog.setRecvEparchyCode(resultSet.getString("RECV_EPARCHY_CODE"));
            paylog.setRecvCityCode(resultSet.getString("RECV_CITY_CODE"));
            paylog.setRecvDepartId(resultSet.getString("RECV_DEPART_ID"));
            paylog.setRecvStaffId(resultSet.getString("RECV_STAFF_ID"));
            paylog.setPaymentReasonCode(resultSet.getInt("PAYMENT_REASON_CODE"));
            paylog.setInputNo(resultSet.getString("INPUT_NO"));
            paylog.setInputMode(resultSet.getInt("INPUT_MODE"));
            paylog.setOuterTradeId(resultSet.getString("OUTER_TRADE_ID"));
            paylog.setActTag(StringUtil.firstOfString(resultSet.getString("ACT_TAG")));
            paylog.setExtendTag(StringUtil.firstOfString(resultSet.getString("EXTEND_TAG")));
            paylog.setActionCode(StringUtil.firstOfString(resultSet.getString("ACTION_CODE")));
            paylog.setActionEventId(resultSet.getString("ACTION_EVENT_ID"));
            paylog.setPaymentRuleId(resultSet.getInt("PAYMENT_RULE_ID"));
            paylog.setRemark(resultSet.getString("REMARK"));
            paylog.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            paylog.setCancelStaffId(resultSet.getString("CANCEL_STAFF_ID"));
            paylog.setCancelDepartId(resultSet.getString("CANCEL_DEPART_ID"));
            paylog.setCancelCityCode(resultSet.getString("CANCEL_CITY_CODE"));
            paylog.setCancelEparchyCode(resultSet.getString("CANCEL_EPARCHY_CODE"));
            paylog.setCancelTime(resultSet.getString("CANCEL_TIME"));
            paylog.setCancelChargeId(resultSet.getString("CANCEL_CHARGE_ID"));
            paylog.setDevCode(resultSet.getString("DEV_CODE"));
            paylog.setDevName(resultSet.getString("DEV_NAME"));
            paylog.setNpTag(resultSet.getString("NP_TAG"));
            paylog.setAgentTag(StringUtil.firstOfString(resultSet.getString("AGENT_TAG")));
            paylog.setContractTag(StringUtil.firstOfString(resultSet.getString("CONTRACT_TAG")));
            paylog.setRsrvFee1(resultSet.getLong("RSRV_FEE1"));
            paylog.setRsrvFee2(resultSet.getLong("RSRV_FEE2"));
            paylog.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            //paylog.setRsrvInfo2(resultSet.getString("RSRV_INFO2"));
            paylog.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            //paylog.setStandardKindCode(resultSet.getString("STANDARD_KIND_CODE"));
            return paylog;
        }
    }



    class ClPaylogRowMapper implements RowMapper<CLPayLog> {
        @Override
        public CLPayLog mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            CLPayLog clPayLog = new CLPayLog();
            clPayLog.setUserId(resultSet.getString("USER_ID"));
            clPayLog.setClPaylogId(resultSet.getString("CL_PAYLOG_ID"));
            clPayLog.setOuterTradeId(resultSet.getString("OUTER_TRADE_ID"));
            clPayLog.setAcctId(resultSet.getString("ACCT_ID"));
            clPayLog.setChargeId(resultSet.getString("CHARGE_ID"));
            clPayLog.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            clPayLog.setDownTag(StringUtil.firstOfString(resultSet.getString("DOWN_TAG")));
            clPayLog.setCancelEparchyCode(resultSet.getString("CANCEL_EPARCHY_CODE"));
            clPayLog.setCancelCityCode(resultSet.getString("CANCEL_CITY_CODE"));
            clPayLog.setCancelDepartId(resultSet.getString("CANCEL_DEPART_ID"));
            clPayLog.setCancelStaffId(resultSet.getString("CANCEL_STAFF_ID"));
            return clPayLog;
        }
    }

}
