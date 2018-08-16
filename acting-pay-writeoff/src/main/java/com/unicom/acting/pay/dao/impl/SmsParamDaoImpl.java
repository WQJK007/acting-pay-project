package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.SmsParamDao;
import com.unicom.acting.pay.domain.SmsCond;
import com.unicom.acting.pay.domain.SmsConvert;
import com.unicom.acting.pay.domain.SmsTemplet;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Wangkh
 */
@Repository
public class SmsParamDaoImpl extends JdbcBaseDao implements SmsParamDao {
    @Override
    public List<SmsCond> getProvSmsCond(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TRADE_DEF_ID,SM_TEMPLET_ID,COND_ID,PROVINCE_CODE FROM TD_B_SMS_COND");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new SmsCondRowMapper());
    }

    @Override
    public List<SmsConvert> getSmsConvertId(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ORI_SM_TEMPLET_ID,SMS_TYPE,CONV_SM_TEMPLET_ID,PROVINCE_CODE FROM TD_B_SMS_CONVERT");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new SmsConvertRowMapper());
    }

    @Override
    public List<SmsTemplet> getSmsTemplet(String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SM_TEMPLET_ID,SM_TEMPLET_NAME,SM_TEMPLET_CONTEXT,");
        sql.append("SM_TEMPLET_TYPE,SM_KIND_CODE FROM TD_B_SMS_TEMPLET");
        return this.getJdbcTemplate(provinceCode).query(sql.toString(), new SmsTempletRowMapper());
    }

    private class SmsCondRowMapper implements RowMapper<SmsCond> {
        @Override
        public SmsCond mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            SmsCond smsCond = new SmsCond();
            smsCond.setTradeDefId(resultSet.getLong("TRADE_DEF_ID"));
            smsCond.setCondId(resultSet.getLong("COND_ID"));
            smsCond.setSmTempletId(resultSet.getLong("SM_TEMPLET_ID"));
            smsCond.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            return smsCond;
        }
    }

    private class SmsConvertRowMapper implements RowMapper<SmsConvert> {
        @Override
        public SmsConvert mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            SmsConvert smsConvert = new SmsConvert();
            smsConvert.setOriSmTempletId(resultSet.getLong("ORI_SM_TEMPLET_ID"));
            smsConvert.setSmsType(resultSet.getString("SMS_TYPE"));
            smsConvert.setConvSmTempletId(resultSet.getLong("CONV_SM_TEMPLET_ID"));
            smsConvert.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
            return smsConvert;
        }
    }

    private class SmsTempletRowMapper implements RowMapper<SmsTemplet> {
        @Override
        public SmsTemplet mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            SmsTemplet smsTemplet = new SmsTemplet();
            smsTemplet.setSmTempletId(resultSet.getInt("SM_TEMPLET_ID"));
            smsTemplet.setSmTempletName(resultSet.getString("SM_TEMPLET_NAME"));
            smsTemplet.setSmTempletContext(resultSet.getString("SM_TEMPLET_CONTEXT"));
            smsTemplet.setSmTempletType(StringUtil.firstOfString(resultSet.getString("SM_TEMPLET_TYPE")));
            smsTemplet.setSmKindCode(resultSet.getString("SM_KIND_CODE"));
            return smsTemplet;
        }
    }
}
