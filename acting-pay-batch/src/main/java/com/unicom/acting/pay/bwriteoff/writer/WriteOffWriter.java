package com.unicom.acting.pay.bwriteoff.writer;

import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.batch.common.domain.BaseOrderMgr;
import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.common.exception.ActBSysTypes;
import com.unicom.acting.pay.bwriteoff.business.BPayFeeSmsMgr;
import com.unicom.acting.pay.bwriteoff.business.BWriteOffMgr;
import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.bwriteoff.domain.RecvFeeHolder;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.acting.pay.writeoff.service.CreditService;
import com.unicom.skyark.component.exception.SkyArkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("writeOffWriter")
@StepScope
public class WriteOffWriter implements ItemWriter<BTradeCommInfo<TradeCommResultInfo>> {
    private static final Logger logger = LoggerFactory.getLogger(WriteOffWriter.class);

    @Autowired
    private BPayFeeSmsMgr bPayFeeSmsMgr;
    @Autowired
    private CreditService creditService;
    @Autowired
    private BWriteOffMgr bWriteOffMgr;
    @Autowired
    private RecvFeeHolder<BatchRecvFeeIn> recvFeeHolder;
    @Autowired
    private BaseOrderMgr<BatchRecvFeeIn> baseOrderMgr;
    @Override
    //@Transactional(value = DbTypes.ACTING_DRDS)
    public void write(List<? extends BTradeCommInfo<TradeCommResultInfo>> tradeCommInfos) throws Exception {
        for (BTradeCommInfo<TradeCommResultInfo> bTradeCommInfo : tradeCommInfos) {
            try {

                TradeCommResultInfo resultInfo=bTradeCommInfo.getTradeCommonInfo();
                bWriteOffMgr.recvFeeLogIndb(resultInfo);
                BatchRecvFeeIn brecvfeeIn=recvFeeHolder.getListInstance().get(bTradeCommInfo.getOrderNumber());
                brecvfeeIn.setResultCode(ActBSysTypes.RESULT_SUCCESS);
                brecvfeeIn.setDealtag(ActBSysTypes.DEALED_TAG_SUCCESS);
                brecvfeeIn.setResultInfo("writer success!");
                brecvfeeIn.setRemark("交费成功");
                baseOrderMgr.execute(brecvfeeIn);
            }
            catch(ActBException e)
            {
                recvFeeHolder.setOrderNumber(bTradeCommInfo.getOrderNumber());
                bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.getStackTrace().toString(),"交费入库失败");
                logger.info("错误1：{},{}",e.getErrorCode(),e.getErrorMsg());
                throw new ActBException(e.getErrorCode(), e.getMessage());
            }
            catch (SkyArkException e)
            {
                recvFeeHolder.setOrderNumber(bTradeCommInfo.getOrderNumber());
                bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.getStackTrace().toString(),"交费入库失败");
                logger.info("错误2：{},{}",e.getErrorCode(),e.getErrorMsg());
                throw new ActBException("SkyArkException错误："+e.getErrorMsg());
            }
            catch(Exception e)
            {
                recvFeeHolder.setOrderNumber(bTradeCommInfo.getOrderNumber());
                bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.getStackTrace().toString(),"交费入库失败");
                logger.info("错误3：{}",e.getStackTrace());
                throw new ActBException("Exception错误："+e.getStackTrace());
            }

        }
    }
}
