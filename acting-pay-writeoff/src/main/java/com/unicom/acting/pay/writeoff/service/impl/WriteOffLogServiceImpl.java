package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.pay.domain.AccessLogMQInfo;
import com.unicom.acting.pay.domain.AsynWorkMQInfo;
import com.unicom.acting.pay.domain.PayLogDmnMQInfo;
import com.unicom.acting.pay.domain.PayLogMQInfo;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.dao.WriteOffLogPayDao;
import com.unicom.acting.pay.writeoff.service.WriteOffLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过JDBC方式操作销账日志表
 *
 * @author Administrators
 */
@Service
public class WriteOffLogServiceImpl implements WriteOffLogService {
    private static final Logger logger = LoggerFactory.getLogger(WriteOffLogServiceImpl.class);
    @Autowired
    private WriteOffLogPayDao writeOffLogPayDao;
    @Autowired
    private SysCommOperFeeService sysCommOperPayService;


    @Override
    public long insertPayLog(PayLog payLog, String provinceCode) {
        return writeOffLogPayDao.insertPayLog(payLog, provinceCode);
    }

    @Override
    public List<WriteOffLog> genWriteOffLogInfo(List<WriteOffLog> writeOffLogs, PayLog payLog, WriteOffRuleInfo writeOffRuleInfo) {
        if (CollectionUtils.isEmpty(writeOffLogs)) {
            return null;
        }

        List<WriteOffLog> writeOffLogsIndb = new ArrayList<>();
        //剔除实时账单销账日志
        for (WriteOffLog writeOffLog : writeOffLogs) {
            if ('2' != writeOffLog.getCanPaytag()) {
                writeOffLogsIndb.add(writeOffLog);
            }
        }
        if (CollectionUtils.isEmpty(writeOffLogsIndb)) {
            return writeOffLogsIndb;
        }
        //生成销账日志流水
        List<String> sequences = sysCommOperPayService.getSequence(payLog.getEparchyCode(),
                ActPayPubDef.SEQ_WRITEOFF_ID, writeOffLogsIndb.size(), payLog.getProvinceCode());

        for (int i = 0; i < writeOffLogsIndb.size(); ++i) {
            writeOffLogsIndb.get(i).setWriteoffId(sequences.get(i));
            writeOffLogsIndb.get(i).setChargeId(payLog.getChargeId());
            writeOffLogsIndb.get(i).setOperateTime(payLog.getRecvTime());
            if ("".equals(writeOffLogsIndb.get(i).getNetTypeCode())) {
                writeOffLogsIndb.get(i).setNetTypeCode("**");
            }
            writeOffLogsIndb.get(i).setDepositLimitRuleid(writeOffRuleInfo.getDepositLimitRuleId());
            writeOffLogsIndb.get(i).setDepositPriorRuleid(writeOffRuleInfo.getDepositPriorRuleId());
            writeOffLogsIndb.get(i).setItemPriorRuleid(writeOffRuleInfo.getItemPriorRuleId());
        }
        return writeOffLogsIndb;
    }

    @Override
    public void insertWriteOffLog(List<WriteOffLog> writeOffLogs, String provinceCode) {
        if (CollectionUtils.isEmpty(writeOffLogs)) {
            return;
        }

        long maxRecords = 10000;
        //防止帐单批量插入数据量太大
        if (writeOffLogs.size() > maxRecords) {
            List<WriteOffLog> logTmps = new ArrayList<>();
            for (int i = 0; i < writeOffLogs.size(); ++i) {
                logTmps.add(writeOffLogs.get(i));
                if (logTmps.size() >= maxRecords) {
                    writeOffLogPayDao.insertWriteOffLog(writeOffLogs, provinceCode);
                    logTmps.clear();
                }
            }
            if (!logTmps.isEmpty()) {
                writeOffLogPayDao.insertWriteOffLog(writeOffLogs, provinceCode);
            }

        } else {
            writeOffLogPayDao.insertWriteOffLog(writeOffLogs, provinceCode);
        }
    }

