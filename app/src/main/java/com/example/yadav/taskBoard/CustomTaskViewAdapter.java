package com.example.yadav.taskBoard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.libreerp.Helper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Pradeep on 3/9/2017.
 */

public class CustomTaskViewAdapter extends RecyclerView.Adapter<CustomTaskViewAdapter.ViewHolder> {

    private Context context;
    private List<Task> my_data;
    DBHandler dba;
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
        final int pos = position;
        dba = new DBHandler(context, null, null, 2);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date formattedDueDate = new Date();
        Date current = new Date();
        try {
            formattedDueDate = formatter.parse(my_data.get(pos).getDueDate());
        } catch (ParseException e) {
            System.out.println("error while parsing");
        }
        if (formattedDueDate.getYear() == current.getYear()) {
            holder.dueDate.setText(new SimpleDateFormat("dd MMM").format(formattedDueDate));
        } else {
            holder.dueDate.setText(new SimpleDateFormat("dd MMM").format(formattedDueDate));
        }

        if(my_data.get(pos).getTitle().length()<=40) {
            holder.title.setText(my_data.get(pos).getTitle());
        }
        else holder.title.setText(my_data.get(pos).getTitle().substring(0,40) + "...");
        if(my_data.get(pos).getDescription().length()<=60) {
            holder.description.setText(my_data.get(pos).getDescription());
        }
        else holder.description.setText(my_data.get(pos).getDescription().substring(0,60) + "...");
       // Glide.with(context).load("http://placehold.it/350x150").into(holder.imageView);
        holder.progressBar.setProgress(my_data.get(pos).getCompletion());
        Integer color;
        Integer lightColor;
        if (my_data.get(pos).getCompletion() <= 20){
            color = R.color.redDark;
            lightColor = R.color.white;
        }else if (my_data.get(pos).getCompletion() >20 && my_data.get(pos).getCompletion() < 50){
            color = R.color.yellowDark;
            lightColor = R.color.white;
        }else if (my_data.get(pos).getCompletion() >=50 && my_data.get(pos).getCompletion()<75){
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

        holder.card_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskCardActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", pos);
                intent.putExtra("TASK",my_data.get(pos));
                context.startActivity(intent);
            }
        });

        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Helper helper = new Helper(context);
                                AsyncHttpClient client = helper.getHTTPClient();
                                RequestParams params = new RequestParams();
                                params.put("action","archive");

                                final String url = String.format("%s/%s/", helper.serverURL, "/api/taskBoard/");
                                client.patch(url, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        dba.deleteTask(my_data.get(pos).getPk());
                                        my_data.remove(my_data.get(pos));
                                        notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                        System.out.println("failure");
                                        System.out.println(statusCode);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


        holder.editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditTask.class);
                intent.putExtra("Task_Object",my_data.get(pos));
                context.startActivity(intent);
            }
        });

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
        public RelativeLayout card_task;
        public TextView editTask;
        public TextView deleteTask;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            dueDate = (TextView) itemView.findViewById(R.id.dueDate);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            description = (TextView) itemView.findViewById(R.id.description);
            editTask = (TextView) itemView.findViewById(R.id.edit_task);
            deleteTask = (TextView) itemView.findViewById(R.id.delete_task);
            swipeRevealLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout_2);
            card_task = (RelativeLayout) itemView.findViewById(R.id.card_task);
        }
    }

    public void clearData() {
        my_data.clear();
        notifyDataSetChanged();
    }
}