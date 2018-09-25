package com.unicom.acting.pay.oeprecvfee.dao;

import com.unicom.acting.pay.bwriteoff.domain.BatchRecvFeeIn;
import com.unicom.skyark.component.dao.IBaseDao;

public interface PayLogChkDao extends IBaseDao{
    public int updatePayLogChk(BatchRecvFeeIn recvFeeCommInfoIn);
    public int updatePayLogChkByType(BatchRecvFeeIn recvFeeIn, int desTradeTypeCode, int oraTradeTypeCode);
    }
