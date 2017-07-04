package com.example.yadav.IM;

import java.util.Date;

/**
 * Created by cioc on 29/6/17.
 */

public class ChatRoomTable {
    private int pkMessage;
    private String message ;
    private String attachement ;
    private int pkOriginator;
    private String created ;
    private int total_unread;
    private int pkUser ;
    private int otherPk ;
    private boolean prvsMessage ; // true if the prvs is also for same user

    public boolean isPrvsMessage() {
        return prvsMessage;
    }

    public void setPrvsMessage(boolean prvsMessage) {
        this.prvsMessage = prvsMessage;
    }


    public int getOtherPk() {
        return otherPk;
    }

    public void setOtherPk(int otherPk) {
        this.otherPk = otherPk;
    }



    public int getPkMessage() {
        return pkMessage;
    }

    public void setPkMessage(int pkMessage) {
        this.pkMessage = pkMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachement() {
        return attachement;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public int getPkOriginator() {
        return pkOriginator;
    }

    public void setPkOriginator(int pkOriginator) {
        this.pkOriginator = pkOriginator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getTotal_unread() {
        return total_unread;
    }

    public void setRead(int total_unread) {
        this.total_unread = total_unread;
    }

    public int getPkUser() {
        return pkUser;
    }

    public void setPkUser(int pkUser) {
        this.pkUser = pkUser;
    }
}
