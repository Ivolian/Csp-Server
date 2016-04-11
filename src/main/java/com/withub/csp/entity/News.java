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

    private String title;                       // 新闻标题
    private String picture;                     // 新闻图片
    private String pictureFilename;
    private FileUploadInfo pictureAttachment;
    private Date postTime;                      // 发布时间
    private NewsData newsData;                  // 新闻内容
    private List<Comment> commentList;
    private List<Thumb> thumbList;
    private Department department;
    private Integer hasVideo;
    private Integer videoType;
    private String videoUrl;
    private Integer top;

    // ======================= Setter & Getter =======================

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

    @Transient
    public NewsData getNewsData() {
        return newsData;
    }

    public void setNewsData(NewsData newsData) {
        this.newsData = newsData;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY)
    @Where(clause = "delete_flag=0")
    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY)
    @Where(clause = "delete_flag=0")
    public List<Thumb> getThumbList() {
        return thumbList;
    }

    public void setThumbList(List<Thumb> thumbList) {
        this.thumbList = thumbList;
    }

    @ManyToOne
    @JoinColumn(name = "department_id")
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Integer hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Integer getVideoType() {
        return videoType;
    }

    public void setVideoType(Integer videoType) {
        this.videoType = videoType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

}
