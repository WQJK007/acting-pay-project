package com.unicom.acting.pay.writeoff.service.impl;

import com.unicom.acting.fee.domain.*;
import com.unicom.acting.fee.writeoff.service.CommParaFeeService;
import com.unicom.acting.fee.writeoff.service.CycleService;
import com.unicom.acting.fee.writeoff.service.SysCommOperFeeService;
import com.unicom.acting.pay.dao.*;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.dao.*;
import com.unicom.acting.pay.writeoff.domain.*;
import com.unicom.acting.pay.writeoff.service.CancelTradePayService;
import com.unicom.acting.pay.writeoff.service.PayDatumService;
import com.unicom.skyark.component.common.constants.SysTypes;
import com.unicom.skyark.component.exception.SkyArkException;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author ducj
 */
@Service
public class CancelTradePayServiceImpl implements CancelTradePayService {

    private Logger logger = LoggerFactory.getLogger(CancelTradePayServiceImpl.class);

    /**以下为acthing.fee.writeoff.service下的类**/
    @Autowired
    private CommParaFeeService commParaFeeService;
    @Autowired
    private CycleService cycleService;
    @Autowired
    private SysCommOperFeeService sysCommOperFeeService;

    /**以下为acthing.pay.writeoff.service下的类**/
    @Autowired
    private PayDatumService payDatumService;

    /**以下为acting.pau.writeoff.dao包里定义的账户中心的dao, 其内均只有select方法，无任何提交类的方法**/
    @Autowired
    private PayLogActsDao payLogActsDao;
    @Autowired
    private AccesslogActsDao accesslogActsDao;
    @Autowired
    private PrintInfoActsDao printInfoActsDao;
    @Autowired
    private DepositActsDao depositActsDao;
    @Autowired
    private DiscntDepositActsDao discntDepositActsDao;

    /**以下为acting.pay.dao包里定义的账户中心的dao,期内包含了select和insert、update方法**/
    @Autowired
    private ChargeRelationDao chargeRelationDao;
    @Autowired
    private TransLogDao transLogDao;
    @Autowired
    private BillDao billDao;
    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private AccessLogDao accessLogDao;
    @Autowired
    private WriteOffLogDao writeOffLogDao;
    @Autowired
    private WriteSnapLogDao writeSnapLogDao;

    @Override
    public CancelRecvFeeInfo buildCancelTradeInfoCommon(CancelRecvFeeInfoIn cancelRecvFeeInfoIn) {

        logger.info("校验重复交易..........");
        String currOuterTradeId = cancelRecvFeeInfoIn.getCurrOuterTradeId();
        this.repeatTradeCheck(currOuterTradeId);

        logger.debug("校验交费日志..........");
        String origOuterTradeId = cancelRecvFeeInfoIn.getOrigOuterTradeId();
        CancelRecvFeeInfo cancelRecvFeeInfo = payLogCheck(origOuterTradeId);

        logger.debug("校验打印日志..........");
        String acctId = cancelRecvFeeInfo.getAcctId();
        String origChargeId = cancelRecvFeeInfo.getOrigChargeId();
        this.printlogCheck(origChargeId, acctId);

        logger.debug("校验可打金额..........");
        this.canPrintFeeCheck(origChargeId, acctId);

        logger.debug("校验特殊交费期..........");
        this.eparchyCycleCheck(cancelRecvFeeInfo);

        logger.debug("构建返销数据..........");
        this.buildCancelTradeInfo(cancelRecvFeeInfoIn, cancelRecvFeeInfo);

        logger.info("返销前校验结束..........");
        return cancelRecvFeeInfo;
    }


