package com.unicom.acting.pay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.unicom.skyark.component.common.SkyArkPropertyNamingStrategy;

/**
 * 短信模板内容匹配因子
 *
 * @author Wangkh
 */
@JsonNaming(SkyArkPropertyNamingStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TempletValue {
    /**
     * 时间
     * YYYY-MM-DD HH24:MI:SS
     *
     * @see #sysdate
     */
    private String sysdate;
    /**
     * 时间
     * XX日XX时XX分
     *
     * @see #sysdate2
     */
    private String sysdate2;

    /**
     * 交费金额
     *
     * @see #recvfee
     */
    private String recvfee;

    /**
     * 当前可用余额(扣除实时话费)
     *
     * @see #currentavlfee
     */
    private String currentavlfee;

    /**
     * 当前可用余额(扣除实时话费) 不能为负值
     *
     * @see #allnewbalance
     */
    private String allnewbalance;

    /**
     * 当前欠费(含实时话费)
     *
     * @see #allnewrowefee
     */
    private String allnewrowefee;

    /**
     * 账户可用余额
     *
     * @see #allnewmoney
     */
    private String allnewmoney;

    /**
     * 当前欠费(往月欠费)
     *
     * @see #allnewbowefee
     */
    private String allnewbowefee;

    /**
     * 本月已消费（当前实时话费）
     *
     * @see #allrealfee
     */
    private String allrealfee;

    /**
     * 结清月份
     *
     * @see #month
     */
    private String month;

    /**
     * 销户转账转出号码
     *
     * @see #serialnumberOut
     */
    private String serialnumberOut;

    public String getSysdate() {
        return sysdate;
    }

    public void setSysdate(String sysdate) {
        this.sysdate = sysdate;
    }

    public String getSysdate2() {
        return sysdate2;
    }

    public void setSysdate2(String sysdate2) {
        this.sysdate2 = sysdate2;
    }

    public String getRecvfee() {
        return recvfee;
    }

    public void setRecvfee(String recvfee) {
        this.recvfee = recvfee;
    }

    public String getCurrentavlfee() {
        return currentavlfee;
    }

    public void setCurrentavlfee(String currentavlfee) {
        this.currentavlfee = currentavlfee;
    }

    public String getAllnewbalance() {
        return allnewbalance;
    }

    public void setAllnewbalance(String allnewbalance) {
        this.allnewbalance = allnewbalance;
    }

    public String getAllnewrowefee() {
        return allnewrowefee;
    }

    public void setAllnewrowefee(String allnewrowefee) {
        this.allnewrowefee = allnewrowefee;
    }

    public String getAllnewmoney() {
        return allnewmoney;
    }

    public void setAllnewmoney(String allnewmoney) {
        this.allnewmoney = allnewmoney;
    }

    public String getAllnewbowefee() {
        return allnewbowefee;
    }

    public void setAllnewbowefee(String allnewbowefee) {
        this.allnewbowefee = allnewbowefee;
    }

    public String getAllrealfee() {
        return allrealfee;
    }

    public void setAllrealfee(String allrealfee) {
        this.allrealfee = allrealfee;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSerialnumberOut() {
        return serialnumberOut;
    }

    public void setSerialnumberOut(String serialnumberOut) {
        this.serialnumberOut = serialnumberOut;
    }

    @Override
    public String toString() {
        return "SmsFactor{" +
                "sysdate='" + sysdate + '\'' +
                ", sysdate2='" + sysdate2 + '\'' +
                ", recvfee='" + recvfee + '\'' +
                ", currentavlfee='" + currentavlfee + '\'' +
                ", allnewbalance='" + allnewbalance + '\'' +
                ", allnewrowefee='" + allnewrowefee + '\'' +
                ", allnewmoney='" + allnewmoney + '\'' +
                ", allnewbowefee='" + allnewbowefee + '\'' +
                ", allrealfee='" + allrealfee + '\'' +
                ", month='" + month + '\'' +
                ", serialnumberOut='" + serialnumberOut + '\'' +
                '}';
    }
}
