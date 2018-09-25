package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.TradeCommInfo;
import com.unicom.acting.pay.domain.TradeCommResultInfo;
import com.unicom.acting.pay.writeoff.domain.BackFeeCommInfoIn;
import com.unicom.skyark.component.service.IBaseService;

/**
 * 清退公共方法
 *
 * @author Wangkh
 */
public interface BackFeeCommService extends IBaseService {
    /**
     * 设置清退金额
     *
     * @param backFeeCommInfoIn
     * @param tradeCommInfo
     */
    void setBackFee(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo);


    /**
     * 生成预存清退入库信息
     *
     * @param backFeeCommInfoIn
     * @param tradeCommInfo
     * @param tradeCommResultInfo
     */
    void genBackFeeDBInfo(BackFeeCommInfoIn backFeeCommInfoIn, TradeCommInfo tradeCommInfo, TradeCommResultInfo tradeCommResultInfo);

}
