package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.writeoff.domain.UserParamRsp;
import com.unicom.acting.fee.writeoff.domain.UserRelationRsp;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 用户融合关系信息查询
 *
 * @author Wangkh
 */
public interface UserParamRelService extends IBaseService {
    /**
     * 智慧沃家主用户
     *
     * @param userId
     * @param relationType
     * @param headerGray
     * @return
     */
    String getWJMainSN(String userId, String relationType, String headerGray);

    /**
     * 沃享主用户
     *
     * @param userId
     * @param headerGray
     * @return
     */
    String getWXMainSN(String userId, String headerGray);

}
