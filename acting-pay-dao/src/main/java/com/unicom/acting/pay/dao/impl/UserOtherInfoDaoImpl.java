package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.UserOtherInfoDao;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户资料先关数据表操作
 *
 * @author Wangkh
 */
@Repository
public class UserOtherInfoDaoImpl extends JdbcBaseDao implements UserOtherInfoDao {
    @Override
    public long updateBadBillUserInfo(String acctId, String actTag, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_F_BADBILL_USERINFO SET ACT_TAG = :VACT_TAG ");
        sql.append("WHERE ACCT_ID = :VACCT_ID");
        Map<String, String> param = new HashMap<>();
        param.put("VACCT_ID", acctId);
        param.put("VACT_TAG", actTag);
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }
}
