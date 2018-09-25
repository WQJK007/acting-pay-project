package com.unicom.acting.pay.bwriteoff.listener;

import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.batch.common.domain.BaseOrderMgr;
import com.unicom.acting.batch.common.domain.JobInstancesHolder;
import com.unicom.acting.batch.common.exception.ActBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("bWriteoffSkipListener")
@StepScope
public class BWriteoffSkipListener<T> implements SkipListener<BTradeCommInfo, BTradeCommInfo> {
    private static final Logger logger = LoggerFactory.getLogger(BWriteoffSkipListener.class);

    @Autowired
    JobInstancesHolder<T> jobInstancesHolder;

    BaseOrderMgr<T> baseOrderMgr;

    private void execOrder(Throwable throwable,int num,int size)
    {
        try {
            if (throwable instanceof ActBException) {

                if (size > 0 && num >= 0 && num < size) {
                    baseOrderMgr.execute(jobInstancesHolder.getCurrentInstance());
                } else {
                    logger.info("更新工单结果出现异常!!");
                }
                logger.info("if错误码：{},错误信息：{}", ((ActBException) throwable).getErrorCode(), ((ActBException) throwable).getErrorMsg());
            } else {
                if (size > 0 && num >= 0 && num < size) {
                    baseOrderMgr.execute(jobInstancesHolder.getCurrentInstance());
                } else {
                    logger.info("更新工单结果出现异常!!");
                }
                logger.info("else错误码：{},错误信息：{}", ((ActBException) throwable).getErrorCode(), ((ActBException) throwable).getErrorMsg());
            }
        }
        catch(ActBException e)
        {
            logger.info("Batch更新工单失败：{},{}",e.getErrorMsg(),e.getErrorMsg());
        }
        catch(Exception e)
        {
            logger.info("更新工单失败：{}",e.getStackTrace());
        }
    }
    @Override
    public void onSkipInRead(Throwable throwable) {

        logger.info("onSkipInRead");
        int size = jobInstancesHolder.getListInstance().size();
        int num = jobInstancesHolder.getOrderNumber();
        execOrder(throwable,num,size);
    }

    @Override
    public void onSkipInWrite(BTradeCommInfo tradeInfoOut, Throwable throwable) {
        logger.info("onSkipInWrite");
        int size = jobInstancesHolder.getListInstance().size();
        int num = tradeInfoOut.getOrderNumber();
        execOrder(throwable,num,size);
    }

    @Override
    public void onSkipInProcess(BTradeCommInfo tradeInfoIn, Throwable throwable) {
        logger.info("onSkipInProcess");
        int size = jobInstancesHolder.getListInstance().size();
        int num = tradeInfoIn.getOrderNumber();
        execOrder(throwable,num,size);
    }
}
