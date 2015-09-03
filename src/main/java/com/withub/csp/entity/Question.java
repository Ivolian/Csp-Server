package com.withub.csp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "csp_question")
public class Question extends BaseEntity {

    private User user;
    private String content;
    private List<Answer> answerList;

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

    @JsonIgnore
    @OneToMany(mappedBy = "question")
    @OrderBy("eventTime desc")
    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

}
