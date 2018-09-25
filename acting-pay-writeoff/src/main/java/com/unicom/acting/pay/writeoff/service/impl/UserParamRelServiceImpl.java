package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.writeoff.domain.UserParamReqDetailInfo;
import com.unicom.acting.fee.writeoff.domain.UserParamRsp;
import com.unicom.acting.fee.writeoff.domain.UserRelationReqDetailInfo;
import com.unicom.acting.fee.writeoff.domain.UserRelationRsp;
import com.unicom.acting.fee.writeoff.service.DatumParamRelService;
import com.unicom.acting.pay.domain.ActingPayPubDef;
import com.unicom.acting.pay.writeoff.service.UserParamRelService;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@ConditionalOnProperty(name = "batchMode", havingValue = "false", matchIfMissing = true)
public class UserParamRelServiceImpl implements UserParamRelService {
    @Autowired
    private DatumParamRelService datumParamRelService;

    @Override
    public String getWJMainSN(String userId, String relationType, String headerGray) {
        if (StringUtil.isEmptyCheckNullStr(relationType)) {
            return null;
        }
        //解析智慧沃家产品
        String[] relationTypes = relationType.split(",");
        List<UserRelationReqDetailInfo> relationTypecodes = new ArrayList();
        for (String rel : relationTypes) {
            UserRelationReqDetailInfo userRelationReqDetailInfo = new UserRelationReqDetailInfo();
            userRelationReqDetailInfo.setRealtionTypeCode(rel);
            relationTypecodes.add(userRelationReqDetailInfo);
        }
        //调用群组关系查询微服务
        List<UserRelationRsp> userRelationRsps = datumParamRelService.getUserRelation(userId,
                null, false, relationTypecodes, headerGray);
        if (CollectionUtils.isEmpty(userRelationRsps)) {
            return null;
        }

        //虚拟用户标识
        String mainUserId = userRelationRsps.get(0).getUserId();
        UserParamReqDetailInfo userParamReqDetailInfo = new UserParamReqDetailInfo();
        userParamReqDetailInfo.setUserId(mainUserId);
        userParamReqDetailInfo.setParamId(ActingPayPubDef.WJ_MAINUSER_PARAMID);
        //查询智慧沃家主用户号码
        List<UserParamRsp> userParamRsps = datumParamRelService.getUserParam(
                Collections.singletonList(userParamReqDetailInfo), headerGray);
        if (CollectionUtils.isEmpty(userParamRsps)) {
            return null;
        }
        return userParamRsps.get(0).getParamValue();
    }

    @Override
    public String getWXMainSN(String userId, String headerGray) {
        //本号码如果是沃享主用户返回-1
        UserParamReqDetailInfo userParamReqDetailInfo = new UserParamReqDetailInfo();
        userParamReqDetailInfo.setUserId(userId);
        userParamReqDetailInfo.setParamId(ActingPayPubDef.WJ_MAINUSER_PARAMID);
        List<UserParamRsp> mainUserInfos = datumParamRelService.getUserParam(
                Collections.singletonList(userParamReqDetailInfo), headerGray);
        if (!CollectionUtils.isEmpty(mainUserInfos)
                && "1".equals(mainUserInfos.get(0).getParamValue())) {
            return "-1";
        }


        //查询本号码是否沃享成员用户
        List<UserRelationReqDetailInfo> relationInfos = new ArrayList(1);
        UserRelationReqDetailInfo relationInfo = new UserRelationReqDetailInfo();
        relationInfo.setRealtionTypeCode("44");
        relationInfos.add(relationInfo);
        List<UserRelationRsp> memberRelRsps = datumParamRelService.getUserRelation(
                null, userId, true, relationInfos, headerGray);
        if (CollectionUtils.isEmpty(memberRelRsps)) {
            return null;
        }

        //暂不考虑一个沃享成员归属多个沃享群组的情况，根据虚拟用户标识查询成员用户
        List<UserRelationRsp> userRelationRsps = datumParamRelService.getUserRelation(
                memberRelRsps.get(0).getUserId(), null, true, relationInfos, headerGray);
        if (CollectionUtils.isEmpty(userRelationRsps)) {
            return null;
        }

        //查询每个成员的用户属性
        List<UserParamReqDetailInfo> detailInfos = new ArrayList(userRelationRsps.size());
        for (UserRelationRsp rel : userRelationRsps) {
            UserParamReqDetailInfo detailInfo = new UserParamReqDetailInfo();
            detailInfo.setUserId(rel.getMemberRoleId());
            detailInfo.setParamId(ActingPayPubDef.WX_MAINUSER_PARAMID);
            detailInfos.add(detailInfo);
        }
        List<UserParamRsp> userParamRsps = datumParamRelService.getUserParam(detailInfos, headerGray);
        if (!CollectionUtils.isEmpty(userParamRsps)) {
            return null;
        }

        //获取主用户的号码
        for (UserParamRsp userParamRsp : userParamRsps) {
            if ("1".equals(userParamRsp.getParamValue())) {
                for (UserRelationRsp userRelationRsp : userRelationRsps) {
                    if (userParamRsp.getUserId().equals(userRelationRsp.getMemberRoleId())) {
                        return userRelationRsp.getMemberRoleNumber();
                    }
                }
            }
        }
        return null;
    }

}
