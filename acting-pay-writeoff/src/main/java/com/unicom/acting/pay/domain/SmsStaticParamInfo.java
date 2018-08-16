package com.unicom.acting.pay.domain;

import com.unicom.skyark.component.exception.SkyArkException;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信静态对象
 */
public class SmsStaticParamInfo {
    //不发送
    public static final String MSG_SEND_ABNORMAL = "0";
    //进入临时表
    public static final String MSG_SEND_TEMP = "1";
    //进入正式表
    public static final String MSG_SEND_NORMAL = "2";
    //进入临时表状态为已发送
    public static final String MSG_SEND_TEMP_DEALED = "3";

    public static final String ZEROCREDIT_OWED = "0";
    public static final String ZEROCREDIT_BANALCE = "1";
    public static final String ORDCREDIT_OWED = "2";
    public static final String ORDCREDIT_BALANCE = "3";
    public static final String BOUNDLESSCREDIT_OWED = "4";
    public static final String BOUNDLESSCREDIT_BALANCE = "5";
    public static final String BALANCE_BEFORERECV = "6";
    public static final String OWED_AFTERRECV = "7";
    public static final String BALANCE_AFTERERECV = "8";
    public static final String HASOWEFEE_AFTERRECV_NEW = "9";
    public static final String HASNOTOWEFEE_AFTERERECV_NEW = "10";
    public static final String HASNOTOWEFEE_BEFORERECV_NEW = "11";
    public static final int MIN_USER_CREDITVALUE = 0;
    public static final int MAX_USER_CREDITVALUE = 99999999;
    //短信金额临界值
    public static final int COMP_NUM = 10000;


    private SmsStaticParamInfo() {
    }

    //短信条件按省份查询
    private static Map<String, Map<Long, SmsCond>> allMMPSmsCond;
    //短信模板全集
    private static Map<Long, SmsTemplet> allMSmsTemplet;
    //充值短信发送模板标识转换 Map<provCond, Map<smsType, Map<smTempLetId, SmsConvert>>>
    private static Map<String, Map<String, Map<Long, SmsConvert>>> allMMMPSmsConvert;


    public static Map<String, Map<Long, SmsCond>> getAllMPSmsCond() {
        return allMMPSmsCond;
    }

    public static void setAllMPSmsCond(List<SmsCond> smsConds) {
        if (!CollectionUtils.isEmpty(allMMPSmsCond)) {
            allMMPSmsCond.clear();
        } else {
            allMMPSmsCond = new HashMap<>();
        }

        for (SmsCond smsCond : smsConds) {
            if (allMMPSmsCond.containsKey(smsCond.getProvinceCode())) {
                allMMPSmsCond.get(smsCond.getProvinceCode()).put(smsCond.getTradeDefId(), smsCond);
            } else {
                Map<Long, SmsCond> tmp = new HashMap<>();
                tmp.put(smsCond.getTradeDefId(), smsCond);
                allMMPSmsCond.put(smsCond.getProvinceCode(), tmp);
            }
        }
    }

    public static Map<Long, SmsTemplet> getAllMSmsTemplet() {
        return allMSmsTemplet;
    }

    public static void setAllSmsTemplet(List<SmsTemplet> smsTemplets) {
        if (!CollectionUtils.isEmpty(allMSmsTemplet)) {
            allMSmsTemplet.clear();
        } else {
            allMSmsTemplet = new HashMap<>();
        }

        for (SmsTemplet smsTemplet : smsTemplets) {
            allMSmsTemplet.put(smsTemplet.getSmTempletId(), smsTemplet);
        }
    }

    public static Map<String, Map<String, Map<Long, SmsConvert>>> getAllMMMPSmsConvert() {
        return allMMMPSmsConvert;
    }

    ;

    public static void setAllMMMPSmsConvert(List<SmsConvert> smsConverts) {
        if (!CollectionUtils.isEmpty(allMMMPSmsConvert)) {
            allMMMPSmsConvert.clear();
        } else {
            allMMMPSmsConvert = new HashMap<>();
        }

        for (SmsConvert smsConvert : smsConverts) {
            if (allMMMPSmsConvert.containsKey(smsConvert.getProvinceCode())) {
                Map<String, Map<Long, SmsConvert>> tmpMMPSmsConvert = allMMMPSmsConvert.get(smsConvert.getProvinceCode());
                if (tmpMMPSmsConvert.containsKey(smsConvert.getSmsType())) {
                    tmpMMPSmsConvert.get(smsConvert.getSmsType()).put(smsConvert.getOriSmTempletId(), smsConvert);
                } else {
                    Map<Long, SmsConvert> tmpMPSmsConvert = new HashMap<>();
                    tmpMPSmsConvert.put(smsConvert.getOriSmTempletId(), smsConvert);
                    tmpMMPSmsConvert.put(smsConvert.getSmsType(), tmpMPSmsConvert);
                }
            } else {
                Map<Long, SmsConvert> tmpMPSmsConvert = new HashMap<>();
                tmpMPSmsConvert.put(smsConvert.getOriSmTempletId(), smsConvert);
                Map<String, Map<Long, SmsConvert>> tmpMMPSmsConvert = new HashMap<>();
                tmpMMPSmsConvert.put(smsConvert.getSmsType(), tmpMPSmsConvert);
                allMMMPSmsConvert.put(smsConvert.getProvinceCode(), tmpMMPSmsConvert);
            }
        }
    }

    public static SmsTemplet getSmsTemplet(long templetId) {
        if (!allMSmsTemplet.containsKey(templetId)) {
            throw new SkyArkException("没有对应的短信模板!smstempletId=" + templetId);
        }
        return allMSmsTemplet.get(templetId);
    }

}
