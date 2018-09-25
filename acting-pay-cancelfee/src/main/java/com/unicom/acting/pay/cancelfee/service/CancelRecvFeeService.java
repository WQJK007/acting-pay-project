package com.unicom.acting.pay.cancelfee.service;

import com.unicom.acting.pay.domain.PayLogDmn;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfoIn;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfoOut;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeMqMessage;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;

/**
 * @author ducj
 */
public interface CancelRecvFeeService extends IBaseService {
    /**
     * 交费返销 - Common(前台) - acting.pay.cancelfee
     *
     * @param cancelRecvFeeInfoIn 交费返销入参
     * @return 返销输出对象
     */
    CancelRecvFeeInfoOut cancelFeeCommon(CancelRecvFeeInfoIn cancelRecvFeeInfoIn);

    /**
     * paylogdmn表入库
     * @param payLogDmnList  dmn入库列表
     * @return 入库是否成功
     */
    boolean paylogDmnListIndb(List<PayLogDmn> payLogDmnList);

    /**
     * 构建mq消息体
     * @param cancelRecvFeeInfoOut 返销输出对象
     * @return mq消息体
     */
    CancelRecvFeeMqMessage genCancelFeeMqMessage(CancelRecvFeeInfoOut cancelRecvFeeInfoOut);

}
