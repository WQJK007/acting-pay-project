package com.unicom.acting.pay.bwriteoff.business;

import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.common.listener.BaseSkipListener;
import com.unicom.acting.batch.common.listener.BaseStepListener;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.bwriteoff.reader.WriteOffReader;
import com.unicom.acting.pay.bwriteoff.writer.WriteOffWriter;
import com.unicom.acting.pay.bwriteoff.processor.WriteOffProcessor;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BWriteOffConfig {
    private static final Logger logger = LoggerFactory.getLogger(BWriteOffConfig.class);

//    @Autowired
//    private JdbcBaseDao jdbcBaseDao;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Bean("dataSource")
//    public DataSource getRepositoryDataSource() {
//        logger.info("JobConfig 数据库连接获取");
//        return jdbcBaseDao.getJdbcTemplate(DbTypes.ACT_ORDER_RDS, "11").getJdbcTemplate().getDataSource();
//    }
    @Bean("writeoffStep")
    public Step writeOffStep(@Qualifier("writeOffReader") WriteOffReader reader,
                         @Qualifier("writeOffProcessor") WriteOffProcessor processor,
                         @Qualifier("writeOffWriter") WriteOffWriter writer,
                         @Qualifier("baseSkipListener") BaseSkipListener<BatchRecvFeeIn> skipListener,
                         @Qualifier("baseStepListener") BaseStepListener stepListener) {
        return this.stepBuilderFactory.get("writeOffStep")
                .<BTradeCommInfo<TradeCommInfo>, BTradeCommInfo<TradeCommResultInfo>>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipLimit(Integer.MAX_VALUE)
                .skip(ActBException.class)
                .noRetry(ActBException.class)
                .noRollback(ActBException.class)
                .listener(skipListener)
                .listener(stepListener)
                .build();
    }
//    @Bean("recvFeeJob")
//    public Job createRecvFeeJob(@Qualifier("batchRecvFee") Step recvFee)
//    {
//        return this.jobBuilderFactory.get("recvFeeJob")
//                .start(recvFee)
//                .build();
//    }
}
