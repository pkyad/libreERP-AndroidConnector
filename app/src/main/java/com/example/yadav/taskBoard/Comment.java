package com.example.yadav.taskBoard;

import android.graphics.Bitmap;

/**
 * Created by cioc on 6/6/17.
 */

public class Comment {

    private int PkTask;
    private int PkComment;
    private String Created;
    private String Category;
    private String Text;
    private int CommitPK;
    private String CommitMessage;
    private String  user;
    private int userPK;
    private Bitmap dpUser;
    private String CommitDate;
    private String CommitBranch;
    private String CommitCode;

    public void setDpUser(Bitmap dpUser){this.dpUser = dpUser;}
    public void setPkTask(int PkTask){ this.PkTask = PkTask;}
    public void setPkComment(int PkComment){ this.PkComment = PkComment;}
    public void setCreated(String Created){ this.Created = Created;}
    public void setCategory(String Category){ this.Category = Category;}
    public void setText(String Text) {this.Text = Text;}
    public void setCommitPK(int CommitPk){this.CommitPK = CommitPk;}
    public void setCommitMessage(String CommitMessage){this.CommitMessage = CommitMessage;}
    public void setUser(String  user){this.user = user;}
    public void setCommitDate(String CommitDate){this.CommitDate = CommitDate;}

    public void setUserPK(int userPK) {
        this.userPK = userPK;
    }

    public void setCommitBranch(String commitBranch) {
        CommitBranch = commitBranch;
    }

    public void setCommitCode(String commitCode) {
        CommitCode = commitCode;
    }

    public Comment(int PkComment){this.PkComment = PkComment;}

    public int getPkTask(){return PkTask;}
    public int getPkComment(){return PkComment;}
    public String getCreated(){return Created;}
    public String getCategory(){return Category;}
    public String getText(){return Text;}
    public int getCommitPK(){return CommitPK;}
    public String getCommitMessage(){return CommitMessage;}
    public String getUser(){return user;}
    public Bitmap getDpUser(){return dpUser;}

    public String getCommitBranch() {
        return CommitBranch;
    }

    public int getUserPK() {
        return userPK;
    }

    public String getCommitCode() {
        return CommitCode;
    }

    public String getCommitDate() {
        return CommitDate;
    }
}
