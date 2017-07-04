package com.example.yadav.IM;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class ChatRoom implements Serializable {
    String id, name, lastMessage, timestamp;
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

    public ChatRoom(String id, String lastMessage, String timestamp, int unreadCount ) {
        this.id = id;

        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
