package com.withub.csp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.csp.entity.base.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "csp_system_menu")
public class SystemMenu extends BaseEntity {

    private SystemMenu parent;

    private String name;

    private String url;

    private String icon;

    private Integer orderNo;

    private List<SystemMenu> childList = new ArrayList<SystemMenu>();

    // ======================= Setter & Getter =======================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    public SystemMenu getParent() {
        return parent;
    }

    public void setParent(SystemMenu parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "delete_flag = 0")
    @JsonIgnore
    public List<SystemMenu> getChildList() {
        return childList;
    }

    public void setChildList(List<SystemMenu> childList) {
        this.childList = childList;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
