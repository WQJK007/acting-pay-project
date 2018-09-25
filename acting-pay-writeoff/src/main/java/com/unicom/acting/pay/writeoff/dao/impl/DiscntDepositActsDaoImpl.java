package com.unicom.acting.pay.writeoff.dao.impl;

import com.unicom.acting.pay.writeoff.dao.DiscntDepositActsDao;
import com.unicom.acting.pay.writeoff.domain.DiscntDeposit;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ducj
 */
@Repository
public class DiscntDepositActsDaoImpl extends JdbcBaseDao implements DiscntDepositActsDao {

    /**
     * 根据账户标识和用户标识获取活动账本数据
     * @param userId
     * @param acctId
     * @return
     */
    @Override
    public List<DiscntDeposit> getDiscntDepositsByUserIdAndAcctId(String userId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACTION_EVENT_ID,CHARGE_ID,DISCNT_MODE,ACTION_CODE,DISCOUNT_CODE ");
        sql.append(" FROM TF_B_DISCNT_DEPOSIT  WHERE USER_ID = :VUSER_ID AND CANCEL_TAG = '0' ");
        sql.append(" AND ACCT_ID=:VACCT_ID AND LEFT_MONEY > 0");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctId);
        param.put("VUSER_ID", userId);
        return this.getJdbcTemplate(DbTypes.ACTS_DRDS).query(sql.toString(), param, new DiscntDepositMapper());
    }


    @Override
    public List<DiscntDeposit> getUserDiscntDepositByUserId(String acctId, String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ACCT_BALANCE_ID,LEFT_MONEY,LIMIT_MODE,SPLIT_METHOD,MONEY,");
        sql.append("LIMIT_MONEY,MONTHS FROM TF_B_DISCNT_DEPOSIT WHERE USER_ID = :VUSER_ID ");
        sql.append("AND ACCT_ID = :VACCT_ID AND CANCEL_TAG = '0' ");
        sql.append("AND START_CYCLE_ID <= 197001 AND END_CYCLE_ID <= 197001 ");
        sql.append("UNION ALL ");
        sql.append("SELECT ACCT_BALANCE_ID,LEFT_MONEY,LIMIT_MODE,SPLIT_METHOD,MONEY,");
        sql.append("LIMIT_MONEY,MONTHS FROM TF_B_DISCNT_DEPOSIT WHERE USER_ID = :VUSER_ID ");
        sql.append("AND ACCT_ID = :VACCT_ID AND CANCEL_TAG = '0' ");
        sql.append("AND END_CYCLE_ID >= :VCYCLE_ID ");
        Map<String, String> param = new HashMap<>(3);
        param.put("VUSER_ID", userId);
        param.put("VACCT_ID", acctId);
        param.put("VCYCLE_ID", TimeUtil.getSysdate(TimeUtil.DATETIME_FORMAT_6));
        return this.getJdbcTemplate(DbTypes.ACTS_DRDS).query(sql.toString(), param, new RowMapper<DiscntDeposit>() {
            @Override
            public DiscntDeposit mapRow(ResultSet rs, int rowNum) throws SQLException {
                DiscntDeposit discntDeposit = new DiscntDeposit();
                discntDeposit.setAcctBalanceId(rs.getString("ACCT_BALANCE_ID"));
                discntDeposit.setLeftMoney(rs.getLong("LEFT_MONEY"));
                discntDeposit.setLimitMode(StringUtil.firstOfString(rs.getString("LIMIT_MODE")));
                discntDeposit.setMoney(rs.getLong("MONEY"));
                discntDeposit.setLimitMoney(rs.getLong("LIMIT_MONEY"));
                discntDeposit.setMonths(rs.getInt("MONTHS"));
                discntDeposit.setSplitMethod(rs.getString("SPLIT_METHOD"));
                return discntDeposit;
            }

        });
    }


    @Override
    public List<DiscntDeposit> getDiscntDepositsByChargeIdAndAcctId(String userId, String acctId){
        return null;
    }


    @Override
    public List<DiscntDeposit> getDiscntDepositsByEventIdIdAndAcctId(String actionEventId, String acctId){
        return null;
    }

    //活动账本结果集
    class DiscntDepositMapper implements RowMapper<DiscntDeposit> {
        @Override
        public DiscntDeposit mapRow(ResultSet resultSet, int i) throws SQLException {
            DiscntDeposit discntDeposit = new DiscntDeposit();
            discntDeposit.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            discntDeposit.setAcctId(resultSet.getString("ACCT_ID"));
            discntDeposit.setUserId(resultSet.getString("USER_ID"));
            discntDeposit.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            discntDeposit.setMoney(resultSet.getLong("MONEY"));
            discntDeposit.setLimitMode(StringUtil.firstOfString(resultSet.getString("LIMIT_MODE")));
            discntDeposit.setLimitMoney(resultSet.getLong("LIMIT_MONEY"));
            discntDeposit.setStartCycleId(resultSet.getInt("START_CYCLE_ID"));
            discntDeposit.setEndCycleId(resultSet.getInt("END_CYCLE_ID"));
            discntDeposit.setActionCode(resultSet.getInt("ACTION_CODE"));
            discntDeposit.setVersionNo(resultSet.getInt("VERSION_NO"));
            discntDeposit.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            discntDeposit.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            return discntDeposit;
        }
    }

}
