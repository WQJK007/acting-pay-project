package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.BillDao;
import com.unicom.acting.pay.domain.Bill;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BillDaoImpl extends JdbcBaseDao implements BillDao {
    @Override
    public boolean hasBadBillByAcctId(String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TS_B_BADBILL WHERE ACCT_ID=:VACCT_ID AND BILL_PAY_TAG='0'");
        Map param = new HashMap();
        param.put("VACCT_ID",acctId);
        List<String> result = this.getJdbcTemplate(DbTypes.ACTING_DRDS).queryForList(sql.toString(), param, String.class);
        if (!CollectionUtils.isEmpty(result)) {
            return true;
        }
        return false;
    }


    @Override
    public int updateBillBalance(Bill bill) {
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
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), sps);
    }

    @Override
    public int updateBadBillBalance(Bill bill) {
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
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), sps);
    }



    /**
     * 返销更新账单
     * @param bill
     * @return
     */
    @Override
    public int updBillRevert(Bill bill)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TS_B_BILL SET BALANCE=BALANCE+:VBALANCE, LATE_FEE=:VLATE_FEE,");
        sql.append(" LATE_BALANCE=:VLATE_BALANCE, PAY_TAG=:VOLD_PAY_TAG, CANPAY_TAG=:VCANPAY_TAG,");
        sql.append(" LATECAL_DATE=STR_TO_DATE(:VLATECAL_DATE, '%Y-%m-%d %T'),");
        sql.append(" WRITEOFF_FEE1=WRITEOFF_FEE1-:VWRITEOFF_FEE1, WRITEOFF_FEE2=WRITEOFF_FEE2-:VWRITEOFF_FEE2,");
        sql.append(" WRITEOFF_FEE3=WRITEOFF_FEE3-:VWRITEOFF_FEE3, BILL_PAY_TAG='0', ");
        sql.append(" VERSION_NO=VERSION_NO+1 WHERE ACCT_ID=:VACCT_ID AND USER_ID=:VUSER_ID");
        sql.append(" AND BILL_ID=:VBILL_ID AND INTEGRATE_ITEM_CODE=:VINTEGRATE_ITEM_CODE");
        Map param = new HashMap<>(13);
        param.put("VBALANCE", bill.getBalance());
        param.put("VLATE_FEE", bill.getLateFee());
        param.put("VLATE_BALANCE", bill.getLateBalance());
        param.put("VOLD_PAY_TAG", String.valueOf(bill.getOldPayTag()));
        param.put("VCANPAY_TAG", String.valueOf(bill.getCanpayTag()));
        param.put("VLATECAL_DATE", bill.getLateCalDate());
        param.put("VWRITEOFF_FEE1", bill.getWriteoffFee1());
        param.put("VWRITEOFF_FEE2", bill.getWriteoffFee2());
        param.put("VWRITEOFF_FEE3", bill.getWriteoffFee3());
        param.put("VINTEGRATE_ITEM_CODE", bill.getIntegrateItemCode());
        param.put("VBILL_ID", bill.getBillId());
        param.put("VACCT_ID", bill.getAcctId());
        param.put("VUSER_ID", bill.getUserId());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 拷贝历史账单
     * @param acctId
     * @param userId
     * @param billId
     * @return
     */
    @Override
    public int copyHisBillToBill(String acctId, String userId, String billId){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TS_B_BILL (BILL_SEQ, PROVINCE_CODE, EPARCHY_CODE, NET_TYPE_CODE,");
        sql.append(" SERIAL_NUMBER, BILL_ID, ACCT_ID, USER_ID, CYCLE_ID,");
        sql.append(" INTEGRATE_ITEM_CODE, FEE, BALANCE, PRINT_FEE, B_DISCNT, A_DISCNT,");
        sql.append(" ADJUST_BEFORE, ADJUST_AFTER, LATE_FEE, LATE_BALANCE, LATECAL_DATE,");
        sql.append(" CANPAY_TAG, PAY_TAG, BILL_PAY_TAG, VERSION_NO, UPDATE_TIME, UPDATE_DEPART_ID,");
        sql.append(" UPDATE_STAFF_ID, CHARGE_ID, WRITEOFF_FEE1, WRITEOFF_FEE2, WRITEOFF_FEE3,");
        sql.append(" RSRV_FEE1, RSRV_FEE2, RSRV_FEE3, RSRV_INFO1, RSRV_INFO2)  SELECT");
        sql.append(" BILL_SEQ, PROVINCE_CODE, EPARCHY_CODE, NET_TYPE_CODE, SERIAL_NUMBER, BILL_ID,");
        sql.append(" ACCT_ID, USER_ID, CYCLE_ID, INTEGRATE_ITEM_CODE, FEE,");
        sql.append(" BALANCE, PRINT_FEE, B_DISCNT, A_DISCNT, ADJUST_BEFORE, ADJUST_AFTER,");
        sql.append(" LATE_FEE, LATE_BALANCE, LATECAL_DATE, CANPAY_TAG, PAY_TAG, BILL_PAY_TAG,");
        sql.append(" VERSION_NO, UPDATE_TIME, UPDATE_DEPART_ID, UPDATE_STAFF_ID, CHARGE_ID,");
        sql.append(" WRITEOFF_FEE1, WRITEOFF_FEE2, WRITEOFF_FEE3, RSRV_FEE1, RSRV_FEE2, RSRV_FEE3,");
        sql.append(" RSRV_INFO1, RSRV_INFO2  FROM TS_BH_BILL WHERE ACCT_ID = :VACCT_ID  AND");
        sql.append(" USER_ID = :VUSER_ID   AND BILL_ID = :VBILL_ID");
        Map<String, String> param = new HashedMap(3);
        param.put("VACCT_ID", acctId);
        param.put("VUSER_ID", userId);
        param.put("VBILL_ID", billId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
        //DESC_OWE_TAG, BACKUP_INFO, ROLL_BACK_INFO, CITY_CODE原代码中这些字段没拷贝
    }

    /**
     * 删除历史账单
     * @param acctId
     * @param userId
     * @param billId
     */
    @Override
    public int deleteHisBill(String acctId, String userId, String billId){
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM TS_BH_BILL WHERE ACCT_ID=:VACCT_ID AND USER_ID=:VUSER_ID AND BILL_ID=:VBILL_ID");
        Map<String, String> param = new HashedMap(3);
        param.put("VACCT_ID", acctId);
        param.put("VUSER_ID", userId);
        param.put("VBILL_ID", billId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 返销更新坏账账单
     * @param bill
     * @return
     */
    @Override
    public int updBillBadRevert(Bill bill)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TS_B_BADBILL SET BALANCE=BALANCE+:VBALANCE, LATE_FEE=:VLATE_FEE,");
        sql.append(" LATE_BALANCE=:VLATE_BALANCE, PAY_TAG=:VOLD_PAY_TAG, CANPAY_TAG=:VCANPAY_TAG,");
        sql.append(" LATECAL_DATE=STR_TO_DATE(:VLATECAL_DATE, '%Y-%m-%d %T'),");
        sql.append(" WRITEOFF_FEE1=WRITEOFF_FEE1-:VWRITEOFF_FEE1, WRITEOFF_FEE2=WRITEOFF_FEE2-:VWRITEOFF_FEE2,");
        sql.append(" WRITEOFF_FEE3=WRITEOFF_FEE3-:VWRITEOFF_FEE3, BILL_PAY_TAG='0', ");
        sql.append(" VERSION_NO=VERSION_NO+1 WHERE ACCT_ID=:VACCT_ID AND USER_ID=:VUSER_ID");
        sql.append(" AND BILL_ID=:VBILL_ID AND INTEGRATE_ITEM_CODE=:VINTEGRATE_ITEM_CODE");
        Map param = new HashMap<>(13);
        param.put("VBALANCE", bill.getBalance());
        param.put("VLATE_FEE", bill.getLateFee());
        param.put("VLATE_BALANCE", bill.getLateBalance());
        param.put("VOLD_PAY_TAG", String.valueOf(bill.getOldPayTag()));
        param.put("VCANPAY_TAG", String.valueOf(bill.getCanpayTag()));
        param.put("VLATECAL_DATE", bill.getLateCalDate());
        param.put("VWRITEOFF_FEE1", bill.getWriteoffFee1());
        param.put("VWRITEOFF_FEE2", bill.getWriteoffFee2());
        param.put("VWRITEOFF_FEE3", bill.getWriteoffFee3());
        param.put("VINTEGRATE_ITEM_CODE", bill.getIntegrateItemCode());
        param.put("VBILL_ID", bill.getBillId());
        param.put("VACCT_ID", bill.getAcctId());
        param.put("VUSER_ID", bill.getUserId());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 返销更新账单的bill_pay_tag
     * @param acctId
     * @param billId
     */
    @Override
    public int updBillRevertBillPayTag(String acctId, String billId){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TS_B_BILL SET BILL_PAY_TAG='0' WHERE ACCT_ID=:VACCT_ID  AND BILL_ID=:VBILL_ID");
        Map<String, String> param = new HashedMap(2);
        param.put("VACCT_ID", acctId);
        param.put("VBILL_ID", billId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 返销更新坏账账单的bill_pay_tag
     * @param acctId
     * @param billId
     * @return
     */
    @Override
    public int updBadBillRevertBillPayTag(String acctId, String billId){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TS_B_BADBILL SET BILL_PAY_TAG='0' WHERE ACCT_ID=:VACCT_ID  AND BILL_ID=:VBILL_ID");
        Map<String, String> param = new HashedMap(2);
        param.put("VACCT_ID", acctId);
        param.put("VBILL_ID", billId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    /**
     * 还原坏帐用户
     * @param acctId
     * @param actTag
     * @return
     */
    @Override
    public int updateBadbillUserInfo(String acctId, String actTag){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_F_BADBILL_USERINFO SET ACT_TAG='1' WHERE ACCT_ID=:VACCT_ID ");
        Map<String, String> param = new HashedMap(1);
        param.put("VACCT_ID", acctId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }


}
