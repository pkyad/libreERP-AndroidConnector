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

public class CustomTaskViewAdapter extends RecyclerView.Adapter<CustomTaskViewAdapter.ViewHolder> {

    private Context context;
    private List<Task> my_data;

    public CustomTaskViewAdapter(Context context, List<Task> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card ,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.description.setText(my_data.get(position).getDescription());
        Glide.with(context).load("http://placehold.it/350x150").into(holder.imageView);
        holder.progressBar.setProgress(30 + position*10);
        Integer color;
        if (position == 2){
            color = Color.rgb(0, 0, 100);
        }else if (position==3){
            color = Color.rgb(100, 0, 0);
        }else {
            color = Color.rgb(0, 100, 0);
        }

        holder.progressBar.getProgressDrawable().setColorFilter(
                color, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView description;
        public ImageView imageView;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}