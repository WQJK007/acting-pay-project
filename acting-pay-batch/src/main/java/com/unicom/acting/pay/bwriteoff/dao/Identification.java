package com.unicom.acting.pay.bwriteoff.dao;
import com.unicom.skyark.component.dao.IBaseDao;
public interface Identification extends IBaseDao {
    public long getChargeId();
    public long getOperateId();
    public String getTradeId();
}
