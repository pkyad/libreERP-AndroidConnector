package com.example.yadav.IM;

/**
 * Created by guest on 11/7/17.
 */
public class NotificationMessage {
    private String message ;
    private String timestamp ;
    private int with_pk ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getWith_pk() {
        return with_pk;
    }

    public void setWith_pk(int with_pk) {
        this.with_pk = with_pk;
    }
}
