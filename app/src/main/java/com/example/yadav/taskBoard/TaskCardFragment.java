package com.example.yadav.taskBoard;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.libreerp.Helper;
import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;
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

public class TaskCardFragment extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static List<String> arraySubTask = new ArrayList<>();
    private static ListView listView;
    static int myPosition = 0;
    static int myPK = 0;
    static int assignee = 0;
    static int responsible = 0;
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
                    final DBHandler db = new DBHandler(getActivity(), null, null, 1);

                    assignee = db.getAssignee(myPK);
                    responsible = db.getResponsible(myPK);

                    rootView = inflater.inflate(R.layout.fragment_info, container, false);
                    context = getActivity().getApplicationContext();
                    Helper helper = new Helper(context);
                    serverURL = helper.serverURL;
                    client = helper.getHTTPClient();

                    TextView description = (TextView) rootView.findViewById(R.id.description2);
                    description.setText(db.databasetostring(myPosition));

                    TextView title = (TextView) rootView.findViewById(R.id.title);
                    title.setText(db.getTitle(myPosition));

                    TextView created = (TextView) rootView.findViewById(R.id.createdDate);

                    Date dateCreated = db.getCreated(myPosition);
                    Date current = new Date();
                    String formattedCreatedDate;
                    if(dateCreated.getYear() == current.getYear()) {
                        formattedCreatedDate = new SimpleDateFormat("dd MMM").format(dateCreated);
                    }
                    else {
                        formattedCreatedDate = new SimpleDateFormat("dd MMM, yyyy").format(dateCreated);
                    }

                    created.setText(formattedCreatedDate);

                    TextView dueDate = (TextView) rootView.findViewById(R.id.dueDateDate);
                    Date dateDueDate = db.getDueDate(myPosition);
                    String formattedDueDate;
                    if(dateDueDate.getYear() == current.getYear()) {
                        formattedDueDate = new SimpleDateFormat("dd MMM").format(dateDueDate);
                    }
                    else {
                        formattedDueDate = new SimpleDateFormat("dd MMM, yyyy").format(dateDueDate);
                    }
                    dueDate.setText(formattedDueDate);

                    final UserView responsibleImage = (UserView) rootView.findViewById(R.id.responsibledp);
                    responsibleImage.setUser(responsible);

                    final UserView assigneeImage = (UserView) rootView.findViewById(R.id.assigneedp);
                    assigneeImage.setUser(assignee);

                    db.getAllSubtasks(myPK);
                    listView = (ListView) rootView.findViewById(R.id.subtasks);
                    CustomSubTaskCardAdapter customSubTaskCardAdapter = new CustomSubTaskCardAdapter(context, R.layout.list_subtask);

                    for (int i = 0; i < arraySubTask.size(); i++) {
                        SubTaskCard card = new SubTaskCard(arraySubTask.get(i));
                        System.out.println("arraysubtask.get(i)==" + arraySubTask.get(i));
                        customSubTaskCardAdapter.add(card);
                        }


                    List<Integer> followers = db.getFollowers(myPosition);

                    int run = followers.size();
                    LinearLayout layoutList = (LinearLayout) rootView.findViewById(R.id.followers);
                if(run==0){
                    TextView followerText = (TextView) rootView.findViewById(R.id.follower);
                    followerText.setVisibility(View.INVISIBLE);
                }
                else {
                    for (int k = 0; k < run; k++) {
                        View followerView = inflater.inflate(R.layout.follower_list_view, container, false);
                        final UserView followersImage = (UserView) followerView.findViewById(R.id.followerslist);
                        followersImage.setUser(followers.get(k));
                        layoutList.addView(followerView);
                    }
                }
                    listView.setAdapter(customSubTaskCardAdapter);
                        break;

                }
                case 2: {
                    rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
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
                    Helper helper = new Helper(context);

                    client = helper.getHTTPClient();
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
                                String CommitDate;
                                String CommitBranch;
                                String CommitCode;

                                DBHandler dba = new DBHandler(getActivity(), null, null, 1);
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject c = response.getJSONObject(i);
                                    pkComment = c.getInt("pk");
                                    user = c.getInt("user");
                                    if (!dba.CheckIfCOMMENT_PKAlreadyInDBorNot(pkComment)) {
                                        created = c.getString("created").replace("Z","").replace("T"," ");
                                        category = c.getString("category");
                                        if (category.equals("message")) {
                                            text = c.getString("text");
                                            pkCommit = 0;
                                            commitMessage = null;
                                            CommitDate = null;
                                            CommitBranch = null;
                                            CommitCode = null;
                                        } else {
                                            text = null;
                                            JSONObject commit = c.getJSONObject("commit");
                                            JSONObject repo = commit.getJSONObject("repo");
                                            pkCommit = commit.getInt("pk");
                                            commitMessage = commit.getString("message");
                                            CommitBranch = repo.getString("name") + "/" + commit.getString("branch");
                                            CommitCode = commit.getString("sha");
                                            CommitDate = commit.getString("created").replace("Z","").replace("T"," ");
                                        }
                                        pkTask = c.getInt("task");
                                        Comment comment = new Comment(pkComment);
                                        comment.setPkComment(pkComment);
                                        comment.setPkTask(pkTask);
                                        comment.setUserPK(user);
                                        comment.setCategory(category);
                                        comment.setCreated(created);
                                        comment.setText(text);
                                        comment.setCommitPK(pkCommit);
                                        comment.setCommitMessage(commitMessage);
                                        comment.setCommitBranch(CommitBranch);
                                        comment.setCommitCode(CommitCode);
                                        comment.setCommitDate(CommitDate);
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
                            load_data_from_database(0);
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

                final DBHandler dba = new DBHandler(mainContext, null, null, 1);
                //System.out.println("pkTask = "+pkTask);

                for (int i = 0; i < dba.getTotalDBEntries_COMMENT(myPK); i++) {
                    final Comment data = new Comment(i);
                    data.setCategory(dba.getCategory(i,myPK));
                    int comment_pk = dba.getCommentPK(i,myPK);
                    Date commentDate = dba.getCommentDate(comment_pk);
                    String formattedCommitDate;
                    Date current = new Date();
                    if(commentDate.getYear() == current.getYear()) {
                        formattedCommitDate = new SimpleDateFormat("dd MMM").format(commentDate);
                    }
                    else {
                        formattedCommitDate = new SimpleDateFormat("dd MMM, yyyy").format(commentDate);
                    }

                    data.setCreated(formattedCommitDate);

                    if(dba.getCategory(i,myPK).equals("message")) {
                        data.setText(dba.getMessage(comment_pk));
                    }
                    else {
                        data.setText(dba.getCommitMessage(comment_pk));
                        data.setCommitBranch(dba.getCommitBranch(comment_pk));

                        Date commitDate = dba.getCommitDate(comment_pk);
                        if(commitDate.getYear() == current.getYear()) {
                            formattedCommitDate = new SimpleDateFormat("dd MMM").format(commitDate);
                        }
                        else {
                            formattedCommitDate = new SimpleDateFormat("dd MMM, yyyy").format(commitDate);
                        }
                        data.setCommitDate(formattedCommitDate);
                        data.setCommitCode(dba.getCommitCode(comment_pk));
                    }
                    Users users = new Users(context);
                    // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));
                    users.get(dba.getPostUserPK(comment_pk) , new UserMetaHandler(){
                        @Override
                        public void onSuccess(UserMeta user){
                            System.out.println("yes65262626626");
                            data.setUser(user.getFirstName() + " " + user.getLastName());
                            // set text in the layout here
                        }
                        @Override
                        public void handleDP(Bitmap dp){
                            System.out.println("dp dsda");
                            data.setDpUser(dp);
                            // set text in the layout here
                        }

                    });
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
