package com.withub.csp.entity;

import com.withub.common.FileUploadInfo;
import com.withub.csp.entity.base.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name = "csp_app")
public class App extends BaseEntity {

    // 用于比较
    private Integer versionCode;

    // 用于展示
    private String versionName;

    // apk
    private String apk;

    private String apkFilename;

    private FileUploadInfo apkAttachment;

    //

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    //

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getApkFilename() {
        return apkFilename;
    }

    public void setApkFilename(String apkFilename) {
        this.apkFilename = apkFilename;
    }

    @Transient
    public FileUploadInfo getApkAttachment() {
        return apkAttachment;
    }

    public void setApkAttachment(FileUploadInfo apkAttachment) {
        this.apkAttachment = apkAttachment;
    }

}
