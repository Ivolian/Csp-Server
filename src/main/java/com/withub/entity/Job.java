package com.withub.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ss_job")
public class Job extends IdEntity {
    /*招聘人数*/
    private Integer recruitmentNumber;
    /*任职地区*/
    private Menu menu;
    /*任职地点*//*
    private String place;*/
    /*发布
    * 0：发布
    * 1：不发布*/
    private Integer publish;

    private Position position;

//    private CspUser cspUser;
    /*排序*/
    private Integer orderNo;
    /*删除
    * 0：存在
    * 1：删除*/
    private Integer deleteFlag;

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getRecruitmentNumber() {
        return recruitmentNumber;
    }

    public void setRecruitmentNumber(Integer recruitmentNumber) {
        this.recruitmentNumber = recruitmentNumber;
    }

    public Integer getPublish() {
        return publish;
    }

    public void setPublish(Integer publish) {
        this.publish = publish;
    }

    @ManyToOne(targetEntity = Position.class)
    @JoinColumn(name = "position_id")
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

//    @ManyToOne(targetEntity = CspUser.class)
//    @JoinColumn(name = "email_id")
//    public CspUser getCspUser() {
//        return cspUser;
//    }
//
//    public void setCspUser(CspUser cspUser) {
//        this.cspUser = cspUser;
//    }

    @ManyToOne(targetEntity = Menu.class)
    @JoinColumn(name = "region_id")
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
