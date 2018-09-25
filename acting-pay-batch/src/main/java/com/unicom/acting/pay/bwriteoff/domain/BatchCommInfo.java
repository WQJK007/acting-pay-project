package com.unicom.acting.pay.bwriteoff.domain;

import com.unicom.acting.fee.domain.TradeCommInfo;

public class BatchCommInfo extends TradeCommInfo {
    private int orderNumber;
    public BatchCommInfo()
    {
        super();
        orderNumber = 0;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
