package com.example.yadav.taskBoard;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "taskboard.db";
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
    public static final String COLUMN_POST_USER = "POST_USER";
    public static final String COLUMN_POST_USER_PK = "POST_USER_PK";
    public static final String COLUMN_COMMIT_DATE = "COMMIT_DATE";
    public static final String COLUMN_COMMIT_CODE = "COMMIT_CODE";
    public static final String COLUMN_COMMIT_BRANCH = "COMMIT_BRANCH";



    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
                + COLUMN_COMMIT_MESSAGE + " TEXT,"
                + COLUMN_POST_USER + " TEXT,"
                + COLUMN_POST_USER_PK + " INTEGER,"
                + COLUMN_COMMIT_DATE + " TEXT,"
                + COLUMN_COMMIT_CODE + " TEXT,"
                + COLUMN_COMMIT_BRANCH + " TEXT"
                + ");";
        db.execSQL(query);
        db.execSQL(querySubTask);

        db.execSQL(queryComment);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKVALUE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }

    public void cleanTasks(){
        String query = "DELETE FROM "+ TABLE_TASKVALUE;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
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
        String followersMergerd = new String();
        for (int i = 0; i < task.getFollowers().length; i++) {
            int[] followers = task.getFollowers();
            followersMergerd = followersMergerd + "," + followers[i];
        }
        System.out.println("followersmerged = "+followersMergerd);
        initialValues.put(COLUMN_FOLLOWERS,followersMergerd);
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
        initialValues.put(COLUMN_POST_USER,comment.getUser());
        initialValues.put(COLUMN_POST_USER_PK,comment.getUserPK());
        initialValues.put(COLUMN_COMMIT_DATE,comment.getCommitDate());
        initialValues.put(COLUMN_COMMIT_CODE,comment.getCommitCode());
        initialValues.put(COLUMN_COMMIT_BRANCH,comment.getCommitBranch());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_COMMENTS, null, initialValues);
    }



    // retrive data from table tasks




    public void cleanUsers(){
        String query = "DELETE FROM "+ TABLE_TASKVALUE;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

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

    public Date getCreated(int position){
        ArrayList<Date> dbstring = new ArrayList<Date>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("CREATED"))!=null){
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    date = formatter.parse(c.getString(c.getColumnIndex("CREATED")));
                }
                catch (ParseException e){
                    System.out.println("error while parsing");
                }
                dbstring.add(date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }
    public Date getDueDate(int position){
        ArrayList<Date> dbstring = new ArrayList<Date>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("DUEDATE"))!=null){
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    date = formatter.parse(c.getString(c.getColumnIndex("DUEDATE")));
                }
                catch (ParseException e){
                    System.out.println("error while parsing");
                }
                dbstring.add(date);
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

    public String getDescription(int position){
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

    public List<Integer> getFollowers(int position){
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("FOLLOWERS"))!=null){
                dbstring.add(c.getString(c.getColumnIndex("FOLLOWERS")));
            }
            c.moveToNext();
        }
        db.close();
        dbstring.get(position);
        String [] followers = dbstring.get(position).split(",");
        List<Integer> followersList = new ArrayList<>();

//        System.out.println("followersp[0]"+followers[1]);
        for (int i=0;i<followers.length;i++){
            if(!followers[i].equals(""))
                followersList.add(Integer.parseInt(followers[i]));
        }
        return followersList;
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
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("TEXT"))!=null){
                dbstring = c.getString(c.getColumnIndex("TEXT"));

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
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public String getCommitBranch(int pk_comment){
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_COMMIT_BRANCH + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("COMMIT_BRANCH"))!=null){
                dbstring = c.getString(c.getColumnIndex("COMMIT_BRANCH"));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public Date getCommitDate(int pk_comment){
        Date dbstring = new Date();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("COMMIT_DATE"))!=null){
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    c.getColumnIndex("COMMIT_DATE");
                    date = formatter.parse(c.getString(c.getColumnIndex("COMMIT_DATE")));
                }
                catch (ParseException e){
                    System.out.println("error while parsing");
                }
                dbstring =(date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }
    public Date getCommentDate(int pk_comment){
        Date dbstring = new Date();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_COMMENT_CREATED + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_COMMENT_CREATED))!=null){
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    date = formatter.parse(c.getString(c.getColumnIndex(COLUMN_COMMENT_CREATED)));
                }
                catch (ParseException e){
                    System.out.println("error while parsing");
                }
                dbstring =(date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }


    public String getCommitCode(int pk_comment){
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_COMMIT_CODE + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("COMMIT_CODE"))!=null){
                dbstring = c.getString(c.getColumnIndex("COMMIT_CODE"));
            }
            c.moveToNext();
        }
        dbstring = dbstring.substring(30);
        db.close();
        return dbstring;
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


    public String getPostUser(int pk_comment){
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("POST_USER"))!=null){
                dbstring = c.getString(c.getColumnIndex("POST_USER"));
            }
            c.moveToNext();
        }
        db.close();

        return dbstring;
    }

    public int getPostUserPK(int pk_comment){
        int dbstring =0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("POST_USER_PK"))!=null){
                dbstring = c.getInt(c.getColumnIndex("POST_USER_PK"));
            }
            c.moveToNext();
        }
        db.close();

        return dbstring;
    }










    // retrive data from table subtask

    public List<SubTask> getAllSubtasks(int pkTask) {
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
                TaskCardActivity.arraySubTask.add(name);
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
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}