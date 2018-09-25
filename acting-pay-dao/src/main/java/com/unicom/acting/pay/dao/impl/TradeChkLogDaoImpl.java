package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.TradeChkLogDao;
import com.unicom.acting.pay.domain.TradeHyLog;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TradeChkLogDaoImpl extends JdbcBaseDao implements TradeChkLogDao {
    @Override
    public int insertTradeHyLog(TradeHyLog tradeHyLog) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TL_B_TRADELOG_HY (PROVINCE_CODE,EPARCHY_CODE,OPER_ID,");
        sql.append("CHANNEL_CODE,TRADE_ID,SERIAL_NUMBER,OUTER_TRADE_TIME,CHANNEL_ID,");
        sql.append("PAYMENT_ID,PAY_FEE_MODE_CODE,TRADE_FEE,CHARGE_ID,NET_TYPE_CODE,");
        sql.append("CITY_CODE) VALUES (:VPROVINCE_CODE,:VEPARCHY_CODE,:VOPER_ID,");
        sql.append(":VCHANNEL_CODE,:VTRADE_ID,:VSERIAL_NUMBER,");
        sql.append("STR_TO_DATE(:VOUTER_TRADE_TIME, 'YYYYMMDDHH24MISS'),");
        sql.append(":VCHANNEL_ID,:VPAYMENT_ID,:VPAY_FEE_MODE_CODE,");
        sql.append(":VTRADE_FEE,:VCHARGE_ID,:VNET_TYPE_CODE,:VCITY_CODE)");
        Map param = new HashMap<>();
        param.put("VPROVINCE_CODE", tradeHyLog.getProvinceCode());
        param.put("VEPARCHY_CODE", tradeHyLog.getEparchyCode());
        param.put("VOPER_ID", tradeHyLog.getOperId());
        param.put("VCHANNEL_CODE", tradeHyLog.getChannelCode());
        param.put("VTRADE_ID", tradeHyLog.getTradeId());
        param.put("VSERIAL_NUMBER", tradeHyLog.getSerialNumber());
        param.put("VOUTER_TRADE_TIME", tradeHyLog.getOuterTradeTime());
        param.put("VCHANNEL_ID", tradeHyLog.getChannelId());
        param.put("VPAYMENT_ID", tradeHyLog.getPaymentId());
        param.put("VPAY_FEE_MODE_CODE", tradeHyLog.getPayFeeModeCode());
        param.put("VTRADE_FEE", tradeHyLog.getTradeFee());
        param.put("VCHARGE_ID", tradeHyLog.getChargerId());
        param.put("VNET_TYPE_CODE", tradeHyLog.getNetTypeCode());
        param.put("VCITY_CODE", tradeHyLog.getCityCode());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }
}
