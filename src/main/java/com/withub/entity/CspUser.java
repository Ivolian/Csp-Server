package com.withub.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;


// 因为 User 类已经存在，所以暂时命名为 CwpUser
@Entity
@Table(name = "csp_user")
public class CspUser extends IdEntity {

    private String username;

    private String password;

    private Integer deleteFlag;

//    private List<Favorite> favoriteList;

//    // todo @where
//    @OneToMany(mappedBy = "cspUser")
//    public List<Favorite> getFavoriteList() {
//        return favoriteList;
//    }
//
//    public void setFavoriteList(List<Favorite> favoriteList) {
//        this.favoriteList = favoriteList;
//    }
//
//    @Transient
//    public int getFavoriteCount() {
//
//        if (favoriteList != null) {
//            return favoriteList.size();
//        }
//        return 0;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
