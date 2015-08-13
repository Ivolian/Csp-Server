package com.withub.csp.entity.base;

import javax.persistence.MappedSuperclass;
import java.util.Date;


@MappedSuperclass
public abstract class BaseEntity extends IdEntity {

    protected Integer deleteFlag;

    protected Date eventTime;

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

}
