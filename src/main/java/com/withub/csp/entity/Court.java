package com.withub.csp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.csp.entity.base.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "csp_court")
public class Court extends BaseEntity {

    private String name;

    private Integer courtType;

    private Court parent;

    private List<Department> departmentList;

    private List<Court> children;

    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCourtType() {
        return courtType;
    }

    public void setCourtType(Integer courtType) {
        this.courtType = courtType;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Court getParent() {
        return parent;
    }

    public void setParent(Court parent) {
        this.parent = parent;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "court", fetch = FetchType.LAZY)
    @OrderBy(value = "eventTime asc")
    @Where(clause = "delete_flag=0")
    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "eventTime asc")
    @Where(clause = "delete_flag=0")
    public List<Court> getChildren() {
        return children;
    }

    public void setChildren(List<Court> children) {
        this.children = children;
    }

}
