package com.unicom.acting.pay.bwriteoff.initialize;

import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.fee.writeoff.service.WriteOffRuleService;
import com.unicom.acting.pay.domain.ActingPayCommparaDef;
import com.unicom.acting.pay.writeoff.service.SmsService;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.jdbc.DbTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component("paramsLoader")
public class ParamsLoader {
    private static final Logger logger = LoggerFactory.getLogger(ParamsLoader.class);
    /**
     * 时间戳
     */
    private static String timeStamp = "";
    @Autowired
    private CommParaFeeService commParaService;
    @Autowired
    private WriteOffRuleService writeOffRuleService;
    @Autowired
    private SmsService smsService;
    @PostConstruct
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void initWriteOffParams() throws SkyArkException {
        //获取系统当前时间戳
        String currTimeStamp = commParaService.getParamTimeStamp(
                ActingPayCommparaDef.ASM_PARAM_TIMESTAMP);
        logger.info("timeStamp = " + timeStamp + ",currTimeStamp = " + currTimeStamp);
        if ("".equals(timeStamp) || !currTimeStamp.equals(timeStamp)) {
            long startTime = System.currentTimeMillis();   //获取开始时间
            //加载缴费销账相关参数
            writeOffRuleService.loadWriteOffParam();
            //加载短信相关参数
            smsService.loadSmsParam();

            if ("".equals(timeStamp) && "".equals(currTimeStamp)) {
                timeStamp = "1900-01-01 01:01:01";
            } else {
                timeStamp = currTimeStamp;
            }
            logger.info("销账参数加载一共耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        }
    }
}
