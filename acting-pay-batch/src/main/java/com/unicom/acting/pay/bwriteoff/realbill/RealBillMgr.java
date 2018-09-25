package com.unicom.acting.pay.bwriteoff.realbill;

import com.unicom.acting.bill.realbill.domain.Bill;
import com.unicom.acting.bill.realbill.domain.RealBillQryInfo;
import com.unicom.acting.fee.domain.FeeBill;
import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.skyark.component.util.StringUtil;
import com.unicom.skyark.component.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
@Component
public class RealBillMgr {
    private static final Logger logger = LoggerFactory.getLogger(RealBillMgr.class);

    public void getReallbill(TradeCommInfo tradeCommInfo)
    {
        // 准备数据
        //List<FeeBill> bills = Collections.synchronizedList(new ArrayList<FeeBill>());

        RealBillQryInfo realBillQryInfo = new RealBillQryInfo();
        realBillQryInfo.setAcctId(tradeCommInfo.getAccount().getAcctId());

        //String eparchyCode = tradeCommInfo.getMainUser().getEparchyCode();
        // 获取最大开账账期
        int maxCycle = tradeCommInfo.getWriteOffRuleInfo().getMaxAcctCycle().getCycleId();
        int startCycle = TimeUtil.genCycle(maxCycle, 1);
        // 获取当前开账账期
        int endCycle = tradeCommInfo.getWriteOffRuleInfo().getCurCycle().getCycleId();
        List<FeeBill> bills = new ArrayList();
        List<Bill> tmpBills=null;
        //循环查询实时账单
        FeeBill realBill = null;
        for(int i = startCycle ; i <= endCycle; i = TimeUtil.genCycle(i, 1)) {
            realBillQryInfo.setCycleId(i);
            //tmpBills = billAccountService.getRealBillAll(realBillQryInfo,tradeCommInfo.getMainUser().getProvinceCode());

            if(!CollectionUtils.isEmpty(tmpBills)) {

                for (Bill tmpBill : tmpBills) {
                    realBill = new FeeBill();
                    realBill.setAcctId(tmpBill.getAcctId());
                    realBill.setUserId(tmpBill.getUserId());
                    realBill.setCycleId(tmpBill.getCycleId());
                    realBill.setBillId("");
                    realBill.setIntegrateItemCode(tmpBill.getIntegrateItemCode());
                    realBill.setFee(tmpBill.getFee());
                    realBill.setBalance(tmpBill.getBalance());
                    realBill.setbDiscnt(tmpBill.getbDiscnt());
                    realBill.setaDiscnt(tmpBill.getaDiscnt());
                    realBill.setAdjustBefore(tmpBill.getAdjustBefore());
                    realBill.setAdjustAfter(tmpBill.getAdjustAfter());
                    if (!StringUtil.isEmptyCheckNullStr(tmpBill.getUpdateTime())) {
                        realBill.setUpdateTime(tmpBill.getUpdateTime());
                    }
                    //bill.canpayTag = '2';
                    //bill.payTag = '0';
                    //bill.billPayTag='0';
                    logger.info("realFeeBill getCanpayTag() = {}",tmpBill.getCanpayTag());
                    logger.info("realFeeBill getPayTag() = {}",tmpBill.getPayTag());
                    logger.info("realFeeBill getBillPayTag() = {}",tmpBill.getBillPayTag());
                    realBill.setCanpayTag(tmpBill.getCanpayTag());
                    realBill.setPayTag(tmpBill.getPayTag());
                    realBill.setBillPayTag(tmpBill.getBillPayTag());
                    realBill.setPrepayTag(tmpBill.getPrepayTag());

                    bills.add(realBill);
                }
                tradeCommInfo.getFeeBills().addAll(bills);
            }
        }
    }
}
