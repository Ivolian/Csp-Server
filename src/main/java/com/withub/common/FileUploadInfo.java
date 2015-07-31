package com.withub.common;

public class FileUploadInfo {

    private String fileId;

    private Integer fileDelete;

    private String tempFileName;

    private String fileName;

    private String fileStatus;

    private String fileDescription;

    private Integer fileOrderNo;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getFileDelete() {
        return fileDelete;
    }

    public void setFileDelete(Integer fileDelete) {
        this.fileDelete = fileDelete;
    }

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public Integer getFileOrderNo() {
        return fileOrderNo;
    }

    public void setFileOrderNo(Integer fileOrderNo) {
        this.fileOrderNo = fileOrderNo;
    }
}
