package com.example.yadav.taskBoard;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.libreerp.Helper;
import com.example.libreerp.UserViewBS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import rx.Subscription;
import rx.functions.Action1;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;

import static android.R.attr.action;
import static android.R.attr.tunerCount;
import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by cioc on 12/6/17.
 */
public class CustomSubTaskCardAdapter extends RecyclerView.Adapter<CustomSubTaskCardAdapter.ViewHolder> {

    private DBHandler dba;
    private Context context;
    private List<SubTask> my_data;
    List<ActivityManager.RunningTaskInfo> taskInfo;
    public CustomSubTaskCardAdapter(Context context, List<SubTask> my_data) {
        this.context = context;
        this.my_data = my_data;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        taskInfo = am.getRunningTasks(1);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtask_card ,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(taskInfo.get(0).topActivity.getClassName().contains("taskBoard.TaskCardActivity")){
            holder.taskTitle.setVisibility(View.GONE);
        }
        dba = new DBHandler(context, null, null, 2);
        holder.txtTitle.setText(my_data.get(position).getTitle());
        holder.taskTitle.setText(dba.getTitle(my_data.get(position).getPkTask()));
        if (my_data.get(position).getStatus().equals("complete")) {
            holder.status.setImageResource(R.drawable.ic_action_success);
        } else if (my_data.get(position).getStatus().equals("stuck")) {
            holder.status.setImageResource(R.drawable.ic_action_stuck);
        } else if (my_data.get(position).getStatus().equals("inProgress")) {
            holder.status.setImageResource(R.drawable.ic_action_progress);
        } else {
            holder.status.setImageResource(R.drawable.ic_action_stopwatch);
        }
        final int pos = position;

        holder.imageview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.subtaskoptionsmenu,
                        popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.inProgress:
                                onStatusChange("inProgress",pos);
                                break;
                            case R.id.stuck:
                                onStatusChange( "stuck",pos);
                                break;

                            case R.id.complete:
                                onStatusChange("complete",pos);
                                break;
                            case R.id.delete:
                                deleteSubTask(pos);
                                break;
                            case R.id.editSubtask:
                                editSubTask(pos);
                                break;
                            default:
                                break;
                        }

                        return true;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView txtTitle;
        ImageView status;
        ImageView imageview;
        RelativeLayout layout;
        TextView taskTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.textTitle);
            taskTitle = (TextView) itemView.findViewById(R.id.tasktitle);
            status = (ImageView) itemView.findViewById(R.id.subtaskStatus);
            imageview = (ImageView) itemView.findViewById(R.id.popup);
            layout = (RelativeLayout) itemView.findViewById(R.id.subtaskCard);
        }
    }

    public void clearData() {
        my_data.clear();
        notifyDataSetChanged();
    }


    private void onStatusChange(final String status, final int position) {


        Helper helper = new Helper(context);
        AsyncHttpClient client = helper.getHTTPClient();
        RequestParams params = new RequestParams();
        params.put("pk", my_data.get(position).getPk());
        params.put("title", my_data.get(position).getTitle());
        params.put("status", status);
//
        final String url = String.format("%s/%s/", helper.serverURL, "/api/taskBoard/subTask/" + my_data.get(position).getPk());
        client.patch(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dba.updateSubTaskStatus( my_data.get(position).getPk(), status);
                List<SubTask> newCard = dba.getAllSubtasks(my_data.get(position).getPkTask());
                for(int i=0;i<newCard.size();i++){
                    if(newCard.get(i).getPk() == my_data.get(position).getPk()){
                        String status = newCard.get(i).getStatus();
                        my_data.get(position).setStatus(status);
                    }
                }

                ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                taskInfo = am.getRunningTasks(1);
                System.out.println(" taskInfo.get(0).topActivity.getClassName() = " +  taskInfo.get(0).topActivity.getClassName());
                if(my_data.get(position).getStatus().equals("complete") && taskInfo.get(0).topActivity.getClassName().contains("taskBoard.HomeActivity")){
                    my_data.remove(my_data.get(position));
                }
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

    private void deleteSubTask(final int position){

        new AlertDialog.Builder(context)
                .setTitle("Delete Sub Task")
                .setMessage("Are you sure you want to delete")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Helper helper = new Helper(context);
                        AsyncHttpClient client = helper.getHTTPClient();
//
                        final String url = String.format("%s/%s/", helper.serverURL, "/api/taskBoard/subTask/" + my_data.get(position).getPk());
                        client.delete(url, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                dba.deleteSubTask(my_data.get(position).getPk());
                                my_data.remove(my_data.get(position));
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

    private void editSubTask(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText subTaskTitle;
        builder.setTitle("Sub Task");
        subTaskTitle = new EditText(context);
        subTaskTitle.setText(my_data.get(position).getTitle());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        subTaskTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        subTaskTitle.setMinLines(1);
        builder.setView(subTaskTitle);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Helper helper = new Helper(context);
                AsyncHttpClient client = helper.getHTTPClient();
                RequestParams params = new RequestParams();
                params.put("title",subTaskTitle.getText().toString());
                params.put("status", my_data.get(position).getStatus());
//
                final String url = String.format("%s/%s/", helper.serverURL, "/api/taskBoard/subTask/" + my_data.get(position).getPk());
                client.patch(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        dba.updateSubTaskTitle( my_data.get(position).getPk(),subTaskTitle.getText().toString());
                        List<SubTask> newCard = dba.getAllSubtasks(my_data.get(position).getPkTask());
                        for(int i=0;i<newCard.size();i++){
                            if(newCard.get(i).getPk() == my_data.get(position).getPk()){
                                String title = newCard.get(i).getTitle();
                                my_data.get(position).setTitle(title);
                            }
                        }
                        if(subTaskTitle.getText().toString().equals("")){
                            my_data.remove(my_data.get(position));
                        }
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
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}