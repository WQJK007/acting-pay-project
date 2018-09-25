package com.unicom.acting.pay.bwriteoff.business;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import org.springframework.stereotype.Component;


@Component("bPayFeeSmsMgr")
public class BPayFeeSmsMgr {


    public void createSmsInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
//        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
//        //短信发送类型
//        String msgSendType = getMsgSendType(writeOffRuleInfo);
//
//        PayLog payLog = tradeCommResultInfo.getPayLog();
//        User mainUser = tradeCommInfo.getMainUser();
//
//        //校验是否发送短信
//        if (!ifSendSmsMsg(payLog, writeOffRuleInfo, mainUser, msgSendType, tradeCommInfoIn.getInvoiceFee())) {
//            return;
//        }
//
//        FeeWriteSnapLog feeWriteSnapLog = tradeCommInfo.getFeeWriteSnapLog();
//        //获取短信模板标识
//        long smTempletId = getSmTempletId(payLog.getPaymentId(), mainUser, feeWriteSnapLog, writeOffRuleInfo);
//        if (smTempletId == -1) {
//            throw new SkyArkException("没有找到对应的短信模板,tradeDefId = " + payLog.getPaymentId());
//        }
//        //获取短信模板对象
//        SmsTemplet smsTemplet = getSmsTemplet(smTempletId);
//        logger.info("content = " + smsTemplet.getSmTempletContext());
//        //短信模板内容为空，不发送短信
//        if (StringUtil.isEmptyCheckNullStr(smsTemplet.getSmTempletContext())) {
//            return;
//        }
//
//        //用户网别  ActingPayPubDef.ACT_RDS_DBCONN
//        String parentTypeCode = commParaPayService.getParentTypeCode(payLog.getNetTypeCode(), DbTypes.ACT_PARA_RDS);
//
//        //生成短信工单数据
//        List<SmsOrder> smsOrders = genSmsOrder(payLog, writeOffRuleInfo,
//                feeWriteSnapLog, smTempletId, msgSendType, parentTypeCode);
//
//        tradeCommResultInfo.setSmsOrders(smsOrders);
//
//        //生成短信MQ对象信息
//        if (!CollectionUtils.isEmpty(smsOrders)) {
//            tradeCommResultInfo.setSmsMQInfos(genSmsMQInfo(smsOrders));
//        }
    }
}