    @Override
    public List<AccessLog> genAccessLogInfo(List<AccessLog> accessLogs, PayLog payLog, boolean existsWriteOffLog) {
        if (CollectionUtils.isEmpty(accessLogs)) {
            throw new SkyArkException("本次缴费没有生成存取款日志");
        }

        //没有往月账单的销账记录,就排除0取款记录
        if (existsWriteOffLog) {
            List<AccessLog> tmpAccessLogs = new ArrayList<>();
            for (AccessLog accessLog : accessLogs) {
                if (0 != accessLog.getMoney() || '0' == accessLog.getAccessTag()) {
                    tmpAccessLogs.add(accessLog);
                }
            }
            accessLogs = tmpAccessLogs;
        }

        if (CollectionUtils.isEmpty(accessLogs)) {
            throw new SkyArkException("本次缴费没有生成存取款日志");
        }
        logger.debug("accessLogs.size = " + accessLogs.size());
        //存款日志流水
        List<String> sequences = sysCommOperPayService.getSequence(payLog.getRecvEparchyCode(),
                ActPayPubDef.SEQ_ACCESS_ID, accessLogs.size(), payLog.getProvinceCode());
        List<AccessLog> tmpAccessLogs = new ArrayList<>();
        for (int i = 0; i < accessLogs.size(); i++) {
            accessLogs.get(i).setAccessId(sequences.get(i));
            accessLogs.get(i).setChargeId(payLog.getChargeId());
            accessLogs.get(i).setOperateTime(payLog.getRecvTime());
            tmpAccessLogs.add(accessLogs.get(i));
        }
        return tmpAccessLogs;
    }

    @Override
    public void insertAccessLog(List<AccessLog> accessLogList, String provinceCode) {
        writeOffLogPayDao.insertAccessLog(accessLogList, provinceCode);
    }

    @Override
    public void insertWriteSnapLog(WriteSnapLog writeSnapLog, String provinceCode) {
        if (writeOffLogPayDao.insertWriteSnapLog(writeSnapLog, provinceCode) == 0) {
            throw new SkyArkException("插入快照日志表失败!chargeId=" + writeSnapLog.getChargeId());
        }
    }

    @Override
    public PayLogMQInfo genPayLogMQInfo(PayLog paylog) {
        PayLogMQInfo payLogMQInfo = new PayLogMQInfo();
        payLogMQInfo.setChargeId(paylog.getChargeId());
        payLogMQInfo.setEparchyCode(paylog.getEparchyCode());
        payLogMQInfo.setCityCode(paylog.getCityCode());
        payLogMQInfo.setCustId(paylog.getCustId());
        payLogMQInfo.setUserId(paylog.getUserId());
        payLogMQInfo.setSerialNumber(paylog.getSerialNumber());
        payLogMQInfo.setNetTypeCode(paylog.getNetTypeCode());
        payLogMQInfo.setAcctId(paylog.getAcctId());
        payLogMQInfo.setChannelId(paylog.getChannelId());
        payLogMQInfo.setPaymentId(paylog.getPaymentId());
        payLogMQInfo.setPayFeeModeCode(paylog.getPayFeeModeCode());
        payLogMQInfo.setPaymentOp(paylog.getPaymentOp());
        payLogMQInfo.setRecvFee(paylog.getRecvFee());
        payLogMQInfo.setLimitMoney(paylog.getLimitMoney());
        payLogMQInfo.setRecvTime(paylog.getRecvTime());
        payLogMQInfo.setRecvProvinceCode(paylog.getRecvProvinceCode());
        payLogMQInfo.setRecvEparchyCode(paylog.getRecvEparchyCode());
        payLogMQInfo.setRecvCityCode(paylog.getRecvCityCode());
        payLogMQInfo.setRecvDepartId(paylog.getRecvDepartId());
        payLogMQInfo.setRecvStaffId(paylog.getRecvStaffId());
        payLogMQInfo.setPaymentReasonCode(paylog.getPaymentReasonCode());
        payLogMQInfo.setInputMode(paylog.getInputMode());
        payLogMQInfo.setInputNo(paylog.getInputNo());
        payLogMQInfo.setOuterTradeId(paylog.getOuterTradeId());
        payLogMQInfo.setActionCode(paylog.getActionCode());
        payLogMQInfo.setActionEventId(paylog.getActionEventId());
        payLogMQInfo.setActTag(paylog.getActTag());
        payLogMQInfo.setExtendTag(paylog.getActTag());
        payLogMQInfo.setPaymentRuleId(paylog.getPaymentRuleId());
        payLogMQInfo.setRemark(paylog.getRemark());
        payLogMQInfo.setProvinceCode(paylog.getProvinceCode());
        payLogMQInfo.setCancelStaffId(paylog.getCancelStaffId());
        payLogMQInfo.setCancelDepartId(paylog.getCancelDepartId());
        payLogMQInfo.setCancelCityCode(paylog.getCancelCityCode());
        payLogMQInfo.setCancelEparchyCode(paylog.getCancelEparchyCode());
        payLogMQInfo.setCancelChargeId(paylog.getCancelChargeId());
        payLogMQInfo.setCancelTime(paylog.getCancelTime());
        payLogMQInfo.setRsrvFee1(paylog.getRsrvFee1());
        payLogMQInfo.setRsrvFee2(paylog.getRsrvFee2());
        payLogMQInfo.setRsrvInfo1(paylog.getRsrvInfo1());
        payLogMQInfo.setDevCode(paylog.getDevCode());
        payLogMQInfo.setNpTag(paylog.getNpTag());
        payLogMQInfo.setAgentTag(paylog.getAgentTag());
        payLogMQInfo.setContractTag(paylog.getContractTag());
        return payLogMQInfo;
    }

