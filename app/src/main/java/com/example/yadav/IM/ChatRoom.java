package com.example.yadav.IM;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class ChatRoom implements Serializable {
    int id ;
    String name, lastMessage, timestamp, username;
    int unreadCount;
    Bitmap DP ;
    int with_pk;

    public int getWith_pk() {
        return with_pk;
    }

    public void setWith_pk(int with_pk) {
        this.with_pk = with_pk;
    }

    public ChatRoom() {
    }

    public ChatRoom( String lastMessage, String timestamp, int unreadCount ) {

        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getDP() { return DP;}

    public void setDP(Bitmap bp) { this.DP = bp ;}


    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }



    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
