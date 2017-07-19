package com.example.yadav.IM;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.multidex.MultiDex;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Config;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libreerp.Helper;
import com.example.libreerp.User;
import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;

import static android.R.attr.path;


public class ChatRoomActivity extends AppCompatActivity  {

    private String TAG = ChatRoomActivity.class.getSimpleName();

    private static  String with_id;
    private static RecyclerView recyclerView;
    private static ChatRoomThreadAdapter mAdapter;
    private static ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private Button btnSend;
    private Button btnAttach;
    private GoogleMap mMap;
    private ImageView card_location_image;
    private boolean menuOpen = false;
    private static Context context ;
    private View theMenu;
    private View menu1;
    private View menu2;
    private View menu3;
    private View menu4;
    private View coverView;
    private ImageButton btn_location ;
    private ImageButton btn_gallery ;
    private ImageButton btn_camera ;
    private ImageButton btn_files ;
    private TextView title_bar ;
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    private static final int CAMERA_REQUEST = 1024 ;
    private static final int GALLERY_REQUEST = 2134 ;
    private static final int CHOOSE_FILE_REQUESTCODE = 4512 ;
    private static final int PLACE_PICKER_REQUEST = 1000;
    private static LinearLayoutManager layoutManager ;
    private ImageView back_button ;
    private static int chatRoomId;
    private TextView typing ;
    private boolean isType = false ;
    private int typing_id ;
    private String username ;
    private View mCustomView;
    private WampClient client;
    private Boolean connected;
    private String chennel;
    private User login;
    private AsyncHttpClient httpClient;
    DBHandler dba;
    private BroadcastReceiver mReceiver;
    private Helper helper;

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // send the RTC notification

            if (connected){
                String rtcMsg = String.format("%s||%s||%s" , "T" , s.toString(), login.getUsername());
                client.publish(chennel , rtcMsg);
            }

