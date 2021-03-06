package com.unicom.acting.pay.writeoff.service;

import com.unicom.skyark.component.service.IBaseService;
import com.unicom.acting.fee.domain.Cycle;
import com.unicom.acting.fee.domain.WriteOffRuleInfo;

/**
 * 三户资料相关资料访问
 *
 * @author Administrators
 */
public interface PayDatumService extends IBaseService {
    /**
     * 省份系统账户标识
     *
     * @param userId
     * @param provinceCode
     * @return
     */
    String getOldAcctIdOf2G3G(String userId, String provinceCode);

    /**
     * 省份系统用户标识
     *
     * @param userId
     * @param provinceCode
     * @return
     */
    String getOldUserIdOf2G3G(String userId, String provinceCode);

    /**
     * 获取账户表行锁
     *
     * @param acctId
     * @param provinceCode
     * @return
     */
    int genLockAccount(String acctId, String provinceCode);

    /**
     * 更新坏账用户资料
     *
     * @param acctId
     * @param actTag
     * @return
     */
    long updateBadBillUserInfo(String acctId, String actTag);

    /**
     * 是否信控大合帐用户
     *
     * @param tradeStaffId
     * @param acctId
     * @param provinceCode
     * @param writeOffRuleInfo
     * @return
     */
    boolean ifBigAcctForFireCreditCtrl(String tradeStaffId, String acctId, String provinceCode,
                                       WriteOffRuleInfo writeOffRuleInfo);

    /**
     * 智慧沃家共享和组合版群主用户
     *
     * @param userId
     * @param relationType
     * @param provinceCode
     * @return
     */
    String getWJMainSN(String userId, String relationType, String sysdate, String provinceCode);

    /**
     * 沃享主用户记录查询
     *
     * @param userId
     * @param provinceCode
     * @return
     */
    String getWXmainSNumber(String userId, String sysdate, String provinceCode);
}
