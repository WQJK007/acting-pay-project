package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.domain.FeeAccountDeposit;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoIn;
import com.unicom.acting.pay.domain.DepositMQInfo;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.acting.pay.writeoff.domain.TransFeeCommInfoIn;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.skyark.component.web.rest.RestClient;

import com.unicom.acting.fee.calc.service.BillCalcService;
import com.unicom.acting.fee.calc.service.DepositCalcService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.writeoff.service.AcctDepositPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 账本表和账本销账关系表通过JDBC方式操作
 *
 * @author Administrators
 */
@Service
public class AcctDepositPayServiceImpl implements AcctDepositPayService {
    private static final Logger logger = LoggerFactory.getLogger(AcctDepositPayServiceImpl.class);
    @Autowired
    private RestClient restClient;
    @Autowired
    private SysCommOperFeeService sysCommOperPayService;
    @Autowired
    private BillCalcService billCalcService;
    @Autowired
    private DepositCalcService depositCalcService;

    @Override
    public List<DepositMQInfo> genDepositMQInfo(List<FeeAccountDeposit> feeAccountDeposits) {
        List<DepositMQInfo> depositMQInfoList = new ArrayList(feeAccountDeposits.size());
        for (FeeAccountDeposit deposit : feeAccountDeposits) {
            DepositMQInfo depositMQInfo = new DepositMQInfo();
            depositMQInfo.setAcctBalanceId(deposit.getAcctBalanceId());
            depositMQInfo.setAcctId(deposit.getAcctId());
            depositMQInfo.setUserId(deposit.getUserId());
            depositMQInfo.setDepositCode(deposit.getDepositCode());
            depositMQInfo.setDepositMoney(deposit.getDepositMoney());
            depositMQInfo.setInitMoney(deposit.getInitMoney());
            depositMQInfo.setMoney(deposit.getMoney());
            depositMQInfo.setLimitMode(deposit.getLimitMode());
            depositMQInfo.setLimitMoney(deposit.getLimitMoney());
            depositMQInfo.setLimitLeft(deposit.getLimitLeft());
            depositMQInfo.setInvoiceFee(deposit.getInvoiceFee());
            depositMQInfo.setPrintFee(deposit.getPrintFee());
            depositMQInfo.setStartCycleId(deposit.getStartCycleId());
            depositMQInfo.setEndCycleId(deposit.getEndCycleId());
            depositMQInfo.setStartDate(deposit.getStartDate());
            depositMQInfo.setEndDate(deposit.getEndDate());
            depositMQInfo.setOweFee(deposit.getOweFee());
            depositMQInfo.setValidTag(deposit.getValidTag());
            depositMQInfo.setFreezeFee(deposit.getFreezeFee());
            depositMQInfo.setPrivateTag(deposit.getPrivateTag());
            depositMQInfo.setEparchyCode(deposit.getEparchyCode());
            depositMQInfo.setBackupInfo(deposit.getBackupInfo());
            depositMQInfo.setRollBackInfo(deposit.getRollBackInfo());
            depositMQInfo.setVersionNo(deposit.getVersionNo());
            depositMQInfo.setActionCode(deposit.getActionCode());
            depositMQInfo.setOpenCycleId(deposit.getOpenCycleId());
            depositMQInfo.setUpdateTime(deposit.getUpdateTime());
            depositMQInfo.setRsrvFee1(deposit.getRsrvFee1());
            depositMQInfo.setRsrvFee2(deposit.getRsrvFee2());
            depositMQInfo.setRsrvInfo1(deposit.getRsrvInfo1());
            depositMQInfo.setRsrvInfo2(deposit.getRsrvInfo2());
            depositMQInfo.setProvinceCode(deposit.getProvinceCode());
            depositMQInfo.setNewFlag(deposit.getNewFlag());
            depositMQInfoList.add(depositMQInfo);
        }
        return depositMQInfoList;
    }

    //根据交易信息生成账本
    @Override
    public FeeAccountDeposit genAcctDeposit(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (tradeCommInfo.getFeeAccount() == null) {
            throw new SkyArkException("没有账户信息，请先查询账户资料");
        }
        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();

        if (tradeCommInfo.getMainUser() == null) {
            throw new SkyArkException("没有用户信息，请先查询用户资料");
        }
        User mainUser = tradeCommInfo.getMainUser();

        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        FeeAccountDeposit actDeposit = new FeeAccountDeposit();
        PaymentDeposit paymentDeposit = writeOffRuleInfo.getPaymentDeposit(recvFeeCommInfoIn.getPaymentId(), recvFeeCommInfoIn.getPayFeeModeCode());

        if (paymentDeposit == null) {
            throw new SkyArkException("没有配置储值方式和帐本科目关系!paymentId=" + recvFeeCommInfoIn.getPaymentId() + ",payFeeModeCode=" + recvFeeCommInfoIn.getPayFeeModeCode());
        }
        logger.info("depositCode=" + paymentDeposit.getDepositCode());

        //以外围传入的可打金额属性为准
        if (!StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getInvoiceTag())) {
            if ("2".equals(recvFeeCommInfoIn.getInvoiceTag())) {
                paymentDeposit.setInvoiceTag('1');
            } else {
                paymentDeposit.setInvoiceTag(StringUtil.firstOfString(recvFeeCommInfoIn.getInvoiceTag()));
            }
        }

