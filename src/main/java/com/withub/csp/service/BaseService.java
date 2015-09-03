package com.withub.csp.service;

import com.withub.csp.entity.base.BaseEntity;
import org.springside.modules.utils.Identities;

import java.util.Date;


public abstract class BaseService {

    public void initEntity(BaseEntity baseEntity){

        baseEntity.setId(Identities.uuid());
        baseEntity.setEventTime(new Date());
        baseEntity.setDeleteFlag(0);
    }

}
