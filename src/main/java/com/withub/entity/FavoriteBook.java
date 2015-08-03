package com.withub.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.awt.print.Book;

@Entity
@Table(name = "csp_favoritebook")
public class FavoriteBook extends IdEntity {

    private CspUser user;

    private Position book;

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
    @JoinColumn(name = "book_id")
    public Position getBook() {
        return book;
    }

    public void setBook(Position book) {
        this.book = book;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}
