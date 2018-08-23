package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.service.CreditService;
import com.unicom.acting.pay.writeoff.service.PayDatumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缴费触发信控服务
 *
 * @author Administrators
 */
@Service
public class CreditServiceImpl implements CreditService {
    private static final Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);
    @Autowired
    private PayDatumService payDatumService;

    @Override
    public void genCreditInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        logger.info("genCreditInfo fun begin");
        //不触发信控直接返回
        if (!ifFireCreditCtrl(tradeCommInfo.getFeePayLog().getPaymentId(),
                tradeCommInfo.getMainUser().getOpenMode(),
                tradeCommInfoIn.getRemoveTag())) {
            return;
        }

        //触发信控类型
        String fireCreditCtrlType = getFireCreditCtrlType(tradeCommInfoIn.getProvinceCode(),
                tradeCommInfo.getFeeAccount().getAcctId(),
                tradeCommInfo.getWriteOffRuleInfo());
        logger.info("fireCreditCtrlType = " + fireCreditCtrlType);

        if (ActPayPubDef.JIAOFEI_TO_CREDIT.equals(fireCreditCtrlType)) {
            genJFCreditMQInfo(tradeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        } else if (ActPayPubDef.RECV_TO_CREDIT.equals(fireCreditCtrlType)) {
            genRecvCreditMQInfo(tradeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        }
    }

    /**
     * 是否触发信控
     *
     * @param paymentId
     * @param openMode
     * @param removeTag
     * @return
     */
    private boolean ifFireCreditCtrl(int paymentId, String openMode, String removeTag) {
        //特定缴费方式不触发信控
        if (101010 == paymentId || 100050 == paymentId) {
            return false;
        }

        //预开用户缴费和携号转出不触发信控
        if ("1".equals(openMode) || "9".equals(removeTag)) {
            return false;
        }
        return true;
    }

    /**
     * 触发信控类型
     *
     * @param provinceCode
     * @param acctId
     * @param writeOffRuleInfo
     * @return
     */
    private String getFireCreditCtrlType(String provinceCode, String acctId, WriteOffRuleInfo writeOffRuleInfo) {
        if (payDatumService.isSpecialRecvState(writeOffRuleInfo.getCurCycle())) {
            return ActPayPubDef.JIAOFEI_TO_CREDIT;
        } else {
            CommPara commPara = writeOffRuleInfo.getCommpara(PubCommParaDef.FIRE_CREDIT_MODE);
            if (commPara != null && "1".equals(commPara.getParaCode1())) {
                return ActPayPubDef.RECV_TO_CREDIT;
            }
            return ActPayPubDef.JIAOFEI_TO_CREDIT;
        }
    }

    /**
     * 生成抵扣期MQ信息
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private void genJFCreditMQInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();
        String provinceCode = tradeCommInfoIn.getProvinceCode();
        Staff staff = tradeCommInfo.getTradeStaff();
        String tradeStaffId = staff.getStaffId();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //是否大合帐账户
        boolean isBigAcct = payDatumService.ifBigAcctForFireCreditCtrl(tradeStaffId,
                feeAccount.getAcctId(), provinceCode, writeOffRuleInfo);

        //实时费用
        long curRealFee = tradeCommInfo.getFeeWriteSnapLog().getCurRealFee();
        //缴费流水
        String chargeId = tradeCommInfo.getFeePayLog().getChargeId();
        //默认付费用户结余
        Map<String, Long> defaultUserBalance = new HashMap<>();
        //高级付费用户
        List<String> otherPayUsers = new ArrayList<>();
        //用户结余
        Map<String, UserBalance> userBalanceMap = tradeCommInfo.getUserBalance();
        for (Map.Entry<String, UserBalance> it : userBalanceMap.entrySet()) {
            if ('1' == it.getValue().getDefaultPay()) {
                logger.info("default_user " + it.getKey());
                defaultUserBalance.put(it.getKey(), it.getValue().getBalance());
            } else {
                logger.info("other_user " + it.getKey());
                otherPayUsers.add(it.getKey());
            }
        }

        //如果是大合帐缴费且是按用户缴费，其他用户则不插入TI_O_JFToCredit_BIGACCT触发开机
        if (isBigAcct && "2".equals(tradeCommInfoIn.getWriteoffMode())) {
            otherPayUsers.clear();
        }

        //设置信控工单公共信息
        JFCreditMQInfo jfCreditMQInfo = new JFCreditMQInfo();
        jfCreditMQInfo.setAcctId(feeAccount.getAcctId());
        jfCreditMQInfo.setProvinceCode(provinceCode);
        jfCreditMQInfo.setTradeId(chargeId);
        jfCreditMQInfo.setRealFee(curRealFee);
        jfCreditMQInfo.setUpdateStaffId(tradeStaffId);
        jfCreditMQInfo.setUpdateDepartId(staff.getDepartId());
        jfCreditMQInfo.setUpdateTime(writeOffRuleInfo.getSysdate());
        jfCreditMQInfo.setWriteOffMode(tradeCommInfoIn.getWriteoffMode());
        jfCreditMQInfo.setRecoverTag(tradeCommInfoIn.getRecoverTag());
        jfCreditMQInfo.setCancelTag("0");
        jfCreditMQInfo.setRemark("销账触发");
        if (!CollectionUtils.isEmpty(tradeCommInfo.getPayUsers())
                && tradeCommInfo.getPayUsers().size() > 100) {
            jfCreditMQInfo.setTradeTypeCode(ActPayPubDef.RECVCREDIT_TRADE_TYPE_BIGACCT);
            jfCreditMQInfo.setBatchTag("1");
        } else {
            jfCreditMQInfo.setTradeTypeCode(ActPayPubDef.RECVCREDIT_TRADE_TYPE_DEFAULT);
            jfCreditMQInfo.setBatchTag("0");
        }

        if (isBigAcct) {
            jfCreditMQInfo.setBigAcctTag("1");
        } else {
            jfCreditMQInfo.setBigAcctTag("0");
        }

        //缴费信控工单用户结余信息
        List<JFUserLeaveFeeInfo> jfUserLeaveFeeInfos = new ArrayList(defaultUserBalance.size() + otherPayUsers.size());


        //用户实时结余信息
        LeaveRealFeeMQInfo leaveRealFeeMQInfo = new LeaveRealFeeMQInfo();
        leaveRealFeeMQInfo.setInTag('0');
        leaveRealFeeMQInfo.setUpdateTime(writeOffRuleInfo.getSysdate());
        leaveRealFeeMQInfo.setRealFee(curRealFee);

        //待更新实时用户结余信息
        List<UserLeaveFeeInfo> userLeaveFeeInfos = new ArrayList(defaultUserBalance.size());
        leaveRealFeeMQInfo.setUserLeaveFeeInfos(userLeaveFeeInfos);

        //更新用户实时结余
        for (Map.Entry<String, Long> itr : defaultUserBalance.entrySet()) {
            //待更新用户结余
            UserLeaveFeeInfo userLeaveFeeInfo = new UserLeaveFeeInfo();
            userLeaveFeeInfo.setUserId(itr.getKey());
            userLeaveFeeInfo.setLeaveRealFee(itr.getValue());
            userLeaveFeeInfos.add(userLeaveFeeInfo);

            //信控工单用户结余
            JFUserLeaveFeeInfo jfUserLeaveFeeInfo = new JFUserLeaveFeeInfo();
            jfUserLeaveFeeInfo.setUserId(itr.getKey());
            jfUserLeaveFeeInfo.setLeaveRealFee(itr.getValue());
            if (tradeCommInfo.getFeePayLog().getCancelTag() == '0') {
                jfUserLeaveFeeInfo.setProcessTag("0");
            } else {
                jfUserLeaveFeeInfo.setProcessTag("1");
            }
            jfUserLeaveFeeInfos.add(jfUserLeaveFeeInfo);
        }

        //非默认付费用户结余信息
        if (!CollectionUtils.isEmpty(otherPayUsers)) {
            for (String userId : otherPayUsers) {
                JFUserLeaveFeeInfo jfUserLeaveFeeInfo = new JFUserLeaveFeeInfo();
                jfUserLeaveFeeInfo.setUserId(userId);
                jfUserLeaveFeeInfo.setProcessTag("1");
                jfUserLeaveFeeInfo.setLeaveRealFee(1);
                jfUserLeaveFeeInfos.add(jfUserLeaveFeeInfo);
            }
        }

        //信控缴费接口工单信息
        tradeCommResultInfo.setJfCreditMQInfo(jfCreditMQInfo);
        //用户实时结余信息
        tradeCommResultInfo.setLeaveRealFeeMQInfo(leaveRealFeeMQInfo);
    }

    /**
     * 非抵扣期信控MQ信息
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    private void genRecvCreditMQInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();
        String provinceCode = tradeCommInfoIn.getProvinceCode();
        Staff staff = tradeCommInfo.getTradeStaff();
        String tradeStaffId = staff.getStaffId();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //是否大合帐账户
        boolean isBigAcct = payDatumService.ifBigAcctForFireCreditCtrl(tradeStaffId,
                feeAccount.getAcctId(), provinceCode, writeOffRuleInfo);

        //缴费流水
        String chargeId = tradeCommInfo.getFeePayLog().getChargeId();

        //设置信控工单公共信息
        RecvCreditMQInfo recvCreditMQInfo = new RecvCreditMQInfo();
        recvCreditMQInfo.setAcctId(feeAccount.getAcctId());
        recvCreditMQInfo.setUserId(tradeCommInfo.getMainUser().getUserId());
        recvCreditMQInfo.setProvinceCode(provinceCode);
        recvCreditMQInfo.setTradeId(chargeId);
        recvCreditMQInfo.setUpdateStaffId(tradeStaffId);
        recvCreditMQInfo.setUpdateDepartId(staff.getDepartId());
        recvCreditMQInfo.setRecvTime(writeOffRuleInfo.getSysdate());
        recvCreditMQInfo.setWriteOffMode(tradeCommInfoIn.getWriteoffMode());
        recvCreditMQInfo.setRecoveTag(tradeCommInfoIn.getRecoverTag());
        recvCreditMQInfo.setCancelTag("0");
        recvCreditMQInfo.setRemark("销账触发");
        if (!CollectionUtils.isEmpty(tradeCommInfo.getPayUsers())
                && tradeCommInfo.getPayUsers().size() > 100) {
            recvCreditMQInfo.setBatchTag("1");
        } else {
            recvCreditMQInfo.setBatchTag("0");
        }
        if (isBigAcct) {
            recvCreditMQInfo.setBigAcctTag("1");
        } else {
            recvCreditMQInfo.setBigAcctTag("0");
        }
        //信控解耦工单MQ信息
        tradeCommResultInfo.setRecvCreditMQInfo(recvCreditMQInfo);
    }
}
