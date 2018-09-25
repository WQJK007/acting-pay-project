package com.unicom.acting.pay.writeoff.dao.impl;

import com.unicom.acting.pay.writeoff.dao.DepositActsDao;
import com.unicom.acting.pay.writeoff.domain.AccountDeposit;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DepositActsDaoImpl extends JdbcBaseDao implements DepositActsDao {

    /**
     * 账户中心根据acctBalance和acctId获取账本可打金额
     * @param acctBalanceId
     * @param acctId
     * @return
     */
    @Override
    public List<AccountDeposit> getDepositCanPrintFeeByAcctBalanceIdAndAcctId(String acctBalanceId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT INVOICE_FEE,PRINT_FEE,VALID_TAG ");
        sql.append(" FROM TF_F_ACCOUNTDEPOSIT WHERE ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(1);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTS_DRDS).query(sql.toString(), param, new PAcctDepositMapper());
    }

    //账本结果集
    class PAcctDepositMapper implements RowMapper<AccountDeposit> {
        @Override
        public AccountDeposit mapRow(ResultSet resultSet, int i) throws SQLException {
            AccountDeposit pAcctDeposit = new AccountDeposit();
            pAcctDeposit.setInvoiceFee(resultSet.getLong("INVOICE_FEE"));
            pAcctDeposit.setPrintFee(resultSet.getLong("PRINT_FEE"));
            pAcctDeposit.setValidTag(StringUtil.firstOfString(resultSet.getString("VALID_TAG")));
/*          pAcctDeposit.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            pAcctDeposit.setAcctId(resultSet.getString("ACCT_ID"));
            pAcctDeposit.setUserId(resultSet.getString("USER_ID"));
            pAcctDeposit.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            pAcctDeposit.setDepositMoney(resultSet.getLong("DEPOSIT_MONEY"));
            pAcctDeposit.setInitMoney(resultSet.getLong("INIT_MONEY"));
            pAcctDeposit.setMoney(resultSet.getLong("MONEY"));
            pAcctDeposit.setLimitMode(StringUtil.firstOfString(resultSet.getString("LIMIT_MODE")));
            pAcctDeposit.setLimitMoney(resultSet.getLong("LIMIT_MONEY"));
            pAcctDeposit.setLimitLeft(resultSet.getLong("LIMIT_LEFT"));
            pAcctDeposit.setStartCycleId(resultSet.getInt("START_CYCLE_ID"));
            pAcctDeposit.setEndCycleId(resultSet.getInt("END_CYCLE_ID"));
            pAcctDeposit.setStartDate(resultSet.getString("START_DATE"));
            pAcctDeposit.setEndDate(resultSet.getString("END_DATE"));
            pAcctDeposit.setOweFee(resultSet.getLong("OWE_FEE"));
            pAcctDeposit.setFreezeFee(resultSet.getLong("FREEZE_FEE"));
            pAcctDeposit.setPrivateTag(StringUtil.firstOfString(resultSet.getString("PRIVATE_TAG")));
            pAcctDeposit.setActionCode(resultSet.getInt("ACTION_CODE"));
            pAcctDeposit.setVersionNo(resultSet.getInt("VERSION_NO"));
            pAcctDeposit.setUpdateTime(resultSet.getString("UPDATE_TIME"));
            pAcctDeposit.setOpenCycleId(resultSet.getInt("OPEN_CYCLE_ID"));
            pAcctDeposit.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            pAcctDeposit.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            pAcctDeposit.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            pAcctDeposit.setRsrvInfo2(resultSet.getString("RSRV_INFO2"));
            pAcctDeposit.setRsrvFee1(resultSet.getInt("RSRV_FEE1"));
            pAcctDeposit.setRsrvFee2(resultSet.getInt("RSRV_FEE2"));*/
            return pAcctDeposit;
        }
    }
}
