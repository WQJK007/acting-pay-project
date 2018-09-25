package com.unicom.acting.pay.bwriteoff.processor;

import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.common.exception.ActBSysTypes;
import com.unicom.acting.fee.calc.service.CalculateService;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.pay.bwriteoff.business.BPayFeeSmsMgr;
import com.unicom.acting.pay.bwriteoff.business.BWriteOffMgr;
import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.bwriteoff.domain.RecvFeeHolder;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.acting.pay.writeoff.service.CreditService;
import com.unicom.acting.pay.writeoff.service.RecvFeeCommService;
import com.unicom.skyark.component.exception.SkyArkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("writeOffProcessor")
@StepScope
public class WriteOffProcessor implements ItemProcessor<BTradeCommInfo<TradeCommInfo>, BTradeCommInfo<TradeCommResultInfo>> {
    private static final Logger logger = LoggerFactory.getLogger(WriteOffProcessor.class);

    @Autowired
    CalculateService calculateService;
    @Autowired
    private RecvFeeCommService recvFeeCommService;

    @Autowired
    private RecvFeeHolder<BatchRecvFeeIn> recvFeeHolder;
    @Autowired
    private BPayFeeSmsMgr bPayFeeSmsMgr;
    @Autowired
    private CreditService creditService;
    @Autowired
    private BWriteOffMgr bWriteOffMgr;

    public BTradeCommInfo<TradeCommResultInfo> process(BTradeCommInfo<TradeCommInfo> bTradeCommInfo) throws Exception {
        try {
            //缴费前销账计算
            TradeCommInfo tradeCommInfo = bTradeCommInfo.getTradeCommonInfo();
            calculateService.calc(tradeCommInfo);
            //设置缴费金额
            logger.info("--------process:count = {}",bTradeCommInfo.getOrderNumber());
            BatchRecvFeeIn recvFeeIn =recvFeeHolder.getListInstance().get(bTradeCommInfo.getOrderNumber());
            recvFeeCommService.setRecvfee(recvFeeIn, tradeCommInfo);
            logger.info("after setRecvfee");
            //缴费后销账计算
            calculateService.recvCalc(tradeCommInfo);
            TradeCommResultInfo tradeCommResultInfo = new TradeCommResultInfo();

            //生成缴费入库信息
            bWriteOffMgr.genRecvDBInfo(recvFeeIn, tradeCommInfo, tradeCommResultInfo);
            //recvFeeCommService.genRecvDBInfo(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
            //生成短信信息
            //smsService.genSmsInfo(recvFeeCommInfoIn, tradeCommInfo, tradeCommResultInfo);
            bPayFeeSmsMgr.createSmsInfo(recvFeeIn,tradeCommInfo,tradeCommResultInfo);
            //生成信控工单
            creditService.genCreditInfo(recvFeeIn, tradeCommInfo, tradeCommResultInfo);
            //缴费结果整理返回
            //TradeCommInfoOut tradeCommInfoOut=bWriteOffMgr.genTradeCommInfoOut(recvFeeInfoIn, tradeCommInfo, tradeCommResultInfo);
            BTradeCommInfo bTradeCommInfoRet =new BTradeCommInfo<TradeCommResultInfo>();
            bTradeCommInfoRet.setOrderNumber(bTradeCommInfo.getOrderNumber());
            bTradeCommInfoRet.setTradeCommonInfo(tradeCommResultInfo);
            recvFeeIn.setResultCode(ActBSysTypes.RESULT_DEALING);
            recvFeeIn.setResultInfo("processor success!");
            recvFeeIn.setRemark("交费处理成功");

            return bTradeCommInfoRet;
        }
        catch(ActBException e)
        {
            recvFeeHolder.setOrderNumber(bTradeCommInfo.getOrderNumber());
            bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.getStackTrace().toString(),"交费处理失败");
            logger.info("错误1：{},{}",e.getErrorCode(),e.getErrorMsg());
            throw new ActBException(e.getErrorCode(), "ActBatchException错误："+e.getMessage());
        }
        catch (SkyArkException e)
        {
            recvFeeHolder.setOrderNumber(bTradeCommInfo.getOrderNumber());
            bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.getStackTrace().toString(),"交费处理失败");
            logger.info("错误2：{},{}",e.getErrorCode(),e.getErrorMsg());
            throw new ActBException(e.getErrorCode(),"SkyArkException错误："+e.getErrorMsg());
        }
        catch(Exception e)
        {
            recvFeeHolder.setOrderNumber(bTradeCommInfo.getOrderNumber());
            bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.getStackTrace().toString(),"交费处理失败");
            logger.info("错误3：{}",e.getStackTrace());
            throw new ActBException("Exception错误："+e.getStackTrace());
        }
    }
}
