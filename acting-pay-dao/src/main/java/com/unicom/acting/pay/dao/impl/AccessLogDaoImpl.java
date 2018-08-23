package com.unicom.acting.pay.dao.impl;

import com.unicom.acting.pay.dao.AccessLogDao;
import com.unicom.acting.pay.domain.AccessLog;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccessLogDaoImpl extends JdbcBaseDao implements AccessLogDao {
    @Override
    public void insertAccessLog(List<AccessLog> accessLogs, String provinceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TF_B_ACCESSLOG(ACCESS_ID,CHARGE_ID,ACCT_ID,");
        sql.append("ACCT_BALANCE_ID,DEPOSIT_CODE,OLD_BALANCE,MONEY,NEW_BALANCE,");
        sql.append("ACCESS_TAG,OPERATE_TIME,PROVINCE_CODE,EPARCHY_CODE,CANCEL_TAG,");
        sql.append("INVOICE_FEE) VALUES(:VACCESS_ID,:VCHARGE_ID,");
        sql.append(":VACCT_ID,:VACCT_BALANCE_ID,:VDEPOSIT_CODE,");
        sql.append(" :VOLD_BALANCE,:VMONEY,:VNEW_BALANCE,");
        sql.append(":VACCESS_TAG,STR_TO_DATE(:VOPERATE_TIME,'%Y-%m-%d %T'),");
        sql.append(":VPROVINCE_CODE,:VEPARCHY_CODE,:VCANCEL_TAG,:VINVOICE_FEE)");
        List params = new ArrayList(accessLogs.size());
        for(AccessLog pAccessLog : accessLogs) {
            Map<String, String> param = new HashMap<>();
            param.put("VACCESS_ID", pAccessLog.getAccessId ());
            param.put("VCHARGE_ID", pAccessLog.getChargeId());
            param.put("VACCT_ID", pAccessLog.getAcctId ());
            param.put("VACCT_BALANCE_ID", pAccessLog.getAcctBalanceId ());
            param.put("VDEPOSIT_CODE", String.valueOf(pAccessLog.getDepositCode()));
            param.put("VACCESS_TAG", String.valueOf(pAccessLog.getAccessTag()));
            param.put("VOLD_BALANCE", String.valueOf(pAccessLog.getOldBalance()));
            param.put("VMONEY", String.valueOf(pAccessLog.getMoney()));
            param.put("VNEW_BALANCE", String.valueOf(pAccessLog.getNewBalance()));
            param.put("VOPERATE_TIME", pAccessLog.getOperateTime());
            param.put("VEPARCHY_CODE", pAccessLog.getEparchyCode());
            param.put("VPROVINCE_CODE", pAccessLog.getProvinceCode());
            param.put("VCANCEL_TAG", String.valueOf(pAccessLog.getCancelTag()));
            param.put("VINVOICE_FEE", String.valueOf(pAccessLog.getInvoiceFee()));
            params.add(param);
        }
        this.getJdbcTemplate(provinceCode).batchUpdate(sql.toString(),
                (Map<String, String>[]) params.toArray(new Map[params.size()]));
    }
}
