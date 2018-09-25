package com.unicom.acting.pay.cancelfee.service;

import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfoOut;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeMqMessage;
import com.unicom.skyark.component.service.IBaseService;

/**
 * @author ducj
 */
public interface CancelRecvFeeTransactionService extends IBaseService {

    /**
     * 交费返销入库 - Common(前台) - acting.pay.cancelfee
     *
     * @param cancelRecvFeeInfoOut 交费返销入库出参
     * @param acctId' 账户标识
     * @param cancelRecvFeeMqMessage mq消息
     * @return 入库是否成功
     */
    boolean cancelFeeIndbCommon(CancelRecvFeeInfoOut cancelRecvFeeInfoOut, String acctId, CancelRecvFeeMqMessage cancelRecvFeeMqMessage);

}