package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.SmsOrderDao;
import com.unicom.acting.pay.domain.SmsOrder;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SmsOrderDaoImpl extends JdbcBaseDao implements SmsOrderDao {
    @Override
    public void insertSmsOrder(List<SmsOrder> smsOrders) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_SMSLOG (SMS_ID,ACCESS_CODE,SEND_TIME_CODE,");
        sql.append("RECV_OBJECT_TYPE,RECV_OBJECT,ACCT_ID,EPARCHY_CODE,PROVINCE_CODE,PRODUCT_ID,");
        sql.append("NOTICE_CONTENT,TEMPLET_ID,GENERATE_TIME,SEND_TIME_START,END_TIME,");
        sql.append("REVIEW_FLAG,REMARK) VALUES (:VSMS_ID,:VACCESS_CODE,:VSEND_TIME_CODE,");
        sql.append(":VRECV_OBJECT_TYPE,:VRECV_OBJECT,:VACCT_ID,:VEPARCHY_CODE,");
        sql.append(":VPROVINCE_CODE,:VPRODUCT_ID,:VNOTICE_CONTENT,:VTEMPLET_ID,");
        sql.append("STR_TO_DATE(:VGENERATE_TIME,'%Y-%m-%d %T'),");
        sql.append("STR_TO_DATE(:VSEND_TIME_START,'%Y-%m-%d %T'),");
        sql.append("STR_TO_DATE(:VEND_TIME,'%Y-%m-%d %T'),");
        sql.append(":VREVIEW_FLAG,:VREMARK)");
        List params = new ArrayList(smsOrders.size());
        for (SmsOrder smsOrder : smsOrders) {
            Map<String, String> param = new HashMap();
            param.put("VSMS_ID", smsOrder.getSmsNoticeId());
            param.put("VACCESS_CODE", smsOrder.getAccessCode());
            param.put("VSEND_TIME_CODE", smsOrder.getSendTimeCode());
            param.put("VRECV_OBJECT_TYPE", smsOrder.getRecvObjectType());
            param.put("VRECV_OBJECT", smsOrder.getRecvObject());
            param.put("VACCT_ID", smsOrder.getAcctId());
            param.put("VEPARCHY_CODE", smsOrder.getEparchyCode());
            param.put("VPROVINCE_CODE", smsOrder.getProvinceCode());
            param.put("VPRODUCT_ID", smsOrder.getProductId());
            param.put("VNOTICE_CONTENT", smsOrder.getNoticeContent());
            param.put("VTEMPLET_ID", smsOrder.getTempletId());
            param.put("VGENERATE_TIME", smsOrder.getGenerateTime());
            param.put("VSEND_TIME_START", smsOrder.getSendTimeStart());
            param.put("VEND_TIME", smsOrder.getEndTime());
            param.put("VREVIEW_FLAG", smsOrder.getReviewFlag());
            param.put("VREMARK", smsOrder.getRemark());
            params.add(param);
        }
        this.getJdbcTemplate(DbTypes.ACTING_DRDS).batchUpdate(sql.toString(),
                (Map<String, String>[]) params.toArray(new Map[params.size()]));
    }
}
