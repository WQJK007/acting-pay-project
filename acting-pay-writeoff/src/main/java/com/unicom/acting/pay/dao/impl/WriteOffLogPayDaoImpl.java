package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.fee.domain.AccessLog;
import com.unicom.acting.fee.domain.PayLog;
import com.unicom.acting.fee.domain.WriteOffLog;
import com.unicom.acting.fee.domain.WriteSnapLog;
import com.unicom.acting.pay.dao.WriteOffLogPayDao;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WriteOffLogPayDaoImpl extends JdbcBaseDao implements WriteOffLogPayDao {
    @Override
    public long insertPayLog(PayLog payLog, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_PAYLOG(CHARGE_ID,PROVINCE_CODE,EPARCHY_CODE,");
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
    public void insertWriteOffLog(List<WriteOffLog> writeOffLogList, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_WRITEOFFLOG(WRITEOFF_ID,CHARGE_ID,ACCT_ID,");
        sql.append("USER_ID,CYCLE_ID,NET_TYPE_CODE,BILL_ID,INTEGRATE_ITEM_CODE,DEPOSIT_CODE,");
        sql.append("ACCT_BALANCE_ID,WRITEOFF_FEE,IMP_FEE,FEE,OLD_BALANCE,NEW_BALANCE,LATE_FEE,");
        sql.append("LATE_BALANCE,OLD_LATE_BALANCE,NEW_LATE_BALANCE,DERATE_LATE_FEE,");
        sql.append("LATECAL_DATE,OLD_PAYTAG,NEW_PAYTAG,CAN_PAYTAG,OPERATE_TIME,PROVINCE_CODE,");
        sql.append("EPARCHY_CODE,DRECV_TIMES,CANCEL_TAG,DEPOSIT_LIMIT_RULEID,");
        sql.append("DEPOSIT_PRIOR_RULEID,ITEM_PRIOR_RULEID) VALUES (:VWRITEOFF_ID,:VCHARGE_ID,");
        sql.append(":VACCT_ID,:VUSER_ID,:VCYCLE_ID,");
        sql.append(":VNET_TYPE_CODE,:VBILL_ID,:VINTEGRATE_ITEM_CODE,:VDEPOSIT_CODE,");
        sql.append(":VACCT_BALANCE_ID,:VWRITEOFF_FEE,:VIMP_FEE,");
        sql.append(":VFEE,:VOLD_BALANCE,:VNEW_BALANCE,");
        sql.append(":VLATE_FEE,:VLATE_BALANCE,");
        sql.append(":VOLD_LATE_BALANCE,:VNEW_LATE_BALANCE,");
        sql.append(":VDERATE_LATE_FEE,");
        sql.append("STR_TO_DATE(:VLATECAL_DATE,'%Y-%m-%d %T'),");
        sql.append(":VOLD_PAYTAG,:VNEW_PAYTAG,:VCAN_PAYTAG,");
        sql.append("STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),:VPROVINCE_CODE,");
        sql.append(":VEPARCHY_CODE,:VDRECV_TIMES,:VCANCEL_TAG,:VDEPOSIT_LIMIT_RULEID,");
        sql.append(":VDEPOSIT_PRIOR_RULEID,:VITEM_PRIOR_RULEID)");
        List params = new ArrayList(writeOffLogList.size());
        for(WriteOffLog writeOffLog : writeOffLogList) {
            Map<String, String> param = new HashMap<>();
            param.put("VWRITEOFF_ID", writeOffLog.getWriteoffId());
            param.put("VCHARGE_ID", writeOffLog.getChargeId());
            param.put("VACCT_ID", writeOffLog.getAcctId());
            param.put("VUSER_ID", writeOffLog.getUserId());
            param.put("VCYCLE_ID", String.valueOf(writeOffLog.getCycleId()));
            param.put("VNET_TYPE_CODE", writeOffLog.getNetTypeCode());
            param.put("VBILL_ID", writeOffLog.getBillId());
            param.put("VINTEGRATE_ITEM_CODE", String.valueOf(writeOffLog.getIntegrateItemCode()));
            param.put("VDEPOSIT_CODE", String.valueOf(writeOffLog.getDepositCode()));
            param.put("VACCT_BALANCE_ID", writeOffLog.getAcctBalanceId());
            param.put("VWRITEOFF_FEE", String.valueOf(writeOffLog.getWriteoffFee()));
            param.put("VIMP_FEE", String.valueOf(writeOffLog.getImpFee()));
            param.put("VFEE", String.valueOf(writeOffLog.getFee()));
            param.put("VOLD_BALANCE", String.valueOf(writeOffLog.getOldBalance()));
            param.put("VNEW_BALANCE", String.valueOf(writeOffLog.getNewBalance()));
            param.put("VLATE_FEE", String.valueOf(writeOffLog.getLateFee()));
            param.put("VLATE_BALANCE", String.valueOf(writeOffLog.getLateBalance()));
            param.put("VOLD_LATE_BALANCE", String.valueOf(writeOffLog.getOldLateBalance()));
            param.put("VNEW_LATE_BALANCE", String.valueOf(writeOffLog.getNewLateBalance()));
            param.put("VDERATE_LATE_FEE", String.valueOf(writeOffLog.getDerateLateFee()));
            param.put("VLATECAL_DATE", writeOffLog.getLatecalDate());
            param.put("VOLD_PAYTAG", String.valueOf(writeOffLog.getOldPaytag()));
            param.put("VNEW_PAYTAG", String.valueOf(writeOffLog.getNewPaytag()));
            param.put("VCAN_PAYTAG", String.valueOf(writeOffLog.getCanPaytag()));
            param.put("VOPERATE_TIME", writeOffLog.getOperateTime());
            param.put("VPROVINCE_CODE", writeOffLog.getProvinceCode());
            param.put("VEPARCHY_CODE", writeOffLog.getEparchyCode());
            param.put("VDRECV_TIMES", String.valueOf(writeOffLog.getDrecvTimes()));
            param.put("VCANCEL_TAG", String.valueOf(writeOffLog.getCancelTag()));
            param.put("VDEPOSIT_LIMIT_RULEID", String.valueOf(writeOffLog.getDepositLimitRuleid()));
            param.put("VDEPOSIT_PRIOR_RULEID", String.valueOf(writeOffLog.getDepositPriorRuleid()));
            param.put("VITEM_PRIOR_RULEID", String.valueOf(writeOffLog.getItemPriorRuleid()));
            params.add(param);
        }
        this.getJdbcTemplate(provinceCode).batchUpdate(sql.toString(), (Map<String, String>[]) params.toArray(new Map[params.size()]));
    }

    @Override
    public void insertAccessLog(List<AccessLog> logs, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_ACCESSLOG(ACCESS_ID,CHARGE_ID,ACCT_ID,");
        sql.append("ACCT_BALANCE_ID,DEPOSIT_CODE,OLD_BALANCE,MONEY,NEW_BALANCE,");
        sql.append("ACCESS_TAG,OPERATE_TIME,PROVINCE_CODE,EPARCHY_CODE,CANCEL_TAG,INVOICE_FEE) ");
        sql.append("VALUES(:VACCESS_ID,:VCHARGE_ID,");
        sql.append(":VACCT_ID,:VACCT_BALANCE_ID,:VDEPOSIT_CODE,");
        sql.append(" :VOLD_BALANCE,:VMONEY,:VNEW_BALANCE,");
        sql.append(":VACCESS_TAG,STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),");
        sql.append(":VPROVINCE_CODE,:VEPARCHY_CODE,:VCANCEL_TAG,:VINVOICE_FEE)");
        List params = new ArrayList(logs.size());
        for(AccessLog pAccessLog : logs) {
            Map<String, String> param = new HashMap<>();
            param.put("VACCESS_ID", pAccessLog.getAccessId ());
            param.put("VCHARGE_ID", pAccessLog.getChargeId());
            param.put("VACCT_ID", pAccessLog.getAcctId ());
            param.put("VACCT_BALANCE_ID", pAccessLog.getAcctBalanceId ());
            param.put("VDEPOSIT_CODE", String.valueOf(pAccessLog.getDepositCode()));
            param.put("VACCESS_TAG", String.valueOf(pAccessLog.getAccessTag()));
            param.put("VOLD_BALANCE", String.valueOf(pAccessLog.getOldBalance()));
            param.put("VMONEY", String.valueOf(pAccessLog.getMoney()));
            param.put("VNEW_BALANCE", String.valueOf(pAccessLog.getNewBalance()));
            param.put("VOPERATE_TIME", pAccessLog.getOperateTime());
            param.put("VEPARCHY_CODE", pAccessLog.getEparchyCode());
            param.put("VPROVINCE_CODE", pAccessLog.getProvinceCode());
            param.put("VCANCEL_TAG", String.valueOf(pAccessLog.getCancelTag()));
            param.put("VINVOICE_FEE", String.valueOf(pAccessLog.getInvoiceFee()));
            params.add(param);
        }
        this.getJdbcTemplate(provinceCode).batchUpdate(sql.toString(), (Map<String, String>[]) params.toArray(new Map[params.size()]));

    }

    @Override
    public long insertWriteSnapLog(WriteSnapLog writeSnapLog, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_WRITESNAP_LOG (CHARGE_ID,ACCT_ID,");
        sql.append("WRITEOFF_MODE,SPAY_FEE,ALL_MONEY,ALL_NEW_MONEY,ALL_BALANCE,");
        sql.append("ALL_NEW_BALANCE,ALLBOWE_FEE,AIMP_FEE,ALLNEWBOWE_FEE,PREREAL_FEE,");
        sql.append("CURREAL_FEE,PROTOCOL_BALANCE,OPERATE_TIME,EPARCHY_CODE,");
        sql.append("PROVINCE_CODE,CYCLE_ID,REMARK,RSRV_FEE1,RSRV_FEE2,RSRV_INFO1) ");
        sql.append("VALUES (:VCHARGE_ID,:VACCT_ID,:VWRITEOFF_MODE,");
        sql.append(":VSPAY_FEE,:VALL_MONEY,:VALL_NEW_MONEY,:VALL_BALANCE,:VALL_NEW_BALANCE,");
        sql.append(":VALLBOWE_FEE,:VAIMP_FEE,:VALLNEWBOWE_FEE,");
        sql.append(":VPREREAL_FEE,:VCURREAL_FEE,:VPROTOCOL_BALANCE,");
        sql.append("STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),:VEPARCHY_CODE,");
        sql.append(":VPROVINCE_CODE,:VCYCLE_ID,:VREMARK,:VRSRV_FEE1,:VRSRV_FEE2,:VRSRV_INFO1) ");
        Map<String, String> param = new HashMap<>();
        param.put("VCHARGE_ID", writeSnapLog.getChargeId());
        param.put("VACCT_ID", writeSnapLog.getAcctId());
        param.put("VWRITEOFF_MODE", String.valueOf(writeSnapLog.getWriteoffMode()));
        param.put("VSPAY_FEE", String.valueOf(writeSnapLog.getSpayFee()));
        param.put("VALL_MONEY", String.valueOf(writeSnapLog.getAllMoney()));
        param.put("VALL_NEW_MONEY", String.valueOf(writeSnapLog.getAllNewMoney()));
        param.put("VALL_BALANCE", String.valueOf(writeSnapLog.getAllBalance()));
        param.put("VALL_NEW_BALANCE", String.valueOf(writeSnapLog.getAllNewBalance()));
        param.put("VALLBOWE_FEE", String.valueOf(writeSnapLog.getAllBOweFee()));
        param.put("VAIMP_FEE", String.valueOf(writeSnapLog.getaImpFee()));
        param.put("VALLNEWBOWE_FEE", String.valueOf(writeSnapLog.getAllNewBOweFee()));
        param.put("VPREREAL_FEE", String.valueOf(writeSnapLog.getPreRealFee()));
        param.put("VCURREAL_FEE", String.valueOf(writeSnapLog.getCurRealFee()));
        param.put("VPROTOCOL_BALANCE", String.valueOf(writeSnapLog.getProtocolBalance()));
        param.put("VOPERATE_TIME", writeSnapLog.getOperateTime());
        param.put("VEPARCHY_CODE", writeSnapLog.getEparchyCode());
        param.put("VPROVINCE_CODE", writeSnapLog.getProvinceCode());
        param.put("VCYCLE_ID", String.valueOf(writeSnapLog.getCycleId()));
        param.put("VREMARK", writeSnapLog.getRemark());
        param.put("VRSRV_FEE1", String.valueOf(writeSnapLog.getRsrvFee1()));
        param.put("VRSRV_FEE2", String.valueOf(writeSnapLog.getRsrvFee2()));
        param.put("VRSRV_INFO1",writeSnapLog.getRsrvInfo1());
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }
}
