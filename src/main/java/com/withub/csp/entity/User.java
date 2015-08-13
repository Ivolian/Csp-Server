package com.withub.csp.entity;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "csp_user")
public class User extends BaseEntity {

    private String username;

    private String password;

    //

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
