package com.withub.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ss_product")
public class Product extends IdEntity {
    /*产品名称*/
    private String productName;
    /*产品简称*/
    private String shortName;
    /*描述*/
    private String description;
    /*功能*/
    private String function;
    /*特点*/
    private String feature;
    /*案例*/
    private String documentation;

    /**
     * 1: 办案平台
     * 2: 政务平台
     * 3: 队伍平台
     * 4: 公开平台
     * 5: 数据平台
     * 6: 企业应用
     */
    private Integer productClassification;

    private String icon;

    private Integer orderNo;

    /*删除
    * 0：存在
    * 1：删除
    * */
    private Integer deleteFlag;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public Integer getProductClassification() {
        return productClassification;
    }

    public void setProductClassification(Integer productClassification) {
        this.productClassification = productClassification;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
