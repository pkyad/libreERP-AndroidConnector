package com.example.yadav.myapplication2;

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

    public void setPkTask(int PkTask){ this.PkTask = PkTask;}
    public void setPkComment(int PkComment){ this.PkComment = PkComment;}
    public void setCreated(String Created){ this.Created = Created;}
    public void setCategory(String Category){ this.Category = Category;}
    public void setText(String Text) {this.Text = Text;}
    public void setCommitPK(int CommitPk){this.CommitPK = CommitPk;}
    public void setCommitMessage(String CommitMessage){this.CommitMessage = CommitMessage;}

    public Comment(int PkComment){this.PkComment = PkComment;}

    public int getPkTask(){return PkTask;}
    public int getPkComment(){return PkComment;}
    public String getCreated(){return Created;}
    public String getCategory(){return Category;}
    public String getText(){return Text;}
    public int getCommitPK(){return CommitPK;}
    public String getCommitMessage(){return CommitMessage;}
}
