package com.withub.csp.entity;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "csp_favorite_news")
public class FavoriteNews extends BaseEntity {

    private User user;

    private News news;

    //

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "news_id")
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

}
