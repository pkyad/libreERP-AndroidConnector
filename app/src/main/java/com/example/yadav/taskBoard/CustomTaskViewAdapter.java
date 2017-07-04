package com.example.yadav.taskBoard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

/**
 * Created by Pradeep on 3/9/2017.
 */

public class CustomTaskViewAdapter extends RecyclerView.Adapter<CustomTaskViewAdapter.ViewHolder> {

    private Context context;
    private List<Task> my_data;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
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

        holder.dueDate.setText(my_data.get(position).getDueDate());
        if(my_data.get(position).getTitle().length()<=40) {
            holder.title.setText(my_data.get(position).getTitle());
        }
        else holder.title.setText(my_data.get(position).getTitle().substring(0,40) + "...");
        if(my_data.get(position).getDescription().length()<=60) {
            holder.description.setText(my_data.get(position).getDescription());
        }
        else holder.description.setText(my_data.get(position).getDescription().substring(0,60) + "...");
       // Glide.with(context).load("http://placehold.it/350x150").into(holder.imageView);
        holder.progressBar.setProgress(my_data.get(position).getCompletion());
        Integer color;
        Integer lightColor;
        if (my_data.get(position).getCompletion() <= 20){
            color = R.color.redDark;
            lightColor = R.color.white;
        }else if (my_data.get(position).getCompletion() >20 && my_data.get(position).getCompletion() < 50){
            color = R.color.yellowDark;
            lightColor = R.color.white;
        }else if (my_data.get(position).getCompletion() >=50 && my_data.get(position).getCompletion()<75){
            color = R.color.skyBlueDark;
            lightColor = R.color.white;
        }else {
            color = R.color.lightGreenDark;
            lightColor = R.color.white;
        }

        holder.progressBar.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
        viewBinderHelper.bind(holder.swipeRevealLayout, my_data.get(position).getTitle());
        viewBinderHelper.setOpenOnlyOne(true);
        ((CardView) holder.itemView.findViewById(R.id.card_view)).setCardBackgroundColor(ContextCompat.getColor(context, lightColor));
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView title;
        public TextView description;
        public TextView dueDate;
        public ProgressBar progressBar;
public SwipeRevealLayout swipeRevealLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            dueDate = (TextView) itemView.findViewById(R.id.dueDate);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            description = (TextView) itemView.findViewById(R.id.description);
            swipeRevealLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout_2);
        }
    }
}