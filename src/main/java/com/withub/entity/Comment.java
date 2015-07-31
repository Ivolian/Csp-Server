package com.withub.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ss_comment")
public class Comment extends IdEntity {

//    这个是冒充用户的
//    private CspUser cspUser;

    private Date eventTime;

    private String words;

    private Content content;

    /*删除
    * 0：存在
    * 1：删除
    * */
    private Integer deleteFlag;

//    @ManyToOne
//    @JoinColumn(name = "email_id")
//    public CspUser getCspUser() {
//        return cspUser;
//    }
//
//    public void setCspUser(CspUser cspUser) {
//        this.cspUser = cspUser;
//    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    @ManyToOne
    @JoinColumn(name = "content_id")
    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}
