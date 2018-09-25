package com.unicom.acting.pay.oeprecvfee.listener;

import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.pay.oeprecvfee.domain.PayLogChk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("payLogChkWriterListener")
public class PayLogChkWriterListener implements ItemWriteListener<PayLogChk> {
    private static final Logger logger = LoggerFactory.getLogger(PayLogChkWriterListener.class);

    @Override
    public void beforeWrite(List<? extends PayLogChk> items) {
        logger.info("beforeWrite");
    }

    @Override
    public void afterWrite(List<? extends PayLogChk> items) {
        logger.info("afterWrite");
        for(PayLogChk item:items)
        {
            logger.info("afterWrite getAcctId = {}",item.getAcctId());
        }
    }

    @Override
    public void onWriteError(Exception exception, List<? extends PayLogChk> items) {
        logger.info("onWriteError");
        for(PayLogChk item:items)
        {
            logger.info("onWriteError getAcctId = {}",item.getAcctId());
            if(exception instanceof ActBException)
            {
                logger.info("onWriteError getAcctId = {}",item.getAcctId());
                logger.info("if错误码：{},错误信息：{}",((ActBException) exception).getErrorCode(),((ActBException) exception).getErrorMsg());
            }
            else
            {
                logger.info("onWriteError getAcctId = {}",item.getAcctId());

                logger.info("else错误码：{},错误信息：{}",((ActBException) exception).getErrorCode(),((ActBException) exception).getErrorMsg());
            }
        }

    }
}
