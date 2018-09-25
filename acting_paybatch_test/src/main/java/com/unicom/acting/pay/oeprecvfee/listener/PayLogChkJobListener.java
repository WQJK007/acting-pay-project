package com.unicom.acting.pay.oeprecvfee.listener;

import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.bwriteoff.domain.RecvFeeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component("payLogChkJobListener")
public class PayLogChkJobListener implements JobExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(PayLogChkJobListener.class);

    @Autowired
    RecvFeeHolder<BatchRecvFeeIn> recvFeeHolder;

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("-------任务:" + jobExecution.getJobConfigurationName() + "执行结束--------------开始时间：{}， 结束时间：{}",
                jobExecution.getExecutionContext().get("start_date"),
                getCurTime());
        logger.info("处理条数：{}", this.recvFeeHolder.getListInstance().size());
        this.recvFeeHolder.getListInstance().clear();
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext().put("start_date", getCurTime());
        logger.info("-------任务:" + jobExecution.getJobConfigurationName() + "执行开始--------------");
    }

    protected String getCurTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }
}
