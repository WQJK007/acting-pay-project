package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.DerateLateFeeLogDao;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DerateLateFeeLogDaoImpl extends JdbcBaseDao implements DerateLateFeeLogDao {
    @Override
    public int updDerateLateFeeLogByDerateId(String derateId, String operateId, long usedDerateFee, char oldUseTag, String newUseTag, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TF_B_DERATELATEFEELOG SET USED_DERATE_FEE=:VUSED_DERATE_FEE,");
        sql.append("USE_TAG=:VNEW_USE_TAG,OPERATE_ID=:VOPERATE_ID ");
        sql.append("WHERE DERATE_ID=:VDERATE_ID AND USE_TAG=:VOLD_USE_TAG");
        Map<String, String> param = new HashMap<>();
        param.put("VUSED_DERATE_FEE", String.valueOf(usedDerateFee));
        param.put("VNEW_USE_TAG", newUseTag);
        param.put("VOPERATE_ID", operateId);
        param.put("VDERATE_ID", derateId);
        param.put("VOLD_USE_TAG", String.valueOf(oldUseTag));
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }
}
