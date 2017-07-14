package com.example.yadav.IM;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by cioc on 29/6/17.
 */

public class ChatRoomTable implements Parcelable{
    public int getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(int chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    private int chatRoomID;
    private int pkMessage;
    private String message ;
    private String attachement ;
    private int pkOriginator;
    private String created ;
    private int total_unread;
    private int pkUser ;
    private int otherPk ;

    private int sender_change;


    public int getIsReadStatus() {
        return isReadStatus;
    }

    public void setIsReadStatus(int isReadStatus) {
        this.isReadStatus = isReadStatus;
    }

    private int isReadStatus;

    public ChatRoomTable(){
        this.sender_change = 0 ;
    }

    public int isSender_change() {
        return sender_change;
    }

    public void setSender_change(int sender_change) {
        this.sender_change = sender_change;
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

    public void setTotal_UnRead(int total_unread) {
        this.total_unread = total_unread;
    }

    public int getPkUser() {
        return pkUser;
    }

    public void setPkUser(int pkUser) {
        this.pkUser = pkUser;
    }


    public ChatRoomTable(Parcel parcel){
        this.chatRoomID = parcel.readInt();
        this.message = parcel.readString();
        this.pkMessage = parcel.readInt();
        this.created = parcel.readString();
        this.attachement = parcel.readString();
        this.pkUser = parcel.readInt();
        this.pkOriginator = parcel.readInt();
        this.total_unread = parcel.readInt();
        this.otherPk = parcel.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt( this.chatRoomID);
        parcel.writeString(this.message);
        parcel.writeInt(this.pkMessage );
        parcel.writeString(this.created);
        parcel.writeString(this.attachement);
        parcel.writeInt(this.pkUser);
        parcel.writeInt( this.pkOriginator);
        parcel.writeInt(this.total_unread );

        parcel.writeInt(this.otherPk);
    }

    public static final Creator<ChatRoomTable> CREATOR = new Creator<ChatRoomTable>() {
        @Override
        public ChatRoomTable createFromParcel(Parcel parcel) {
            return new ChatRoomTable(parcel);
        }

        @Override
        public ChatRoomTable[] newArray(int size) {
            return new ChatRoomTable[size];
        }
    };
}
