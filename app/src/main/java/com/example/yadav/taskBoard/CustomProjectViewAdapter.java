package com.example.yadav.taskBoard;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        if(my_data.get(position).getTeam().length !=0 ) {
            holder.team.setText(Integer.toString(my_data.get(position).getTeam().length));
        }
        else if(my_data.get(position).getTeam().length ==0){
            holder.team.setVisibility(View.GONE);
            holder.teamLogo.setVisibility(View.GONE);
        }

        if (my_data.get(position).getFiles().length -1!=0) {
            holder.files.setText(Integer.toString(my_data.get(position).getFiles().length - 1));
        }
        else if (my_data.get(position).getFiles().length -1==0)
        {
            holder.files.setVisibility(View.GONE);
            holder.filesLogo.setVisibility(View.GONE);
        }
        if(my_data.get(position).getRepoCount() !=0)
            holder.repoCount.setText(Integer.toString(my_data.get(position).getRepoCount()));
        else if(my_data.get(position).getRepoCount()==0){
            holder.repoCount.setVisibility(View.GONE);
            holder.reposLogo.setVisibility(View.GONE);
        }

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
        public TextView repoCount;
        public TextView files;
        public ImageView teamLogo;
        public ImageView filesLogo;
        public ImageView reposLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_project);
            team = (TextView) itemView.findViewById(R.id.team_member_number);
            files = (TextView) itemView.findViewById(R.id.numbers_file);
            description = (TextView) itemView.findViewById(R.id.description_project);
            repoCount = (TextView) itemView.findViewById(R.id.numbers_repos);
            teamLogo = (ImageView) itemView.findViewById(R.id.peopleLogo);
            filesLogo = (ImageView) itemView.findViewById(R.id.filesLogo);
            reposLogo = (ImageView) itemView.findViewById(R.id.reposLogo);
        }
    }
    public void clearData() {
        my_data.clear();
        notifyDataSetChanged();
    }
}