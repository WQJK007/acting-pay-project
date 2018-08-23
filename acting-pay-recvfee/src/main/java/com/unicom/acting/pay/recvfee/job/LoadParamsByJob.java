package com.unicom.acting.pay.recvfee.job;

import com.unicom.acting.fee.domain.PubCommParaDef;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.fee.writeoff.service.WriteOffRuleService;
import com.unicom.acting.pay.domain.ActPayPubDef;
import com.unicom.acting.pay.writeoff.service.SmsService;
import com.unicom.skyark.component.jdbc.DbTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 通过定时任务加载缴费销账相关参数
 *
 * @author Wangkh
 */
@Component
public class LoadParamsByJob {
    private static final Logger logger = LoggerFactory.getLogger(LoadParamsByJob.class);
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

    /**
     * 缴费销账相关参数每5分钟查询一次时间戳实现自动加载
     */
    @PostConstruct
    @Scheduled(fixedRate = 1000 * 60 * 5)
    private void initParam() {
        //获取系统当前时间戳
        String currTimeStamp = commParaService.getParamTimeStamp(
                PubCommParaDef.ASM_PARAM_TIMESTAMP, DbTypes.ACT_PARA_RDS);
        logger.info("timeStamp = " + timeStamp + ",currTimeStamp = " + currTimeStamp);
        if ("".equals(timeStamp) || !currTimeStamp.equals(timeStamp)) {
            long startTime = System.currentTimeMillis();   //获取开始时间
            //加载缴费销账相关参数
            writeOffRuleService.loadWriteOffParam(DbTypes.ACT_PARA_RDS);
            //加载短信相关参数
            smsService.loadSmsParam(DbTypes.ACT_PARA_RDS);

            if ("".equals(timeStamp) && "".equals(currTimeStamp)) {
                timeStamp = "1900-01-01 01:01:01";
            } else {
                timeStamp = currTimeStamp;
            }
            logger.info("销账参数加载一共耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        }

    }
}
