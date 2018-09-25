package com.unicom.acting.pay.oeprecvfee;

import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.common.partitioner.BasePartitioner;
import com.unicom.acting.pay.oeprecvfee.domain.PayLogChk;
import com.unicom.acting.pay.oeprecvfee.listener.PayLogChkJobListener;
import com.unicom.acting.pay.oeprecvfee.listener.PayLogChkSkipListener;
import com.unicom.acting.pay.oeprecvfee.listener.PayLogChkStepListener;
import com.unicom.acting.pay.oeprecvfee.listener.PayLogChkWriterListener;
import com.unicom.acting.pay.oeprecvfee.processor.PayLogChkProcessor;
import com.unicom.acting.pay.oeprecvfee.reader.PayLogChkReader;
import com.unicom.acting.pay.oeprecvfee.writer.PayLogChkWriter;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;

@EnableBatchProcessing
@Configuration
public class RecvFeeConfig {
    private static final Logger logger = LoggerFactory.getLogger(RecvFeeConfig.class);

    @Autowired
    private JdbcBaseDao jdbcBaseDao;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean("dataSource")
    public DataSource getRepositoryDataSource() {
        logger.info("JobConfig 数据库连接获取");
        return jdbcBaseDao.getJdbcTemplate(DbTypes.ACT_RDS,"11").getJdbcTemplate().getDataSource();
    }
    @Bean("partitionPaylogChk")
    public Step step1Master(@Qualifier("basePartitioner") BasePartitioner partitioner,
                            @Qualifier("partitionHandler") PartitionHandler handler) {
        return stepBuilderFactory.get("payLogChK.master")
                .<PayLogChk, PayLogChk>partitioner("payLogChK", partitioner)
                .partitionHandler(handler)
                .build();
    }


    @Bean("partitionHandler")
    public PartitionHandler partitionHandler(@Qualifier("payLogChK") Step stepPaylogChk) {
        TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
        retVal.setTaskExecutor(new SimpleAsyncTaskExecutor("spring_batch_paylog"));
        retVal.setStep(stepPaylogChk);
        retVal.setGridSize(2);
        return retVal;
    }


    @Bean("payLogChK")
    public Step createStep1(@Qualifier("payLogChkReader") PayLogChkReader reader,
                            @Qualifier("payLogChkProcessor") PayLogChkProcessor processor,
                            @Qualifier("payLogChkWriter") PayLogChkWriter writer,
                            @Qualifier("payLogChkSkipListener") PayLogChkSkipListener skipListener,
                            @Qualifier("payLogChkStepListener") PayLogChkStepListener stepListener,
                            @Qualifier("payLogChkWriterListener") PayLogChkWriterListener writeListener) {
        return this.stepBuilderFactory.get("payLogChK")
                .<PayLogChk, PayLogChk>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(Integer.MAX_VALUE)
                .skip(ActBException.class)
                .retryLimit(0)
                .noRetry(ActBException.class)
                .noRollback(ActBException.class)
                .listener(skipListener)
                .listener(writeListener)
                .listener(stepListener)
                .build();

    }
    @Bean("recvFeeJob")
    public Job createRecvFeeJob(@Qualifier("partitionPaylogChk") Step partitionPaylogChk,
                                @Qualifier("writeoffStep") Step recvFee,
                                @Qualifier("payLogChkJobListener") PayLogChkJobListener listener)
    {
        return this.jobBuilderFactory.get("recvFeeJob")
                .start(partitionPaylogChk)
                .next(recvFee)
                .listener(listener)
                .build();
    }
}
