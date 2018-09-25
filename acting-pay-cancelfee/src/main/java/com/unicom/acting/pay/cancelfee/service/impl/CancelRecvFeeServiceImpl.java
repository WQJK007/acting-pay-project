package com.unicom.acting.pay.cancelfee.service.impl;


import com.unicom.acting.pay.cancelfee.service.CancelRecvFeeService;
import com.unicom.acting.pay.domain.PayLogDmn;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfo;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfoIn;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfoOut;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeMqMessage;
import com.unicom.acting.pay.writeoff.service.CancelTradePayService;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.transaction.annotation.SkyArkTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ducj
 */
@Service
public class CancelRecvFeeServiceImpl implements CancelRecvFeeService {
    private Logger logger = LoggerFactory.getLogger(CancelRecvFeeServiceImpl.class);
    @Autowired
    private CancelTradePayService cancelTradePayService;

    @Override
    public CancelRecvFeeInfoOut cancelFeeCommon(CancelRecvFeeInfoIn cancelRecvFeeInfoIn) {

        //返销前业务校验
        CancelRecvFeeInfo cancelRecvFeeInfo = cancelTradePayService.buildCancelTradeInfoCommon(cancelRecvFeeInfoIn);

        //调用返销服务返销交费
        CancelRecvFeeInfoOut cancelRecvFeeInfoOut = cancelTradePayService.cancelFee(cancelRecvFeeInfo);

        //返回待入库和处理的数据
        return cancelRecvFeeInfoOut;
    }


    @Override
    @SkyArkTransactional(DbTypes.ACTING_DRDS)
    public boolean paylogDmnListIndb(List<PayLogDmn> payLogDmnList) {
        return cancelTradePayService.insertPaylogDmnList(payLogDmnList);
    }


    @Override
    public CancelRecvFeeMqMessage genCancelFeeMqMessage(CancelRecvFeeInfoOut cancelRecvFeeInfoOut) {
        return cancelTradePayService.genCancelFeeMqMessageCommon(cancelRecvFeeInfoOut);
    }


}