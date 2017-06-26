package com.example.yadav.taskBoard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.libreerp.Helper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yadav on 10/3/17.
 */
public class HomeHomeFragment extends Fragment {

    public HomeHomeFragment(){

    }

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomTaskViewAdapter adapter;
    private List<Task> data_list;
    private String serverURL;


    int pk;
    String title = new String();
    String description = new String();
    String files = new String();

    String created = new String();
    String dueDate = new String();
    int completion;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    View ChildView ;
    int RecyclerViewItemPosition ;
    DBHandler dbhandler;
    View myView;
    View rootView;
    Context mainContext;
    ImageButton loginBtn;
    FloatingActionButton fab;
    private void presentFilterSettingsDialog(Context context) {

        final CharSequence[] items = {" Follower ", " Assignee ", " Responsible "};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Filter");

        final ArrayList seletedItems = new ArrayList();

        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    seletedItems.add(indexSelected);
                } else if (seletedItems.contains(indexSelected)) {
                    // Else, if the item is already in the array, remove it
                    seletedItems.remove(Integer.valueOf(indexSelected));
                }
            }
        });

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // handle ok
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setIcon(R.drawable.ic_action_filter_black);

        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        MenuItem homeSort = menu.getItem(0);
        homeSort.setVisible(false);
        MenuItem itemSort = menu.getItem(1);
        itemSort.setVisible(true);
        MenuItem itemFilter = menu.getItem(2);
        itemFilter.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                presentFilterSettingsDialog(mainContext);
                return false;
            case R.id.action_sort:
                return false;
            default:
                break;
        }

        return false;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DBHandler dba=new DBHandler(getActivity(),null,null,1);
        myView = inflater.inflate(R.layout.fragment_home_sub_home, container, false);
        mainContext = myView.getContext();
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) myView.findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();


        gridLayoutManager = new GridLayoutManager(mainContext, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomTaskViewAdapter(mainContext, data_list);
        recyclerView.setAdapter(adapter);






///////////////

        //http://pradeepyadav.net/api/taskBoard/task/?&title__contains=&limit=9&offset=0&assignee=0&follower=0&orderBy=created:true&responsible=1

        Context context = getActivity().getApplicationContext();
        Helper helper = new Helper(context);
        serverURL = helper.serverURL;





        AsyncHttpClient client = helper.getHTTPClient();
        RequestParams params = new RequestParams();
        params.put("title__contains", "");
        params.put("limit",15);
        params.put("offset",0);
        params.put("assignee",1);
        params.put("created",true);
        params.put("responsible",1);

        String url = String.format("%s/%s/" , serverURL, "/api/taskBoard/task" );
        client.get( url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success 001xzc");
                try {
                    JSONArray tasks = response.getJSONArray("results");
               //     System.out.println(response.length());
                    // looping through All tasks

                    dba.cleanTasks();

                    for (int i = 0; i < tasks.length(); i++) {
                        JSONObject c = tasks.getJSONObject(i);

                        pk = c.getInt("pk");
                        if(!dba.CheckIfPKAlreadyInDBorNot(pk)) {
                            title=(c.getString("title"));

                            description=(c.getString("description"));


                            files  = (c.getString("files"));
                            JSONArray followersArray = c.getJSONArray("followers");
                            int[] followers = new int[followersArray.length()];
                            for (int j=0;j<followers.length;j++){
                                followers[j] = followersArray.getInt(j);
                            }

                            int assignee = c.getInt("user");

                            created = (c.getString("created")).replace("Z","").replace("T"," ");
                            dueDate = (c.getString("dueDate")).replace("Z","").replace("T"," ");
                            int responsible = c.getInt("to");

                            int pkSubTask;
                            String titleSubTask;
                            String statusSubTask;
                            // Phone node is JSON Object
                            JSONArray subTasks = c.getJSONArray("subTasks");
                            for (int j = 0; j < subTasks.length(); j++) {
                                JSONObject sub = subTasks.getJSONObject(j);

                                pkSubTask = sub.getInt("pk");
                                if(!dba.CheckIfSUB_PKAlreadyInDBorNot(pkSubTask)) {
                                    titleSubTask = sub.getString("title");

                                    statusSubTask = sub.getString("status");
                                    SubTask subTask = new SubTask(i);
                                    subTask.setPk(pkSubTask);
                                    subTask.setTitle(titleSubTask);
                                    subTask.setStatus(statusSubTask);
                                    subTask.setPkTask(pk);
                                    dba.insertTableSubtask(subTask);
                                }
                            }
                            JSONObject project = c.getJSONObject("project");
                            int pk2 = project.getInt("pk");

                            String title2 = project.getString("title");

                            String description2 = project.getString("description");

                            String personal = c.getString("personal");

                            completion = (c.getInt("completion"));
                            Task task = new Task(i);
                            task.setPk(pk);
                            task.setDescription(description);
                            task.setCompletion(completion);
                            task.setCreated(created);
                            task.setDueDate(dueDate);
                            task.setTitle(title);
                            task.setAssignee(assignee);
                            task.setResponsible(responsible);
                            task.setFollowers(followers);
                            dba.insertTableMain(task);
                        }
                    }
                    load_data_from_server(0);
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());

                }



            }
            @Override
            public void onFinish() {
                System.out.println("finished 001cxcz");
                // retrieve all the db entries

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                load_data_from_server(0);
                System.out.println("finished failed 001xczxc");
            }
        });






/////////////////


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    Intent intent = new Intent(getActivity(), TaskCardActivity.class);
                    intent.putExtra("EXTRA_SESSION_ID", RecyclerViewItemPosition);
                    intent.putExtra("PK_TASK",dba.getPK(RecyclerViewItemPosition));
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size() - 1) {
//                    load_data_from_server(data_list.get(data_list.size() - 1).getPk());
//                }
//
//            }
//        });
        return myView;
    }


    private void load_data_from_server(int id) {

        final AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                DBHandler dba = new DBHandler(getActivity(),null,null,1);

                for (int i = 0; i < dba.getTotalDBEntries_TASKVALUE(); i++) {

                    Task data = new Task(i);
                    data.setTitle(dba.getTitle(i));
                    data.setCompletion(dba.getcompletion(i));
                    data.setDescription(dba.getDescription(i));

                    Date dateDueDate = dba.getDueDate(i);
                    Date current = new Date();
                    String formattedDueDate;
                    if(dateDueDate.getYear() == current.getYear()) {
                        formattedDueDate = new SimpleDateFormat("dd MMM").format(dateDueDate);
                    }
                    else {
                        formattedDueDate = new SimpleDateFormat("dd MMM, yyyy").format(dateDueDate);
                    }

                    data.setDueDate(formattedDueDate);
                    data_list.add(data);
                }

                //
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder()
//                            .url("http://192.168.178.26/test/script.php?id="+integers[0])
//                            .build();
//                    try {
//                        Response response = client.newCall(request).execute();
//
//                        JSONArray array = new JSONArray(response.body().string());
//
//                        for (int i=0; i<array.length(); i++){
//
//                            JSONObject object = array.getJSONObject(i);
//
//                            MyData data = new MyData(object.getInt("id"),object.getString("description"),
//                                    object.getString("image"));
//
//                            data_list.add(data);
//                        }
//
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        System.out.println("End of content");
//                    }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };

        task.execute(id);
    }

//    @Override
//    public void taskcard(View view) {
//        Intent intent = new Intent(getActivity(), TaskCardActivity.class);
//        startActivity(intent);
//
//    }
//
//

}
