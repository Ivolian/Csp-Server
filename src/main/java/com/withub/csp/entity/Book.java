package com.withub.csp.entity;

import com.withub.common.FileUploadInfo;
import com.withub.csp.entity.base.BaseEntity;
import com.withub.csp.entity.Menu;
import com.withub.csp.entity.base.MenuEntity;

import javax.persistence.*;


@Entity
@Table(name = "csp_book")
public class Book extends MenuEntity {

    // 书名
    private String name;

    // 作者
    private String author;

    // 简介
    private String summary;

    // 封面
    private String picture;

    private String pictureFilename;

    private FileUploadInfo pictureAttachment;

    // 电子书
    private String ebook;

    private String ebookFilename;

    private FileUploadInfo ebookAttachment;

    // 排序号，有他用
    private Integer orderNo;

    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    //

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

    //

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

}
