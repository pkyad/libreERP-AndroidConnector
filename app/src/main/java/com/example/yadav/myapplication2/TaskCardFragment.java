package com.example.yadav.myapplication2;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TaskCardFragment extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static List<String> arraySubTask = new ArrayList<>();
    private static ListView listView;
    static int myPosition = 0;
    static int myPK = 0;
    static int assignee = 0;
    static int responsible = 0;
    static ArrayList<Integer> users = new ArrayList<Integer>();
    static View myView;
    //    private FragmentManager fragmentManager;
    private static RecyclerView recyclerView;
    private static GridLayoutManager gridLayoutManager;
    private static CustomTaskViewAdapterTimeline adapter;
    static int RecyclerViewItemPosition;
    static Context mainContext;
    static View ChildView;
    private static List<Comment> data_list;
    private static String serverURL;
    static AsyncHttpClient client;
    static Context context;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskcardfragment);

        myPosition = getIntent().getIntExtra("EXTRA_SESSION_ID", myPosition);
        myPK = getIntent().getIntExtra("PK_TASK", myPK);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1: {
                    arraySubTask.clear();
                    final dbhandler db = new dbhandler(getActivity(), null, null, 2);

                    assignee = db.getAssignee(myPK);
                    responsible = db.getResponsible(myPK);
                    users.add(assignee);
                    users.add(responsible);


                    rootView = inflater.inflate(R.layout.fragment_info, container, false);

                    /////////////////////////////////////

                    context = getActivity().getApplicationContext();
                    JSONObject settJson = MainActivity.getSettingsJson(context);
                    try {
                        serverURL = settJson.getString("domain");
                    } catch (JSONException e) {
                        System.out.println("Error while getting the domain from settings");
                    }
                    client = MainActivity.getHTTPClient(context);
                    for(int i=0;i<users.size();i++) {

                        String url1 = String.format("%s/%s/%s/", serverURL, "/api/HR/userSearch", users.get(i));

                        if (!db.CheckIfUserIsInDatabase(users.get(i))) {

//                    System.out.println("url = "+ url1);
                            client.get(url1, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject c) {
                                    System.out.println("success 001");
                                    try {
//                    JSONArray tasks = response.getJSONArray("results");

                                        // looping through All tasks


                                        int userpk;
                                        String username;
                                        String firstName;
                                        String lastName;
                                        int designation;
                                        int social;
                                        String displaypicture;

                                        userpk = c.getInt("pk");
                                        username = c.getString("username");
                                        firstName = c.getString("first_name");
                                        lastName = c.getString("last_name");
                                        designation = c.getInt("designation");
                                        social = c.getInt("social");
                                        JSONObject profile = c.getJSONObject("profile");
                                        displaypicture = profile.getString("displayPicture");

                                        final Users users = new Users(userpk);
                                        users.setDesignation(designation);
                                        users.setDisplayPicture(displaypicture);
                                        users.setFirstName(firstName);
                                        users.setLastName(lastName);
                                        users.setPkUsers(userpk);
                                        users.setSocial(social);
                                        users.setUsername(username);

                                        client.get(displaypicture, new FileAsyncHttpResponseHandler(context) {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, File file) {
                                                // Do something with the file `response`
                                                Bitmap pp = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                users.setProfilePicture(pp);
                                                users.saveUserToFile(context);
                                            }
                                            @Override
                                            public void onFailure(int statusCode, Header[] headers,Throwable e, File file) {
                                                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                                System.out.println("failure");
                                                System.out.println(statusCode);
                                            }
                                        });

                                        db.insertTableUsers(users);



                                        // set it here
                                        TextView responsible1 = (TextView) rootView.findViewById(R.id.responsiblename);

                                        responsible1.setText(db.getUser(responsible));

                                        TextView assignee1 = (TextView) rootView.findViewById(R.id.assigneename);

                                        assignee1.setText(db.getUser(assignee));

                                        System.out.println("Inserted users successfully");
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
                        }
                    }
                        users.clear();

                    ////////////////////////////////////////


                    TextView description = (TextView) rootView.findViewById(R.id.description2);

                    description.setText(db.databasetostring(myPosition));

                    TextView title = (TextView) rootView.findViewById(R.id.title);

                    title.setText(db.getTitle(myPosition));

                    TextView created = (TextView) rootView.findViewById(R.id.created);

                    created.setText(created.getText().toString() + " " + db.getCreated(myPosition));

                    TextView dueDate = (TextView) rootView.findViewById(R.id.dueDate);

                    dueDate.setText(dueDate.getText().toString() + " " + db.getDueDate(myPosition));

                    TextView responsible1 = (TextView) rootView.findViewById(R.id.responsiblename);

                    responsible1.setText(db.getUser(responsible));

                    TextView assignee1 = (TextView) rootView.findViewById(R.id.assigneename);



                    assignee1.setText(db.getUser(assignee));
                    db.getAllContacts(myPK);
                    listView = (ListView) rootView.findViewById(R.id.subtasks);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.list_subtask, arraySubTask);

                    listView.setAdapter(adapter);


                    break;
                }
                case 2: {
                    rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

                    /////////////////////////////////////


                    myView = inflater.inflate(R.layout.fragment_timeline_data, container, false);
                    mainContext = myView.getContext();

                    recyclerView = (RecyclerView) myView.findViewById(R.id.msgTexts);
                    data_list = new ArrayList<>();
                    gridLayoutManager = new GridLayoutManager(mainContext, 1);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    adapter = new CustomTaskViewAdapterTimeline(mainContext, data_list);
                    recyclerView.setAdapter(adapter);

                    ///////////////////
                    context = getActivity().getApplicationContext();
                    JSONObject settJson = MainActivity.getSettingsJson(context);
                    try {
                        serverURL = settJson.getString("domain");
                    } catch (JSONException e) {
                        System.out.println("Error while getting the domain from settings");
                    }


                    client = MainActivity.getHTTPClient(context);
                    RequestParams params = new RequestParams();
                    params.put("task", myPK);
                    String url = String.format("%s/%s/", serverURL, "/api/taskBoard/timelineItem");
                    client.get(url,params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers,response);

                            System.out.println("success 001xzc");
                            try {
//                                JSONArray response = tasks.getJSONArray("results");
                                int pkComment;
                                int pkTask;
                                String created;
                                String category;
                                String text;
                                int pkCommit;
                                String commitMessage;
                                int user;

                                dbhandler dba = new dbhandler(getActivity(), null, null, 3);
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject c = response.getJSONObject(i);
                                    pkComment = c.getInt("pk");
                                    user = c.getInt("user");
                                    if (!dba.CheckIfCOMMENT_PKAlreadyInDBorNot(pkComment)) {
                                        created = c.getString("created");
                                        category = c.getString("category");
                                        if (category.equals("message")) {
                                            text = c.getString("text");
                                            pkCommit = 0;
                                            commitMessage = null;
                                        } else {
                                            text = null;
                                            JSONObject commit = c.getJSONObject("commit");
                                            pkCommit = commit.getInt("pk");
                                            commitMessage = commit.getString("message");
                                        }
                                        pkTask = c.getInt("task");
                                        Comment comment = new Comment(pkComment);
                                        comment.setPkComment(pkComment);
                                        comment.setPkTask(pkTask);
                                        comment.setUser(dba.getUser(user));

                                        comment.setCategory(category);
                                        comment.setCreated(created);
                                        comment.setText(text);
                                        comment.setCommitPK(pkCommit);
                                        comment.setCommitMessage(commitMessage);
                                        dba.insertTableComment(comment);
                                    }
                                }
                                load_data_from_database(0);
                            } catch (final JSONException e) {
                                Log.e("TAG", "Json parsing error: " + e.getMessage());

                            }


                        }

                        @Override
                        public void onFinish() {
                            System.out.println("finished 001cxczdfhgfg");
                            // retrieve all the db entries

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            System.out.println("finished failed 001xczxc");
                        }
                    });


                    //////////////////////
                    recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                        GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                            @Override
                            public boolean onSingleTapUp(MotionEvent motionEvent) {

                                return true;
                            }

                        });

                        @Override
                        public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                            ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                            if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                                RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

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


                    ///////////////////////////////////////
                }

                case 3: {
                    rootView = inflater.inflate(R.layout.fragment_files, container, false);
                    break;
                }


            }
            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Info";
                case 1:
                    return "Timeline";
                case 2:
                    return "Files";
            }
            return null;
        }
    }


    private static void load_data_from_database(int id) {

        final AsyncTask<Integer, Void, Void> comment = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                dbhandler dba = new dbhandler(mainContext, null, null, 2);
                //System.out.println("pkTask = "+pkTask);

                for (int i = 0; i < dba.getTotalDBEntries_COMMENT(myPK); i++) {
                    final Comment data = new Comment(i);
                    data.setCategory(dba.getCategory(i,myPK));
                    int comment_pk = dba.getCommentPK(i,myPK);
                    data.setUser(dba.getPostUser(comment_pk));
                    if(dba.getCategory(i,myPK).equals("message")) {
                        data.setText(dba.getMessage(comment_pk));
                    }
                    else data.setText(dba.getCommitMessage(comment_pk));
                   // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));

                    File dpFile = new File(context.getFilesDir(),dba.getPostUser(comment_pk) );
                    File path = context.getFilesDir();
                    Bitmap dpBitmap = BitmapFactory.decodeFile(dpFile.getPath());
                    data.setDpUser(dpBitmap);

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

        comment.execute(id);
    }

}

