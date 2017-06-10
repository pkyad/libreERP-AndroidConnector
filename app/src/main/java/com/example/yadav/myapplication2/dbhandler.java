package com.example.yadav.myapplication2;

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

public class dbhandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "taskvalue.db";
    public static final String TABLE_TASKVALUE = "taskvalue";
    public static final String COLUMN_PK = "PK";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_COMPLETION = "COMPLETION";
    public static final String COLUMN_FOLLOWERS = "FOLLOWERS";
    public static final String COLUMN_USER = "USER";
    public static final String COLUMN_CREATED = "CREATED";
    public static final String COLUMN_DUEDATE = "DUEDATE";
    public static final String  COLUMN_ASSIGNEE = "ASSIGNEE";
    public static final String COLUMN_RESPONSIBLE = "RESPONSIBLE";

//    Subtask table variables
    public static final String TABLE_SUBTASK = "subTask";
    public static final String COLUMN_PK_SUBTASK = "PK_SUBTASK";
    public static final String COLUMN_TITLE_SUBTASK = "TITLE_SUBTASK";
    public static final String COLUMN_STATUS_SUBTASK = "STATUS_SUBTASK";

//     comments and commit table
    public static final String TABLE_COMMENTS = "comment";
    public static final String COLUMN_PK_COMMENT = "PK_COMMENT";
    public static final String COLUMN_COMMENT_CREATED = "COMMENT_CREATED";
    public static final String COLUMN_CATEGORY = "CATEGORY";
    public static final String COLUMN_TEXT = "TEXT";
    public static final String COLUMN_COMMIT_PK = "COMMIT_PK";
    public static final String COLUMN_COMMIT_MESSAGE = "COMMIT_MESSAGE";


