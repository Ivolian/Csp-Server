package com.withub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//JPA标识
@Entity
@Table(name = "csp_news_data")
public class ContentData extends IdEntity {

    private Content content;

    private String data;

    @JsonIgnore
    @OneToOne(targetEntity = Content.class)
    @JoinColumn(name = "news_id")
    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
