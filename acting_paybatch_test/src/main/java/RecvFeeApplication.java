import com.unicom.skyark.component.SkyArkApplication;
import com.unicom.skyark.component.web.config.RestConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@SkyArkApplication(exclude= {DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class, JtaAutoConfiguration.class, DataSourceHealthIndicatorAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class, JtaAutoConfiguration.class, DataSourceHealthIndicatorAutoConfiguration.class})
//@SpringBootApplication
//@ComponentScan(basePackages = "com.unicom")
@RestController
public class RecvFeeApplication {

    @Autowired
    @Qualifier("recvFeeJob")
    private Job myJob;


//    @Autowired
//    @Qualifier("MyJob1")
//    private Job job1;

    @Autowired
    private JobLauncher jobLauncher;

    @RequestMapping(value = "/startjob", method = RequestMethod.GET)
    public String startJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("startId", "0")
                .addString("endId", "9999")
                .addString("dataSourceId", "11")
                .addLong("curTime", System.currentTimeMillis())
                .addString("jobName", "缴费批处理")
                .toJobParameters();
        this.jobLauncher.run(myJob, jobParameters);
        return "success";
    }
//    @RequestMapping(value = "/startjob", method = RequestMethod.GET)
//    public String startJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
//
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("curTime", System.currentTimeMillis())
//                .addString("jobName", "缴费批处理")
//                .toJobParameters();
//        this.jobLauncher.run(myJob, jobParameters);
//        return "success";
//    }
   @RequestMapping(value = "/test", method = RequestMethod.GET)
   public String test()
   {
       return "OK";
   }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RecvFeeApplication.class, args);
    }
}
