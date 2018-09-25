package com.unicom.acting.pay.oeprecvfee.listener;

import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.oeprecvfee.business.PayLogChkMgr;
import com.unicom.acting.pay.oeprecvfee.domain.PayLogChk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("payLogChkSkipListener")
@StepScope
public class PayLogChkSkipListener implements SkipListener<PayLogChk, PayLogChk> {
    private static final Logger logger = LoggerFactory.getLogger(PayLogChkSkipListener.class);

    @Autowired
    PayLogChkMgr payLogChkMgr;

    private void updatePayLogChk(Throwable throwable,PayLogChk payLogChk,String dealTag,String actTag)
    {
        try
        {
            if(throwable instanceof ActBException)
            {
                logger.info("if错误码：{}，错误信息：{}",((ActBException) throwable).getErrorCode(),((ActBException) throwable).getErrorMsg());
                payLogChk.setResultCode(((ActBException) throwable).getErrorCode());
                payLogChk.setResultInfo(((ActBException) throwable).getErrorMsg());
            }
            else
            {
                logger.info("else错误码：{}，错误信息：{}",((ActBException) throwable).getErrorCode(),((ActBException) throwable).getErrorMsg());

                payLogChk.setResultCode(((ActBException) throwable).getErrorCode());
                payLogChk.setResultInfo(((ActBException) throwable).getErrorMsg());
            }
            BatchRecvFeeIn recvFeeIn = new BatchRecvFeeIn();
            recvFeeIn.setOriginTradeId(payLogChk.getTradeId());
            recvFeeIn.setTradeTypeCode(payLogChk.getTradeTypeCode());
            recvFeeIn.setOriginActTag(payLogChk.getActTag());
            recvFeeIn.setOriginDealtag(payLogChk.getDealTag());
            recvFeeIn.setBatchId(payLogChk.getBatchId());
            recvFeeIn.setResultCode(Integer.parseInt(payLogChk.getResultCode()));
            recvFeeIn.setResultInfo(payLogChk.getResultInfo());
            recvFeeIn.setActTag(actTag);
            recvFeeIn.setDealtag(dealTag);
            payLogChkMgr.execute(recvFeeIn);

        }catch(Exception e)
        {
            logger.info("Exception::{}",e.getStackTrace());
        }
    }

    @Override
    public void onSkipInRead(Throwable throwable) {

        logger.info("onSkipInRead");
        logger.info("错误码：{},错误信息：{}",((ActBException) throwable).getErrorCode(),((ActBException) throwable).getErrorMsg());
        throw new ActBException(((ActBException) throwable).getErrorCode());

    }

    @Override
    public void onSkipInWrite(PayLogChk tradeInfoOut, Throwable throwable) {
        logger.info("onSkipInWrite");
        updatePayLogChk(throwable,tradeInfoOut,"2","5");
    }

    @Override
    public void onSkipInProcess(PayLogChk tradeInfoIn, Throwable throwable) {
        logger.info("onSkipInProcess");
        updatePayLogChk(throwable,tradeInfoIn,"2","5");
    }
}
