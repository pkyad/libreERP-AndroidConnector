package com.example.yadav.taskBoard;

/**
 * Created by Pradeep on 3/9/2017.
 */

public class Projects {

    private Integer pk;
    private String description;
    private String created;
    private String dueDate;
    private String title;
    private Integer user;
    private int[] team;
    private int[] files;

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    public void setFiles(int[] files) {
        this.files = files;
    }

    public void setTeam(int[] team) {
        this.team = team;
    }


    public void setCreated(String created) {
        this.created = created;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTitle(String title) {this.title = title;}

    public void setUser(Integer user){this.user = user;}



    public Projects(Integer pk) {
        this.pk = pk;
    }


    public Integer getPk() {
        return pk;
    }
    public String getDescription() { return description;}



    public int[] getTeam() {
        return team;
    }

    public int[] getFiles() {
        return files;
    }

    public String getCreated() {
        return created;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTitle() {return title;}

    public Integer getUser() {return user;}

}
