package com.withub.csp.entity;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


// todo 提醒小任 表结构改动
@Entity
@Table(name = "csp_court")
public class Court extends BaseEntity{

    private String name;

    private Integer courtType;

    private Court parent;

    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCourtType() {
        return courtType;
    }

    public void setCourtType(Integer courtType) {
        this.courtType = courtType;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Court getParent() {
        return parent;
    }

    public void setParent(Court parent) {
        this.parent = parent;
    }

}