            if (menuOpen == true){
                hideMenu();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    TextView.OnEditorActionListener exampleListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                btnSend.performClick();//match this behavior to your 'Send' (or Confirm) button
            }
            return true;
        }
    };



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("com.libreERP.TYPING");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
//                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                String is_typing = intent.getStringExtra("type");
                String message = intent.getStringExtra("new_message");
                String type_user = intent.getStringExtra("type_user");

                if (is_typing.equals("T") && (type_user.equals(username))) {

                    typing.setVisibility(mCustomView.VISIBLE);
                    new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            typing.setVisibility(mCustomView.GONE);
                        }
                    },
                    1000);


                }
                else if (is_typing.equals("M") && (type_user.equals(username)  || type_user.equals(login.getUsername()) )){
                    int msgPK = Integer.parseInt(intent.getStringExtra("msgPK"));

                    String url = String.format("%s/%s/%s/?mode=" , helper.serverURL, "api/PIM/chatMessage" , msgPK );




                    httpClient.get(url,  new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            String message;
                            try {

                                String attachement;
                                int pkOriginator;
                                String created;
                                boolean read;
                                int pkUser;
                                int withPK ;
                                int msgPk = response.getInt("pk");

                                inputMessage.setText("");
                                if (!dba.CheckIfMessagePKAlreadyInDBorNot(msgPk)) { // check in table of Message
                                    message = response.getString("message");
                                    attachement = response.getString("attachment");
                                    pkOriginator = response.getInt("originator");
                                    created = response.getString("created").replace("Z", "").replace("T", " ");
                                    read = response.getBoolean("read");
                                    pkUser = response.getInt("user");
                                    ChatRoomTable chatRoomTable = new ChatRoomTable();
                                    if (login.getPk() == pkOriginator){
                                        withPK = pkUser ;
                                    }
                                    else {
                                        withPK = pkOriginator ;
                                    }



                                    chatRoomTable.setAttachement(attachement);
                                    chatRoomTable.setCreated(created);
                                    chatRoomTable.setMessage(message);
                                    chatRoomTable.setPkMessage(msgPk);
                                    chatRoomTable.setPkOriginator(pkOriginator);
                                    chatRoomTable.setPkUser(pkUser);
                                    chatRoomTable.setChatRoomID(chatRoomId);
                                    chatRoomTable.setOtherPk(withPK);
                                    dba.insertTableMessage(chatRoomTable);
                                    UserMeta usermeta = new UserMeta(pkOriginator);
                                    Message messageAfterType = new Message(Integer.toString(msgPk),message,created,usermeta);
                                    messageAfterType.setAttachment(chatRoomTable.getAttachement());
                                    messageArrayList.add(messageAfterType);
                                    mAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {

                            }
                           // load_data_from_database(0);

                            // recyclerView.scrollToPosition(messageArrayList.size()-1);

                            // layoutManager.setStackFromEnd(true);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            System.out.println("failure");
                            System.out.println(statusCode);
                        }
                    });;



                }

            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connected = false;

        context = getApplicationContext();

        helper = new Helper(context);

        messageArrayList = new ArrayList<>();

        WampClientBuilder builder = new WampClientBuilder();

        String wampUrl = helper.serverURL.replace("http://", "ws://").replace("https://", "ws://")+":8080/ws";

        try{
            builder.withUri("ws://pradeepyadav.net:8080/ws")
                    .withRealm("default")
                    .withInfiniteReconnects()
                    .withReconnectInterval(10, TimeUnit.SECONDS);

            client = builder.build();

            client.open();

            String done = "ok";

            client.statusChanged().subscribe(new Action1<WampClient.Status>() {
                private Subscription procSubscription;

                public void call(WampClient.Status t1) {
                    Log.d("info","Session status changed to " + t1);

                    if (t1 == WampClient.Status.Connected) {
                        Log.d("info","Connected");
                        connected = true;
//
                    }
                }

            });

            client.open();


        }catch (ws.wamp.jawampa.ApplicationError e){
            String done = "ok";
        }



        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        inputMessage = (EditText) findViewById(R.id.message_text);
        inputMessage.addTextChangedListener(mTextEditorWatcher);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnAttach = (Button) findViewById(R.id.btn_attach);
        theMenu = findViewById(R.id.theMenu);
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);
        menu4 = findViewById(R.id.menu4);

        btn_location = (ImageButton) findViewById(R.id.btn_location);
        btn_gallery = (ImageButton) findViewById(R.id.btn_gallery);
        btn_camera = (ImageButton) findViewById(R.id.btn_camera);
        btn_files = (ImageButton)findViewById(R.id.btn_files);
        card_location_image = (ImageView) findViewById(R.id.image_location);
        inputMessage.setOnEditorActionListener(exampleListener);





        httpClient = helper.getHTTPClient();

        login = User.loadUser(context);



        inputMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                if(menuOpen ==  true){
                    hideMenu();;
                }
                coverView.setVisibility(View.GONE);
            }
        });
        btnAttach.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Do something in response to button click
                if (menuOpen == false)
                    revealMenu();
                else{
                    hideMenu();
                }

            }


        });
        dba = new DBHandler(context, null, null, 1);
        btnSend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Do something in response to button click
                if (menuOpen == true)
                    hideMenu();

                long currentTime=System.currentTimeMillis(); //getting current time in millis
                //converting it into user readable format
                Calendar cal= Calendar.getInstance();
                cal.setTimeInMillis(currentTime);
                String showTime=String.format("%1$tI:%1$tM:%1$tS %1$Tp",cal);
                final String content = inputMessage.getText().toString();


                RequestParams params = new RequestParams();

                params.put("message", content);
                params.put("user",with_id);
                params.put("read",false);
                int msgPk ;

                String url = String.format("%s/%s/" , helper.serverURL, "api/PIM/chatMessage");
                final ChatRoomTable chatRoomTable = new ChatRoomTable();
                httpClient.post(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            String message;
                            String attachement;
                            int pkOriginator;
                            String created;
                            boolean read;
                            int pkUser;
                            int withPK ;
                            int msgPk = response.getInt("pk");
                            String rtcMsg = String.format("%s||%s||%s||%s", "M", content, login.getUsername(), msgPk);
                            client.publish(chennel, rtcMsg);

                            inputMessage.setText("");
                            if (!dba.CheckIfMessagePKAlreadyInDBorNot(msgPk)) { // check in table of Message
                                message = response.getString("message");
                                attachement = response.getString("attachment");
                                pkOriginator = response.getInt("originator");
                                created = response.getString("created").replace("Z", "").replace("T", " ");
                                read = response.getBoolean("read");
                                pkUser = response.getInt("user");

                                chatRoomTable.setSender_change(0);
                                if (login.getPk() == pkOriginator){
                                    withPK = pkUser ;
                                }
                                else {
                                    withPK = pkOriginator ;
                                }


                                chatRoomTable.setAttachement(attachement);
                                chatRoomTable.setCreated(created);
                                chatRoomTable.setMessage(message);
                                chatRoomTable.setPkMessage(msgPk);
                                chatRoomTable.setPkOriginator(pkOriginator);
                                chatRoomTable.setPkUser(pkUser);
                                chatRoomTable.setOtherPk(withPK);
                                chatRoomTable.setChatRoomID(chatRoomId);
                                dba.insertTableMessage(chatRoomTable);

                                dba.updateMessageTableChatRoom(pkUser ,message ,0 ,created);

                                UserMeta usermeta = new UserMeta(login.getPk());
                                Message sentMessage = new Message(Integer.toString(chatRoomTable.getPkMessage()),content,chatRoomTable.getCreated(),usermeta);
                                sentMessage.setAttachment(chatRoomTable.getAttachement());
                                messageArrayList.add(sentMessage);
                                mAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(mAdapter.getItemCount()-1);

                            }
                            // now to update last message of chatRoomTable from db

                        } catch (JSONException e) {

                        }
                       // mAdapter.notifyDataSetChanged();
                       // recyclerView.scrollToPosition(messageArrayList.size()-1);

                       // layoutManager.setStackFromEnd(true);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        System.out.println("failure");
                        System.out.println(statusCode);
                    }
                });;







            }


        });
        btn_location.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                sendLocation();
            }
        });
        btn_gallery.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                sendGallery();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                sendPhoto();
            }
        });
        btn_files.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                attachFiles();
            }
        });


        final Intent intent = getIntent();
        chatRoomId = Integer.parseInt(intent.getStringExtra("chatID"));
        with_id = intent.getStringExtra("with_id"); // chat Room id is same as with id here in this case
        String title = intent.getStringExtra("name");
        // read all messages
