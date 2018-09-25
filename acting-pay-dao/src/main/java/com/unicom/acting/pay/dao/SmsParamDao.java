package com.unicom.acting.pay.dao;

import com.unicom.acting.pay.domain.SmsCond;
import com.unicom.acting.pay.domain.SmsConvert;
import com.unicom.acting.pay.domain.SmsTemplet;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * 充值短信发送相关参数查询
 *
 * @author Wangkh
 */
public interface SmsParamDao extends IBaseDao {
    /**
     * 储值方式发送短信模板
     *
     * @return
     */
    List<SmsCond> getProvSmsCond();

    /**
     * 短信转换模板
     *
     * @return
     */
    List<SmsConvert> getSmsConvertId();

    /**
     * 短信模板内容
     *
     * @return
     */
    List<SmsTemplet> getSmsTemplet();


}
