package com.unicom.acting.pay.writeoff.domain;

import com.unicom.acting.pay.domain.AccessLog;

import java.util.Comparator;

/**
 * 存取款日志按照accesslogId排序
 */
public class AccssslogCancelSortRule implements Comparator<AccessLog> {
    @Override
    public int compare(AccessLog left, AccessLog right) {
        //小的优先
        if (left.getAccessId().compareTo(right.getAccessId()) < 0) {
            return -1;
        } else if(left.getAccessId().compareTo(right.getAccessId()) == 0) {
            return 0;
        }else{
            return 1;
        }
    }
}
