package com.withub.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ss_favorite")
public class Favorite extends IdEntity {

//    private CspUser cspUser;

    private Content content;

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
