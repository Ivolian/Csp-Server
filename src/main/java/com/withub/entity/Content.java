/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.withub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.common.FileUploadInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//JPA标识
@Entity
@Table(name = "ss_content")
public class Content extends IdEntity {

    private String title;
    private String summary;
    private ContentColumn contentColumn;
    private String contentColumnId;
    private ContentData contentData;
    private User user;
    private Integer publish;
    private Date eventTime;
    private String picture;
    private String pictureFilename;
    private Integer deleteFlag;
    private FileUploadInfo attachment;


    // 新加的,冒充新闻类型
    private Region region;

    // 评论
    @Transient
    private List<Comment> commentList;

    @Transient
    @JsonIgnore
    @OneToMany(mappedBy = "content")
    public List<Comment> getCommentList() {
        return commentList;
    }

    @Transient
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @JsonIgnore
    @Transient
    public int getCommentCount() {

        if (commentList != null) {
            return commentList.size();
        }
        return 0;
    }

    // JSR303 BeanValidator的校验规则
    @NotBlank
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // JPA 基于CONTENT_COLUMN_ID列的多对一关系定义
    @OneToOne()
    @JoinColumn(name = "content_column_id", insertable = false, updatable = false)
    public ContentColumn getContentColumn() {
        return contentColumn;
    }

    public void setContentColumn(ContentColumn contentColumn) {
        this.contentColumn = contentColumn;
    }

    @Column(name = "content_column_id")
    public String getContentColumnId() {
        return contentColumnId;
    }

    public void setContentColumnId(String contentColumnId) {
        this.contentColumnId = contentColumnId;
    }

    @OneToOne(targetEntity = ContentData.class, mappedBy = "content", fetch = FetchType.LAZY)
//    @Transient
    public ContentData getContentData() {
        return contentData;
    }

    public void setContentData(ContentData contentData) {
        this.contentData = contentData;
    }

    public Integer getPublish() {
        return publish;
    }

    public void setPublish(Integer publish) {
        this.publish = publish;
    }

    // JPA 基于USER_ID列的多对一关系定义
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
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
    public FileUploadInfo getAttachment() {
        return attachment;
    }

    public void setAttachment(FileUploadInfo attachment) {
        this.attachment = attachment;
    }

    @ManyToOne(targetEntity = Region.class)
    @JoinColumn(name = "region_id")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

}
