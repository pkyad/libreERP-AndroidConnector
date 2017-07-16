package com.example.yadav.taskBoard;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectCardActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static List<String> arraySubTask = new ArrayList<>();
    private static ListView listView;
    static int myPosition = 0;
    static Projects projects;
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
    private static final int CAMERA_REQUEST = 1024;
    private static final int GALLERY_REQUEST = 2134;
    private static final int CHOOSE_FILE_REQUESTCODE = 4512;
    static FloatingActionMenu menuRed;
    static CustomFileCardAdapter customFileCardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projectcardactivity);

        myPosition = getIntent().getIntExtra("EXTRA_SESSION_ID", myPosition);
        projects = getIntent().getParcelableExtra("PROJECT");

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

        private View getUsersRowView(LayoutInflater inflater, ViewGroup container,
                                     final List<Integer> usersList) {


            View layoutList = inflater.inflate(R.layout.teamtwomembers, container, false);
            LinearLayout layout = (LinearLayout) layoutList.findViewById(R.id.team);


            for (int i = 0; i < usersList.size(); i++) {
                View layout1 = inflater.inflate(R.layout.team_view, container, false);
                final CircleImageView userImage = (CircleImageView) layout1.findViewById(R.id.userImageProject);
                final TextView userName = (TextView) layout1.findViewById(R.id.userNameProject);
                final TextView userPK = (TextView) layout1.findViewById(R.id.pk_user111);
                userPK.setText(usersList.get(i).toString());
                Users users = new Users(getContext());
                users.get(usersList.get(i), new UserMetaHandler() {
                    @Override
                    public void onSuccess(UserMeta user) {
                        userName.setText(user.getFirstName() + " " + user.getLastName());
                    }

                    @Override
                    public void handleDP(Bitmap dp) {

                        userImage.setImageBitmap(dp);
                    }

                });


                layout.addView(layout1);
            }
            return layoutList;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1: {
                    arraySubTask.clear();
                    final DBHandler db = new DBHandler(getActivity(), null, null, 2);
                    rootView = inflater.inflate(R.layout.fragment_info_project, container, false);
                    context = getActivity().getApplicationContext();

                    TextView description = (TextView) rootView.findViewById(R.id.description2);
                    description.setText(projects.getDescription());

                    TextView title = (TextView) rootView.findViewById(R.id.title);
                    title.setText(projects.getTitle());

                    TextView created = (TextView) rootView.findViewById(R.id.createdDate);
                    created.setText(projects.getCreated());

                    TextView dueDate = (TextView) rootView.findViewById(R.id.dueDateDate);
                    dueDate.setText(projects.getDueDate());

                    int team[] = projects.getTeam();

                    int run = team.length;
                    LinearLayout view = (LinearLayout) rootView.findViewById(R.id.layout222);

                    if (run == 0) {
                        TextView text = (TextView) rootView.findViewById(R.id.texttext);
                        text.setVisibility(View.GONE);
                    } else {
                        for (int k = 0; k < run; k = k + 2) {
                            int u1 = k;
                            int u2 = 0;
                            if (u1 != run - 1)
                                u2 = k + 1;

                            final List<Integer> usersList = new ArrayList<>();
                            usersList.add(team[u1]);
                            if (u1 != run - 1)
                                usersList.add(team[u2]);
                            view.addView(getUsersRowView(inflater, container, usersList));

                        }
                    }

                    rootView.findViewById(R.id.showTasksProject).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.putExtra("PK_PROJECT", projects.getPk());
                            startActivity(intent);
                        }
                    });

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
                    mSwipeRefreshLayout = (SwipeRefreshLayout) myView.findViewById(R.id.swipeRefreshTaskBoard);
                    ///////////////////
                    context = getActivity().getApplicationContext();
                    Helper helper = new Helper(context);
                    serverURL = helper.serverURL;
                    client = helper.getHTTPClient();
                    RequestParams params = new RequestParams();
                    params.put("task", projects.getPk());
                    String url = String.format("%s/%s/", serverURL, "/api/projects/timelineItem");
                    client.get(url, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);

                            System.out.println("success 001xzc");
                            try {
//                                JSONArray response = tasks.getJSONArray("results");
                                int pkComment;
                                int pkProject;
                                String created;
                                String category;
                                String text = null;
                                int user;

                                DBHandler dba = new DBHandler(getActivity(), null, null, 2);
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject c = response.getJSONObject(i);
                                    pkComment = c.getInt("pk");
                                    user = c.getInt("user");
                                    if (!dba.CheckIfCOMMENT_PKAlreadyInDBorNot(pkComment)) {
                                        created = c.getString("created").replace("Z", "").replace("T", " ");
                                        category = c.getString("category");
                                        if (category.equals("message")) {
                                            text = c.getString("text");
                                        }
                                        pkProject = c.getInt("project");
                                        Comment comment = new Comment(pkComment);
                                        comment.setPkComment(pkComment);
                                        comment.setPkProject(pkProject);
                                        comment.setUserPK(user);
                                        comment.setCategory(category);
                                        comment.setCreated(created);
                                        comment.setText(text);
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


                    final EditText postTimeline = (EditText) myView.findViewById(R.id.msgBox);
                    Button send = (Button) myView.findViewById(R.id.button);


                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!postTimeline.getText().toString().equals("")){
                                String categoryPOST = "message";

                                Helper helper = new Helper(context);
                                serverURL = helper.serverURL;
                                client = helper.getHTTPClient();
                                RequestParams params = new RequestParams();
                                params.put("project", projects.getPk());
                                params.put("text", postTimeline.getText());
                                params.put("category",categoryPOST);
                                String url = String.format("%s/%s/", serverURL, "/api/projects/timelineItem");
                                client.post(url, params, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject c) {
                                        super.onSuccess(statusCode, headers, c);

                                        System.out.println("success 001xzc");
                                        try {
//                                JSONArray response = tasks.getJSONArray("results");
                                            int pkComment;
                                            int pkProject;
                                            String created;
                                            String category;
                                            String text = null;
                                            int user;

                                            DBHandler dba = new DBHandler(getActivity(), null, null, 2);
                                                pkComment = c.getInt("pk");
                                                user = c.getInt("user");

                                                    created = c.getString("created").replace("Z", "").replace("T", " ");
                                                    category = c.getString("category");
                                                    if (category.equals("message")) {
                                                        text = c.getString("text");
                                                    }
                                                    pkProject = c.getInt("project");
                                                    Comment comment = new Comment(pkComment);
                                                    comment.setPkComment(pkComment);
                                                    comment.setPkProject(pkProject);
                                                    comment.setUserPK(user);
                                                    comment.setCategory(category);
                                                    comment.setCreated(created);
                                                    comment.setText(text);
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


                                            data.setText(text);

                                            Users users = new Users(context);
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
                            }
                        }
                    });






                    return myView;


                    ///////////////////////////////////////
                }

                case 3: {
                    rootView = inflater.inflate(R.layout.fragment_files, container, false);
                    DBHandler dba = new DBHandler(getActivity(), null, null, 2);

                    List<Files> fileList = dba.getProjectFiles(projects.getPk());
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

        private void sendGallery() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            getActivity().startActivityForResult(intent, GALLERY_REQUEST);
        }

        private void sendPhoto() {
            Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityForResult(cameraintent, CAMERA_REQUEST);

        }

        private void attachFiles() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // special intent for Samsung file manager
            Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            // if you want any file type, you can skip next line

            sIntent.addCategory(Intent.CATEGORY_DEFAULT);

            Intent chooserIntent;
            if (getContext().getPackageManager().resolveActivity(sIntent, 0) != null) {
                // it is device with samsung file manager
                chooserIntent = Intent.createChooser(sIntent, "Open file");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
            } else {
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

        String url = String.format("%s/%s/", serverURL, "/api/projects/media");
        client.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers,response);
                try {
//                        JSONObject response = jsonArray.getJSONObject(jsonArray.length());
                    final Files file1 = new Files(projects.getPk());

                    file1.setProject_pk(projects.getPk());
                    file1.setFilePk(response.getInt("pk"));
                    file1.setFileLink(response.getString("link"));
                    file1.setAttachment(response.getString("attachment"));
                    file1.setMediaType(response.getString("mediaType"));
                    file1.setName(response.getString("name"));
                    file1.setPostedUser(response.getInt("user"));
                    file1.setFileCreated(response.getString("created").replace("Z","").replace("T"," "));

                    List<Files> files = dba.getAllFiles(projects.getPk());
                    files.add(file1);
                    String filesArray = new String ();
                    for(int i=0;i<files.size();i++){
                        filesArray = filesArray + "||" + (files.get(i).getFilePk());
                    }
                    String filesArrayNew = filesArray.substring(2);
                    String url1 = String.format("%s/%s/", serverURL, "/api/projects/project/" + projects.getPk());
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

    static void refreshItems() {

        context = myView.getContext();
        final DBHandler dba = new DBHandler(context, null, null, 2);
        Helper helper = new Helper(context);
        serverURL = helper.serverURL;
        client = helper.getHTTPClient();
        RequestParams params = new RequestParams();
        params.put("task", projects.getPk());
        String url = String.format("%s/%s/", serverURL, "/api/projects/timelineItem");
        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers,response);

                System.out.println("success 001xzc");
                try {
//                                JSONArray response = tasks.getJSONArray("results");
                    int pkComment;
                    int pkProject;
                    String created;
                    String category;
                    String text=null;
                    int user;


                    for (int i = 0; i < response.length(); i++) {

                        JSONObject c = response.getJSONObject(i);
                        pkComment = c.getInt("pk");
                        user = c.getInt("user");
                        if (!dba.CheckIfCOMMENT_PKAlreadyInDBorNot(pkComment)) {
                            created = c.getString("created").replace("Z","").replace("T"," ");
                            category = c.getString("category");
                            if (category.equals("message")) {
                                text = c.getString("text");
                            }
                            pkProject = c.getInt("project");
                            Comment comment = new Comment(pkComment);
                            comment.setPkComment(pkComment);
                            comment.setPkProject(pkProject);
                            comment.setUserPK(user);
                            comment.setCategory(category);
                            comment.setCreated(created);
                            comment.setText(text);
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


    private static void load_data_from_database(int id) {

        final AsyncTask<Integer, Void, Void> comment = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                final DBHandler dba = new DBHandler(mainContext, null, null, 2);
                //System.out.println("pkTask = "+pkTask);

                for (int i = 0; i < dba.getTotalDBEntries_COMMENT_Project(projects.getPk()); i++) {
                    final Comment data = new Comment(i);

                    data.setCategory(dba.getCategory_COMMENT_Project(i,projects.getPk()));
                    int comment_pk = dba.getCommentPK_Project_Comment(i,projects.getPk());
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


                        data.setText(dba.getMessage(comment_pk));

                    Users users = new Users(context);
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

}
