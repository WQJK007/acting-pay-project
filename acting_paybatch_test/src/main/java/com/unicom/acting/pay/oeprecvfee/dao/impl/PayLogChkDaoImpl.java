package com.unicom.acting.pay.oeprecvfee.dao.impl;

import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.oeprecvfee.dao.PayLogChkDao;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PayLogChkDaoImpl extends JdbcBaseDao implements PayLogChkDao {
    @Override
    public int updatePayLogChk(BatchRecvFeeIn recvFeeCommInfoIn){
        StringBuilder sql = new StringBuilder();
        sql.append("update tf_b_paylog_chk");
        sql.append(" set act_tag     = :VACT_TAG,");
        sql.append(" deal_tag    = :VDEAL_TAG, ");
        sql.append(" deal_time   = sysdate(),");
        sql.append(" result_code = :VRESULT_CODE,");
        sql.append(" result_info = :VRESULT_INFO");
        sql.append(" where trade_id =:VTRADE_ID");
        sql.append(" and trade_type_code = :VTRADE_TYPE_CODE");
        sql.append(" and batch_id = :VBATCH_ID");
        sql.append(" and deal_tag = :VORG_DEAL_TAG");
        sql.append(" and act_tag = :VORG_ACT_TAG");
        Map param = new HashMap<>(9);
        param.put("VTRADE_ID", recvFeeCommInfoIn.getOriginTradeId());
        param.put("VBATCH_ID", recvFeeCommInfoIn.getBatchId());
        param.put("VTRADE_TYPE_CODE", recvFeeCommInfoIn.getTradeTypeCode());
        param.put("VACT_TAG", recvFeeCommInfoIn.getActTag());
        param.put("VDEAL_TAG", recvFeeCommInfoIn.getDealtag());
        param.put("VORG_ACT_TAG", recvFeeCommInfoIn.getOriginActTag());
        param.put("VORG_DEAL_TAG", recvFeeCommInfoIn.getOriginDealtag());
        param.put("VRESULT_CODE", recvFeeCommInfoIn.getResultCode());
        param.put("VRESULT_INFO", recvFeeCommInfoIn.getResultInfo());
        return this.getJdbcTemplate(DbTypes.ACT_RDS,recvFeeCommInfoIn.getProvinceCode()).update(sql.toString(), param);
    }
    public int updatePayLogChkByType(BatchRecvFeeIn recvFeeIn, int desTradeTypeCode, int oraTradeTypeCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("update TF_B_PAYLOG_CHK");
        sql.append(" set TRADE_TYPE_CODE = :VDES_TRADE_TYPE_CODE");
        sql.append(" where TRADE_ID = :VTRADE_ID ");
        sql.append(" and BATCH_ID = :VBATCH_ID ");
        sql.append(" and TRADE_TYPE_CODE = :VTRADE_TYPE_CODE ");
        Map param = new HashMap<>(4);
        param.put("VDES_TRADE_TYPE_CODE", desTradeTypeCode);
        param.put("VTRADE_ID", recvFeeIn.getOriginTradeId());
        param.put("VBATCH_ID", recvFeeIn.getBatchId());
        param.put("VTRADE_TYPE_CODE", oraTradeTypeCode);
        return this.getJdbcTemplate(DbTypes.ACT_RDS,recvFeeIn.getProvinceCode()).update(sql.toString(), param);

    }

}
