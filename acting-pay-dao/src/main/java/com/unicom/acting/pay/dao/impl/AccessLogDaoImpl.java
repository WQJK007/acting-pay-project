package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.AccessLogDao;
import com.unicom.acting.pay.domain.AccessLog;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccessLogDaoImpl extends JdbcBaseDao implements AccessLogDao {
    @Override
    public void insertAccessLog(List<AccessLog> accessLogs) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_ACCESSLOG(ACCESS_ID,CHARGE_ID,ACCT_ID,");
        sql.append("ACCT_BALANCE_ID,DEPOSIT_CODE,OLD_BALANCE,MONEY,NEW_BALANCE,");
        sql.append("ACCESS_TAG,OPERATE_TIME,PROVINCE_CODE,EPARCHY_CODE,CANCEL_TAG,");
        sql.append("INVOICE_FEE) VALUES(:VACCESS_ID,:VCHARGE_ID,");
        sql.append(":VACCT_ID,:VACCT_BALANCE_ID,:VDEPOSIT_CODE,");
        sql.append(" :VOLD_BALANCE,:VMONEY,:VNEW_BALANCE,");
        sql.append(":VACCESS_TAG,STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),");
        sql.append(":VPROVINCE_CODE,:VEPARCHY_CODE,:VCANCEL_TAG,:VINVOICE_FEE)");
        List params = new ArrayList(accessLogs.size());
        for(AccessLog pAccessLog : accessLogs) {
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
        this.getJdbcTemplate(DbTypes.ACTING_DRDS).batchUpdate(sql.toString(),
                (Map<String, String>[]) params.toArray(new Map[params.size()]));
    }

    /**
     * 根据交费流水和账户标识校验原交费的存取款日志
     * @param acctId
     * @param chargeId
     * @return
     */
    @Override
    public List<AccessLog> getOrigAccesslogsByAcctIdAndChargeId(String acctId, String chargeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCESS_ID, CHARGE_ID, ACCT_ID, ACCT_BALANCE_ID, DEPOSIT_CODE, OLD_BALANCE, MONEY,");
        sql.append(" NEW_BALANCE, ACCESS_TAG, DATE_FORMAT(OPERATE_TIME,'%Y-%m-%d %T') OPERATE_TIME,");
        sql.append(" EPARCHY_CODE, CANCEL_TAG, INVOICE_FEE, PROVINCE_CODE FROM TF_B_ACCESSLOG WHERE");
        sql.append(" ACCT_ID=:VACCT_ID AND CHARGE_ID=:VCHARGE_ID AND DEPOSIT_CODE <> 1007 AND ACCESS_TAG = '0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctId);
        param.put("VCHARGE_ID", chargeId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new AccessLogRowMapper());
    }

    /**
     * 根据交费流水和账户标识获取存取款日志
     * @param acctId
     * @param chargeId
     * @return
     */
    @Override
    public List<AccessLog> getAccesslogsByAcctIdAndChargeId(String acctId, String chargeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCESS_ID, CHARGE_ID, ACCT_ID, ACCT_BALANCE_ID, DEPOSIT_CODE, OLD_BALANCE, MONEY,");
        sql.append(" NEW_BALANCE, ACCESS_TAG, DATE_FORMAT(OPERATE_TIME,'%Y-%m-%d %T') OPERATE_TIME,");
        sql.append(" EPARCHY_CODE, CANCEL_TAG, INVOICE_FEE, PROVINCE_CODE FROM TF_B_ACCESSLOG");
        sql.append(" WHERE ACCT_ID=:VACCT_ID AND CHARGE_ID=:VCHARGE_ID AND CANCEL_TAG = '0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctId);
        param.put("VCHARGE_ID", chargeId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new AccessLogRowMapper());
    }

    /**
     * 根据交费流水和账户标识获取抵扣存取款日志
     * @param acctId
     * @param chargeId
     * @return
     */
    @Override
    public List<AccessLog> getAccesslogDByAcctIdAndChargeId(String acctId, String chargeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCESS_ID, CHARGE_ID, ACCT_ID, ACCT_BALANCE_ID, DEPOSIT_CODE, OLD_BALANCE, MONEY,");
        sql.append(" NEW_BALANCE, ACCESS_TAG, DATE_FORMAT(OPERATE_TIME,'%Y-%m-%d %T') OPERATE_TIME," );
        sql.append(" EPARCHY_CODE, CANCEL_TAG, INVOICE_FEE, PROVINCE_CODE FROM TF_B_ACCESSLOG_D");
        sql.append(" WHERE ACCT_ID=:VACCT_ID AND CHARGE_ID=:VCHARGE_ID AND CANCEL_TAG = '0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctId);
        param.put("VCHARGE_ID", chargeId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new AccessLogRowMapper());
    }

    /**
     * 隔笔返销判断
     * @param acctId
     * @param time
     * @return
     */
    @Override
    public int isLastAccesslog(String acctId, String time){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) COUNTS FROM TF_B_ACCESSLOG WHERE ACCT_ID = :VACCT_ID AND ");
        sql.append(" DATE_FORMAT(OPERATE_TIME,'%Y-%m-%d %H:%T') > DATE_FORMAT(:VOPERATE_TIME,'%Y-%m-%d %H:%T')" );
        sql.append(" AND CANCEL_TAG = '0' ");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctId);
        param.put("VOPERATE_TIME", time);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForObject(sql.toString(), param, new RowMapper<Integer>() {
            @Nullable
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("COUNTS");
            }
        });
    }

    /**
     * 隔笔返销判断
     * @param acctId
     * @param time
     * @return
     */
    @Override
    public int isLastAccesslogD(String acctId, String time){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) COUNTS FROM TF_B_ACCESSLOG_D WHERE ACCT_ID = :VACCT_ID AND ");
        sql.append(" DATE_FORMAT(OPERATE_TIME,'%Y-%m-%d %H:%T') > DATE_FORMAT(:VOPERATE_TIME,'%Y-%m-%d %H:%T')" );
        sql.append(" AND CANCEL_TAG = '0' ");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctId);
        param.put("VOPERATE_TIME", time);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForObject(sql.toString(), param, new RowMapper<Integer>() {
            @Nullable
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("COUNTS");
            }
        });
    }

    /**
     * 返销存取款日志
     * @param chargeId
     * @param acctId
     * @return
     */
    @Override
    public int updateAccesslogByChargeIdAndAcctId(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_ACCESSLOG SET CANCEL_TAG='1' WHERE CHARGE_ID=:VCHARGE_ID");
        sql.append(" AND CANCEL_TAG='0' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 返销抵扣存取款日志
     * @param chargeId
     * @param acctId
     * @return
     */
    @Override
    public int updateAccesslogDByChargeIdAndAcctId(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_ACCESSLOG_D SET CANCEL_TAG='1' WHERE CHARGE_ID=:VCHARGE_ID");
        sql.append(" AND CANCEL_TAG='0' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 存取款日志入库
     * @param accessLog
     * @return
     */
    @Override
    public int insertAccesslog(AccessLog accessLog){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_ACCESSLOG(ACCESS_ID,CHARGE_ID,ACCT_ID,");
        sql.append("ACCT_BALANCE_ID,DEPOSIT_CODE,OLD_BALANCE,MONEY,NEW_BALANCE,");
        sql.append("ACCESS_TAG,OPERATE_TIME,PROVINCE_CODE,EPARCHY_CODE,CANCEL_TAG,INVOICE_FEE) ");
        sql.append("VALUES(:VACCESS_ID,:VCHARGE_ID,");
        sql.append(":VACCT_ID,:VACCT_BALANCE_ID,:VDEPOSIT_CODE,");
        sql.append(" :VOLD_BALANCE,:VMONEY,:VNEW_BALANCE,");
        sql.append(":VACCESS_TAG,STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),");
        sql.append(":VPROVINCE_CODE,:VEPARCHY_CODE,:VCANCEL_TAG,:VINVOICE_FEE)");
        Map<String, String> param = new HashMap<>(14);
        param.put("VACCESS_ID", accessLog.getAccessId ());
        param.put("VCHARGE_ID", accessLog.getChargeId());
        param.put("VACCT_ID", accessLog.getAcctId ());
        param.put("VACCT_BALANCE_ID", accessLog.getAcctBalanceId ());
        param.put("VDEPOSIT_CODE", String.valueOf(accessLog.getDepositCode()));
        param.put("VACCESS_TAG", String.valueOf(accessLog.getAccessTag()));
        param.put("VOLD_BALANCE", String.valueOf(accessLog.getOldBalance()));
        param.put("VMONEY", String.valueOf(accessLog.getMoney()));
        param.put("VNEW_BALANCE", String.valueOf(accessLog.getNewBalance()));
        param.put("VOPERATE_TIME", accessLog.getOperateTime());
        param.put("VEPARCHY_CODE", accessLog.getEparchyCode());
        param.put("VPROVINCE_CODE", accessLog.getProvinceCode());
        param.put("VCANCEL_TAG", String.valueOf(accessLog.getCancelTag()));
        param.put("VINVOICE_FEE", String.valueOf(accessLog.getInvoiceFee()));
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 抵扣存取款日志入库
     * @param accessLog
     * @return
     */
    @Override
    public int insertAccesslogD(AccessLog accessLog){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_ACCESSLOG_D (ACCESS_ID,CHARGE_ID,ACCT_ID,");
        sql.append("ACCT_BALANCE_ID,DEPOSIT_CODE,OLD_BALANCE,MONEY,NEW_BALANCE,");
        sql.append("ACCESS_TAG,OPERATE_TIME,PROVINCE_CODE,EPARCHY_CODE,CANCEL_TAG,INVOICE_FEE) ");
        sql.append("VALUES(:VACCESS_ID,:VCHARGE_ID,");
        sql.append(":VACCT_ID,:VACCT_BALANCE_ID,:VDEPOSIT_CODE,");
        sql.append(" :VOLD_BALANCE,:VMONEY,:VNEW_BALANCE,");
        sql.append(":VACCESS_TAG,STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),");
        sql.append(":VPROVINCE_CODE,:VEPARCHY_CODE,:VCANCEL_TAG,:VINVOICE_FEE)");
        Map<String, String> param = new HashMap<>(14);
        param.put("VACCESS_ID", accessLog.getAccessId ());
        param.put("VCHARGE_ID", accessLog.getChargeId());
        param.put("VACCT_ID", accessLog.getAcctId ());
        param.put("VACCT_BALANCE_ID", accessLog.getAcctBalanceId ());
        param.put("VDEPOSIT_CODE", String.valueOf(accessLog.getDepositCode()));
        param.put("VACCESS_TAG", String.valueOf(accessLog.getAccessTag()));
        param.put("VOLD_BALANCE", String.valueOf(accessLog.getOldBalance()));
        param.put("VMONEY", String.valueOf(accessLog.getMoney()));
        param.put("VNEW_BALANCE", String.valueOf(accessLog.getNewBalance()));
        param.put("VOPERATE_TIME", accessLog.getOperateTime());
        param.put("VEPARCHY_CODE", accessLog.getEparchyCode());
        param.put("VPROVINCE_CODE", accessLog.getProvinceCode());
        param.put("VCANCEL_TAG", String.valueOf(accessLog.getCancelTag()));
        param.put("VINVOICE_FEE", String.valueOf(accessLog.getInvoiceFee()));
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    class AccessLogRowMapper implements RowMapper<AccessLog> {
        @Override
        public AccessLog mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            AccessLog accessLog = new AccessLog();
            accessLog.setAccessId(resultSet.getString("ACCESS_ID"));
            accessLog.setChargeId(resultSet.getString("CHARGE_ID"));
            accessLog.setAcctId(resultSet.getString("ACCT_ID"));
            accessLog.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            accessLog.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            accessLog.setOldBalance(resultSet.getLong("OLD_BALANCE"));
            accessLog.setMoney(resultSet.getLong("MONEY"));
            accessLog.setNewBalance(resultSet.getLong("NEW_BALANCE"));
            accessLog.setCancelTag(StringUtil.firstOfString(resultSet.getString("ACCESS_TAG")));
            accessLog.setOperateTime(resultSet.getString("OPERATE_TIME"));
            accessLog.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            accessLog.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            accessLog.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            accessLog.setInvoiceFee(resultSet.getLong("INVOICE_FEE"));
            return accessLog;
        }
    }
}
