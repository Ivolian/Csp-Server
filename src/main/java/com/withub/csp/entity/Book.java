package com.withub.csp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.common.FileUploadInfo;
import com.withub.csp.entity.base.MenuEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "csp_book")
public class Book extends MenuEntity {

    private String name;                    // 书名
    private String author;
    private String summary;                 // 简介
    private String picture;                 // 封面
    private String pictureFilename;
    private FileUploadInfo pictureAttachment;
    private String ebook;                   // 电子书
    private String ebookFilename;
    private FileUploadInfo ebookAttachment;

    // 排序号，因为手机端这边需要个唯一的整型来标识一本书
    private Integer orderNo;

    private List<BookComment> bookCommentList;


    // ======================= Setter & Getter =======================

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

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    //

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @Where(clause = "delete_flag=0")
    public List<BookComment> getBookCommentList() {
        return bookCommentList;
    }

    public void setBookCommentList(List<BookComment> bookCommentList) {
        this.bookCommentList = bookCommentList;
    }
}
