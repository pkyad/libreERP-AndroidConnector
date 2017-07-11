package com.example.yadav.taskBoard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pradeep on 3/9/2017.
 */


public class CustomTaskViewAdapterTimeline extends RecyclerView.Adapter<CustomTaskViewAdapterTimeline.ViewHolder> {
    public int i=0;
    private Context context;
    private List<Comment> my_data;
    private int category;
    View itemView;

    public CustomTaskViewAdapterTimeline(Context context, List<Comment> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (;i<my_data.size();i++) {
            i++ ;
            if (getItemViewType(i-1) == 1) {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_card1, parent, false);
                return new ViewHolder(itemView);
            }
            else {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_card2, parent, false);
                return new ViewHolder(itemView);
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.comment.setText(my_data.get(position).getText());
        holder.imageView.setImageBitmap(my_data.get(position).getDpUser());
        holder.username.setText(my_data.get(position).getUser());
        holder.commitbranch.setText(my_data.get(position).getCommitBranch());
        holder.commitdate.setText(my_data.get(position).getCommitDate());
        holder.commitcode.setText(my_data.get(position).getCommitCode());
        holder.createdComment.setText(my_data.get(position).getCreated());
        holder.pk_comment_user.setText( Integer.toString(my_data.get(position).getUserPK()));
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comment;
        public ImageView imageView;
        public TextView username;
        public TextView commitdate;
        public TextView commitcode;
        public TextView commitbranch;
        public TextView createdComment;
        public TextView pk_comment_user;

        public ViewHolder(View itemView) {
            super(itemView);
            comment = (TextView) itemView.findViewById(R.id.comment);
            imageView = (ImageView) itemView.findViewById(R.id.userimage);
            username = (TextView) itemView.findViewById(R.id.username);
            commitbranch = (TextView) itemView.findViewById(R.id.commitbranch);
            commitdate = (TextView) itemView.findViewById(R.id.commitdate);
            commitcode = (TextView) itemView.findViewById(R.id.commitcode);
            createdComment = (TextView) itemView.findViewById(R.id.commentdate);
            pk_comment_user = (TextView) itemView.findViewById(R.id.pk_user111);
        }
    }


    @Override
    public int getItemViewType(int position) {
        Comment comment = my_data.get(position);
       // System.out.println("category = "+ comment.getText());
        if(comment.getCategory().equals("git")){
            category = 2;
            return category;
        }
        else return category = 1;
    }
    public void clearData() {
        my_data.clear();
        notifyDataSetChanged();
    }
}