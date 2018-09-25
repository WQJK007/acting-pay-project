package com.unicom.acting.pay.oeprecvfee.processor;

import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.common.exception.ActBSysTypes;
import com.unicom.acting.pay.oeprecvfee.domain.PayLogChk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("payLogChkProcessor")
@StepScope
public class PayLogChkProcessor implements ItemProcessor<PayLogChk, PayLogChk> {
    private static final Logger logger = LoggerFactory.getLogger(PayLogChkProcessor.class);

    public PayLogChk process(PayLogChk item) {

        logger.info("PayLogChkProcessor:item:{},{}",item.getAcctId(),item.getActTag());
        if(item.getAcctId().equals("1115052358953838"))
        {
            throw new ActBException(ActBSysTypes.ERR_BUSI_PROCESSOR,"跳过执行错误！");
        }

        return  item;
    }

}
