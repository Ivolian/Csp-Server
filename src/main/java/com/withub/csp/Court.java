package com.withub.csp;

import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "csp_court")
public class Court extends BaseEntity{

    private String courtName;

    private Integer CourtType;

    //

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public Integer getCourtType() {
        return CourtType;
    }

    public void setCourtType(Integer courtType) {
        CourtType = courtType;
    }

    // todo countParent

}
