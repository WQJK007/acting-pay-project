package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.WriteSnapLogDao;
import com.unicom.acting.pay.domain.WriteSnapLog;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WriteSnapLogDaoImpl extends JdbcBaseDao implements WriteSnapLogDao {
    @Override
    public int insertWriteSnapLog(WriteSnapLog writeSnapLog) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_WRITESNAP_LOG (CHARGE_ID,ACCT_ID,");
        sql.append("WRITEOFF_MODE,SPAY_FEE,ALL_MONEY,ALL_NEW_MONEY,ALL_BALANCE,");
        sql.append("ALL_NEW_BALANCE,ALLBOWE_FEE,AIMP_FEE,ALLNEWBOWE_FEE,PREREAL_FEE,");
        sql.append("CURREAL_FEE,PROTOCOL_BALANCE,RECOVER_TAG,OPERATE_TIME,EPARCHY_CODE,");
        sql.append("PROVINCE_CODE,CYCLE_ID,REMARK,RSRV_FEE1,RSRV_FEE2,RSRV_INFO1) ");
        sql.append("VALUES (:VCHARGE_ID,:VACCT_ID,:VWRITEOFF_MODE,");
        sql.append(":VSPAY_FEE,:VALL_MONEY,:VALL_NEW_MONEY,:VALL_BALANCE,:VALL_NEW_BALANCE,");
        sql.append(":VALLBOWE_FEE,:VAIMP_FEE,:VALLNEWBOWE_FEE,");
        sql.append(":VPREREAL_FEE,:VCURREAL_FEE,:VPROTOCOL_BALANCE,:VRECOVER_TAG,");
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
        param.put("VRECOVER_TAG", String.valueOf(writeSnapLog.getRecoverTag()));
        param.put("VOPERATE_TIME", writeSnapLog.getOperateTime());
        param.put("VEPARCHY_CODE", writeSnapLog.getEparchyCode());
        param.put("VPROVINCE_CODE", writeSnapLog.getProvinceCode());
        param.put("VCYCLE_ID", String.valueOf(writeSnapLog.getCycleId()));
        param.put("VREMARK", writeSnapLog.getRemark());
        param.put("VRSRV_FEE1", String.valueOf(writeSnapLog.getRsrvFee1()));
        param.put("VRSRV_FEE2", String.valueOf(writeSnapLog.getRsrvFee2()));
        param.put("VRSRV_INFO1", writeSnapLog.getRsrvInfo1());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    /**
     * 查询快照日志
     *
     * @param chargeId 交费流水
     * @param acctId   账户标识
     * @return
     */
    @Override
    public int getWriteOffSnapLogByChargeIdAndAcctId(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) COUNTS FROM TF_B_WRITESNAP_LOG ");
        sql.append(" WHERE CHARGE_ID=:VCHARGE_ID AND ACCT_ID=:VACCT_ID AND CANCEL_TAG = '0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForObject(sql.toString(), param, new RowMapper<Integer>() {
            @Nullable
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("COUNTS");
            }
        });
    }

    /**
     * 查询抵扣快照日志
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    @Override
    public int getWriteOffSnapLogDByChargeIdAndAcctId(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  count(1) COUNTS FROM TF_B_WRITESNAP_LOG_D ");
        sql.append(" WHERE CHARGE_ID=:VCHARGE_ID AND ACCT_ID=:VACCT_ID AND CANCEL_TAG = '0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForObject(sql.toString(), param, new RowMapper<Integer>() {
            @Nullable
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("COUNTS");
            }
        });
    }
    /**
     * 返销更新快照表
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    @Override
    public int updateWriteoffSnapCancelTag(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_WRITESNAP_LOG SET CANCEL_TAG='1' WHERE CHARGE_ID=:VCHARGE_ID");
        sql.append("  AND CANCEL_TAG='0' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 更新抵扣快照表
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    @Override
    public int updateWriteoffDSanpCancelTag(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_WRITESNAP_LOG_D SET CANCEL_TAG='1' WHERE CHARGE_ID=:VCHARGE_ID");
        sql.append("  AND CANCEL_TAG='0' AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


    class WriteSnapLogRowMapper implements RowMapper<WriteSnapLog> {
        @Override
        public WriteSnapLog mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            WriteSnapLog writeSnapLog = new WriteSnapLog();
            writeSnapLog.setChargeId(resultSet.getString("CHARGE_ID"));
            writeSnapLog.setAcctId(resultSet.getString("ACCT_ID"));
            writeSnapLog.setWriteoffMode(StringUtil.firstOfString(resultSet.getString("WRITEOFF_MODE")));
            writeSnapLog.setSpayFee(resultSet.getInt("SPAY_FEE"));
            writeSnapLog.setAllMoney(resultSet.getInt("ALL_MONEY"));
            writeSnapLog.setAllNewMoney(resultSet.getInt("ALL_NEW_MONEY"));
            writeSnapLog.setAllBalance(resultSet.getInt("ALL_BALANCE"));
            writeSnapLog.setAllNewBalance(resultSet.getInt("ALL_NEW_BALANCE"));
            writeSnapLog.setAllBOweFee(resultSet.getInt("ALLBOWE_FEE"));
            writeSnapLog.setaImpFee(resultSet.getInt("AIMP_FEE"));
            writeSnapLog.setAllNewBOweFee(resultSet.getInt("ALLNEWBOWE_FEE"));
            writeSnapLog.setPreRealFee(resultSet.getInt("PREREAL_FEE"));
            writeSnapLog.setCurRealFee(resultSet.getInt("CURREAL_FEE"));
            writeSnapLog.setProtocolBalance(resultSet.getInt("PROTOCOL_BALANCE"));
            writeSnapLog.setOldRoundFee(StringUtil.firstOfString(resultSet.getString("OLD_ROUND_FEE")));
            writeSnapLog.setNewRoundFee(resultSet.getInt("NEW_ROUND_FEE"));
            writeSnapLog.setRecoverTag(StringUtil.firstOfString(resultSet.getString("RECOVER_TAG")));
            writeSnapLog.setOperateTime(resultSet.getString("OPERATE_TIME"));
            writeSnapLog.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            writeSnapLog.setCycleId(resultSet.getInt("CYCLE_ID"));
            writeSnapLog.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            writeSnapLog.setRemark(resultSet.getString("REMARK"));
            writeSnapLog.setRsrvFee1(resultSet.getInt("RSRV_FEE1"));
            writeSnapLog.setRsrvFee2(resultSet.getInt("RSRV_FEE2"));
            writeSnapLog.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            writeSnapLog.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            return writeSnapLog;
        }
    }

}
