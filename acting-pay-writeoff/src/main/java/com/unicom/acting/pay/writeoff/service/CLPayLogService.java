package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.pay.domain.CLPayLog;
import com.unicom.acting.pay.domain.PayLog;
import com.unicom.acting.pay.domain.WriteOffLog;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * 省份代收日志生成服务
 *
 * @author Wangkh
 */
public interface CLPayLogService extends IBaseService {
    /**
     * 生成省份代收日志
     *
     * @param writeOffLogs
     * @param payLog
     * @param headerGray
     * @return
     */
    List<CLPayLog> genCLPaylog(List<WriteOffLog> writeOffLogs, PayLog payLog, String headerGray);
}
