package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.WriteSnapLogDao;
import com.unicom.acting.pay.domain.WriteSnapLog;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class WriteSnapLogDaoImpl extends JdbcBaseDao implements WriteSnapLogDao {
    @Override
    public int insertWriteSnapLog(WriteSnapLog writeSnapLog, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_WRITESNAP_LOG (CHARGE_ID,ACCT_ID,");
        sql.append("WRITEOFF_MODE,SPAY_FEE,ALL_MONEY,ALL_NEW_MONEY,ALL_BALANCE,");
        sql.append("ALL_NEW_BALANCE,ALLBOWE_FEE,AIMP_FEE,ALLNEWBOWE_FEE,PREREAL_FEE,");
        sql.append("CURREAL_FEE,PROTOCOL_BALANCE,OPERATE_TIME,EPARCHY_CODE,");
        sql.append("PROVINCE_CODE,CYCLE_ID,REMARK,RSRV_FEE1,RSRV_FEE2,RSRV_INFO1) ");
        sql.append("VALUES (:VCHARGE_ID,:VACCT_ID,:VWRITEOFF_MODE,");
        sql.append(":VSPAY_FEE,:VALL_MONEY,:VALL_NEW_MONEY,:VALL_BALANCE,:VALL_NEW_BALANCE,");
        sql.append(":VALLBOWE_FEE,:VAIMP_FEE,:VALLNEWBOWE_FEE,");
        sql.append(":VPREREAL_FEE,:VCURREAL_FEE,:VPROTOCOL_BALANCE,");
        sql.append("STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),:VEPARCHY_CODE,");
        sql.append(":VPROVINCE_CODE,:VCYCLE_ID,:VREMARK,:VRSRV_FEE1,:VRSRV_FEE2,:VRSRV_INFO1) ");
        Map<String, String> param = new HashMap<>();
        param.put("VCHARGE_ID", writeSnapLog.getChargeId());
        param.put("VACCT_ID", writeSnapLog.getAcctId());
        param.put("VWRITEOFF_MODE", String.valueOf(writeSnapLog.getWriteoffMode()));
        param.put("VSPAY_FEE", String.valueOf(writeSnapLog.getSpayFee()));
        param.put("VALL_MONEY", String.valueOf(writeSnapLog.getAllMoney()));
        param.put("VALL_NEW_MONEY", String.valueOf(writeSnapLog.getAllNewMoney()));
        param.put("VALL_BALANCE", String.valueOf(writeSnapLog.getAllBalance()));
        param.put("VALL_NEW_BALANCE", String.valueOf(writeSnapLog.getAllNewBalance()));
        param.put("VALLBOWE_FEE", String.valueOf(writeSnapLog.getAllBOweFee()));
        param.put("VAIMP_FEE", String.valueOf(writeSnapLog.getaImpFee()));
        param.put("VALLNEWBOWE_FEE", String.valueOf(writeSnapLog.getAllNewBOweFee()));
        param.put("VPREREAL_FEE", String.valueOf(writeSnapLog.getPreRealFee()));
        param.put("VCURREAL_FEE", String.valueOf(writeSnapLog.getCurRealFee()));
        param.put("VPROTOCOL_BALANCE", String.valueOf(writeSnapLog.getProtocolBalance()));
        param.put("VOPERATE_TIME", writeSnapLog.getOperateTime());
        param.put("VEPARCHY_CODE", writeSnapLog.getEparchyCode());
        param.put("VPROVINCE_CODE", writeSnapLog.getProvinceCode());
        param.put("VCYCLE_ID", String.valueOf(writeSnapLog.getCycleId()));
        param.put("VREMARK", writeSnapLog.getRemark());
        param.put("VRSRV_FEE1", String.valueOf(writeSnapLog.getRsrvFee1()));
        param.put("VRSRV_FEE2", String.valueOf(writeSnapLog.getRsrvFee2()));
        param.put("VRSRV_INFO1",writeSnapLog.getRsrvInfo1());
        return this.getJdbcTemplate(provinceCode).update(sql.toString(), param);
    }
}
