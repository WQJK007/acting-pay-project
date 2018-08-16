package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.pay.domain.DepositMQInfo;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import com.unicom.skyark.component.web.rest.RestClient;

import com.unicom.acting.fee.calc.domain.TradeCommInfo;
import com.unicom.acting.fee.calc.service.BillService;
import com.unicom.acting.fee.calc.service.DepositService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.writeoff.service.AcctDepositPayService;
import com.unicom.acts.pay.domain.Account;
import com.unicom.acts.pay.domain.AccountDeposit;
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
    private BillService billService;
    @Autowired
    private DepositService depositService;

    @Override
    public List<DepositMQInfo> genDepositMQInfo(List<AccountDeposit> accountDepositList) {
        List<DepositMQInfo> depositMQInfoList = new ArrayList(accountDepositList.size());
        for (AccountDeposit deposit : accountDepositList) {
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
    public AccountDeposit genAcctDeposit(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (tradeCommInfo.getAccount() == null) {
            throw new SkyArkException("没有账户信息，请先查询账户资料");
        }
        Account account = tradeCommInfo.getAccount();

        if (tradeCommInfo.getMainUser() == null) {
            throw new SkyArkException("没有用户信息，请先查询用户资料");
        }
        User mainUser = tradeCommInfo.getMainUser();

        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        AccountDeposit accountDeposit = new AccountDeposit();
        PaymentDeposit paymentDeposit = writeOffRuleInfo.getPaymentDeposit(tradeCommInfoIn.getPaymentId(), tradeCommInfoIn.getPayFeeModeCode());

        if (paymentDeposit == null) {
            throw new SkyArkException("没有配置储值方式和帐本科目关系!paymentId=" + tradeCommInfoIn.getPaymentId() + ",payFeeModeCode=" + tradeCommInfoIn.getPayFeeModeCode());
        }
        logger.info("depositCode=" + paymentDeposit.getDepositCode());

        //以外围传入的可打金额属性为准
        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getInvoiceTag())) {
            if ("2".equals(tradeCommInfoIn.getInvoiceTag())) {
                paymentDeposit.setInvoiceTag('1');
            } else {
                paymentDeposit.setInvoiceTag(StringUtil.firstOfString(tradeCommInfoIn.getInvoiceTag()));
            }
        }

        //帐本使用级别
        char privateTag = StringUtil.firstOfString(tradeCommInfoIn.getPrivateTag());
        //如果程序外没有传入使用等级以配置表里的为准。
        if ('0' != privateTag && '1' != privateTag) {
            privateTag = paymentDeposit.getPrivateTag();
        }
        //抵扣期间生成DMN日志使用
        tradeCommInfoIn.setPrivateTag(String.valueOf(privateTag));

        //校验账本生效时间是否合法
        String depositStartDate = tradeCommInfoIn.getDepositStartDate();
        if (!StringUtil.isEmptyCheckNullStr(depositStartDate)
                && (depositStartDate.length() < 10
                || !"-".equals(depositStartDate.substring(4, 5))
                || !"-".equals(depositStartDate.substring(7, 8)))) {
            throw new SkyArkException("帐本生效时间不合法!必须为YYYY-MM-DD HH24:MI:SS格式,传入的账本生效时间为" + depositStartDate);
        }

        if (tradeCommInfoIn.getMonths() > ActPayPubDef.MAX_MONTH_NUM || tradeCommInfoIn.getMonths() <= 0) {
            tradeCommInfoIn.setMonths(ActPayPubDef.MAX_MONTH_NUM);
        }

        logger.info("depositStartDate=" + depositStartDate + ",months=" + tradeCommInfoIn.getMonths() + ",privateTag=" + privateTag);

        //外围没有传入账本生效时间或者传入的生效时间小于系统当前时间设置为false,这种情况可以与现有同类型账本做合并
        boolean appointedStartDate = true;
        if (StringUtil.isEmptyCheckNullStr(depositStartDate) ||
                depositStartDate.substring(0, 10).compareTo(writeOffRuleInfo.getSysdate().substring(0, 10)) < 0) {
            tradeCommInfoIn.setDepositStartDate(writeOffRuleInfo.getSysdate());
            appointedStartDate = false;
        }

        //如果存在负账单，负账单要转化为指定账本 commpara表参数ASM_NEGATIVEBILL_DEPOSIT,cBSS目前配1007账本
        int negativeBillDeposit = writeOffRuleInfo.getNegativeBillDeposit();
        List<Bill> bills = tradeCommInfo.getBills();
        if (negativeBillDeposit >= 0) {
            boolean negativeBillTag = false;   //是否存在负账单    默认不存在,如果存在设置为true
            if (!CollectionUtils.isEmpty(bills)) {
                for (Bill pBill : bills) {
                    if (billService.getBillBalance(pBill) < 0 && pBill.getCanpayTag() != '2') {
                        negativeBillTag = true;
                        break;
                    }
                }
            }

            if (negativeBillTag) {
                AccountDeposit negativeAccountDeposit = depositService.getAcctDepositByDepositCode(tradeCommInfo.getAccountDeposits(), negativeBillDeposit);
                //如果不存在负账本
                if (negativeAccountDeposit == null) {
                    String acctBalanceId = sysCommOperPayService.getSequence(tradeCommInfoIn.getEparchyCode(),
                            ActPayPubDef.SEQ_ACCTBALANCE_ID, tradeCommInfoIn.getProvinceCode());
                    if ("".equals(acctBalanceId)) {
                        throw new SkyArkException("获取账本实例失败!SEQ_ACCTBALANCE_ID");
                    }
                    negativeAccountDeposit = new AccountDeposit();
                    negativeAccountDeposit.setAcctBalanceId(acctBalanceId);
                    negativeAccountDeposit.setAcctId(account.getAcctId());
                    negativeAccountDeposit.setUserId(mainUser.getUserId());
                    negativeAccountDeposit.setDepositCode(negativeBillDeposit);
                    negativeAccountDeposit.setRecvFee(0);
                    negativeAccountDeposit.setInitMoney(0);
                    negativeAccountDeposit.setIfInAccesslog('1');
                    negativeAccountDeposit.setInvoiceFee(0);
                    negativeAccountDeposit.setLimitMoney(-1);
                    negativeAccountDeposit.setLimitMode('0'); // 限额方式
                    negativeAccountDeposit.setStartCycleId(ActPayPubDef.MIN_CYCLE_ID);
                    negativeAccountDeposit.setEndCycleId(ActPayPubDef.MAX_CYCLE_ID);
                    negativeAccountDeposit.setStartDate(tradeCommInfoIn.getDepositStartDate()); // 帐本生效开始时间
                    negativeAccountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(negativeAccountDeposit.getEndCycleId()).getCycEndTime());
                    negativeAccountDeposit.setActionCode(-1);
                    negativeAccountDeposit.setProvinceCode(account.getProvinceCode());
                    negativeAccountDeposit.setEparchyCode(account.getEparchyCode());
                    negativeAccountDeposit.setPrivateTag('0');
                    negativeAccountDeposit.setVersionNo(1); // 更新版本号
                    negativeAccountDeposit.setNewFlag('1'); // 新增标志
                    negativeAccountDeposit.setValidTag('0');
                    depositService.accountDepositUpAndSort(writeOffRuleInfo,
                            tradeCommInfo.getAccountDeposits(), negativeAccountDeposit);
                }
            }
        }

        boolean find = false;
        List<AccountDeposit> deposits = depositService.getAcctDepositsByDepositCode(tradeCommInfo.getAccountDeposits(), paymentDeposit.getDepositCode());
        if (!CollectionUtils.isEmpty(deposits)) {
            for (AccountDeposit pAcctDeposit : deposits) {
                if (pAcctDeposit.getEndCycleId() < ActPayPubDef.MAX_CYCLE_ID) {
                    continue;
                }
                if (pAcctDeposit.getEndCycleId() >= writeOffRuleInfo.getCurCycle().getCycleId()
                        && !appointedStartDate
                        && '0' == StringUtil.firstOfString(tradeCommInfoIn.getLimitMode())  //没有月限额
                        && '0' == pAcctDeposit.getLimitMode() //无限额
                        && tradeCommInfoIn.getMonths() == ActPayPubDef.MAX_MONTH_NUM //没有指定使用月份
                        && writeOffRuleInfo.depositIfUnite(pAcctDeposit.getDepositCode())
                        && pAcctDeposit.getActionCode() <= 0
                        && pAcctDeposit.getValidTag() == tradeCommInfoIn.getValidTag() //帐本状态一样
                        && ('1' != pAcctDeposit.getPrivateTag() && '1' != privateTag
                        || '1' == pAcctDeposit.getPrivateTag() && '1' == privateTag
                        && pAcctDeposit.getUserId().equals(mainUser.getUserId()))
                        && writeOffRuleInfo.getSysdate().compareTo(pAcctDeposit.getStartDate()) > 0) {
                    //可以合并,非私有(或者相同用户的私有)
                    find = true;
                    accountDeposit = pAcctDeposit;
                    break;
                }
            }
        }

        logger.info("find = " + find);

        // 如果已经使用旧账本 且是营业过来的私有缴费 则判断用户是否有预打账单 有就强制生成新账本
        if (find && privateTag == '1' && "15000".equals(tradeCommInfoIn.getChannelId())) {
            if (!CollectionUtils.isEmpty(bills)) {
                for (Bill bill : bills) {
                    if (bill.getPayTag() == '8' && mainUser.getUserId().equals(bill.getUserId())) {
                        find = false;
                        break;
                    }
                }
            }
        }

        //没有可用的帐本
        if (!find) {
            String acctBalanceId = sysCommOperPayService.getSequence(tradeCommInfoIn.getEparchyCode(),
                    ActPayPubDef.SEQ_ACCTBALANCE_ID, tradeCommInfoIn.getProvinceCode());
            if ("".equals(acctBalanceId)) {
                throw new SkyArkException("获取账本实例失败!SEQ_ACCTBALANCE_ID");
            }
            //accountDeposit = new AccountDeposit();
            accountDeposit.setAcctBalanceId(acctBalanceId);
            accountDeposit.setAcctId(account.getAcctId());
            accountDeposit.setUserId(mainUser.getUserId());
            accountDeposit.setDepositCode(paymentDeposit.getDepositCode());
            accountDeposit.setRecvFee(tradeCommInfoIn.getTradeFee());
            accountDeposit.setInitMoney(accountDeposit.getRecvFee());
            accountDeposit.setIfInAccesslog('1');

            //设置限额方式
            accountDeposit.setLimitMode(StringUtil.firstOfString(tradeCommInfoIn.getLimitMode()));
            if (tradeCommInfoIn.getLimitMoney() <= 0
                    || tradeCommInfoIn.getLimitMoney() >= ActPayPubDef.MAX_LIMIT_FEE) {
                accountDeposit.setLimitMoney(-1);
            } else {
                //限额
                accountDeposit.setLimitMoney(tradeCommInfoIn.getLimitMoney());
            }

            //设置账本销账账期范围
            if (ActPayPubDef.MAX_MONTH_NUM == tradeCommInfoIn.getMonths()
                    || tradeCommInfoIn.getMonths() <= 0) {
                accountDeposit.setStartCycleId(ActPayPubDef.MIN_CYCLE_ID);
                accountDeposit.setEndCycleId(ActPayPubDef.MAX_CYCLE_ID);
            } else {
                String tmpDepositStartDate = tradeCommInfoIn.getDepositStartDate();
                accountDeposit.setStartCycleId(Integer.parseInt(tmpDepositStartDate.substring(0, 4) + tmpDepositStartDate.substring(5, 7)));
                accountDeposit.setEndCycleId(TimeUtil.genCycle(accountDeposit.getStartCycleId(), (tradeCommInfoIn.getMonths() - 1)));
                if (accountDeposit.getEndCycleId() > ActPayPubDef.MAX_CYCLE_ID) {
                    accountDeposit.setEndCycleId(ActPayPubDef.MAX_CYCLE_ID);
                }
            }

            //设置账本生失效时间
            accountDeposit.setStartDate(tradeCommInfoIn.getDepositStartDate());
            if (accountDeposit.getEndCycleId() >= ActPayPubDef.MAX_CYCLE_ID) {
                accountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(accountDeposit.getEndCycleId()).getCycEndTime());
            } else {
                accountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(TimeUtil.genCycle(accountDeposit.getEndCycleId(), 1)).getCycEndTime());
            }

            accountDeposit.setActionCode(0);
            accountDeposit.setProvinceCode(account.getProvinceCode());
            accountDeposit.setEparchyCode(account.getEparchyCode());
            accountDeposit.setPrivateTag(privateTag);
            accountDeposit.setVersionNo(1);
            accountDeposit.setNewFlag('1');
            accountDeposit.setValidTag('0');
        } else {
            accountDeposit.setRecvFee(accountDeposit.getRecvFee() + tradeCommInfoIn.getTradeFee());
            accountDeposit.setIfInAccesslog('1');
        }

        //可作为发票金额
        if ('1' == paymentDeposit.getInvoiceTag()) {
            //发票金额
            long invoiceFee = 0;
            if (!StringUtil.isEmptyCheckNullStr((tradeCommInfoIn.getInvoiceTag()))) {
                //
                if ("2".equals(tradeCommInfoIn.getInvoiceTag())) {
                    invoiceFee = tradeCommInfoIn.getInvoiceFee();

                } else {
                    invoiceFee = tradeCommInfoIn.getTradeFee();
                }
            } else {
                if (StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getFeePayMode())
                        || "01".equals(tradeCommInfoIn.getFeePayMode())) {
                    //现金缴费
                    invoiceFee = tradeCommInfoIn.getTradeFee();
                } else if ("02".equals(tradeCommInfoIn.getFeePayMode())) {
                    //现金+积分缴费
                    invoiceFee = tradeCommInfoIn.getTradeFee() - tradeCommInfoIn.getUserScore();
                } else if ("03".equals(tradeCommInfoIn.getFeePayMode())) {
                    //积分缴费
                    invoiceFee = 0;
                }

            }
            accountDeposit.setInvoiceFee(accountDeposit.getInvoiceFee() + invoiceFee);
            tradeCommInfo.setInvoiceFee(accountDeposit.getAcctBalanceId(), invoiceFee);
        }

        //设置paylog表对象
        //tradeCommInfo.setReccFee(tradeCommInfoIn.getPaymentId(), accountDeposit.getRecvFee());
        return accountDeposit;
    }

    //根据账本类型生成账本
    @Override
    public AccountDeposit genAcctDepositByDepositCode(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, int depositCode) {
        if (tradeCommInfo.getAccount() == null) {
            throw new SkyArkException("没有账户信息，请先查询账户资料");
        }
        Account account = tradeCommInfo.getAccount();

        if (tradeCommInfo.getMainUser() == null) {
            throw new SkyArkException("没有用户信息，请先查询用户资料");
        }
        User mainUser = tradeCommInfo.getMainUser();

        if (tradeCommInfo.getWriteOffRuleInfo() == null) {
            throw new SkyArkException("没有加载销账规则参数");
        }
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        AccountDeposit accountDeposit = new AccountDeposit();
        //帐本使用级别
        char privateTag = StringUtil.firstOfString(tradeCommInfoIn.getPrivateTag());
        //如果程序外没有传入使用等级以配置表里的为准。
        if ('0' != privateTag && '1' != privateTag) {
            privateTag = '0';
        }

        //校验账本生效时间是否合法
        String depositStartDate = tradeCommInfoIn.getDepositStartDate();
        if (!StringUtil.isEmptyCheckNullStr(depositStartDate)
                && (depositStartDate.length() < 10
                || !"-".equals(depositStartDate.substring(4, 5))
                || !"-".equals(depositStartDate.substring(7, 8)))) {
            throw new SkyArkException("帐本生效时间不合法!必须为YYYY-MM-DD HH24:MI:SS格式,传入的账本生效时间为" + depositStartDate);
        }

        logger.info("depositStartDate=" + depositStartDate + ",months=" + tradeCommInfoIn.getMonths() + ",privateTag=" + privateTag);

        //外围没有传入账本生效时间或者传入的生效时间小于系统当前时间设置为false,这种情况可以与现有同类型账本做合并
        boolean appointedStartDate = true;
        if (StringUtil.isEmptyCheckNullStr(depositStartDate) ||
                depositStartDate.substring(0, 10).compareTo(writeOffRuleInfo.getSysdate().substring(0, 10)) < 0) {
            tradeCommInfoIn.setDepositStartDate(writeOffRuleInfo.getSysdate());
            appointedStartDate = false;
        }

        boolean find = false;
        List<AccountDeposit> deposits = depositService.getAcctDepositsByDepositCode(tradeCommInfo.getAccountDeposits(), depositCode);
        if (!CollectionUtils.isEmpty(deposits)) {
            for (AccountDeposit pAcctDeposit : deposits) {
                if (pAcctDeposit.getEndCycleId() >= writeOffRuleInfo.getCurCycle().getCycleId()
                        && !appointedStartDate
                        && '0' == StringUtil.firstOfString(tradeCommInfoIn.getLimitMode())  //没有月限额
                        && '0' == pAcctDeposit.getLimitMode() //无限额
                        && tradeCommInfoIn.getMonths() == ActPayPubDef.MAX_MONTH_NUM //没有指定使用月份
                        && writeOffRuleInfo.depositIfUnite(pAcctDeposit.getDepositCode())
                        && pAcctDeposit.getActionCode() <= 0
                        && pAcctDeposit.getValidTag() == tradeCommInfoIn.getValidTag() //帐本状态一样
                        && ('1' != pAcctDeposit.getPrivateTag() && '1' != privateTag
                        || '1' == pAcctDeposit.getPrivateTag() && '1' == privateTag
                        && pAcctDeposit.getUserId().equals(mainUser.getUserId()))
                        && writeOffRuleInfo.getSysdate().compareTo(pAcctDeposit.getStartDate()) > 0) {
                    //可以合并,非私有(或者相同用户的私有)
                    find = true;
                    accountDeposit = pAcctDeposit;
                    break;
                }
            }
        }

        //没有可用的帐本
        if (!find) {
            String acctBalanceId = sysCommOperPayService.getSequence(tradeCommInfoIn.getEparchyCode(),
                    ActPayPubDef.SEQ_ACCTBALANCE_ID, tradeCommInfoIn.getProvinceCode());
            if ("".equals(acctBalanceId)) {
                throw new SkyArkException("获取账本实例失败!SEQ_ACCTBALANCE_ID");
            }
            accountDeposit.setAcctBalanceId(acctBalanceId);
            accountDeposit.setAcctId(account.getAcctId());
            accountDeposit.setUserId(mainUser.getUserId());
            accountDeposit.setDepositCode(depositCode);
            accountDeposit.setRecvFee(tradeCommInfoIn.getTradeFee());
            accountDeposit.setInitMoney(accountDeposit.getRecvFee());
            accountDeposit.setIfInAccesslog('1');

            //设置限额方式
            accountDeposit.setLimitMode(StringUtil.firstOfString(tradeCommInfoIn.getLimitMode()));
            if (tradeCommInfoIn.getLimitMoney() <= 0
                    || tradeCommInfoIn.getLimitMoney() >= ActPayPubDef.MAX_LIMIT_FEE) {
                accountDeposit.setLimitMoney(-1);
            } else {
                accountDeposit.setLimitMoney(tradeCommInfoIn.getLimitMoney());  //限额
            }

            //设置账本销账账期范围
            if (ActPayPubDef.MAX_MONTH_NUM == tradeCommInfoIn.getMonths()
                    || tradeCommInfoIn.getMonths() <= 0) {
                accountDeposit.setStartCycleId(ActPayPubDef.MIN_CYCLE_ID);
                accountDeposit.setEndCycleId(ActPayPubDef.MAX_CYCLE_ID);
            } else {
                String tmpDepositStartDate = tradeCommInfoIn.getDepositStartDate();
                accountDeposit.setStartCycleId(Integer.parseInt(tmpDepositStartDate.substring(0, 4) + tmpDepositStartDate.substring(5, 7)));
                accountDeposit.setEndCycleId(TimeUtil.genCycle(accountDeposit.getStartCycleId(), (tradeCommInfoIn.getMonths() - 1)));
                if (accountDeposit.getEndCycleId() > ActPayPubDef.MAX_CYCLE_ID) {
                    accountDeposit.setEndCycleId(ActPayPubDef.MAX_CYCLE_ID);
                }
            }

            //设置账本生失效时间
            accountDeposit.setStartDate(tradeCommInfoIn.getDepositStartDate());
            if (accountDeposit.getEndCycleId() >= ActPayPubDef.MAX_CYCLE_ID) {
                accountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(accountDeposit.getEndCycleId()).getCycEndTime());
            } else {
                accountDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(TimeUtil.genCycle(accountDeposit.getEndCycleId(), 1)).getCycEndTime());
            }

            accountDeposit.setActionCode(0);
            accountDeposit.setProvinceCode(account.getProvinceCode());
            accountDeposit.setEparchyCode(account.getEparchyCode());
            accountDeposit.setPrivateTag(privateTag);
            accountDeposit.setVersionNo(1); // 更新版本号
            accountDeposit.setNewFlag('1'); // 新增标志
            accountDeposit.setValidTag('0');
        } else {
            accountDeposit.setRecvFee(accountDeposit.getRecvFee() + tradeCommInfoIn.getTradeFee());
            accountDeposit.setIfInAccesslog('1');
        }

        //可作为发票金额
        if ('2' != writeOffRuleInfo.depositTypeCode(depositCode)
                && '3' != writeOffRuleInfo.depositTypeCode(depositCode)) {
            if ("2".equals(tradeCommInfoIn.getInvoiceTag())) {
                accountDeposit.setInvoiceFee(accountDeposit.getInvoiceFee() + tradeCommInfoIn.getInvoiceFee());
            } else {
                accountDeposit.setInvoiceFee(accountDeposit.getInvoiceFee() + tradeCommInfoIn.getTradeFee());
            }
            tradeCommInfo.setInvoiceFee(accountDeposit.getAcctBalanceId(), accountDeposit.getInvoiceFee());

        }
        return accountDeposit;
    }

    @Override
    public AccountDeposit genAcctDepositByTransFer(TradeCommInfoIn tradeCommInfoIn, TradeCommInfo tradeCommInfo, AccountDeposit transOutDeposit) {
        //转入目标账本
        int desDepositCode = tradeCommInfoIn.getDepositCode();
        char newPrivateTag = transOutDeposit.getPrivateTag();
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        User mainUser = tradeCommInfo.getMainUser();
        boolean isCompType = isCompType(writeOffRuleInfo, mainUser.getUserId());
        boolean isSharedType = isSharedType(writeOffRuleInfo, mainUser.getUserId());
        CommPara chgDepositPara = getChgDepositPara(writeOffRuleInfo, mainUser.getNetTypeCode(), desDepositCode);
        //转入时融合关系账本公私有属性转换
        if (100015 == tradeCommInfoIn.getPaymentId() && (isCompType || isSharedType) && chgDepositPara != null) {
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

        List<AccountDeposit> accountDeposits = tradeCommInfo.getAccountDeposits();
        List<AccountDeposit> desDeposits = depositService.getAcctDepositsByDepositCode(accountDeposits, desDepositCode);
        AccountDeposit desAccoutDeposit = new AccountDeposit();
        //是否存在可合并账本
        boolean find = false;
        if (!CollectionUtils.isEmpty(desDeposits)) {
            for (AccountDeposit deposit : desDeposits) {
                //失效账本不合并
                if (deposit.getEndCycleId() < writeOffRuleInfo.getCurCycle().getCycleId()) {
                    continue;
                }
                //过户转账转兑帐本不合并
                if ('1' == tradeCommInfoIn.getChgAcctTag() && deposit.getActionCode() > 0) {
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
            desAccoutDeposit.setProvinceCode(tradeCommInfo.getAccount().getProvinceCode());
            desAccoutDeposit.setEparchyCode(tradeCommInfo.getAccount().getEparchyCode());
            desAccoutDeposit.setAcctId(tradeCommInfo.getAccount().getAcctId());
            desAccoutDeposit.setAcctBalanceId(sysCommOperPayService.getSequence(tradeCommInfoIn.getEparchyCode(),
                    ActPayPubDef.SEQ_ACCTBALANCE_ID, tradeCommInfoIn.getProvinceCode()));
            desAccoutDeposit.setUserId(mainUser.getUserId());
            desAccoutDeposit.setDepositCode(desDepositCode);
            desAccoutDeposit.setRecvFee(tradeCommInfoIn.getTradeFee());
            desAccoutDeposit.setInitMoney(tradeCommInfoIn.getTradeFee());
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

            if (StringUtils.isEmpty(tradeCommInfoIn.getDepositStartDate())) {
                tradeCommInfoIn.setDepositStartDate(writeOffRuleInfo.getSysdate());
            } else {
                if (tradeCommInfoIn.getDepositStartDate().length() < 10
                        || !"-".equals(tradeCommInfoIn.getDepositStartDate().substring(4, 5))
                        || !"-".equals(tradeCommInfoIn.getDepositStartDate().substring(7, 8))) {
                    throw new SkyArkException("帐本生效时间不合法!必须为YYYY-MM-DD HH24:MI:SS格式,传入的账本生效时间为" + tradeCommInfoIn.getDepositStartDate());
                }
            }

            desAccoutDeposit.setLimitMoney(tradeCommInfoIn.getLimitMoney());
            desAccoutDeposit.setLimitMode(StringUtil.firstOfString(tradeCommInfoIn.getLimitMode()));

            if (tradeCommInfoIn.getMonths() == ActPayPubDef.MAX_MONTH_NUM ||
                    tradeCommInfoIn.getMonths() <= 0) {
                desAccoutDeposit.setStartCycleId(ActPayPubDef.MIN_CYCLE_ID);
                desAccoutDeposit.setEndCycleId(ActPayPubDef.MAX_CYCLE_ID);
            } else {
                desAccoutDeposit.setStartCycleId(Integer.parseInt(
                        tradeCommInfoIn.getDepositStartDate().substring(0, 4)
                                + tradeCommInfoIn.getDepositStartDate().substring(5, 7)));
                desAccoutDeposit.setEndCycleId(TimeUtil.genCycle(desAccoutDeposit.getStartCycleId(), tradeCommInfoIn.getMonths() - 1));
            }
            desAccoutDeposit.setStartDate(tradeCommInfoIn.getDepositStartDate());
            if (desAccoutDeposit.getEndCycleId() < ActPayPubDef.MAX_CYCLE_ID) {
                desAccoutDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(
                        TimeUtil.genCycle(desAccoutDeposit.getStartCycleId(), 1)).getCycStartTime());
            } else {
                desAccoutDeposit.setEndDate(WriteOffRuleStaticInfo.getCycle(
                        desAccoutDeposit.getStartCycleId()).getCycStartTime());
            }

            //转帐转入
            if (100015 == tradeCommInfoIn.getPaymentId()) {
                desAccoutDeposit.setPrivateTag(newPrivateTag);
                if ('0' == desAccoutDeposit.getPrivateTag()) {
                    desAccoutDeposit.setUserId(tradeCommInfoIn.getUserId());
                } else {
                    //当前台余额转账时 如果转出为私有账本 目标账本的user_id为目标user_id
                    if ("1".equals(tradeCommInfoIn.getTransTag())) {
                        desAccoutDeposit.setUserId(tradeCommInfoIn.getUserId());
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
            desAccoutDeposit.setRecvFee(desAccoutDeposit.getRecvFee() + tradeCommInfoIn.getTradeFee());
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
    public void updateAcctDepositOweFee(List<AccountDeposit> accountDeposits, WriteSnapLog writeSnapLog, int maxAcctCycleId) {
        //往月欠费
        long acctOweFee = writeSnapLog.getAllNewBOweFee();
        //是否存在私有账本
        boolean hasPrivateDeposit = false;
        for (AccountDeposit accountDeposit : accountDeposits) {
            if ('0' == accountDeposit.getPrivateTag()) {
                acctOweFee = accountDeposit.getOweFee();
            } else {
                hasPrivateDeposit = true;
            }
        }

        //没有私有账本，更新账户所有账本的往月欠费字段
        if (!hasPrivateDeposit) {
            for (AccountDeposit deposit : accountDeposits) {
                deposit.setOpenCycleId(maxAcctCycleId);
                deposit.setOweFee(acctOweFee);
            }
        } else {
            for (AccountDeposit deposit : accountDeposits) {
                deposit.setOpenCycleId(maxAcctCycleId);
            }
        }
    }

    @Override
    public void updateAcctDepositInfo(List<AccountDeposit> accountDeposits, WriteOffRuleInfo writeOffRuleInfo, String provinceCode) {
        if (CollectionUtils.isEmpty(accountDeposits)) {
            throw new SkyArkException("没有获取本次缴费对应的账本记录");
        }
        for (AccountDeposit deposit : accountDeposits) {
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
    public void updateDepositInfo(List<AccountDeposit> accountDeposits, long acctOweFee, String sysdate, int maxAcctCycleId) {
        if (CollectionUtils.isEmpty(accountDeposits)) {
            throw new SkyArkException("没有获取本次缴费对应的账本记录");
        }

        //先更新账本余额
        for (AccountDeposit deposit : accountDeposits) {
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
        for (AccountDeposit accountDeposit : accountDeposits) {
            if ('0' == accountDeposit.getPrivateTag()) {
                acctOweFee = accountDeposit.getOweFee();
            } else {
                hasPrivateDeposit = true;
            }
        }

        //没有私有账本，更新账户所有账本的往月欠费字段
        if (!hasPrivateDeposit) {
            for (AccountDeposit deposit : accountDeposits) {
                deposit.setOpenCycleId(maxAcctCycleId);
                deposit.setOweFee(acctOweFee);
            }
        } else {
            for (AccountDeposit deposit : accountDeposits) {
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
