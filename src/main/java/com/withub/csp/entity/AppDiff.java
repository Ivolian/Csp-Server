package com.withub.csp.entity;

import com.withub.common.FileUploadInfo;
import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "csp_app_diff")
public class AppDiff extends BaseEntity {

    private String versionName;     // 版本号
    private String clientVersionName;
    private String diff;
    private String diffFilename;
    private FileUploadInfo diffAttachment;
    private String content;

    //

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getDiffFilename() {
        return diffFilename;
    }

    public void setDiffFilename(String diffFilename) {
        this.diffFilename = diffFilename;
    }

    @Transient
    public FileUploadInfo getDiffAttachment() {
        return diffAttachment;
    }

    public void setDiffAttachment(FileUploadInfo diffAttachment) {
        this.diffAttachment = diffAttachment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClientVersionName() {
        return clientVersionName;
    }

    public void setClientVersionName(String clientVersionName) {
        this.clientVersionName = clientVersionName;
    }

}
