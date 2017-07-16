package com.example.yadav.taskBoard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.libreerp.Helper;
import com.example.libreerp.User;
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
public class HomeToDoFragment extends Fragment {

    public HomeToDoFragment(){

    }
public boolean removeFilter = false;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomTaskViewAdapter adapter;
    private List<Task> data_list;
    private String serverURL;
    int pk;
    String title = new String();
    String description = new String();
    String created = new String();
    String dueDate = new String();
    int completion;
    View ChildView ;
    int RecyclerViewItemPosition ;
    View myView;
    int PK_Project = 0;
    Context mainContext;
    boolean isResponsible = true;
    boolean isAssignee = false;
    boolean isFollower = false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int filterCount=0;
    User usr;
    List<Task> data;
    LayoutInflater inflater1 = null;
    ViewGroup container1 = null;
    DBHandler dba1 = null;
    View filterCardView = null;
    int initialCount;
    AsyncHttpClient client;
    AsyncTask<Integer, Void, Void> task;
    private void presentFilterSettingsDialog(final Context context) {

        final CharSequence[] items = {"Follower", "Assignee", "Responsible"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Filter");


        final ArrayList<Integer> seletedItems = new ArrayList<>();
        final boolean[] items_checked = new boolean[3];
        if(isAssignee)
            items_checked[1] = true;
        else items_checked[1] = false;
        if (isResponsible)
            items_checked[2] = true;
        else items_checked[2] = false;
        if (isFollower)
            items_checked[0] = true;
        else items_checked[0] = false;

        for(int i =0;i<3;i++){
            if(items_checked[i]){
                seletedItems.add(i);
            }
        }

        builder.setMultiChoiceItems(items, items_checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected,boolean isChecked) {
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
                isResponsible = false;
                isFollower = false;
                isAssignee = false;
                for (int i=0;i<seletedItems.size();i++){
                    if ((items[seletedItems.get(i)].equals("Follower")))
                        isFollower = true;
                    else if((items[seletedItems.get(i)].equals("Assignee")))
                        isAssignee = true;
                    else if ((items[seletedItems.get(i)].equals("Responsible")))
                        isResponsible = true;
                }
            setFilters(inflater1,container1,dba1);

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
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        MenuItem homeSort = menu.getItem(0);
        homeSort.setVisible(false);
        MenuItem itemSort = menu.getItem(2);
        itemSort.setVisible(false);
        MenuItem itemFilter = menu.getItem(3);
        itemFilter.setVisible(true);
        MenuItem itemSearch = menu.getItem(1);
        SearchView searchview = (SearchView) itemSearch.getActionView();

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                ArrayList<Task>  filterData = new ArrayList<Task>();
                for (int i = 0 ; i < data.size() ; i++){
                    String taskTitle = data.get(i).getTitle();
                    if (taskTitle.contains(query)){
                        filterData.add(data.get(i));
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new CustomTaskViewAdapter(mainContext,filterData);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(!newText.equals("")) {
                    String query = newText.toLowerCase();
                    System.out.print(query);
                    ArrayList<Task> filterData = new ArrayList<Task>();
                    for (int i = 0; i < data.size(); i++) {
                        String chatRoomName = data.get(i).getTitle();
                        if (chatRoomName.toLowerCase().contains(query)) {
                            System.out.print("query is  " + chatRoomName + " \n");
                            filterData.add(data.get(i));
                        }
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new CustomTaskViewAdapter(mainContext, filterData);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    return false;
                }
                else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new CustomTaskViewAdapter(mainContext,data);
                    setFilters(inflater1,container1,dba1);
                    return false;
                }
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                presentFilterSettingsDialog(mainContext);
                return true;
            case R.id.action_sort:
                return false;
            default:
                break;
        }

        return false;
    }

    private void setFilters(final LayoutInflater inflater, final ViewGroup container,DBHandler dba){
        filterCount = 0;

        if(removeFilter){
            PK_Project = 0;
        }
        if(PK_Project!=0){
            filterCount++;
        }
        if(isResponsible){
            filterCount++;
        }
        if(isAssignee){
            filterCount++;
        }
        if(isFollower){
            filterCount++;
        }

        final LinearLayout filter = (LinearLayout) myView.findViewById(R.id.filter);
        filter.removeAllViews();
        if(isResponsible){
            filterCardView = inflater.inflate(R.layout.filter_view, container, false);
            final LinearLayout layout = (LinearLayout) filterCardView.findViewById(R.id.filterView);
            final TextView text = (TextView) filterCardView.findViewById(R.id.filterName);
            Button delete = (Button) filterCardView.findViewById(R.id.deleteFilter);
            delete.setOnClickListener(getOnClickDoSomething(layout,text,filter));
            text.setText("Responsible");
            filter.addView(filterCardView);
        }

        if (PK_Project != 0) {
            filterCardView = inflater.inflate(R.layout.filter_view, container, false);
            final TextView text = (TextView) filterCardView.findViewById(R.id.filterName);
            final LinearLayout layout = (LinearLayout) filterCardView.findViewById(R.id.filterView);

            Button delete = (Button) filterCardView.findViewById(R.id.deleteFilter);
            delete.setOnClickListener(getOnClickDoSomething(layout,text,filter));
            text.setText("Project: " + dba.getTitleProjectFormPK(PK_Project));
            filter.addView(filterCardView);
        }
        if(isFollower){
            filterCardView = inflater.inflate(R.layout.filter_view, container, false);
            final TextView text = (TextView) filterCardView.findViewById(R.id.filterName);
            Button delete = (Button) filterCardView.findViewById(R.id.deleteFilter);
            final LinearLayout layout = (LinearLayout) filterCardView.findViewById(R.id.filterView);
            delete.setOnClickListener(getOnClickDoSomething(layout,text,filter));
            text.setText("Follower");
            filter.addView(filterCardView);
        }
        if (isAssignee){
            filterCardView = inflater.inflate(R.layout.filter_view, container, false);
            final TextView text = (TextView) filterCardView.findViewById(R.id.filterName);
            final LinearLayout layout = (LinearLayout) filterCardView.findViewById(R.id.filterView);
            Button delete = (Button) filterCardView.findViewById(R.id.deleteFilter);
            delete.setOnClickListener(getOnClickDoSomething(layout,text,filter));
            text.setText("Assignee");
            filter.addView(filterCardView);
        }
        if(filterCount!=0){
            filter.setVisibility(View.VISIBLE);
        }

        adapter.clearData();
        load_data_from_server(0);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        myView = inflater.inflate(R.layout.fragment_home_sub_todo, container, false);
        mainContext = myView.getContext();
        usr = User.loadUser(mainContext);
        setHasOptionsMenu(true);
        final DBHandler dba=new DBHandler(getActivity(),null,null,2);
        recyclerView = (RecyclerView) myView.findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle!=null)
            PK_Project = bundle.getInt("PK_PROJECT");
        else PK_Project = 0;

        inflater1 = inflater;
        container1 = container;
        dba1 = dba;
        mSwipeRefreshLayout = (SwipeRefreshLayout) myView.findViewById(R.id.swipeRefreshTaskBoard);
        initialCount = dba.getTotalDBEntries_TASK();
        if(initialCount==0)
            isResponsible = false;
        else isResponsible = true;
        gridLayoutManager = new GridLayoutManager(mainContext, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomTaskViewAdapter(mainContext, data_list);
        recyclerView.setAdapter(adapter);

        //http://pradeepyadav.net/api/taskBoard/task/?&title__contains=&limit=9&offset=0&assignee=0&follower=0&orderBy=created:true&responsible=1

        Context context = getActivity().getApplicationContext();
        Helper helper = new Helper(context);
        serverURL = helper.serverURL;


        setFilters(inflater1,container1,dba1);
        client = helper.getHTTPClient();
        RequestParams params = new RequestParams();
        params.put("title__contains", "");
        params.put("limit",150);
        params.put("offset",0);
        params.put("assignee",1);
        params.put("created",true);
        params.put("responsible",1);

        final String url = String.format("%s/%s/" , serverURL, "/api/taskBoard/task" );
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

                        pk = c.getInt("pk");
                        if(!dba.CheckIfPKAlreadyInDBorNot(pk)) {
                            title=(c.getString("title"));

                            description=(c.getString("description"));

                            JSONArray filesArray = c.getJSONArray("files");
                            int[] files = new int[filesArray.length()];
                            for (int j=0;j<files.length;j++){
                                JSONObject file = filesArray.getJSONObject(j);
                                files[j] = file.getInt("pk");
                                if(!dba.CheckIfFILE_PKAlreadyInDBorNot(files[j])) {
                                    Files file1 = new Files(pk);
                                    file1.setPkTask(pk);
                                    file1.setFilePk(file.getInt("pk"));
                                    file1.setProject_pk(0);
                                    file1.setFileLink(file.getString("link"));
                                    file1.setAttachment(file.getString("attachment"));
                                    file1.setMediaType(file.getString("mediaType"));
                                    file1.setName(file.getString("name"));
                                    file1.setPostedUser(file.getInt("user"));
                                    file1.setFileCreated((file.getString("created")).replace("Z","").replace("T"," "));
                                    dba.insetTableFiles(file1);
                                }
                            }

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
                            int pk_project = project.getInt("pk");

                            boolean personal = c.getBoolean("personal");

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
                            task.setFiles(files);
                            task.setPk_project(pk_project);
                            task.setPersonal(personal);
                            dba.insertTableMain(task);


                        }

                    }

                    if(initialCount == 0){
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content_frame_home,new HomeHomeFragment())
                                .commit();
                    }
                    else
                        setFilters(inflater1,container1,dba1);
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
                if(initialCount == 0){
                    initialCount = dba.getTotalDBEntries_TASK();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame_home,new HomeHomeFragment())
                            .commit();
                }
                else
                    setFilters(inflater1,container1,dba1);
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


//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//
//            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//
//                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
//                    return true;
//                }
//
//            });
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
//
//                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
//
//                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
//
//                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
//
//                    Intent intent = new Intent(getActivity(), TaskCardActivity.class);
//                    intent.putExtra("EXTRA_SESSION_ID", RecyclerViewItemPosition);
//                    intent.putExtra("TASK",data_list.get(RecyclerViewItemPosition));
//                    startActivity(intent);
//                }
//
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });


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
        task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                DBHandler dba = new DBHandler(getContext(),null,null,2);
                data = dba.getAllTasksList();
                for (int i = 0; i < data.size(); i++) {
                    if(PK_Project!=0) {
                        if (data.get(i).getPk_project() == PK_Project) {

                            int followers[] = data.get(i).getFollowers();
                            boolean flag_follower = false;
                            for (int m=0;m<followers.length;m++){
                                if(usr.getPk() == followers[m])
                                    flag_follower = true;
                            }
                            if(isResponsible && !isAssignee && !isFollower) {
                                if (usr.getPk().equals(data.get(i).getResponsible())) {
                                    data_list.add(data.get(i));
                                }
                            }
                            else if(isResponsible && isAssignee && !isFollower){
                                if(usr.getPk().equals(data.get(i).getResponsible()) || usr.getPk().equals(data.get(i).getAssignee())){
                                    data_list.add(data.get(i));
                                }
                            }
                            else if(isResponsible && isFollower && isAssignee){
                                if(usr.getPk().equals(data.get(i).getResponsible()) || usr.getPk().equals(data.get(i).getAssignee()) || flag_follower ){
                                    data_list.add(data.get(i));
                                }
                            }
                            else if(!isResponsible && isAssignee && !isFollower){
                                if(usr.getPk().equals(data.get(i).getAssignee())){
                                    data_list.add(data.get(i));
                                }
                            }
                            else if(isAssignee && isFollower && !isResponsible){
                                if(usr.getPk().equals(data.get(i).getAssignee()) || flag_follower){
                                    data_list.add(data.get(i));
                                }
                            }
                            else if(isFollower && !isResponsible && !isAssignee){
                                if(flag_follower){
                                    data_list.add(data.get(i));
                                }
                            }
                            else if(isFollower && isResponsible && !isAssignee){
                                if(flag_follower || usr.getPk().equals(data.get(i).getResponsible())){
                                    data_list.add(data.get(i));
                                }
                            }
                        }
                    }
                    else {
                        int followers[] = data.get(i).getFollowers();
                        boolean flag_follower = false;
                        for (int m=0;m<followers.length;m++){
                            if(usr.getPk() == followers[m])
                                flag_follower = true;
                        }
                        if(isResponsible && !isAssignee && !isFollower) {
                            if (usr.getPk().equals(data.get(i).getResponsible())) {
                                data_list.add(data.get(i));
                            }
                        }
                        else if(isResponsible && isAssignee && !isFollower){
                            if(usr.getPk().equals(data.get(i).getResponsible()) || usr.getPk().equals(data.get(i).getAssignee())){
                                data_list.add(data.get(i));
                            }
                        }
                        else if(isResponsible && isFollower && isAssignee){
                            if(usr.getPk().equals(data.get(i).getResponsible()) || usr.getPk().equals(data.get(i).getAssignee()) || flag_follower ){
                                data_list.add(data.get(i));
                            }
                        }
                        else if(!isResponsible && isAssignee && !isFollower){
                            if(usr.getPk().equals(data.get(i).getAssignee())){
                                data_list.add(data.get(i));
                            }
                        }
                        else if(isAssignee && isFollower && !isResponsible){
                            if(usr.getPk().equals(data.get(i).getAssignee()) || flag_follower){
                                data_list.add(data.get(i));
                            }
                        }
                        else if(isFollower && !isResponsible && !isAssignee){
                            if(flag_follower){
                                data_list.add(data.get(i));
                            }
                        }
                        else if(isFollower && isResponsible && !isAssignee){
                            if(flag_follower || usr.getPk().equals(data.get(i).getResponsible())){
                                data_list.add(data.get(i));
                            }
                        }
                    }
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

    void refreshItems() {
        final DBHandler dba = new DBHandler(getActivity(),null,null,2);
        Context context = getActivity().getApplicationContext();
        Helper helper = new Helper(context);
        serverURL = helper.serverURL;
        AsyncHttpClient client = helper.getHTTPClient();
        RequestParams params = new RequestParams();
        params.put("title__contains", "");
        params.put("limit",150);
        params.put("offset",0);
        params.put("assignee",1);
        params.put("created",true);
        params.put("responsible",1);

        final String url = String.format("%s/%s/" , serverURL, "/api/taskBoard/task" );
        client.get( url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dba.cleanTasks();
                dba.cleanSubTasks();
                dba.cleanFiles();
                System.out.println("success 001xzc");
                try {
                    JSONArray tasks = response.getJSONArray("results");
                    //     System.out.println(response.length());
                    // looping through All tasks

                    for (int i = 0; i < tasks.length(); i++) {
                        JSONObject c = tasks.getJSONObject(i);

                        pk = c.getInt("pk");
                        if(!dba.CheckIfPKAlreadyInDBorNot(pk)) {
                            title=(c.getString("title"));

                            description=(c.getString("description"));

                            JSONArray filesArray = c.getJSONArray("files");
                            int[] files = new int[filesArray.length()];
                            for (int j=0;j<files.length;j++){
                                JSONObject file = filesArray.getJSONObject(j);
                                files[j] = file.getInt("pk");
                                if(!dba.CheckIfFILE_PKAlreadyInDBorNot(files[j])) {
                                    Files file1 = new Files(pk);
                                    file1.setPkTask(pk);
                                    file1.setFilePk(file.getInt("pk"));
                                    file1.setProject_pk(0);
                                    file1.setFileLink(file.getString("link"));
                                    file1.setAttachment(file.getString("attachment"));
                                    file1.setMediaType(file.getString("mediaType"));
                                    file1.setName(file.getString("name"));
                                    file1.setPostedUser(file.getInt("user"));
                                    file1.setFileCreated((file.getString("created")).replace("Z","").replace("T"," "));
                                    dba.insetTableFiles(file1);
                                }
                            }

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
                            int pk_project = project.getInt("pk");

                            boolean personal = c.getBoolean("personal");

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
                            task.setFiles(files);
                            task.setPk_project(pk_project);
                            task.setPersonal(personal);
                            dba.insertTableMain(task);
                        }

                    }

                    setFilters(inflater1,container1,dba1);
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
                setFilters(inflater1,container1,dba1);
                onItemsLoadComplete();
                System.out.println("finished failed 001xczxc");
            }
        });

    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
    View.OnClickListener getOnClickDoSomething(final LinearLayout layout, final TextView text, final LinearLayout filter)  {
        return new View.OnClickListener() {
            public void onClick(View v) {

                if (text.getText().toString().equals("Responsible")) {
                    isResponsible = false;
                    filterCount--;
                    adapter.clearData();

                    layout.setVisibility(View.GONE);
                    if (filterCount != 0) {
                        filter.setVisibility(View.VISIBLE);
                    } else filter.setVisibility(View.GONE);
                    load_data_from_server(0);
                }
                if (text.getText().toString().equals("Assignee")) {
                    isAssignee = false;
                    filterCount--;
                    adapter.clearData();
                    layout.setVisibility(View.GONE);
                    if (filterCount != 0) {
                        filter.setVisibility(View.VISIBLE);
                    } else filter.setVisibility(View.GONE);
                    load_data_from_server(0);
                }
                if (text.getText().toString().equals("Follower")) {
                    isFollower = false;
                    filterCount--;
                    adapter.clearData();

                    layout.setVisibility(View.GONE);
                    if (filterCount != 0) {
                        filter.setVisibility(View.VISIBLE);
                    } else filter.setVisibility(View.GONE);
                    load_data_from_server(0);
                } else if (PK_Project != 0) {
                    PK_Project = 0;
                    filterCount--;
                    adapter.clearData();

                    layout.setVisibility(View.GONE);
                    if (filterCount != 0) {
                        filter.setVisibility(View.VISIBLE);
                    } else filter.setVisibility(View.GONE);
                    load_data_from_server(0);
                }
                if(filterCount==0){
                    TextView textView = (TextView) myView.findViewById(R.id.noFilterText);
                    textView.setVisibility(View.VISIBLE);
                }
                else if(filterCount!=0){
                    TextView textView = (TextView) myView.findViewById(R.id.noFilterText);
                    textView.setVisibility(View.GONE);
                }
            }
        };
    }
}