    /**
     * 重复流水校验
     * @param currOuterTradeId  当前交易外围流水
     */
    public void repeatTradeCheck(String currOuterTradeId)
    {
        //????此时没有分库键，需要outer_Trade_id索引
        if(payLogDao.ifExistOuterTradeId(currOuterTradeId)){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "该笔流水已经进行过交易，无法发起返销！");
        }
        //???dmn表是否核查，若不验证，最后的补收肯定需要验证
    }


    /**
     * 根据原交费的outer_trade_id在账务中心获取交费记录，账户中心校验流水是否存在
     * @param origOuterTradeId  原交费外围流水
     * @return 返销交易数据
     */
    public CancelRecvFeeInfo payLogCheck(String origOuterTradeId) {
        CancelRecvFeeInfo cancelRecvFeeInfo = new CancelRecvFeeInfo();
        List<PayLog> payLogs = payLogDao.getPaylogByOuterTradeId(origOuterTradeId);
        if(!CollectionUtils.isEmpty(payLogs)){
            PayLog payLogTmp = payLogs.get(0);
            if('0' == payLogTmp.getCancelTag()){
                if(payLogActsDao.ifExistOuterTradeId(origOuterTradeId, payLogTmp.getAcctId())){
                    //cancelRecvFeeInfo.setOrigOuterTradeId(origOuterTradeId);//后有公共一起赋值的地方，无需多此一举
                    cancelRecvFeeInfo.setProvinceId(payLogTmp.getProvinceCode());
                    //此处用交费记录中的信息覆盖，保证归属地的正确
                    cancelRecvFeeInfo.setEparchyid(payLogTmp.getEparchyCode());
                    //此处用交费记录中的信息覆盖，保证归属地的正确
                    cancelRecvFeeInfo.setAcctId(payLogTmp.getAcctId());
                    // 保证查询时的分库键正确
                    cancelRecvFeeInfo.setOrigChargeId(payLogTmp.getChargeId());
                    cancelRecvFeeInfo.setPayLog(payLogTmp);
                    return cancelRecvFeeInfo;
                }else{
                    throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "账户中心未获取到原缴费日志 outerTradeId:" + origOuterTradeId +"  acctId:"+ payLogTmp.getAcctId());
                }
            } else if('1' == payLogTmp.getCancelTag()){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "原缴费已被返销 outerTradeId:" + origOuterTradeId +"  acctId:"+ payLogTmp.getAcctId());
            } else {
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "根据流水未获取到原缴费日志 outerTradeId:" + origOuterTradeId +"  acctId:"+ payLogTmp.getAcctId());
            }
        } else {
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "根据流水未获取到原缴费日志 outerTradeId:" + origOuterTradeId);
        }
    }


    /**
     * 验证是否已打发票
     * @param chargeId 交费流水
     * @param acctId 账户标识
     */
    public void printlogCheck(String chargeId, String acctId){
        List<PrintInfo> printInfos = printInfoActsDao.getPrintInfoByChargeId(chargeId, acctId);
        if(!CollectionUtils.isEmpty(printInfos)){
            for(PrintInfo printInfo : printInfos){
                if(printInfo.getFee() > 0){
                    throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "该缴费已打发票, 请先返销关联的发票. chargeId:" + chargeId +"  acctId:"+ acctId);
                }
            }
        }
    }

    /**
     * 验证可打金额是否足够返销
     * @param origChargeId  原返销交费流水
     * @param acctId 账户标识
     */
    public void canPrintFeeCheck(String origChargeId, String acctId){
        //获取存取款日志,抵扣的存取款日志并没有取，保留的现状
        List<AccessLog> accessLogs = accessLogDao.getOrigAccesslogsByAcctIdAndChargeId(acctId, origChargeId);
        if(!CollectionUtils.isEmpty(accessLogs)) {
            if (!accesslogActsDao.ifExistAccesslogByAcctIdAndChargeId(acctId, origChargeId)) {
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "账户中心未找到对应的存取款日志 chargeId:" + origChargeId + "  acctId:" + acctId);
            }
        }else{
            return;
        }
        //是否需要校验可打金额
        if(ifSkipCheckPrintFee()){
            return ;
        }
        //根据存取款日志获取对应的账本可打金额
        for(AccessLog accessLogTmp : accessLogs){
            if('0' == accessLogTmp.getAccessTag() && accessLogTmp.getInvoiceFee() > 0){
                //只有增加可打金额的才校验，不增加可打金额的不校验，判断改为按增加的可打金额来判断
                List<AccountDeposit> accountDepositList = depositActsDao.getDepositCanPrintFeeByAcctBalanceIdAndAcctId(accessLogTmp.getAcctBalanceId(),
                        acctId );
                if(!CollectionUtils.isEmpty(accountDepositList)){
                    if('0' == accountDepositList.get(0).getValidTag() ){
                        //只对交费充值到的账本做了校验，如果账本可打金额使用了的话，会限制返销
                        if(accessLogTmp.getInvoiceFee() > accountDepositList.get(0).getInvoiceFee() - accountDepositList.get(0).getPrintFee()){
                            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "可打金额不足,不允许缴费返销 acctBalanceId:" +
                                    accessLogTmp.getAcctBalanceId() +"  acctId:"+ acctId);
                        }
                    }else{
                        throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "没有找到对应的有效账本 acctBalanceId:" +
                                accessLogTmp.getAcctBalanceId() +"  acctId:"+ accessLogTmp.getAcctId());
                    }
                }else{
                    throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "没有找到对应的账本 acctBalanceId:" +
                            accessLogTmp.getAcctBalanceId() +"  acctId:"+ accessLogTmp.getAcctId());
                }
                return ;
            }
        }
    }


    /**
     * 过滤某种形式的交费记录，校验可打金额
     * @return 是否忽略
     */
    public boolean ifSkipCheckPrintFee(){
        CommPara commPara = commParaFeeService.getCommpara(ActingPayCommparaDef.ASM_NOPRINTFEE_CANCELLIMIT, WriteOffRuleInfo.DEFAULT_PROVINCE_CODE,
                WriteOffRuleInfo.DEFAULT_EPARCHY_CODE);
        if(commPara != null && "1".equals(commPara.getParaCode1())){
            return false;
        }
        return true;
    }

    /**
     * 校验特殊交费期
     * @param cancelRecvFeeInfo 返销交易数据
     */
    public void eparchyCycleCheck(CancelRecvFeeInfo cancelRecvFeeInfo) {
        genEparchyCycleInfo(cancelRecvFeeInfo);
        if(cycleService.isDrecvPeriod(cancelRecvFeeInfo.getCurCycle())){
            throw new SkyArkException("抵扣期不允许发起返销!");
        }
    }

    /**
     * 获取账期数据
     * @param cancelRecvFeeInfo 返销交易数据
     */
    private void genEparchyCycleInfo(CancelRecvFeeInfo cancelRecvFeeInfo){
        String provinceCode = cancelRecvFeeInfo.getProvinceId();
        String eparchyCode = cancelRecvFeeInfo.getEparchyid();
        String sysdate = sysCommOperFeeService.getSysdate(TimeUtil.DATETIME_FORMAT);
        String day = sysdate.substring(8, 10);
        Cycle curCycle = null;
        CommPara commPara = commParaFeeService.getCommpara(ActingPayCommparaDef.ASM_AUXACCTSTATUS_FROMCACHE,
                provinceCode, eparchyCode);
        if (commPara != null && "1".equals(commPara.getParaCode1())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode2())
                && !StringUtil.isEmptyCheckNullStr(commPara.getParaCode3())
                && day.compareTo(commPara.getParaCode2()) >= 0
                && day.compareTo(commPara.getParaCode3()) <= 0) {
            curCycle = cycleService.getCacheCurCycle(eparchyCode);
        } else {
            curCycle = cycleService.getCurCycle(eparchyCode);
        }
        if (curCycle == null) {
            throw new SkyArkException("没有取到当前帐期!");
        }
        cancelRecvFeeInfo.setCurCycle(curCycle);
        cancelRecvFeeInfo.setSysDate(sysdate);
    }


    /**
     * 根据入参构建返销交易数据 - 目前ess使用
     * @param cancelRecvFeeInfoIn 返销输入对象
     * @param cancelRecvFeeInfo 返销交易数据
     */
    private void buildCancelTradeInfo(CancelRecvFeeInfoIn cancelRecvFeeInfoIn, CancelRecvFeeInfo cancelRecvFeeInfo) {
        buildCancelTradeInfoPublic(cancelRecvFeeInfoIn, cancelRecvFeeInfo);
        /*cancelRecvFeeInfo.setCurrChargeId(cancelRecvFeeInfoIn.getCurrChargeId());
        cancelRecvFeeInfo.setCrmDestoryTag(cancelRecvFeeInfoIn.getCrmDestoryTag());
        cancelRecvFeeInfo.setActionCode(cancelRecvFeeInfoIn.getActionCode());
        cancelRecvFeeInfo.setItemId(cancelRecvFeeInfoIn.getItemId());
        cancelRecvFeeInfo.setSpecialDoFlag("");*/
    }

    /**
     * 根据入参构建返销交易数据 - 外围xxxx使用
     * @param cancelRecvFeeInfoIn 返销输入对象
     * @param cancelRecvFeeInfo 返销交易数据
     */
    private void buildCancelTradeInfoForElse(CancelRecvFeeInfoIn cancelRecvFeeInfoIn, CancelRecvFeeInfo cancelRecvFeeInfo) {
        buildCancelTradeInfoPublic(cancelRecvFeeInfoIn, cancelRecvFeeInfo);
    }

    /**
     * 组织交费返销所使用的公共信息--理应是核心所必须的字段，即所有微服务都会传的公共字段
     * @param cancelRecvFeeInfoIn 返销输入对象
     * @param cancelRecvFeeInfo 返销交易数据
     */
    private void buildCancelTradeInfoPublic(CancelRecvFeeInfoIn cancelRecvFeeInfoIn, CancelRecvFeeInfo cancelRecvFeeInfo) {
        //cancelRecvFeeInfoIn
        cancelRecvFeeInfo.setOrigOuterTradeId(cancelRecvFeeInfoIn.getOrigOuterTradeId());
        cancelRecvFeeInfo.setOrigOuterTradeTime(cancelRecvFeeInfoIn.getOrigOuterTradeTime());
        cancelRecvFeeInfo.setCurrOuterTradeId(cancelRecvFeeInfoIn.getCurrOuterTradeId());
        cancelRecvFeeInfo.setCancelFee(cancelRecvFeeInfoIn.getCancelFee());
        cancelRecvFeeInfo.setAgentTag(cancelRecvFeeInfoIn.getAgentTag());
        cancelRecvFeeInfo.setTag(cancelRecvFeeInfoIn.getTag());
        cancelRecvFeeInfo.setReqSrc(cancelRecvFeeInfoIn.getReqSrc());
        cancelRecvFeeInfo.setUserId(cancelRecvFeeInfoIn.getUserId());
        cancelRecvFeeInfo.setOperateType(cancelRecvFeeInfoIn.getOperateType());
        cancelRecvFeeInfo.setRemark(cancelRecvFeeInfoIn.getRemark());
        cancelRecvFeeInfo.setChannelId(cancelRecvFeeInfoIn.getChannelId());
        TradeStaff tradeStaff = new TradeStaff();
        tradeStaff.setCityCode(cancelRecvFeeInfoIn.getTradeCityCode());
        tradeStaff.setDepartId(cancelRecvFeeInfoIn.getTradeDepartId());
        tradeStaff.setEparchyCode(cancelRecvFeeInfoIn.getTradeEparchyCode());
        tradeStaff.setProvinceCode(cancelRecvFeeInfoIn.getTradeProvinceCode());
        tradeStaff.setStaffId(cancelRecvFeeInfoIn.getTradeStaffId());
        cancelRecvFeeInfo.setTradeTradeStaff(tradeStaff);
    }


    /**
     * 返销主流程  -- acting.pay.writeoff
     * @param cancelRecvFeeInfo 返销交易数据
     * @return 返销输出对象
     */
    @Override
    public CancelRecvFeeInfoOut cancelFee(CancelRecvFeeInfo cancelRecvFeeInfo) {
        logger.info("cancelFee begin..........");
        Set<String> origChargeIdSet = genOrigChargeidSet(cancelRecvFeeInfo);
        logger.info("生成待返销的记录size:"+ origChargeIdSet.size() + "..........");

        //非抵扣期账户加锁
        if(isSpecialRecvState(cancelRecvFeeInfo.getCurCycle()) && !"1".equals(cancelRecvFeeInfo.getSpecialDoFlag())) {
            logger.debug("账户加锁..........");
            payDatumService.genLockAccount(cancelRecvFeeInfo.getAcctId(), cancelRecvFeeInfo.getProvinceId());
        }
        CancelRecvFeeInfoOut cancelRecvFeeInfoOut = new CancelRecvFeeInfoOut();
        for (String origChargeId : origChargeIdSet)
        {
            logger.info("返销原缴费chargeId:" + origChargeId + "..........");
            cancelRecvFeeInfo.setOrigChargeId(origChargeId);
            logger.info("cancelFeeCore begin..........");
            this.cancelFeeCore(cancelRecvFeeInfo, cancelRecvFeeInfoOut);
            logger.info("cancelFeeCore end..........");
        }
        cancelRecvFeeInfoOut.setTradeTradeStaff(cancelRecvFeeInfo.getTradeTradeStaff());
        cancelRecvFeeInfoOut.setOrigChargeId(cancelRecvFeeInfo.getOrigChargeId());
        cancelRecvFeeInfoOut.setCurrOuterTradeId(cancelRecvFeeInfo.getCurrOuterTradeId());
        return cancelRecvFeeInfoOut;
    }


    /**
     * 生成待返销的charge_id
     *
     * @param cancelRecvFeeInfo 交费数据整理
     * @throws SkyArkException 天舟平台公共异常类
     */
    public Set<String> genOrigChargeidSet(CancelRecvFeeInfo cancelRecvFeeInfo) {
        logger.debug("获取返销的缴费记录..........");
        //String acctId = cancelRecvFeeInfo.getAcctId();
        //String provinceId = cancelRecvFeeInfo.getProvinceId();
        Set<String> chargeIdSet = new HashSet<>();
        if("2".equals(cancelRecvFeeInfo.getCrmDestoryTag())){
            /* 获取活动实例，先干掉，前台不会涉及
            String userId = cancelRecvFeeInfo.getUserId();
            String itemId = cancelRecvFeeInfo.getItemId();
            String actionCode = cancelRecvFeeInfo.getActionCode();
            List<DiscntDeposit> discntDeposits = discntDepositActsDao.getDiscntDepositsByUserIdAndAcctId(userId, acctId, provinceId);
            for(DiscntDeposit discntDepositTmp : discntDeposits){
                if(actionCode.equals(discntDepositTmp.getActionCode()) && itemId.equals(discntDepositTmp.getDiscountCode())
                        && !StringUtil.isEmptyCheckNullStr(discntDepositTmp.getChargeId()) && !chargeIdSet.contains(discntDepositTmp.getChargeId())){
                    chargeIdSet.add(discntDepositTmp.getChargeId());
                }
            }
            if(CollectionUtils.isEmpty(chargeIdSet)){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "用户没有可以返销的转兑包!userId:" + userId + "  actionCode:" + actionCode);
            }
            cancelRecvFeeInfo.setOrigOuterTradeId("");//将原交费的outerTradeId置空
            */
        }else{
            String origChargeId = cancelRecvFeeInfo.getOrigChargeId();
            //关联查询chargeRelatiaon表的查询先暂时可以去掉
            /*if(!cancelRecvFeeInfo.getEparchyid().equals(cancelRecvFeeInfo.getTradeTradeStaff().getEparchyCode())){
                List<PayLog> payLogs = this.getRelationPaylogsByChargeIdAndAcctId(origChargeId, acctId, provinceId);
                if(!CollectionUtils.isEmpty(payLogs) && payLogs.size() == 1){
                    chargeIdSet.add(payLogs.get(0).getChargeId());
                    return chargeIdSet;
                }
            }*/
            chargeIdSet.add(origChargeId);
        }
        return chargeIdSet;
    }


    @Override
    public CancelRecvFeeInfoOut cancelFeeCore(CancelRecvFeeInfo cancelRecvFeeInfo, CancelRecvFeeInfoOut cancelRecvFeeInfoOut) {
        String origChargeId = cancelRecvFeeInfo.getOrigChargeId();
        String acctId = cancelRecvFeeInfo.getAcctId();
        String provinceId = cancelRecvFeeInfo.getProvinceId();
        //if 抵扣期返销  else  正常返销
        if(isSpecialRecvState(cancelRecvFeeInfo.getCurCycle()) && !"1".equals(cancelRecvFeeInfo.getSpecialDoFlag())){
            PayLogDmn paylogDmn = this.genPaylogDmn(cancelRecvFeeInfo.getPayLog(), origChargeId, acctId);
            paylogDmn.setTradeTime(cancelRecvFeeInfo.getSysDate());
            //此处和原c++代码不同
            paylogDmn.setTradeCityCode(cancelRecvFeeInfo.getTradeTradeStaff().getCityCode());
            paylogDmn.setTradeDepartId(cancelRecvFeeInfo.getTradeTradeStaff().getDepartId());
            paylogDmn.setTradeEparchyCode(cancelRecvFeeInfo.getTradeTradeStaff().getEparchyCode());
            paylogDmn.setTradeStaffId(cancelRecvFeeInfo.getTradeTradeStaff().getStaffId());
            //组织返回的数据和入库的数据 ， 没有加重复判断
            cancelRecvFeeInfoOut.getPaylogDmnList().add(paylogDmn);
            cancelRecvFeeInfoOut.setAcctId(paylogDmn.getAcctId());
            cancelRecvFeeInfoOut.setUserId(paylogDmn.getUserId());
            cancelRecvFeeInfoOut.setSerialNumber(paylogDmn.getSerialNumber());
            cancelRecvFeeInfoOut.setChargeId("");
            cancelRecvFeeInfoOut.setCancelChargeId(paylogDmn.getTradeId());
            //是否隔笔返销判断
            cancelRecvFeeInfoOut.setResultTag(0);
        } else {
            List<PayLog> payLogs = new ArrayList<>();
            //if("1".equals(cancelRecvFeeInfo.getCrmDestoryTag())){
            //}else if("2".equals(cancelRecvFeeInfo.getCrmDestoryTag()) || StringUtil.isEmptyCheckNullStr(origOuterTradeId)){
            //}else{
            String origOuterTradeId = cancelRecvFeeInfo.getOrigOuterTradeId();
            payLogs = payLogDao.getPaylogByOuterTradeIdAndAcctId(origOuterTradeId, acctId);
            if(CollectionUtils.isEmpty(payLogs)){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "没有获取到对应的缴费记录！outerTradeId:" + origOuterTradeId + "  acctId: " + acctId);
            }
            //关联查询chargeRelatiaon表的查询先暂时可以去掉
            /*if(origOuterTradeId.length() == 30){
                payLogs.addAll(this.getRelationPaylogsByChargeIdAndAcctId(payLogs.get(0).getChargeId(), acctId, provinceId));//ESS 缴费返销，由于ESS关联缴费只有第一笔缴费有总部流水. 所以必须取一下缴费关系才能保证全部返销
            }
            }*/
            //新产品首冲判断
            this.translogCheck(payLogs);
            //循环返销
            int ret = 0;
            String currOuterTradeId = cancelRecvFeeInfo.getCurrOuterTradeId();
            cancelRecvFeeInfo.setCurrOuterTradeId("");
            for(int i = payLogs.size()-1; i >= 0; --i ){
                PayLog paylogCancel = payLogs.get(i);
                //最开始的一条 和 转帐（转入）
                if(i == 0 || 100015 == paylogCancel.getPaymentId()){
                    cancelRecvFeeInfo.setCurrOuterTradeId(currOuterTradeId);
                }
                //返销业务日志
                logger.info("cancelRecvfeelog begin..........");
                this.cancelRecvfeelog(paylogCancel, cancelRecvFeeInfo, cancelRecvFeeInfoOut);
                logger.info("cancelRecvfeelog end..........");
                //判断是否为隔笔返销
                int retTmp = isLastAccesslog(acctId, paylogCancel.getRecvTime());
                //获取最大值
                ret = ret > retTmp ? ret : retTmp;
            }
            //隔笔返销限制
            if(ret > 0){
                this.intervalTradLimit(provinceId, cancelRecvFeeInfo.getEparchyid());
            }
            //把交易对象CancelRecvFeeInfo覆盖掉的数据重新赋值
            cancelRecvFeeInfo.setCurrOuterTradeId(currOuterTradeId);
            //组织待返回的非入库数据
            cancelRecvFeeInfoOut.setResultTag(ret);
            cancelRecvFeeInfoOut.setAcctId(acctId);
            cancelRecvFeeInfoOut.setSerialNumber("");
            cancelRecvFeeInfoOut.setUserId(payLogs.get(0).getUserId());
            cancelRecvFeeInfoOut.setPaymentId(payLogs.get(0).getPaymentId());
            cancelRecvFeeInfoOut.setPayfeeModeCode(payLogs.get(0).getPayFeeModeCode());
            cancelRecvFeeInfoOut.setChannelId(cancelRecvFeeInfo.getChannelId());
            if(StringUtil.isEmptyCheckNullStr(cancelRecvFeeInfoOut.getChannelId())){
                cancelRecvFeeInfoOut.setChannelId(payLogs.get(0).getChannelId());
            }
            //cancelRecvFeeInfoOut.setActionCode(-1);
            //cancelRecvFeeInfoOut.setRecvFee(0);
            //cancelRecvFeeInfoOut.setCancelTag(1);
        }
        return cancelRecvFeeInfoOut;
    }



    /**
     * 数据清理
     * @param cancelRecvFeeInfo 返销交易数据
     * @throws SkyArkException 天舟平台公共异常类
     */
    private void clearCancelFeeInfo(CancelRecvFeeInfo cancelRecvFeeInfo) { }


    /**
     * 抵扣和补收期判断
     * @param cycle 账期
     * @return 是否是特殊交费期
     */
    public boolean isSpecialRecvState(Cycle cycle) {
        if (cycleService.isDrecvPeriod(cycle) || cycleService.isPatchDrecvPeriod(cycle)) {
            return true;
        }
        return false;
    }

    /**
     * 生成dmn数据
     * @param payLog 交费日志
     * @param origChargeId  原返销交费流水
     * @param acctId 账户标识
     * @return dmn对象
     */
    public PayLogDmn genPaylogDmn(PayLog payLog, String origChargeId, String acctId){
        List<PayLogDmn> payLogDmns = payLogDao.getPaylogDmnByChargeIdAndAcctId(origChargeId, acctId);
        PayLogDmn paylogDmn = new PayLogDmn();
        if(CollectionUtils.isEmpty(payLogDmns)){
            if(payLog == null || !payLog.getChargeId().equals(origChargeId)){
                List<PayLog> paylogList = payLogDao.getPaylogByChargeIdAndAcctId(origChargeId, acctId);
                if(!CollectionUtils.isEmpty(paylogList)){
                    payLog = paylogList.get(0);
                }else{
                    payLog = null;
                }
            }
            if(payLog != null){
                paylogDmn.setChargeId(payLog.getChargeId());
                paylogDmn.setAcctId(payLog.getAcctId());
                paylogDmn.setUserId(payLog.getUserId());
                paylogDmn.setSerialNumber(payLog.getSerialNumber());
                paylogDmn.setChannelId(payLog.getChannelId());
                paylogDmn.setPaymentId(payLog.getPaymentId());
                paylogDmn.setPayFeeModeCode(payLog.getPayFeeModeCode());
                paylogDmn.setRecvFee(payLog.getRecvFee());
                paylogDmn.setEparchyCode(payLog.getEparchyCode());
                paylogDmn.setProvinceCode(payLog.getProvinceCode());
            }else{
                List<DiscntDeposit> discntDeposits = discntDepositActsDao.getDiscntDepositsByChargeIdAndAcctId(origChargeId, acctId);
                if(CollectionUtils.isEmpty(discntDeposits)){
                    throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "没有对应的缴费记录,可能缴费记录已经被返销!原缴费chargeId:" + origChargeId);
                }
                paylogDmn.setChargeId(discntDeposits.get(0).getChargeId());
                paylogDmn.setAcctId(discntDeposits.get(0).getAcctId());
                paylogDmn.setUserId(discntDeposits.get(0).getUserId());
                paylogDmn.setPaymentId(discntDeposits.get(0).getDiscntItemId());
                paylogDmn.setRecvFee(discntDeposits.get(0).getMoney());
                paylogDmn.setEparchyCode(discntDeposits.get(0).getEparchyCode());
                paylogDmn.setProvinceCode(discntDeposits.get(0).getProvinceCode());
            }
        }else{
            for(PayLogDmn payLogDmnTmp : payLogDmns){
                if('1'==payLogDmnTmp.getCancelTag()){
                    throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "抵扣期间该记录返销指令已发出,请稍后!原缴费chargeId:" + origChargeId);
                }
            }
            paylogDmn = payLogDmns.get(0);
        }
        String tradeId = sysCommOperFeeService.getActingSequence("TF_B_PAYLOG_DMN","TRADE_ID", paylogDmn.getProvinceCode());
        paylogDmn.setTradeId(tradeId);
        paylogDmn.setBatchId(tradeId);
        paylogDmn.setTradeTypeCode(3);
        paylogDmn.setPriority(0);
        paylogDmn.setPaymentOp(16003);
        paylogDmn.setRecvFee(paylogDmn.getRecvFee() * -1);
        paylogDmn.setCancelTag('1');
        paylogDmn.setDealTag('0');
        paylogDmn.setResultCode(0);
        return paylogDmn;
    }

    /**
     * 账务中心- 根据chargeId和acctId获取关联的交费记录 -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return 交费日志对象列表
     */
    @Override
    public List<PayLog> getRelationPaylogsByChargeIdAndAcctId(String chargeId, String acctId){
        List <ChargeRelation> chargeRelations = chargeRelationDao.getChargeRelationByChargeId(chargeId, acctId);
        if(CollectionUtils.isEmpty(chargeRelations)){
            return Collections.emptyList();
        }
        List<PayLog> payLogs = new ArrayList<>();
        for(ChargeRelation chargeRelationTmp : chargeRelations){
            PayLog payLogsTmp = payLogDao.getPaylogByChargeIdAndAcctId(chargeRelationTmp.getOperateId1(), acctId).get(0);
            if(payLogsTmp != null){
                payLogs.add(payLogsTmp);
            }
        }
        return payLogs;
    }

    /**
     * 首冲判断
     * @param payLogs 交费日志对象列表
     */
    public void translogCheck(List<PayLog> payLogs){
        for(PayLog paylogTmp : payLogs){
            if(transLogDao.getTransLogNumByCharegeIdAndAcctId(paylogTmp.getChargeId(), paylogTmp.getAcctId()) > 0){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "该缴费记录为新产品首笔缴费，不能被返销!chargeId:" + paylogTmp.getChargeId());
             }
        }
    }



    /**
     * 获取返销交易需要入库的数据
     * @param payLog 交费日志对象
     * @param cancelRecvFeeInfo 返销交易数据
     * @param cancelRecvFeeInfoOut 返销输出对象 返销输出对象
     */
    public void cancelRecvfeelog(PayLog payLog, CancelRecvFeeInfo cancelRecvFeeInfo, CancelRecvFeeInfoOut cancelRecvFeeInfoOut){
        String acctId = payLog.getAcctId();
        String origChargeId = payLog.getChargeId();
        String provinceId = payLog.getProvinceCode();
        String sysDate = cancelRecvFeeInfo.getSysDate();
        String cancelChargeId = cancelRecvFeeInfo.getCurrChargeId();
        TradeStaff tradeTradeStaff = cancelRecvFeeInfo.getTradeTradeStaff();
        List<AccessLog> accessLogs = new ArrayList<>();
        List<WriteOffLog> writeOffLogs = new ArrayList<>();
        //生成返销负记录
        if(StringUtil.isEmptyCheckNullStr(cancelChargeId)){
            cancelChargeId = sysCommOperFeeService.getActingSequence("TF_B_PAYLOG","CHARGE_ID",payLog.getProvinceCode());
        }
        PayLog cancelPaylog = payLog.clone();
        cancelPaylog.setChargeId(cancelChargeId);
        cancelPaylog.setRecvFee(-payLog.getRecvFee());
        cancelPaylog.setCancelChargeId(origChargeId);
        cancelPaylog.setCancelStaffId(payLog.getRecvStaffId());
        cancelPaylog.setCancelDepartId(payLog.getRecvDepartId());
        cancelPaylog.setCancelCityCode(payLog.getRecvCityCode());
        cancelPaylog.setCancelEparchyCode(payLog.getRecvEparchyCode());
        cancelPaylog.setCancelTime(payLog.getRecvTime());
        cancelPaylog.setCancelTag('2');
        cancelPaylog.setPaymentOp(16003);//返销
        cancelPaylog.setRemark(cancelRecvFeeInfo.getRemark());
        if(!StringUtil.isEmptyCheckNullStr(cancelRecvFeeInfo.getChannelId())){
            cancelPaylog.setChannelId(cancelRecvFeeInfo.getChannelId());
        }
        if(!StringUtil.isEmptyCheckNullStr(cancelRecvFeeInfo.getCurrOuterTradeId())){
            cancelPaylog.setOuterTradeId(cancelRecvFeeInfo.getCurrOuterTradeId());
        }
        cancelPaylog.setRecvStaffId(tradeTradeStaff.getStaffId());
        cancelPaylog.setRecvDepartId(tradeTradeStaff.getDepartId());
        cancelPaylog.setRecvCityCode(tradeTradeStaff.getCityCode());
        cancelPaylog.setRecvEparchyCode(tradeTradeStaff.getEparchyCode());
        cancelPaylog.setRecvTime(sysDate);
        cancelRecvFeeInfoOut.getCancelPayLogList().add(cancelPaylog);//???????EXTEND_TAG 是否有用

        //生成交费返销关系数据
        cancelRecvFeeInfoOut.getChargeRelationList().add(genChargeRelation(cancelRecvFeeInfo, payLog, tradeTradeStaff,cancelChargeId));

        //生成交费是否抵扣标识
        boolean drecvTag = this.getDrecvTag(origChargeId, acctId);
        cancelRecvFeeInfoOut.getDrecvTagMap().put(origChargeId,drecvTag);

        //无抵扣日志的返销
        if(!drecvTag){
            //返销转兑，只在返销转出记录时修改tf_b_discnt_deposit.left_money字段,暂不考虑返销转兑
            /*if("100022".equals(payLog.getPaymentId()) && payLog.getActionEventId().length()>0){
                updateDiscntDepositByEventIdIdAndAcctId(payLog.getActionEventId(), payLog.getRecvFee(), acctId, provinceId);
            }*/
            //生成销账日志
            writeOffLogs = writeOffLogDao.getWriteOffLogByChargeIdAndAcctId(origChargeId, acctId);
            if(!CollectionUtils.isEmpty(writeOffLogs)){
                cancelRecvFeeInfoOut.getWriteOffLogListMap().put(origChargeId, writeOffLogs);
            }
            //生成存取款日志
            accessLogs = accessLogDao.getAccesslogsByAcctIdAndChargeId(acctId, origChargeId);
            if(!CollectionUtils.isEmpty(accessLogs)){
                for(AccessLog accessLogTmp : accessLogs){
                    //在这里先把accessId获取
                    accessLogTmp.setAccessId(sysCommOperFeeService.getActingSequence("TF_B_ACCESSLOG","ACCESS_ID",cancelPaylog.getProvinceCode()));
                }
                cancelRecvFeeInfoOut.getAccesslogListMap().put(origChargeId, accessLogs);
            }
        }else{
            //生成抵扣销账日志
            writeOffLogs = writeOffLogDao.getWriteOffLogDByChargeIdAndAcctId(origChargeId, acctId);
            if(!CollectionUtils.isEmpty(writeOffLogs)){
                cancelRecvFeeInfoOut.getWriteOffLogListMap().put(origChargeId, writeOffLogs);
            }
            //生成存取款日志
            accessLogs = accessLogDao.getAccesslogDByAcctIdAndChargeId(acctId, origChargeId);
            if(!CollectionUtils.isEmpty(accessLogs)){
                for(AccessLog accessLogTmp : accessLogs){
                    //在这里先把accessId获取
                    accessLogTmp.setAccessId(sysCommOperFeeService.getActingSequence("TF_B_ACCESSLOG","ACCESS_ID",cancelPaylog.getProvinceCode()));
                }
                cancelRecvFeeInfoOut.getAccesslogListMap().put(origChargeId, accessLogs);
            }
        }
        //隔日返销限制
        if(sysDate.substring(0, 10).equals(payLog.getRecvTime().substring(0, 10)) && !CollectionUtils.isEmpty(writeOffLogs) && writeOffLogs.size()>0){
            this.intervalDayLimit(provinceId, payLog.getEparchyCode());
        }
        //数据返回 新生成返销记录的流水
        cancelRecvFeeInfoOut.setCancelChargeId(cancelChargeId);
    }

    /**
     * 获取是否抵扣标识
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return 是否存在抵扣
     */
    private boolean getDrecvTag(String chargeId, String acctId){
        if(0 == writeSnapLogDao.getWriteOffSnapLogByChargeIdAndAcctId(chargeId, acctId)){
            if(0 == writeSnapLogDao.getWriteOffSnapLogDByChargeIdAndAcctId(chargeId, acctId)){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "不存在对应流水的记录!chargeId:" + chargeId + "  acctId:" + acctId);
            }
            return true;
        }
        return false;
    }

    /**
     * 生成交费关系表数据
     * @param cancelRecvFeeInfo 返销交易数据
     * @param payLog 交费日志
     * @param tradeTradeStaff 交易员工
     * @param cancelChargeId 返销流水
     * @return 交费关系对象
     */
    private ChargeRelation genChargeRelation(CancelRecvFeeInfo cancelRecvFeeInfo, PayLog payLog, TradeStaff tradeTradeStaff, String cancelChargeId){
        ChargeRelation chargeRelation = new ChargeRelation();
        //???????REL_CHARGE_ID 哪里来的
        chargeRelation.setId(payLog.getChargeId());
        chargeRelation.setOperateId1(cancelChargeId);
        chargeRelation.setOperateId2(payLog.getChargeId());
        chargeRelation.setOperateType(StringUtil.isEmptyCheckNullStr(cancelRecvFeeInfo.getOperateType()) ? "1" : cancelRecvFeeInfo.getOperateType());
        chargeRelation.setOperateStaffId(tradeTradeStaff.getStaffId());
        chargeRelation.setOperateCityCode(tradeTradeStaff.getCityCode());
        chargeRelation.setOperateDepartId(tradeTradeStaff.getDepartId());
        chargeRelation.setOperateEparchyCode(tradeTradeStaff.getEparchyCode());
        chargeRelation.setOperateTime(cancelRecvFeeInfo.getSysDate());
        chargeRelation.setProvinceCode(payLog.getProvinceCode());
        chargeRelation.setEparchyCode(payLog.getEparchyCode());
        chargeRelation.setAcctId(payLog.getAcctId());
        return chargeRelation;
    }


    /**
     * 隔天返销限制
     * @param provinceId 省份编码
     * @param eparchyCode 地市编码
     */
    private void intervalDayLimit(String provinceId, String eparchyCode){
        CommPara commPara = commParaFeeService.getCommpara(ActingPayCommparaDef.ASM_NOALLOW_WRITEOFF_CANCEL,  provinceId,
                eparchyCode);
        if(commPara == null){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "ASM_NOALLOW_WRITEOFF_CANCEL参数没有配置!");
        }else if("1".equals(commPara.getParaCode1())){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "隔天并且发生了销帐系统不允许返销!");
        }
    }

    /**
     * 隔笔返销限制
     * @param provinceId 省份编码
     * @param eparchyCode 地市编码
     */
    public void intervalTradLimit(String provinceId, String eparchyCode){
        CommPara commPara = commParaFeeService.getCommpara(ActingPayCommparaDef.ASM_NOALLOW_INTERVAL_CANCELFEE, provinceId,
                eparchyCode);
        if(commPara == null){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "ASM_NOALLOW_INTERVAL_CANCELFEE参数没有配置!");
        }else if("1".equals(commPara.getParaCode1())){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "不允许隔笔返销!");
        }
    }


    @Override
    public boolean cancelRecvFeeLogIndbCore(CancelRecvFeeInfoOut cancelRecvFeeInfoOut){

        logger.info("logindb begin");
        //交费关系入表
        this.insertChargeRelations(cancelRecvFeeInfoOut.getChargeRelationList());

        TradeStaff tradeTradeStaff = cancelRecvFeeInfoOut.getTradeTradeStaff();
        for(PayLog cancelPaylog : cancelRecvFeeInfoOut.getCancelPayLogList()){
            String origChargeId = cancelPaylog.getCancelChargeId();
            String cancelChargeId = cancelPaylog.getChargeId();
            String acctId = cancelPaylog.getAcctId();
            String sysDate = cancelPaylog.getRecvTime();

            //更新收费其他信息表TF_B_payother_log
            this.cancelPayOtherlogByChargeIdAndAcctId(origChargeId, acctId);

            //更新账单
            this.cancelBillsByWriteOffLogs(cancelRecvFeeInfoOut.getWriteOffLogListMap().get(origChargeId));

            /**if 无抵扣  else 有抵扣**/
            if(!cancelRecvFeeInfoOut.getDrecvTagMap().get(origChargeId)){
                //返销原交费记录
                this.cancelOrigPaylogByChargeIdAndAcctId(tradeTradeStaff, origChargeId, sysDate, acctId);
                //返销CLPAYLG
                this.cancelClPaylogByChargeIdAndAcctId(tradeTradeStaff, origChargeId, cancelPaylog.getOuterTradeId(), cancelChargeId, acctId);
                //更新快照日志
                this.cancelWriteoffSnaplogByChargeIdAndAcctId(origChargeId, acctId);
                //返销存取款日志
                this.cancelAccesslogByChargeIdAndAcctId(origChargeId, acctId);
                //返销销账日志
                if(!CollectionUtils.isEmpty(cancelRecvFeeInfoOut.getWriteOffLogListMap().get(origChargeId))){
                    this.cancelWriteofflogByChargeIdAndAcctId(origChargeId, acctId);
                }
                //插入返销存取款负记录
                List<AccessLog> accessLogList = cancelRecvFeeInfoOut.getAccesslogListMap().get(origChargeId);
                accessLogList.sort(new AccssslogCancelSortRule());
                if(!CollectionUtils.isEmpty(accessLogList)){
                    List<AccessLog> accessLogsTmp = new ArrayList<>();
                    accessLogsTmp.addAll(accessLogList);
                    this.insertAccesslog(accessLogsTmp, cancelPaylog);
                }
                //插入交费负记录
                this.insertPaylog(cancelPaylog);

            }else{
                //返销原交费抵扣记录
                this.cancelOrigPaylogDByChargeIdAndAcctId(tradeTradeStaff, origChargeId, sysDate, acctId);
                //返销CLPAYLG
                this.cancelClPaylogByChargeIdAndAcctId(tradeTradeStaff, origChargeId, cancelPaylog.getOuterTradeId(), cancelChargeId, acctId);
                //更新快照日志
                this.cancelWriteoffSnaplogDByChargeIdAndAcctId(origChargeId, acctId);
                //返销抵扣存取款日志
                this.cancelAccesslogDByChargeIdAndAcctId(origChargeId, acctId);
                //返销抵扣销账日志
                if(!CollectionUtils.isEmpty(cancelRecvFeeInfoOut.getWriteOffLogListMap().get(origChargeId))){
                    this.cancelWriteofflogDByChargeIdAndAcctId(origChargeId, acctId);
                }
                //插入返销抵扣存取款负记录
                List<AccessLog> accessLogList = cancelRecvFeeInfoOut.getAccesslogListMap().get(origChargeId);
                accessLogList.sort(new AccssslogCancelSortRule());
                if(CollectionUtils.isEmpty(accessLogList)){
                    List<AccessLog> accessLogsTmp = new ArrayList<>();
                    accessLogsTmp.addAll(accessLogList);
                    this.insertAccesslogD(accessLogsTmp, cancelPaylog);
                }
                //插入交费抵扣负记录
                this.insertPaylogD(cancelPaylog);
            } //end else
        }
        logger.info("logindb end");
        return true;
    }


    @Override
    public CancelRecvFeeMqMessage genCancelFeeMqMessageCommon(CancelRecvFeeInfoOut cancelRecvFeeInfoOut) {
        CancelRecvFeeMqMessage cancelRecvFeeMqMessage = new CancelRecvFeeMqMessage();
        cancelRecvFeeMqMessage.setResultTag(cancelRecvFeeInfoOut.getResultTag());
        cancelRecvFeeMqMessage.setAccesslogListMap(cancelRecvFeeInfoOut.getAccesslogListMap());
        cancelRecvFeeMqMessage.setCancelPayLogList(cancelRecvFeeInfoOut.getCancelPayLogList());
        return cancelRecvFeeMqMessage;
    }


    @Override
    public boolean insertPaylogDmnList(List<PayLogDmn> payLogDmnList){
        for(PayLogDmn payLogDmn : payLogDmnList){
            if(payLogDao.insertPaylogDmn(payLogDmn) == 0){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "月结期间返销插入指令表失败!chargeId:" + payLogDmn.getChargeId());
            }
        }
        return true;
    }

    @Override
    public void insertChargeRelations(List<ChargeRelation> chargeRelations){
        for(ChargeRelation chargeRelation : chargeRelations){
            if(chargeRelationDao.insertChargeRelation(chargeRelation) == 0){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"插入交费关系表失败!");
            }
        }
    }


    @Override
    public void cancelOrigPaylogByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelTime, String acctid){
        if(payLogDao.updatePayLogByChargeIdAndAcctId(tradeTradeStaff, chargeId, cancelTime, acctid) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"账务中心没有对应的缴费记录!chargeId:" + chargeId);
        }
    }


    @Override
    public void cancelOrigPaylogDByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelTime, String acctid){
        if(payLogDao.updatePayLogDByChargeIdAndAcctId(tradeTradeStaff, chargeId, cancelTime, acctid) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"账务中心没有对应的抵扣记录!chargeId:" + chargeId);
        }
    }


    @Override
    public void cancelClPaylogByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelOuterTradeId, String cancelChargeId, String acctId){
        List<CLPayLog> clPayLogs = payLogDao.getClPaylogByChargeIdAndAcctId(chargeId, acctId);
        if(CollectionUtils.isEmpty(clPayLogs)){
            return;
        }
        String clPaylogId = sysCommOperFeeService.getSequence(tradeTradeStaff.getEparchyCode(), "SEQ_CHARGE_ID", tradeTradeStaff.getProvinceCode());
        CLPayLog clPayLogTmp = clPayLogs.get(0);
        clPayLogTmp.setClPaylogId(clPaylogId);
        clPayLogTmp.setCancelChargeId(cancelChargeId);
        clPayLogTmp.setRecvFee(-clPayLogTmp.getRecvFee());
        clPayLogTmp.setCancelTag('2');
        clPayLogTmp.setDownTag('1');
        clPayLogTmp.setOuterTradeId(cancelOuterTradeId);
        clPayLogTmp.setCancelEparchyCode(clPayLogTmp.getRecvEparchyCode());
        clPayLogTmp.setCancelCityCode(clPayLogTmp.getRecvCityCode());
        clPayLogTmp.setCancelDepartId(clPayLogTmp.getRecvDepartId());
        clPayLogTmp.setCancelStaffId(clPayLogTmp.getRecvStaffId());
        clPayLogTmp.setCancelTime(clPayLogTmp.getRecvTime());
        clPayLogTmp.setCancelChargeId(clPayLogTmp.getChargeId());
        clPayLogTmp.setClCancelPayId(clPayLogTmp.getClPaylogId());
        clPayLogTmp.setRecvEparchyCode(tradeTradeStaff.getEparchyCode());
        clPayLogTmp.setRecvCityCode(tradeTradeStaff.getCityCode());
        clPayLogTmp.setRecvDepartId(tradeTradeStaff.getDepartId());
        clPayLogTmp.setRecvStaffId(tradeTradeStaff.getStaffId());
        //插入返销负记录
        payLogDao.insertClPaylog(clPayLogTmp);
        //更新原有记录
        if(payLogDao.updateOrigClPaylog(clPayLogTmp, "1") > 0){
            //更新返销记录
            payLogDao.updateOrigClPaylogByChargeIdAndAcctId(cancelChargeId, acctId);
        } else{
            if(payLogDao.updateOrigClPaylog(clPayLogTmp, "0")  == 0){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"未找到对应CLPAYLOG记录!chargeId:" + cancelChargeId);
            }
        }
    }


    @Override
    public void cancelWriteoffSnaplogByChargeIdAndAcctId(String chargeId, String acctId){
        if(writeSnapLogDao.updateWriteoffSnapCancelTag(chargeId, acctId) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"更新快照记录失败!chargeId:" + chargeId);
        }
    }


    @Override
    public void cancelWriteoffSnaplogDByChargeIdAndAcctId(String chargeId, String acctId){
        if(writeSnapLogDao.updateWriteoffDSanpCancelTag(chargeId, acctId) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"更新抵扣快照记录失败!chargeId:" + chargeId);
        }
    }


    @Override
    public void cancelWriteofflogByChargeIdAndAcctId(String chargeId, String acctId){
        if(writeOffLogDao.updateWriteoffCancelTag(chargeId, acctId) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"更新销帐记录失败!chargeId:" + chargeId);
        }
    }


    @Override
    public void cancelWriteofflogDByChargeIdAndAcctId(String chargeId, String acctId){
        if(writeOffLogDao.updateWriteoffDCancelTag(chargeId, acctId) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"更新抵扣销帐记录失败!chargeId:" + chargeId);
        }
    }


    @Override
    public void cancelAccesslogByChargeIdAndAcctId(String chargeId, String acctId){
        if(accessLogDao.updateAccesslogByChargeIdAndAcctId(chargeId, acctId) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"账务中心更新存取款记录记录失败!chargeId:" + chargeId);
        }
    }


    @Override
    public void cancelAccesslogDByChargeIdAndAcctId(String chargeId, String acctId){
        if(accessLogDao.updateAccesslogDByChargeIdAndAcctId(chargeId, acctId) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"账务中心更新抵扣存取款记录记录失败!chargeId:" + chargeId);
        }
    }


    @Override
    public void insertAccesslog(List<AccessLog> accessLogs, PayLog cancelPayLog){
        for(AccessLog accessLogTmp : accessLogs){
            accessLogTmp.setCancelTag('2');
            accessLogTmp.setMoney(-accessLogTmp.getMoney());
            accessLogTmp.setOperateTime(cancelPayLog.getRecvTime());
            accessLogTmp.setChargeId(cancelPayLog.getChargeId());
            long tmpBalance =accessLogTmp.getNewBalance();
            accessLogTmp.setNewBalance(accessLogTmp.getOldBalance());
            accessLogTmp.setOldBalance(tmpBalance);
            if(accessLogDao.insertAccesslog(accessLogTmp) == 0){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"账务中心插入存取款负记录失败!");
            }
        }
    }


    @Override
    public void insertAccesslogD(List<AccessLog> accessLogs, PayLog cancelPayLog){
        for(AccessLog accessLogTmp : accessLogs){
            accessLogTmp.setCancelTag('2');
            accessLogTmp.setMoney(-accessLogTmp.getMoney());
            accessLogTmp.setOperateTime(cancelPayLog.getRecvTime());
            accessLogTmp.setChargeId(cancelPayLog.getChargeId());
            long tmpBalance =accessLogTmp.getNewBalance();
            accessLogTmp.setNewBalance(accessLogTmp.getOldBalance());
            accessLogTmp.setOldBalance(tmpBalance);
            accessLogTmp.setAccessId(sysCommOperFeeService.getSequence(cancelPayLog.getEparchyCode(),"SEQ_ACCESS_ID",cancelPayLog.getProvinceCode()));
            if(accessLogDao.insertAccesslogD(accessLogTmp) == 0){
                throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"账务中心插入抵扣存取款负记录失败!");
            }
        }
    }


    @Override
    public void cancelPayOtherlogByChargeIdAndAcctId(String chargeId, String acctId){
        payLogDao.updatePayOtherlogByChargeIdAndAcctId(chargeId, acctId);
    }

    @Override
    public void cancelBillsByWriteOffLogs(List<WriteOffLog> writeOffLogs){
        boolean hasBadBill = false;//有坏帐
        if(!CollectionUtils.isEmpty(writeOffLogs)){
            String eparchyCode = "";
            String provinceCode = "";
            String netTypeCode = "";
            Set<String> setAcctId = new HashSet<>();
            Set<String> setBillId = new HashSet<>();
            char depositTypeCode = '\0';
            Map<Integer, DepositPriorRule> depositPriorRuleMap = new HashMap<>();
            for(int i=writeOffLogs.size()-1; i>=0; --i){
                WriteOffLog writeOffLogTmp = writeOffLogs.get(i);
                long writeoffFee1 = 0;
                long writeoffFee2 = 0;
                if(!eparchyCode.equals(writeOffLogTmp.getEparchyCode()) || !provinceCode.equals(writeOffLogTmp.getProvinceCode())
                        || !netTypeCode.equals(writeOffLogTmp.getNetTypeCode())){
                    eparchyCode = writeOffLogTmp.getEparchyCode();
                    provinceCode = writeOffLogTmp.getProvinceCode();
                    netTypeCode = writeOffLogTmp.getNetTypeCode();
                    depositPriorRuleMap = getDepositPriorRuleMap(eparchyCode, provinceCode, netTypeCode);
                }
                depositTypeCode = getDepositTypeCode(writeOffLogTmp.getDepositCode(), depositPriorRuleMap);
                if('2' == depositTypeCode || '3' == depositTypeCode){
                    writeoffFee1 = writeOffLogTmp.getWriteoffFee();
                }else if('1' == depositTypeCode){
                    writeoffFee2 = writeOffLogTmp.getWriteoffFee();
                }
                //更新账单
                Bill bill = new Bill();
                bill.setAcctId(writeOffLogTmp.getAcctId());
                bill.setUserId(writeOffLogTmp.getUserId());
                bill.setBillId(writeOffLogTmp.getBillId());
                bill.setIntegrateItemCode(writeOffLogTmp.getIntegrateItemCode());
                bill.setCanpayTag(writeOffLogTmp.getCanPaytag());
                bill.setOldPayTag(writeOffLogTmp.getOldPaytag());
                //bill.setNewPayTag(String.valueOf(writeOffLogTmp.getNewPaytag()));
                bill.setBalance(writeOffLogTmp.getOldBalance()-writeOffLogTmp.getNewBalance());
                bill.setLateFee(writeOffLogTmp.getLateFee());
                bill.setLateBalance(writeOffLogTmp.getLateBalance());
                bill.setWriteoffFee1(writeoffFee1);
                bill.setWriteoffFee2(writeoffFee2);
                bill.setWriteoffFee3(0);
                bill.setLateCalDate(writeOffLogTmp.getLatecalDate());
                int updateCount = billDao.updBillRevert(bill);

                if(0 == updateCount){
                    if(billDao.copyHisBillToBill(writeOffLogTmp.getAcctId(), writeOffLogTmp.getUserId(), writeOffLogTmp.getBillId()) > 0){
                        //拷贝历史账单
                        billDao.deleteHisBill(writeOffLogTmp.getAcctId(), writeOffLogTmp.getUserId(), writeOffLogTmp.getBillId());
                        updateCount = billDao.updBillRevert(bill);
                    }
                    if(0 == updateCount && ('7'== writeOffLogTmp.getCanPaytag() || '8' == writeOffLogTmp.getCancelTag())){
                        //更新坏帐
                        updateCount = billDao.updBillBadRevert(bill);
                        hasBadBill = true;
                    }
                }
                if(updateCount != 1){
                    throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "帐单发生变化,更新失败!(acctId="+writeOffLogTmp.getAcctId()+" ,billId="+ writeOffLogTmp.getBillId() +
                            " integrateItemCode=" + writeOffLogTmp.getIntegrateItemCode());
                }
                setAcctId.add(writeOffLogTmp.getAcctId());
                setBillId.add(writeOffLogTmp.getBillId());
            } //end for
            //需要更新BILL_PAY_TAG;
            for(String acctIdTmp : setAcctId){
                for(String billIdTmp : setBillId){
                    billDao.updBillRevertBillPayTag(acctIdTmp, billIdTmp);
                    if(hasBadBill){
                        billDao.updBadBillRevertBillPayTag(acctIdTmp, billIdTmp);
                        ////还原坏帐用户
                        billDao.updateBadbillUserInfo(acctIdTmp, "1");

                    }
                }
            }
        }//更新账单end
    }


    @Override
    public void insertPaylog(PayLog payLog){
        if(payLogDao.insertPayLog(payLog) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"账务中心插入缴费负记录失败!");
        }
    }


    @Override
    public void insertPaylogD(PayLog payLog){
        if(payLogDao.insertPaylogD(payLog) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"账务中心插入抵扣负记录失败!");
        }
    }

    @Override
    public boolean checkbusinessService(CancelRecvFeeMqMessage cancelRecvFeeMqMessage){
        for(PayLog cancelPaylog : cancelRecvFeeMqMessage.getCancelPayLogList()){
            if(!payLogDao.ifExistPaylogByChargeId(cancelPaylog.getChargeId(),cancelPaylog.getAcctId())){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取帐本科目优先地市规则参数
     * @param eparchyCode 地市编码
     * @param provinceCode  省份编码
     * @param netTypeCode 网别
     * @return  帐本科目优先地市规则
     */
    public Map<Integer, DepositPriorRule> getDepositPriorRuleMap(String eparchyCode, String provinceCode, String netTypeCode) {
        //帐本科目优先规则
        int ruleId = getRuleEparchy(WriteOffRuleStaticInfo.getAllVRuleeparchy(),
                WriteOffRuleInfo.DEPOSIT_PRIOR_RULE_TYPE, eparchyCode, provinceCode, netTypeCode);
        if (ruleId > 0) {
            if (WriteOffRuleStaticInfo.getAllMMDepositPriorRule().containsKey(ruleId)) {
                return WriteOffRuleStaticInfo.getAllMMDepositPriorRule().get(ruleId);
            } else {
                throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode + ",ruleId=" + ruleId);
            }
        } else {
            throw new SkyArkException("参数问题，请联系管理员!没有设置帐本科目优先地市规则参数provinceCode=" + provinceCode + ",eparchyCode=" + eparchyCode + ",netTypeCode=" + netTypeCode);
        }
    }


    /**
     * 根据地市，省份和网别获取销账规则类型对应的规则实例标识
     *
     * @param ruleList     地市销账规则列表
     * @param ruleType     销账规则类型
     * @param eparchyCode  账户归属地市
     * @param provinceCode  省份编码 账户归属省份
     * @param netTypeCode  账户网别
     * @return 销账规则实例标识
     */
    private int getRuleEparchy(List<RuleEparchy> ruleList, char ruleType, String eparchyCode, String provinceCode, String netTypeCode) {
        int ruleId = -1;
        RuleEparchy rulePtr = getEparchyRuleInfo(ruleList, ruleType, eparchyCode, provinceCode, netTypeCode);
        if (rulePtr == null) {
            //取默认网别规则
            rulePtr = getEparchyRuleInfo(ruleList, ruleType, eparchyCode, provinceCode,WriteOffRuleInfo.DEFAULT_NET_TYPE_CODE);
            //取默认地市规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(ruleList, ruleType,WriteOffRuleInfo.DEFAULT_EPARCHY_CODE, provinceCode, netTypeCode);
            }
            //取默认地市默认网别规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(ruleList, ruleType,WriteOffRuleInfo.DEFAULT_EPARCHY_CODE, provinceCode,WriteOffRuleInfo.DEFAULT_NET_TYPE_CODE);
            }
            //取默认省别默认地市规则
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(ruleList, ruleType,WriteOffRuleInfo.DEFAULT_EPARCHY_CODE,WriteOffRuleInfo.DEFAULT_PROVINCE_CODE, netTypeCode);
            }
            //全默认优先级最低
            if (rulePtr == null) {
                rulePtr = getEparchyRuleInfo(ruleList, ruleType,WriteOffRuleInfo.DEFAULT_EPARCHY_CODE,WriteOffRuleInfo.DEFAULT_PROVINCE_CODE,WriteOffRuleInfo.DEFAULT_NET_TYPE_CODE);
            }
        }

        if (rulePtr != null) {
            ruleId = rulePtr.getRuleId();
        }
        return ruleId;
    }


    /**
     * 根据地市，省份和网别获取销账规则类型对应的规则对象
     *
     * @param ruleList     地市销账规则列表
     * @param ruleType     销账规则类型
     * @param eparchyCode  账户归属地市
     * @param provinceCode  省份编码 账户归属省份
     * @param netTypeCode  账户网别
     * @return 销账规则对象
     */
    private RuleEparchy getEparchyRuleInfo(List<RuleEparchy> ruleList, char ruleType, String eparchyCode, String provinceCode, String netTypeCode) {
        for (RuleEparchy ruleEparchy : ruleList) {
            if (ruleType == ruleEparchy.getRuleType()
                    && eparchyCode.equals(ruleEparchy.getEparchyCode())
                    && provinceCode.equals(ruleEparchy.getProvinceCode())
                    && netTypeCode.equals(ruleEparchy.getNetTypeCode())) {
                return ruleEparchy;
            }
        }
        return null;
    }

    /**
     * 获取账本的deposit_type_code -- acting.pay.writeoff
     * @param depositCode 账本标识
     * @param depositPriorRuleMap 账本优先级存取Map
     * @return depositTypeCode  depositTypeCode
     */
    @Override
    public char getDepositTypeCode(int depositCode,  Map<Integer, DepositPriorRule> depositPriorRuleMap){
        if(CollectionUtils.isEmpty(depositPriorRuleMap)){
            return '*';
        }
        if(depositPriorRuleMap.containsKey(depositCode)){
            return depositPriorRuleMap.get(depositCode).getDepositTypeCode();
        }
        return '*';
    }

    /**
     * 判断是否为隔笔返销 -- acting.pay.writeoff
     * @param acctId 账户标识
     * @param time
     * @return
     */
    public int isLastAccesslog(String acctId, String time){
        if(accessLogDao.isLastAccesslog(acctId, time) > 0 ||
                accessLogDao.isLastAccesslogD(acctId, time) > 0){
            return 2;
        }
        return 0;
    }

    /**
     * 更新账本 -- acting.pay.writeoff
     * @param accessLogs
     */
