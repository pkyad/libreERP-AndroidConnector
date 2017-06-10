package com.example.yadav.myapplication2;

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


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


    List<Integer> pk = new ArrayList<Integer>();
    List<String> title = new ArrayList<String>();
    List<String> description = new ArrayList<String>();
    List<String> files = new ArrayList<String>();
    List<String> followers = new ArrayList<String>();
    List<String> created = new ArrayList<String>();
    List<String> dueDate = new ArrayList<String>();
    List<Integer> completion = new ArrayList<Integer>();
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    View ChildView ;
    int RecyclerViewItemPosition ;
    dbhandler dbhandler;
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
        final dbhandler dba=new dbhandler(getActivity(),null,null,2);
        myView = inflater.inflate(R.layout.fragment_home_sub_home, container, false);
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
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
        JSONObject settJson = MainActivity.getSettingsJson(context);
        try{
            serverURL = settJson.getString("domain");
        }catch (JSONException e){
            System.out.println("Error while getting the domain from settings");
        }





        AsyncHttpClient client = MainActivity.getHTTPClient(context);
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



                    for (int i = 0; i < tasks.length(); i++) {
                        JSONObject c = tasks.getJSONObject(i);

                        pk.add(c.getInt("pk"));
                        if(!dba.CheckIfPKAlreadyInDBorNot(pk.get(i))) {
                            title.add(c.getString("title"));

                            description.add(c.getString("description"));


                            files.add(c.getString("files"));

                        //    followers.add(c.getString("followers"));

                            int assignee = c.getInt("user");

                            created.add(c.getString("created"));

                            dueDate.add(c.getString("dueDate"));

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
                                    subTask.setPkTask(pk.get(i));
                                    dba.insertTableSubtask(subTask);
                                }
                            }
                            JSONObject project = c.getJSONObject("project");
                            int pk2 = project.getInt("pk");

                            String title2 = project.getString("title");

                            String description2 = project.getString("description");

                            String personal = c.getString("personal");

                            completion.add(c.getInt("completion"));
                            Task task = new Task(i);
                            task.setPk(pk.get(i));
                            task.setDescription(description.get(i));
                            task.setCompletion(completion.get(i));
                            task.setCreated(created.get(i));
                            task.setDueDate(dueDate.get(i));
                            task.setTitle(title.get(i));
                            task.setAssignee(assignee);
                            task.setResponsible(responsible);
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

                    Intent intent = new Intent(getActivity(), TaskCardFragment.class);
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

                dbhandler dba = new dbhandler(getActivity(),null,null,2);

                for (int i = 0; i < dba.getTotalDBEntries_TASKVALUE(); i++) {

                    Task data = new Task(i);

                    data.setDescription(dba.getTitle(i));
                    data.setCompletion(dba.getcompletion(i));
//                    data.setPk(pk.get(i));
//                    data.setCreated(created.get(i));
//                    data.setDueDate(dueDate.get(i));
                   // data.setDescription(dString);S

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
//        Intent intent = new Intent(getActivity(), TaskCardFragment.class);
//        startActivity(intent);
//
//    }
//
//

}
