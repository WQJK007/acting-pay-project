package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.pay.dao.UserOtherInfoDao;
import com.unicom.acting.pay.domain.ActPayPubDef;
import com.unicom.acting.pay.domain.UserParamInfo;
import com.unicom.acting.pay.domain.UserRelationInfo;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.JsonUtil;
import com.unicom.skyark.component.web.data.RequestEntity;
import com.unicom.skyark.component.web.data.Rsp;
import com.unicom.skyark.component.web.rest.RestClient;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.pay.writeoff.service.PayDatumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 三户资料相关信息
 *
 * @author Administrators
 */
@Service
public class PayDatumServiceImpl implements PayDatumService {
    private static final Logger logger = LoggerFactory.getLogger(PayDatumServiceImpl.class);
    @Autowired
    private UserOtherInfoDao userOtherInfoInDBDao;
    @Autowired
    private RestClient restClient;

    @Override
    public boolean isSpecialRecvState(Cycle cycle) {
        if (isDrecvPeriod(cycle) || isPatchDrecvPeriod(cycle)) {
            return true;
        }
        return false;
    }

    private boolean isDrecvPeriod(Cycle curCycle) {
        return (getMonthAcctStatus(curCycle) == 5 || getMonthAcctStatus(curCycle) < 0
                || getMonthAcctStatus(curCycle) >= 20 && getMonthAcctStatus(curCycle) < 90);
    }

    private boolean isPatchDrecvPeriod(Cycle curCycle) {
        return (getMonthAcctStatus(curCycle) == 8 || getMonthAcctStatus(curCycle) == 90);
    }

    private long getMonthAcctStatus(Cycle curCycle) {
        return Long.valueOf(curCycle.getMonthAcctStatus());
    }

    @Override
    public String getOldAcctIdOf2G3G(String userId, String provinceCode) {
        List <UserParamInfo> userParamList = getUserParams(userId, "20000060", "ZZZZ", provinceCode);
        if (CollectionUtils.isEmpty(userParamList)) {
            throw new SkyArkException("未找到迁转前省份OLD_ACCT_ID");
        } else {
            return userParamList.get(0).getParaValue();
        }
    }

    @Override
    public String getOldUserIdOf2G3G(String userId, String provinceCode) {
        List <UserParamInfo> userParamList = getUserParams(userId, "20000050", "ZZZZ", provinceCode);
        if (CollectionUtils.isEmpty(userParamList)) {
            throw new SkyArkException("未找到迁转前省份OLD_USER_ID");
        } else {
            return userParamList.get(0).getParaValue();
        }
    }

    @Override
    public int genLockAccount(String acctId, String provinceCode) {
        //调用redis做封装
        return 0;
    }

    @Override
    public long updateBadBillUserInfo(String acctId, String actTag, String provinceCode) {
        return userOtherInfoInDBDao.updateBadBillUserInfo(acctId, actTag, provinceCode);
    }

    @Override
    public boolean ifBigAcctForFireCreditCtrl(String tradeStaffId, String acctId, String provinceCode, WriteOffRuleInfo writeOffRuleInfo) {
        boolean ifBigAcct = false;
        CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.JF_BIGACCT);
        //信控大合帐用户表不存在，暂时不查询
//        if (commPara != null && "1".equals(commPara.getParaCode1())) {
//            ifBigAcct = accountPayDao.isCreditMutiAcct(acctId, provinceCode);
//            if (ifBigAcct) {
//                return true;
//            }
//        }

