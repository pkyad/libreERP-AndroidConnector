package com.example.yadav.taskBoard;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "taskboard.db";
    private static final String TABLE_TASKVALUE = "taskvalue";
    private static final String COLUMN_PK = "PK";
    private static final String COLUMN_TITLE = "TITLE";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_COMPLETION = "COMPLETION";
    private static final String COLUMN_FOLLOWERS = "FOLLOWERS";
    private static final String COLUMN_USER = "USER";
    private static final String COLUMN_CREATED = "CREATED";
    private static final String COLUMN_DUEDATE = "DUEDATE";
    private static final String COLUMN_ASSIGNEE = "ASSIGNEE";
    private static final String COLUMN_RESPONSIBLE = "RESPONSIBLE";
    private static final String COLUMN_FILES = "FILES";

    //    Subtask table variables
    private static final String TABLE_SUBTASK = "subTask";
    private static final String COLUMN_PK_SUBTASK = "PK_SUBTASK";
    private static final String COLUMN_TITLE_SUBTASK = "TITLE_SUBTASK";
    private static final String COLUMN_STATUS_SUBTASK = "STATUS_SUBTASK";

    //     comments and commit table
    private static final String TABLE_COMMENTS = "comment";
    private static final String COLUMN_PK_COMMENT = "PK_COMMENT";
    private static final String COLUMN_COMMENT_CREATED = "COMMENT_CREATED";
    private static final String COLUMN_CATEGORY = "CATEGORY";
    private static final String COLUMN_TEXT = "TEXT";
    private static final String COLUMN_COMMIT_PK = "COMMIT_PK";
    private static final String COLUMN_COMMIT_MESSAGE = "COMMIT_MESSAGE";
    private static final String COLUMN_POST_USER = "POST_USER";
    private static final String COLUMN_POST_USER_PK = "POST_USER_PK";
    private static final String COLUMN_COMMIT_DATE = "COMMIT_DATE";
    private static final String COLUMN_COMMIT_CODE = "COMMIT_CODE";
    private static final String COLUMN_COMMIT_BRANCH = "COMMIT_BRANCH";

    //      files database
    private static final String TABLE_FILES = "files";
    private static final String COLUMN_FILE_PK = "FILE_PK";
    private static final String COLUMN_PK_TASK = "PK_TASK";
    private static final String COLUMN_FILE_LINK = "FILE_LINK";
    private static final String COLUMN_ATTACHMENT = "ATTACHMENT";
    private static final String COLUMN_MEDIA_TYPE = "MEDIA_TYPE";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_POSTED_USER = "POSTED_USER";
    private static final String COLUMN_FILE_CREATED = "FILE_CREATED";


    //      table projects
    private static final String TABLE_PROJECTS = "projects";
    private static final String COLUMN_PK_PROJECT = "PK_PROJECT";
    private static final String COLUMN_TITLE_PROJECT = "TITLE_PROJECT";
    private static final String COLUMN_DESCRIPTION_PROJECT = "DESCRIPTION_PROJECT";
    private static final String COLUMN_TEAM_PROJECT = "TEAM_PROJECT";
    private static final String COLUMN_USER_PROJECT = "USER_PROJECT";
    private static final String COLUMN_CREATED_PROJECT = "CREATED_PROJECT";
    private static final String COLUMN_DUEDATE_PROJECT = "DUEDATE_PROJECT";
    private static final String COLUMN_FILES_PROJECT = "FILES_PROJECT";

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
                + COLUMN_RESPONSIBLE + " INTEGER,"
                + COLUMN_FILES + " TEXT"
                + ");";


        String querySubTask = "CREATE TABLE " + TABLE_SUBTASK + "(" + COLUMN_PK_SUBTASK + " INTEGER,"
                + COLUMN_PK + " INTEGER,"
                + COLUMN_TITLE_SUBTASK + " TEXT,"
                + COLUMN_STATUS_SUBTASK + "  TEXT"
                + ");";

        String queryComment = "CREATE TABLE " + TABLE_COMMENTS + "(" + COLUMN_PK_COMMENT + " INTEGER,"
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
        String queryFile = "CREATE TABLE " + TABLE_FILES + "(" + COLUMN_FILE_PK + " INTEGER,"
                + COLUMN_PK_TASK + " INTEGER,"
                + COLUMN_FILE_LINK + " TEXT,"
                + COLUMN_ATTACHMENT + "  TEXT,"
                + COLUMN_MEDIA_TYPE + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_POSTED_USER + " INTEGER,"
                + COLUMN_FILE_CREATED + " TEXT"
                + ");";

        String queryProject = "CREATE TABLE " + TABLE_PROJECTS + "(" + COLUMN_PK_PROJECT + " INTEGER,"
                + COLUMN_TITLE_PROJECT + " TEXT,"
                + COLUMN_DESCRIPTION_PROJECT + "  TEXT,"
                + COLUMN_TEAM_PROJECT + " TEXT,"
                + COLUMN_USER_PROJECT + " TEXT,"
                + COLUMN_CREATED_PROJECT + " TEXT,"
                + COLUMN_DUEDATE_PROJECT + " TEXT,"
                + COLUMN_FILES_PROJECT + " TEXT"
                + ");";

        db.execSQL(query);
        db.execSQL(querySubTask);
        db.execSQL(queryFile);
        db.execSQL(queryComment);
        db.execSQL(queryProject);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKVALUE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        onCreate(db);
    }

    public void cleanTasks() {
        String query = "DELETE FROM " + TABLE_TASKVALUE;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }
    // insert functions


    public long insertTableMain(Task task) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK, task.getPk());
        initialValues.put(COLUMN_DESCRIPTION, task.getDescription());
        initialValues.put(COLUMN_COMPLETION, task.getCompletion());
        initialValues.put(COLUMN_CREATED, task.getCreated());
        initialValues.put(COLUMN_DUEDATE, task.getDueDate());
        initialValues.put(COLUMN_TITLE, task.getTitle());
        initialValues.put(COLUMN_ASSIGNEE, task.getAssignee());
        initialValues.put(COLUMN_RESPONSIBLE, task.getResponsible());
        String followersMergerd = new String();
        for (int i = 0; i < task.getFollowers().length; i++) {
            int[] followers = task.getFollowers();
            followersMergerd = followersMergerd + "," + followers[i];
        }
        initialValues.put(COLUMN_FOLLOWERS, followersMergerd);
        String filesMerged = new String();
        for (int i = 0; i < task.getFiles().length; i++) {
            int[] files = task.getFiles();
            filesMerged = filesMerged + "," + files[i];
        }
        initialValues.put(COLUMN_FILES, filesMerged);
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_TASKVALUE, null, initialValues);
    }

    public long insertTableSubtask(SubTask subTask) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK_SUBTASK, subTask.getPk());
        initialValues.put(COLUMN_PK, subTask.getPkTask());
        initialValues.put(COLUMN_TITLE_SUBTASK, subTask.getTitle());
        initialValues.put(COLUMN_STATUS_SUBTASK, subTask.getStatus());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_SUBTASK, null, initialValues);
    }

    public long insertTableComment(Comment comment) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK, comment.getPkTask());
        initialValues.put(COLUMN_PK_COMMENT, comment.getPkComment());
        initialValues.put(COLUMN_COMMENT_CREATED, comment.getCreated());
        initialValues.put(COLUMN_CATEGORY, comment.getCategory());
        initialValues.put(COLUMN_TEXT, comment.getText());
        initialValues.put(COLUMN_COMMIT_PK, comment.getCommitPK());
        initialValues.put(COLUMN_COMMIT_MESSAGE, comment.getCommitMessage());
        initialValues.put(COLUMN_POST_USER, comment.getUser());
        initialValues.put(COLUMN_POST_USER_PK, comment.getUserPK());
        initialValues.put(COLUMN_COMMIT_DATE, comment.getCommitDate());
        initialValues.put(COLUMN_COMMIT_CODE, comment.getCommitCode());
        initialValues.put(COLUMN_COMMIT_BRANCH, comment.getCommitBranch());
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_COMMENTS, null, initialValues);
    }


    public long insetTableFiles(File file) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_FILE_PK, file.getFilePk());
        initialValues.put(COLUMN_PK_TASK, file.getPkTask());
        initialValues.put(COLUMN_FILE_LINK, file.getFileLink());
        initialValues.put(COLUMN_ATTACHMENT, file.getAttachment());
        initialValues.put(COLUMN_MEDIA_TYPE, file.getMediaType());
        initialValues.put(COLUMN_NAME, file.getName());
        initialValues.put(COLUMN_POSTED_USER, file.getPostedUser());
        initialValues.put(COLUMN_FILE_CREATED, file.getFileCreated());
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_FILES, null, initialValues);
    }


    public long insertTableProjects(Projects projects) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK_PROJECT, projects.getPk());
        initialValues.put(COLUMN_DESCRIPTION_PROJECT, projects.getDescription());
        initialValues.put(COLUMN_CREATED_PROJECT, projects.getCreated());
        initialValues.put(COLUMN_DUEDATE_PROJECT, projects.getDueDate());
        initialValues.put(COLUMN_TITLE_PROJECT, projects.getTitle());
        initialValues.put(COLUMN_USER_PROJECT, projects.getUser());
        String teamMergerd = new String();

        for (int i = 0; i < projects.getTeam().length; i++) {
            int[] team = projects.getTeam();
            teamMergerd = teamMergerd + "," + team[i];
        }
        initialValues.put(COLUMN_TEAM_PROJECT, teamMergerd);
        String filesMerged = new String();
        for (int i = 0; i < projects.getFiles().length; i++) {
            int[] files = projects.getFiles();
            filesMerged = filesMerged + "," + files[i];
        }
        initialValues.put(COLUMN_FILES_PROJECT, filesMerged);
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(TABLE_PROJECTS, null, initialValues);
    }

    // retrive data from table tasks


    public void cleanUsers() {
        String query = "DELETE FROM " + TABLE_TASKVALUE;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    public String databasetostring(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

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
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

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

    public int getPK(int position) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

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

    public int getTotalDBEntries_TASKVALUE() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKVALUE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public String getTitleFromPk(int pk) {
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE " + COLUMN_PK + " = " + pk;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("TITLE")) != null) {
                dbstring = c.getString(c.getColumnIndex("TITLE"));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public String getTitle(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

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

    public Date getCreated(int position) {
        ArrayList<Date> dbstring = new ArrayList<Date>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CREATED")) != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    date = formatter.parse(c.getString(c.getColumnIndex("CREATED")));
                } catch (ParseException e) {
                    System.out.println("error while parsing");
                }
                dbstring.add(date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public Date getDueDate(int position) {
        ArrayList<Date> dbstring = new ArrayList<Date>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("DUEDATE")) != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    date = formatter.parse(c.getString(c.getColumnIndex("DUEDATE")));
                } catch (ParseException e) {
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
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getAssignee(int pk_task) {
        int dbstring = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("ASSIGNEE")) != null) {
                dbstring = c.getInt(c.getColumnIndex("ASSIGNEE"));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public int getResponsible(int pk_task) {
        int dbstring = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("RESPONSIBLE")) != null) {
                dbstring = (c.getInt(c.getColumnIndex("RESPONSIBLE")));

            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public String getDescription(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

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

    public List<Integer> getFollowers(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("FOLLOWERS")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("FOLLOWERS")));
            }
            c.moveToNext();
        }
        db.close();
        dbstring.get(position);
        String[] followers = dbstring.get(position).split(",");
        List<Integer> followersList = new ArrayList<>();

//        System.out.println("followersp[0]"+followers[1]);
        for (int i = 0; i < followers.length; i++) {
            if (!followers[i].equals(""))
                followersList.add(Integer.parseInt(followers[i]));
        }
        return followersList;
    }


    public List<Integer> getFilesPk(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("FILES")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("FILES")));
            }
            c.moveToNext();
        }
        db.close();
        dbstring.get(position);
        String[] files = dbstring.get(position).split(",");
        List<Integer> filesList = new ArrayList<>();

