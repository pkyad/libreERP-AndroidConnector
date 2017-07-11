package com.example.yadav.taskBoard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pradeep on 3/9/2017.
 */

public class Projects implements Parcelable{

    private Integer pk;
    private String description;
    private String created;
    private String dueDate;
    private String title;
    private Integer user;
    private int[] team;
    private int[] files;
    private int repoCount;

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    public void setRepoCount(int repoCount) {
        this.repoCount = repoCount;
    }

    public void setFiles(int[] files) {
        this.files = files;
    }

    public void setTeam(int[] team) {
        this.team = team;
    }


    public void setCreated(String created) {
        this.created = created;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTitle(String title) {this.title = title;}

    public void setUser(Integer user){this.user = user;}



    public Projects(Integer pk) {
        this.pk = pk;
    }


    public Integer getPk() {
        return pk;
    }
    public String getDescription() { return description;}



    public int[] getTeam() {
        return team;
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

    public Integer getUser() {return user;}

    public int getRepoCount() {
        return repoCount;
    }

    public Projects(Parcel parcel){
        this.pk = parcel.readInt();
        this.description = parcel.readString();
        this.created = parcel.readString();
        this.dueDate = parcel.readString();
        this.title = parcel.readString();
        this.user = parcel.readInt();
        this.team = parcel.createIntArray();
        this.files = parcel.createIntArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.pk);
        parcel.writeString(this.description);
        parcel.writeString(this.created);
        parcel.writeString(this.dueDate);
        parcel.writeString(this.title);
        parcel.writeInt(this.user);
        parcel.writeIntArray(this.team);
        parcel.writeIntArray(this.files);
    }

    public static final Creator<Projects> CREATOR = new Creator<Projects>() {
        @Override
        public Projects createFromParcel(Parcel parcel) {
            return new Projects(parcel);
        }

        @Override
        public Projects[] newArray(int size) {
            return new Projects[size];
        }
    };
}
