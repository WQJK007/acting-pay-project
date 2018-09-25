package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.writeoff.domain.UserParamReqDetailInfo;
import com.unicom.acting.fee.writeoff.domain.UserParamRsp;
import com.unicom.acting.fee.writeoff.service.DatumParamRelService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.domain.ActingPayPubDef;
import com.unicom.acting.pay.domain.CLPayLog;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.acting.pay.domain.WriteOffLog;
import com.unicom.acting.pay.writeoff.domain.UserOldInfo;
import com.unicom.acting.pay.writeoff.service.CLPayLogService;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "batchMode", havingValue = "false", matchIfMissing = true)
public class CLPayLogServiceImpl implements CLPayLogService {
    @Autowired
    private DatumParamRelService datumParamRelService;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;

    @Override
    public List<CLPayLog> genCLPaylog(List<WriteOffLog> writeOffLogs, PayLog payLog, String headerGray) {
        //没有往月销账日志，不生成代收费日志
        if (CollectionUtils.isEmpty(writeOffLogs)) {
            return null;
        }

        //生成代收费日志
        List<CLPayLog> clPayLogs = new ArrayList();
        //23转4用户省份日志信息
        List<UserOldInfo> userOldInfos = new ArrayList();

        for (WriteOffLog writeOffLog : writeOffLogs) {
            if (writeOffLog.getCanPaytag() != '4' || writeOffLog.getWriteoffFee() == 0) {
                continue;
            }
            int k = 0;
            for (; k < clPayLogs.size(); k++) {
                if (clPayLogs.get(k).getUserId().equals(writeOffLog.getUserId())) {
                    break;
                }
            }

            if (k == clPayLogs.size()) {
                String clPaylogId = sysCommOperFeeService.getActingSequence(ActingPayPubDef.SEQ_CLCHARGEID_TANNAME,
                        ActingPayPubDef.SEQ_CLCHARGEID_COLUMNNAME, payLog.getProvinceCode());
                CLPayLog clPayLog = new CLPayLog();
                clPayLog.setClPaylogId(clPaylogId);
                clPayLog.setProvinceCode(writeOffLog.getProvinceCode());
                clPayLog.setEparchyCode(writeOffLog.getEparchyCode());
                clPayLog.setAreaCode(writeOffLog.getAreaCode());
                clPayLog.setNetTypeCode(writeOffLog.getNetTypeCode());
                clPayLog.setAcctId(writeOffLog.getAcctId());
                clPayLog.setUserId(writeOffLog.getUserId());
                UserOldInfo userOldInfo = new UserOldInfo();
                userOldInfo.setUserId(writeOffLog.getUserId());
                userOldInfos.add(userOldInfo);
                clPayLog.setSerialNumber(writeOffLog.getSerialNumber());
                clPayLog.setPaymentId(payLog.getPaymentId());
                clPayLog.setRecvFee(writeOffLog.getWriteoffFee());
                clPayLog.setChargeId(payLog.getChargeId());
                clPayLog.setOuterTradeId(payLog.getOuterTradeId());
                clPayLog.setRecvTime(payLog.getRecvTime());
                clPayLog.setRecvStaffId(payLog.getRecvStaffId());
                clPayLog.setRecvDepartId(payLog.getRecvDepartId());
                clPayLog.setEparchyCode(payLog.getRecvEparchyCode());
                clPayLog.setRecvCityCode(payLog.getRecvCityCode());
                clPayLogs.add(clPayLog);
            } else {
                clPayLogs.get(k).setRecvFee(clPayLogs.get(k).getRecvFee() + writeOffLog.getWriteoffFee());
            }
        }

        //获取23转4用户的省份用户信息
        genUserOldInfo(userOldInfos, headerGray);
        //设置23转4用户的省份用户信息，genUserOldInfo已经做了校验，可以不用再校验每个用户的信息
        for (CLPayLog clPayLog : clPayLogs) {
            for (UserOldInfo userOldInfo : userOldInfos) {
                if (clPayLog.getUserId().equals(userOldInfo.getUserId())) {
                    clPayLog.setOldUserId(userOldInfo.getOldUserId());
                    clPayLog.setOldAcctId(userOldInfo.getOldAcctId());
                    break;
                }
            }
        }

        return clPayLogs;
    }

    /**
     * 获取用户在省份的用户和账户信息
     *
     * @param userOldInfos
     * @param headerGray
     */
    private void genUserOldInfo(List<UserOldInfo> userOldInfos, String headerGray) {
        if (CollectionUtils.isEmpty(userOldInfos)) {
            return;
        }

        //获取用户省份用户
        List<UserParamReqDetailInfo> detailInfos = new ArrayList(userOldInfos.size());
        for (UserOldInfo userOldInfo : userOldInfos) {
            UserParamReqDetailInfo detailInfo = new UserParamReqDetailInfo();
            detailInfo.setUserId(userOldInfo.getUserId());
            detailInfo.setParamId(ActingPayPubDef.OLD_USERID_PARAMID);
        }
        List<UserParamRsp> oldUserIds = datumParamRelService.getUserParam(detailInfos, headerGray);
        if (CollectionUtils.isEmpty(oldUserIds) || oldUserIds.size() != userOldInfos.size()) {
            throw new SkyArkException("未找到迁转前省份OLD_USER_ID!");
        }

        //获取用户的省份账户
        for (UserParamReqDetailInfo detailInfo : detailInfos) {
            detailInfo.setParamId(ActingPayPubDef.OLD_ACCTID_PARAMID);
        }
        List<UserParamRsp> oldAcctIds = datumParamRelService.getUserParam(detailInfos, headerGray);
        if (CollectionUtils.isEmpty(oldAcctIds) || oldAcctIds.size() != userOldInfos.size()) {
            throw new SkyArkException("未找到迁转前省份OLD_ACCT_ID!");
        }

        //设置用户的历史用户和账户信息
        for (UserOldInfo userOldInfo : userOldInfos) {
            int i = 0;
            for (; i < oldAcctIds.size(); i++) {
                if (userOldInfo.getUserId().equals(oldAcctIds.get(i).getUserId())
                        && !StringUtil.isEmptyCheckNullStr((oldAcctIds.get(i).getParamValue()))) {
                    userOldInfo.setOldAcctId(oldAcctIds.get(i).getParamValue());
                    break;
                }
            }

            if (i == oldAcctIds.size()) {
                throw new SkyArkException("未找到迁转前省份OLD_ACCT_ID!userId = " + userOldInfo.getUserId());
            }

            int j = 0;
            for (; j < oldUserIds.size(); j++) {
                if (userOldInfo.getUserId().equals(oldUserIds.get(j).getUserId())
                        && !StringUtil.isEmptyCheckNullStr(oldUserIds.get(j).getParamValue())) {
                    userOldInfo.setOldUserId(oldUserIds.get(j).getParamValue());
                    break;
                }
            }

            if (j == oldUserIds.size()) {
                throw new SkyArkException("未找到迁转前省份OLD_USER_ID!userId = " + userOldInfo.getUserId());
            }
        }
    }
}
