package com.withub.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "csp_favorite")
public class Favorite extends IdEntity {

    private CspUser user;

    private Content news;

    private Integer deleteFlag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public CspUser getUser() {
        return user;
    }

    public void setUser(CspUser user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "news_id")
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
