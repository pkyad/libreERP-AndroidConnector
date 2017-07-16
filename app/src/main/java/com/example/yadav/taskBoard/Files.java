package com.example.yadav.taskBoard;

/**
 * Created by cioc on 29/6/17.
 */

public class Files {
    private Integer filePk;
    private String fileLink;
    private int project_pk;
    private String  attachment;
    private String mediaType;
    private String name;
    private int postedUser;
    private String  fileCreated;
    private int pkTask;

    public Integer getFilePk() {
        return filePk;
    }

    public int getProject_pk() {
        return project_pk;
    }

    public int getPostedUser() {
        return postedUser;
    }

    public int getPkTask() {
        return pkTask;
    }

    public String  getAttachment() {
        return attachment;
    }

    public String getFileCreated() {
        return fileCreated;
    }

    public String getFileLink() {
        return fileLink;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getName() {
        return name;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public void setFileCreated(String fileCreated) {
        this.fileCreated = fileCreated;
    }

    public void setFilePk(Integer filePk) {
        this.filePk = filePk;
    }

    public void setProject_pk(int project_pk) {
        this.project_pk = project_pk;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPkTask(int pkTask) {
        this.pkTask = pkTask;
    }

    public void setPostedUser(int postedUser) {
        this.postedUser = postedUser;
    }

    public Files(Integer pk) {
        pkTask = pk;
    }

}

