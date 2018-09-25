package com.unicom.acting.pay.backfee.service.impl;

import com.unicom.acting.common.domain.Account;
import com.unicom.acting.common.domain.User;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.FeeCommService;
import com.unicom.acting.pay.backfee.service.BackFeeService;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.domain.*;
import com.unicom.acting.pay.writeoff.service.*;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackFeeServiceImpl implements BackFeeService {
    private Logger logger = LoggerFactory.getLogger(BackFeeServiceImpl.class);
    @Autowired
    private FeeCommService feeCommService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private BackFeeCommService backFeeCommService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private CreditService creditService;

    @Override
    public BackFeeCommInfoOut backFeeEss(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        //查询用户资料
        feeCommService.getUserDatumInfo(backFeeCommInfoIn, tradeCommInfo);
        //查询账期信息
        feeCommService.getEparchyCycleInfo(tradeCommInfo, backFeeCommInfoIn.getEparchyCode(), backFeeCommInfoIn.getProvinceCode());

        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
        if (writeOffRuleInfo.isDrecvPeriod(writeOffRuleInfo.getCurCycle())) {
            throw new SkyArkException("抵扣期间不允许预存清退操作!");
        }

        Account account = tradeCommInfo.getAccount();

        //获取销账规则
        feeCommService.getWriteOffRule(writeOffRuleInfo, account.getProvinceCode(),
                account.getEparchyCode(), account.getNetTypeCode());

        //设置实时费用计算标识
        setDecuctOweFee(backFeeCommInfoIn, writeOffRuleInfo, tradeCommInfo.getMainUser());

        //查询账本
        feeCommService.getAcctBalance(backFeeCommInfoIn, tradeCommInfo);

        //查询账单
        feeCommService.getOweBill(backFeeCommInfoIn, tradeCommInfo);

        //预存清退特殊校验
        specialBackFeeCheck(backFeeCommInfoIn, tradeCommInfo);

        //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
        if (!feeCommService.ifCalcLateFee(backFeeCommInfoIn, tradeCommInfo)) {
            //计算滞纳金
            tradeCommInfo.setCalcLateFee(true);
            //获取滞纳金减免工单
            feeCommService.getFeeDerateLateFeeLog(backFeeCommInfoIn, tradeCommInfo);
            //获取账户自定义缴费期
            feeCommService.getAcctPaymentCycle(tradeCommInfo, account.getAcctId());
        }

        logger.info("begin calc");
        //模拟销账计算
        calculateService.calc(tradeCommInfo);
        //设置账本清退金额
        backFeeCommService.setBackFee(backFeeCommInfoIn, tradeCommInfo);
        logger.info("after setBackFee");
        //清退后销账计算
        calculateService.recvCalc(tradeCommInfo);
        //生成清退入库信息
        backFeeCommService.genBackFeeDBInfo(backFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        //生成短信信息
        smsService.genSmsInfo(backFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
        //生成信控工单
        creditService.genCreditInfo(backFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);

        return genTradeCommInfoOut(backFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
    }


    /**
     * 预存清退特殊校验
     *
     * @param backFeeCommInfoIn
     * @param tradeCommInfo
     */
    private void specialBackFeeCheck(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo) {
        if (feeCommService.ifBillConsigning(backFeeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("托收在途不能前台清退!");
        }

        if (feeCommService.ifPrePrintInvoice(backFeeCommInfoIn, tradeCommInfo)) {
            throw new SkyArkException("代理商预打发票不能前台清退!");
        }
    }

    /**
     * 设置实时费用计算标识
     *
     * @param backFeeCommInfoIn
     * @param writeOffRuleInfo
     * @param mainUser
     */
    private void setDecuctOweFee(BackFeeCommInfoIn backFeeCommInfoIn, WriteOffRuleInfo writeOffRuleInfo, User mainUser) {
        //以参数ASM_IFBACKFEE_NOREALFEE配置为准
        //ASM_IFBACKFEE_NOREALFEE PARA_CODE1 0表示需要扣除实时费用 为1不需要扣除实时费用 PARA_CODE2为需要扣除实时费用的倍数 >=1
        CommPara decuctPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_IFBACKFEE_NOREALFEE);
        if (decuctPara != null) {
            if ("1".equals(decuctPara.getParaCode1())) {
                backFeeCommInfoIn.setDecuctOwefeeTag('1');
            } else {
                if (!"0".equals(mainUser.getPrepayTag())) {
                    backFeeCommInfoIn.setDecuctOwefeeTag('0');
                } else {
                    backFeeCommInfoIn.setDecuctOwefeeTag('1');
                }
            }
        } else {
            backFeeCommInfoIn.setDecuctOwefeeTag('0');
        }

        //合约用户（4转23）立即拆机，预存清退时必须扣除实时话费（remonve_tag=7）
        if ("7".equals(mainUser.getRemoveTag())) {
            backFeeCommInfoIn.setDecuctOwefeeTag('0');
        }
    }

    /**
     * 生成清退应答信息
     *
     * @param backFeeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     * @return
     */
    private BackFeeCommInfoOut genTradeCommInfoOut(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        BackFeeCommInfoOut backFeeCommInfoOut = new BackFeeCommInfoOut();
        //销账规则
        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();

        List<User> defaultUsers = tradeCommInfo.getPayUsers();
        List<FeeAccountDeposit> feeAccountDeposits = tradeCommInfo.getFeeAccountDeposits();
        if (CollectionUtils.isEmpty(feeAccountDeposits)) {
            throw new SkyArkException("该账户无账本");
        }

        List<TradeDepositInfo> backDepositInfos = backFeeCommInfoIn.getBackDepositInfos();
        if (CollectionUtils.isEmpty(backDepositInfos)) {
            throw new SkyArkException("没有清退账本信息");
        }

        //设置清退账本应答信息  需要应答信息考虑在生成可清退账本的时候赋值
        List<BackDepositOutInfo> backDepositOutInfos = new ArrayList(backDepositInfos.size());
        for (TradeDepositInfo depositInfo : backDepositInfos) {
            for (FeeAccountDeposit feeAccountDeposit : feeAccountDeposits) {
                if (depositInfo.getAcctBalanceId().equals(feeAccountDeposit.getAcctBalanceId())) {
                    BackDepositOutInfo backDepositOutInfo = new BackDepositOutInfo();
                    backDepositOutInfo.setAcctBalanceId(feeAccountDeposit.getAcctBalanceId());
                    backDepositOutInfo.setDepositCode(String.valueOf(feeAccountDeposit.getDepositCode()));
                    backDepositOutInfo.setDepositName(writeOffRuleInfo.getDepositName(feeAccountDeposit.getDepositCode()));
                    backDepositOutInfo.setCanUseValue(String.valueOf(feeAccountDeposit.getLeftCanUse()));
                    if (!StringUtil.isEmptyCheckNullStr(feeAccountDeposit)
                            && !"-1".equals(feeAccountDeposit.getUserId())) {
                        for (User user : defaultUsers) {
                            if (user.getUserId().equals(feeAccountDeposit.getUserId())) {
                                backDepositOutInfo.setUserId(user.getUserId());
                                backDepositOutInfo.setSerialNumber(user.getSerialNumber());
                                backDepositOutInfo.setNetTypeCode(user.getNetTypeCode());
                                break;
                            } else {
                                backDepositOutInfo.setUserId(user.getUserId());
                                backDepositOutInfo.setSerialNumber("");
                                backDepositOutInfo.setNetTypeCode("");
                            }
                        }
                    } else {
                        backDepositOutInfo.setUserId(feeAccountDeposit.getUserId());
                        backDepositOutInfo.setSerialNumber("");
                        backDepositOutInfo.setNetTypeCode("");
                    }
                    backDepositOutInfos.add(backDepositOutInfo);
                    break;
                }
            }
        }

        backFeeCommInfoOut.setBackDepositOutInfos(backDepositOutInfos);


        //设置账期信息
        backFeeCommInfoOut.setCurCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
        backFeeCommInfoOut.setMaxAcctCycleId(writeOffRuleInfo.getMaxAcctCycle().getCycleId());

        //设置用户信息
        User mainUser = tradeCommInfo.getMainUser();
        backFeeCommInfoOut.setSerialNumber(mainUser.getSerialNumber());
        backFeeCommInfoOut.setUserId(mainUser.getUserId());
        backFeeCommInfoOut.setBrandCode(mainUser.getBrandCode());

        //设置账户信息
        Account account = tradeCommInfo.getAccount();
        backFeeCommInfoOut.setAcctId(account.getAcctId());
        backFeeCommInfoOut.setPayName(account.getPayName());
        backFeeCommInfoOut.setPayModeCode(account.getPayModeCode());
        backFeeCommInfoOut.setEparchyCode(account.getEparchyCode());

        //缴费日志相关信息
        PayLog payLog = tradeCommResultInfo.getPayLog();
        backFeeCommInfoOut.setChargeId(payLog.getChargeId());
        backFeeCommInfoOut.setRecvFee(String.valueOf(payLog.getRecvFee()));
        backFeeCommInfoOut.setRecvTime(payLog.getRecvTime());

        //设置销账快照信息
        FeeWriteSnapLog writeSnapLog = tradeCommInfo.getFeeWriteSnapLog();
        backFeeCommInfoOut.setSpayFee(String.valueOf(writeSnapLog.getSpayFee()));
        backFeeCommInfoOut.setAllMoney(String.valueOf(writeSnapLog.getAllMoney()));
        backFeeCommInfoOut.setAllNewMoney(String.valueOf(writeSnapLog.getAllNewMoney()));
        backFeeCommInfoOut.setAllBalance(String.valueOf(writeSnapLog.getAllBalance()));
        backFeeCommInfoOut.setAllNewBalance(String.valueOf(writeSnapLog.getAllNewBalance()));
        backFeeCommInfoOut.setAllBOweFee(String.valueOf(writeSnapLog.getAllBOweFee()));
        backFeeCommInfoOut.setAimpFee(String.valueOf(writeSnapLog.getaImpFee()));
        backFeeCommInfoOut.setAllNewBOweFee(String.valueOf(writeSnapLog.getAllNewBOweFee()));
        backFeeCommInfoOut.setPreRealFee(String.valueOf(writeSnapLog.getPreRealFee()));
        backFeeCommInfoOut.setCurRealFee(String.valueOf(writeSnapLog.getCurRealFee()));
        backFeeCommInfoOut.setAllROweFee(String.valueOf(writeSnapLog.getPreRealFee() + writeSnapLog.getCurRealFee()));
        return backFeeCommInfoOut;
    }

}
