package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.fee.domain.Bill;
import com.unicom.acting.pay.dao.BillPayDao;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BillPayDaoImpl extends JdbcBaseDao implements BillPayDao {
    @Override
    public boolean hasBadBillByAcctId(String acctId, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TS_B_BADBILL WHERE ACCT_ID=:VACCT_ID AND BILL_PAY_TAG='0'");
        Map param = new HashMap();
        param.put("VACCT_ID",acctId);
        List<String> result = this.getJdbcTemplate(provinceCode).queryForList(sql.toString(), param, String.class);
        if (!CollectionUtils.isEmpty(result)) {
            return true;
        }
        return false;
    }


    @Override
    public int updateBillBalance(Bill bill, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TS_B_BILL SET BALANCE=:balance,");
        sql.append("LATE_FEE=:lateFee,LATE_BALANCE=:lateBalance,");
        sql.append("LATECAL_DATE=STR_TO_DATE(:lateCalDate, '%Y-%m-%d %T'),");
        sql.append("PAY_TAG=:newPayTag,BILL_PAY_TAG=:newBillPayTag,VERSION_NO=VERSION_NO+1,");
        sql.append("UPDATE_TIME=STR_TO_DATE(:updateTime, '%Y-%m-%d %T'),");
        sql.append("UPDATE_DEPART_ID=:updateDepartId,UPDATE_STAFF_ID=:updateStaffId,");
        sql.append("CHARGE_ID=:chargeId,WRITEOFF_FEE1=:writeoffFee1,");
        sql.append("WRITEOFF_FEE2=:writeoffFee2,");
        sql.append("WRITEOFF_FEE3=:writeoffFee3 ");
        sql.append("WHERE ACCT_ID=:acctId ");
        sql.append("AND USER_ID=:userId ");
        sql.append("AND BILL_ID=:billId ");
        sql.append("AND INTEGRATE_ITEM_CODE=:integrateItemCode AND VERSION_NO=:versionNo");
        SqlParameterSource sps = new BeanPropertySqlParameterSource(bill);
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), sps);
    }

    @Override
    public int updateBadBillBalance(Bill bill, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TS_B_BADBILL SET BALANCE=:balance,");
        sql.append("LATE_FEE=:lateFee,LATE_BALANCE=:lateBalance,");
        sql.append("LATECAL_DATE=STR_TO_DATE(:lateCalDate, '%Y-%m-%d %T'),");
        sql.append("PAY_TAG=:newPayTag,BILL_PAY_TAG=:newBillPayTag,VERSION_NO=VERSION_NO+1,");
        sql.append("UPDATE_TIME=STR_TO_DATE(:updateTime, '%Y-%m-%d %T'),");
        sql.append("UPDATE_DEPART_ID=:updateDepartId,UPDATE_STAFF_ID=:updateStaffId,");
        sql.append("CHARGE_ID=:chargeId,WRITEOFF_FEE1=:writeoffFee1,");
        sql.append("WRITEOFF_FEE2=:writeoffFee2,");
        sql.append("WRITEOFF_FEE3=:writeoffFee3 ");
        sql.append("WHERE ACCT_ID=:acctId ");
        sql.append("AND USER_ID=:userId ");
        sql.append("AND BILL_ID=:billId ");
        sql.append("AND INTEGRATE_ITEM_CODE=:integrateItemCode AND VERSION_NO=:versionNo");
        SqlParameterSource sps = new BeanPropertySqlParameterSource(bill);
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), sps);
    }
}
