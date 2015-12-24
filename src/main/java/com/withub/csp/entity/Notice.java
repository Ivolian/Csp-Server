package com.withub.csp.entity;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "csp_notice")
public class Notice extends BaseEntity {

    private String title;
    private String content;

    //

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
