package com.withub.csp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.csp.entity.base.IdEntity;

import javax.persistence.*;

@Entity
@Table(name = "csp_role_menu")
public class RoleMenu extends IdEntity {

    private SystemMenu systemMenu;

    private Role role;

    // ======================= Setter & Getter =======================

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @JsonIgnore
    public SystemMenu getSystemMenu() {
        return systemMenu;
    }

    public void setSystemMenu(SystemMenu systemMenu) {
        this.systemMenu = systemMenu;
    }

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //

    @Transient
    public String getMenuId() {
        return systemMenu.getId();
    }

    @Transient
    public String getRoleId() {
        return role.getId();
    }

}
