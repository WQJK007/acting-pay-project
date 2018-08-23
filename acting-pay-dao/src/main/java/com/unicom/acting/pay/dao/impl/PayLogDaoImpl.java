package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.PayLogDao;
import com.unicom.acting.pay.domain.CLPayLog;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.acting.pay.domain.PayOtherLog;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PayLogDaoImpl extends JdbcBaseDao implements PayLogDao {
    @Override
    public int insertPayLog(PayLog payLog, String provinceCode) {
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
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }

    @Override
    public void insertCLPayLog(List<CLPayLog> clPayLogs, String provinceCode) {
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
        sql.append("SYSDATE(),SYSDATE(),:VRECV_EPARCHY_CODE,:VRECV_CITY_CODE,:VRECV_DEPART_ID,");
        sql.append(":VRECV_STAFF_ID,'','','','',NULL,'','0',:VPAYMENT_ID,'','','') ");
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
        this.getJdbcTemplate(provinceCode).batchUpdate(sql.toString(),
                (Map<String, String>[]) params.toArray(new Map[params.size()]));


    }

    @Override
    public int insertPayOtherLog(PayOtherLog payOtherLog, String provinceCode) {
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
}
