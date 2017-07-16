package com.example.yadav.taskBoard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.example.libreerp.Helper;
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
public class HomeProjectFragment extends Fragment {

    View myView;
    Context mainContext;
    private RecyclerView recyclerView;
    private List<Projects> data_list;
    private GridLayoutManager gridLayoutManager;
    private CustomProjectViewAdapter adapter;
    private String serverURL;
    int pk_project;
    String title_project = new String();
    String description_project = new String();
    String created_project = new String();
    String dueDate_project = new String();
    int RecyclerViewItemPosition ;
    View ChildView ;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DBHandler dba=new DBHandler(getActivity(),null,null,2);
        myView = inflater.inflate(R.layout.fragment_home_sub_project, container, false);
        mainContext = myView.getContext();
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) myView.findViewById(R.id.projects_view);
        data_list = new ArrayList<>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) myView.findViewById(R.id.swipeRefreshTaskBoard);

        gridLayoutManager = new GridLayoutManager(mainContext, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomProjectViewAdapter(mainContext, data_list);
        recyclerView.setAdapter(adapter);


        Context context = getActivity().getApplicationContext();
        Helper helper = new Helper(context);
        serverURL = helper.serverURL;
        AsyncHttpClient client = helper.getHTTPClient();;
        RequestParams params = new RequestParams();
        params.put("title__contains", "");
        params.put("limit",15);
        params.put("offset",0);


        String url = String.format("%s/%s/" , serverURL, "/api/projects/project" );
        client.get( url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success 001xzc");
                try {
                    JSONArray projects = response.getJSONArray("results");
                    //     System.out.println(response.length());
                    // looping through All tasks

                    for (int i = 0; i < projects.length(); i++) {
                        JSONObject c = projects.getJSONObject(i);

                        pk_project = c.getInt("pk");
                        if(!dba.CheckIfPK_ProjectAlreadyInDBorNot(pk_project)) {
                            title_project=(c.getString("title"));

                            description_project=(c.getString("description"));

                            JSONArray filesArray = c.getJSONArray("files");
                            int[] files = new int[filesArray.length()];
                            for (int j=0;j<files.length;j++){
                                JSONObject file = filesArray.getJSONObject(j);
                                files[j] = file.getInt("pk");
                                if(!dba.CheckIfFILE_PKAlreadyInDBorNot(files[j])) {
                                    Files file1 = new Files(pk_project);
                                    file1.setProject_pk(pk_project);
                                    file1.setFilePk(file.getInt("pk"));
                                    file1.setFileLink(file.getString("link"));
                                    file1.setAttachment(file.getString("attachment"));
                                    file1.setMediaType(file.getString("mediaType"));
                                    file1.setName(file.getString("name"));
                                    file1.setPostedUser(file.getInt("user"));
                                    file1.setFileCreated(file.getString("created").replace("Z","").replace("T"," "));
                                    dba.insetTableFiles(file1);
                                }
                            }

                            JSONArray teammembers = c.getJSONArray("team");
                            int[] team = new int[teammembers.length()];
                            for (int j=0;j<team.length;j++){
                                team[j] = teammembers.getInt(j);
                            }
                            JSONArray repos = c.getJSONArray("repos");
                            int reposCount = repos.length();
                            int user = c.getInt("user");

                            created_project = (c.getString("created")).replace("Z","").replace("T"," ");
                            dueDate_project = (c.getString("dueDate")).replace("Z","").replace("T"," ");

                            Projects projects1 = new Projects(i);
                            projects1.setPk(pk_project);
                            projects1.setDescription(description_project);
                            projects1.setCreated(created_project);
                            projects1.setUser(user);
                            projects1.setDueDate(dueDate_project);
                            projects1.setTitle(title_project);
                            projects1.setFiles(files);
                            projects1.setTeam(team);
                            projects1.setRepoCount(reposCount);
                            dba.insertTableProjects(projects1);
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


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
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
                    Intent intent = new Intent(getActivity(), ProjectCardActivity.class);
                    intent.putExtra("EXTRA_SESSION_ID", RecyclerViewItemPosition);
                    intent.putExtra("PROJECT",data_list.get(RecyclerViewItemPosition));
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

        return myView;
    }
    void refreshItems() {
        final DBHandler dba=new DBHandler(getActivity(),null,null,2);
        Context context = getActivity().getApplicationContext();
        Helper helper = new Helper(context);
        serverURL = helper.serverURL;
        AsyncHttpClient client = helper.getHTTPClient();;
        RequestParams params = new RequestParams();
        params.put("title__contains", "");
        params.put("limit",15);
        params.put("offset",0);


        String url = String.format("%s/%s/" , serverURL, "/api/projects/project" );
        client.get( url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dba.cleanProjects();
                System.out.println("success 001xzc");
                try {
                    JSONArray projects = response.getJSONArray("results");
                    //     System.out.println(response.length());
                    // looping through All tasks

                    for (int i = 0; i < projects.length(); i++) {
                        JSONObject c = projects.getJSONObject(i);

                        pk_project = c.getInt("pk");
                        if(!dba.CheckIfPK_ProjectAlreadyInDBorNot(pk_project)) {
                            title_project=(c.getString("title"));

                            description_project=(c.getString("description"));

                            JSONArray filesArray = c.getJSONArray("files");
                            int[] files = new int[filesArray.length()];
                            for (int j=0;j<files.length;j++){
                                JSONObject file = filesArray.getJSONObject(j);
                                files[j] = file.getInt("pk");
                                if(!dba.CheckIfFILE_PKAlreadyInDBorNot(files[j])) {
                                    Files file1 = new Files(pk_project);
                                    file1.setProject_pk(pk_project);
                                    file1.setFilePk(file.getInt("pk"));
                                    file1.setFileLink(file.getString("link"));
                                    file1.setAttachment(file.getString("attachment"));
                                    file1.setMediaType(file.getString("mediaType"));
                                    file1.setName(file.getString("name"));
                                    file1.setPostedUser(file.getInt("user"));
                                    file1.setFileCreated(file.getString("created").replace("Z","").replace("T"," "));
                                    dba.insetTableFiles(file1);
                                }
                            }

                            JSONArray teammembers = c.getJSONArray("team");
                            int[] team = new int[teammembers.length()];
                            for (int j=0;j<team.length;j++){
                                team[j] = teammembers.getInt(j);
                            }
                            JSONArray repos = c.getJSONArray("repos");
                            int reposCount = repos.length();
                            int user = c.getInt("user");

                            created_project = (c.getString("created")).replace("Z","").replace("T"," ");
                            dueDate_project = (c.getString("dueDate")).replace("Z","").replace("T"," ");

                            Projects projects1 = new Projects(i);
                            projects1.setPk(pk_project);
                            projects1.setDescription(description_project);
                            projects1.setCreated(created_project);
                            projects1.setUser(user);
                            projects1.setDueDate(dueDate_project);
                            projects1.setTitle(title_project);
                            projects1.setFiles(files);
                            projects1.setTeam(team);
                            projects1.setRepoCount(reposCount);
                            dba.insertTableProjects(projects1);
                        }
                    }
                    adapter.clearData();
                    load_data_from_server(0);
                    onItemsLoadComplete();
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
                adapter.clearData();
                load_data_from_server(0);
                onItemsLoadComplete();
                System.out.println("finished failed 001xczxc");
            }
        });
    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void load_data_from_server(int id) {

        final AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                DBHandler dba = new DBHandler(getActivity(),null,null,2);
                List<Projects> data = dba.getAllProjectList();
                for (int i = 0; i < data.size(); i++) {
                    data_list.add(data.get(i));
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };

        task.execute(id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        MenuItem homeSort = menu.getItem(0);
        homeSort.setVisible(false);
        MenuItem itemSort = menu.getItem(1);
        itemSort.setVisible(false);
        MenuItem itemFilter = menu.getItem(2);
        itemFilter.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

}