//        int unreadTotal1 = dba.getUnREADFromWithPk(Integer.parseInt(with_id));
        dba.updateUnreadChatRoom(Integer.parseInt(with_id) , 0);
//        int unreadTotal = dba.getUnREADFromWithPk(Integer.parseInt(with_id));

//        int number = dba.getTotalDBEntries_MESSAGE();

        //ChatRoomTable unReadList = intent.getParcelableExtra("UnreadMessages");
        /*for (int i = 0 ; i < unReadList ; i++){
            messageArrayList.add(unReadList.get(i));
        }
        for (int i = 0 ; i < unReadList.size() ; i++){
            unReadList.remove(i);
        }*/
        LayoutInflater mInflater = LayoutInflater.from(this);
        mCustomView = mInflater.inflate(R.layout.action_bar_chatroom, null);
       final  CircleImageView image = (CircleImageView) mCustomView.findViewById(R.id.circularimageView1);


        Users users = new Users(context);
        users.get(Integer.parseInt(with_id) , new UserMetaHandler(){
            @Override
            public void onSuccess(UserMeta user){
                System.out.println("yes65262626626");
                //bundle.putString("username",user.getUsername());
                username = user.getUsername();
                chennel = String.format("service.chat.%s" , username);
                // set text in the layout here
            }
            @Override
            public void handleDP(Bitmap dp){
                System.out.println("dp dsda");
                image.setImageBitmap(dp);
                // set text in the layout here
            }

        });


        getSupportActionBar().setTitle(title);


        //Drawable drawable = new BitmapDrawable(getResources(), createCircleBitmap(profile));
        //Drawable drawable = new BitmapDrawable(getResources(), profile);
        //getSupportActionBar().setIcon(drawable);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);





        title_bar = (TextView) mCustomView.findViewById(R.id.titleText);
        title_bar.setText(title);
        back_button =  (ImageView) mCustomView.findViewById(R.id.back_button);
        typing = (TextView) mCustomView.findViewById(R.id.typing);

        typing.setVisibility(mCustomView.GONE);


