package com.unicom.acting.pay.bwriteoff.listener;

import com.unicom.acting.batch.common.listener.BaseStepListener;
import com.unicom.acting.pay.bwriteoff.domain.RecvFeeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("bWriteoffStepListener")
public class BWriteoffStepListener extends BaseStepListener {
    private static final Logger logger = LoggerFactory.getLogger(BWriteoffStepListener.class);

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
        logger.info("Step Exit Code ={}",exitCode);
//        if (!exitCode.equals(ExitStatus.FAILED.getExitCode()) &&
//                stepExecution.getSkipCount() > 0) {
//            return new ExitStatus("COMPLETED with SKIPS");
//        }
//        else {
//            return stepExecution.getExitStatus();
//        }
        return stepExecution.getExitStatus();
    }

}
