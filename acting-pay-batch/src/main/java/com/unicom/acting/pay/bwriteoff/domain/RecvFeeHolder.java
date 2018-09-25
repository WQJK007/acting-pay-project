package com.unicom.acting.pay.bwriteoff.domain;
import com.unicom.acting.batch.common.domain.JobInstancesHolder;
import org.springframework.stereotype.Component;

@Component("recvFeeHolder")
public abstract class RecvFeeHolder<T> extends JobInstancesHolder<T> {
    public RecvFeeHolder()
    {
        super();
    }

}


