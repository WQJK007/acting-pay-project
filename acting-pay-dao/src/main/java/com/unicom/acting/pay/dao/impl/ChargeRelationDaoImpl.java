package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.ChargeRelationDao;
import com.unicom.acting.pay.domain.ChargeRelation;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ducj
 */
@Repository
public class ChargeRelationDaoImpl extends JdbcBaseDao implements ChargeRelationDao {

    @Override
    public List<ChargeRelation> getChargeRelationByChargeId(String chargeId, String acctId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT OPERATE_ID1 FROM TF_B_CHARGERELATION WHERE ACCT_ID=:VACCT_ID AND OPERATE_ID2=:VCHARGE_ID");
        Map<String, String> param = new HashMap<>(2);
        param.put("VACCT_ID", acctId);
        param.put("VCHARGE_ID", chargeId);
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).query(sql.toString(), param, new ChargeRelationRowMapper());
    }


    @Override
    public int insertChargeRelation(ChargeRelation chargeRelation){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_CHARGERELATION (ID, OPERATE_ID1, OPERATE_ID2,");
        sql.append(" OPERATE_TYPE, DEBUTY_CODE, OPERATE_STAFF_ID,");
        sql.append(" OPERATE_DEPART_ID, OPERATE_CITY_CODE, OPERATE_EPARCHY_CODE,");
        sql.append(" OPERATE_TIME, PROVINCE_CODE, EPARCHY_CODE, ACCT_ID) VALUES ( ");
        sql.append(":VID, :VOPERATE_ID1, :VOPERATE_ID2, :VOPERATE_TYPE, :VDEBUTY_CODE, :VOPERATE_EPARCHY_CODE,");
        sql.append(":VOPERATE_STAFF_ID, :VOPERATE_DEPART_ID, :VOPERATE_CITY_CODE,");
        sql.append(" STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'), :VPROVINCE_CODE, :VEPARCHY_CODE, :VACCT_ID)");
        Map<String, String> param = new HashMap<>(13);
        param.put("VID", chargeRelation.getId());
        param.put("VOPERATE_ID1", chargeRelation.getOperateId1());
        param.put("VOPERATE_ID2", chargeRelation.getOperateId2());
        param.put("VOPERATE_TYPE", chargeRelation.getOperateType());
        param.put("VDEBUTY_CODE", chargeRelation.getDebutyCode());
        param.put("VOPERATE_STAFF_ID", chargeRelation.getOperateStaffId());
        param.put("VOPERATE_DEPART_ID", chargeRelation.getOperateDepartId());
        param.put("VOPERATE_CITY_CODE", chargeRelation.getOperateCityCode());
        param.put("VOPERATE_EPARCHY_CODE", chargeRelation.getOperateEparchyCode());
        param.put("VOPERATE_TIME", chargeRelation.getOperateTime());
        param.put("VPROVINCE_CODE", chargeRelation.getProvinceCode());
        param.put("VEPARCHY_CODE", chargeRelation.getEparchyCode());
        param.put("VACCT_ID", chargeRelation.getAcctId());
        return this.getJdbcTemplate(DbTypes.ACTING_DRDS).update(sql.toString(), param);
    }

    class ChargeRelationRowMapper implements RowMapper<ChargeRelation> {
        @Override
        public ChargeRelation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            ChargeRelation chargeRelation = new ChargeRelation();
            chargeRelation.setOperateId1(resultSet.getString("OPERATE_ID1"));
//            chargeRelation.setId(resultSet.getString("ID"));
//            chargeRelation.setOperateId2(resultSet.getString("OPERATE_ID2"));
//            chargeRelation.setOperateType(resultSet.getString("OPERATE_TYPE"));
//            chargeRelation.setDebutyCode(resultSet.getString("DEBUTY_CODE"));
//            chargeRelation.setOperateStaffId(resultSet.getString("OPERATE_STAFF_ID"));
//            chargeRelation.setOperateDepartId(resultSet.getString("OPERATE_DEPART_ID"));
//            chargeRelation.setOperateCityCode(resultSet.getString("OPERATE_CITY_CODE"));
//            chargeRelation.setOperateEparchyCode(resultSet.getString("OPERATE_EPARCHY_CODE"));
//            chargeRelation.setOperateTime(resultSet.getString("OPERATE_TIME"));
//            chargeRelation.setProvinceCode(resultSet.getString("PROVINCE_CODE"));
//            chargeRelation.setEparchyCode(resultSet.getString("EPARCHY_CODE"));
//            chargeRelation.setAcctId(resultSet.getString("ACCT_ID"));
            return chargeRelation;
        }
    }



}
