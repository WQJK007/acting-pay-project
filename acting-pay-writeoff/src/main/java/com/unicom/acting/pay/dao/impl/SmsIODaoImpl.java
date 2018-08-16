package com.unicom.acting.pay.dao.impl;

import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.acting.fee.domain.NoticeInfo;
import com.unicom.acting.pay.dao.SmsIODao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SmsIODaoImpl extends JdbcBaseDao implements SmsIODao {
    @Override
    public void insertSmsIO(List<NoticeInfo> noticeInfoList, String provinceCode) {
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
        List params = new ArrayList(noticeInfoList.size());
        for (NoticeInfo noticeInfo : noticeInfoList) {
            Map<String, String> param = new HashMap();
            param.put("VSMS_ID", noticeInfo.getSmsNoticeId());
            param.put("VACCESS_CODE", noticeInfo.getAccessCode());
            param.put("VSEND_TIME_CODE", noticeInfo.getSendTimeCode());
            param.put("VRECV_OBJECT_TYPE", noticeInfo.getRecvObjectType());
            param.put("VRECV_OBJECT", noticeInfo.getRecvObject());
            param.put("VACCT_ID", noticeInfo.getAcctId());
            param.put("VEPARCHY_CODE", noticeInfo.getEparchyCode());
            param.put("VPROVINCE_CODE", noticeInfo.getProvinceCode());
            param.put("VPRODUCT_ID", noticeInfo.getProductId());
            param.put("VNOTICE_CONTENT", noticeInfo.getNoticeContent());
            param.put("VTEMPLET_ID", noticeInfo.getTempletId());
            param.put("VGENERATE_TIME", noticeInfo.getGenerateTime());
            param.put("VSEND_TIME_START", noticeInfo.getSendTimeStart());
            param.put("VEND_TIME", noticeInfo.getEndTime());
            param.put("VREVIEW_FLAG", noticeInfo.getReviewFlag());
            param.put("VREMARK", noticeInfo.getRemark());
            params.add(param);
        }
        this.getJdbcTemplate(provinceCode).batchUpdate(sql.toString(), (Map<String, String>[]) params.toArray(new Map[params.size()]));
    }
}
