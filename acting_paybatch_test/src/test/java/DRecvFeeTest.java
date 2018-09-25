import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DRecvFeeTest.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("local")
//@ComponentScan(basePackages = "com.unicom")
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class, JtaAutoConfiguration.class, DataSourceHealthIndicatorAutoConfiguration.class})
class abc
{
    String b = "ABC";
    String c = "123";
    int a = 4;
    long d = 12;
}

public class DRecvFeeTest {

//    private static final Logger logger = LoggerFactory.getLogger(DRecvFeeTest.class);

//    @Autowired
//    @Qualifier("recvFeeJob")
//    private Job myJob;
//
//    @Autowired
//    private JobLauncher jobLauncher;


    @Test
    public void recvFeeTest() throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("curTime", System.currentTimeMillis())
//                .addString("jobName", "核心缴费批处理测试")
//                .toJobParameters();
//
//        this.jobLauncher.run(myJob, jobParameters);
        //System.out.println("test!!");
        CheckTest<abc> ct=new CheckTest();
        abc a = new abc();
        System.out.print("结果："+ct.getTempletAttr(a,"d")+"\n");


    }
}