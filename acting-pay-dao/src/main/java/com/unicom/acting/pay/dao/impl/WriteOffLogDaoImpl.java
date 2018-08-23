package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.WriteOffLogDao;
import com.unicom.acting.pay.domain.WriteOffLog;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WriteOffLogDaoImpl extends JdbcBaseDao implements WriteOffLogDao {
    @Override
    public void insertWriteOffLog(List<WriteOffLog> writeOffLogs, String provinceCode) {
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
        List params = new ArrayList(writeOffLogs.size());
        for (WriteOffLog writeOffLog : writeOffLogs) {
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
        this.getJdbcTemplate(provinceCode).batchUpdate(sql.toString(),
                (Map<String, String>[]) params.toArray(new Map[params.size()]));
    }
}
