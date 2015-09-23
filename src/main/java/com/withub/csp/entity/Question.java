package com.withub.csp.entity;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "csp_question")
public class Question extends BaseEntity {

    private User user;
    private String content;
    private String type;    // 有问有答 建议意见

    // ======================= Setter & Getter =======================

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
