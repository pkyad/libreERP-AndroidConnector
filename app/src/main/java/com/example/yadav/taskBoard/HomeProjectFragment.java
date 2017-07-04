package com.example.yadav.taskBoard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    List<File> files_project = new ArrayList<>();
    List<Integer> followers;
    int RecyclerViewItemPosition ;
    View ChildView ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DBHandler dba=new DBHandler(getActivity(),null,null,1);
        myView = inflater.inflate(R.layout.fragment_home_sub_project, container, false);
        mainContext = myView.getContext();
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) myView.findViewById(R.id.projects_view);
        data_list = new ArrayList<>();


        gridLayoutManager = new GridLayoutManager(mainContext, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomProjectViewAdapter(mainContext, data_list);
        recyclerView.setAdapter(adapter);

        //http://pradeepyadav.net/api/taskBoard/task/?&title__contains=&limit=9&offset=0&assignee=0&follower=0&orderBy=created:true&responsible=1

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
                                    File file1 = new File(pk_project);
                                    file1.setPkTask(pk_project);
                                    file1.setFilePk(file.getInt("pk"));
                                    file1.setFileLink(file.getString("link"));
                                    file1.setAttachment(file.getString("attachment"));
                                    file1.setMediaType(file.getString("mediaType"));
                                    file1.setName(file.getString("name"));
                                    file1.setPostedUser(file.getInt("user"));
                                    file1.setFileCreated(file.getString("created"));
                                    dba.insetTableFiles(file1);
                                }
                            }

                            JSONArray teammembers = c.getJSONArray("team");
                            int[] team = new int[teammembers.length()];
                            for (int j=0;j<team.length;j++){
                                team[j] = teammembers.getInt(j);
                            }

                            int assignee = c.getInt("user");

                            created_project = (c.getString("created")).replace("Z","").replace("T"," ");
                            dueDate_project = (c.getString("dueDate")).replace("Z","").replace("T"," ");

                            Projects projects1 = new Projects(i);
                            projects1.setPk(pk_project);
                            projects1.setDescription(description_project);
                            projects1.setCreated(created_project);
                            projects1.setDueDate(dueDate_project);
                            projects1.setTitle(title_project);
                            projects1.setFiles(files);
                            projects1.setTeam(team);
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

        return myView;
    }

    private void load_data_from_server(int id) {

        final AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                DBHandler dba = new DBHandler(getActivity(),null,null,1);

                for (int i = 0; i < dba.getTotalDBEntries_PROJECTS(); i++) {

                    Projects projects = new Projects(i);
                    projects.setTitle(dba.getTitleProject(i));
                    projects.setDescription(dba.getDescriptionProject(i));
                    List<Integer> team = dba.getTeamProject(i);
                    int[] teamMembers = new int[team.size()];
                    for (int j=0;j<team.size();j++){
                        teamMembers[j] = team.get(j);
                    }
                    projects.setTeam(teamMembers);
//                    projects.setFiles(dba.getProjectFiles());
                    data_list.add(projects);
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

}

