package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.domain.FeeDerateLateFeeLog;
import com.unicom.acting.pay.dao.DerateLateFeeLogDao;
import com.unicom.acting.pay.domain.ActPayCommparaDef;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.acting.fee.domain.CommPara;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;
import com.unicom.acting.pay.writeoff.service.DerateLateFeeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * 滞纳金减免日志资源通过JDBC方式操作
 */
@Service
public class DerateLateFeeLogServiceImpl implements DerateLateFeeLogService {
    @Autowired
    private DerateLateFeeLogDao derateLateFeeLogPayDao;

    @Override
    public int updDerateLateFeeLogByDerateId(String derateId, String operateId, long usedDerateFee, char oldUseTag, String newUseTag, String provinceCode) {
        return derateLateFeeLogPayDao.updDerateLateFeeLogByDerateId(derateId, operateId, usedDerateFee, oldUseTag, newUseTag, provinceCode);
    }

    @Override
    public void updDerateLateFeeLog(List<FeeDerateLateFeeLog> derateLateFeeLogs, WriteOffRuleInfo writeOffRuleInfo, Set<Integer> writeOffCycle, String chargeId, String provinceCode) {
        if (CollectionUtils.isEmpty(derateLateFeeLogs)) {
            return;
        }
        CommPara commPara = writeOffRuleInfo.getCommpara(ActPayCommparaDef.ASM_LATEUSE_PERSIST);
        if (commPara == null) {
            throw new SkyArkException("ASM_LATEUSE_PERSIST参数没有配置!");
        }
        for (FeeDerateLateFeeLog derateLateFeeLog : derateLateFeeLogs) {
            if ('1' == derateLateFeeLog.getUseTag()) {
                String newUseTag = "1";

                //是按金额才能持续使用
                if (0 == derateLateFeeLog.getDerateRuleId() && "1".equals(commPara.getParaCode1())) {
                    if (derateLateFeeLog.getUsedDerateFee() < derateLateFeeLog.getDerateFee()) {
                        newUseTag = "2";
                    }
                }
                //本账期发生销账，滞纳金减免日志才更新标志
                if (writeOffCycle.contains(derateLateFeeLog.getCycleId())) {
                    if (updDerateLateFeeLogByDerateId(derateLateFeeLog.getDerateId(),
                            chargeId, derateLateFeeLog.getUsedDerateFee(),
                            derateLateFeeLog.getOldUseTag(), newUseTag, provinceCode) == 0) {
                        throw new SkyArkException("更新违约金减免记录失败!derateId="+derateLateFeeLog.getDerateId());
                    }
                }
            }
        }
    }

    @Override
    public void genDerateLateFeeLogInfo(List<FeeDerateLateFeeLog> derateLateFeeLogs, WriteOffRuleInfo writeOffRuleInfo) {
        if (CollectionUtils.isEmpty(derateLateFeeLogs)) {
            return;
        }
        CommPara commPara = writeOffRuleInfo.getCommpara(ActPayCommparaDef.ASM_LATEUSE_PERSIST);
        if (commPara == null) {
            throw new SkyArkException("ASM_LATEUSE_PERSIST参数没有配置!");
        }
        for (FeeDerateLateFeeLog derateLateFeeLog : derateLateFeeLogs) {
            if ('1' == derateLateFeeLog.getUseTag()) {
                derateLateFeeLog.setUseTag('1');

                //是按金额才能持续使用
                if (0 == derateLateFeeLog.getDerateRuleId() && "1".equals(commPara.getParaCode1())) {
                    if (derateLateFeeLog.getUsedDerateFee() < derateLateFeeLog.getDerateFee()) {
                        derateLateFeeLog.setUseTag('2');
                    }
                }
            }
        }
    }
}
