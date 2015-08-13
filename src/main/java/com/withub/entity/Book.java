package com.withub.entity;

import com.withub.common.FileUploadInfo;

import javax.persistence.*;


@Entity
@Table(name = "csp_book")
public class Book extends IdEntity{

    private String name;

    private Integer deleteFlag;

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String picture;
    private String pictureFilename;
    private FileUploadInfo pictureAttachment;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPictureFilename() {
        return pictureFilename;
    }

    public void setPictureFilename(String pictureFilename) {
        this.pictureFilename = pictureFilename;
    }

    @Transient
    public FileUploadInfo getPictureAttachment() {
        return pictureAttachment;
    }

    public void setPictureAttachment(FileUploadInfo pictureAttachment) {
        this.pictureAttachment = pictureAttachment;
    }

    //

    private String ebook;
    private String ebookFilename;
    private FileUploadInfo ebookAttachment;

    public String getEbook() {
        return ebook;
    }

    public void setEbook(String ebook) {
        this.ebook = ebook;
    }

    public String getEbookFilename() {
        return ebookFilename;
    }

    public void setEbookFilename(String ebookFilename) {
        this.ebookFilename = ebookFilename;
    }

    @Transient
    public FileUploadInfo getEbookAttachment() {
        return ebookAttachment;
    }

    public void setEbookAttachment(FileUploadInfo ebookAttachment) {
        this.ebookAttachment = ebookAttachment;
    }


    private Integer orderNo;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }


    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    private String summary;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
