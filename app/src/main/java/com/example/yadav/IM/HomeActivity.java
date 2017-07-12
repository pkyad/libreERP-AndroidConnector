package com.example.yadav.IM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libreerp.AboutFragment;
import com.example.libreerp.ChangePasswordFragment;
import com.example.libreerp.Helper;
import com.example.libreerp.ProfileFragment;
import com.example.libreerp.User;
import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import rx.Subscription;
import rx.functions.Action1;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public User usr;
    private Context context;
    private FragmentManager fragmentManager;
    private WampClient client;
    private User login;
    private AsyncHttpClient httpClient;
    DBHandler dba;
    private BroadcastReceiver mReceiver;
    private Helper helper;
    private ArrayList<NotificationMessage> unreadNotification = new ArrayList<NotificationMessage>();
    private boolean firstTime = true ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //=======================

        Users users = new Users(context);

        users.get(11 , new UserMetaHandler(){
            @Override
            public void onSuccess(UserMeta user){
                System.out.println("yes65262626626");
                // set text in the layout here
            }
            @Override
            public void handleDP(Bitmap dp){
                System.out.println("dp dsda");
                // set text in the layout here
            }

        });

        WampClientBuilder builder = new WampClientBuilder();


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

                        procSubscription = client.makeSubscription("service.chat.admin").subscribe(new Action1<PubSubData>() {
                            @Override
                            public void call(PubSubData pubSubData) {
                                String message = pubSubData.toString();
                                ArrayNode c  = pubSubData.arguments();
                                String type = c.get(0).textValue();
                                String new_message = c.get(1).textValue();
                                String type_user = c.get(2).textValue();

                                Intent intent = new Intent();
                                intent.setAction("com.libreERP.TYPING");
                                intent.putExtra("type",type);
                                intent.putExtra("new_message",new_message);
                                intent.putExtra("type_user",type_user);

                                if (type.equals("M") ){

                                    intent.putExtra("msgPK" , c.get(3).toString());


                                    final int msgPK =  Integer.parseInt(c.get(3).toString());


                                    // if the username is not prsent in chatroom then create new chatRoom else update the last Messsage;

                                   try{
                                       Thread thread = new Thread() {
                                           public void run() {
                                               Looper.prepare();

                                               final Handler handler = new Handler();
                                               handler.postDelayed(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       fetchNotificationData(msgPK);
                                                       handler.removeCallbacks(this);
                                                       //Looper.myLooper().quit();
                                                   }
                                               }, 2000);

                                               Looper.loop();
                                           }
                                       };
                                       thread.start();

                                   }catch (rx.exceptions.OnErrorNotImplementedException e){
                                       System.out.println("failure");
                                   }







                                }

                                sendBroadcast(intent);

                            }
                        });
