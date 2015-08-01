package com.withub.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "csp_comment")
public class Comment extends IdEntity {

    // 所属新闻
    private Content news;

    // 所属用户
    private CspUser user;

    // 创建时间
    private Date eventTime;

    // 评论内容
    private String content;

    private Integer deleteFlag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public CspUser getUser() {
        return user;
    }

    public void setUser(CspUser user) {
        this.user = user;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne
    @JoinColumn(name = "content_id")
    public Content getNews() {
        return news;
    }

    public void setNews(Content news) {
        this.news = news;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}
