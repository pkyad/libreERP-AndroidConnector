package com.example.yadav.taskBoard;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TabHost;

public class Task implements Parcelable {

    private Integer pk;
    private String description;
    private Integer completion;
    private String created;
    private String dueDate;
    private String title;
    private Integer assignee;
    private Integer responsible;
    private int[] followers;
    private int[] files;
    private int pk_project;
    private boolean personal;
    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public void setPk_project(int pk_project) {
        this.pk_project = pk_project;
    }

    public void setFiles(int[] files) {
        this.files = files;
    }

    public void setFollowers(int[] followers) {
        this.followers = followers;
    }

    public void setCompletion(Integer completion) {
        this.completion = completion;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTitle(String title) {this.title = title;}

    public void setAssignee(Integer assignee){this.assignee = assignee;}

    public void setResponsible(Integer responsible) {
        this.responsible = responsible;
    }


    public Task(Integer pk) {
        this.pk = pk;
    }


    public Integer getPk() {
        return pk;
    }
    public String getDescription() { return description;}


    public Integer getCompletion() {
        return completion;
    }

    public int[] getFollowers() {
        return followers;
    }

    public int[] getFiles() {
        return files;
    }

    public String getCreated() {
        return created;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTitle() {return title;}

    public Integer getAssignee() {return assignee;}

    public Integer getResponsible(){return responsible;}

    public boolean getPersonal(){
        return personal;
    }
    public int getPk_project() {
        return pk_project;
    }

    public Task(Parcel parcel){
        this.pk = parcel.readInt();
        this.description = parcel.readString();
        this.completion = parcel.readInt();
        this.created = parcel.readString();
        this.dueDate = parcel.readString();
        this.title = parcel.readString();
        this.assignee = parcel.readInt();
        this.responsible = parcel.readInt();
        this.followers = parcel.createIntArray();
        this.files = parcel.createIntArray();
        this.pk_project = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.pk);
        parcel.writeString(this.description);
        parcel.writeInt(this.completion);
        parcel.writeString(this.created);
        parcel.writeString(this.dueDate);
        parcel.writeString(this.title);
        parcel.writeInt(this.assignee);
        parcel.writeInt(this.responsible);
        parcel.writeIntArray(this.followers);
        parcel.writeIntArray(this.files);
        parcel.writeInt(this.pk_project);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel parcel) {
            return new Task(parcel);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
