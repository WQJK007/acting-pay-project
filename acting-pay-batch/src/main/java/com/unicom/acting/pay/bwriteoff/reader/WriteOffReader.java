package com.unicom.acting.pay.bwriteoff.reader;
import com.unicom.acting.batch.common.domain.BTradeCommInfo;
import com.unicom.acting.batch.common.exception.ActBException;
import com.unicom.acting.batch.common.exception.ActBSysTypes;
import com.unicom.acting.common.domain.Account;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.fee.writeoff.service.FeeCommService;
import com.unicom.acting.pay.bwriteoff.business.BWriteOffMgr;
import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.acting.pay.bwriteoff.domain.RecvFeeHolder;
import com.unicom.acting.pay.bwriteoff.realbill.RealBillMgr;
import com.unicom.skyark.component.exception.SkyArkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("writeOffReader")
@StepScope
public class WriteOffReader implements ItemReader<BTradeCommInfo<TradeCommInfo>> {
    private static final Logger logger = LoggerFactory.getLogger(WriteOffReader.class);

    @Value("#{jobParameters[dataSourceId]}")
    private String provinceId = "";
    @Autowired
    private RecvFeeHolder<BatchRecvFeeIn> recvFeeHolder;

    @Autowired
    private BWriteOffMgr bWriteOffMgr;

    @Autowired
    RealBillMgr realBillMgr;

    @Autowired
    private FeeCommService feeCommService;

    private int count = 0;
    //工单计数
    public BTradeCommInfo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        try {

            List<BatchRecvFeeIn> orders = recvFeeHolder.getListInstance();
            if (null != orders && count < orders.size()) {
                logger.info("#########记录数标记 count = {}",count);
                BatchRecvFeeIn order = recvFeeHolder.getListInstance().get(count);
                recvFeeHolder.setOrderNumber(count);

                BTradeCommInfo bTradeCommInfo = new BTradeCommInfo();
                bTradeCommInfo.setOrderNumber(count);
                count++;

                TradeCommInfo tradeCommInfo = new TradeCommInfo();

                logger.info("-------获取用户资料通过 begin");
                bWriteOffMgr.getBUserDatumInfo(order,tradeCommInfo);
                logger.info("-------获取用户资料通过 end");
                feeCommService.getEparchyCycleInfo(tradeCommInfo, order.getEparchyCode(), order.getProvinceCode());
                Account feeAccount = tradeCommInfo.getAccount();
                //tradeCommService.genLockAccount(feeAccount.getAcctId(), recvFeeCommInfoIn.getProvinceCode());
                //获取省份地市销账规则
                feeCommService.getWriteOffRule(tradeCommInfo.getWriteOffRuleInfo(), feeAccount.getProvinceCode(), feeAccount.getEparchyCode(), feeAccount.getNetTypeCode());
                //查询账本
                feeCommService.getAcctBalance(order, tradeCommInfo);
                //查询实时账单
                realBillMgr.getReallbill(tradeCommInfo);
                //查询账单
                feeCommService.getOweBill(order, tradeCommInfo);
                //特殊业务校验
                feeCommService.specialBusiCheck(order, tradeCommInfo);
                //只有做滞纳金计算才加载滞纳金减免工单和账户自定义缴费期
                if (!feeCommService.ifCalcLateFee(order, tradeCommInfo)) {
                    //计算滞纳金
                    tradeCommInfo.setCalcLateFee(true);
                    //获取滞纳金减免工单
                    feeCommService.getFeeDerateLateFeeLog(order, tradeCommInfo);
                    //获取账户自定义缴费期
                    feeCommService.getAcctPaymentCycle(tradeCommInfo, feeAccount.getAcctId());
                }
                bTradeCommInfo.setTradeCommonInfo(tradeCommInfo);
                order.setResultCode(ActBSysTypes.RESULT_DEALING);
                order.setResultInfo("reader success！");
                order.setRemark("读取数据成功");
                return bTradeCommInfo;
            }
            else {
                return null;
            }
        }catch(ActBException e)
        {
            bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.toString(),"交费读取资料失败");
            logger.info("错误1：{},{}",e.getErrorCode(),e.getErrorMsg());
            throw new ActBException(e.getErrorCode(), e.getMessage());
        }
        catch (SkyArkException e)
        {
            bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.toString(),"交费读取资料失败");
            logger.info("错误2：{},{}",e.getErrorCode(),e.getErrorMsg());
            throw new ActBException("SkyArkException错误："+e.getErrorMsg());
        }
        catch(Exception e)
        {
            bWriteOffMgr.setResultInfo(recvFeeHolder,ActBSysTypes.RESULT_ERROR,ActBSysTypes.DEALED_TAG_ERROR,e.getStackTrace().toString(),"交费读取资料失败");
            logger.info("错误3：{}",e.getStackTrace());
            throw new ActBException("Exception错误："+e.getStackTrace(),"交费读取资料失败");
        }
    }
}