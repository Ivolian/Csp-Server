package com.withub.csp.entity;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/*
    todo
    表结构修改
    1. 添加 position, telephone, qq, email
    2. court_id 从 int 改为 varchar36
 */

@Entity
@Table(name = "csp_user")
public class User extends BaseEntity {

    private String username;     // 用户名（和手机一致）
    private String password;
    private String cnName;       // 姓名
    private Court court;         // 所属法院
    private String position;     // 职位
    private String telephone;    // 手机号
    private String qq;
    private String email;

    // ======================= Setter & Getter =======================

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

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    @ManyToOne
    @JoinColumn(name = "court_id")
    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