//        System.out.println("followersp[0]"+followers[1]);
        for (int i = 0; i < files.length; i++) {
            if (!files[i].equals(""))
                filesList.add(Integer.parseInt(files[i]));
        }
        return filesList;
    }
    // retrive data from table comments


    public int getTotalDBEntries_COMMENT(int pk_task) {
        String countQuery = "SELECT  * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;
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
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public String getCategory(int position, int pk_task) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CATEGORY")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("CATEGORY")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public String getCreatedMessage(int position, int pk_task) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMENT_CREATED")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("COMMENT_CREATED")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

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

    public String getCommitBranch(int pk_comment) {
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_COMMIT_BRANCH + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMIT_BRANCH")) != null) {
                dbstring = c.getString(c.getColumnIndex("COMMIT_BRANCH"));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public Date getCommitDate(int pk_comment) {
        Date dbstring = new Date();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMIT_DATE")) != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    c.getColumnIndex("COMMIT_DATE");
                    date = formatter.parse(c.getString(c.getColumnIndex("COMMIT_DATE")));
                } catch (ParseException e) {
                    System.out.println("error while parsing");
                }
                dbstring = (date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }

    public Date getCommentDate(int pk_comment) {
        Date dbstring = new Date();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_COMMENT_CREATED + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_COMMENT_CREATED)) != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    date = formatter.parse(c.getString(c.getColumnIndex(COLUMN_COMMENT_CREATED)));
                } catch (ParseException e) {
                    System.out.println("error while parsing");
                }
                dbstring = (date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }


    public String getCommitCode(int pk_comment) {
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_COMMIT_CODE + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMIT_CODE")) != null) {
                dbstring = c.getString(c.getColumnIndex("COMMIT_CODE"));
            }
            c.moveToNext();
        }
        dbstring = dbstring.substring(30);
        db.close();
        return dbstring;
    }

    public int getCommentPK(int position, int pk_task) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("PK_COMMENT")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("PK_COMMENT")));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }


    public String getPostUser(int pk_comment) {
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("POST_USER")) != null) {
                dbstring = c.getString(c.getColumnIndex("POST_USER"));
            }
            c.moveToNext();
        }
        db.close();

        return dbstring;
    }

    public int getPostUserPK(int pk_comment) {
        int dbstring = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("POST_USER_PK")) != null) {
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
        String selectQuery = "SELECT  * FROM " + TABLE_SUBTASK + " WHERE " + COLUMN_PK + " = " + pkTask;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SubTask subTask = new SubTask(pkTask);
                subTask.setPk(Integer.parseInt(cursor.getString(0)));
                subTask.setTitle(cursor.getString(2));
                subTask.setStatus(cursor.getString(3));

                String name = cursor.getString(2) + "\n" + cursor.getString(3);
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


    // retrive data from table file

    public String getColumnAttachment(int pk_file) {
        String dbstring = new String();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_FILES + " WHERE " + COLUMN_FILE_PK + " = " + pk_file;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("FILE_PK")) != null) {
                dbstring = c.getString(c.getColumnIndex("FILE_PK"));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }


    public List<File> getAllFiles(int pkTask) {
        List<File> fileList = new ArrayList<File>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FILES + " WHERE " + COLUMN_PK_TASK + " = " + pkTask;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                File file = new File(pkTask);
                file.setFilePk(Integer.parseInt(cursor.getString(0)));
                file.setFileLink(cursor.getString(2));
                file.setAttachment(cursor.getString(3));
                file.setMediaType(cursor.getString(4));
                file.setName(cursor.getString(5));
                file.setPostedUser(Integer.parseInt(cursor.getString(6)));
                file.setFileCreated(cursor.getString(7));
                fileList.add(file);
            } while (cursor.moveToNext());
        }

        // return contact list
        return fileList;
    }

    public boolean CheckIfFILE_PKAlreadyInDBorNot(int fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_FILES + " where " + COLUMN_FILE_PK + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Date getFileUploadDate(int pk_file) {
        Date dbstring = new Date();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_FILES + " WHERE " + COLUMN_FILE_PK + " = " + pk_file;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("FILE_CREATED")) != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    c.getColumnIndex("FILE_CREATED");
                    date = formatter.parse(c.getString(c.getColumnIndex("FILE_CREATED")));
                } catch (ParseException e) {
                    System.out.println("error while parsing");
                }
                dbstring = (date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring;
    }


    public List<SubTask> getAllSubtasksList() {
        List<SubTask> subTaskList = new ArrayList<SubTask>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUBTASK + " WHERE 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SubTask subTask = new SubTask(Integer.parseInt(cursor.getString(0)));
                subTask.setPk(Integer.parseInt(cursor.getString(0)));
                subTask.setPkTask(Integer.parseInt(cursor.getString(1)));
                subTask.setTitle(cursor.getString(2));
                subTask.setStatus(cursor.getString(3));
                String name = cursor.getString(2) + "\n" + cursor.getString(3);
                subTaskList.add(subTask);
            } while (cursor.moveToNext());
        }
        return subTaskList;
    }


    /////// table projects

    public boolean CheckIfPK_ProjectAlreadyInDBorNot(int pk_project) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_PROJECTS + " where " + COLUMN_PK_PROJECT + " = " + pk_project;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getTotalDBEntries_PROJECTS() {
        String countQuery = "SELECT  * FROM " + TABLE_PROJECTS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public String getTitleProject(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_TITLE_PROJECT)) != null) {
                dbstring.add(c.getString(c.getColumnIndex(COLUMN_TITLE_PROJECT)));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public String getDescriptionProject(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_DESCRIPTION_PROJECT)) != null) {
                dbstring.add(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION_PROJECT)));
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public List<File> getProjectFiles(int pkTask) {
        List<File> fileList = new ArrayList<File>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FILES + " WHERE " + COLUMN_PK_TASK + " = " + pkTask;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                File file = new File(pkTask);
                file.setFilePk(Integer.parseInt(cursor.getString(0)));
                file.setFileLink(cursor.getString(2));
                file.setAttachment(cursor.getString(3));
                file.setMediaType(cursor.getString(4));
                file.setName(cursor.getString(5));
                file.setPostedUser(Integer.parseInt(cursor.getString(6)));
                file.setFileCreated(cursor.getString(7));
                fileList.add(file);
            } while (cursor.moveToNext());
        }
        return fileList;
    }


    public List<Integer> getTeamProject(int position) {
        ArrayList<String> dbstring = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("TEAM_PROJECT")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("TEAM_PROJECT")));
            }
            c.moveToNext();
        }
        db.close();
        dbstring.get(position);
        String[] team = dbstring.get(position).split(",");
        List<Integer> teamList = new ArrayList<>();

//        System.out.println("followersp[0]"+followers[1]);
        for (int i = 0; i < team.length; i++) {
            if (!team[i].equals(""))
                teamList.add(Integer.parseInt(team[i]));
        }
        return teamList;
    }
    public Date getCreatedProject(int position) {
        ArrayList<Date> dbstring = new ArrayList<Date>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CREATED_PROJECT")) != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    date = formatter.parse(c.getString(c.getColumnIndex("CREATED_PROJECT")));
                } catch (ParseException e) {
                    System.out.println("error while parsing");
                }
                dbstring.add(date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

    public Date getDueDateProject(int position) {
        ArrayList<Date> dbstring = new ArrayList<Date>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("DUEDATE_PROJECT")) != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {

                    date = formatter.parse(c.getString(c.getColumnIndex("DUEDATE_PROJECT")));
                } catch (ParseException e) {
                    System.out.println("error while parsing");
                }
                dbstring.add(date);
            }
            c.moveToNext();
        }
        db.close();
        return dbstring.get(position);
    }

}