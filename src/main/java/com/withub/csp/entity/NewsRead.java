package com.withub.csp.entity;

import com.withub.common.FileUploadInfo;
import com.withub.csp.entity.base.BaseEntity;
import com.withub.csp.entity.base.IdEntity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "csp_news_read")
public class NewsRead extends IdEntity {

    private News news;

    private User user;

    private Date eventTime;

    //

    @ManyToOne
    @JoinColumn(name = "news_id")
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

}
