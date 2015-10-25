package com.withub.csp.entity;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "csp_department")
public class Department extends BaseEntity {

    private String name;

    private Court court;

    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "court_id")
    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

}
