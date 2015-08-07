package com.withub.entity;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "csp_question")
public class Question extends IdEntity {

    private String content;

    private CspUser user;

    private Date eventTime;

    private Integer deleteFlag;

    //

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    //


    private List<Answer> answerList;

    @OneToMany(mappedBy = "question")
    @OrderBy("eventTime desc ")
    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }
}
