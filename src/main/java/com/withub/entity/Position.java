package com.withub.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ss_position")
public class Position extends IdEntity{
    /*职位名*/
    private String name;
    /*学历
    * 0: 无限制
    * 1：高中
    * 2：大专
    * 3：本科
    * 4：硕士以上
    * */
    private Integer education;
    /*招聘人数*/
    private Integer experience;
    /*年龄要求*/
    private String ageRequirement;
    /*任职要求*/
    private String jobRequirements;
    /*删除
    * 0：存在
    * 1：删除*/
    private Integer deleteFlag;

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getAgeRequirement() {
        return ageRequirement;
    }

    public void setAgeRequirement(String ageRequirement) {
        this.ageRequirement = ageRequirement;
    }

    public String getJobRequirements() {
        return jobRequirements;
    }

    public void setJobRequirements(String jobRequirements) {
        this.jobRequirements = jobRequirements;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
