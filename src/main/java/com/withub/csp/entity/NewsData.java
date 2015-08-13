package com.withub.csp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.csp.entity.base.IdEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "csp_news_data")
public class NewsData extends IdEntity {

    private News news;

    private String data;

    //

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "news_id")
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
