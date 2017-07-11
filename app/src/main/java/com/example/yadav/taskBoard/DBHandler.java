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
    private static final int DATABASE_VERSION = 2;
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
    private static final String COLUMN_PK_PROJECT111 = "PK_PROJECT";
    private static final String COLUMN_PERSONAL = "PERSONAL";


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
    private static final String COLUMN_PROJECT_PK_FILE = "PROJECT_PK_FILE";
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
    private static final String COLUMN_REPOS_COUNT = "PROJECT_REPOS";
    private SQLiteDatabase db;
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
        db = this.getWritableDatabase();
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
                + COLUMN_FILES + " TEXT,"
                + COLUMN_PK_PROJECT111 + " INTEGER,"
                + COLUMN_PERSONAL + " TEXT"
                + ");";


        String querySubTask = "CREATE TABLE " + TABLE_SUBTASK + "(" + COLUMN_PK_SUBTASK + " INTEGER,"
                + COLUMN_PK + " INTEGER,"
                + COLUMN_TITLE_SUBTASK + " TEXT,"
                + COLUMN_STATUS_SUBTASK + "  TEXT"
                + ");";

        String queryComment = "CREATE TABLE " + TABLE_COMMENTS + "(" + COLUMN_PK_COMMENT + " INTEGER,"
                + COLUMN_PK + " INTEGER,"
                + COLUMN_PK_PROJECT + " INTEGER,"
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
                + COLUMN_PROJECT_PK_FILE + " INTEGER,"
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
                + COLUMN_USER_PROJECT + " INTEGER,"
                + COLUMN_CREATED_PROJECT + " TEXT,"
                + COLUMN_DUEDATE_PROJECT + " TEXT,"
                + COLUMN_FILES_PROJECT + " TEXT,"
                + COLUMN_REPOS_COUNT + " INTEGER"
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
        initialValues.put(COLUMN_PK_PROJECT111,task.getPk_project());
        initialValues.put(COLUMN_PERSONAL,task.getPersonal());
        return db.insert(TABLE_TASKVALUE, null, initialValues);
    }

    public long insertTableSubtask(SubTask subTask) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK_SUBTASK, subTask.getPk());
        initialValues.put(COLUMN_PK, subTask.getPkTask());
        initialValues.put(COLUMN_TITLE_SUBTASK, subTask.getTitle());
        initialValues.put(COLUMN_STATUS_SUBTASK, subTask.getStatus());


        return db.insert(TABLE_SUBTASK, null, initialValues);
    }

    public long insertTableComment(Comment comment) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_PK, comment.getPkTask());
        initialValues.put(COLUMN_PK_COMMENT, comment.getPkComment());
        initialValues.put(COLUMN_PK_PROJECT,comment.getPkProject());
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


        return db.insert(TABLE_COMMENTS, null, initialValues);
    }


    public long insetTableFiles(File file) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_FILE_PK, file.getFilePk());
        initialValues.put(COLUMN_PK_TASK, file.getPkTask());
        initialValues.put(COLUMN_PROJECT_PK_FILE,file.getProject_pk());
        initialValues.put(COLUMN_FILE_LINK, file.getFileLink());
        initialValues.put(COLUMN_ATTACHMENT, file.getAttachment());
        initialValues.put(COLUMN_MEDIA_TYPE, file.getMediaType());
        initialValues.put(COLUMN_NAME, file.getName());
        initialValues.put(COLUMN_POSTED_USER, file.getPostedUser());
        initialValues.put(COLUMN_FILE_CREATED, file.getFileCreated());

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
        initialValues.put(COLUMN_REPOS_COUNT, projects.getRepoCount());


        return db.insert(TABLE_PROJECTS, null, initialValues);
    }

    // retrive data from table tasks


    public void cleanTasks() {
        String query = "DELETE FROM " + TABLE_TASKVALUE;

        db.execSQL(query);
    }
    public void cleanSubTasks() {
        String query = "DELETE FROM " + TABLE_SUBTASK;
        db.execSQL(query);
    }
    public void cleanFiles() {
        String query = "DELETE FROM " + TABLE_FILES;
        db.execSQL(query);
    }
    public void cleanProjects() {
        String query = "DELETE FROM " + TABLE_PROJECTS;
        db.execSQL(query);
    }
    public void cleanComments() {
        String query = "DELETE FROM " + TABLE_COMMENTS;

        db.execSQL(query);
    }
    public List<Task> getAllTasksList() {
        List<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(Integer.parseInt(cursor.getString(0)));
                task.setPk(Integer.parseInt(cursor.getString(0)));
                task.setTitle((cursor.getString(1)));
                task.setDescription(cursor.getString(2));
                task.setCompletion(Integer.parseInt(cursor.getString(3)));

                String[] followers = cursor.getString(4).split(",");
                List<Integer> followersListList = new ArrayList<>();

                for (int i = 0; i < followers.length; i++) {
                    if (!followers[i].equals(""))
                        followersListList.add(Integer.parseInt(followers[i]));
                }
                int followersList[] = new int[followersListList.size()];
                for (int i=0;i<followersListList.size();i++)
                    followersList[i] = followersListList.get(i);
                task.setFollowers(followersList);



                task.setCreated(cursor.getString(6));
                task.setDueDate(cursor.getString(7));
                task.setAssignee(Integer.parseInt(cursor.getString(8)));
                task.setResponsible(Integer.parseInt(cursor.getString(9)));
                String[] files = cursor.getString(10).split(",");

                int filesList[] = new int[files.length];
                for (int i = 0; i < files.length; i++) {
                    if (!files[i].equals(""))
                        filesList[i]=(Integer.parseInt(files[i]));
                }
                task.setFiles(filesList);
                task.setPk_project(Integer.parseInt(cursor.getString(11)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }

    public void deleteTask(int PK_Task){
        String query = "DELETE FROM " + TABLE_TASKVALUE + " WHERE " + COLUMN_PK + " = " + PK_Task;
        db.execSQL(query);
    }
    public void updateTask(int PK_Task,String title,String description,String dueDate){
        String query = "UPDATE " + TABLE_TASKVALUE + " SET " + COLUMN_TITLE + " = '" + title + "', " + COLUMN_DESCRIPTION + " = '" + description + "', " +
                COLUMN_DUEDATE + " = '" + dueDate + "' WHERE " + COLUMN_PK + " = " + PK_Task;
        db.execSQL(query);
    }

    public String getTitle(int pk) {
        String dbstring = new String();

        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE " + COLUMN_PK + " = " + pk;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("TITLE")) != null) {
                dbstring = c.getString(c.getColumnIndex("TITLE"));
            }
            c.moveToNext();
        }

        return dbstring;
    }



    public boolean CheckIfPKAlreadyInDBorNot(int pk_Task) {

        String Query = "Select * from " + TABLE_TASKVALUE + " where " + COLUMN_PK + " = " + pk_Task;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getResponsible(int pk_task) {
        int dbstring = 0;

        String query = "SELECT * FROM " + TABLE_TASKVALUE + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("RESPONSIBLE")) != null) {
                dbstring = (c.getInt(c.getColumnIndex("RESPONSIBLE")));

            }
            c.moveToNext();
        }

        return dbstring;
    }


    public int getTotalDBEntries_TASK() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKVALUE + " WHERE 1";

        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // retrive data from table comments


    public int getTotalDBEntries_COMMENT(int pk_task) {
        String countQuery = "SELECT  * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public boolean CheckIfCOMMENT_PKAlreadyInDBorNot(int pk_Comment) {

        String Query = "Select * from " + TABLE_COMMENTS + " where " + COLUMN_PK_COMMENT + " = " + pk_Comment;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public String getCategory(int position, int pk_task) {
        ArrayList<String> dbstring = new ArrayList<String>();

        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CATEGORY")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("CATEGORY")));
            }
            c.moveToNext();
        }

        return dbstring.get(position);
    }

    public String getCategory_COMMENT_Project(int position, int pk_project) {
        ArrayList<String> dbstring = new ArrayList<String>();

        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_PROJECT + " = " + pk_project;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("CATEGORY")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("CATEGORY")));
            }
            c.moveToNext();
        }

        return dbstring.get(position);
    }

    public String getCreatedMessage(int position, int pk_task) {
        ArrayList<String> dbstring = new ArrayList<String>();

        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMENT_CREATED")) != null) {
                dbstring.add(c.getString(c.getColumnIndex("COMMENT_CREATED")));
            }
            c.moveToNext();
        }

        return dbstring.get(position);
    }

    public String getMessage(int pk_comment) {
//        ArrayList<String> dbstring = new ArrayList<String>();
        String dbstring = new String();

        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("TEXT")) != null) {
                dbstring = c.getString(c.getColumnIndex("TEXT"));

            }
            c.moveToNext();
        }

        return dbstring;
    }

    public String getCommitMessage(int pk_comment) {
        String dbstring = new String();

        String query = "SELECT " + COLUMN_COMMIT_MESSAGE + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMIT_MESSAGE")) != null) {
                dbstring = c.getString(c.getColumnIndex("COMMIT_MESSAGE"));
            }
            c.moveToNext();
        }

        return dbstring;
    }

    public String getCommitBranch(int pk_comment) {
        String dbstring = new String();

        String query = "SELECT " + COLUMN_COMMIT_BRANCH + " FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("COMMIT_BRANCH")) != null) {
                dbstring = c.getString(c.getColumnIndex("COMMIT_BRANCH"));
            }
            c.moveToNext();
        }

        return dbstring;
    }

    public Date getCommitDate(int pk_comment) {
        Date dbstring = new Date();

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

        return dbstring;
    }

    public Date getCommentDate(int pk_comment) {
        Date dbstring = new Date();

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

        return dbstring;
    }


    public String getCommitCode(int pk_comment) {
        String dbstring = new String();

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

        return dbstring;
    }

    public int getCommentPK(int position, int pk_task) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();

        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK + " = " + pk_task;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("PK_COMMENT")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("PK_COMMENT")));
            }
            c.moveToNext();
        }

        return dbstring.get(position);
    }

    public int getCommentPK_Project_Comment(int position, int pk_project) {
        ArrayList<Integer> dbstring = new ArrayList<Integer>();

        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_PROJECT + " = " + pk_project;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("PK_COMMENT")) != null) {
                dbstring.add(c.getInt(c.getColumnIndex("PK_COMMENT")));
            }
            c.moveToNext();
        }

        return dbstring.get(position);
    }
    public String getPostUser(int pk_comment) {
        String dbstring = new String();

        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("POST_USER")) != null) {
                dbstring = c.getString(c.getColumnIndex("POST_USER"));
            }
            c.moveToNext();
        }


        return dbstring;
    }

    public int getPostUserPK(int pk_comment) {
        int dbstring = 0;

        String query = "SELECT * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_COMMENT + " = " + pk_comment;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("POST_USER_PK")) != null) {
                dbstring = c.getInt(c.getColumnIndex("POST_USER_PK"));
            }
            c.moveToNext();
        }


        return dbstring;
    }


    // retrive data from table subtask

    public List<SubTask> getAllSubtasks(int pkTask) {
        List<SubTask> subTaskList = new ArrayList<SubTask>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUBTASK + " WHERE " + COLUMN_PK + " = " + pkTask;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SubTask subTask = new SubTask(pkTask);
                subTask.setPk(Integer.parseInt(cursor.getString(0)));
                subTask.setPkTask(Integer.parseInt(cursor.getString(1)));
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

    public void updateSubTaskStatus(int PK_Subtask,String status){
        String Query = "UPDATE " + TABLE_SUBTASK +   " SET " + COLUMN_STATUS_SUBTASK + " = " + "'" + status + "'" + " WHERE " + COLUMN_PK_SUBTASK + " = " + PK_Subtask;
        db.execSQL(Query);
    }
    public void deleteSubTask(int PK_Subtask){
        String query = "DELETE FROM " + TABLE_SUBTASK + " WHERE " + COLUMN_PK_SUBTASK + " = " + PK_Subtask;
        db.execSQL(query);
    }
    public void updateSubTaskTitle(int PK_Subtask,String title){
        String Query = "UPDATE " + TABLE_SUBTASK +   " SET " + COLUMN_TITLE_SUBTASK + " = " + "'" + title + "'" + " WHERE " + COLUMN_PK_SUBTASK + " = " + PK_Subtask;
        db.execSQL(Query);
    }

    public boolean CheckIfSUB_PKAlreadyInDBorNot(int fieldValue) {

        String Query = "Select * from " + TABLE_SUBTASK + " where " + COLUMN_PK_SUBTASK + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
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
        String query = "SELECT * FROM " + TABLE_FILES + " WHERE " + COLUMN_FILE_PK + " = " + pk_file;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("FILE_PK")) != null) {
                dbstring = c.getString(c.getColumnIndex("FILE_PK"));
            }
            c.moveToNext();
        }

        return dbstring;
    }


    public List<File> getAllFiles(int pkTask) {
        List<File> fileList = new ArrayList<File>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FILES + " WHERE " + COLUMN_PK_TASK + " = " + pkTask;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                File file = new File(pkTask);
                file.setFilePk(Integer.parseInt(cursor.getString(0)));
                file.setPkTask(Integer.parseInt(cursor.getString(1)));
                file.setProject_pk(Integer.parseInt(cursor.getString(2)));
                file.setFileLink(cursor.getString(3));
                file.setAttachment(cursor.getString(4));
                file.setMediaType(cursor.getString(5));
                file.setName(cursor.getString(6));
                file.setPostedUser(Integer.parseInt(cursor.getString(7)));
                file.setFileCreated(cursor.getString(8));
                fileList.add(file);
            } while (cursor.moveToNext());
        }

        // return contact list
        return fileList;
    }

    public boolean CheckIfFILE_PKAlreadyInDBorNot(int fieldValue) {

        String Query = "Select * from " + TABLE_FILES + " where " + COLUMN_FILE_PK + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Date getFileUploadDate(int pk_file) {
        Date dbstring = new Date();
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

        return dbstring;
    }


    public List<SubTask> getAllSubtasksList() {
        List<SubTask> subTaskList = new ArrayList<SubTask>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUBTASK + " WHERE 1";

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



    public List<Projects> getAllProjectList() {
        List<Projects> projectsList = new ArrayList<Projects>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROJECTS + " WHERE 1";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Projects projects = new Projects(Integer.parseInt(cursor.getString(0)));
                projects.setPk(Integer.parseInt(cursor.getString(0)));
                projects.setTitle((cursor.getString(1)));
                projects.setDescription(cursor.getString(2));

                String[] team = cursor.getString(3).split(",");
                List<Integer> teamListList = new ArrayList<>();

                for (int i = 0; i < team.length; i++) {
                    if (!team[i].equals(""))
                        teamListList.add(Integer.parseInt(team[i]));
                }
                int teamList[] = new int[teamListList.size()];
                for(int i=0;i<teamListList.size();i++){
                    teamList[i] = teamListList.get(i);
                }
                projects.setTeam(teamList);

                projects.setUser(Integer.parseInt(cursor.getString(4)));
                Date createdDate = new Date();
                Date dueDate = new Date();
                Date current = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    createdDate = formatter.parse(cursor.getString(5));
                    dueDate = formatter.parse(cursor.getString(6));
                } catch (ParseException e) {
                    System.out.println("error while parsing");
                }
                String formattedCreatedDate;
                String formattedDueDate;
                if (createdDate.getYear() == current.getYear()) {
                    formattedCreatedDate = new SimpleDateFormat("dd MMM").format(createdDate);
                } else {
                    formattedCreatedDate = new SimpleDateFormat("dd MMM, yyyy").format(createdDate);
                }
                if (dueDate.getYear() == current.getYear()) {
                    formattedDueDate = new SimpleDateFormat("dd MMM").format(dueDate);
                } else {
                    formattedDueDate = new SimpleDateFormat("dd MMM, yyyy").format(dueDate);
                }
                projects.setCreated(formattedCreatedDate);
                projects.setDueDate(formattedDueDate);

                String[] files = cursor.getString(7).split(",");

                int filesList[] = new int[files.length];
                for (int i = 0; i < files.length; i++) {
                    if (!files[i].equals(""))
                        filesList[i]=(Integer.parseInt(files[i]));
                }
                projects.setFiles(filesList);
                projects.setRepoCount(Integer.parseInt(cursor.getString(8)));
                projectsList.add(projects);


            } while (cursor.moveToNext());
        }
        return projectsList;
    }


    public boolean CheckIfPK_ProjectAlreadyInDBorNot(int pk_project) {

        String Query = "Select * from " + TABLE_PROJECTS + " where " + COLUMN_PK_PROJECT + " = " + pk_project;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public String getTitleProjectFormPK(int PK_Project) {
        String dbstring = new String();

        String query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE " + COLUMN_PK_PROJECT +  " = " + PK_Project;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_TITLE_PROJECT)) != null) {
                dbstring = (c.getString(c.getColumnIndex(COLUMN_TITLE_PROJECT)));
            }
            c.moveToNext();
        }

        return dbstring;
    }




    public List<File> getProjectFiles(int pk_project) {
        List<File> fileList = new ArrayList<File>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FILES + " WHERE " + COLUMN_PROJECT_PK_FILE + " = " + pk_project;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                File file = new File(pk_project);
                file.setProject_pk(pk_project);
                file.setFilePk(Integer.parseInt(cursor.getString(0)));
                file.setPkTask(Integer.parseInt(cursor.getString(1)));
                file.setProject_pk(Integer.parseInt(cursor.getString(2)));
                file.setFileLink(cursor.getString(3));
                file.setAttachment(cursor.getString(4));
                file.setMediaType(cursor.getString(5));
                file.setName(cursor.getString(6));
                file.setPostedUser(Integer.parseInt(cursor.getString(7)));
                file.setFileCreated(cursor.getString(8));
                fileList.add(file);
            } while (cursor.moveToNext());
        }
        return fileList;
    }


    public int getTotalDBEntries_COMMENT_Project(int pk_projet) {
        String countQuery = "SELECT  * FROM " + TABLE_COMMENTS + " WHERE " + COLUMN_PK_PROJECT + " = " + pk_projet;

        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

}