//       users table
    public static final String TABLE_USERS = "USERS";
    public static final String COLUMN_USERS_PK = "PK_USERS";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    public static final String COLUMN_LAST_NAME = "LAST_NAME";
    public static final String COLUMN_DESIGNATION = "DESIGNATION";
    public static final String COLUMN_SOCIAL = "SOCIAL";
    public static final String COLUMN_DISPLAY_PICTURE = "DISPLAY_PICTURE";

    public dbhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_TASKVALUE + "(" + COLUMN_PK + " INTEGER,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + "  TEXT,"
                + COLUMN_COMPLETION + " INTEGER,"
                + COLUMN_FOLLOWERS + " TEXT,"
                + COLUMN_USER + " TEXT,"
                + COLUMN_CREATED + " TEXT,"
                + COLUMN_DUEDATE + " TEXT,"
                + COLUMN_ASSIGNEE + " INTEGER,"
                + COLUMN_RESPONSIBLE + " INTEGER"
                + ");";


        String querySubTask = "CREATE TABLE " + TABLE_SUBTASK + "(" + COLUMN_PK_SUBTASK + " INTEGER,"
                + COLUMN_PK + " INTEGER,"
                + COLUMN_TITLE_SUBTASK + " TEXT,"
                + COLUMN_STATUS_SUBTASK + "  TEXT"
                + ");";

        String queryComment =  "CREATE TABLE " + TABLE_COMMENTS + "(" + COLUMN_PK_COMMENT + " INTEGER,"
                + COLUMN_PK + " INTEGER,"
                + COLUMN_COMMENT_CREATED + " TEXT,"
                + COLUMN_CATEGORY + "  TEXT,"
                + COLUMN_TEXT + " TEXT,"
                + COLUMN_COMMIT_PK + " INTEGER,"
                + COLUMN_COMMIT_MESSAGE + " TEXT"
                + ");";

        String queryusers =  "CREATE TABLE " + TABLE_USERS + "(" + COLUMN_USERS_PK + " INTEGER,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_FIRST_NAME + "  TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_DESIGNATION + " INTEGER,"
                + COLUMN_SOCIAL + " INTEGER,"
                + COLUMN_DISPLAY_PICTURE + " TEXT "
                + ");";
        db.execSQL(querySubTask);
        db.execSQL(query);
        db.execSQL(queryComment);
        db.execSQL(queryusers);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKVALUE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    // insert functions


    public long insertTableMain(Task task) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK,task.getPk());
        initialValues.put(COLUMN_DESCRIPTION,task.getDescription());
        initialValues.put(COLUMN_COMPLETION, task.getCompletion());
        initialValues.put(COLUMN_CREATED,task.getCreated());
        initialValues.put(COLUMN_DUEDATE,task.getDueDate());
        initialValues.put(COLUMN_TITLE,task.getTitle());
        initialValues.put(COLUMN_ASSIGNEE,task.getAssignee());
        initialValues.put(COLUMN_RESPONSIBLE,task.getResponsible());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_TASKVALUE, null, initialValues);
    }

    public long insertTableSubtask(SubTask subTask){
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK_SUBTASK,subTask.getPk());
        initialValues.put(COLUMN_PK,subTask.getPkTask());
        initialValues.put(COLUMN_TITLE_SUBTASK,subTask.getTitle());
        initialValues.put(COLUMN_STATUS_SUBTASK, subTask.getStatus());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_SUBTASK, null, initialValues);
    }

    public long insertTableComment(Comment comment){
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK,comment.getPkTask());
        initialValues.put(COLUMN_PK_COMMENT,comment.getPkComment());
        initialValues.put(COLUMN_COMMENT_CREATED,comment.getCreated());
        initialValues.put(COLUMN_CATEGORY, comment.getCategory());
        initialValues.put(COLUMN_TEXT, comment.getText());
        initialValues.put(COLUMN_COMMIT_PK, comment.getCommitPK());
        initialValues.put(COLUMN_COMMIT_MESSAGE, comment.getCommitMessage());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_COMMENTS, null, initialValues);
    }

    public long insertTableUsers(Users users){
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_USERS_PK,users.getPkUsers());
        initialValues.put(COLUMN_USERNAME,users.getUsername());
        initialValues.put(COLUMN_FIRST_NAME, users.getFirstName());
        initialValues.put(COLUMN_LAST_NAME, users.getLastName());
        initialValues.put(COLUMN_DESIGNATION, users.getDesignation());
        initialValues.put(COLUMN_SOCIAL, users.getSocial());
        initialValues.put(COLUMN_DISPLAY_PICTURE, users.getDisplayPicture());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_USERS, null, initialValues);
    }

    // retrive data from table tasks


    public String databasetostring(int position){
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("DESCRIPTION"))!=null){
                dbstring.add(c.getString(c.getColumnIndex("DESCRIPTION")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public int getcompletion(int position){
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("COMPLETION"))!=null){
                dbstring.add(c.getInt(c.getColumnIndex("COMPLETION")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public int getPK(int position){
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("PK"))!=null){
                dbstring.add(c.getInt(c.getColumnIndex("PK")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public int getTotalDBEntries_TASKVALUE() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKVALUE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public String getTitle(int position){
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("TITLE"))!=null){
                dbstring.add(c.getString(c.getColumnIndex("TITLE")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public String getCreated(int position){
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("CREATED"))!=null){
                dbstring.add(c.getString(c.getColumnIndex("CREATED")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }
    public String getDueDate(int position){
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("DUEDATE"))!=null){
                dbstring.add(c.getString(c.getColumnIndex("DUEDATE")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public boolean CheckIfPKAlreadyInDBorNot(int fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_TASKVALUE + " where " + COLUMN_PK + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public int getAssignee(int pk_task){
        int dbstring = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("ASSIGNEE"))!=null){
                dbstring = c.getInt(c.getColumnIndex("ASSIGNEE"));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }
    public int getResponsible(int pk_task){
        int dbstring = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("RESPONSIBLE"))!=null){
                dbstring = (c.getInt(c.getColumnIndex("RESPONSIBLE")));

            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }





















    // retrive data from table comments

    public int getTotalDBEntries_COMMENT(int pk_task) {
        String countQuery = "SELECT  * FROM " + TABLE_COMMENTS + " WHERE " +  COLUMN_PK + " = " + pk_task;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public String getCategory(int position, int pk_task){
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("CATEGORY"))!=null){
                dbstring.add(c.getString(c.getColumnIndex("CATEGORY")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }
    public String getCreatedMessage(int position,int pk_task){
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("COMMENT_CREATED"))!=null){
                dbstring.add(c.getString(c.getColumnIndex("COMMENT_CREATED")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }
    public String getMessage(int pk_comment){
//        ArrayList<String> dbstring = new ArrayList<String>();
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
//        dbstring = c.getString(c.getColumnIndex("TEXT"));
//        System.out.println("sdfgsdgsdgsdgsd======" + dbstring);
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("TEXT"))!=null){
                dbstring = c.getString(c.getColumnIndex("TEXT"));
                System.out.println("sdfgsdgsdgsdgsd======" + dbstring);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public String getCommitMessage(int pk_comment){
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_COMMIT_MESSAGE + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("COMMIT_MESSAGE"))!=null){
                dbstring = c.getString(c.getColumnIndex("COMMIT_MESSAGE"));
                System.out.println("hololololo======" + dbstring);
            }
            c.moveToNext();
        }
        db.close();
        System.out.println("hololololo1======" + dbstring);
        return dbstring;
    }

    public int getCommitPK(int position){
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE 1";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("COMMIT_PK"))!=null){
                dbstring.add(c.getInt(c.getColumnIndex("COMMIT_PK")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public int getCommentPK(int position, int pk_task){
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("PK_COMMENT"))!=null){
                dbstring.add(c.getInt(c.getColumnIndex("PK_COMMENT")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public boolean CheckIfCOMMENT_PKAlreadyInDBorNot(int fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_COMMENTS + " where " + COLUMN_PK_COMMENT + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }






















    // retrive data from table subtask

    public List<SubTask> getAllContacts(int pkTask) {
        List<SubTask> subTaskList = new ArrayList<SubTask>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUBTASK + " WHERE " + COLUMN_PK + " = " +  pkTask;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SubTask subTask = new SubTask(pkTask);
                subTask.setPk(Integer.parseInt(cursor.getString(0)));
                subTask.setTitle(cursor.getString(2));
                subTask.setStatus(cursor.getString(3));

                String name = cursor.getString(2) +"\n"+ cursor.getString(3);
                TaskCardFragment.arraySubTask.add(name);
                // Adding contact to list
                subTaskList.add(subTask);
            } while (cursor.moveToNext());
        }

        // return contact list
        return subTaskList;
    }

    public boolean CheckIfSUB_PKAlreadyInDBorNot(int fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_SUBTASK + " where " + COLUMN_PK_SUBTASK + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

















    // retrive data from table users

    public String getUser(int pk){
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_PK + " = " + pk;

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("FIRST_NAME"))!=null || c.getString(c.getColumnIndex("LAST_NAME"))!=null){
                dbstring = c.getString(c.getColumnIndex("FIRST_NAME")) + " " +  c.getString(c.getColumnIndex("LAST_NAME"));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
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

}