package com.example.yadav.IM;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.sax.StartElementListener;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cioc on 29/5/17.
 */

public class DBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "message.db";

    // chat Room table
    public static final String TABLE_CHATROOM = "chatRoomTable";
    public static final String COLUMN_CHATROOM_PK = "CHATROOMPK";
    public static final String COLUMN_WITH_PK = "PK";
    public static final String COLUMN_MESSAGE_PK = "MESSAGEPK";
    public static final String COLUMN_LAST_MESSAGE = "LASTMESSAGE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_UNREAD = "UNREAD";


    //    Message table variables
    public static final String TABLE_MESSAGE = "messageTable";

    public static final String COLUMN_PK_MESSAGE = "PKMESSAGE";
    public static final String COLUMN_CHATROOMID = "CHATROOMID";
    public static final String COLUMN_MESSAGE_MESSAGE = "MESSAGETEXT";
    public static final String COLUMN_MESSAGE_ATTACHMENT = "ATTACHMENT";
    public static final String COLUMN_MESSAGE_ORIGINATOR = "ORIGINATOR";
    public static final String COLUMN_MESSAGE_CREATED = "CREATED";
    public static final String COLUMN_MESSAGE_USER_PK = "USERPK";
    public static final String COLUMN_MESSAGE_WITHID = "WITHID";
    public static final String COLUMN_MESSAGE_READ_STATUS = "READ"; // 0 for Unread and 1 for read
    //     file upload


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryChat = "CREATE TABLE " + TABLE_CHATROOM + "(" + COLUMN_WITH_PK + " INTEGER,"
                + COLUMN_CHATROOM_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MESSAGE_PK + " INTEGER,"
                + COLUMN_LAST_MESSAGE + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_UNREAD + " INTEGER"
                + ");";


        String queryMessage = "CREATE TABLE " + TABLE_MESSAGE + "(" + COLUMN_PK_MESSAGE + " INTEGER,"
                + COLUMN_MESSAGE_MESSAGE + " TEXT,"
                + COLUMN_CHATROOMID + " INTEGER,"
                + COLUMN_MESSAGE_ATTACHMENT + " TEXT,"
                + COLUMN_MESSAGE_ORIGINATOR + " INTEGER,"
                + COLUMN_MESSAGE_CREATED + " TEXT,"
                + COLUMN_MESSAGE_USER_PK + "  INTEGER,"
                + COLUMN_MESSAGE_READ_STATUS + "  INTEGER,"
                + COLUMN_MESSAGE_WITHID + "  INTEGER"
                + ");";




        db.execSQL(queryChat);
        db.execSQL(queryMessage);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);

        onCreate(db);
    }


    // insert functions


    public long insertTableChatRoom(ChatRoomTable chatRoomTable) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_WITH_PK, chatRoomTable.getOtherPk());
        initialValues.put(COLUMN_MESSAGE_PK, chatRoomTable.getPkMessage());
        initialValues.put(COLUMN_LAST_MESSAGE, chatRoomTable.getMessage());
        initialValues.put(COLUMN_DATE, chatRoomTable.getCreated());
        initialValues.put(COLUMN_UNREAD, chatRoomTable.getTotal_unread());

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_CHATROOM, null, initialValues);
    }

    public long insertTableMessage(ChatRoomTable chatRoomTable) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK_MESSAGE, chatRoomTable.getPkMessage());
        initialValues.put(COLUMN_CHATROOMID, chatRoomTable.getChatRoomID());
        initialValues.put(COLUMN_MESSAGE_MESSAGE, chatRoomTable.getMessage());
        initialValues.put(COLUMN_MESSAGE_ATTACHMENT, chatRoomTable.getAttachement());
        initialValues.put(COLUMN_MESSAGE_ORIGINATOR, chatRoomTable.getPkOriginator());
        initialValues.put(COLUMN_MESSAGE_CREATED,chatRoomTable.getCreated());
        initialValues.put(COLUMN_MESSAGE_USER_PK, chatRoomTable.getPkUser());
        initialValues.put(COLUMN_MESSAGE_WITHID, chatRoomTable.getOtherPk());
        initialValues.put(COLUMN_MESSAGE_READ_STATUS, chatRoomTable.getIsReadStatus());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_MESSAGE, null, initialValues);
    }








    public long  updateMessageTableChatRoom(int with_pk , String lastMessage , int unread , String timestamp) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(COLUMN_LAST_MESSAGE,lastMessage);
        updateValues.put(COLUMN_UNREAD, unread);
        updateValues.put(COLUMN_DATE, timestamp);
        SQLiteDatabase db = getWritableDatabase();

        return db.update(TABLE_CHATROOM, updateValues, COLUMN_WITH_PK + " = ?",new String[]{String.valueOf(with_pk)});
    }

    public long  updateUnreadChatRoom(int with_pk ,int unread) {
        ContentValues updateValues = new ContentValues();

        updateValues.put(COLUMN_UNREAD, unread);

        SQLiteDatabase db = getWritableDatabase();

        return db.update(TABLE_CHATROOM, updateValues, COLUMN_WITH_PK + " = ?",new String[]{String.valueOf(with_pk)});
    }

    public long  updateAllUnReadMessage(int withid ) {
        ContentValues updateValues = new ContentValues();

        updateValues.put(COLUMN_MESSAGE_READ_STATUS, 1);

        SQLiteDatabase db = getWritableDatabase();

        return db.update(TABLE_MESSAGE, updateValues, COLUMN_MESSAGE_WITHID + " = ?",new String[]{String.valueOf(withid)});
    }
    // retrive data from table tasks





    public int getID(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CHATROOMPK")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("CHATROOMPK")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public int getIDFromWithPk(int withPk) {
        int chatRoomId = -1 ;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE " +COLUMN_WITH_PK +" = '" +Integer.toString(withPk).trim() + "'" ;

        Cursor c = db.rawQuery(query, null);
        int k =3 ;
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CHATROOMPK")) != null) {
               chatRoomId = (c.getInt(c.getColumnIndex("CHATROOMPK")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return chatRoomId;
    }
    public int getUnREADFromWithPk(int withPk) {
        int unRead = -1 ;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE " +COLUMN_WITH_PK +" = '" +Integer.toString(withPk).trim() + "'" ;

        Cursor c = db.rawQuery(query, null);
        int k =3 ;
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("UNREAD")) != null) {
                unRead = (c.getInt(c.getColumnIndex("UNREAD")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return unRead;
    }

    public int message_getOriginatorPK(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGE + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("ORIGINATOR")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("ORIGINATOR")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }
    public String message_getAttachment(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGE + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("ATTACHMENT")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("ATTACHMENT")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public int message_getMessagePK(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("PKMESSAGE")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("PKMESSAGE")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public String message_getMessage(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("MESSAGETEXT")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("MESSAGETEXT")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public String message_getDate(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CREATED")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("CREATED")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public int message_getUserPk(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGE + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("USERPK")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("USERPK")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }



    public int getTotalDBEntries_CHATROOM() {
        String countQuery = "SELECT  * FROM " + TABLE_CHATROOM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void deleteChatRoom(int withId){
        String query = "DELETE FROM " +  TABLE_CHATROOM + " WHERE " + COLUMN_WITH_PK + " = " + withId;
        getWritableDatabase().execSQL(query);

    }

    public int getWithPK(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("PK")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("PK")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public int getMessagePK(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("MESSAGEPK")) != null) {

                dbstring.add(c.getInt(c.getColumnIndex("MESSAGEPK")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }
    public ArrayList<ChatRoomTable> getData(int withId ) {
        ArrayList<ChatRoomTable> dbstring = new ArrayList<ChatRoomTable>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGE + " WHERE " + COLUMN_MESSAGE_WITHID + " = " +withId;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            ChatRoomTable chatRoomTable = new ChatRoomTable();
            if (c.getString(c.getColumnIndex("MESSAGETEXT")) != null) {

                chatRoomTable.setMessage(c.getString(c.getColumnIndex("MESSAGETEXT")));
            }

            if (c.getString(c.getColumnIndex("ATTACHMENT")) != null) {
                chatRoomTable.setAttachement(c.getString(c.getColumnIndex("ATTACHMENT")));
            }
            if (c.getString(c.getColumnIndex("ORIGINATOR")) != null) {
                chatRoomTable.setPkOriginator(c.getInt(c.getColumnIndex("ORIGINATOR")));
            }
            if (c.getString(c.getColumnIndex("CREATED")) != null) {
                chatRoomTable.setCreated(c.getString(c.getColumnIndex("CREATED")));
            }
            if (c.getString(c.getColumnIndex("USERPK")) != null) {
                chatRoomTable.setPkUser(c.getInt(c.getColumnIndex("USERPK")));
            }
            if (c.getString(c.getColumnIndex("WITHID")) != null) {
                chatRoomTable.setOtherPk(c.getInt(c.getColumnIndex("WITHID")));
            }
            if (c.getString(c.getColumnIndex("READ")) != null) {
                chatRoomTable.setIsReadStatus(c.getInt(c.getColumnIndex("READ")));
            }
            dbstring.add(chatRoomTable);
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring ;
    }

    public ArrayList<NotificationMessage> getAllUnReadMessages(int value) {
        ArrayList<NotificationMessage> dbstring = new ArrayList<NotificationMessage>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            NotificationMessage unreadMessage = new NotificationMessage();
            if (c.getString(c.getColumnIndex("LASTMESSAGE")) != null) {

                unreadMessage.setMessage(c.getString(c.getColumnIndex("LASTMESSAGE")));
            }


            if (c.getString(c.getColumnIndex("PK")) != null) {
                unreadMessage.setWith_pk(c.getInt(c.getColumnIndex("PK")));
            }
            if (c.getString(c.getColumnIndex("UNREAD")) != null) {
                unreadMessage.setUnreadChat(c.getInt(c.getColumnIndex("UNREAD")));
            }
            if (c.getString(c.getColumnIndex("DATE")) != null) {
                unreadMessage.setTimestamp(c.getString(c.getColumnIndex("DATE")));
            }

            dbstring.add(unreadMessage);
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring ;
    }

    public String getLastMessage(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("LASTMESSAGE")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("LASTMESSAGE")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public String getDate(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("DATE")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("DATE")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public int getUnRead(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("UNREAD")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("UNREAD")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }
    public int getTotalDBEntries_MESSAGE() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public String getTitle(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("TITLE")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("TITLE")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public String getCreated(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CREATED")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("CREATED")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public String getDueDate(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("DUEDATE")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("DUEDATE")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }

    public boolean CheckIfPKAlreadyInDBorNot(int fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_CHATROOM + " where " + COLUMN_WITH_PK + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean CheckIfMessagePKAlreadyInDBorNot(int fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_MESSAGE + " where " + COLUMN_PK_MESSAGE + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public String getDescription(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("DESCRIPTION")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("DESCRIPTION")));
            }
            c.moveToNext();
        }
        db.close(); c.close();
        return dbstring.get(position);
    }


    // retrive data from table comments


}























    // retrive data from table subtask

















    // retrive data from table users

