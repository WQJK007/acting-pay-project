package com.unicom.acting.pay.bwriteoff.dao.impl;

import com.unicom.acting.pay.bwriteoff.dao.Identification;
import com.unicom.skyark.component.jdbc.dao.impl.JdbcBaseDao;

public class IdentificationImpl  extends JdbcBaseDao implements Identification {
    @Override
    public long getChargeId() {
        return 0;
    }

    @Override
    public long getOperateId() {
        return 0;
    }

    @Override
    public String getTradeId() {
        return null;
    }
}
