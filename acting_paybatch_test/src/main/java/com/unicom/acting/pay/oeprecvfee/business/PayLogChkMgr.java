package com.unicom.acting.pay.oeprecvfee.business;

import com.unicom.acting.batch.common.domain.BaseOrderMgr;
import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.oeprecvfee.dao.PayLogChkDao;
import com.unicom.skyark.component.jdbc.DbTypes;
import com.unicom.skyark.component.jdbc.transaction.annotation.SkyArkTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("baseOrderMgr")
public class PayLogChkMgr implements BaseOrderMgr<BatchRecvFeeIn> {
    private static final Logger logger = LoggerFactory.getLogger(PayLogChkMgr.class);

    @Autowired
    private PayLogChkDao payLogChkDao;
    @SkyArkTransactional(DbTypes.ACT_RDS)
    //@Transactional(value = DbTypes.ACT_RDS)
    public int setData(BatchRecvFeeIn batchRecvFeeIn)
    {
        if (9 == batchRecvFeeIn.getResultCode())
        {
            return 0;
        }

        batchRecvFeeIn.setActTag("5");
        String resultInfo = batchRecvFeeIn.getResultInfo();
        batchRecvFeeIn.setResultInfo(resultInfo.substring(0,resultInfo.length()>=300?299 : resultInfo.length()));

        if(7== batchRecvFeeIn.getResultCode())
        {
            int updateTag = 0;
            updateTag = payLogChkDao.updatePayLogChkByType(batchRecvFeeIn,7045, batchRecvFeeIn.getTradeTypeCode());
            updateTag = payLogChkDao.updatePayLogChkByType(batchRecvFeeIn,7046,7043);
            if(updateTag != 1)
                throw new ActBException("-100", "TF_B_PAYLOG_CHK没有更新到数据!!tradeId ="+ batchRecvFeeIn.getOriginTradeId());

            return 0;
        }
        int iRet = 0;
        logger.info("TradeTypeCode = {},iRet1 = {}", batchRecvFeeIn.getTradeTypeCode(),iRet);
        logger.info("batchRecvFeeIn.getOldActTag()) = {}", batchRecvFeeIn.getOriginActTag());
        logger.info("batchRecvFeeIn.getOldDealtag()) = {}", batchRecvFeeIn.getOriginDealtag());
        logger.info("batchRecvFeeIn.getActTag()) = {}", batchRecvFeeIn.getActTag());
        logger.info("batchRecvFeeIn.getDealtag()) = {}", batchRecvFeeIn.getDealtag());
        iRet += payLogChkDao.updatePayLogChk(batchRecvFeeIn);
        logger.info("TradeTypeCode = {},iRet2 = {}", batchRecvFeeIn.getTradeTypeCode(),iRet);
        if(7040 == batchRecvFeeIn.getTradeTypeCode())//增加了营业打发票
        {
            batchRecvFeeIn.setTradeTypeCode(7125);
            iRet += payLogChkDao.updatePayLogChk(batchRecvFeeIn);
            logger.info("iRet2 = {}",iRet);
        }
        if (iRet <= 0)
        {
            throw new ActBException("-101", "没有更新到数据!!tradeId = "+ batchRecvFeeIn.getOriginTradeId());
        }
        return 0;
    }

    @Override
    public void execute(BatchRecvFeeIn bRecvFeIn) {
        this.setData(bRecvFeIn);
    }
}
