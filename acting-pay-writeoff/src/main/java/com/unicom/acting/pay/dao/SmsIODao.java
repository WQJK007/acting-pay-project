package com.unicom.acting.pay.dao;

import com.unicom.skyark.component.dao.IBaseDao;
import com.unicom.acting.fee.domain.NoticeInfo;

import java.util.List;

public interface SmsIODao extends IBaseDao {

    /**
     * 短信入库
     * @param noticeInfoList
     * @param provinceCode
     */
    void insertSmsIO(List<NoticeInfo> noticeInfoList, String provinceCode);
}
