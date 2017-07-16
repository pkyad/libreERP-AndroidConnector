package com.example.yadav.taskBoard;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libreerp.Helper;
import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.UserViewBS;
import com.example.libreerp.Users;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class TaskCardActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static List<String> arraySubTask = new ArrayList<>();

    static int myPosition = 0;
    static Task task;
    static int assignee = 0;
    static int responsible = 0;
    static View myView;
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
    static SwipeRefreshLayout mSwipeRefreshLayout;

    static RecyclerView subTaskListView;
    static CustomSubTaskCardAdapter customSubTaskCardAdapter;
    private static final int CAMERA_REQUEST = 1024;
    private static final int GALLERY_REQUEST = 2134;
    private static final int CHOOSE_FILE_REQUESTCODE = 4512;
    static List<SubTask> subTasksList;
    private static FloatingActionMenu menuRed;
    static CustomFileCardAdapter customFileCardAdapter;

    static int pkComment;
    static int pkTask;
    static String created;
    static String category;
    static String text;
    static int pkCommit;
    static String commitMessage;
    static int user;
    static String CommitDate;
    static String CommitBranch;
    static String CommitCode;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsubtask, menu);
        return false;
    }

    private void newSubTask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText subTaskTitle;
        builder.setTitle("New Sub Task");
        subTaskTitle = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        subTaskTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        subTaskTitle.setTextColor(getResources().getColor(R.color.black));
        subTaskTitle.setMinLines(1);
        builder.setView(subTaskTitle);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Helper helper = new Helper(context);
                AsyncHttpClient client = helper.getHTTPClient();
                final DBHandler dba = new DBHandler(context,null,null,2);
                RequestParams params = new RequestParams();
                params.put("title",subTaskTitle.getText().toString());
                params.put("status", "notStarted");
                params.put("task",task.getPk());
                final String url = String.format("%s/%s/", helper.serverURL, "/api/taskBoard/subTask");
                client.post(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            int pkSubTask = response.getInt("pk");

                            String titleSubTask = response.getString("title");

                            String statusSubTask = response.getString("status");

                            SubTask subTask = new SubTask(pkSubTask);
                            subTask.setPk(pkSubTask);
                            subTask.setTitle(titleSubTask);
                            subTask.setStatus(statusSubTask);
                            subTask.setPkTask(task.getPk());
                            dba.insertTableSubtask(subTask);
                            subTasksList.add(subTask);
                            customSubTaskCardAdapter.notifyDataSetChanged();

                        }catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());

                        }

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_newSubTask: {
                newSubTask();
                break;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskcardactivity);

        myPosition = getIntent().getIntExtra("EXTRA_SESSION_ID", myPosition);
        task = getIntent().getParcelableExtra("TASK");

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
                    final DBHandler db = new DBHandler(getActivity(), null, null, 2);

                    assignee = task.getAssignee();
                    responsible = task.getResponsible();

                    rootView = inflater.inflate(R.layout.fragment_info, container, false);
                    setHasOptionsMenu(true);
                    context = rootView.getContext();
                    Helper helper = new Helper(context);
                    serverURL = helper.serverURL;
                    client = helper.getHTTPClient();

                    TextView description = (TextView) rootView.findViewById(R.id.description2);
                    description.setText(task.getDescription());

                    TextView title = (TextView) rootView.findViewById(R.id.title);
                    title.setText(task.getTitle());

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    TextView created = (TextView) rootView.findViewById(R.id.createdDate);
                    TextView dueDate = (TextView) rootView.findViewById(R.id.dueDateDate);
                    Date formattedCreatedDate = new Date();
                    Date formattedDueDate = new Date();
                    Date current = new Date();
                    try {
                        formattedCreatedDate = formatter.parse(task.getCreated());
                        formattedDueDate = formatter.parse(task.getDueDate());
                    } catch (ParseException e) {
                        System.out.println("error while parsing");
                    }

                    if (formattedCreatedDate.getYear() == current.getYear()) {
                        created.setText(new SimpleDateFormat("dd MMM").format(formattedCreatedDate));
                    } else {
                        created.setText(new SimpleDateFormat("dd MMM").format(formattedCreatedDate));
                    }
                    if (formattedDueDate.getYear() == current.getYear()) {
                        dueDate.setText(new SimpleDateFormat("dd MMM").format(formattedDueDate));
                    } else {
                        dueDate.setText(new SimpleDateFormat("dd MMM").format(formattedDueDate));
                    }

                    final UserView responsibleImage = (UserView) rootView.findViewById(R.id.responsibledp);
                    responsibleImage.setUser(responsible);

                    final UserView assigneeImage = (UserView) rootView.findViewById(R.id.assigneedp);
                    assigneeImage.setUser(assignee);
                    int followers[] = task.getFollowers();

                    int run = followers.length;
                    LinearLayout layoutList = (LinearLayout) rootView.findViewById(R.id.followers);
                    if(run==0){
                        TextView followerText = (TextView) rootView.findViewById(R.id.follower);
                        followerText.setVisibility(View.INVISIBLE);
                    }
                    else {
                        for (int k = 0; k < run; k++) {
                            View followerView = inflater.inflate(R.layout.follower_list_view, container, false);
                            UserView followersImage = (UserView) followerView.findViewById(R.id.followerslist);
                            followersImage.setUser(followers[k]);
                            layoutList.addView(followerView);
                        }
                    }

                    subTasksList = db.getAllSubtasks(task.getPk());
                    subTaskListView = (RecyclerView) rootView.findViewById(R.id.subtasks);
                    gridLayoutManager = new GridLayoutManager(context, 1);
                    subTaskListView.setLayoutManager(gridLayoutManager);
                    customSubTaskCardAdapter = new CustomSubTaskCardAdapter(context, subTasksList);
                    subTaskListView.setAdapter(customSubTaskCardAdapter);
                    customSubTaskCardAdapter.notifyDataSetChanged();
                    break;
                }
                case 2: {

                    rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
                    myView = inflater.inflate(R.layout.fragment_timeline_data, container, false);
                    mainContext = myView.getContext();
                    setHasOptionsMenu(false);
                    recyclerView = (RecyclerView) myView.findViewById(R.id.msgTexts);
                    data_list = new ArrayList<>();
                    gridLayoutManager = new GridLayoutManager(mainContext, 1);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    final EditText postTimeline = (EditText) myView.findViewById(R.id.msgBox);
                    Button send = (Button) myView.findViewById(R.id.button);

                    adapter = new CustomTaskViewAdapterTimeline(mainContext, data_list);
                    recyclerView.setAdapter(adapter);
                    mSwipeRefreshLayout = (SwipeRefreshLayout) myView.findViewById(R.id.swipeRefreshTaskBoard);
                    ///////////////////

                    final DBHandler dba = new DBHandler(getActivity(), null, null, 2);

                    context = getActivity().getApplicationContext();
                    Helper helper = new Helper(context);

                    client = helper.getHTTPClient();
                    RequestParams params = new RequestParams();
                    params.put("task", task.getPk());
                    String url = String.format("%s/%s/", serverURL, "/api/taskBoard/timelineItem");
                    client.get(url,params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers,response);

                            System.out.println("success 001xzc");
                            try {

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
                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            // Refresh items
                            refreshItems();
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


                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!postTimeline.getText().toString().equals("")){
                                String categoryPOST = "message";


                                Helper helper = new Helper(context);
                                AsyncHttpClient client = helper.getHTTPClient();
                                RequestParams params = new RequestParams();
                                params.put("task",task.getPk());
                                params.put("text", postTimeline.getText());
                                params.put("category",categoryPOST);

                                final String url = String.format("%s/%s/", helper.serverURL, "/api/taskBoard/timelineItem");
                                client.post(url, params,new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        try {
                                            pkComment = response.getInt("pk");
                                            user = response.getInt("user");

                                                created = response.getString("created").replace("Z", "").replace("T", " ");
                                                category = response.getString("category");
                                                if (category.equals("message")) {
                                                    text = response.getString("text");
                                                    pkCommit = 0;
                                                    commitMessage = null;
                                                    CommitDate = null;
                                                    CommitBranch = null;
                                                    CommitCode = null;
                                                } else {
                                                    text = null;
                                                    JSONObject commit = response.getJSONObject("commit");
                                                    JSONObject repo = commit.getJSONObject("repo");
                                                    pkCommit = commit.getInt("pk");
                                                    commitMessage = commit.getString("message");
                                                    CommitBranch = repo.getString("name") + "/" + commit.getString("branch");
                                                    CommitCode = commit.getString("sha");
                                                    CommitDate = commit.getString("created").replace("Z", "").replace("T", " ");
                                                }
                                                pkTask = response.getInt("task");
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
                                            postTimeline.setText("");

                                            final Comment data = new Comment(pkComment);
                                            data.setCategory(category);
                                            data.setUserPK(user);
                                            Date commentDate = new Date();
                                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try {
                                                commentDate = formatter.parse(created);
                                            } catch (ParseException e) {
                                                System.out.println("error while parsing");
                                            }
                                            String formattedCommitDate;
                                            Date current = new Date();
                                            if(commentDate.getYear() == current.getYear()) {
                                                formattedCommitDate = new SimpleDateFormat("dd MMM").format(commentDate);
                                            }
                                            else {
                                                formattedCommitDate = new SimpleDateFormat("dd MMM, yyyy").format(commentDate);
                                            }
                                            data.setCreated(formattedCommitDate);

                                            if(category.equals("message")) {
                                                data.setText(text);
                                            }
                                            else {
                                                data.setText(commitMessage);
                                                data.setCommitBranch(CommitBranch);

                                                Date commitDate = new Date();
                                                try {
                                                    commitDate = formatter.parse(CommitDate);
                                                } catch (ParseException e) {
                                                    System.out.println("error while parsing");
                                                }
                                                if(commitDate.getYear() == current.getYear()) {
                                                    formattedCommitDate = new SimpleDateFormat("dd MMM").format(commitDate);
                                                }
                                                else {
                                                    formattedCommitDate = new SimpleDateFormat("dd MMM, yyyy").format(commitDate);
                                                }
                                                data.setCommitDate(formattedCommitDate);
                                                data.setCommitCode(CommitCode);
                                            }
                                            Users users = new Users(context);
                                            // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));
                                            users.get(user , new UserMetaHandler(){
                                                @Override
                                                public void onSuccess(UserMeta user){

                                                    data.setUser(user.getFirstName() + " " + user.getLastName());
                                                    // set text in the layout here
                                                }
                                                @Override
                                                public void handleDP(Bitmap dp){

                                                    data.setDpUser(dp);
                                                    // set text in the layout here
                                                }

                                            });


                                            data_list.add(data);
                                            adapter.notifyDataSetChanged();
                                        }
                                        catch (JSONException e){
                                            Log.e("TAG", "Json parsing error: " + e.getMessage());
                                        }

                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                        System.out.println("failure");
                                        System.out.println(statusCode);
                                    }
                                });
                            }
                        }
                    });

                    return myView;


                    ///////////////////////////////////////
                }

                case 3: {

                    rootView = inflater.inflate(R.layout.fragment_files, container, false);
                    setHasOptionsMenu(false);
                    DBHandler dba = new DBHandler(getActivity(), null, null, 2);
                    ListView listView;
                    List<Files> fileList = dba.getAllFiles(task.getPk());
                    listView = (ListView) rootView.findViewById(R.id.files);
                    customFileCardAdapter = new CustomFileCardAdapter(context, R.layout.fileview);

                    for (int i = 0; i < fileList.size(); i++) {
                        customFileCardAdapter.add(fileList.get(i));
                    }


                    List<FloatingActionMenu> menus = new ArrayList<>();
                    Handler mUiHandler = new Handler();

                    menuRed = (FloatingActionMenu) rootView.findViewById(R.id.fab_attach_files);
                    FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab_camera);
                    FloatingActionButton fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab_gallery);
                    FloatingActionButton fab3 = (FloatingActionButton) rootView.findViewById(R.id.fab_files);

                    menuRed.setClosedOnTouchOutside(true);
                    menuRed.hideMenuButton(false);
                    menus.add(menuRed);


                    int delay = 400;
                    for (final FloatingActionMenu menu : menus) {
                        mUiHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                menu.showMenuButton(true);
                            }
                        }, delay);
                        delay += 150;
                    }
                    fab1.setOnClickListener(clickListener);
                    fab2.setOnClickListener(clickListener);
                    fab3.setOnClickListener(clickListener);
                    listView.setAdapter(customFileCardAdapter);






                    break;
                }


            }
            return rootView;
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab_camera:
                        sendPhoto();
                        break;
                    case R.id.fab_gallery:
                        sendGallery();
                        break;
                    case R.id.fab_files:
                        attachFiles();
                        break;
                }
            }
        };
        private void sendGallery()  {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            getActivity().startActivityForResult(intent, GALLERY_REQUEST);
        }

        private void sendPhoto()  {
            Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityForResult(cameraintent, CAMERA_REQUEST);

        }
        private void attachFiles(){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // special intent for Samsung file manager
            Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            // if you want any file type, you can skip next line

            sIntent.addCategory(Intent.CATEGORY_DEFAULT);

            Intent chooserIntent;
            if (getContext().getPackageManager().resolveActivity(sIntent, 0) != null){
                // it is device with samsung file manager
                chooserIntent = Intent.createChooser(sIntent, "Open file");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
            }
            else {
                chooserIntent = Intent.createChooser(intent, "Open file");
            }

            try {
                getActivity().startActivityForResult(chooserIntent, CHOOSE_FILE_REQUESTCODE);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getContext(), "No suitable Files Manager was found.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        Bitmap bm = null ;
        if (requestCode == CAMERA_REQUEST) {
            bm = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100 , bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            Date date = new Date();
            String formattedDate = new SimpleDateFormat("HH:mm").format(date);
            uploadFile(bs,"captured_" + formattedDate + ".png");
        }

        if (requestCode == GALLERY_REQUEST) {

            if (data != null) {
                try {
                    Uri Fpath = data.getData();
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100 , bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

                    String uriString = Fpath.toString();
                    File myFile = new File(uriString);
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = context.getContentResolver().query(Fpath, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    String[] splitted = displayName.split("_");
                    displayName = splitted[splitted.length-1];
                  //  String s = getFileNameByUri(Fpath);
                    uploadFile(bs,displayName);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        if (requestCode == CHOOSE_FILE_REQUESTCODE) {
            Uri Fpath = data.getData();
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(Fpath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String uriString = Fpath.toString();
            File myFile = new File(uriString);
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(Fpath, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
            String[] splitted = displayName.split("_");
            displayName = splitted[splitted.length-1];
            uploadFile(is,displayName);

        }

    }


    private void uploadFile(InputStream is,String fileName){
        final DBHandler dba = new DBHandler(this, null, null, 2);

        RequestParams params = new RequestParams();

        params.put("attachment", is, fileName);

        String url = String.format("%s/%s/", serverURL, "/api/taskBoard/media");
        client.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers,response);
                try {
//                        JSONObject response = jsonArray.getJSONObject(jsonArray.length());
                    final Files file1 = new Files(task.getPk());
                    file1.setPkTask(task.getPk());
                    file1.setFilePk(response.getInt("pk"));
                    file1.setProject_pk(0);
                    file1.setFileLink(response.getString("link"));
                    file1.setAttachment(response.getString("attachment"));
                    file1.setMediaType(response.getString("mediaType"));
                    file1.setName(response.getString("name"));
                    file1.setPostedUser(response.getInt("user"));
                    file1.setFileCreated((response.getString("created")).replace("Z", "").replace("T", " "));
                    List<Files> files = dba.getAllFiles(task.getPk());
                    files.add(file1);
                    String filesArray = new String ();
                    for(int i=0;i<files.size();i++){
                        filesArray = filesArray + "||" + (files.get(i).getFilePk());
                    }
                    String filesArrayNew = filesArray.substring(2);
                    String url1 = String.format("%s/%s/", serverURL, "/api/taskBoard/task/" + task.getPk());
                    RequestParams params1 = new RequestParams();
                    params1.put("files", filesArrayNew);
                    client.patch(url1,params1, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonArray) {
                            super.onSuccess(statusCode, headers,jsonArray);
                            dba.insetTableFiles(file1);
                            customFileCardAdapter.add(file1);
                            menuRed.close(true);
                        }
                        @Override
                        public void onFinish() {
                            System.out.println("finished ");
                            // retrieve all the db entries

                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            System.out.println("finished failed 001xczxc");
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            System.out.println("finished failed 001xczxc sadasdsadsa");
                        }

                    });


                }catch (JSONException e){
                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                }
            }

            @Override
            public void onFinish() {
                System.out.println("finished 001cxczdfhgfg");
                // retrieve all the db entries

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("finished failed 001xczxc gbfdgfdgdf");
            }


        });

    }

    static void refreshItems() {

        Context context = myView.getContext();
        final DBHandler dba = new DBHandler(context,null,null,2);
        Helper helper = new Helper(context);
        serverURL = helper.serverURL;
        AsyncHttpClient client = helper.getHTTPClient();
        RequestParams params = new RequestParams();
        params.put("task", task.getPk());
        String url = String.format("%s/%s/", serverURL, "/api/taskBoard/timelineItem");
        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers,response);
                dba.cleanComments();
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
                    adapter.clearData();
                    load_data_from_database(0);
                    onItemsLoadComplete();
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
                adapter.clearData();
                load_data_from_database(0);
                onItemsLoadComplete();
                System.out.println("finished failed 001xczxc");
            }
        });


    }

    static void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
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

                final DBHandler dba = new DBHandler(mainContext, null, null, 2);
                //System.out.println("pkTask = "+pkTask);

                for (int i = 0; i < dba.getTotalDBEntries_COMMENT(task.getPk()); i++) {
                    final Comment data = new Comment(i);
                    data.setCategory(dba.getCategory(i,task.getPk()));
                    int comment_pk = dba.getCommentPK(i,task.getPk());
                    data.setUserPK(dba.getPostUserPK(comment_pk));
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

                    if(dba.getCategory(i,task.getPk()).equals("message")) {
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

                            data.setUser(user.getFirstName() + " " + user.getLastName());
                            // set text in the layout here
                        }
                        @Override
                        public void handleDP(Bitmap dp){

                            data.setDpUser(dp);
                            // set text in the layout here
                        }

                    });
                    data_list.add(data);
                }

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };

        comment.execute(id);
    }

    public void bottomSheet(View view){
        TextView view1 = (TextView) view.findViewById(R.id.pk_user111);

        final BottomSheetDialogFragment userViewBS = UserViewBS.newInstance(Integer.parseInt(view1.getText().toString()));
        userViewBS.show(getSupportFragmentManager(), userViewBS.getTag());

    }



/////////////////////////





}

