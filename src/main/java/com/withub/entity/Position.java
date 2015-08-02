package com.withub.entity;

import com.withub.common.FileUploadInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "csp_book")
public class Position extends IdEntity{

    /*
        书名
        封面
        书籍
     */

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
}