/*    @Override
    public void cancelUpdateAccountDeposit(List<AccessLog> accessLogs){
        if(CollectionUtils.isEmpty(accessLogs) && accessLogs.size()>0){
            //更新存折(需要逆序更新)
            for (int i = accessLogs.size() - 1; i >= 0; --i) {
                AccessLog accessLogTmp = accessLogs.get(i);
                long money = accessLogTmp.getMoney();
                //返销存
                if ('0' != accessLogTmp.getAccessTag())//返销取(取款是负数)
                {
                    money = -money;;
                }
                //返销账本
                if(depositActsDao.updAcctdepositRevert(accessLogTmp.getAcctId(), accessLogTmp.getAcctBalanceId(), money, money,
                        0, accessLogTmp.getInvoiceFee(), String.valueOf(accessLogTmp.getAccessTag())) == 0){
                    throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "返销存折失败!(acctId="+accessLogTmp.getAcctId()+" ,acctBalanceId="+ accessLogTmp.getAcctBalanceId());
                }
                AccountDeposit  accountDepositTmp = depositActsDao.getDepositCanPrintFeeByAcctBalanceIdAndAcctId(accessLogTmp.getAcctBalanceId(),
                        accessLogTmp.getAcctId(), accessLogTmp.getProvinceCode()).get(0);
                if('0' == accessLogTmp.getAccessTag() && accountDepositTmp.getMoney() < 0){
                    throw new SkyArkException(SysTypes.BUSI_ERROR_CODE, "返销出现负存折,系统限制此返销!(acctId="+accessLogTmp.getAcctId()+" ,acctBalanceId="+ accessLogTmp.getAcctBalanceId());
                }
            }//end for
        }

    }*/


/*    @Override
    public void updateDiscntDepositByEventIdIdAndAcctId(String actionEventId, long recvFee, String acctId, String provinceId){
        DiscntDeposit discntDeposit = discntDepositActsDao.getDiscntDepositsByEventIdIdAndAcctId(actionEventId, acctId , provinceId).get(0);
        if(discntDeposit==null || StringUtil.isEmptyCheckNullStr(discntDeposit.getActionEventId())){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"获取活动表失败!actionEventId:" + actionEventId);
        }
        discntDeposit.setLeftMoney(discntDeposit.getLeftMoney()-recvFee);
        if(discntDepositActsDao.updateDiscntDepositsByEventIdIdAndAcctId(discntDeposit, acctId, provinceId) == 0){
            throw new SkyArkException(SysTypes.BUSI_ERROR_CODE,"获取活动表失败!actionEventId:" + discntDeposit.getActionEventId());
        }
    }*/

}
