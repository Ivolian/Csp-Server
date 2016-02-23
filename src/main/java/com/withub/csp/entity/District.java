package com.withub.csp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withub.csp.entity.base.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "csp_district")
public class District extends BaseEntity {

    private String name;

    private List<Court> courtList;

    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    @OrderBy(value = "eventTime asc")
    @Where(clause = "delete_flag=0")
    public List<Court> getCourtList() {
        return courtList;
    }

    public void setCourtList(List<Court> courtList) {
        this.courtList = courtList;
    }
}
