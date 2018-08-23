package com.unicom.acting.pay.dao;

import com.unicom.skyark.component.dao.IBaseDao;

/**
 * TF_B_DERATELATEFEELOG表相关的数据操作
 *
 * @author Wangkh
 */
public interface DerateLateFeeLogDao extends IBaseDao {

    /**
     * 更新滞纳金使用信息
     *
     * @param derateId
     * @param operateId
     * @param usedDerateFee
     * @param oldUseTag
     * @param newUseTag
     * @param provinceCode
     * @return
     */
    int updDerateLateFeeLogByDerateId(String derateId, String operateId,
                                      long usedDerateFee, char oldUseTag,
                                      String newUseTag, String provinceCode);

}
