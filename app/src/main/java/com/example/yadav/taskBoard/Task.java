package com.example.yadav.taskBoard;

/**
 * Created by Pradeep on 3/9/2017.
 */

public class Task {

    private Integer pk;
    private String description;
    private Integer completion;
    private String created;
    private String dueDate;
    private String title;
    private Integer assignee;
    private Integer responsible;
    private int[] followers;

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    public void setFollowers(int[] followers) {
        this.followers = followers;
    }

    public void setCompletion(Integer completion) {
        this.completion = completion;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTitle(String title) {this.title = title;}

    public void setAssignee(Integer assignee){this.assignee = assignee;}

    public void setResponsible(Integer responsible) {
        this.responsible = responsible;
    }


    public Task(Integer pk) {
        this.pk = pk;
    }


    public Integer getPk() {
        return pk;
    }
    public String getDescription() { return description;}


    public Integer getCompletion() {
        return completion;
    }

    public int[] getFollowers() {
        return followers;
    }

    public String getCreated() {
        return created;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTitle() {return title;}

    public Integer getAssignee() {return assignee;}

    public Integer getResponsible(){return responsible;}
}
