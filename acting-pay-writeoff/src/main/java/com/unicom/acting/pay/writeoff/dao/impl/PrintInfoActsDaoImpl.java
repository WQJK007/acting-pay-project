package com.unicom.acting.pay.writeoff.dao.impl;


import com.unicom.acting.pay.writeoff.dao.PrintInfoActsDao;
import com.unicom.acting.pay.writeoff.domain.PrintInfo;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PrintInfoActsDaoImpl extends JdbcBaseDao implements PrintInfoActsDao {

    private Logger logger = LoggerFactory.getLogger(PrintInfoActsDaoImpl.class);

    /**
     * 根据缴费流水和账户标识捞取打印日志
     * @param chargeId
     * @param acctId
     * @return
     */
    @Override
    public List<PrintInfo> getPrintInfoByChargeId(String chargeId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T1.FEE FROM TF_B_INVOICE_PRINTINFO T1,TF_B_INVOICE_PRINTLOG T2 ");
        sql.append(" WHERE T1.RECYCLE_TAG = '0' AND T1.ACCT_ID=T2.ACCT_ID");
        sql.append(" AND T1.PRINT_LOG_ID = T2.PRINT_LOG_ID AND  T2.TEMPLET_TYPE = '1'");
        sql.append(" AND T1.CHARGE_ID = :VCHARGE_ID AND T1.ACCT_ID=:VACCT_ID ");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctId);
        param.put("VCHARGE_ID", chargeId);
        return this.getJdbcTemplate(DbTypes.ACTS_DRDS).query(sql.toString(), param, new PrintInfoRowMapper());
    }

    class PrintInfoRowMapper implements RowMapper<PrintInfo> {
        @Override
        public PrintInfo mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            PrintInfo printInfo = new PrintInfo();
            printInfo.setFee(resultSet.getLong("FEE"));
            return printInfo;
        }
    }
}
