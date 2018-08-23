package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.dao.AccessLogDao;
import com.unicom.acting.pay.dao.PayLogDao;
import com.unicom.acting.pay.dao.WriteOffLogDao;
import com.unicom.acting.pay.dao.WriteSnapLogDao;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.domain.PayOtherLog;
import com.unicom.acting.pay.writeoff.service.TradeCommLogService;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TradeCommLogServiceImpl implements TradeCommLogService {
    private static final Logger logger = LoggerFactory.getLogger(TradeCommLogServiceImpl.class);
    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private AccessLogDao accessLogDao;
    @Autowired
    private WriteOffLogDao writeOffLogDao;
    @Autowired
    private WriteSnapLogDao writeSnapLogDao;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;


    @Override
    public void insertPayLog(FeePayLog feePayLog, String provinceCode) {
        PayLog payLog = new PayLog();
        payLog.setProvinceCode(feePayLog.getProvinceCode());
        payLog.setEparchyCode(feePayLog.getEparchyCode());
        payLog.setCityCode(feePayLog.getCityCode());
        payLog.setCustId(feePayLog.getCustId());
        payLog.setAcctId(feePayLog.getAcctId());
        payLog.setUserId(feePayLog.getUserId());
        payLog.setSerialNumber(feePayLog.getSerialNumber());
        payLog.setNetTypeCode(feePayLog.getNetTypeCode());
        payLog.setChargeId(feePayLog.getChargeId());
        payLog.setExtendTag(feePayLog.getExtendTag());
        payLog.setOuterTradeId(feePayLog.getOuterTradeId());
        payLog.setChannelId(feePayLog.getChannelId());
        payLog.setPaymentId(feePayLog.getPaymentId());
        payLog.setPaymentOp(feePayLog.getPaymentOp());
        payLog.setPayFeeModeCode(feePayLog.getPayFeeModeCode());
        payLog.setLimitMoney(feePayLog.getLimitMoney());
        payLog.setRecvTime(feePayLog.getRecvTime());
        payLog.setRecvFee(feePayLog.getRecvFee());
        payLog.setRecvProvinceCode(feePayLog.getRecvProvinceCode());
        payLog.setRecvEparchyCode(feePayLog.getRecvEparchyCode());
        payLog.setRecvCityCode(feePayLog.getCityCode());
        payLog.setRecvDepartId(feePayLog.getRecvDepartId());
        payLog.setRecvStaffId(feePayLog.getRecvStaffId());
        payLog.setCancelTag(feePayLog.getCancelTag());
        payLog.setRemark(StringUtil.isEmptyCheckNullStr(feePayLog.getRemark())?null : feePayLog.getRemark());
        payLog.setPaymentRuleId(feePayLog.getPaymentRuleId());
        payLog.setActionCode(feePayLog.getActionCode());
        payLog.setPaymentReasonCode(feePayLog.getPaymentReasonCode());
        payLog.setActionEventId(feePayLog.getActionEventId());
        payLog.setNpTag(feePayLog.getNpTag());
        if (payLogDao.insertPayLog(payLog, provinceCode) == 0) {
            throw new SkyArkException("新增缴费日志失败！chargeId=" + payLog.getChargeId());
        }
    }

    @Override
    public void insertCLPayLog(List<FeeCLPayLog> feeCLPayLogs, String provinceCode) {
        if (CollectionUtils.isEmpty(feeCLPayLogs)) {
            return;
        }
        List<CLPayLog> clPayLogs = new ArrayList(feeCLPayLogs);
        for (FeeCLPayLog feeCLPayLog : feeCLPayLogs) {
            CLPayLog clPayLog = new CLPayLog();
            clPayLog.setClPaylogId(feeCLPayLog.getClPaylogId());
            clPayLog.setProvinceCode(feeCLPayLog.getProvinceCode());
            clPayLog.setEparchyCode(feeCLPayLog.getEparchyCode());
            clPayLog.setAreaCode(feeCLPayLog.getAreaCode());
            clPayLog.setNetTypeCode(feeCLPayLog.getNetTypeCode());
            clPayLog.setAcctId(feeCLPayLog.getAcctId());
            clPayLog.setUserId(feeCLPayLog.getUserId());
            clPayLog.setOldAcctId(feeCLPayLog.getOldAcctId());
            clPayLog.setOldUserId(feeCLPayLog.getOldUserId());
            clPayLog.setSerialNumber(feeCLPayLog.getSerialNumber());
            clPayLog.setPaymentId(feeCLPayLog.getPaymentId());
            clPayLog.setRecvFee(feeCLPayLog.getRecvFee());
            clPayLog.setChargeId(feeCLPayLog.getChargeId());
            clPayLog.setOuterTradeId(feeCLPayLog.getOuterTradeId());
            clPayLog.setRecvTime(feeCLPayLog.getRecvTime());
            clPayLog.setRecvStaffId(feeCLPayLog.getRecvStaffId());
            clPayLog.setRecvDepartId(feeCLPayLog.getRecvDepartId());
            clPayLog.setRecvEparchyCode(feeCLPayLog.getRecvEparchyCode());
            clPayLog.setRecvCityCode(feeCLPayLog.getRecvCityCode());
            clPayLogs.add(clPayLog);
        }
        payLogDao.insertCLPayLog(clPayLogs, provinceCode);
    }

    @Override
    public long insertPayOtherLog(PayOtherLog payOtherLog, String provinceCode) {
        return payLogDao.insertPayOtherLog(payOtherLog, provinceCode);
    }

    @Override
    public List<FeeWriteOffLog> genWriteOffLogInfo(List<FeeWriteOffLog> writeOffLogs, FeePayLog payLog, WriteOffRuleInfo writeOffRuleInfo) {
        if (CollectionUtils.isEmpty(writeOffLogs)) {
            return null;
        }

        List<FeeWriteOffLog> writeOffLogsIndb = new ArrayList<>();
        //剔除实时账单销账日志
        for (FeeWriteOffLog writeOffLog : writeOffLogs) {
            if ('2' != writeOffLog.getCanPaytag()) {
                writeOffLogsIndb.add(writeOffLog);
            }
        }
        if (CollectionUtils.isEmpty(writeOffLogsIndb)) {
            return writeOffLogsIndb;
        }
        //生成销账日志流水
        List<String> sequences = sysCommOperFeeService.getSequence(payLog.getEparchyCode(),
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
    public void insertWriteOffLog(List<FeeWriteOffLog> feeWriteOffLogs, String provinceCode) {
        if (CollectionUtils.isEmpty(feeWriteOffLogs)){
            return;
        }
        List<WriteOffLog> writeOffLogs = new ArrayList(feeWriteOffLogs.size());
        for (FeeWriteOffLog feeWriteOffLog : feeWriteOffLogs) {
            WriteOffLog writeOffLog = new WriteOffLog();
            writeOffLog.setWriteoffId(feeWriteOffLog.getWriteoffId());
            writeOffLog.setChargeId(feeWriteOffLog.getChargeId());
            writeOffLog.setAcctId(feeWriteOffLog.getAcctId());
            writeOffLog.setUserId(feeWriteOffLog.getUserId());
            writeOffLog.setCycleId(feeWriteOffLog.getCycleId());
            writeOffLog.setNetTypeCode(feeWriteOffLog.getNetTypeCode());
            writeOffLog.setBillId(feeWriteOffLog.getBillId());
            writeOffLog.setIntegrateItemCode(feeWriteOffLog.getIntegrateItemCode());
            writeOffLog.setDepositCode(feeWriteOffLog.getDepositCode());
            writeOffLog.setAcctBalanceId(feeWriteOffLog.getAcctBalanceId());
            writeOffLog.setWriteoffFee(feeWriteOffLog.getWriteoffFee());
            writeOffLog.setImpFee(feeWriteOffLog.getImpFee());
            writeOffLog.setFee(feeWriteOffLog.getFee());
            writeOffLog.setOldBalance(feeWriteOffLog.getOldBalance());
            writeOffLog.setNewBalance(feeWriteOffLog.getNewBalance());
            writeOffLog.setLateFee(feeWriteOffLog.getLateFee());
            writeOffLog.setLateBalance(feeWriteOffLog.getLateBalance());
            writeOffLog.setOldLateBalance(feeWriteOffLog.getOldLateBalance());
            writeOffLog.setNewLateBalance(feeWriteOffLog.getNewLateBalance());
            writeOffLog.setDerateLateFee(feeWriteOffLog.getDerateLateFee());
            writeOffLog.setLatecalDate(feeWriteOffLog.getLatecalDate());
            writeOffLog.setOldPaytag(feeWriteOffLog.getOldPaytag());
            writeOffLog.setNewPaytag(feeWriteOffLog.getNewPaytag());
            writeOffLog.setCanPaytag(feeWriteOffLog.getCanPaytag());
            writeOffLog.setOperateTime(feeWriteOffLog.getOperateTime());
            writeOffLog.setProvinceCode(feeWriteOffLog.getProvinceCode());
            writeOffLog.setEparchyCode(feeWriteOffLog.getEparchyCode());
            writeOffLog.setDrecvTimes(feeWriteOffLog.getDrecvTimes());
            writeOffLog.setCancelTag(feeWriteOffLog.getCancelTag());
            writeOffLog.setDepositLimitRuleid(feeWriteOffLog.getDepositLimitRuleid());
            writeOffLog.setDepositPriorRuleid(feeWriteOffLog.getDepositPriorRuleid());
            writeOffLog.setItemPriorRuleid(feeWriteOffLog.getItemPriorRuleid());
            writeOffLogs.add(writeOffLog);
        }
        writeOffLogDao.insertWriteOffLog(writeOffLogs, provinceCode);
    }

    @Override
    public List<FeeAccessLog> genAccessLogInfo(List<FeeAccessLog> feeAccessLogs, FeePayLog feePayLog, boolean existsWriteOffLog) {
        if (CollectionUtils.isEmpty(feeAccessLogs)) {
            throw new SkyArkException("本次交易没有生成存取款日志");
        }

        //没有往月账单的销账记录,就排除0取款记录
        if (existsWriteOffLog) {
            List<FeeAccessLog> tmpAccessLogs = new ArrayList();
            for (FeeAccessLog accessLog : feeAccessLogs) {
                if (0 != accessLog.getMoney() || '0' == accessLog.getAccessTag()) {
                    tmpAccessLogs.add(accessLog);
                }
            }
            feeAccessLogs = tmpAccessLogs;
        }

        if (CollectionUtils.isEmpty(feeAccessLogs)) {
            throw new SkyArkException("本次交易没有生成存取款日志");
        }
        logger.debug("feeAccessLogs.size = " + feeAccessLogs.size());
        //存款日志流水
        List<String> sequences = sysCommOperFeeService.getSequence(feePayLog.getRecvEparchyCode(),
                ActPayPubDef.SEQ_ACCESS_ID, feeAccessLogs.size(), feePayLog.getProvinceCode());
        List<FeeAccessLog> tmpAccessLogs = new ArrayList();
        for (int i = 0; i < feeAccessLogs.size(); i++) {
            feeAccessLogs.get(i).setAccessId(sequences.get(i));
            feeAccessLogs.get(i).setChargeId(feePayLog.getChargeId());
            feeAccessLogs.get(i).setOperateTime(feePayLog.getRecvTime());
            tmpAccessLogs.add(feeAccessLogs.get(i));
        }
        return tmpAccessLogs;
    }

    @Override
    public void insertAccessLog(List<FeeAccessLog> feeAccessLogs, String provinceCode) {
        if (CollectionUtils.isEmpty(feeAccessLogs)) {
            return;
        }
        List<AccessLog> accessLogs = new ArrayList(feeAccessLogs.size());
        for (FeeAccessLog feeAccessLog : feeAccessLogs) {
            AccessLog accessLog = new AccessLog();
            accessLog.setAccessId(feeAccessLog.getAccessId ());
            accessLog.setChargeId(feeAccessLog.getChargeId());
            accessLog.setAcctId(feeAccessLog.getAcctId());
            accessLog.setAcctBalanceId(feeAccessLog.getAcctBalanceId());
            accessLog.setDepositCode(feeAccessLog.getDepositCode());
            accessLog.setAccessTag(feeAccessLog.getAccessTag());
            accessLog.setOldBalance(feeAccessLog.getOldBalance());
            accessLog.setMoney(feeAccessLog.getMoney());
            accessLog.setNewBalance(feeAccessLog.getNewBalance());
            accessLog.setOperateTime(feeAccessLog.getOperateTime());
            accessLog.setEparchyCode(feeAccessLog.getEparchyCode());
            accessLog.setProvinceCode(feeAccessLog.getProvinceCode());
            accessLog.setCancelTag(feeAccessLog.getCancelTag());
            accessLog.setInvoiceFee(feeAccessLog.getInvoiceFee());
            accessLogs.add(accessLog);
        }
        accessLogDao.insertAccessLog(accessLogs, provinceCode);
    }

    @Override
    public void insertWriteSnapLog(FeeWriteSnapLog feeWriteSnapLog, String provinceCode) {
        WriteSnapLog writeSnapLog = new WriteSnapLog();
        writeSnapLog.setChargeId(feeWriteSnapLog.getChargeId());
        writeSnapLog.setAcctId(feeWriteSnapLog.getAcctId());
        writeSnapLog.setWriteoffMode(feeWriteSnapLog.getWriteoffMode());
        writeSnapLog.setSpayFee(feeWriteSnapLog.getSpayFee());
        writeSnapLog.setAllMoney(feeWriteSnapLog.getAllMoney());
        writeSnapLog.setAllNewMoney(feeWriteSnapLog.getAllNewMoney());
        writeSnapLog.setAllBalance(feeWriteSnapLog.getAllBalance());
        writeSnapLog.setAllNewBalance(feeWriteSnapLog.getAllNewBalance());
        writeSnapLog.setAllBOweFee(feeWriteSnapLog.getAllBOweFee());
        writeSnapLog.setaImpFee(feeWriteSnapLog.getaImpFee());
        writeSnapLog.setAllNewBOweFee(feeWriteSnapLog.getAllNewBOweFee());
        writeSnapLog.setPreRealFee(feeWriteSnapLog.getPreRealFee());
        writeSnapLog.setCurRealFee(feeWriteSnapLog.getCurRealFee());
        writeSnapLog.setProtocolBalance(feeWriteSnapLog.getProtocolBalance());
        writeSnapLog.setOperateTime(feeWriteSnapLog.getOperateTime());
        writeSnapLog.setEparchyCode(feeWriteSnapLog.getEparchyCode());
        writeSnapLog.setProvinceCode(feeWriteSnapLog.getProvinceCode());
        writeSnapLog.setCycleId(feeWriteSnapLog.getCycleId());
        writeSnapLog.setRemark(feeWriteSnapLog.getRemark());
        writeSnapLog.setRsrvFee1(feeWriteSnapLog.getRsrvFee1());
        writeSnapLog.setRsrvFee2(feeWriteSnapLog.getRsrvFee2());
        writeSnapLog.setRsrvInfo1(feeWriteSnapLog.getRsrvInfo1());
        if (writeSnapLogDao.insertWriteSnapLog(writeSnapLog, provinceCode) == 0) {
            throw new SkyArkException("插入快照日志表失败!chargeId=" + writeSnapLog.getChargeId());
        }
    }

    @Override
    public PayLogMQInfo genPayLogMQInfo(FeePayLog feePaylog) {
        PayLogMQInfo payLogMQInfo = new PayLogMQInfo();
        payLogMQInfo.setChargeId(feePaylog.getChargeId());
        payLogMQInfo.setEparchyCode(feePaylog.getEparchyCode());
        payLogMQInfo.setCityCode(feePaylog.getCityCode());
        payLogMQInfo.setCustId(feePaylog.getCustId());
        payLogMQInfo.setUserId(feePaylog.getUserId());
        payLogMQInfo.setSerialNumber(feePaylog.getSerialNumber());
        payLogMQInfo.setNetTypeCode(feePaylog.getNetTypeCode());
        payLogMQInfo.setAcctId(feePaylog.getAcctId());
        payLogMQInfo.setChannelId(feePaylog.getChannelId());
        payLogMQInfo.setPaymentId(feePaylog.getPaymentId());
        payLogMQInfo.setPayFeeModeCode(feePaylog.getPayFeeModeCode());
        payLogMQInfo.setPaymentOp(feePaylog.getPaymentOp());
        payLogMQInfo.setRecvFee(feePaylog.getRecvFee());
        payLogMQInfo.setLimitMoney(feePaylog.getLimitMoney());
        payLogMQInfo.setRecvTime(feePaylog.getRecvTime());
        payLogMQInfo.setRecvProvinceCode(feePaylog.getRecvProvinceCode());
        payLogMQInfo.setRecvEparchyCode(feePaylog.getRecvEparchyCode());
        payLogMQInfo.setRecvCityCode(feePaylog.getRecvCityCode());
        payLogMQInfo.setRecvDepartId(feePaylog.getRecvDepartId());
        payLogMQInfo.setRecvStaffId(feePaylog.getRecvStaffId());
        payLogMQInfo.setPaymentReasonCode(feePaylog.getPaymentReasonCode());
        payLogMQInfo.setInputMode(feePaylog.getInputMode());
        payLogMQInfo.setInputNo(feePaylog.getInputNo());
        payLogMQInfo.setOuterTradeId(feePaylog.getOuterTradeId());
        payLogMQInfo.setActionCode(feePaylog.getActionCode());
        payLogMQInfo.setActionEventId(feePaylog.getActionEventId());
        payLogMQInfo.setActTag(feePaylog.getActTag());
        payLogMQInfo.setExtendTag(feePaylog.getActTag());
        payLogMQInfo.setPaymentRuleId(feePaylog.getPaymentRuleId());
        payLogMQInfo.setRemark(feePaylog.getRemark());
        payLogMQInfo.setProvinceCode(feePaylog.getProvinceCode());
        payLogMQInfo.setCancelStaffId(feePaylog.getCancelStaffId());
        payLogMQInfo.setCancelDepartId(feePaylog.getCancelDepartId());
        payLogMQInfo.setCancelCityCode(feePaylog.getCancelCityCode());
        payLogMQInfo.setCancelEparchyCode(feePaylog.getCancelEparchyCode());
        payLogMQInfo.setCancelChargeId(feePaylog.getCancelChargeId());
        payLogMQInfo.setCancelTime(feePaylog.getCancelTime());
        payLogMQInfo.setRsrvFee1(feePaylog.getRsrvFee1());
        payLogMQInfo.setRsrvFee2(feePaylog.getRsrvFee2());
        payLogMQInfo.setRsrvInfo1(feePaylog.getRsrvInfo1());
        payLogMQInfo.setDevCode(feePaylog.getDevCode());
        payLogMQInfo.setNpTag(feePaylog.getNpTag());
        payLogMQInfo.setAgentTag(feePaylog.getAgentTag());
        payLogMQInfo.setContractTag(feePaylog.getContractTag());
        return payLogMQInfo;
    }

    @Override
    public List<AccessLogMQInfo> genAccessLogMQInfo(List<FeeAccessLog> feeAccessLogs) {
        List<AccessLogMQInfo> accessLogMQInfos = new ArrayList(feeAccessLogs.size());
        for (FeeAccessLog feeAccessLog :  feeAccessLogs) {
            AccessLogMQInfo accessLogMQInfo = new AccessLogMQInfo();
            accessLogMQInfo.setAccessId(feeAccessLog.getAccessId());
            accessLogMQInfo.setAcctId(feeAccessLog.getAcctId());
            accessLogMQInfo.setChargeId(feeAccessLog.getChargeId());
            accessLogMQInfo.setAcctBalanceId(feeAccessLog.getAcctBalanceId());
            accessLogMQInfo.setDepositCode(feeAccessLog.getDepositCode());
            accessLogMQInfo.setOldBalance(feeAccessLog.getOldBalance());
            accessLogMQInfo.setMoney(feeAccessLog.getMoney());
            accessLogMQInfo.setNewBalance(feeAccessLog.getNewBalance());
            accessLogMQInfo.setAccessTag(feeAccessLog.getAccessTag());
            accessLogMQInfo.setOperateTime(feeAccessLog.getOperateTime());
            accessLogMQInfo.setEparchyCode(feeAccessLog.getEparchyCode());
            accessLogMQInfo.setCancelTag(feeAccessLog.getCancelTag());
            accessLogMQInfo.setInvoiceFee(feeAccessLog.getInvoiceFee());
            accessLogMQInfo.setProvinceCode(feeAccessLog.getProvinceCode());
            accessLogMQInfos.add(accessLogMQInfo);
        }
        return accessLogMQInfos;
    }
}
