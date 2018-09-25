package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.*;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.writeoff.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 销账相关日志对象和MQ消息生成公共方法
 *
 * @author wangkh
 */
@Service
public class TradeCommServiceImpl implements TradeCommService {
    private static final Logger logger = LoggerFactory.getLogger(TradeCommServiceImpl.class);
    @Autowired
    private AcctDepositPayService acctDepositPayService;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;
    @Autowired
    private CommParaFeeService commParaFeeService;
    @Autowired
    private CLPayLogService clPayLogService;

    @Override
    public void genInDBInfo(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        //地市销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        //生成交费日志信息
        PayLog payLog = genPayLog(tradeCommInfoIn, tradeCommInfo);
        tradeCommResultInfo.setPayLog(payLog);

        //生成缴费日志MQ消息
        tradeCommResultInfo.setPayLogMQInfo(genPayLogMQInfo(payLog));

        //更新交费快照信息
        WriteSnapLog writeSnapLog = genWriteSnapLog(tradeCommInfoIn,
                tradeCommInfo.getFeeWriteSnapLog(), payLog, writeOffRuleInfo.getCurCycle().getCycleId());
        tradeCommResultInfo.setWriteSnapLog(writeSnapLog);

        //生成销账日志数据
        List<WriteOffLog> writeOffLogs = genWriteOffLog(tradeCommInfo.getFeeWriteOffLogs(), payLog, writeOffRuleInfo);
        //销账日志入库
        tradeCommResultInfo.setWriteOffLogs(writeOffLogs);


        //生成取款日志数据
        List<AccessLog> accessLogs = genAccessLog(tradeCommInfo, payLog, writeOffLogs);
        tradeCommResultInfo.setAccessLogs(accessLogs);

        //生成取款日志MQ信息
        if (!CollectionUtils.isEmpty(accessLogs)) {
            tradeCommResultInfo.setAccessLogMQInfos(genAccessLogMQInfo(accessLogs));
        }

        //生成代收费日志
        List<CLPayLog> clPayLogs = genCLPaylog(writeOffLogs, payLog, tradeCommInfoIn.getHeaderGray());
        tradeCommResultInfo.setClPayLogs(clPayLogs);

        //更新存折入库信息
        List<FeeAccountDeposit> deposits = tradeCommInfo.getFeeAccountDeposits();
        acctDepositPayService.updateDepositInfo(deposits, writeSnapLog.getAllNewBOweFee(),
                writeOffRuleInfo.getSysdate(), writeOffRuleInfo.getMaxAcctCycle().getCycleId());
        tradeCommResultInfo.setDepositMQInfos(acctDepositPayService.genDepositMQInfo(deposits));
    }

    /**
     * 账务交易日志公共信息
     *
     * @param tradeCommInfoIn
     * @param tradeCommInfo
     * @return
     */
    @Override
    public PayLog genPayLog(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        PayLog payLog = new PayLog();
        Account account = tradeCommInfo.getAccount();
        User mainUser = tradeCommInfo.getMainUser();
        Staff staff = tradeCommInfo.getTradeStaff();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        payLog.setProvinceCode(account.getProvinceCode());
        payLog.setEparchyCode(account.getEparchyCode());
        payLog.setCityCode(account.getCityCode());
        payLog.setCustId(account.getCustId());
        payLog.setAcctId(account.getAcctId());
        payLog.setUserId(mainUser.getUserId());
        payLog.setSerialNumber(mainUser.getSerialNumber());
        payLog.setNetTypeCode(mainUser.getNetTypeCode());
        //帐户缴费，没有输入用户信息，需要填写帐户的网别
        if (StringUtil.isEmptyCheckNullStr(payLog.getNetTypeCode())) {
            payLog.setNetTypeCode(account.getNetTypeCode());
        }
        if (StringUtil.isEmptyCheckNullStr(payLog.getUserId())) {
            payLog.setUserId("-1");
        }
        if (StringUtil.isEmptyCheckNullStr(payLog.getSerialNumber())) {
            payLog.setSerialNumber("-1");
        }
        //外围没有传入交费流水，系统会重新生成
        if (StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getChargeId())) {
            String tmpChargeId = sysCommOperFeeService.getActingSequence(ActingPayPubDef.SEQ_CHARGEID_TANNAME,
                    ActingPayPubDef.SEQ_CHARGEID_COLUMNNAME, payLog.getProvinceCode());
            tradeCommInfoIn.setChargeId(tmpChargeId);
        }

