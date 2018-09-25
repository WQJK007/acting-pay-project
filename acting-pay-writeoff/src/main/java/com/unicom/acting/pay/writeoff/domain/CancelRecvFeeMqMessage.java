package com.unicom.acting.pay.writeoff.domain;

import com.unicom.acting.pay.domain.AccessLog;
import com.unicom.acting.pay.domain.PayLog;

import java.util.List;
import java.util.Map;

/**
 * @author ducj
 */
public class CancelRecvFeeMqMessage {

    private int resultTag;

    private Map<String, List<AccessLog>> accesslogListMap;

    private List<PayLog> cancelPayLogList;

    public int getResultTag() {
        return resultTag;
    }

    public void setResultTag(int resultTag) {
        this.resultTag = resultTag;
    }

    public Map<String, List<AccessLog>> getAccesslogListMap() {
        return accesslogListMap;
    }

    public void setAccesslogListMap(Map<String, List<AccessLog>> accesslogListMap) {
        this.accesslogListMap = accesslogListMap;
    }

    public List<PayLog> getCancelPayLogList() {
        return cancelPayLogList;
    }

    public void setCancelPayLogList(List<PayLog> cancelPayLogList) {
        this.cancelPayLogList = cancelPayLogList;
    }

    @Override
    public String toString() {
        return "CancelRecvFeeMqMessage{" +
                "resultTag=" + resultTag +
                ", accesslogListMap=" + accesslogListMap +
                ", cancelPayLogList=" + cancelPayLogList +
                '}';
    }
}
