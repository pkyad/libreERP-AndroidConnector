package com.example.yadav.IM;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.libreerp.User;
import com.example.libreerp.UserMeta;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class Message  {
    String id, message, createdAt;
    UserMeta user;

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    private String attachment ;
    public boolean isMargin() {
        return margin;
    }

    public void setMargin(boolean margin) {
        this.margin = margin;
    }

    boolean margin ;


    Bitmap bm ;



    int location ;

    public Message() {
    }

    public Message(String id, String message, String createdAt, UserMeta user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
        this.location = 0 ;
        this.bm = null ;
        this.margin = false ;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserMeta getUser() {
        return user;
    }

    public void setUser(UserMeta user) {
        this.user = user;
    }
    public int isLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }



}
