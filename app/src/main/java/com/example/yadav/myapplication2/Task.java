package com.example.yadav.myapplication2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pradeep on 3/9/2017.
 */

public class Task {

//    created = models.DateTimeField(auto_now_add = True)
//    title = models.CharField(blank = False , max_length = 200)
//    description = models.TextField(max_length=2000 , blank=False)
//    files = models.ManyToManyField(media , related_name='tasks', blank = True)
//    followers = models.ManyToManyField(User , related_name = 'taskFollowing', blank = True)
//    dueDate = models.DateTimeField(null = False)
//    user = models.ForeignKey(User , null = True) # the one who created it
//            to = models.ForeignKey(User , null = True , related_name='tasks')
//    personal = models.BooleanField(default = False)
//    project = models.ForeignKey(project , null = True)
//    completion = models.PositiveIntegerField(default=0)

    private Integer pk;
    private String description;
    private Integer completion;
    private String created;
    private String dueDate;
    private String title;
    private Integer assignee;
    private Integer responsible;

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setDescription(String description) {
        this.description = description;

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
