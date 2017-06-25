package com.example.yadav.taskBoard;

/**
 * Created by cioc on 2/6/17.
 */

public class SubTask {

    private int pk;
    private int pkTask;
    private String title;
    private String status;
    public void setPk(int pk){this.pk = pk;}
    public void setPkTask(int pkTask){this.pkTask = pkTask;}
    public void setTitle(String title){this.title = title;}
    public void setStatus(String status){this.status = status;}

    public int getPk(){return pk;}
    public String getTitle(){return title;}
    public String getStatus(){return status;}
    public int getPkTask(){return pkTask;}
    public SubTask(Integer pk) {
        this.pk = pk;
    }


}
