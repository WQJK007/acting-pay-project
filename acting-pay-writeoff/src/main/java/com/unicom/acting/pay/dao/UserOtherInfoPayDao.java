package com.unicom.acting.pay.dao;

import com.unicom.skyark.component.dao.IBaseDao;

/**
 * 用户资料先关数据表操作，主要包括以下几张表：
 * TF_F_BADBILL_USERINFO 坏账用户资料表
 *
 * @author Wangkh
 */
public interface UserOtherInfoPayDao extends IBaseDao {
    /**
     * 更新坏账账户资料记录
     *
     * @param acctId
     * @param actTag
     * @param provinceCode
     * @return
     */
    long updateBadBillUserInfo(String acctId, String actTag, String provinceCode);
}