        //帐本使用级别
        char privateTag = StringUtil.firstOfString(recvFeeCommInfoIn.getPrivateTag());
        //如果程序外没有传入使用等级以配置表里的为准。
        if ('0' != privateTag && '1' != privateTag) {
            privateTag = paymentDeposit.getPrivateTag();
        }
        //抵扣期间生成DMN日志使用
        recvFeeCommInfoIn.setPrivateTag(String.valueOf(privateTag));

        //校验账本生效时间是否合法
        String depositStartDate = recvFeeCommInfoIn.getDepositStartDate();
        if (!StringUtil.isEmptyCheckNullStr(depositStartDate)
                && (depositStartDate.length() < 10
                || !"-".equals(depositStartDate.substring(4, 5))
                || !"-".equals(depositStartDate.substring(7, 8)))) {
            throw new SkyArkException("帐本生效时间不合法!必须为YYYY-MM-DD HH24:MI:SS格式,传入的账本生效时间为" + depositStartDate);
        }

        if (recvFeeCommInfoIn.getMonths() > ActingFeePubDef.MAX_MONTH_NUM || recvFeeCommInfoIn.getMonths() <= 0) {
            recvFeeCommInfoIn.setMonths(ActingFeePubDef.MAX_MONTH_NUM);
        }

        logger.info("depositStartDate=" + depositStartDate + ",months=" + recvFeeCommInfoIn.getMonths() + ",privateTag=" + privateTag);

        //外围没有传入账本生效时间或者传入的生效时间小于系统当前时间设置为false,这种情况可以与现有同类型账本做合并
        boolean appointedStartDate = true;
        if (StringUtil.isEmptyCheckNullStr(depositStartDate) ||
                depositStartDate.substring(0, 10).compareTo(writeOffRuleInfo.getSysdate().substring(0, 10)) < 0) {
            recvFeeCommInfoIn.setDepositStartDate(writeOffRuleInfo.getSysdate());
            appointedStartDate = false;
        }

