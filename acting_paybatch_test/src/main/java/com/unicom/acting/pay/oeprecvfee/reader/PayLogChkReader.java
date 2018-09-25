package com.unicom.acting.pay.oeprecvfee.reader;
import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.common.exception.ActBSysTypes;
import com.unicom.acting.pay.oeprecvfee.domain.PayLogChk;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@StepScope
@Component("payLogChkReader")
public class PayLogChkReader extends JdbcCursorItemReader<PayLogChk> {
    private static final Logger logger = LoggerFactory.getLogger(PayLogChkReader.class);
    @Value("#{jobParameters[dataSourceId]}")
    private String provinceId = "";

    @Value("#{stepExecutionContext[startVal]}")
    private String startId = "";

    @Value("#{stepExecutionContext[endVal]}")
    private String endId = "";

   // @Autowired
   // private JdbcDataSourceFactory dataSourceFactory;
    @Autowired
    private SpringContextUtil springContextUtil;

    @Autowired
    JdbcBaseDao jdbcBaseDao;

    @PostConstruct
    public void getPayLogChkReader() {
        try {
            logger.info("数据读入部分实例：{}, 参数：{}", this.hashCode(), startId + " : " + endId);

            //DataSource dataSource = dataSourceFactory.getDataSource("36");
            DataSource dataSource = jdbcBaseDao.getJdbcTemplate(DbTypes.ACT_RDS, provinceId).getJdbcTemplate().getDataSource();
            this.setDataSource(dataSource);
            this.setFetchSize(1000);
            this.setSql("select cast(trade_id as char) trade_id,                                            " +
                    "                      trade_type_code,                                             " +
                    "                      cast(batch_id as char) batch_id,                             " +
                    "                      priority,                                                    " +
                    "                      cast(charge_id as char) charge_id,                           " +
                    "                      cast(acct_id as char) acct_id,                               " +
                    "                      cast(user_id as char) user_id,                               " +
                    "                      serial_number,                                               " +
                    "                      writeoff_mode,                                               " +
                    "                      channel_id,                                                  " +
                    "                      payment_id,                                                  " +
                    "                      payment_op,                                                  " +
                    "                      pay_fee_mode_code,                                           " +
                    "                      cast(recv_fee as char) recv_fee,                             " +
                    "                      outer_trade_id,                                              " +
                    "                      bill_start_cycle_id start_cycle_id,                          " +
                    "                      bill_end_cycle_id end_cycle_id,                              " +
                    "                      date_format(start_date, 'yyyy-mm-dd hh24:mi:ss') start_date, " +
                    "                      date_format(end_date, 'yyyy-mm-dd hh24:mi:ss') end_date,     " +
                    "                      months,                                                      " +
                    "                      cast(limit_money as char) limit_money,                       " +
                    "                      payment_reason_code,                                         " +
                    "                      extend_tag,                                                  " +
                    "                      act_tag,                                                     " +
                    "                      action_code,                                                 " +
                    "                      cast(action_event_id as char) action_event_id,               " +
                    "                      cast(acct_balance_id as char) acct_balance_id,               " +
                    "                      deposit_code,                                                " +
                    "                      private_tag,                                                 " +
                    "                      remark,                                                      " +
                    "                      input_no,                                                    " +
                    "                      input_mode,                                                  " +
                    "                      cast(acct_id2 as char) acct_id2,                             " +
                    "                      cast(user_id2 as char) user_id2,                             " +
                    "                      deposit_code2,                                               " +
                    "                      cast(rel_charge_id as char) rel_charge_id,                   " +
                    "                      date_format(trade_time, 'yyyy-mm-dd hh24:mi:ss') trade_time, " +
                    "                      trade_eparchy_code,                                          " +
                    "                      trade_city_code,                                             " +
                    "                      trade_depart_id,                                             " +
                    "                      trade_staff_id,                                              " +
                    "                      cancel_tag,                                                  " +
                    "                      deal_tag,                                                    " +
                    "                      date_format(deal_time, 'yyyy-mm-dd hh24:mi:ss') deal_time,   " +
                    "                      result_code,                                                 " +
                    "                      result_info,                                                 " +
                    "                      cast(allbowe_fee as char) allbowe_fee,                       " +
                    "                      cast(allrowe_fee as char) allrowe_fee,                       " +
                    "                      cast(all_new_balance as char) all_new_balance,               " +
                    "                      cast(spay_fee as char) spay_fee,                             " +
                    "                      cast(rsrv_fee1 as char) rsrv_fee1,                           " +
                    "                      cast(rsrv_fee2 as char) rsrv_fee2,                           " +
                    "                      cast(rsrv_info1 as char) rsrv_info1,                         " +
                    "                      limit_mode,                                                  " +
                    "                      recover_tag,                                                  " +
                    "                      province_code                                                " +
                    "                 from tf_b_paylog_chk                                              " +
                    "                where trade_type_code = '7040'                                     " +
                    "                  and mod(acct_id, 10000) >= ?                              " +
                    "                  and mod(acct_id, 10000) <= ?                               " +
                    "                  and act_tag = '5'                                                " +
                    "                  and deal_tag = '1'                                              "+
                    "                   and province_code = ?                              "+
                    "                   and acct_id in(1115052358921184,1115050854857295,1115052358953838)");


            this.setPreparedStatementSetter(new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, startId);
                    ps.setString(2, endId);
                    ps.setString(3, provinceId);
                }
            });
            this.setRowMapper(new RowMapper<PayLogChk>() {
                @Override
                public PayLogChk mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PayLogChk paylogChk = new PayLogChk();
                    paylogChk.setTradeId(rs.getString(1));
                    paylogChk.setTradeTypeCode(rs.getInt(2));
                    paylogChk.setBatchId(rs.getString(3));
                    paylogChk.setPriority(rs.getInt(4));
                    paylogChk.setChargeId(rs.getString(5));
                    paylogChk.setAcctId(rs.getString(6));
                    paylogChk.setUserId(rs.getString(7));
                    paylogChk.setSerialNumber(rs.getString(8));
                    paylogChk.setWriteoffMode(rs.getString(9));
                    paylogChk.setChannelId(rs.getString(10));
                    paylogChk.setPaymentId(rs.getInt(11));
                    paylogChk.setPaymentOp(rs.getInt(12));
                    paylogChk.setPayFeeModeCode(rs.getInt(13));
                    paylogChk.setRecvFee(rs.getString(14));
                    paylogChk.setOuterTradeId(rs.getString(15));
                    paylogChk.setStartCycleId(rs.getInt(16));
                    paylogChk.setEndCycleId(rs.getInt(17));
                    paylogChk.setStartDate(rs.getString(18));
                    paylogChk.setEndDate(rs.getString(19));
                    paylogChk.setMonths(rs.getInt(20));
                    paylogChk.setLimitMoney(rs.getString(21));
                    paylogChk.setPaymentReasonCode(rs.getInt(22));
                    paylogChk.setExtendTag((char) rs.getInt(23));
                    paylogChk.setActTag(rs.getString(24));
                    paylogChk.setActionCode(rs.getString(25));
                    paylogChk.setActionEventId(rs.getString(26));
                    paylogChk.setAcctBalanceId(rs.getString(27));
                    paylogChk.setDepositCode(rs.getInt(28));
                    paylogChk.setPrivateTag((char) rs.getInt(29));
                    paylogChk.setRemark(rs.getString(30));
                    paylogChk.setInputNo(rs.getString(31));
                    paylogChk.setInputMode(rs.getInt(32));
                    paylogChk.setAcctId2(rs.getString(33));
                    paylogChk.setUserId2(rs.getString(34));
                    paylogChk.setDepositCode2(rs.getInt(35));
                    paylogChk.setRelChargeId(rs.getString(36));
                    paylogChk.setTradeTime(rs.getString(37));
                    paylogChk.setTradeEparchyCode(rs.getString(38));
                    paylogChk.setTradeCityCode(rs.getString(39));
                    paylogChk.setTradeDepartId(rs.getString(40));
                    paylogChk.setTradeStaffId(rs.getString(41));
                    paylogChk.setCancelTag(rs.getString(42));
                    paylogChk.setDealTag(rs.getString(43));
                    paylogChk.setDealTime(rs.getString(44));
                    paylogChk.setResultCode(rs.getString(45));
                    paylogChk.setResultInfo(rs.getString(46));
                    paylogChk.setAllboweFee(rs.getString(47));
                    paylogChk.setAllroweFee(rs.getString(48));
                    paylogChk.setAllNewBalance(rs.getString(49));
                    paylogChk.setSpayFee(rs.getString(50));
                    paylogChk.setRsrvFee1(rs.getString(51));
                    paylogChk.setRsrvFee2(rs.getString(52));
                    paylogChk.setRsrvInfo1(rs.getString(53));
                    paylogChk.setLimitMode(rs.getString(54));
                    paylogChk.setRecoverTag((char) rs.getInt(55));
                    paylogChk.setProvinceCode(rs.getString(56));
                    return paylogChk;
                }
            });
        }
        catch(ActBException e)
        {
            throw new ActBException(ActBSysTypes.ERR_BUSI_READER,"查询记录错误！");
        }
    }
}
