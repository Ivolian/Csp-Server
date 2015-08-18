package com.withub.csp.entity;

import com.withub.csp.entity.User;
import com.withub.csp.entity.base.IdEntity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "csp_user_login")
public class UserLogin extends IdEntity {

    private User user;

    private Date eventTime;

    //

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
}