    @Override
    public List<AccessLogMQInfo> genAccessLogMQInfo(List<AccessLog> accessLogList) {
        List<AccessLogMQInfo> accessLogMQInfoList = new ArrayList(accessLogList.size());
        for (AccessLog accessLog : accessLogList) {
            AccessLogMQInfo accessLogMQInfo = new AccessLogMQInfo();
            accessLogMQInfo.setAccessId(accessLog.getAccessId());
            accessLogMQInfo.setAcctId(accessLog.getAcctId());
            accessLogMQInfo.setChargeId(accessLog.getChargeId());
            accessLogMQInfo.setAcctBalanceId(accessLog.getAcctBalanceId());
            accessLogMQInfo.setDepositCode(accessLog.getDepositCode());
            accessLogMQInfo.setOldBalance(accessLog.getOldBalance());
            accessLogMQInfo.setMoney(accessLog.getMoney());
            accessLogMQInfo.setNewBalance(accessLog.getNewBalance());
            accessLogMQInfo.setAccessTag(accessLog.getAccessTag());
            accessLogMQInfo.setOperateTime(accessLog.getOperateTime());
            accessLogMQInfo.setEparchyCode(accessLog.getEparchyCode());
            accessLogMQInfo.setCancelTag(accessLog.getCancelTag());
            accessLogMQInfo.setInvoiceFee(accessLog.getInvoiceFee());
            accessLogMQInfo.setProvinceCode(accessLog.getProvinceCode());
            accessLogMQInfoList.add(accessLogMQInfo);
        }
        return accessLogMQInfoList;
    }

