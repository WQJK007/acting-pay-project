package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.TransLogDao;
import com.unicom.acting.pay.domain.TransLog;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
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
public class TransLogDaoImpl extends JdbcBaseDao implements TransLogDao {

    /**
     * 根据acctId和chargeId获取transLog表中的记录
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return
     */
    @Override
    public int getTransLogNumByCharegeIdAndAcctId(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) COUNTS  FROM TF_B_TRANSLOG ");
        sql.append(" WHERE CHARGE_ID=:VCHARGE_ID AND ACCT_ID=:VACCT_ID");
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

}