        //异地缴费参数校验,默认不是异地缴费
        boolean isExtendFee = false;
        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_NONLOCAL_RECVFEE);
        if (commPara != null && "1".equals(commPara.getParaCode1())) {
            String channelPayment = "|" + tradeCommInfoIn.getChannelId() + ":" + tradeCommInfoIn.getPaymentId() + "|";
            if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode2())
                    && commPara.getParaCode2().contains(channelPayment)) {
                isExtendFee = true;
            }
        }

        if (isExtendFee
                && isEffectiveEparchy(staff.getEparchyCode())
                && isEffectiveEparchy(payLog.getEparchyCode())
                && !payLog.getEparchyCode().equals(staff.getEparchyCode())) {
            //以工号归属地市编码查询工号归属省份，外围传递的省份编码可能不是工号归属省份编码
            String provinceCode = commParaFeeService.getProvCodeByEparchyCode(
                    staff.getEparchyCode());
            if (StringUtil.isEmptyCheckNullStr(provinceCode)) {
                throw new SkyArkException("未找到地市编码对应的省份编码!, TRADE_EPARCHY_CODE = " + staff.getEparchyCode());
            }
            staff.setProvinceCode(provinceCode);
            if (!payLog.getProvinceCode().equals(staff.getProvinceCode())) {
                //跨省异地缴费
                payLog.setExtendTag('2');
            } else {
                //本省异地缴费
                payLog.setExtendTag('1');
            }
        } else {
            //非异地缴费
            payLog.setExtendTag('0');
        }

        payLog.setChargeId(tradeCommInfoIn.getChargeId());
        payLog.setOuterTradeId(tradeCommInfoIn.getTradeId());
        payLog.setPaymentId(tradeCommInfoIn.getPaymentId());
        payLog.setPaymentOp(tradeCommInfoIn.getPaymentOp());
        payLog.setPayFeeModeCode(tradeCommInfoIn.getPayFeeModeCode());
        payLog.setRecvTime(writeOffRuleInfo.getSysdate());
        payLog.setRecvFee(tradeCommInfoIn.getTradeFee());
        payLog.setRecvProvinceCode(staff.getProvinceCode());
        payLog.setRecvEparchyCode(staff.getEparchyCode());
        payLog.setRecvCityCode(staff.getCityCode());
        payLog.setRecvDepartId(staff.getDepartId());
        payLog.setRecvStaffId(staff.getStaffId());
        payLog.setCancelTag('0');

        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getRemark())) {
            if (tradeCommInfoIn.getRemark().length() > 150) {
                payLog.setRemark(tradeCommInfoIn.getRemark().substring(0, 149));
            } else {
                payLog.setRemark(tradeCommInfoIn.getRemark());
            }
        }

        payLog.setPaymentRuleId(writeOffRuleInfo.getPaymentDepositRuleId());
        payLog.setActionCode(0);
        payLog.setPaymentReasonCode(0);
        payLog.setChannelId(tradeCommInfoIn.getChannelId());
        payLog.setLimitMoney(tradeCommInfoIn.getLimitMoney());
        payLog.setActionEventId(null);
        return payLog;
    }

    /**
     * 校验地市编码是否有效
     *
     * @param eparchyCode
     * @return
     */
    private boolean isEffectiveEparchy(String eparchyCode) {
        if (StringUtil.isEmptyCheckNullStr(eparchyCode)
                || 4 != eparchyCode.length()
                || ActingPayPubDef.DEFAULT_EPARCHY_CODE.equalsIgnoreCase(eparchyCode)) {
            return false;
        }
        return true;
    }

    @Override
    public WriteSnapLog genWriteSnapLog(TradeCommInfoIn tradeCommInfoIn, FeeWriteSnapLog feeWriteSnapLog, PayLog payLog, int CycleId) {
        WriteSnapLog writeSnapLog = new WriteSnapLog();
        writeSnapLog.setChargeId(payLog.getChargeId());
        writeSnapLog.setAcctId(feeWriteSnapLog.getAcctId());
        writeSnapLog.setWriteoffMode(StringUtil.firstOfString(tradeCommInfoIn.getWriteoffMode()));
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
        writeSnapLog.setRecoverTag(StringUtil.firstOfString(tradeCommInfoIn.getRecoverTag()));
        writeSnapLog.setOperateTime(payLog.getRecvTime());
        writeSnapLog.setEparchyCode(feeWriteSnapLog.getEparchyCode());
        writeSnapLog.setProvinceCode(feeWriteSnapLog.getProvinceCode());
        if (tradeCommInfoIn.getBillEndCycleId() == ActingPayPubDef.MAX_CYCLE_ID) {
            writeSnapLog.setCycleId(CycleId);
        } else {
            writeSnapLog.setCycleId(tradeCommInfoIn.getBillEndCycleId());
        }
        writeSnapLog.setRemark(feeWriteSnapLog.getRemark());
        writeSnapLog.setRsrvFee1(feeWriteSnapLog.getRsrvFee1());
        writeSnapLog.setRsrvFee2(feeWriteSnapLog.getRsrvFee2());
        writeSnapLog.setRsrvInfo1(feeWriteSnapLog.getRsrvInfo1());
        return writeSnapLog;
    }

    @Override
    public List<WriteOffLog> genWriteOffLog(List<FeeWriteOffLog> feeWriteOffLogs, PayLog payLog, WriteOffRuleInfo writeOffRuleInfo) {
        if (CollectionUtils.isEmpty(feeWriteOffLogs)) {
            return null;
        }

        //剔除实时账单销账日志
        List<FeeWriteOffLog> writeOffLogsIndb = new ArrayList();
        for (FeeWriteOffLog writeOffLog : feeWriteOffLogs) {
            if ('2' != writeOffLog.getCanPaytag()) {
                writeOffLogsIndb.add(writeOffLog);
            }
        }
        if (CollectionUtils.isEmpty(writeOffLogsIndb)) {
            return null;
        }

        //生成销账日志流水
        List<String> sequences = sysCommOperFeeService.getActingSequence(ActingPayPubDef.SEQ_WRITEOFFID_TABNAME,
                ActingPayPubDef.SEQ_WRITEOFFID_COLUMNNAME, writeOffLogsIndb.size(), payLog.getProvinceCode());

        //生成入库销账日志信息
        List<WriteOffLog> writeOffLogs = new ArrayList(writeOffLogsIndb.size());
        int index = 0;
        for (FeeWriteOffLog feeWriteOffLog : writeOffLogsIndb) {
            WriteOffLog writeOffLog = new WriteOffLog();
            writeOffLog.setWriteoffId(sequences.get(index));
            writeOffLog.setChargeId(payLog.getChargeId());
            writeOffLog.setAcctId(feeWriteOffLog.getAcctId());
            writeOffLog.setUserId(feeWriteOffLog.getUserId());
            writeOffLog.setCycleId(feeWriteOffLog.getCycleId());

            if ("".equals(feeWriteOffLog.getNetTypeCode())) {
                writeOffLog.setNetTypeCode("**");
            } else {
                writeOffLog.setNetTypeCode(feeWriteOffLog.getNetTypeCode());
            }

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
            writeOffLog.setOperateTime(payLog.getRecvTime());
            writeOffLog.setProvinceCode(feeWriteOffLog.getProvinceCode());
            writeOffLog.setEparchyCode(feeWriteOffLog.getEparchyCode());
            writeOffLog.setDrecvTimes(feeWriteOffLog.getDrecvTimes());
            writeOffLog.setCancelTag(feeWriteOffLog.getCancelTag());
            writeOffLog.setDepositLimitRuleid(writeOffRuleInfo.getDepositLimitRuleId());
            writeOffLog.setDepositPriorRuleid(writeOffRuleInfo.getDepositPriorRuleId());
            writeOffLog.setItemPriorRuleid(writeOffRuleInfo.getItemPriorRuleId());
            writeOffLogs.add(writeOffLog);
            index++;
        }
        return writeOffLogs;
    }

    @Override
    public List<AccessLog> genAccessLog(TradeCommInfo tradeCommInfo, PayLog payLog, List<WriteOffLog> writeOffLogs) {
        Account account = tradeCommInfo.getAccount();
        List<FeeAccountDeposit> depositList = tradeCommInfo.getFeeAccountDeposits();
        List<AccessLog> accessLogs = new ArrayList();
        //发票可打金额
        Map<String, Long> invoiceFee = tradeCommInfo.getInvoiceFeeMap();

        for (FeeAccountDeposit deposit : depositList) {
            if (deposit.getRecvFee() != 0 || deposit.getIfInAccesslog() == '1') {
                AccessLog accessLog = new AccessLog();
                accessLog.setProvinceCode(account.getProvinceCode());
                accessLog.setEparchyCode(account.getEparchyCode());
                accessLog.setAcctId(deposit.getAcctId());
                accessLog.setAcctBalanceId(deposit.getAcctBalanceId());
                accessLog.setDepositCode(deposit.getDepositCode());
                accessLog.setOldBalance(deposit.getMoney());
                accessLog.setMoney(deposit.getRecvFee());
                accessLog.setNewBalance(accessLog.getOldBalance() + deposit.getRecvFee());
                accessLog.setAccessTag(deposit.getRecvFee() >= 0 ? '0' : '1');
                // 本次发票金额
                if (!CollectionUtils.isEmpty(invoiceFee) && invoiceFee.containsKey(accessLog.getAcctBalanceId())) {
                    accessLog.setInvoiceFee(invoiceFee.get(accessLog.getAcctBalanceId()));
                }
                //本次缴费触发相关标志
                accessLog.setCurTag('1');
                accessLog.setCancelTag('0');
                accessLogs.add(accessLog);
            }
        }

        //判断pAcctdeposit.impRealFee != 0的原因可能有往月负账单发生了销账，并且转换为预存冲抵了实时话费，需要转化为预存
        for (FeeAccountDeposit deposit : depositList) {
            if (deposit.getUseRecvFee() != 0 || deposit.getImpFee() != 0 || deposit.getImpRealFee() != 0) {
                AccessLog accessLog = new AccessLog();
                accessLog.setProvinceCode(account.getProvinceCode());
                accessLog.setEparchyCode(account.getEparchyCode());
                accessLog.setAcctId(deposit.getAcctId());
                accessLog.setAcctBalanceId(deposit.getAcctBalanceId());
                accessLog.setDepositCode(deposit.getDepositCode());
                accessLog.setOldBalance(deposit.getMoney() + deposit.getRecvFee());
                long tmpMoney = deposit.getUseRecvFee() + deposit.getImpFee() - deposit.getImpRealFee();
                accessLog.setMoney(-tmpMoney);
                accessLog.setNewBalance(accessLog.getOldBalance() - tmpMoney);
                //帐本销账
                accessLog.setAccessTag('2');
                //销帐标志
                accessLog.setCurTag('0');
                accessLog.setCancelTag('0');

                if (accessLog.getMoney() != 0) {
                    accessLogs.add(accessLog);
                }
            }
        }

        //0帐单参与了销帐,需要补取款记录,如果没有往月账单的销账记录,就排除0取款记录
        if (accessLogs.isEmpty() && !CollectionUtils.isEmpty(writeOffLogs)) {
            for (int i = 0; i < writeOffLogs.size(); ++i) {
                int k = 0;
                for (; k < accessLogs.size(); k++) {
                    if (writeOffLogs.get(i).getAcctBalanceId().equals(accessLogs.get(k).getAcctBalanceId())) {
                        break;
                    }
                }

                //多条0账单销账，只生成一条取款日志
                if (k == accessLogs.size()) {
                    AccessLog accessLog = new AccessLog();
                    accessLog.setProvinceCode(account.getProvinceCode());
                    accessLog.setEparchyCode(account.getEparchyCode());

                    int j = 0;
                    for (; j < depositList.size(); ++j) {
                        if (writeOffLogs.get(i).getAcctBalanceId().equals(depositList.get(j).getAcctBalanceId())) {
                            break;
                        }
                    }

                    if (j == depositList.size()) {
                        throw new SkyArkException("销0帐单发生错误!acctBalanceId=" + writeOffLogs.get(i).getAcctBalanceId());
                    }
                    accessLog.setAcctId(depositList.get(j).getAcctId());
                    accessLog.setAcctBalanceId(depositList.get(j).getAcctBalanceId());
                    accessLog.setDepositCode(depositList.get(j).getDepositCode());
                    accessLog.setOldBalance(depositList.get(j).getMoney());
                    accessLog.setMoney(0);
                    accessLog.setNewBalance(accessLog.getOldBalance());
                    accessLog.setAccessTag('2');
                    // 销帐标志
                    accessLog.setCurTag('0');
                    accessLog.setCancelTag('0');
                    accessLogs.add(accessLog);
                }
            }
        }

        if (CollectionUtils.isEmpty(accessLogs)) {
            throw new SkyArkException("本次交易没有生成存取款日志");
        }

        logger.debug("accessLogs.size = " + accessLogs.size());
        //获取存取款日志流水
        List<String> sequences = sysCommOperFeeService.getActingSequence(ActingPayPubDef.SEQ_ACCESSID_TABNAME,
                ActingPayPubDef.SEQ_ACCESSID_COLUMNNAME, accessLogs.size(), payLog.getProvinceCode());
        List<AccessLog> tmpAccessLogs = new ArrayList();
        for (int i = 0; i < accessLogs.size(); i++) {
            accessLogs.get(i).setAccessId(sequences.get(i));
            accessLogs.get(i).setChargeId(payLog.getChargeId());
            accessLogs.get(i).setOperateTime(payLog.getRecvTime());
            tmpAccessLogs.add(accessLogs.get(i));
        }

        return accessLogs;
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
    public List<CLPayLog> genCLPaylog(List<WriteOffLog> writeOffLogs, PayLog payLog, String headerGray) {
        return clPayLogService.genCLPaylog(writeOffLogs, payLog, headerGray);
    }

}
