package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.ChargeRelation;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * @author ducj
 */
public interface ChargeRelationDao extends IBaseDao {

    /**
     * 根据chargeId 和 acctId获取交费关系数据
     * @param chargeId
     * @param acctId 账户标识
     * @return
     */
    List<ChargeRelation> getChargeRelationByChargeId(String chargeId, String acctId);

    /**
     * insert tf_b_chargerelation表
     * @param chargeRelation
     * @return
     */
    int insertChargeRelation(ChargeRelation chargeRelation);
}
