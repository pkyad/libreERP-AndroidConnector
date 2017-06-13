package com.example.libreerp;

/**
 * Created by yadav on 13/6/17.
 */

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
    public static final String DATABASE_NAME = "taskvalue.db";
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

        String queryusers =  "CREATE TABLE " + TABLE_USERS + "(" + COLUMN_USERS_PK + " INTEGER,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_FIRST_NAME + "  TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_DESIGNATION + " INTEGER,"
                + COLUMN_SOCIAL + " INTEGER,"
                + COLUMN_DISPLAY_PICTURE + " TEXT "
                + ");";

        db.execSQL(queryusers);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    public long insertTableUsers(UserMeta userMeta){
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_USERS_PK,userMeta.getPkUser());
        initialValues.put(COLUMN_USERNAME,userMeta.getUsername());
        initialValues.put(COLUMN_FIRST_NAME, userMeta.getFirstName());
        initialValues.put(COLUMN_LAST_NAME, userMeta.getLastName());
        initialValues.put(COLUMN_DESIGNATION, userMeta.getDesignation());
        initialValues.put(COLUMN_SOCIAL, userMeta.getSocial());
        initialValues.put(COLUMN_DISPLAY_PICTURE, userMeta.getDisplayPictureLink());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_USERS, null, initialValues);
    }

    // retrive data from table users
    public void cleanUsers(){
        String query = "DELETE FROM "+ TABLE_USERS;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    public UserMeta getUser(int userPk){
        UserMeta userMeta = new UserMeta(userPk);
        String dbstring = new String();
        int tablevalue = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_PK + " = " + userPk;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("USERNAME"))!=null){
                dbstring = c.getString(c.getColumnIndex("USERNAME"));
                userMeta.setUsername(dbstring);
            }
            if(c.getString(c.getColumnIndex("FIRST_NAME"))!=null){
                dbstring = c.getString(c.getColumnIndex("FIRST_NAME"));
                userMeta.setFirstName(dbstring);
            }
            if(c.getString(c.getColumnIndex("LAST_NAME"))!=null){
                dbstring = c.getString(c.getColumnIndex("LAST_NAME"));
                userMeta.setLastName(dbstring);
            }
            if(c.getString(c.getColumnIndex("DESIGNATION"))!=null){
                tablevalue = c.getInt(c.getColumnIndex("DESIGNATION"));
                userMeta.setDesignation(tablevalue);
            }
            if(c.getString(c.getColumnIndex("SOCIAL"))!=null){
                tablevalue = c.getInt(c.getColumnIndex("SOCIAL"));
                userMeta.setSocial(tablevalue);
            }
            if(c.getString(c.getColumnIndex("DISPLAY_PICTURE"))!=null){
                dbstring = c.getString(c.getColumnIndex("DISPLAY_PICTURE"));
                userMeta.setProfilePictureLink(dbstring);
            }
            if(c.getString(c.getColumnIndex("PK_USERS"))!=null){
                tablevalue = c.getInt(c.getColumnIndex("PK_USERS"));
                userMeta.setPkUsers(tablevalue);
            }
            c.moveToNext();
        }
        db.close();
        return userMeta;
    }
    public boolean CheckIfUserIsInDatabase(int userPK) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_USERS + " where " + COLUMN_USERS_PK + " = " + userPK;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public int getTotalTableEntries_USERS() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }



}