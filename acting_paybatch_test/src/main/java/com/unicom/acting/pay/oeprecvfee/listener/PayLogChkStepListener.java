package com.unicom.acting.pay.oeprecvfee.listener;

import com.unicom.acting.batch.common.listener.BaseStepListener;
import com.unicom.acting.pay.bwriteoff.domain.RecvFeeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("payLogChkStepListener")
public class PayLogChkStepListener extends BaseStepListener {
    private static final Logger logger = LoggerFactory.getLogger(PayLogChkStepListener.class);

    @Autowired
    RecvFeeHolder recvFeeHolder;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        JobExecution jobEx = stepExecution.getJobExecution();
        logger.info("#############" + jobEx.getJobId() + ":" + stepExecution.getStepName() + " begin execute #############");

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        JobExecution jobEx = stepExecution.getJobExecution();
        logger.info("#############" + jobEx.getJobId() + ":" + stepExecution.getStepName() + " end execute #############");
        logger.info("工单条数 num = {}",recvFeeHolder.getListInstance().size());
        String exitCode = stepExecution.getExitStatus().getExitCode();
        logger.info("更新表的状态{}",exitCode);
        if (!exitCode.equals(ExitStatus.FAILED.getExitCode()) &&
                stepExecution.getSkipCount() > 0) {
            logger.info("更新表1！！！！");
            return new ExitStatus("COMPLETED with SKIPS");
        }
        else {
            logger.info("更新表2！！！！");
            return stepExecution.getExitStatus();
        }
    }

}
