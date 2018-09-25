package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.WriteOffLogDao;
import com.unicom.acting.pay.domain.WriteOffLog;
import com.unicom.acting.pay.domain.WriteSnapLog;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WriteOffLogDaoImpl extends JdbcBaseDao implements WriteOffLogDao {
    @Override
    public void insertWriteOffLog(List<WriteOffLog> writeOffLogs) {
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
        this.getJdbcTemplate(DbTypes.ACTING_DRDS).batchUpdate(sql.toString(),
                (Map<String, String>[]) params.toArray(new Map[params.size()]));
    }


    /**
     * 查询销账日志
     *
     * @param chargeId 交费流水
     * @param acctId   账户标识
     * @return 销账日志
     */
    @Override
    public List<WriteOffLog> getWriteOffLogByChargeIdAndAcctId(String chargeId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT WRITEOFF_ID, CHARGE_ID, ACCT_ID, USER_ID, CYCLE_ID, NET_TYPE_CODE, BILL_ID,");
        sql.append(" INTEGRATE_ITEM_CODE, DEPOSIT_CODE, ACCT_BALANCE_ID, WRITEOFF_FEE, IMP_FEE, FEE, OLD_BALANCE,");
        sql.append(" NEW_BALANCE, LATE_FEE, LATE_BALANCE, OLD_LATE_BALANCE, NEW_LATE_BALANCE, DERATE_LATE_FEE,");
        sql.append(" DATE_FORMAT(LATECAL_DATE,'%Y-%m-%d %T') LATECAL_DATE, OLD_PAYTAG, NEW_PAYTAG, CAN_PAYTAG,");
        sql.append(" DATE_FORMAT(OPERATE_TIME,'%Y-%m-%d %T') OPERATE_TIME, EPARCHY_CODE, DRECV_TIMES, CANCEL_TAG,");
        sql.append(" DEPOSIT_LIMIT_RULEID,DEPOSIT_PRIOR_RULEID, ITEM_PRIOR_RULEID, PROVINCE_CODE FROM TF_B_WRITEOFFLOG ");
        sql.append(" WHERE CHARGE_ID=:VCHARGE_ID AND  ACCT_ID=:VACCT_ID AND CANCEL_TAG = '0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new WriteOffLogRowMapper());
    }

    /**
     * 查询抵扣销账日志
     *
     * @param chargeId 交费流水
     * @param acctId   账户标识
     * @return 抵扣销账日志
     */
    @Override
    public List<WriteOffLog> getWriteOffLogDByChargeIdAndAcctId(String chargeId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT WRITEOFF_ID, CHARGE_ID, ACCT_ID, USER_ID, CYCLE_ID, NET_TYPE_CODE, BILL_ID,");
        sql.append(" INTEGRATE_ITEM_CODE, DEPOSIT_CODE, ACCT_BALANCE_ID, WRITEOFF_FEE, IMP_FEE, FEE, OLD_BALANCE,");
        sql.append(" NEW_BALANCE, LATE_FEE, LATE_BALANCE, OLD_LATE_BALANCE, NEW_LATE_BALANCE, DERATE_LATE_FEE,");
        sql.append(" DATE_FORMAT(LATECAL_DATE,'%Y-%m-%d %T') LATECAL_DATE, OLD_PAYTAG, NEW_PAYTAG, CAN_PAYTAG,");
        sql.append(" DATE_FORMAT(OPERATE_TIME,'%Y-%m-%d %T') OPERATE_TIME, EPARCHY_CODE, DRECV_TIMES, CANCEL_TAG,");
        sql.append(" DEPOSIT_LIMIT_RULEID,DEPOSIT_PRIOR_RULEID, ITEM_PRIOR_RULEID, PROVINCE_CODE FROM TF_B_WRITEOFFLOG_D ");
        sql.append(" WHERE CHARGE_ID=:VCHARGE_ID AND  ACCT_ID=:VACCT_ID AND CANCEL_TAG = '0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new WriteOffLogRowMapper());
    }

    /**
     * 返销更新销账日志表
     *
     * @param chargeId 交费流水
     * @param acctId   账户标识
     * @return 更新结果
     */
    @Override
    public int updateWriteoffCancelTag(String chargeId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_WRITEOFFLOG SET CANCEL_TAG='1' WHERE CHARGE_ID=:VCHARGE_ID");
        sql.append("  AND CANCEL_TAG='0' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 返销更新抵扣销账日志表
     *
     * @param chargeId 交费流水
     * @param acctId   账户标识
     * @return 更新结果
     */
    @Override
    public int updateWriteoffDCancelTag(String chargeId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_WRITEOFFLOG_D SET CANCEL_TAG='1' WHERE CHARGE_ID=:VCHARGE_ID");
        sql.append("  AND CANCEL_TAG='0' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    class WriteOffLogRowMapper implements RowMapper<WriteOffLog> {
        @Override
        public WriteOffLog mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            WriteOffLog writeOffLog = new WriteOffLog();
            writeOffLog.setWriteoffId(resultSet.getString("WRITEOFF_ID"));
            writeOffLog.setChargeId(resultSet.getString("CHARGE_ID"));
            writeOffLog.setAcctId(resultSet.getString("ACCT_ID"));
            writeOffLog.setUserId(resultSet.getString("USER_ID"));
            writeOffLog.setCycleId(resultSet.getInt("CYCLE_ID"));
            writeOffLog.setNetTypeCode(resultSet.getString("NET_TYPE_CODE"));
            writeOffLog.setBillId(resultSet.getString("BILL_ID"));
            writeOffLog.setIntegrateItemCode(resultSet.getInt("INTEGRATE_ITEM_CODE"));
            writeOffLog.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            writeOffLog.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            writeOffLog.setWriteoffFee(resultSet.getInt("WRITEOFF_FEE"));
            writeOffLog.setImpFee(resultSet.getInt("IMP_FEE"));
            writeOffLog.setFee(resultSet.getInt("FEE"));
            writeOffLog.setOldBalance(resultSet.getInt("OLD_BALANCE"));
            writeOffLog.setNewBalance(resultSet.getInt("NEW_BALANCE"));
            writeOffLog.setLateFee(resultSet.getInt("LATE_FEE"));
            writeOffLog.setLateBalance(resultSet.getInt("LATE_BALANCE"));
            writeOffLog.setOldLateBalance(resultSet.getInt("OLD_LATE_BALANCE"));
            writeOffLog.setNewLateBalance(resultSet.getInt("NEW_LATE_BALANCE"));
            writeOffLog.setDerateLateFee(resultSet.getInt("DERATE_LATE_FEE"));
            writeOffLog.setLatecalDate(resultSet.getString("LATECAL_DATE"));
            writeOffLog.setOldPaytag(StringUtil.firstOfString(resultSet.getString("OLD_PAYTAG")));
            writeOffLog.setNewPaytag(StringUtil.firstOfString(resultSet.getString("NEW_PAYTAG")));
            writeOffLog.setCanPaytag(StringUtil.firstOfString(resultSet.getString("CAN_PAYTAG")));
            writeOffLog.setOperateTime(resultSet.getString("OPERATE_TIME"));
            writeOffLog.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            writeOffLog.setDrecvTimes(resultSet.getInt("DRECV_TIMES"));
            writeOffLog.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            writeOffLog.setDepositLimitRuleid(resultSet.getInt("DEPOSIT_LIMIT_RULEID"));
            writeOffLog.setDepositPriorRuleid(resultSet.getInt("DEPOSIT_PRIOR_RULEID"));
            writeOffLog.setItemPriorRuleid(resultSet.getInt("ITEM_PRIOR_RULEID"));
            writeOffLog.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            return writeOffLog;
        }
    }


}
