package com.unicom.acting.pay.writeoff.dao.impl;


import com.unicom.acting.fee.domain.Staff;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.acting.pay.domain.PayLogDmn;
import com.unicom.acting.pay.writeoff.dao.PayLogActsDao;
import com.unicom.acting.pay.writeoff.domain.DiscntDeposit;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PayLogActsDaoImpl extends JdbcBaseDao implements PayLogActsDao {

    /**
     * 账户中心根据外围流水和账户标识判断缴费是否存在
     * @param tradeId
     * @param acctId
     * @return
     */
    @Override
    public boolean ifExistOuterTradeId(String tradeId, String acctId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TF_B_PAYLOG WHERE OUTER_TRADE_ID=:VOUTER_TRADE_ID AND ACCT_ID=:VACCT_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VOUTER_TRADE_ID", tradeId);
        param.put("VACCT_ID", acctId);
        List<String> result = this.getJdbcTemplate(DbTypes.ACTS_DRDS).queryForList(sql.toString(), param, String.class);
        if (!CollectionUtils.isEmpty(result)) {
            return true;
        }
        return false;
    }


    class PaylogDmnRowMapper implements RowMapper<PayLogDmn> {
        @Override
        public PayLogDmn mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            PayLogDmn payLogDmn = new PayLogDmn();
            payLogDmn.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
            payLogDmn.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            payLogDmn.setTradeId(resultSet.getString("TRADE_ID"));
            payLogDmn.setTradeTypeCode(resultSet.getInt("TRADE_TYPE_CODE"));
            payLogDmn.setBatchId(resultSet.getString("BATCH_ID"));
            payLogDmn.setPriority(resultSet.getInt("PRIORITY"));
            payLogDmn.setChargeId(resultSet.getString("CHARGE_ID"));
            payLogDmn.setAcctId(resultSet.getString("ACCT_ID"));
            payLogDmn.setUserId(resultSet.getString("USER_ID"));
            payLogDmn.setSerialNumber(resultSet.getString("SERIAL_NUMBER"));
            payLogDmn.setWriteoffMode(StringUtil.firstOfString(resultSet.getString("WRITEOFF_MODE")));
            payLogDmn.setLimitMode(StringUtil.firstOfString(resultSet.getString("LIMIT_MODE")));
            payLogDmn.setChannelId(resultSet.getString("CHANNEL_ID"));
            payLogDmn.setPaymentId(resultSet.getInt("PAYMENT_ID"));
            payLogDmn.setPaymentOp(resultSet.getInt("PAYMENT_OP"));
            payLogDmn.setPayFeeModeCode(resultSet.getInt("PAY_FEE_MODE_CODE"));
            payLogDmn.setRecvFee(resultSet.getInt("RECV_FEE"));
            payLogDmn.setOuterTradeId(resultSet.getString("OUTER_TRADE_ID"));
            payLogDmn.setBillStartCycleId(resultSet.getInt("START_CYCLE_ID"));
            payLogDmn.setBillEndCycleId(resultSet.getInt("END_CYCLE_ID"));
            payLogDmn.setStartDate(resultSet.getString("START_DATE"));
            payLogDmn.setEndDate(resultSet.getString("END_DATE"));
            payLogDmn.setMonths(resultSet.getInt("MONTHS"));
            payLogDmn.setLimitMoney(resultSet.getInt("LIMIT_MONEY"));
            payLogDmn.setPaymentReasonCode(resultSet.getInt("PAYMENT_REASON_CODE"));
            payLogDmn.setExtendTag(StringUtil.firstOfString(resultSet.getString("EXTEND_TAG")));
            payLogDmn.setActionCode(resultSet.getInt("ACTION_CODE"));
            payLogDmn.setActionEventId(resultSet.getString("ACTION_EVENT_ID"));
            payLogDmn.setAcctBalanceId(resultSet.getString("ACCT_BALANCE_ID"));
            payLogDmn.setDepositCode(resultSet.getInt("DEPOSIT_CODE"));
            payLogDmn.setPrivateTag(StringUtil.firstOfString(resultSet.getString("PRIVATE_TAG")));
            payLogDmn.setRemark(resultSet.getString("REMARK"));
            payLogDmn.setInputNo(resultSet.getString("INPUT_NO"));
            payLogDmn.setInputMode(resultSet.getInt("INPUT_MODE"));
            payLogDmn.setAcctId2(resultSet.getString("ACCT_ID2"));
            payLogDmn.setUserId2(resultSet.getString("USER_ID2"));
            payLogDmn.setDepositCode2(resultSet.getInt("DEPOSIT_CODE2"));
            payLogDmn.setRelChargeId(resultSet.getString("REL_CHARGE_ID"));
            payLogDmn.setTradeTime(resultSet.getString("RECV_TIME"));
            payLogDmn.setTradeEparchyCode(resultSet.getString("TRADE_EPARCHY_CODE"));
            payLogDmn.setTradeCityCode(resultSet.getString("TRADE_CITY_CODE"));
            payLogDmn.setTradeDepartId(resultSet.getString("TRADE_DEPART_ID"));
            payLogDmn.setTradeStaffId(resultSet.getString("TRADE_STAFF_ID"));
            payLogDmn.setCancelTag(StringUtil.firstOfString(resultSet.getString("CANCEL_TAG")));
            payLogDmn.setDealTag(StringUtil.firstOfString(resultSet.getString("DEAL_TAG")));
            payLogDmn.setDealTime(resultSet.getString("DEAL_TIME"));
            payLogDmn.setResultCode(resultSet.getInt("RESULT_CODE"));
            payLogDmn.setResultInfo(resultSet.getString("RESULT_INFO"));
            payLogDmn.setRsrvFee1(resultSet.getInt("RSRV_FEE1"));
            payLogDmn.setRsrvFee2(resultSet.getInt("RSRV_FEE2"));
            payLogDmn.setRsrvInfo1(resultSet.getString("RSRV_INFO1"));
            payLogDmn.setPrintTag(StringUtil.firstOfString(resultSet.getString("PRINT_TAG")));
            return payLogDmn;
        }
    }

}
