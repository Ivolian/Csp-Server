package com.withub.csp.entity;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "csp_menu")
public class Menu extends BaseEntity {

    private String name;

    private String type;

    private Menu parent;

    private Integer orderNo;

    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "parent_id")
    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
