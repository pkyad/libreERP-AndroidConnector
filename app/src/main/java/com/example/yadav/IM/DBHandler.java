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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cioc on 29/5/17.
 */

public class DBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "message.db";

    // chat Room table
    public static final String TABLE_CHATROOM = "chatRoom";
    //public static final String COLUMN_CHATROOM_PK = "PK";
    public static final String COLUMN_WITH_PK = "PK";
    public static final String COLUMN_MESSAGE_PK = "MEESAGEPK";
    public static final String COLUMN_LAST_MESSAGE = "LASTMEESAGE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_UNREAD = "UNREAD";


    //    Message table variables
    public static final String TABLE_MESSAGE = "subTask";
    public static final String COLUMN_PK_MESSAGE = "PKMESSAGE";
    public static final String COLUMN_MESSAGE_MESSAGE = "MEESAGE";
    public static final String COLUMN_MESSAGE_ATTACHMENT = "ATTACHMENT";
    public static final String COLUMN_MESSAGE_ORIGINATOR = "ORIGINATOR";
    public static final String COLUMN_MESSAGE_CREATED = "CREATED";
    public static final String COLUMN_MESSAGE_USER_PK = "USERPK";

    //     comments and commit table
    public static final String TABLE_COMMENTS = "comment";
    public static final String COLUMN_PK_COMMENT = "PK_COMMENT";
    public static final String COLUMN_COMMENT_CREATED = "COMMENT_CREATED";
    public static final String COLUMN_CATEGORY = "CATEGORY";
    public static final String COLUMN_TEXT = "TEXT";
    public static final String COLUMN_COMMIT_PK = "COMMIT_PK";
    public static final String COLUMN_COMMIT_MESSAGE = "COMMIT_MESSAGE";
    public static final String COLUMN_POST_USER = "POST_USER";


    //       users table
    public static final String TABLE_USERS = "USERS";
    public static final String COLUMN_USERS_PK = "PK_USERS";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    public static final String COLUMN_LAST_NAME = "LAST_NAME";
    public static final String COLUMN_DESIGNATION = "DESIGNATION";
    public static final String COLUMN_SOCIAL = "SOCIAL";
    public static final String COLUMN_DISPLAY_PICTURE = "DISPLAY_PICTURE";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryChat = "CREATE TABLE " + TABLE_CHATROOM + "(" + COLUMN_WITH_PK + " INTEGER,"
                + COLUMN_MESSAGE_PK + " INTEGER,"
                + COLUMN_LAST_MESSAGE + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_UNREAD + " INTEGER,"

                + ");";


        String queryMessage = "CREATE TABLE " + TABLE_MESSAGE + "(" + COLUMN_PK_MESSAGE + " INTEGER,"
                + COLUMN_MESSAGE_ATTACHMENT + " TEXT,"
                + COLUMN_MESSAGE_ORIGINATOR + " INTEGER,"
                + COLUMN_MESSAGE_CREATED + " TIMESTAMP,"
                + COLUMN_MESSAGE_USER_PK + "  INTEGER"
                + ");";


        db.execSQL(queryChat);
        db.execSQL(queryMessage);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    // insert functions


    public long insertTableChatRoom(ChatRoomTable chatRoomTable) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_WITH_PK, chatRoomTable.getOtherPk());
        initialValues.put(COLUMN_MESSAGE_PK, chatRoomTable.getPkMessage());
        initialValues.put(COLUMN_LAST_MESSAGE, chatRoomTable.getMessage());
        initialValues.put(COLUMN_DATE, String.valueOf(chatRoomTable.getCreated()));
        initialValues.put(COLUMN_UNREAD, chatRoomTable.getTotal_unread());

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_CHATROOM, null, initialValues);
    }

    public long insertTableMessage(ChatRoomTable chatRoomTable) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK_MESSAGE, chatRoomTable.getPkMessage());
        initialValues.put(COLUMN_MESSAGE_MESSAGE, chatRoomTable.getMessage());
        initialValues.put(COLUMN_MESSAGE_ATTACHMENT, chatRoomTable.getAttachement());
        initialValues.put(COLUMN_MESSAGE_ORIGINATOR, String.valueOf(chatRoomTable.getCreated()));
        initialValues.put(COLUMN_MESSAGE_USER_PK, chatRoomTable.getPkUser());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_MESSAGE, null, initialValues);
    }


    // retrive data from table tasks


    public String databasetostring(int position) {
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
        db.close();
        return dbstring.get(position);
    }

    public int getcompletion(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMPLETION")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("COMPLETION")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
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
        db.close();
        return dbstring.get(position);
    }

    public int getMessagePK(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("MEESAGEPK")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("MEESAGEPK")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public String getLastMessage(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CHATROOM + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("LASTMEESAGE")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("LASTMEESAGE")));
            }
            c.moveToNext();
        }
        db.close();
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
        db.close();
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
        db.close();
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
        db.close();
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
        db.close();
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
        db.close();
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
        String Query = "Select * from " + TABLE_CHATROOM + " where " + COLUMN_MESSAGE_PK + " = " + fieldValue;
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
        db.close();
        return dbstring.get(position);
    }


    // retrive data from table comments

    public String getMessage(int pk_comment) {
//        ArrayList<String> dbstring = new ArrayList<String>();
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("TEXT")) != null) {
                dbstring = c.getString(c.getColumnIndex("TEXT"));

            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public String getCommitMessage(int pk_comment) {
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_COMMIT_MESSAGE + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMIT_MESSAGE")) != null) {
                dbstring = c.getString(c.getColumnIndex("COMMIT_MESSAGE"));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public int getCommitPK(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMIT_PK")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("COMMIT_PK")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }
}























    // retrive data from table subtask

















    // retrive data from table users

