package com.withub.csp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.common.FileUploadInfo;
import com.withub.csp.entity.base.MenuEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "csp_news")
public class News extends MenuEntity {

    // 新闻标题
    private String title;

    // 图片
    private String picture;

    private String pictureFilename;

    private FileUploadInfo pictureAttachment;

    // 发布时间
    private Date postTime;

    // 新闻内容
    private NewsData newsData;

    //

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    @OneToOne(mappedBy = "news", fetch = FetchType.LAZY)
    public NewsData getNewsData() {
        return newsData;
    }

    public void setNewsData(NewsData newsData) {
        this.newsData = newsData;
    }


    private List<Comment> commentList;

    /*
        todo
        我尝试用 @Where(clause = "deleteFlag=0")
        但好像有点问题
     */

//    @JsonIgnore
//    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY)
//    @Where(clause = "delete_flag=0")
//    public List<Comment> getCommentList() {
//        return commentList;
//    }
//
//    public void setCommentList(List<Comment> commentList) {
//        this.commentList = commentList;
//    }
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY)
//    public List<Thumb> getThumbList() {
//        return thumbList;
//    }
//
//    public void setThumbList(List<Thumb> thumbList) {
//        this.thumbList = thumbList;
//    }

}
