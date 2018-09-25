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
    public void tradeFeeCommInDB(TradeCommResultInfo tradeCommResultInfo) {
        logger.info("账务费用交易公共日志入库开始");
        //缴费日志入库
        payLogDao.insertPayLog(tradeCommResultInfo.getPayLog());
        //省份代收日志入库
        if (!CollectionUtils.isEmpty(tradeCommResultInfo.getClPayLogs())) {
            payLogDao.insertCLPayLog(tradeCommResultInfo.getClPayLogs());
        }
        //存取款日志
        if (!CollectionUtils.isEmpty(tradeCommResultInfo.getAccessLogs())) {
            accessLogDao.insertAccessLog(tradeCommResultInfo.getAccessLogs());
        }
        //销账日志
        if (!CollectionUtils.isEmpty(tradeCommResultInfo.getWriteOffLogs())) {
            writeOffLogDao.insertWriteOffLog(tradeCommResultInfo.getWriteOffLogs());
        }
        //销账快照
        writeSnapLogDao.insertWriteSnapLog(tradeCommResultInfo.getWriteSnapLog());
        logger.info("账务费用交易公共日志入库结束");
    }

    @Override
    public void insertPayLog(PayLog payLog) {
        try {
            if (payLogDao.insertPayLog(payLog) == 0) {
                throw new SkyArkException("新增缴费日志失败！chargeId=" + payLog.getChargeId());
            }
        } catch (Exception ex) {
            throw new SkyArkException("新增缴费日志失败！chargeId=" + payLog.getChargeId() + ",ExceptionInfo = " + ex.getMessage());
        }

    }

    @Override
    public void insertCLPayLog(List<CLPayLog> clPayLogs) {
        if (CollectionUtils.isEmpty(clPayLogs)) {
            return;
        }
        try {
            payLogDao.insertCLPayLog(clPayLogs);
        } catch (Exception ex) {
            throw new SkyArkException("新增省份代收费日志失败！chargeId=" + clPayLogs.get(0).getChargeId() + ",ExceptionInfo = " + ex.getMessage());
        }

    }

    @Override
    public void insertPayOtherLog(PayOtherLog payOtherLog) {
        try {
            if (payLogDao.insertPayOtherLog(payOtherLog) == 0) {
                throw new SkyArkException("新增缴费其他日志失败！chargeId=" + payOtherLog.getChargeId());
            }
        } catch (Exception ex) {
            throw new SkyArkException("新增缴费其他日志失败！chargeId=" + payOtherLog.getChargeId() + ",ExceptionInfo = " + ex.getMessage());
        }
    }

    @Override
    public void insertChargerelation(ChargeRelation chargeRelation) {
        try {
            if (payLogDao.insertChargerelation(chargeRelation) == 0) {
                throw new SkyArkException("新增交易关联日志失败！chargeId=" + chargeRelation.getId());
            }
        } catch (Exception ex) {
            throw new SkyArkException("新增交易关联日志失败！chargeId=" + chargeRelation.getId() + ",ExceptionInfo = " + ex.getMessage());
        }
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
        List<String> sequences = sysCommOperFeeService.getActingSequence(ActingPayPubDef.SEQ_WRITEOFFID_TABNAME,
                ActingPayPubDef.SEQ_WRITEOFFID_COLUMNNAME, writeOffLogsIndb.size(), payLog.getProvinceCode());

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
    public void insertWriteOffLog(List<WriteOffLog> writeOffLogs) {
        if (CollectionUtils.isEmpty(writeOffLogs)) {
            return;
        }
        try {
            writeOffLogDao.insertWriteOffLog(writeOffLogs);
        } catch (Exception ex) {
            throw new SkyArkException("新增销账日志失败！chargeId=" + writeOffLogs.get(0).getChargeId() + ",ExceptionInfo = " + ex.getMessage());
        }
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
        List<String> sequences = sysCommOperFeeService.getActingSequence(ActingPayPubDef.SEQ_ACCESSID_TABNAME,
                ActingPayPubDef.SEQ_ACCESSID_COLUMNNAME, feeAccessLogs.size(), feePayLog.getProvinceCode());
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
    public void insertAccessLog(List<AccessLog> accessLogs) {
        if (CollectionUtils.isEmpty(accessLogs)) {
            return;
        }
        try {
            accessLogDao.insertAccessLog(accessLogs);
        } catch (Exception ex) {
            throw new SkyArkException("新增销账日志失败！chargeId=" + accessLogs.get(0).getChargeId() + ",ExceptionInfo = " + ex.getMessage());
        }
    }

    @Override
    public void insertWriteSnapLog(WriteSnapLog writeSnapLog) {
        try {
            if (writeSnapLogDao.insertWriteSnapLog(writeSnapLog) == 0) {
                throw new SkyArkException("插入快照日志表失败!chargeId=" + writeSnapLog.getChargeId());
            }
        } catch (Exception ex) {
            throw new SkyArkException("新增销账日志失败！chargeId=" + writeSnapLog.getChargeId() + ",ExceptionInfo = " + ex.getMessage());
        }

    }

    @Override
    public PayLogMQInfo genPayLogMQInfo(PayLog payLog) {
        PayLogMQInfo payLogMQInfo = new PayLogMQInfo();
        payLogMQInfo.setChargeId(payLog.getChargeId());
        payLogMQInfo.setEparchyCode(payLog.getEparchyCode());
        payLogMQInfo.setCityCode(payLog.getCityCode());
        payLogMQInfo.setCustId(payLog.getCustId());
        payLogMQInfo.setUserId(payLog.getUserId());
        payLogMQInfo.setSerialNumber(payLog.getSerialNumber());
        payLogMQInfo.setNetTypeCode(payLog.getNetTypeCode());
        payLogMQInfo.setAcctId(payLog.getAcctId());
        payLogMQInfo.setChannelId(payLog.getChannelId());
        payLogMQInfo.setPaymentId(payLog.getPaymentId());
        payLogMQInfo.setPayFeeModeCode(payLog.getPayFeeModeCode());
        payLogMQInfo.setPaymentOp(payLog.getPaymentOp());
        payLogMQInfo.setRecvFee(payLog.getRecvFee());
        payLogMQInfo.setLimitMoney(payLog.getLimitMoney());
        payLogMQInfo.setRecvTime(payLog.getRecvTime());
        payLogMQInfo.setRecvProvinceCode(payLog.getRecvProvinceCode());
        payLogMQInfo.setRecvEparchyCode(payLog.getRecvEparchyCode());
        payLogMQInfo.setRecvCityCode(payLog.getRecvCityCode());
        payLogMQInfo.setRecvDepartId(payLog.getRecvDepartId());
        payLogMQInfo.setRecvStaffId(payLog.getRecvStaffId());
        payLogMQInfo.setPaymentReasonCode(payLog.getPaymentReasonCode());
        payLogMQInfo.setInputMode(payLog.getInputMode());
        payLogMQInfo.setInputNo(payLog.getInputNo());
        payLogMQInfo.setOuterTradeId(payLog.getOuterTradeId());
        payLogMQInfo.setActionCode(payLog.getActionCode());
        payLogMQInfo.setActionEventId(payLog.getActionEventId());
        payLogMQInfo.setActTag(payLog.getActTag());
        payLogMQInfo.setExtendTag(payLog.getActTag());
        payLogMQInfo.setPaymentRuleId(payLog.getPaymentRuleId());
        payLogMQInfo.setRemark(payLog.getRemark());
        payLogMQInfo.setProvinceCode(payLog.getProvinceCode());
        payLogMQInfo.setCancelStaffId(payLog.getCancelStaffId());
        payLogMQInfo.setCancelDepartId(payLog.getCancelDepartId());
        payLogMQInfo.setCancelCityCode(payLog.getCancelCityCode());
        payLogMQInfo.setCancelEparchyCode(payLog.getCancelEparchyCode());
        payLogMQInfo.setCancelChargeId(payLog.getCancelChargeId());
        payLogMQInfo.setCancelTime(payLog.getCancelTime());
        payLogMQInfo.setRsrvFee1(payLog.getRsrvFee1());
        payLogMQInfo.setRsrvFee2(payLog.getRsrvFee2());
        payLogMQInfo.setRsrvInfo1(payLog.getRsrvInfo1());
        payLogMQInfo.setDevCode(payLog.getDevCode());
        payLogMQInfo.setNpTag(payLog.getNpTag());
        payLogMQInfo.setAgentTag(payLog.getAgentTag());
        payLogMQInfo.setContractTag(payLog.getContractTag());
        return payLogMQInfo;
    }

    @Override
    public List<AccessLogMQInfo> genAccessLogMQInfo(List<AccessLog> accessLogs) {
        List<AccessLogMQInfo> accessLogMQInfos = new ArrayList(accessLogs.size());
        for (AccessLog accessLog : accessLogs) {
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
            accessLogMQInfos.add(accessLogMQInfo);
        }
        return accessLogMQInfos;
    }
}