    @Override
    public PayLogDmnMQInfo genPayLogDmnMQInfo(PayLogDmn payLogDmn) {
        PayLogDmnMQInfo payLogDmnMQInfo = new PayLogDmnMQInfo();
        payLogDmnMQInfo.setTradeId(payLogDmn.getChargeId());
        payLogDmnMQInfo.setTradeTypeCode(0);
        payLogDmnMQInfo.setEparchyCode(payLogDmn.getEparchyCode());
        payLogDmnMQInfo.setProvinceCode(payLogDmn.getProvinceCode());
        payLogDmnMQInfo.setBatchId(payLogDmn.getChargeId());
        payLogDmnMQInfo.setChargeId(payLogDmn.getChargeId());
        payLogDmnMQInfo.setAcctId(payLogDmn.getAcctId());
        payLogDmnMQInfo.setUserId(payLogDmn.getUserId());
        payLogDmnMQInfo.setSerialNumber(payLogDmn.getSerialNumber());
        payLogDmnMQInfo.setWriteoffMode(payLogDmn.getWriteoffMode());
        payLogDmnMQInfo.setLimitMode(payLogDmn.getLimitMode());
        payLogDmnMQInfo.setChannelId(payLogDmn.getChannelId());
        payLogDmnMQInfo.setPaymentId(payLogDmn.getPaymentId());
        payLogDmnMQInfo.setPaymentOp(payLogDmn.getPaymentOp());
        payLogDmnMQInfo.setPayFeeModeCode(payLogDmn.getPayFeeModeCode());
        payLogDmnMQInfo.setRecvFee(payLogDmn.getRecvFee());
        payLogDmnMQInfo.setOuterTradeId(payLogDmn.getOuterTradeId());
        payLogDmnMQInfo.setBillStartCycleId(payLogDmn.getBillStartCycleId());
        payLogDmnMQInfo.setBillEndCycleId(payLogDmn.getBillEndCycleId());
        payLogDmnMQInfo.setStartDate(payLogDmn.getStartDate());
        payLogDmnMQInfo.setMonths(payLogDmn.getMonths());
        payLogDmnMQInfo.setLimitMoney(payLogDmn.getLimitMoney());
        payLogDmnMQInfo.setPaymentReasonCode(payLogDmn.getPaymentReasonCode());
        payLogDmnMQInfo.setExtendTag(payLogDmn.getExtendTag());
        payLogDmnMQInfo.setAcctBalanceId(payLogDmn.getAcctBalanceId());
        payLogDmnMQInfo.setDepositCode(payLogDmn.getDepositCode());
        payLogDmnMQInfo.setPrivateTag(payLogDmn.getPrivateTag());
        payLogDmnMQInfo.setRemark(payLogDmn.getRemark());
        payLogDmnMQInfo.setTradeTime(payLogDmn.getTradeTime());
        payLogDmnMQInfo.setTradeStaffId(payLogDmn.getTradeStaffId());
        payLogDmnMQInfo.setTradeDepartId(payLogDmn.getTradeDepartId());
        payLogDmnMQInfo.setTradeEparchyCode(payLogDmn.getTradeEparchyCode());
        payLogDmnMQInfo.setTradeCityCode(payLogDmn.getTradeCityCode());

        // 一卡充缴费可打发票金额放入备用字段rsrvInfo1
        if (100006 == payLogDmn.getPaymentId()) {
            payLogDmnMQInfo.setRsrvInfo1(payLogDmn.getRsrvInfo1());
        }
        payLogDmnMQInfo.setRelChargeId(payLogDmn.getRelChargeId());
        return payLogDmnMQInfo;
    }

    @Override
    public AsynWorkMQInfo genAsynWorkMQInfo(AsynWork asynWork) {
        AsynWorkMQInfo asynWorkMQInfo = new AsynWorkMQInfo();
        asynWorkMQInfo.setWorkId(asynWork.getWorkId());
        asynWorkMQInfo.setChargeId(asynWork.getChargeId());
        asynWorkMQInfo.setTradeTime(asynWork.getTradeTime());
        //外围缴费流水
        asynWorkMQInfo.setRsrvStr1(asynWork.getRsrvStr1());
        asynWorkMQInfo.setRsrvStr3(asynWork.getRsrvStr3());
        asynWorkMQInfo.setDealTag(asynWork.getDealTag());
        asynWorkMQInfo.setWorkTypeCode(asynWork.getWorkTypeCode());
        asynWorkMQInfo.setStartCycleId(asynWork.getStartCycleId());
        asynWorkMQInfo.setEndCycleId(asynWork.getEndCycleId());
        asynWorkMQInfo.setTradeStaffId(asynWork.getTradeStaffId());
        asynWorkMQInfo.setTradeDepartId(asynWork.getTradeDepartId());
        asynWorkMQInfo.setTradeCityCode(asynWork.getTradeCityCode());
        asynWorkMQInfo.setTradeEparchyCode(asynWork.getTradeEparchyCode());
        asynWorkMQInfo.setChannelId(asynWork.getChannelId());
        asynWorkMQInfo.setPaymentId(asynWork.getPaymentId());
        asynWorkMQInfo.setPayFeeModeCode(asynWork.getPayFeeModeCode());
        asynWorkMQInfo.setPaymentOp(asynWork.getPaymentOp());
        asynWorkMQInfo.setWriteoffMode(asynWork.getWriteoffMode());
        asynWorkMQInfo.setRsrvFee1(asynWork.getRsrvFee1());
        asynWorkMQInfo.setAcctId(asynWork.getAcctId());
        asynWorkMQInfo.setUserId(asynWork.getUserId());
        asynWorkMQInfo.setEparchyCode(asynWork.getEparchyCode());
        asynWorkMQInfo.setNetTypeCode(asynWork.getNetTypeCode());
        asynWorkMQInfo.setSerialNumber(asynWork.getSerialNumber());
        asynWorkMQInfo.setRsrvStr2(asynWork.getRsrvStr2());
        asynWorkMQInfo.setRsrvFee8(asynWork.getRsrvFee8());
        return asynWorkMQInfo;
    }
}
