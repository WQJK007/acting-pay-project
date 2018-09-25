package com.unicom.acting.pay.bwriteoff.business;

import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.common.exception.ActBSysTypes;
import com.unicom.acting.batch.domain.QryUserDatumIn;
import com.unicom.acting.batch.domain.UserDatum;
import com.unicom.acting.batch.service.BatchGetUserDatum;
import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.domain.TradeCommInfoOut;
import com.unicom.acting.fee.writeoff.domain.UserDatumInfo;
import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.bwriteoff.domain.RecvFeeHolder;
import com.unicom.acting.pay.dao.AccessLogDao;
import com.unicom.acting.pay.dao.BillDao;
import com.unicom.acting.pay.dao.PayLogDao;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.domain.RecvFeeCommInfoIn;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.transaction.annotation.SkyArkTransactional;
import com.unicom.skyark.component.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component("bWriteOffMgr")
public class BWriteOffMgr {
    private static final Logger logger = LoggerFactory.getLogger(BWriteOffMgr.class);

    @Autowired
    BatchGetUserDatum batchGetUserDatum;
    @Autowired
    AccessLogDao accessLogDao;
    @Autowired
    PayLogDao payLogDao;
    @Autowired
    BillDao billDao;
    public void getBUserDatumInfo(BatchRecvFeeIn tradeCommInfoIn, TradeCommInfo tradeCommInfo) {

        //调用微服务查询三户资料
        logger.info("===============调用基础三户资料 begin");
        QryUserDatumIn userDatumIn = new QryUserDatumIn();
        userDatumIn.setAcctId(tradeCommInfoIn.getAcctId());
        UserDatum userDatum =batchGetUserDatum.getUserDatum(userDatumIn);//= datumFeeService.getUserDatumByMS(tradeCommInfoIn);
        logger.info("===============调用基础三户资料 end");
        //充值用户
        tradeCommInfo.setMainUser(userDatum.getMainUser());
        //付费账户
        tradeCommInfo.setAccount(userDatum.getAccount());
        //按用户交易
        if (!StringUtil.isEmptyCheckNullStr(tradeCommInfoIn.getWriteoffMode())
                && "2".equals(tradeCommInfoIn.getWriteoffMode())) {
            if (userDatum.getMainUser() == null) {
                throw new ActBException("按用户交易，未获取交易用户信息!");
            }
            tradeCommInfo.setPayUsers(Collections.singletonList(userDatum.getMainUser()));
            tradeCommInfo.setAllPayUsers(userDatum.getDefaultPayUsers());
            //按用户缴费设置缴费用户标识
            tradeCommInfo.setChooseUserId(Collections.singleton(userDatum.getMainUser().getUserId()));
            tradeCommInfo.setWriteOffMode(Integer.parseInt(tradeCommInfoIn.getWriteoffMode()));
        } else {
            //账户付费用户
            tradeCommInfo.setPayUsers(userDatum.getDefaultPayUsers());
        }
        //请求入参中的号码归属地市和省份编码替换为三户资料查询结果中的地市和省份编码
        tradeCommInfoIn.setProvinceCode(tradeCommInfo.getAccount().getProvinceCode());
        tradeCommInfoIn.setEparchyCode(tradeCommInfo.getAccount().getEparchyCode());
        //大合帐用户
        tradeCommInfoIn.setBigAcctRecvFee(userDatum.isBigAcct());

    }
    public void genRecvDBInfo(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
//        //地市销账规则
//        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
//        //生成交费日志信息
//        PayLog payLog = recvFeeCommService.genRecvPayLog(recvFeeCommInfoIn, tradeCommInfo);
//        tradeCommResultInfo.setPayLog(payLog);
//
//        //生成缴费其他日志表
//        if (recvFeeCommInfoIn.getCarrierInfo() != null
//                && !StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getCarrierInfo().getCarrierId())) {
//            tradeCommResultInfo.setPayOtherLog(genPayOtherLog(recvFeeCommInfoIn.getCarrierInfo(), payLog));
//        }
//
//        //抵扣或者补收期间并且对应临时表中有数据
//        if (tradeCommInfo.isSpecialCycleStatus()) {
//            if (payLog.getExtendTag() != '0') {
//                throw new SkyArkException("月结期间不允许进行异地缴费!");
//            }
//            if (recvFeeCommInfoIn.isTradeCheckFlag()) {
//                //涉及第三方对账只能发送MQ消息
//                tradeCommResultInfo.setPayLogDmnMQInfo(genPayLogDmnMQInfo(recvFeeCommInfoIn, payLog));
//            } else {
//                PayLogDmn payLogDmn = genPayLogDmn(recvFeeCommInfoIn, payLog);
//                tradeCommResultInfo.setPayLogDmn(payLogDmn);
//            }
//            return;
//        }
//
//        //生成缴费日志MQ消息
//        tradeCommResultInfo.setPayLogMQInfo(tradeCommService.genPayLogMQInfo(payLog));
//
//        //更新交费快照信息
//        WriteSnapLog writeSnapLog = tradeCommService.genWriteSnapLog(recvFeeCommInfoIn,
//                tradeCommInfo.getFeeWriteSnapLog(),payLog, writeOffRuleInfo.getCurCycle().getCycleId());
//        tradeCommResultInfo.setWriteSnapLog(writeSnapLog);
//
//        //生成销账日志数据
//        List<WriteOffLog> writeOffLogs = tradeCommService.genWriteOffLog(tradeCommInfo.getFeeWriteOffLogs(), payLog, writeOffRuleInfo);
//        //销账日志入库
//        tradeCommResultInfo.setWriteOffLogs(writeOffLogs);
//
//
//        //生成取款日志数据
//        List<AccessLog> accessLogs = tradeCommService.genAccessLog(tradeCommInfo, payLog, writeOffLogs);
//        tradeCommResultInfo.setAccessLogs(accessLogs);
//
//        //生成取款日志MQ信息
//        if (!CollectionUtils.isEmpty(accessLogs)) {
//            tradeCommResultInfo.setAccessLogMQInfos(tradeCommService.genAccessLogMQInfo(accessLogs));
//        }
//
//        //生成代收费日志
//        List<CLPayLog> clPayLogs = tradeCommService.genCLPaylog(writeOffLogs, payLog);
//        tradeCommResultInfo.setClPayLogs(clPayLogs);
//
//        //更新存折入库信息
//        List<FeeAccountDeposit> deposits = tradeCommInfo.getFeeAccountDeposits();
//        acctDepositPayService.updateDepositInfo(deposits, writeSnapLog.getAllNewBOweFee(),
//                writeOffRuleInfo.getSysdate(), writeOffRuleInfo.getMaxAcctCycle().getCycleId());
//        tradeCommResultInfo.setDepositMQInfos(acctDepositPayService.genDepositMQInfo(deposits));
    }
    public TradeCommInfoOut genTradeCommInfoOut(RecvFeeCommInfoIn recvFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo) {
        TradeCommInfoOut tradeCommInfoOut = new TradeCommInfoOut();
//        //销账规则
////        WriteOffRuleInfo writeOffRuleInfo = tradeCommInfo.getWriteOffRuleInfo();
////
////        //设置账期信息
////        tradeCommInfoOut.setCurCycleId(writeOffRuleInfo.getCurCycle().getCycleId());
////        tradeCommInfoOut.setMaxAcctCycleId(writeOffRuleInfo.getMaxAcctCycle().getCycleId());
////
////        //设置用户信息
////        User mainUser = tradeCommInfo.getMainUser();
////        tradeCommInfoOut.setSerialNumber(mainUser.getSerialNumber());
////        tradeCommInfoOut.setNetTypeCode(mainUser.getNetTypeCode());
////        tradeCommInfoOut.setUserId(mainUser.getUserId());
////        tradeCommInfoOut.setBrandCode(mainUser.getBrandCode());
////
////        //设置账户信息
////        FeeAccount feeAccount = tradeCommInfo.getFeeAccount();
////        tradeCommInfoOut.setAcctId(feeAccount.getAcctId());
////        tradeCommInfoOut.setPayName(feeAccount.getPayName());
////        tradeCommInfoOut.setPayModeCode(feeAccount.getPayModeCode());
////        tradeCommInfoOut.setEparchyCode(feeAccount.getEparchyCode());
////        tradeCommInfoOut.setProvinceCode(recvFeeCommInfoIn.getProvinceCode());
////
////        // 大合账优化，为防止初期启动时无返回值造成实时接口报错，所以临时把相关字段都赋值为0，并不代表真实值。
////        // 如开关启动，需细化每一个缴费接口的处理模式，尤其是返回字段如何处理。
////        if (recvFeeCommInfoIn.isBigAcctRecvFee()) {
////            tradeCommInfoOut.setOuterTradeId(recvFeeCommInfoIn.getTradeId());
////            tradeCommInfoOut.setChargeId(recvFeeCommInfoIn.getChargeId());
////            tradeCommInfoOut.setPayChargeId(recvFeeCommInfoIn.getChargeId());
////            tradeCommInfoOut.setRecvFee(String.valueOf(recvFeeCommInfoIn.getTradeFee()));
////            tradeCommInfoOut.setExtendTag("0");
////            tradeCommInfoOut.setSpayFee("0");
////            tradeCommInfoOut.setAllMoney("0");
////            tradeCommInfoOut.setAllNewMoney("0");
////            tradeCommInfoOut.setAllBalance("0");
////            tradeCommInfoOut.setAllNewBalance("0");
////            tradeCommInfoOut.setAllBOweFee("0");
////            tradeCommInfoOut.setAimpFee("0");
////            tradeCommInfoOut.setAllNewBOweFee("0");
////            tradeCommInfoOut.setPreRealFee("0");
////            tradeCommInfoOut.setCurRealFee("0");
////            tradeCommInfoOut.setAllROweFee("0");
////            tradeCommInfoOut.setRsrvStr18("0");
////            tradeCommInfoOut.setRsrvDate("00000000000000");
////            tradeCommInfoOut.setAcctBalanceId1("0");
////            tradeCommInfoOut.setResFee("0");
////            return tradeCommInfoOut;
////        }
////
////        //缴费日志相关信息
////        PayLog payLog = tradeCommResultInfo.getPayLog();
////        //如果是关联缴费需要主记录的流水
////        if (!StringUtil.isEmptyCheckNullStr(recvFeeCommInfoIn.getRelChargeId())) {
////            tradeCommInfoOut.setChargeId(recvFeeCommInfoIn.getRelChargeId());
////        } else {
////            tradeCommInfoOut.setChargeId(recvFeeCommInfoIn.getChargeId());
////        }
////
////        tradeCommInfoOut.setPayChargeId(payLog.getChargeId());
////        tradeCommInfoOut.setOuterTradeId(payLog.getOuterTradeId());
////        tradeCommInfoOut.setRecvFee(String.valueOf(payLog.getRecvFee()));
////        tradeCommInfoOut.setExtendTag(String.valueOf(payLog.getExtendTag()));
////
////        //设置销账快照信息
////        FeeWriteSnapLog writeSnapLog = tradeCommInfo.getFeeWriteSnapLog();
////        tradeCommInfoOut.setSpayFee(String.valueOf(writeSnapLog.getSpayFee()));
////        tradeCommInfoOut.setAllMoney(String.valueOf(writeSnapLog.getAllMoney()));
////        tradeCommInfoOut.setAllNewMoney(String.valueOf(writeSnapLog.getAllNewMoney()));
////        tradeCommInfoOut.setAllBalance(String.valueOf(writeSnapLog.getAllBalance()));
////        tradeCommInfoOut.setAllNewBalance(String.valueOf(writeSnapLog.getAllNewBalance()));
////        tradeCommInfoOut.setAllBOweFee(String.valueOf(writeSnapLog.getAllBOweFee()));
////        tradeCommInfoOut.setAimpFee(String.valueOf(writeSnapLog.getaImpFee()));
////        tradeCommInfoOut.setAllNewBOweFee(String.valueOf(writeSnapLog.getAllNewBOweFee()));
////        tradeCommInfoOut.setPreRealFee(String.valueOf(writeSnapLog.getPreRealFee()));
////        tradeCommInfoOut.setCurRealFee(String.valueOf(writeSnapLog.getCurRealFee()));
////        tradeCommInfoOut.setAllROweFee(String.valueOf(writeSnapLog.getPreRealFee() + writeSnapLog.getCurRealFee()));
////
////        //统一余额播报
////        CommPara commPara = writeOffRuleInfo.getCommpara(ActingPayCommparaDef.ASM_SHOW_TYPE);
////        if (commPara == null) {
////            throw new SkyArkException("没有配置统一余额播报方案参数:ASM_SHOW_TYPE");
////        }
////
////        String contactType = "";
////        if (!StringUtil.isEmptyCheckNullStr(commPara.getParaCode1())) {
////            contactType = commPara.getParaCode1();
////        }
////        tradeCommInfoOut.setConTactType(contactType);
////
////        List<FeeAccountDeposit> depositList = tradeCommInfo.getFeeAccountDeposits();
////        //账户当前可用余额
////        long rsrvFee18 = 0;
////        //储备账本余额
////        long storeFee = 0;
////        for (FeeAccountDeposit actDeposit : depositList) {
////            if ("2".equals(recvFeeCommInfoIn.getWriteoffMode())
////                    && ('0' == actDeposit.getPrivateTag() || actDeposit.getUserId().equals(mainUser.getUserId()))) {
////                if ('0' == actDeposit.getDepositTypeCode() || '2' == actDeposit.getDepositTypeCode()) {
////                    rsrvFee18 += actDeposit.getLeftCanUse();
////                }
////            } else {
////                if ('0' == actDeposit.getDepositTypeCode() || '2' == actDeposit.getDepositTypeCode()) {
////                    rsrvFee18 += actDeposit.getLeftCanUse();
////                }
////            }
////
////            if ('1' == actDeposit.getDepositTypeCode() || '3' == actDeposit.getDepositTypeCode()) {
////                storeFee += actDeposit.getMoney() + actDeposit.getRecvFee() - actDeposit.getImpFee() - actDeposit.getUseRecvFee();
////            }
////
////        }
////        //账户当前可用余额
////        tradeCommInfoOut.setRsrvStr18(String.valueOf(rsrvFee18));
////        //储备金额
////        tradeCommInfoOut.setResFee(String.valueOf(storeFee));
////        //本次缴费的帐本标识返回
////        tradeCommInfoOut.setAcctBalanceId1(recvFeeCommInfoIn.getAcctBalanceId());
////
////        //交易时间
////        if (!StringUtil.isEmpty(payLog.getRecvTime())) {
////            tradeCommInfoOut.setRsrvDate(
////                    payLog.getRecvTime().substring(0, 4) + payLog.getRecvTime().substring(5, 7)
////                            + payLog.getRecvTime().substring(8, 10) + payLog.getRecvTime().substring(11, 13)
////                            + payLog.getRecvTime().substring(14, 16) + payLog.getRecvTime().substring(17, 19));
////        }
        return tradeCommInfoOut;
    }
    @SkyArkTransactional(DbTypes.ACTING_DRDS)
    public void recvFeeLogIndb(TradeCommResultInfo tradeCommResultInfo) {
        accessLogDao.insertAccessLog(tradeCommResultInfo.getAccessLogs());
        payLogDao.insertPayLog(tradeCommResultInfo.getPayLog());
    }
    public void setResultInfo(RecvFeeHolder<BatchRecvFeeIn> recvFeeHolder, int resultCode, String dealTag, String errInfo, String remark)
    {
        if(recvFeeHolder.getCurrentInstance()!= null) {
            logger.info("mark count = {}",recvFeeHolder.getOrderNumber());
            BatchRecvFeeIn recvFeeIn = recvFeeHolder.getCurrentInstance();
            recvFeeIn.setDealtag(ActBSysTypes.DEALED_TAG_ERROR);
            recvFeeIn.setResultCode(ActBSysTypes.RESULT_ERROR);
            recvFeeIn.setResultInfo(errInfo);
            recvFeeIn.setRemark(remark);
        }
    }

}
