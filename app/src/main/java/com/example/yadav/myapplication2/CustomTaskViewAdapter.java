package com.example.yadav.myapplication2;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
//        Glide.with(context).load("http://placehold.it/350x150").into(holder.imageView);
        holder.progressBar.setProgress(30 + position*10);
        Integer color;
        Integer lightColor;
        if (position == 1){
            color = R.color.skyBlueDark;
            lightColor = R.color.skyBlueLight;
        }else if (position == 2){
            color = R.color.orangeDark;
            lightColor = R.color.orangeLight;
        }else if (position==3){
            color = R.color.greenDark;
            lightColor = R.color.greenLight;
        }else {
            color = R.color.yellowDark;
            lightColor = R.color.yellowLight;
        }

        holder.progressBar.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);

//        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//            }
//        });

        ((CardView) holder.itemView.findViewById(R.id.card_view)).setCardBackgroundColor(ContextCompat.getColor(context, lightColor));
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView description;
        public ProgressBar progressBar;
        public TextView buttonViewOption;

        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.description);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
//            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }
}