        if (!ifBigAcct && commPara != null && "1".equals(commPara.getParaCode2())
                && ActPayPubDef.DEFAULT_PROVINCE_CODE.equals(tradeStaffId)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean ifSmsBlackList(String userId, String provinceCode) {
        //屏蔽暂时没有黑名单用户
        return false;
    }

    @Override
    public String getWJMainSN(String userId, String relationType, String sysdate, String provinceCode) {
        if (true) {
            return "";
        }
        List<UserRelationInfo> userRelations = getUserRelation(userId, "0", relationType, "ZZZZ", provinceCode);
        if (CollectionUtils.isEmpty(userRelations)) {
            return "";
        } else {
            String mainUserId = "";
            for (UserRelationInfo userRelationInfo : userRelations) {
                //获取当前有效的融合群组关系的虚拟用户标识
                if (relationType.contains("|" + userRelationInfo.getRelationTypeCode() + "|")
                        && userRelationInfo.getStartDate().compareTo(sysdate) <= 0
                        && sysdate.compareTo(userRelationInfo.getEndDate()) <= 0) {
                    mainUserId = userRelationInfo.getUserId();
                    break;
                }
            }
            List <UserParamInfo> userParamList = getUserParams(mainUserId, "20000006", "ZZZZ", provinceCode);
            if (CollectionUtils.isEmpty(userParamList)) {
                return "";
            } else {
                return userParamList.get(0).getParaValue();
            }
        }
    }

    @Override
    public String getWXmainSNumber(String userId, String sysdate, String provinceCode) {
        /**
         * 1.查询用户标识是否存在21000007属性，如果不存在，不是沃享用户
         * 2.如果存在该属性，并且paramValue=1,该关系已经失效，不是沃享用户
         * 3.如果存在该属性paramValue不是1，查询融合关系类型，是否存在44%的关系，如果不存在，不是沃享用户,如果存在，获取所有群组用户
         * 4.根据用户群组用户循环调用用户属性服务，获取21000007属性paramValue=1的是主用户
         * 如果该用户是主用户，在第一次查询的时候直接被排除了，只有成员用户才能查询到自己的主用户
         */
        return "";
    }


    private List<UserParamInfo> getUserParams(String userId, String paramId, String eparchyCode, String provinceCode) {
        RequestEntity requestEntity = new RequestEntity();
        String[] param = new String[]{provinceCode, eparchyCode};
        requestEntity.setUriPaths(param);
        //组织请求参数
        Map<String, String> reqParam = new HashMap<>();
        reqParam.put("userId", userId);
        reqParam.put("paramId", paramId);
        requestEntity.setUriParams(reqParam);
        //用户资料查询公共微服务
        Rsp rsp = restClient.callSkyArkMicroService("accounting",
                ActPayPubDef.QRY_USER_PARAM, HttpMethod.GET, requestEntity);

        if (!SysTypes.SYS_SUCCESS_CODE.equals(rsp.getRspCode())) {
            throw new SkyArkException(rsp.getRspCode(), rsp.getRspDesc());
        } else {
            if (CollectionUtils.isEmpty(rsp.getData())) {
                return Collections.emptyList();
            }
            List<UserParamInfo> userParamList = JsonUtil.transMapsToObjs(rsp.getData(), UserParamInfo.class);
            if (CollectionUtils.isEmpty(userParamList)) {
                return Collections.emptyList();
            }
            return userParamList;
        }
    }

    private List<UserRelationInfo> getUserRelation(String id, String idType, String relationTypeCode, String eparchyCode, String provinceCode) {
        RequestEntity requestEntity = new RequestEntity();
        String[] param = new String[]{provinceCode, eparchyCode};
        requestEntity.setUriPaths(param);
        //组织请求参数
        Map<String, String> reqParam = new HashMap<>();
        if ("0".equals(idType)) {
            reqParam.put("userId", id);
        } else {
            reqParam.put("memberRoleId", id);
        }
        reqParam.put("relationTypeCode", relationTypeCode);
        reqParam.put("IsLike", "1");
        requestEntity.setUriParams(reqParam);
        //用户资料查询公共微服务
        Rsp rsp = restClient.callSkyArkMicroService("accounting",
                ActPayPubDef.QRY_USER_RELATION, HttpMethod.GET, requestEntity);

        if (!SysTypes.SYS_SUCCESS_CODE.equals(rsp.getRspCode())) {
            throw new SkyArkException(rsp.getRspCode(), rsp.getRspDesc());
        } else {
            if (CollectionUtils.isEmpty(rsp.getData())) {
                return Collections.emptyList();
            }
            List<UserRelationInfo> userMemberList = JsonUtil.transMapsToObjs(rsp.getData(), UserRelationInfo.class);
            if (CollectionUtils.isEmpty(userMemberList)) {
                return Collections.emptyList();
            }
            return userMemberList;
        }
    }

}
