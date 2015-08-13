package com.withub.entity;

import com.withub.common.FileUploadInfo;

import javax.persistence.*;


@Entity
@Table(name = "csp_app")
public class App extends IdEntity{

    private String versionName;

    private Integer versionCode;

    private Integer deleteFlag;

    private String apk;
    private String apkFilename;
    private FileUploadInfo apkAttachment;


    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

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