        //如果存在负账单，负账单要转化为指定账本 commpara表参数ASM_NEGATIVEBILL_DEPOSIT,cBSS目前配1007账本
        int negativeBillDeposit = writeOffRuleInfo.getNegativeBillDeposit();
        List<FeeBill> bills = tradeCommInfo.getFeeBills();
        if (negativeBillDeposit >= 0) {
            boolean negativeBillTag = false;   //是否存在负账单    默认不存在,如果存在设置为true
            if (!CollectionUtils.isEmpty(bills)) {
                for (FeeBill pBill : bills) {
                    if (billCalcService.getBillBalance(pBill) < 0 && pBill.getCanpayTag() != '2') {
                        negativeBillTag = true;
                        break;
                    }
                }
            }

            if (negativeBillTag) {
                FeeAccountDeposit negativeFeeAccountDeposit = depositCalcService.getAcctDepositByDepositCode(tradeCommInfo.getFeeAccountDeposits(), negativeBillDeposit);
                //如果不存在负账本
                if (negativeFeeAccountDeposit == null) {
                    String acctBalanceId = sysCommOperPayService.getSequence(recvFeeCommInfoIn.getEparchyCode(),
                            ActingFeePubDef.SEQ_ACCTBALANCE_ID, recvFeeCommInfoIn.getProvinceCode());
                    if ("".equals(acctBalanceId)) {
                        throw new SkyArkException("获取账本实例失败!SEQ_ACCTBALANCE_ID");
                    }
                    negativeFeeAccountDeposit = new FeeAccountDeposit();
                    negativeFeeAccountDeposit.setAcctBalanceId(acctBalanceId);
                    negativeFeeAccountDeposit.setAcctId(feeAccount.getAcctId());
                    negativeFeeAccountDeposit.setUserId(mainUser.getUserId());
                    negativeFeeAccountDeposit.setDepositCode(negativeBillDeposit);
                    negativeFeeAccountDeposit.setRecvFee(0);
                    negativeFeeAccountDeposit.setInitMoney(0);
                    negativeFeeAccountDeposit.setIfInAccesslog('1');
                    negativeFeeAccountDeposit.setInvoiceFee(0);
                    negativeFeeAccountDeposit.setLimitMoney(-1);
                    negativeFeeAccountDeposit.setLimitMode('0'); // 限额方式
                    negativeFeeAccountDeposit.setStartCycleId(ActingFeePubDef.MIN_CYCLE_ID);
                    negativeFeeAccountDeposit.setEndCycleId(ActingFeePubDef.MAX_CYCLE_ID);
                    negativeFeeAccountDeposit.setStartDate(recvFeeCommInfoIn.getDepositStartDate()); // 帐本生效开始时间
                    negativeFeeAccountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(negativeFeeAccountDeposit.getEndCycleId()).getCycEndTime());
                    negativeFeeAccountDeposit.setActionCode(-1);
                    negativeFeeAccountDeposit.setProvinceCode(feeAccount.getProvinceCode());
                    negativeFeeAccountDeposit.setEparchyCode(feeAccount.getEparchyCode());
                    negativeFeeAccountDeposit.setPrivateTag('0');
                    negativeFeeAccountDeposit.setVersionNo(1); // 更新版本号
                    negativeFeeAccountDeposit.setNewFlag('1'); // 新增标志
                    negativeFeeAccountDeposit.setValidTag('0');
                    depositCalcService.accountDepositUpAndSort(writeOffRuleInfo,
                            tradeCommInfo.getFeeAccountDeposits(), negativeFeeAccountDeposit);
                }
            }
        }

        boolean find = false;
        List<FeeAccountDeposit> deposits = depositCalcService.getAcctDepositsByDepositCode(tradeCommInfo.getFeeAccountDeposits(), paymentDeposit.getDepositCode());
        if (!CollectionUtils.isEmpty(deposits)) {
            for (FeeAccountDeposit pFeeAccountDeposit : deposits) {
                if (pFeeAccountDeposit.getEndCycleId() < ActingFeePubDef.MAX_CYCLE_ID) {
                    continue;
                }
                if (pFeeAccountDeposit.getEndCycleId() >= writeOffRuleInfo.getCurCycle().getCycleId()
                        && !appointedStartDate
                        && '0' == StringUtil.firstOfString(recvFeeCommInfoIn.getLimitMode())  //没有月限额
                        && '0' == pFeeAccountDeposit.getLimitMode() //无限额
                        && recvFeeCommInfoIn.getMonths() == ActingFeePubDef.MAX_MONTH_NUM //没有指定使用月份
                        && writeOffRuleInfo.depositIfUnite(pFeeAccountDeposit.getDepositCode())
                        && pFeeAccountDeposit.getActionCode() <= 0
                        && pFeeAccountDeposit.getValidTag() == recvFeeCommInfoIn.getValidTag() //帐本状态一样
                        && ('1' != pFeeAccountDeposit.getPrivateTag() && '1' != privateTag
                        || '1' == pFeeAccountDeposit.getPrivateTag() && '1' == privateTag
                        && pFeeAccountDeposit.getUserId().equals(mainUser.getUserId()))
                        && writeOffRuleInfo.getSysdate().compareTo(pFeeAccountDeposit.getStartDate()) > 0) {
                    //可以合并,非私有(或者相同用户的私有)
                    find = true;
                    actDeposit = pFeeAccountDeposit;
                    break;
                }
            }
        }

        logger.info("find = " + find);

        // 如果已经使用旧账本 且是营业过来的私有缴费 则判断用户是否有预打账单 有就强制生成新账本
        if (find && privateTag == '1' && "15000".equals(recvFeeCommInfoIn.getChannelId())) {
            if (!CollectionUtils.isEmpty(bills)) {
                for (FeeBill bill : bills) {
                    if (bill.getPayTag() == '8' && mainUser.getUserId().equals(bill.getUserId())) {
                        find = false;
                        break;
                    }
                }
            }
        }

        //没有可用的帐本
        if (!find) {
            String acctBalanceId = sysCommOperPayService.getSequence(recvFeeCommInfoIn.getEparchyCode(),
                    ActingFeePubDef.SEQ_ACCTBALANCE_ID, recvFeeCommInfoIn.getProvinceCode());
            if ("".equals(acctBalanceId)) {
                throw new SkyArkException("获取账本实例失败!SEQ_ACCTBALANCE_ID");
            }
            //actDeposit = new FeeAccountDeposit();
            actDeposit.setAcctBalanceId(acctBalanceId);
            actDeposit.setAcctId(feeAccount.getAcctId());
            actDeposit.setUserId(mainUser.getUserId());
            actDeposit.setDepositCode(paymentDeposit.getDepositCode());
            actDeposit.setRecvFee(recvFeeCommInfoIn.getTradeFee());
            actDeposit.setInitMoney(actDeposit.getRecvFee());
            actDeposit.setIfInAccesslog('1');

            //设置限额方式
            actDeposit.setLimitMode(StringUtil.firstOfString(recvFeeCommInfoIn.getLimitMode()));
            if (recvFeeCommInfoIn.getLimitMoney() <= 0
                    || recvFeeCommInfoIn.getLimitMoney() >= ActingFeePubDef.MAX_LIMIT_FEE) {
                actDeposit.setLimitMoney(-1);
            } else {
                //限额
                actDeposit.setLimitMoney(recvFeeCommInfoIn.getLimitMoney());
            }

            //设置账本销账账期范围
            if (ActingFeePubDef.MAX_MONTH_NUM == recvFeeCommInfoIn.getMonths()
                    || recvFeeCommInfoIn.getMonths() <= 0) {
                actDeposit.setStartCycleId(ActingFeePubDef.MIN_CYCLE_ID);
                actDeposit.setEndCycleId(ActingFeePubDef.MAX_CYCLE_ID);
            } else {
                String tmpDepositStartDate = recvFeeCommInfoIn.getDepositStartDate();
                actDeposit.setStartCycleId(Integer.parseInt(tmpDepositStartDate.substring(0, 4) + tmpDepositStartDate.substring(5, 7)));
                actDeposit.setEndCycleId(TimeUtil.genCycle(actDeposit.getStartCycleId(), (recvFeeCommInfoIn.getMonths() - 1)));
                if (actDeposit.getEndCycleId() > ActingFeePubDef.MAX_CYCLE_ID) {
                    actDeposit.setEndCycleId(ActingFeePubDef.MAX_CYCLE_ID);
                }
            }

            //设置账本生失效时间
            actDeposit.setStartDate(recvFeeCommInfoIn.getDepositStartDate());
            if (actDeposit.getEndCycleId() >= ActingFeePubDef.MAX_CYCLE_ID) {
                actDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(actDeposit.getEndCycleId()).getCycEndTime());
            } else {
                actDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(TimeUtil.genCycle(actDeposit.getEndCycleId(), 1)).getCycEndTime());
            }

            actDeposit.setActionCode(0);
            actDeposit.setProvinceCode(feeAccount.getProvinceCode());
            actDeposit.setEparchyCode(feeAccount.getEparchyCode());
            actDeposit.setPrivateTag(privateTag);
            actDeposit.setVersionNo(1);
            actDeposit.setNewFlag('1');
            actDeposit.setValidTag('0');
        } else {
            actDeposit.setRecvFee(actDeposit.getRecvFee() + recvFeeCommInfoIn.getTradeFee());
            actDeposit.setIfInAccesslog('1');
        }

        //可作为发票金额
        if ('1' == paymentDeposit.getInvoiceTag()) {
            //发票金额
            long invoiceFee = 0;
            if (!StringUtil.isEmptyCheckNullStr((recvFeeCommInfoIn.getInvoiceTag()))) {
                //
                if ("2".equals(recvFeeCommInfoIn.getInvoiceTag())) {
                    invoiceFee = recvFeeCommInfoIn.getInvoiceFee();

                } else {
                    invoiceFee = recvFeeCommInfoIn.getTradeFee();
                }
            } else {
                if (StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getFeePayMode())
                        || "01".equals(recvFeeCommInfoIn.getFeePayMode())) {
                    //现金缴费
                    invoiceFee = recvFeeCommInfoIn.getTradeFee();
                } else if ("02".equals(recvFeeCommInfoIn.getFeePayMode())) {
                    //现金+积分缴费
                    invoiceFee = recvFeeCommInfoIn.getTradeFee() - recvFeeCommInfoIn.getUserScore();
                } else if ("03".equals(recvFeeCommInfoIn.getFeePayMode())) {
                    //积分缴费
                    invoiceFee = 0;
                }

            }
            actDeposit.setInvoiceFee(actDeposit.getInvoiceFee() + invoiceFee);
            tradeCommInfo.setInvoiceFee(actDeposit.getAcctBalanceId(), invoiceFee);
        }

        //设置paylog表对象
        //tradeCommInfo.setReccFee(recvFeeCommInfoIn.getPaymentId(), actDeposit.getRecvFee());
        return actDeposit;
    }

    //根据账本类型生成账本
    @Override
    public FeeAccountDeposit genAcctDepositByDepositCode(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, int depositCode) {
        if (tradeCommInfo.getFeeAccount() == null) {
            throw new SkyArkException("没有账户信息，请先查询账户资料");
        }
        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();

        if (tradeCommInfo.getMainUser() == null) {
            throw new SkyArkException("没有用户信息，请先查询用户资料");
        }
        User mainUser = tradeCommInfo.getMainUser();

        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        FeeAccountDeposit actDeposit = new FeeAccountDeposit();
        //帐本使用级别
        char privateTag = StringUtil.firstOfString(recvFeeCommInfoIn.getPrivateTag());
        //如果程序外没有传入使用等级以配置表里的为准。
        if ('0' != privateTag && '1' != privateTag) {
            privateTag = '0';
        }

        //校验账本生效时间是否合法
        String depositStartDate = recvFeeCommInfoIn.getDepositStartDate();
        if (!StringUtil.isEmptyCheckNullStr(depositStartDate)
                && (depositStartDate.length() < 10
                || !"-".equals(depositStartDate.substring(4, 5))
                || !"-".equals(depositStartDate.substring(7, 8)))) {
            throw new SkyArkException("帐本生效时间不合法!必须为YYYY-MM-DD HH24:MI:SS格式,传入的账本生效时间为" + depositStartDate);
        }

        logger.info("depositStartDate=" + depositStartDate + ",months=" + recvFeeCommInfoIn.getMonths() + ",privateTag=" + privateTag);

        //外围没有传入账本生效时间或者传入的生效时间小于系统当前时间设置为false,这种情况可以与现有同类型账本做合并
        boolean appointedStartDate = true;
        if (StringUtil.isEmptyCheckNullStr(depositStartDate) ||
                depositStartDate.substring(0, 10).compareTo(writeOffRuleInfo.getSysdate().substring(0, 10)) < 0) {
            recvFeeCommInfoIn.setDepositStartDate(writeOffRuleInfo.getSysdate());
            appointedStartDate = false;
        }

        boolean find = false;
        List<FeeAccountDeposit> deposits = depositCalcService.getAcctDepositsByDepositCode(tradeCommInfo.getFeeAccountDeposits(), depositCode);
        if (!CollectionUtils.isEmpty(deposits)) {
            for (FeeAccountDeposit pFeeAccountDeposit : deposits) {
                if (pFeeAccountDeposit.getEndCycleId() >= writeOffRuleInfo.getCurCycle().getCycleId()
                        && !appointedStartDate
                        && '0' == StringUtil.firstOfString(recvFeeCommInfoIn.getLimitMode())  //没有月限额
                        && '0' == pFeeAccountDeposit.getLimitMode() //无限额
                        && recvFeeCommInfoIn.getMonths() == ActingFeePubDef.MAX_MONTH_NUM //没有指定使用月份
                        && writeOffRuleInfo.depositIfUnite(pFeeAccountDeposit.getDepositCode())
                        && pFeeAccountDeposit.getActionCode() <= 0
                        && pFeeAccountDeposit.getValidTag() == recvFeeCommInfoIn.getValidTag() //帐本状态一样
                        && ('1' != pFeeAccountDeposit.getPrivateTag() && '1' != privateTag
                        || '1' == pFeeAccountDeposit.getPrivateTag() && '1' == privateTag
                        && pFeeAccountDeposit.getUserId().equals(mainUser.getUserId()))
                        && writeOffRuleInfo.getSysdate().compareTo(pFeeAccountDeposit.getStartDate()) > 0) {
                    //可以合并,非私有(或者相同用户的私有)
                    find = true;
                    actDeposit = pFeeAccountDeposit;
                    break;
                }
            }
        }

        //没有可用的帐本
        if (!find) {
            String acctBalanceId = sysCommOperPayService.getSequence(recvFeeCommInfoIn.getEparchyCode(),
                    ActingFeePubDef.SEQ_ACCTBALANCE_ID, recvFeeCommInfoIn.getProvinceCode());
            if ("".equals(acctBalanceId)) {
                throw new SkyArkException("获取账本实例失败!SEQ_ACCTBALANCE_ID");
            }
            actDeposit.setAcctBalanceId(acctBalanceId);
            actDeposit.setAcctId(feeAccount.getAcctId());
            actDeposit.setUserId(mainUser.getUserId());
            actDeposit.setDepositCode(depositCode);
            actDeposit.setRecvFee(recvFeeCommInfoIn.getTradeFee());
            actDeposit.setInitMoney(actDeposit.getRecvFee());
            actDeposit.setIfInAccesslog('1');

            //设置限额方式
            actDeposit.setLimitMode(StringUtil.firstOfString(recvFeeCommInfoIn.getLimitMode()));
            if (recvFeeCommInfoIn.getLimitMoney() <= 0
                    || recvFeeCommInfoIn.getLimitMoney() >= ActingFeePubDef.MAX_LIMIT_FEE) {
                actDeposit.setLimitMoney(-1);
            } else {
                actDeposit.setLimitMoney(recvFeeCommInfoIn.getLimitMoney());  //限额
            }

            //设置账本销账账期范围
            if (ActingFeePubDef.MAX_MONTH_NUM == recvFeeCommInfoIn.getMonths()
                    || recvFeeCommInfoIn.getMonths() <= 0) {
                actDeposit.setStartCycleId(ActingFeePubDef.MIN_CYCLE_ID);
                actDeposit.setEndCycleId(ActingFeePubDef.MAX_CYCLE_ID);
            } else {
                String tmpDepositStartDate = recvFeeCommInfoIn.getDepositStartDate();
                actDeposit.setStartCycleId(Integer.parseInt(tmpDepositStartDate.substring(0, 4) + tmpDepositStartDate.substring(5, 7)));
                actDeposit.setEndCycleId(TimeUtil.genCycle(actDeposit.getStartCycleId(), (recvFeeCommInfoIn.getMonths() - 1)));
                if (actDeposit.getEndCycleId() > ActingFeePubDef.MAX_CYCLE_ID) {
                    actDeposit.setEndCycleId(ActingFeePubDef.MAX_CYCLE_ID);
                }
            }

            //设置账本生失效时间
            actDeposit.setStartDate(recvFeeCommInfoIn.getDepositStartDate());
            if (actDeposit.getEndCycleId() >= ActingFeePubDef.MAX_CYCLE_ID) {
                actDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(actDeposit.getEndCycleId()).getCycEndTime());
            } else {
                actDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(TimeUtil.genCycle(actDeposit.getEndCycleId(), 1)).getCycEndTime());
            }

            actDeposit.setActionCode(0);
            actDeposit.setProvinceCode(feeAccount.getProvinceCode());
            actDeposit.setEparchyCode(feeAccount.getEparchyCode());
            actDeposit.setPrivateTag(privateTag);
            actDeposit.setVersionNo(1); // 更新版本号
            actDeposit.setNewFlag('1'); // 新增标志
            actDeposit.setValidTag('0');
        } else {
            actDeposit.setRecvFee(actDeposit.getRecvFee() + recvFeeCommInfoIn.getTradeFee());
            actDeposit.setIfInAccesslog('1');
        }

        //可作为发票金额
        if ('2' != writeOffRuleInfo.depositTypeCode(depositCode)
                && '3' != writeOffRuleInfo.depositTypeCode(depositCode)) {
            if ("2".equals(recvFeeCommInfoIn.getInvoiceTag())) {
                actDeposit.setInvoiceFee(actDeposit.getInvoiceFee() + recvFeeCommInfoIn.getInvoiceFee());
            } else {
                actDeposit.setInvoiceFee(actDeposit.getInvoiceFee() + recvFeeCommInfoIn.getTradeFee());
            }
            tradeCommInfo.setInvoiceFee(actDeposit.getAcctBalanceId(), actDeposit.getInvoiceFee());

        }
        return actDeposit;
    }

    @Override
    public FeeAccountDeposit genAcctDepositByTransFer(TransFeeCommInfoIn transFeeCommInfoIn, TradeCommInfo tradeCommInfo, FeeAccountDeposit transOutDeposit) {
        //转入目标账本
        int desDepositCode = transFeeCommInfoIn.getDepositCode();
        char newPrivateTag = transOutDeposit.getPrivateTag();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        User mainUser = tradeCommInfo.getMainUser();
        boolean isCompType = isCompType(writeOffRuleInfo, mainUser.getUserId());
        boolean isSharedType = isSharedType(writeOffRuleInfo, mainUser.getUserId());
        CommPara chgDepositPara = getChgDepositPara(writeOffRuleInfo, mainUser.getNetTypeCode(), desDepositCode);
        //转入时融合关系账本公私有属性转换
        if (100015 == transFeeCommInfoIn.getPaymentId() && (isCompType || isSharedType) && chgDepositPara != null) {
            if (isCompType) {
                //组合套餐转换规则
                if (!StringUtils.isEmpty(chgDepositPara.getParaCode1()) && Integer.parseInt(chgDepositPara.getParaCode1()) > 0) {
                    //转换后的目标账本编码
                    desDepositCode = Integer.parseInt(chgDepositPara.getParaCode1());
                }
                if ("1".equals(chgDepositPara.getParaCode2()) && '0' == newPrivateTag) {
                    //公有转私有
                    newPrivateTag = '1';
                } else if ("0".equals(chgDepositPara.getParaCode2()) && '1' == newPrivateTag) {
                    //私有转公有
                    newPrivateTag = '0';
                }
            } else if (isSharedType) {
                //共享套餐转换规则
                if (!StringUtils.isEmpty(chgDepositPara.getParaCode3()) && Integer.parseInt(chgDepositPara.getParaCode3()) > 0) {
                    //转换后的目标账本编码
                    desDepositCode = Integer.parseInt(chgDepositPara.getParaCode3());
                }
                if ("1".equals(chgDepositPara.getParaCode4()) && '0' == newPrivateTag) {
                    //公有转私有
                    newPrivateTag = '1';
                } else if ("0".equals(chgDepositPara.getParaCode4()) && '1' == newPrivateTag) {
                    //私有转公有
                    newPrivateTag = '0';
                }
            }
        }

        List<FeeAccountDeposit> actDeposits = tradeCommInfo.getFeeAccountDeposits();
        List<FeeAccountDeposit> desDeposits = depositCalcService.getAcctDepositsByDepositCode(actDeposits, desDepositCode);
        FeeAccountDeposit desAccoutDeposit = new FeeAccountDeposit();
        //是否存在可合并账本
        boolean find = false;
        if (!CollectionUtils.isEmpty(desDeposits)) {
            for (FeeAccountDeposit deposit : desDeposits) {
                //失效账本不合并
                if (deposit.getEndCycleId() < writeOffRuleInfo.getCurCycle().getCycleId()) {
                    continue;
                }
                //过户转账转兑帐本不合并
                if ('1' == transFeeCommInfoIn.getChgAcctTag() && deposit.getActionCode() > 0) {
                    continue;
                }

                if (writeOffRuleInfo.depositIfUnite(desDepositCode)
                        && '1' != deposit.getPrivateTag() && '1' != newPrivateTag
                        && '0' == deposit.getLimitMode() && '0' == transOutDeposit.getLimitMode()
                        && transOutDeposit.getActionCode() <= 0) {
                    find = true;
                    desAccoutDeposit = deposit;
                    break;
                }
            }
        }

        if (!find) {
            desAccoutDeposit.setProvinceCode(tradeCommInfo.getFeeAccount().getProvinceCode());
            desAccoutDeposit.setEparchyCode(tradeCommInfo.getFeeAccount().getEparchyCode());
            desAccoutDeposit.setAcctId(tradeCommInfo.getFeeAccount().getAcctId());
            desAccoutDeposit.setAcctBalanceId(sysCommOperPayService.getSequence(transFeeCommInfoIn.getEparchyCode(),
                    ActingFeePubDef.SEQ_ACCTBALANCE_ID, transFeeCommInfoIn.getProvinceCode()));
            desAccoutDeposit.setUserId(mainUser.getUserId());
            desAccoutDeposit.setDepositCode(desDepositCode);
            desAccoutDeposit.setRecvFee(transFeeCommInfoIn.getTradeFee());
            desAccoutDeposit.setInitMoney(transFeeCommInfoIn.getTradeFee());
            desAccoutDeposit.setActionCode(transOutDeposit.getActionCode());
            if (!CollectionUtils.isEmpty(tradeCommInfo.getInvoiceFeeMap())) {
                if (tradeCommInfo.getInvoiceFeeMap().containsKey(transOutDeposit.getAcctBalanceId())) {
                    //设置转入账本的可打金额
                    desAccoutDeposit.setInvoiceFee(desAccoutDeposit.getInvoiceFee()
                            + tradeCommInfo.getInvoiceFeeMap().get(transOutDeposit.getAcctBalanceId()));
                    //记录转入账本的可打金额生成存取款日志使用
                    tradeCommInfo.getInvoiceFeeMap().put(desAccoutDeposit.getAcctBalanceId(),
                            tradeCommInfo.getInvoiceFeeMap().get(transOutDeposit.getAcctBalanceId()));
                }
            }

            if (StringUtils.isEmpty(transFeeCommInfoIn.getDepositStartDate())) {
                transFeeCommInfoIn.setDepositStartDate(writeOffRuleInfo.getSysdate());
            } else {
                if (transFeeCommInfoIn.getDepositStartDate().length() < 10
                        || !"-".equals(transFeeCommInfoIn.getDepositStartDate().substring(4, 5))
                        || !"-".equals(transFeeCommInfoIn.getDepositStartDate().substring(7, 8))) {
                    throw new SkyArkException("帐本生效时间不合法!必须为YYYY-MM-DD HH24:MI:SS格式,传入的账本生效时间为" + transFeeCommInfoIn.getDepositStartDate());
                }
            }

            desAccoutDeposit.setLimitMoney(transFeeCommInfoIn.getLimitMoney());
            desAccoutDeposit.setLimitMode(StringUtil.firstOfString(transFeeCommInfoIn.getLimitMode()));

            if (transFeeCommInfoIn.getMonths() == ActingFeePubDef.MAX_MONTH_NUM ||
                    transFeeCommInfoIn.getMonths() <= 0) {
                desAccoutDeposit.setStartCycleId(ActingFeePubDef.MIN_CYCLE_ID);
                desAccoutDeposit.setEndCycleId(ActingFeePubDef.MAX_CYCLE_ID);
            } else {
                desAccoutDeposit.setStartCycleId(Integer.parseInt(
                        transFeeCommInfoIn.getDepositStartDate().substring(0, 4)
                                + transFeeCommInfoIn.getDepositStartDate().substring(5, 7)));
                desAccoutDeposit.setEndCycleId(TimeUtil.genCycle(desAccoutDeposit.getStartCycleId(), transFeeCommInfoIn.getMonths() - 1));
            }
            desAccoutDeposit.setStartDate(transFeeCommInfoIn.getDepositStartDate());
            if (desAccoutDeposit.getEndCycleId() < ActingFeePubDef.MAX_CYCLE_ID) {
                desAccoutDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(
                        TimeUtil.genCycle(desAccoutDeposit.getStartCycleId(), 1)).getCycStartTime());
            } else {
                desAccoutDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(
                        desAccoutDeposit.getStartCycleId()).getCycStartTime());
            }

            //转帐转入
            if (100015 == transFeeCommInfoIn.getPaymentId()) {
                desAccoutDeposit.setPrivateTag(newPrivateTag);
                if ('0' == desAccoutDeposit.getPrivateTag()) {
                    desAccoutDeposit.setUserId(transFeeCommInfoIn.getUserId());
                } else {
                    //当前台余额转账时 如果转出为私有账本 目标账本的user_id为目标user_id
                    if ("1".equals(transFeeCommInfoIn.getTransTag())) {
                        desAccoutDeposit.setUserId(transFeeCommInfoIn.getUserId());
                    } else {
                        desAccoutDeposit.setUserId(transOutDeposit.getUserId());
                    }
                }
                desAccoutDeposit.setLimitLeft(transOutDeposit.getLimitLeft());

            } else {
                desAccoutDeposit.setPrivateTag('0');
            }


            desAccoutDeposit.setVersionNo(1); // 更新版本号
            desAccoutDeposit.setNewFlag('1'); // 新增标志
            desAccoutDeposit.setValidTag('0');
        } else {
            //设置转入金额和发票可打金额
            desAccoutDeposit.setRecvFee(desAccoutDeposit.getRecvFee() + transFeeCommInfoIn.getTradeFee());
            if (!CollectionUtils.isEmpty(tradeCommInfo.getInvoiceFeeMap())) {
                if (tradeCommInfo.getInvoiceFeeMap().containsKey(transOutDeposit.getAcctBalanceId())) {
                    //设置转入账本的可打金额
                    desAccoutDeposit.setInvoiceFee(desAccoutDeposit.getInvoiceFee()
                            + tradeCommInfo.getInvoiceFeeMap().get(transOutDeposit.getAcctBalanceId()));
                    //记录转入账本的可打金额生成存取款日志使用
                    tradeCommInfo.getInvoiceFeeMap().put(desAccoutDeposit.getAcctBalanceId(),
                            tradeCommInfo.getInvoiceFeeMap().get(transOutDeposit.getAcctBalanceId()));
                }
            }
        }
        return desAccoutDeposit;
    }

    @Override
    public void updateAcctDepositOweFee(List<FeeAccountDeposit> actDeposits, FeeWriteSnapLog writeSnapLog, int maxAcctCycleId) {
        //往月欠费
        long acctOweFee = writeSnapLog.getAllNewBOweFee();
        //是否存在私有账本
        boolean hasPrivateDeposit = false;
        for (FeeAccountDeposit actDeposit : actDeposits) {
            if ('0' == actDeposit.getPrivateTag()) {
                acctOweFee = actDeposit.getOweFee();
            } else {
                hasPrivateDeposit = true;
            }
        }

        //没有私有账本，更新账户所有账本的往月欠费字段
        if (!hasPrivateDeposit) {
            for (FeeAccountDeposit deposit : actDeposits) {
                deposit.setOpenCycleId(maxAcctCycleId);
                deposit.setOweFee(acctOweFee);
            }
        } else {
            for (FeeAccountDeposit deposit : actDeposits) {
                deposit.setOpenCycleId(maxAcctCycleId);
            }
        }
    }

    @Override
    public void updateAcctDepositInfo(List<FeeAccountDeposit> actDeposits, WriteOffRuleInfo writeOffRuleInfo, String provinceCode) {
        if (CollectionUtils.isEmpty(actDeposits)) {
            throw new SkyArkException("没有获取本次缴费对应的账本记录");
        }
        for (FeeAccountDeposit deposit : actDeposits) {
            long currUseMoney = deposit.getImpFee() + deposit.getUseRecvFee() - deposit.getImpRealFee();
            deposit.setDepositMoney(deposit.getDepositMoney() + deposit.getRecvFee());
            deposit.setUpdateTime(writeOffRuleInfo.getSysdate());
            if ('1' == deposit.getNewFlag()) {
                deposit.setMoney(deposit.getMoney() + deposit.getRecvFee() - currUseMoney);
            } else {
                long leftMoney = deposit.getMoney() + deposit.getRecvFee() - currUseMoney;
                deposit.setMoney(leftMoney);
            }
        }
    }

    @Override
    public void updateDepositInfo(List<FeeAccountDeposit> actDeposits, long acctOweFee, String sysdate, int maxAcctCycleId) {
        if (CollectionUtils.isEmpty(actDeposits)) {
            throw new SkyArkException("没有获取本次缴费对应的账本记录");
        }

        //先更新账本余额
        for (FeeAccountDeposit deposit : actDeposits) {
            long currUseMoney = deposit.getImpFee() + deposit.getUseRecvFee() - deposit.getImpRealFee();
            deposit.setDepositMoney(deposit.getDepositMoney() + deposit.getRecvFee());
            deposit.setUpdateTime(sysdate);
            if ('1' == deposit.getNewFlag()) {
                deposit.setMoney(deposit.getMoney() + deposit.getRecvFee() - currUseMoney);
            } else {
                long leftMoney = deposit.getMoney() + deposit.getRecvFee() - currUseMoney;
                deposit.setMoney(leftMoney);
            }
        }

        //是否存在私有账本
        boolean hasPrivateDeposit = false;
        for (FeeAccountDeposit actDeposit : actDeposits) {
            if ('0' == actDeposit.getPrivateTag()) {
                acctOweFee = actDeposit.getOweFee();
            } else {
                hasPrivateDeposit = true;
            }
        }

        //没有私有账本，更新账户所有账本的往月欠费字段
        if (!hasPrivateDeposit) {
            for (FeeAccountDeposit deposit : actDeposits) {
                deposit.setOpenCycleId(maxAcctCycleId);
                deposit.setOweFee(acctOweFee);
            }
        } else {
            for (FeeAccountDeposit deposit : actDeposits) {
                deposit.setOpenCycleId(maxAcctCycleId);
            }
        }
    }

    /**
     * 组合类关系
     *
     * @param writeOffRuleInfo
     * @param userId
     * @return
     */
    private boolean isCompType(WriteOffRuleInfo writeOffRuleInfo, String userId) {
        CommPara commPara = writeOffRuleInfo.getCommpara("ASM_CHANGEACCT_RULE");
        if (commPara == null) {
            throw new SkyArkException("ASM_CHANGEACCT_RULE参数没有配置!");
        }

        if (StringUtils.isEmpty(commPara.getParaCode1())) {
            return false;
        }

        String[] memberTypes = commPara.getParaCode1().split(",");
        //生成请求参数
        return false;
    }

    /**
     * 共享类关系
     *
     * @param writeOffRuleInfo
     * @param userId
     * @return
     */
    private boolean isSharedType(WriteOffRuleInfo writeOffRuleInfo, String userId) {
        CommPara commPara = writeOffRuleInfo.getCommpara("ASM_CHANGEACCT_RULE");
        if (commPara == null) {
            throw new SkyArkException("ASM_CHANGEACCT_RULE参数没有配置!");
        }

        if (StringUtils.isEmpty(commPara.getParaCode1())) {
            return false;
        }

        String[] memberTypes = commPara.getParaCode1().split(",");
        //生成请求参数
        return false;
    }

    //获取账本属性转换参数
    private CommPara getChgDepositPara(WriteOffRuleInfo writeOffRuleInfo, String netTypeCode, int deposit) {
        //特定网别用户
        String chgDeposit = "ASM_CHGDEPOSIT_";
        String paraCode = chgDeposit + netTypeCode + "_2";
        CommPara commpara = writeOffRuleInfo.getCommpara(paraCode);
        if (commpara != null) {
            return commpara;
        }

        //特殊网别特殊账本科目用户
        paraCode = chgDeposit + netTypeCode + "_0_" + deposit;
        commpara = writeOffRuleInfo.getCommpara(paraCode);
        if (commpara != null) {
            return commpara;
        }

        //特殊网别特殊账本类型用户
        char depositTypeCode = writeOffRuleInfo.depositTypeCode(deposit);
        paraCode = chgDeposit + netTypeCode + "_1_" + depositTypeCode;
        commpara = writeOffRuleInfo.getCommpara(paraCode);
        if (commpara != null) {
            return commpara;
        }

        //默认网别特殊账本科目用户
        paraCode = chgDeposit + "ZZ" + "_0_" + deposit;
        commpara = writeOffRuleInfo.getCommpara(paraCode);
        if (commpara != null) {
            return commpara;
        }

        //默认网别特殊账本类型用户
        paraCode = chgDeposit + "ZZ" + "_1_" + depositTypeCode;
        commpara = writeOffRuleInfo.getCommpara(paraCode);
        if (commpara != null) {
            return commpara;
        }

        return null;
    }
}
