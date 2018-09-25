package com.unicom.acting.pay.writeoff.dao.impl;

import com.unicom.acting.pay.domain.AccessLog;
import com.unicom.acting.pay.writeoff.dao.AccesslogActsDao;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ducj
 * @date 2018/8/2 10:05
 */
@Repository
public class AccesslogActsDaoImpl extends JdbcBaseDao implements AccesslogActsDao {

    /**
     * 账户中心- 根据acctId和chargeId判断存取款日志是否存在
     * @param acctId
     * @param chargeId
     * @return
     */
    @Override
    public boolean ifExistAccesslogByAcctIdAndChargeId(String acctId, String chargeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 1 FROM TF_B_ACCESSLOG WHERE CHARGE_ID=:VCHARGE_ID AND ACCT_ID=:VACCT_ID AND ACCESS_TAG='0'");
        Map<String, String> param = new HashMap<>(2);
        param.put("VCHARGE_ID", chargeId);
        param.put("VACCT_ID", acctId);
        List<String> result = this.getJdbcTemplate(DbTypes.ACTS_DRDS).queryForList(sql.toString(), param, String.class);
        if (!CollectionUtils.isEmpty(result)) {
            return true;
        }
        return false;
    }
}