//        if (isType == true){
//            //typing.setVisibility(mCustomView.GONE);
//            typing.setVisibility(mCustomView.VISIBLE);
//        }
//        else {
//            typing.setVisibility(mCustomView.GONE);
//        }

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialogFragment userViewBS = UserViewBS.newInstance(Integer.parseInt(with_id));
                userViewBS.show(getSupportFragmentManager(), userViewBS.getTag());
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentback = new Intent(context, HomeActivity.class);
                boolean isEmpty = false ;
                if (messageArrayList.size() == 0){
                    isEmpty = true ;
                }
                intentback.putExtra("isEmpty",isEmpty);
                intentback.putExtra("withId",Integer.parseInt(with_id));
                startActivity(intentback);
            }
        });
        if (with_id == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }


        int login_pk = login.getPk();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setStackFromEnd(true);
        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList ,login_pk);
        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();



        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override

            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {


                recyclerView.scrollToPosition(mAdapter.getItemCount());

            }
        });

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(this, recyclerView, new ChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                if(menuOpen ==  true){
                    hideMenu();;
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.scrollToPosition(messageArrayList.size() - 1);
        fetchChatThread();


        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);


        //layoutManager.setStackFromEnd(true);
    }
    private void handlePushNotification(Intent intent) {
        String message =  intent.getStringExtra("new_message");
        String Typing = intent.getStringExtra("type");
        String user_name = intent.getStringExtra("type_user");




       /* if (message != null && chatRoomId != null) {
           // messageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }*/
    }


    private void fetchChatThread() {
        //load_data_from_database(0);
        //recyclerView.scrollToPosition(messageArrayList.size() - 1);
        layoutManager.setStackFromEnd(true);



        final Helper helper = new Helper(context);

        AsyncHttpClient client = helper.getHTTPClient();
        Users users = new Users(context);
        final String[] name = new String[1];
        final String[] url = new String[1];
        // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));

        users.get(Integer.parseInt(with_id) , new UserMetaHandler(){
            @Override
            public void onSuccess(UserMeta user){
                System.out.println("yes65262626626");
                name[0] = user.getUsername();
                url[0] = String.format("%s/%s%s", helper.serverURL, "api/PIM/chatMessageBetween/?other=",name[0]);
                // set text in the layout here
            }
            @Override
            public void handleDP(Bitmap dp){

                // set text in the layout here
            }

        });

        // i have to change it

        client.get(url[0], new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                System.out.println("success 001xzc");
                try {
//                                JSONArray response = tasks.getJSONArray("results");
                    int pkMessage;
                    String message;
                    String attachement;
                    int pkOriginator;
                    String created;
                    boolean read;
                    int pkUser;
                    int withPK ;

                    User login = User.loadUser(context);
                    int login_pk = login.getPk();
                    int responseLength = response.length();
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject c = response.getJSONObject(i);
                        pkMessage = c.getInt("pk");
                        if (!dba.CheckIfMessagePKAlreadyInDBorNot(pkMessage)) { // check in table of Message
                            message = c.getString("message");
                            attachement = c.getString("attachment");
                            pkOriginator = c.getInt("originator");
                            created =  c.getString("created").replace("Z","").replace("T"," ");
                            read = c.getBoolean("read");
                            pkUser = c.getInt("user");
                            ChatRoomTable chatRoomTable = new ChatRoomTable();



                            if (read == false){
                                chatRoomTable.setIsReadStatus(1);
                            }
                            else {
                                chatRoomTable.setIsReadStatus(0);
                            }

                            if (login.getPk() == pkOriginator){
                                withPK = pkUser ;
                            }
                            else {
                                withPK = pkOriginator ;
                            }
                            chatRoomTable.setAttachement(attachement);
                            chatRoomTable.setCreated(created);
                            chatRoomTable.setMessage(message);
                            chatRoomTable.setPkMessage(pkMessage);
                            chatRoomTable.setPkOriginator(pkOriginator);
                            chatRoomTable.setPkUser(pkUser);
                            chatRoomTable.setOtherPk(withPK);
                            chatRoomTable.setChatRoomID(chatRoomId);
                            dba.insertTableMessage(chatRoomTable);

                        }



                        // every messages will be inserted





                    }
                    messageArrayList.clear();
                    load_data_from_database(0);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messageArrayList.size() - 1);
                    layoutManager.setStackFromEnd(true);

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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                load_data_from_database(0);
                recyclerView.scrollToPosition(messageArrayList.size() - 1);

                layoutManager.setStackFromEnd(true);
                System.out.println("finished failed 001xczxc");
            }
        });
    }




    private static void load_data_from_database(int id) {

        final AsyncTask<Integer, Void, Void> comment = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                messageArrayList.clear();
                final DBHandler dba = new DBHandler(context, null, null, 1); // see this
                //System.out.println("pkTask = "+pkTask);
                dba.updateAllUnReadMessage(Integer.parseInt(with_id));
                ArrayList<ChatRoomTable> message_table = dba.getData(Integer.parseInt(with_id));

                int size = message_table.size();
                User login = User.loadUser(context);
                int login_pk = login.getPk();


                boolean firstMessage = false ;
                int sender_change = 1 ;
                for (int i = 0; i < message_table.size() ; i++) {

                   /* final ChatRoomTable data = new ChatRoomTable();
                    data.setPkOriginator(dba.message_getOriginatorPK(i));
                    data.setPkUser(dba.message_getUserPk(i));
                    if (data.getPkOriginator() == Integer.parseInt(with_id) || data.getPkUser() ==  Integer.parseInt(with_id)){
                        data.setPkMessage(dba.message_getMessagePK(i));
                        data.setMessage(dba.message_getMessage(i));
                        data.setAttachement(dba.message_getAttachment(i));
                        data.setSender_change(dba.message_getSenderChange(i));
                        String date = dba.message_getDate(i);
                        data.setCreated(date);
                        String messageDate;
                        //messageDate = new SimpleDateFormat("dd MMM, yyyy").format(date);


                        Users users = new Users(context);
                        final String[] name = new String[1];
                        final Bitmap[] bp = new Bitmap[1];
                        // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));

                        */
                        String s = message_table.get(message_table.size()-1).getMessage();
                        //System.out.print(message_table.get(message_table.size()-1).getMessage());
                        UserMeta usermeta = new UserMeta(message_table.get(i).getPkOriginator());
                        Message message = new Message(Integer.toString(message_table.get(i).getPkMessage()),message_table.get(i).getMessage(),message_table.get(i).getCreated(),usermeta);
                        message.setAttachment(message_table.get(i).getAttachement());
                        messageArrayList.add(message);

                        //)

                }

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageArrayList.size() - 1);

                layoutManager.setStackFromEnd(true);
            }
        };

        comment.execute(id);
    }




    private void sendLocation()  {



        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent = new Intent() ;
        try {
            intent = builder.build(this);
            startActivityForResult(intent,PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }


    }

    private void sendGallery()  {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    private void sendPhoto()  {

        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);


                double lat = place.getLatLng().latitude ;
                double longitude = place.getLatLng().longitude;
                String latitude_string =  Double.toString(lat);
                String longitude_string =  Double.toString(longitude);
                String latlong = latitude_string + " " + longitude_string ;

                Toast.makeText(this, latlong, Toast.LENGTH_LONG).show();
                String imageUrl = "http://maps.google.com/maps/api/staticmap?center=" +latitude_string + "," + longitude_string + "&zoom=15&size=200x200&sensor=false";
               /* try {

                    URL url = new URL("http://maps.google.com/maps/api/staticmap?center=" +latitude_string + "," + longitude_string + "&zoom=15&size=200x200&sensor=false");
                    InputStream in = url.openConnection().getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(in,1024*8);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    int len=0;
                    byte[] buffer = new byte[1024];
                    while((len = bis.read(buffer)) != -1){
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    bis.close();

                    byte[] datas = out.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
                    card_location_image.setImageBitmap(bitmap);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }*/
                long currentTime=System.currentTimeMillis(); //getting current time in millis
                //converting it into user readable format
                Calendar cal= Calendar.getInstance();
                cal.setTimeInMillis(currentTime);
                String showTime=String.format("%1$tI:%1$tM:%1$tS %1$Tp",cal);
                final String content = "GPS://" + latitude_string + " " + longitude_string ;;


                RequestParams params = new RequestParams();

                params.put("message", content);
                params.put("user",with_id);
                params.put("read",false);

                String url = String.format("%s/%s/" , helper.serverURL, "api/PIM/chatMessage");
                final ChatRoomTable chatRoomTable = new ChatRoomTable();
                httpClient.post(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            String message;
                            String attachement;
                            int pkOriginator;
                            String created;
                            boolean read;
                            int pkUser;
                            int withPK;
                            int msgPk = response.getInt("pk");
                            String rtcMsg = String.format("%s||%s||%s||%s", "M", content, login.getUsername(), msgPk);
                            client.publish(chennel, rtcMsg);

                            inputMessage.setText("");
                            if (!dba.CheckIfMessagePKAlreadyInDBorNot(msgPk)) { // check in table of Message
                                message = response.getString("message");
                                attachement = response.getString("attachment");
                                pkOriginator = response.getInt("originator");
                                created = response.getString("created").replace("Z", "").replace("T", " ");
                                read = response.getBoolean("read");
                                pkUser = response.getInt("user");
                                if (login.getPk() == pkOriginator){
                                    withPK = pkUser ;
                                }
                                else {
                                    withPK = pkOriginator ;
                                }



                                chatRoomTable.setAttachement(attachement);
                                chatRoomTable.setCreated(created);
                                chatRoomTable.setMessage(message);
                                chatRoomTable.setPkMessage(msgPk);
                                chatRoomTable.setPkOriginator(pkOriginator);
                                chatRoomTable.setPkUser(pkUser);
                                chatRoomTable.setOtherPk(withPK);
                                chatRoomTable.setChatRoomID(chatRoomId);
                                dba.insertTableMessage(chatRoomTable);

                                dba.updateMessageTableChatRoom(pkUser ,message ,0 ,created);
                                UserMeta usermeta = new UserMeta(login.getPk());
                                Message locationMessage = new Message(Integer.toString(chatRoomTable.getPkMessage()),content,chatRoomTable.getCreated(),usermeta);
                                locationMessage.setAttachment(chatRoomTable.getAttachement());
                                messageArrayList.add(locationMessage);
                                mAdapter.notifyDataSetChanged();


                            }
                            // now to update last message of chatRoomTable from db

                        } catch (JSONException e) {

                        }
                        // mAdapter.notifyDataSetChanged();
                        // recyclerView.scrollToPosition(messageArrayList.size()-1);

                        // layoutManager.setStackFromEnd(true);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        System.out.println("failure");
                        System.out.println(statusCode);
                    }
                });;






            }

        }
        Bitmap bm = null ;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bm = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100 , bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            Date date = new Date();
            String formattedDate = new SimpleDateFormat("HH:mm").format(date);
            uploadFile(bs,"captured_" + formattedDate + ".png");

            UserMeta user = new UserMeta(login.getPk());

            Message dummy = new Message("1","","1 am" ,user);
            dummy.setLocation(3);// 3 is for camera
            dummy.setBm(bm);
            messageArrayList.add(dummy) ;
            mAdapter.notifyDataSetChanged();
           saveImageTOExternalStorage(bm);

        }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

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


                    card_location_image.setImageBitmap(bm);


                    // Message dummy = new Message("1","","1 am" ,user1);
                   // dummy.setLocation(2); // 2 is for gallery
                  //  dummy.setBm(bm);
                  //  messageArrayList.add(dummy) ;
                    mAdapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            card_location_image.setImageBitmap(bm);
            saveImageTOExternalStorage(bm);

        }
        if (requestCode == CHOOSE_FILE_REQUESTCODE && resultCode == Activity.RESULT_OK) {
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
        final Helper helper = new Helper(context);

        final AsyncHttpClient httpClient = helper.getHTTPClient();


        params.put("message", "");
        params.put("user",with_id);
        params.put("read",false);
        params.put("attachment", is,fileName);
        int msgPk ;

        String url = String.format("%s/%s/" , helper.serverURL, "api/PIM/chatMessage");
        final ChatRoomTable chatRoomTable = new ChatRoomTable();
        httpClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                        String message;
                        String attachement;
                        int pkOriginator;
                        String created;
                        boolean read;
                        int pkUser;
                        int withPK;
                        int msgPk = response.getInt("pk");
                        String rtcMsg = String.format("%s||%s||%s||%s", "M", "", login.getUsername(), msgPk);
                        client.publish(chennel, rtcMsg);

                        inputMessage.setText("");
                        if (!dba.CheckIfMessagePKAlreadyInDBorNot(msgPk)) { // check in table of Message
                            message = response.getString("message");
                            attachement = response.getString("attachment");
                            pkOriginator = response.getInt("originator");
                            created = response.getString("created").replace("Z", "").replace("T", " ");
                            read = response.getBoolean("read");
                            pkUser = response.getInt("user");

                            chatRoomTable.setSender_change(0);
                            if (login.getPk() == pkOriginator){
                                withPK = pkUser ;
                            }
                            else {
                                withPK = pkOriginator ;
                            }

                            chatRoomTable.setAttachement(attachement);
                            chatRoomTable.setCreated(created);
                            chatRoomTable.setMessage(message);
                            chatRoomTable.setPkMessage(msgPk);
                            chatRoomTable.setPkOriginator(pkOriginator);
                            chatRoomTable.setPkUser(pkUser);
                            chatRoomTable.setOtherPk(withPK);
                            chatRoomTable.setChatRoomID(chatRoomId);
                            dba.insertTableMessage(chatRoomTable);

                            dba.updateMessageTableChatRoom(pkUser, message, 0, created);

                            UserMeta usermeta = new UserMeta(login.getPk());
                            Message sentMessage = new Message(Integer.toString(chatRoomTable.getPkMessage()), "", chatRoomTable.getCreated(), usermeta);

                            sentMessage.setAttachment(chatRoomTable.getAttachement());
                            messageArrayList.add(sentMessage);
                            mAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                        }

                    // now to update last message of chatRoomTable from db

                } catch (JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                }
                // mAdapter.notifyDataSetChanged();
                // recyclerView.scrollToPosition(messageArrayList.size()-1);

                // layoutManager.setStackFromEnd(true);
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










    private void revealMenu() {
        menuOpen = true;
        theMenu.setVisibility(View.VISIBLE);
    }


    private void hideMenu(){
        menuOpen = false;
        theMenu.setVisibility(View.GONE);
    }


    //Copy downloaded file into SD Card From cache
    public static void cacheCopy(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally
        {
            if (inChannel != null) inChannel.close(); if (outChannel != null) outChannel.close();
        }
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
        if (getPackageManager().resolveActivity(sIntent, 0) != null){
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
        }
        else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, CHOOSE_FILE_REQUESTCODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }

    }
    private void saveImageTOExternalStorage(Bitmap bm){
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        File path = new File(root);
        File myDir = new File(path + "/saveImages");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String fname = "Image-" + timeStamp + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()){

        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();


        }
        catch (Exception e) {
            e.printStackTrace();

        }

        String text = traverse(myDir);


    }
    public String traverse (File dir) {
        String name = "";
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null){
                for (File file : files){
                    name = name + file.getName() + ",";

                }
            }

        }
        return name ;
    }



}

