package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.dao.SmsOrderDao;
import com.unicom.acting.pay.dao.SmsParamDao;
import com.unicom.acting.pay.writeoff.service.UserParamRelService;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值短信发送服务,通过JDBC方式访问短信参数和短信工单
 *
 * @author wangkh
 */
@Service
public class SmsServiceImpl implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    @Autowired
    private SmsParamDao smsParamDao;
    @Autowired
    private SmsOrderDao smsIODao;
    @Autowired
    private SysCommOperFeeService sysCommOperPayService;
    @Autowired
    private CommParaFeeService commParaPayService;
    @Autowired
    private UserParamRelService userParamRelService;

    @Override
    public void loadSmsParam() {
        //短信条件
        List<SmsCond> smsConds = smsParamDao.getProvSmsCond();
        //短信模板
        List<SmsTemplet> smsTemplets = smsParamDao.getSmsTemplet();
        //短信转换
        List<SmsConvert> smsConverts = smsParamDao.getSmsConvertId();

        SmsStaticParamInfo.setAllMPSmsCond(smsConds);
        SmsStaticParamInfo.setAllSmsTemplet(smsTemplets);
        SmsStaticParamInfo.setAllMMMPSmsConvert(smsConverts);
    }

    @Override
    public void genSmsInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //短信发送类型
        String msgSendType = getMsgSendType(writeOffRuleInfo);

        PayLog payLog = tradeCommResultInfo.getPayLog();
        User mainUser = tradeCommInfo.getMainUser();

        //校验是否发送短信
        if (!ifSendSmsMsg(payLog, writeOffRuleInfo, mainUser, msgSendType, tradeCommInfoIn.getInvoiceFee())) {
            return;
        }

        FeeWriteSnapLog feeWriteSnapLog = tradeCommInfo.getFeeWriteSnapLog();
        //获取短信模板标识
        long smTempletId = getSmTempletId(payLog.getPaymentId(), mainUser, feeWriteSnapLog, writeOffRuleInfo);
        if (smTempletId == -1) {
            throw new SkyArkException("没有找到对应的短信模板,tradeDefId = " + payLog.getPaymentId());
        }
        //获取短信模板对象
        SmsTemplet smsTemplet = getSmsTemplet(smTempletId);
        logger.info("content = " + smsTemplet.getSmTempletContext());
        //短信模板内容为空，不发送短信
        if (StringUtil.isEmptyCheckNullStr(smsTemplet.getSmTempletContext())) {
            return;
        }

        //用户网别  ActingPayPubDef.ACT_RDS_DBCONN
        String parentTypeCode = commParaPayService.getParentTypeCode(payLog.getNetTypeCode());

        //生成短信工单数据
        List<SmsOrder> smsOrders = genSmsOrder(payLog, writeOffRuleInfo,
                feeWriteSnapLog, smTempletId, msgSendType, parentTypeCode,
                tradeCommInfoIn.getHeaderGray());

        tradeCommResultInfo.setSmsOrders(smsOrders);

        //生成短信MQ对象信息
        if (!CollectionUtils.isEmpty(smsOrders)) {
            tradeCommResultInfo.setSmsMQInfos(genSmsMQInfo(smsOrders));
        }
    }

    @Override
    public void insertSmsIO(List<SmsOrder> smsOrders) {
        //短信格式化转换
        smsIODao.insertSmsOrder(smsOrders);
    }

    /**
     * 生成充值后短信MQ对象信息
     *
     * @param smsOrders
     * @return
     */
    private List<SmsMQInfo> genSmsMQInfo(List<SmsOrder> smsOrders) {
        List<SmsMQInfo> smsMQInfoList = new ArrayList(smsOrders.size());
        for (SmsOrder smsOrder : smsOrders) {
            //短信因子
            TempletValue templetValue = new TempletValue();
            templetValue.setSysdate(smsOrder.getSysdate());
            templetValue.setSysdate2(smsOrder.getSysdate2());
            templetValue.setRecvfee(smsOrder.getRecvfee());
            templetValue.setCurrentavlfee(smsOrder.getCurrentavlfee());
            templetValue.setAllnewbalance(smsOrder.getAllnewbalance());
            templetValue.setAllnewrowefee(smsOrder.getAllnewrowefee());
            templetValue.setAllnewmoney(smsOrder.getAllnewmoney());
            templetValue.setAllnewbowefee(smsOrder.getAllnewbowefee());
            templetValue.setAllrealfee(smsOrder.getAllrealfee());
            templetValue.setMonth(smsOrder.getMonth());
            templetValue.setSerialnumberOut(smsOrder.getSerialnumberOut());
            //短信内容
            SmsMQInfo smsMQInfo = new SmsMQInfo();
            smsMQInfo.setTempletValue(templetValue);
            smsMQInfo.setAccessCode(smsOrder.getAccessCode());
            smsMQInfo.setSendTimeCode(smsOrder.getSendTimeCode());
            smsMQInfo.setRecvObjectType(smsOrder.getRecvObjectType());
            smsMQInfo.setRecvObject(smsOrder.getRecvObject());
            smsMQInfo.setEparchyCode(smsOrder.getEparchyCode());
            smsMQInfo.setProvinceCode(smsOrder.getProvinceCode());
            smsMQInfo.setTempletId(smsOrder.getTempletId());
            smsMQInfo.setGenerateTime(smsOrder.getGenerateTime());
            try {
                smsMQInfo.setGenerateTime(TimeUtil.tranDateFormat(smsOrder.getGenerateTime(),
                        TimeUtil.DATETIME_FORMAT, TimeUtil.DATETIME_FORMAT_14));
            } catch (Exception ex) {
                throw new SkyArkException("短信发送时间格式化转换失败,time = " + smsOrder.getGenerateTime());
            }
            smsMQInfo.setReviewFlag(smsOrder.getReviewFlag());
            smsMQInfo.setRemark(smsOrder.getRemark());
            smsMQInfoList.add(smsMQInfo);
        }
        return smsMQInfoList;
    }

    /**
     * 省份短信发送类型
     *
     * @param writeOffRuleInfo
     * @return
     */
    private String getMsgSendType(WriteOffRuleInfo writeOffRuleInfo) {
        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_MSGSEND_TYPE);
        //参数没有配置或者配置值为空，不发送短信
        if (commPara == null || StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                || "0".equals(commPara.getParaCode1())) {
            return SmsStaticParamInfo.MSG_SEND_ABNORMAL;
        }

        if ("1".equals(commPara.getParaCode1())) {
            return SmsStaticParamInfo.MSG_SEND_TEMP;
        } else if ("2".equals(commPara.getParaCode1())) {
            return SmsStaticParamInfo.MSG_SEND_NORMAL;
        } else {
            //未知的发送方式，暂时不发送短信
            return SmsStaticParamInfo.MSG_SEND_ABNORMAL;
        }
    }

    /**
     * 是否发送短信校验
     *
     * @param payLog
     * @param writeOffRuleInfo
     * @param mainUser
     * @param msgSendType
     * @param invoiceFee
     * @return
     */
    private boolean ifSendSmsMsg(PayLog payLog, WriteOffRuleInfo writeOffRuleInfo, User mainUser, String msgSendType, long invoiceFee) {
        //省份配置短信类型不发送则直接返回
        if (SmsStaticParamInfo.MSG_SEND_ABNORMAL.equals(msgSendType)) {
            return false;
        }

        //特定渠道充值不发送短信
        if (!ifChannelSendMsg(payLog.getChannelId(), writeOffRuleInfo)) {
            return false;
        }

        //储值方式没有配置对应短信模板不发送短信
        if (!hasSmsCond(payLog.getPaymentId(), payLog.getProvinceCode())) {
            return false;
        }

        //不在网用户不发送短信
        if (!"0".equals(mainUser.getRemoveTag())) {
            return false;
        }

        //非缴费站，非ESS清退，并且金额小于0不发短信
        if (invoiceFee <= 0 && payLog.getRecvFee() <= 0 && 100025 != payLog.getPaymentId()
                && !(100014 == payLog.getPaymentId() && "15008".equals(payLog.getChannelId()))) {
            return false;
        }

        return true;
    }

    /**
     * 生成短信工单信息
     *
     * @param payLog
     * @param writeOffRuleInfo
     * @param feeWriteSnapLog
     * @param smTempletId
     * @param msgSendType
     * @param parentTypeCode
     * @param headerGray
     * @return
     */
    private List<SmsOrder> genSmsOrder(PayLog payLog, WriteOffRuleInfo writeOffRuleInfo, FeeWriteSnapLog feeWriteSnapLog,
                                       long smTempletId, String msgSendType, String parentTypeCode,
                                       String headerGray) {
        //生成短信工单对象
        List<SmsOrder> noticeInfoList = new ArrayList();
        SmsOrder smsOrder = new SmsOrder();
        String smsId = sysCommOperPayService.getActingSequence(ActingPayPubDef.SEQ_SMSSENDID_TABNAME,
                ActingPayPubDef.SEQ_SMSSENDID_COLUMNNAME, payLog.getProvinceCode());
        smsOrder.setSmsNoticeId(smsId);
        smsOrder.setAccessCode("*******");
        smsOrder.setSendTimeCode("03");
        smsOrder.setRecvObjectType("00");
        smsOrder.setRecvObject(payLog.getSerialNumber());
        smsOrder.setAcctId(payLog.getAcctId());
        smsOrder.setEparchyCode(payLog.getEparchyCode());
        smsOrder.setProvinceCode(payLog.getProvinceCode());
        smsOrder.setTempletId(String.valueOf(smTempletId));
        smsOrder.setRemark(payLog.getChargeId());
        if (SmsStaticParamInfo.MSG_SEND_NORMAL.equals(msgSendType)) {
            smsOrder.setReviewFlag("0");
        } else {
            smsOrder.setReviewFlag("1");
        }

        smsOrder.setGenerateTime(writeOffRuleInfo.getSysdate());

        //短信模板因子
        genSmsFactor(smsOrder, payLog, writeOffRuleInfo, feeWriteSnapLog);

        //融合用户发送短信方式
        String msgSendFRType = getFRMsgSendType(writeOffRuleInfo);
        //充值短信发送主号码的融合业务类型
        String relationType = getRelationType(writeOffRuleInfo);

        logger.info("msgSendFRType = " + msgSendFRType);

        if ("1".equals(msgSendFRType)) {
            //查询是否智慧沃家主用户
            String mainSN = userParamRelService.getWJMainSN(payLog.getUserId(), relationType, headerGray);
            //如果不是智慧沃家用户，校验是否是沃享用户
            if (StringUtil.isEmptyCheckNullStr(mainSN)) {
                //返回值为-1代表该用户是沃享主用户，短信只发送一次即可
                mainSN = userParamRelService.getWXMainSN(payLog.getUserId(), headerGray);
            }
            //新增一条主用户短信
            if (!StringUtil.isEmptyCheckNullStr(mainSN) && !"-1".equals(mainSN)) {
                SmsOrder mainSmsOrder = smsOrder.clone();
                mainSmsOrder.setSmsNoticeId(sysCommOperPayService.getActingSequence(ActingPayPubDef.SEQ_SMSSENDID_TABNAME,
                        ActingPayPubDef.SEQ_SMSSENDID_COLUMNNAME, payLog.getProvinceCode()));
                mainSmsOrder.setRecvObject(mainSN);
                noticeInfoList.add(mainSmsOrder);
            }
            if ("0".equals(parentTypeCode)) {
                //仅移网用户发送
                noticeInfoList.add(smsOrder);
            }
        } else {
            //如果是智慧沃家融合用户，只发送群主用户，否则发送充值号码
            String mainSN = userParamRelService.getWJMainSN(payLog.getUserId(), relationType, headerGray);
            //如果是智慧沃家用户，设置短信接受号码是群主号码
            if (!StringUtil.isEmptyCheckNullStr(mainSN)) {
                smsOrder.setRecvObject(mainSN);
                noticeInfoList.add(smsOrder);
            } else if ("0".equals(parentTypeCode)) {
                //仅移网用户发送
                noticeInfoList.add(smsOrder);
            }
        }
        return noticeInfoList;
    }

    /**
     * 渠道是否发送充值短信
     *
     * @param channelId
     * @param writeOffRuleInfo
     * @return
     */
    private boolean ifChannelSendMsg(String channelId, WriteOffRuleInfo writeOffRuleInfo) {
        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_SENDSMS_CHANNELID);
        if (commPara != null && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())
                && commPara.getParaCode1().contains("|" + channelId + "|")) {
            return false;
        }
        return true;
    }

    /**
     * 获取对应的短信模板
     *
     * @param templet
     * @return
     */
    private SmsTemplet getSmsTemplet(long templet) {
        logger.info("templet = " + templet);
        if (CollectionUtils.isEmpty(SmsStaticParamInfo.getAllMSmsTemplet())) {
            throw new SkyArkException("没有配置短信模板，请联系系统管理员");
        }

        if (!SmsStaticParamInfo.getAllMSmsTemplet().containsKey(templet)) {
            throw new SkyArkException("没有对应的短信模板,smTempletId = " + templet);
        }

        return SmsStaticParamInfo.getAllMSmsTemplet().get(templet);
    }

    /**
     * 储值方式是否存在对应的短信模板
     *
     * @param tradeDefId
     * @param provCode
     * @return
     */
    private boolean hasSmsCond(long tradeDefId, String provCode) {
        if (CollectionUtils.isEmpty(SmsStaticParamInfo.getAllMPSmsCond())) {
            throw new SkyArkException("没有配置储值方式对应的短信模板，请联系系统管理员");
        }

        if (SmsStaticParamInfo.getAllMPSmsCond().containsKey(provCode)) {
            if (SmsStaticParamInfo.getAllMPSmsCond().get(provCode).containsKey(tradeDefId)) {
                return true;
            }
        }

        if (SmsStaticParamInfo.getAllMPSmsCond().containsKey(ActingPayPubDef.DEFAULT_PROVINCE_CODE)) {
            if (SmsStaticParamInfo.getAllMPSmsCond().get(ActingPayPubDef.DEFAULT_PROVINCE_CODE).containsKey(tradeDefId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取储值方式对应的短信模板类型
     *
     * @param paymentId
     * @param mainUser
     * @param feeWriteSnapLog
     * @param writeOffRuleInfo
     * @return
     */
    public long getSmTempletId(long paymentId, User mainUser, FeeWriteSnapLog feeWriteSnapLog, WriteOffRuleInfo writeOffRuleInfo) {
        if (CollectionUtils.isEmpty(SmsStaticParamInfo.getAllMPSmsCond())) {
            throw new SkyArkException("没有配置储值方式对应的短信模板，请联系系统管理员");
        }

        //短信模板标识
        long smsTempletId = -1;
        if (SmsStaticParamInfo.getAllMPSmsCond().containsKey(mainUser.getProvinceCode())) {
            if (SmsStaticParamInfo.getAllMPSmsCond().get(mainUser.getProvinceCode()).containsKey(paymentId)) {
                smsTempletId = SmsStaticParamInfo.getAllMPSmsCond().get(mainUser.getProvinceCode()).get(paymentId).getSmTempletId();
            }
            logger.info("smsTempletId = " + smsTempletId);
            if (smsTempletId != -1) {
                if (10000 != smsTempletId) {
                    return smsTempletId;
                } else {
                    return getCommonSmTempletByProv(smsTempletId, mainUser, feeWriteSnapLog, writeOffRuleInfo);
                }
            }
        }

        if (SmsStaticParamInfo.getAllMPSmsCond().containsKey(ActingPayPubDef.DEFAULT_PROVINCE_CODE)) {
            if (SmsStaticParamInfo.getAllMPSmsCond().get(ActingPayPubDef.DEFAULT_PROVINCE_CODE).containsKey(paymentId)) {
                smsTempletId = SmsStaticParamInfo.getAllMPSmsCond().get(ActingPayPubDef.DEFAULT_PROVINCE_CODE).get(paymentId).getSmTempletId();
                if (smsTempletId != -1) {
                    if (10000 != smsTempletId) {
                        return smsTempletId;
                    } else {
                        return getCommonSmTempletByProv(smsTempletId, mainUser, feeWriteSnapLog, writeOffRuleInfo);
                    }
                }
            }
        }
        return smsTempletId;
    }

    /**
     * 充值公共短信模板按省份获取
     *
     * @param oriSmTempLetId
     * @param mainUser
     * @param feeWriteSnapLog
     * @param writeOffRuleInfo
     * @return
     */
    private long getCommonSmTempletByProv(long oriSmTempLetId, User mainUser, FeeWriteSnapLog feeWriteSnapLog, WriteOffRuleInfo writeOffRuleInfo) {
        //实际发送的短信模板
        long smsTempletId = -1;
        //短信发送类型
        String smsType = "0";
        //欠费判断模式
        String oweFeeType = "0";
        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_PAYFEESMS_OPTIMIZED);
        if (commPara != null) {
            smsType = commPara.getParaCode1();
            oweFeeType = commPara.getParaCode2();
        }
        logger.info("smsType = " + smsType + ",oweFeeType = " + oweFeeType);

        //增加用户归属省份编码传递
        String provinceCode = mainUser.getProvinceCode();
        //根据用户缴费前后欠费情况选择短信模板发送
        if ("1".equals(smsType)) {
            if ("1".equals(oweFeeType)) {
                //按照充值前后用户欠费情况发送短信
                smsTempletId = getSmTempletIdByOweFee(smsType, oriSmTempLetId, feeWriteSnapLog, provinceCode);
            } else if ("2".equals(oweFeeType)) {
                //按照充值前后用户结余情况发送短信
                smsTempletId = getSmTempletIdByBalance(oriSmTempLetId, feeWriteSnapLog.getAllBalance(),
                        feeWriteSnapLog.getAllNewBalance(), provinceCode);
            } else {
                throw new SkyArkException("短信新模板参数ASM_PAYFEESMS_OPTIMIZED的费用取值方式(PARA_CODE2)配置不正确，请检查！");
            }
        } else if ("2".equals(smsType)) {
            smsTempletId = getSmTempletIdByOweFee(smsType, oriSmTempLetId, feeWriteSnapLog, provinceCode);
        } else if ("3".equals(smsType)) {
            smsTempletId = getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.HASNOTOWEFEE_BEFORERECV_NEW, provinceCode);
        } else {
            //默认按照用户信用度和缴费后欠费情况选择短信模板
            smsTempletId = getSmTempletIdByUserCreditAndOweFee(mainUser.getCreditValue(), feeWriteSnapLog.getAllNewBOweFee(), oriSmTempLetId, provinceCode);
        }

        return smsTempletId;
    }

    /**
     * 根据用户信用度和欠费情况
     *
     * @param creditValue
     * @param allNewBOweFee
     * @param oriSmTempLetId
     * @param provCode
     * @return
     */
    private long getSmTempletIdByUserCreditAndOweFee(long creditValue, long allNewBOweFee, long oriSmTempLetId, String provCode) {
        if (allNewBOweFee > 0) {
            if (SmsStaticParamInfo.MIN_USER_CREDITVALUE == creditValue) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.ZEROCREDIT_OWED, provCode);
            }

            if (SmsStaticParamInfo.MIN_USER_CREDITVALUE < creditValue
                    && creditValue < SmsStaticParamInfo.MAX_USER_CREDITVALUE) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.ORDCREDIT_OWED, provCode);
            }

            if (SmsStaticParamInfo.MAX_USER_CREDITVALUE <= creditValue) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.BOUNDLESSCREDIT_OWED, provCode);
            }
        } else {
            if (SmsStaticParamInfo.MIN_USER_CREDITVALUE == creditValue) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.ZEROCREDIT_BANALCE, provCode);
            }

            if (SmsStaticParamInfo.MIN_USER_CREDITVALUE < creditValue && creditValue < SmsStaticParamInfo.MAX_USER_CREDITVALUE) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.ORDCREDIT_BALANCE, provCode);
            }

            if (SmsStaticParamInfo.MAX_USER_CREDITVALUE <= creditValue) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.BOUNDLESSCREDIT_BALANCE, provCode);
            }
        }
        return oriSmTempLetId;
    }

    /**
     * 根据用户缴费前后欠费情况
     *
     * @param smsType
     * @param oriSmTempLetId
     * @param feeWriteSnapLog
     * @param provCode
     * @return
     */
    private long getSmTempletIdByOweFee(String smsType, long oriSmTempLetId, FeeWriteSnapLog feeWriteSnapLog, String provCode) {
        //根据缴费前后欠费情况选择短信模板发送
        if ("1".equals(smsType)) {
            //缴费前无欠费
            if (feeWriteSnapLog.getAllBOweFee() <= 0) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId,
                        SmsStaticParamInfo.BALANCE_BEFORERECV, provCode);
            }
            //缴费前有欠费，缴费后有欠费
            if (0 < feeWriteSnapLog.getAllBOweFee() && 0 < feeWriteSnapLog.getAllNewBOweFee()) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId,
                        SmsStaticParamInfo.OWED_AFTERRECV, provCode);
            }
            //缴费前有欠费，缴费后无欠费
            if (0 < feeWriteSnapLog.getAllBOweFee() && feeWriteSnapLog.getAllNewBOweFee() <= 0) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId,
                        SmsStaticParamInfo.BALANCE_AFTERERECV, provCode);
            }
        } else if ("2".equals(smsType)) {
            //根据缴费前后欠费情况选择新的短信模板发送
            //缴费前无欠费
            if (feeWriteSnapLog.getAllBOweFee() <= 0) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId,
                        SmsStaticParamInfo.HASNOTOWEFEE_BEFORERECV_NEW, provCode);
            }
            //缴费前有欠费，缴费后有欠费
            if (0 < feeWriteSnapLog.getAllBOweFee() && 0 < feeWriteSnapLog.getAllNewBOweFee()) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId,
                        SmsStaticParamInfo.HASOWEFEE_AFTERRECV_NEW, provCode);
            }
            //缴费前有欠费，缴费后无欠费
            if (0 < feeWriteSnapLog.getAllBOweFee() && feeWriteSnapLog.getAllNewBOweFee() <= 0) {
                return getSmTempLetIdAfterConvert(oriSmTempLetId,
                        SmsStaticParamInfo.HASNOTOWEFEE_AFTERERECV_NEW, provCode);
            }
        }
        return oriSmTempLetId;
    }

    /**
     * 根据缴费前后结余情况
     *
     * @param oriSmTempLetId
     * @param allBalance
     * @param allNewBalance
     * @param provCode
     * @return
     */
    private long getSmTempletIdByBalance(long oriSmTempLetId, long allBalance, long allNewBalance, String provCode) {
        //缴费前无欠费
        if (0 <= allBalance) {
            return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.BALANCE_BEFORERECV, provCode);
        }
        //缴费前有欠费，缴费后有欠费
        if (allBalance < 0 && allNewBalance < 0) {
            return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.OWED_AFTERRECV, provCode);
        }
        //缴费前有欠费，缴费后无欠费
        if (allBalance < 0 && 0 <= allNewBalance) {
            return getSmTempLetIdAfterConvert(oriSmTempLetId, SmsStaticParamInfo.BALANCE_AFTERERECV, provCode);
        }
        return oriSmTempLetId;
    }

    /**
     * 短信转换模板
     *
     * @param oriSmTempLetId
     * @param smsMode
     * @param provCode
     * @return
     */
    private long getSmTempLetIdAfterConvert(long oriSmTempLetId, String smsMode, String provCode) {
        logger.info("prov_code = " + provCode + ",smsMode = " + smsMode);
        if (SmsStaticParamInfo.getAllMMMPSmsConvert().containsKey(provCode)
                && SmsStaticParamInfo.getAllMMMPSmsConvert().get(provCode).containsKey(smsMode)
                && SmsStaticParamInfo.getAllMMMPSmsConvert().get(provCode).get(smsMode).containsKey(oriSmTempLetId)) {
            return SmsStaticParamInfo.getAllMMMPSmsConvert().get(provCode).get(smsMode).get(oriSmTempLetId).getConvSmTempletId();
        }

        if (SmsStaticParamInfo.getAllMMMPSmsConvert().containsKey(ActingPayPubDef.DEFAULT_PROVINCE_CODE)
                && SmsStaticParamInfo.getAllMMMPSmsConvert().get(ActingPayPubDef.DEFAULT_PROVINCE_CODE).containsKey(smsMode)
                && SmsStaticParamInfo.getAllMMMPSmsConvert().get(ActingPayPubDef.DEFAULT_PROVINCE_CODE).get(smsMode).containsKey(oriSmTempLetId)) {
            return SmsStaticParamInfo.getAllMMMPSmsConvert().get(ActingPayPubDef.DEFAULT_PROVINCE_CODE).get(smsMode).get(oriSmTempLetId).getConvSmTempletId();
        }

        return -1;
    }

    private void genSmsFactor(SmsOrder smsOrder, PayLog payLog, WriteOffRuleInfo writeOffRuleInfo, FeeWriteSnapLog feeWriteSnapLog) {
        //短信模板
        String sysdate = writeOffRuleInfo.getSysdate();

        //系统时间
        smsOrder.setSysdate(sysdate);

        //系统时间(*日*时*分)
        smsOrder.setSysdate2(sysdate.substring(8, 10) + "日" + sysdate.substring(11, 13) + "时" + sysdate.substring(14, 16) + "分");

        //结清月份
        smsOrder.setMonth(String.valueOf(writeOffRuleInfo.getMaxAcctCycle().getCycleId() % 100));

        //交费金额
        smsOrder.setRecvfee(genStandardFee(payLog.getRecvFee()));

        //当前可用余额（扣除实时话费）
        smsOrder.setCurrentavlfee(genStandardFee(feeWriteSnapLog.getCurrentAvlFee()));

        //当前可用余额（扣除实时话费）
        smsOrder.setAllnewbalance(genStandardFee(feeWriteSnapLog.getAllNewBalance() > 0 ? feeWriteSnapLog.getAllNewBalance() : 0));

        //当前欠费(含实时话费)
        smsOrder.setAllnewrowefee(genStandardFee(feeWriteSnapLog.getAllNewBalance() < 0 ? (-feeWriteSnapLog.getAllNewBalance()) : 0));

        //账户可用余额
        smsOrder.setAllnewmoney(genStandardFee(feeWriteSnapLog.getAllNewMoney()));

        //当前欠费
        smsOrder.setAllnewbowefee(genStandardFee(feeWriteSnapLog.getAllNewBOweFee()));

        //本月已消费（当前实时话费）
        smsOrder.setAllrealfee(genStandardFee(feeWriteSnapLog.getCurRealFee()));

        StringBuilder content = new StringBuilder();
        content.append("sysdate=" + smsOrder.getSysdate())
                .append(",sysdate2=" + smsOrder.getSysdate2())
                .append(",recvfee=" + smsOrder.getRecvfee())
                .append(",currentavlfee=" + smsOrder.getCurrentavlfee())
                .append(",allnewbalance=" + smsOrder.getAllnewbalance())
                .append(",allnewrowefee=" + smsOrder.getAllnewrowefee())
                .append(",allnewmoney=" + smsOrder.getAllnewmoney())
                .append(",allnewbowefee=" + smsOrder.getAllnewbowefee())
                .append(",allrealfee=" + smsOrder.getAllrealfee())
                .append(",month=" + smsOrder.getMonth())
                .append(",serialnumberOut=" + smsOrder.getSerialnumberOut());
        smsOrder.setNoticeContent(content.toString());
    }

    //以元为单位返回费用
    private String genStandardFee(long fee) {
        if (fee % 100 == 0) {
            return "" + (fee / 100);
        } else {
            return String.format("%.2f", (double) fee / 100.00f);
        }
    }

    /**
     * 沃享和智慧沃家特定群组用户发送充值号码和主号码
     * 通过参数配置控制，如果参数不启用，智慧沃家特定群组用户发送群主号码，其他类型用户发送充值号码
     *
     * @param writeOffRuleInfo
     * @return
     */
    private String getFRMsgSendType(WriteOffRuleInfo writeOffRuleInfo) {
        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_PAYFEESMS_WXWJ);
        //参数没有配置或者配置值不为1，只发送主号码
        if (commPara == null || !"1".equals(commPara.getParaCode1())) {
            return "0";
        }

        return "1";
    }

    /**
     * 充值短信发送主号码的融合业务类型
     *
     * @param writeOffRuleInfo
     * @return
     */
    private String getRelationType(WriteOffRuleInfo writeOffRuleInfo) {
        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_RHMSGSEND_TYPE);
        //参数没有配置或者配置值不为1，只发送主号码
        if (commPara != null && "1".equals(commPara.getParaCode1())) {
            return commPara.getParaCode2();
        }
        return "'8800', '8900'";
    }

}
