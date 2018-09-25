package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.SmsOrder;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * 主要记录交易短信工单数据，待系统运行稳定后不再记录短信
 * 交易短信可以通过MQ消息进行查看
 *
 * @author Wangkh
 */
public interface SmsOrderDao extends IBaseDao {

    /**
     * 短信入库
     *
     * @param smsOrders
     */
    void insertSmsOrder(List<SmsOrder> smsOrders);
}
