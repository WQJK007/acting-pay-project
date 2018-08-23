package com.unicom.acting.pay.writeoff.service;


import com.unicom.acting.fee.domain.FeeDerateLateFeeLog;
import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;

import java.util.List;
import java.util.Set;

/**
 * 滞纳金减免日志资源操作
 *
 * @author Wangkh
 */
public interface DerateLateFeeLogService extends IBaseService {
    /**
     * 更新滞纳金减免工单状态
     *
     * @param derateId
     * @param operateId
     * @param usedDerateFee
     * @param oldUseTag
     * @param newUseTag
     * @param provinceCode
     * @return
     */
    int updDerateLateFeeLogByDerateId(String derateId, String operateId, long usedDerateFee,
                                      char oldUseTag, String newUseTag, String provinceCode);


    /**
     * 更新滞纳金减免工单状态
     *
     * @param derateLateFeeLogs
     * @param writeOffRuleInfo
     * @param writeOffCycle
     * @param chargeId
     * @param provinceCode
     */
    void updDerateLateFeeLog(List<FeeDerateLateFeeLog> derateLateFeeLogs,
                             WriteOffRuleInfo writeOffRuleInfo, Set<Integer> writeOffCycle,
                             String chargeId, String provinceCode);


    /**
     * 生成待入库滞纳金减免对象信息
     *
     * @param derateLateFeeLogs
     * @param writeOffRuleInfo
     */
    void genDerateLateFeeLogInfo(List<FeeDerateLateFeeLog> derateLateFeeLogs, WriteOffRuleInfo writeOffRuleInfo);


}
