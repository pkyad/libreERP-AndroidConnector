package com.example.yadav.myapplication2;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
            //System.out.println("getItemViewType = "+getItemViewType(i));
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
        //Glide.with(context).load("http://placehold.it/350x150").into(holder.imageView);
        holder.imageView.setImageBitmap(my_data.get(position).getDpUser());
        holder.username.setText(my_data.get(position).getUser());
//        Integer color;
//        if (position == 2){
//            color = Color.rgb(0, 0, 100);
//        }else if (position==3){
//            color = Color.rgb(100, 0, 0);
//        }else {
//            color = Color.rgb(0, 100, 0);
//        }
//
//        holder.progressBar.getProgressDrawable().setColorFilter(
//                color, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comment;
        public ImageView imageView;
        public TextView username;


        public ViewHolder(View itemView) {
            super(itemView);
            comment = (TextView) itemView.findViewById(R.id.comment);
            imageView = (ImageView) itemView.findViewById(R.id.userimage);
            username = (TextView) itemView.findViewById(R.id.username);
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

}