//                        client.publish("service.chat.admin", "{'key': 'some text messag'}");
                    }
                }

            });

            client.open();


        }catch (ws.wamp.jawampa.ApplicationError e){
            String done = "ok";
        }
        //=======================

        usr = User.loadUser(context);

        ImageView image =(ImageView) navigationView.getHeaderView(0).findViewById(R.id.displayPic);

        TextView name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        if (usr.getProfilePicture()!= null){
            image.setImageBitmap(usr.getProfilePicture());
            name.setText(usr.getName());
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new HomeFragment())
                .commit();
    }

    private void fetchNotificationData(int msgPK){

        Helper helper = new Helper(context);
        httpClient = helper.getHTTPClient();
        String url = String.format("%s/%s/%s/?mode=" , helper.serverURL, "api/PIM/chatMessage" ,msgPK );

        httpClient.get(url,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    String attachement;
                    int pkOriginator;
                    final String created = response.getString("created").replace("Z", "").replace("T", " ");;
                    boolean read;
                    int pkUser;

                    System.out.println("FETCHED NEW MSG");

                    int msgPk = response.getInt("pk");
                    final String message = response.getString("message");

                    attachement = response.getString("attachment");
                    pkOriginator = response.getInt("originator");
                    read = response.getBoolean("read");
                    pkUser = response.getInt("user");
                    login = User.loadUser(context);
                    final int with_pk;
                    if (login.getPk() == pkOriginator){
                        with_pk = pkUser ;
                    }else{
                        with_pk = pkOriginator ;
                    }



                    // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));

                    addNotification(message,with_pk ,getCommitDate(created));



                } catch (JSONException e) {

                }
                // load_data_from_database(0);

                // recyclerView.scrollToPosition(messageArrayList.size()-1);

                // layoutManager.setStackFromEnd(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("failure");
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("failure");
                System.out.println(statusCode);
            }
        });;





    }

    public String getCommitDate(String timestamp) {

        Date date = new Date();
        Date currentDate = new Date() ;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String targetDate = date.toString() ;
        try {

            date = formatter.parse(timestamp);
            targetDate = (date).toString();
            if (date.getYear() == currentDate.getYear()){
                SimpleDateFormat formatter_yr = new SimpleDateFormat("hh:mm a , dd MMM");
                targetDate = formatter_yr.format(date);

                if (date.getDate() == currentDate.getDate()){
                    SimpleDateFormat formatter_day = new SimpleDateFormat("hh:mm a");
                    targetDate = formatter_day.format(date);

                }
            }
            else {
                SimpleDateFormat formatter_yr = new SimpleDateFormat("hh:mm a , dd|MM|yy");
                targetDate = formatter_yr.format(date);
            }
        } catch (ParseException e) {
            System.out.println("error while parsing date");
        }

        return targetDate ;
    }


    private void addNotification(String message ,int withPK, String time) {
        int icon = R.drawable.ic_action_home;
        long when = System.currentTimeMillis();
        int notifyID = 1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        NotificationManager manager  = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Users users = new Users(context);
        final String[] name = new String[1];
        final String[] username = new String[1];
        final Bitmap[] bp = new Bitmap[1];
        final Intent notificationIntent = new Intent(this, ChatRoomActivity.class);
        users.get(withPK, new UserMetaHandler() {
            @Override
            public void onSuccess(UserMeta user) {
                System.out.println("yes65262626626");
                name[0] = user.getFirstName() + " " + user.getLastName();
                // set text in the layout here'
                username[0] = user.getUsername();


            }

            @Override
            public void handleDP(Bitmap dp) {
                System.out.println("dp dsda");
                bp[0] = dp;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp[0].compress(Bitmap.CompressFormat.PNG, 80, stream);
                byte[] byteArray = stream.toByteArray();
                notificationIntent.putExtra("dp", byteArray);




                // set text in the layout here
            }

        });


        if (firstTime) {
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);


            contentView.setImageViewResource(R.id.notificationDp, R.drawable.ic_action_gear);
        contentView.setTextViewText(R.id.notificationTime, time);
            contentView.setTextViewText(R.id.notificationTitle, message);
            contentView.setTextViewText(R.id.notificationText, name[0]);

            contentView.setTextViewText(R.id.not_count, "6");
            builder.setSmallIcon(icon);
            builder.setContent(contentView);


            dba = new DBHandler(context, null, null, 1);
            int chatId = dba.getIDFromWithPk(withPK);


            notificationIntent.putExtra("with_id",Integer.toString(withPK));
            notificationIntent.putExtra("chatID",Integer.toString(chatId));
            notificationIntent.putExtra("name",name[0]);
            notificationIntent.putExtra("userName",username[0]);


            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            builder.getNotification().defaults |= Notification.DEFAULT_LIGHTS; // LED
            builder.getNotification().defaults |= Notification.DEFAULT_VIBRATE; //Vibration
            builder.getNotification().defaults |= Notification.DEFAULT_SOUND; // Sound
            builder.setAutoCancel(true);
            builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

            // Add as notification


            firstTime = false ;
        }
        builder.setContentText(message);


        manager.notify(notifyID, builder.build());
        // Add as notification

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new HomeFragment())
                    .commit();
            setTitle("Home");
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(Context context){
        File path = context.getFilesDir();
        File file = new File(path, ".libreerp.key");
        boolean deleted = file.delete();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish(); // finish activity
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,new ProfileFragment())
                    .commit();
            setTitle("Profile");

        } else if (id == R.id.nav_password) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ChangePasswordFragment())
                    .commit();
            setTitle("Change Password");
        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout(context);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AboutFragment())
                    .commit();
            setTitle("About");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (exit) {
            this.moveTaskToBack(true);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


}
