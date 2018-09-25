package com.unicom.acting.pay.cancelfee.service.impl;

import com.unicom.acting.pay.cancelfee.service.CancelRecvFeeTransactionService;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfoOut;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeMqMessage;
import com.unicom.acting.pay.writeoff.service.CancelTradePayService;
import com.unicom.skyark.component.jdbc.transaction.annotation.SkyArkTransactional;
import com.unicom.skyark.component.mq.annotation.MqTransactionMsgSender;
import com.unicom.skyark.component.mq.annotation.MsgBody;
import com.unicom.skyark.component.mq.annotation.MsgKey;
import com.unicom.skyark.component.mq.domain.MqMessage;
import com.unicom.skyark.component.mq.producer.mqtransaction.MqTransactionHandler;
import com.unicom.skyark.component.mq.producer.mqtransaction.MqTransactionStatus;
import com.unicom.skyark.component.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CancelRecvFeeTransactionServiceImpl implements CancelRecvFeeTransactionService, MqTransactionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CancelRecvFeeTransactionServiceImpl.class);

    @Autowired
    private CancelTradePayService cancelTradePayService;

    @Override
    @SkyArkTransactional
    @MqTransactionMsgSender("topic")
    public boolean cancelFeeIndbCommon(CancelRecvFeeInfoOut cancelRecvFeeInfoOut,
                                       @MsgKey String acctId,
                                       @MsgBody CancelRecvFeeMqMessage cancelRecvFeeMqMessage) {
        return cancelTradePayService.cancelRecvFeeLogIndbCore(cancelRecvFeeInfoOut);
    }


    @Override
    public MqTransactionStatus check(MqMessage msg) {
        CancelRecvFeeMqMessage cancelRecvFeeMqMessage = JsonUtil.transJsonbytesToObj(msg.getBody(), CancelRecvFeeMqMessage.class);
        logger.info(cancelRecvFeeMqMessage.toString());
        boolean isCommit = cancelTradePayService.checkbusinessService(cancelRecvFeeMqMessage);
        if (isCommit) {
            logger.info("check msgId:" + msg.getMsgId()+"，本地事务已提交");
            return MqTransactionStatus.CommitTransaction;
        }
        logger.info("check msgId:" + msg.getMsgId()+"，本地事务未提交，mq半消息回滚");
        return MqTransactionStatus.RollbackTransaction;
    }

    @Override
    public boolean exceptionHandle(MqMessage message, Throwable e) {
        logger.error("msgId:" + message.getMsgId() + "，发送异常" + e.getMessage());
        return true;
    }
}
