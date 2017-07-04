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

public class CustomProjectViewAdapter extends RecyclerView.Adapter<CustomProjectViewAdapter.ViewHolder> {

    private Context context;
    private List<Projects> my_data;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public CustomProjectViewAdapter(Context context, List<Projects> my_data) {
        this.context = context;
        this.my_data = my_data;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card ,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.team.setText(my_data.get(position).getTeam().length + " Team Members");
     //   holder.files.setText(my_data.get(position).getFiles().length + " Files");
        if(my_data.get(position).getTitle().length()<=40) {
            holder.title.setText(my_data.get(position).getTitle());
        }
        else holder.title.setText(my_data.get(position).getTitle().substring(0,40) + "...");
        if(my_data.get(position).getDescription().length()<=60) {
            holder.description.setText(my_data.get(position).getDescription());
        }
        else holder.description.setText(my_data.get(position).getDescription().substring(0,60) + "...");
       // Glide.with(context).load("http://placehold.it/350x150").into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView title;
        public TextView description;
        public TextView team;
        public TextView files;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_project);
            team = (TextView) itemView.findViewById(R.id.team_member_number);
            files = (TextView) itemView.findViewById(R.id.numbers_file);
            description = (TextView) itemView.findViewById(R.id.description_project);

        }
    }
}