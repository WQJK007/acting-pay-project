package com.unicom.acting.pay.oeprecvfee.writer;

import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.oeprecvfee.domain.PayLogChk;
import com.unicom.acting.pay.bwriteoff.domain.RecvFeeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("payLogChkWriter")
@StepScope
public class PayLogChkWriter implements ItemWriter<PayLogChk> {
    private static final Logger logger = LoggerFactory.getLogger(PayLogChkWriter.class);
    @Autowired
    private RecvFeeHolder<BatchRecvFeeIn> recvFeeHolder ;
    @Override
    public void write(List<? extends PayLogChk> items) throws ActBException {
        for(PayLogChk item:items)
        {
            BatchRecvFeeIn recvFeeIn = new BatchRecvFeeIn();
            logger.info("write::getAcctId:{}",item.getAcctId());
            recvFeeIn.setAcctId(item.getAcctId());
            recvFeeIn.setUserId(item.getUserId());
            recvFeeIn.setSerialNumber(item.getSerialNumber());
            recvFeeIn.setWriteoffMode(item.getWriteoffMode());
            recvFeeIn.setChannelId(item.getChannelId());
            recvFeeIn.setPaymentId(item.getPaymentId());
            recvFeeIn.setPayFeeModeCode(item.getPayFeeModeCode());
            recvFeeIn.setPaymentOp(item.getPaymentOp());
            recvFeeIn.setTradeId(item.getOuterTradeId());
            recvFeeIn.setDepositStartDate(item.getStartDate());
            recvFeeIn.setLimitMoney(Long.parseLong(item.getLimitMoney()));
            recvFeeIn.setRemark(item.getRemark());
            recvFeeIn.setLimitMode(item.getLimitMode());
            recvFeeIn.setProvinceCode(item.getProvinceCode());
            recvFeeIn.setTradeFee(Long.parseLong(item.getRecvFee()));
            recvFeeIn.setAmonths(item.getMonths());
            recvFeeIn.setBillStartCycleId(item.getStartCycleId());
            recvFeeIn.setBillEndCycleId(item.getEndCycleId());
            recvFeeIn.setOriginTradeId(item.getTradeId());
            recvFeeIn.setBatchId(item.getBatchId());
            recvFeeIn.setOriginActTag(item.getActTag());
            recvFeeIn.setOriginDealtag(item.getDealTag());
            logger.info("write::setTradeTypeCode:{}",item.getTradeTypeCode());

            recvFeeIn.setTradeTypeCode(item.getTradeTypeCode());
            //标志批处理调用
            recvFeeIn.setBatchDealTag("1");
            recvFeeHolder.getListInstance().add(recvFeeIn);
        }

    }
}
