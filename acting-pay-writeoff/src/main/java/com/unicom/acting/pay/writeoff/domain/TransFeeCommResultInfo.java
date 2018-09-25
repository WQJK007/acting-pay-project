package com.unicom.acting.pay.writeoff.domain;

import com.unicom.acting.pay.domain.*;

import java.util.List;

/**
 * 余额转移应答信息
 *
 * @author Wangkh
 */
public class TransFeeCommResultInfo extends TradeCommResultInfo {
    /**
     * @see #payLogs 缴费日志表
     */
    private List<PayLog> payLogs;

    /**
     * @see #transOutCLPayLogs 省份代收日志
     */
    private List<CLPayLog> transOutCLPayLogs;


    /**
     * @see #transOutAccessLogs 存取款日志
     */
    private List<AccessLog> transOutAccessLogs;

    /**
     * @see #transOutWriteOffLogs 销账日志
     */
    private List<WriteOffLog> transOutWriteOffLogs;

    /**
     * @see #writeSnapLogs 销账快照
     */
    private List<WriteSnapLog> writeSnapLogs;

    /**
     * @see #payLogMQInfos 缴费日志MQ
     */
    private List<PayLogMQInfo> payLogMQInfos;

    /**
     * @see #recvCreditMQInfos 信控缴费解耦
     */
    private List<RecvCreditMQInfo> recvCreditMQInfos;

    /**
     * @see #smsOrders 短信工单
     */
    private List<SmsOrder> smsOrders;

    /**
     * @see #smsMQInfos 短信MQ
     */
    private List<SmsMQInfo> smsMQInfos;

    /**
     * @see #chargeRelation 交易关联日志
     */
    private ChargeRelation chargeRelation;

    public List<PayLog> getPayLogs() {
        return payLogs;
    }

    public void setPayLogs(List<PayLog> payLogs) {
        this.payLogs = payLogs;
    }

    public List<CLPayLog> getTransOutCLPayLogs() {
        return transOutCLPayLogs;
    }

    public void setTransOutCLPayLogs(List<CLPayLog> transOutCLPayLogs) {
        this.transOutCLPayLogs = transOutCLPayLogs;
    }

    public List<AccessLog> getTransOutAccessLogs() {
        return transOutAccessLogs;
    }

    public void setTransOutAccessLogs(List<AccessLog> transOutAccessLogs) {
        this.transOutAccessLogs = transOutAccessLogs;
    }

    public List<WriteOffLog> getTransOutWriteOffLogs() {
        return transOutWriteOffLogs;
    }

    public void setTransOutWriteOffLogs(List<WriteOffLog> transOutWriteOffLogs) {
        this.transOutWriteOffLogs = transOutWriteOffLogs;
    }

    public List<WriteSnapLog> getWriteSnapLogs() {
        return writeSnapLogs;
    }

    public void setWriteSnapLogs(List<WriteSnapLog> writeSnapLogs) {
        this.writeSnapLogs = writeSnapLogs;
    }

    public List<PayLogMQInfo> getPayLogMQInfos() {
        return payLogMQInfos;
    }

    public void setPayLogMQInfos(List<PayLogMQInfo> payLogMQInfos) {
        this.payLogMQInfos = payLogMQInfos;
    }

    public List<RecvCreditMQInfo> getRecvCreditMQInfos() {
        return recvCreditMQInfos;
    }

    public void setRecvCreditMQInfos(List<RecvCreditMQInfo> recvCreditMQInfos) {
        this.recvCreditMQInfos = recvCreditMQInfos;
    }

    @Override
    public List<SmsOrder> getSmsOrders() {
        return smsOrders;
    }

    @Override
    public void setSmsOrders(List<SmsOrder> smsOrders) {
        this.smsOrders = smsOrders;
    }

    @Override
    public List<SmsMQInfo> getSmsMQInfos() {
        return smsMQInfos;
    }

    @Override
    public void setSmsMQInfos(List<SmsMQInfo> smsMQInfos) {
        this.smsMQInfos = smsMQInfos;
    }

    public ChargeRelation getChargeRelation() {
        return chargeRelation;
    }

    public void setChargeRelation(ChargeRelation chargeRelation) {
        this.chargeRelation = chargeRelation;
    }

    @Override
    public String toString() {
        return "TransFeeCommResultInfo{" +
                "payLogs=" + payLogs +
                ", transOutCLPayLogs=" + transOutCLPayLogs +
                ", transOutAccessLogs=" + transOutAccessLogs +
                ", transOutWriteOffLogs=" + transOutWriteOffLogs +
                ", writeSnapLogs=" + writeSnapLogs +
                ", payLogMQInfos=" + payLogMQInfos +
                ", recvCreditMQInfos=" + recvCreditMQInfos +
                ", smsOrders=" + smsOrders +
                ", smsMQInfos=" + smsMQInfos +
                ", chargeRelation=" + chargeRelation +
                '}';
    }
}
