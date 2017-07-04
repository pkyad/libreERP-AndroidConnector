package com.example.yadav.IM;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.loopj.android.http.AsyncHttpClient;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.path;


public class ChatRoomActivity extends AppCompatActivity {

    private String TAG = ChatRoomActivity.class.getSimpleName();

    private static  String chatRoomId;
    private RecyclerView recyclerView;
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


    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (menuOpen == true){
                hideMenu();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        coverView = findViewById(R.id.coverView);
        btn_location = (ImageButton) findViewById(R.id.btn_location);
        btn_gallery = (ImageButton) findViewById(R.id.btn_gallery);
        btn_camera = (ImageButton) findViewById(R.id.btn_camera);
        btn_files = (ImageButton)findViewById(R.id.btn_files);
        card_location_image = (ImageView) findViewById(R.id.image_location);
        title_bar = (TextView) findViewById(R.id.titleText);
        context = getApplicationContext();

        inputMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                if(menuOpen ==  true){
                    hideMenu();;
                }
                coverView.setVisibility(View.INVISIBLE);
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
        btnSend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Do something in response to button click
                if (menuOpen == true)
                    hideMenu();

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


        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("with_id"); // chat Room id is same as with id here in this case
        String title = intent.getStringExtra("name");
        byte[] byteArray = getIntent().getByteArrayExtra("dp");
        Bitmap profile = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // Drawable drawable = new BitmapDrawable(getResources(), createCircleBitmap(profile));
        //Drawable drawable = new BitmapDrawable(getResources(), profile);
       // getSupportActionBar().setIcon(drawable);
        /*ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.action_bar_chatroom, null);

        title_bar.setText(title);
        CircleImageView image = (CircleImageView) findViewById(R.id.circularimageView1);
        image.setImageBitmap(profile);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);*/

        if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();
        int i;

        // self user id is to identify the message owner
        // String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();

        User login = User.loadUser(context);
        int login_pk = login.getPk();
        Bitmap bm = null ;
        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList ,login_pk);
        mAdapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setStackFromEnd(true);
        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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


       /* btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        */
       //fetchChatRooms();
        fetchChatThread();


        recyclerView.scrollToPosition(mAdapter.getItemCount()-1);


        //layoutManager.setStackFromEnd(true);
    }
    public Bitmap createCircleBitmap(Bitmap bitmapimg){
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(4*bitmapimg.getWidth() / 10,
                4*bitmapimg.getHeight() / 10, 4*bitmapimg.getWidth() / 10, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    private void fetchChatThread() {
        load_data_from_database(0);
        System.out.print("yess");


        Helper helper = new Helper(context);

        AsyncHttpClient client = helper.getHTTPClient();
        Users users = new Users(context);
        final String[] name = new String[1];

        // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));

        users.get(Integer.parseInt(chatRoomId) , new UserMetaHandler(){
            @Override
            public void onSuccess(UserMeta user){
                System.out.println("yes65262626626");
                name[0] = user.getUsername();
                // set text in the layout here
            }
            @Override
            public void handleDP(Bitmap dp){

                // set text in the layout here
            }

        });

        String url = String.format("%s/%s%s", helper.serverURL, "api/PIM/chatMessageBetween/?other=",name[0]); // i have to change it

        client.get(url, new JsonHttpResponseHandler() {
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

                    String category;
                    String text;
                    int pkCommit;
                    String commitMessage;
                    int user;
                    String CommitDate;
                    String CommitBranch;
                    String CommitCode;

                    DBHandler dba = new DBHandler(context, null, null, 1);
                    User login = User.loadUser(context);
                    int login_pk = login.getPk();

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject c = response.getJSONObject(i);
                        pkMessage = c.getInt("pk");
                        if (!dba.CheckIfMessagePKAlreadyInDBorNot(pkMessage)) { // check in table of Message
                            message = c.getString("message");
                            attachement = c.getString("attachment");
                            pkOriginator = c.getInt("originator");
                            created =  c.getString("created");
                            read = c.getBoolean("read");
                            pkUser = c.getInt("user");


                            ChatRoomTable chatRoomTable = new ChatRoomTable();
                            chatRoomTable.setPrvsMessage(true);


                            chatRoomTable.setAttachement(attachement);
                            chatRoomTable.setCreated(created);
                            chatRoomTable.setMessage(message);
                            chatRoomTable.setPkMessage(pkMessage);
                            chatRoomTable.setPkOriginator(pkOriginator);
                            chatRoomTable.setPkUser(pkUser);

                            dba.insertTableMessage(chatRoomTable);

                        }



                        // every messages will be inserted





                    }
                    mAdapter.notifyDataSetChanged();


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
    }




    private static void load_data_from_database(int id) {

        final AsyncTask<Integer, Void, Void> comment = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                final DBHandler dba = new DBHandler(context, null, null, 1); // see this
                //System.out.println("pkTask = "+pkTask);
                int entries_database_message = dba.getTotalDBEntries_MESSAGE();
                User login = User.loadUser(context);
                int login_pk = login.getPk();

                int prvs_originator_pk = login_pk;
                int margin = 1 ;
                for (int i = 0; i < entries_database_message ; i++) {

                    final ChatRoomTable data = new ChatRoomTable();
                    data.setPkOriginator(dba.message_getOriginatorPK(i));
                    data.setPkUser(dba.message_getUserPk(i));
                    if (data.getPkOriginator() == Integer.parseInt(chatRoomId) || data.getPkUser() ==  Integer.parseInt(chatRoomId)){
                        data.setPkMessage(dba.message_getMessagePK(i));
                        data.setMessage(dba.message_getMessage(i));
                        data.setAttachement(dba.message_getAttachment(i));
                        margin = 0 ;
                        if (i == 0) {
                            margin = 0;
                        }
                        else if(i > 0 && prvs_originator_pk != data.getPkOriginator() ){
                            margin = 1 ;
                        }
                        else {
                           margin = 0;// previous and same message are not from same user and hence add margin
                        }
                        String date = dba.message_getDate(i);
                        data.setCreated(date);
                        String messageDate;
                        //messageDate = new SimpleDateFormat("dd MMM, yyyy").format(date);


                        Users users = new Users(context);
                        final String[] name = new String[1];
                        final Bitmap[] bp = new Bitmap[1];
                        // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));




                        UserMeta usermeta = new UserMeta(data.getPkOriginator());
                        Message message = new Message(Integer.toString(data.getPkMessage()),data.getMessage(),data.getCreated(),usermeta);
                        if (margin == 1){
                            message.setMargin(true);
                        }
                        else {
                            message.setMargin(false);
                        }
                        //chatRoom.setDP(bp[0]);
                        messageArrayList.add(message);
                        prvs_originator_pk = data.getPkOriginator();
                    }

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
                mAdapter.notifyDataSetChanged();
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


              //  Message dummy = new Message("1","","1 am" ,user1);
              //  dummy.setLocation(1); // 1 is for location
              //  messageArrayList.add(dummy) ;
              //  mAdapter.notifyDataSetChanged();



            }

        }
        Bitmap bm = null ;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bm = (Bitmap) data.getExtras().get("data");




           // Message dummy = new Message("1","","1 am" ,user1);
           // dummy.setLocation(3);// 3 is for camera
           // dummy.setBm(bm);
           // messageArrayList.add(dummy) ;
           // mAdapter.notifyDataSetChanged();
           // saveImageTOExternalStorage(bm);

        }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
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
            String Fpath = data.getData().getPath();
            String filename=Fpath.substring(Fpath.lastIndexOf("/")+1);
            String extension = Fpath.substring(Fpath.lastIndexOf(".") + 1);
           // Message dummy = new Message("1",Fpath,"1 am" ,user1);
            extension = "pdf";
            if (extension.equals("pdf") == true || extension.equals("PDF") == true || extension.equals(".pdf") == true ||  extension.equals(".PDF") == true ){
           //     dummy.setLocation(5); // 5 is for pdf
            }
            else {
          //      dummy.setLocation(6); // 6 is for other document
            }
         //   messageArrayList.add(dummy) ;
            mAdapter.notifyDataSetChanged();

        }

    }








    public  Bitmap getGoogleMapThumbnail(double lati, double longi){
        String URL = "http://maps.google.com/maps/api/staticmap?center=" +lati + "," + longi + "&zoom=15&size=200x200&sensor=false";
        Bitmap bmp = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        InputStream in = null;
        try {
            in = httpclient.execute(request).getEntity().getContent();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp;
    }
    public Bitmap getBitmapFromURL(double lati , double longi) {
        String src ="http://maps.google.com/maps/api/staticmap?center=" +lati + "," + longi + "&zoom=15&size=200x200&sensor=false";
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public  Bitmap getBitmap(double lati , double longi) {
        String url ="http://maps.google.com/maps/api/staticmap?center=" +lati + "," + longi + "&zoom=15&size=200x200&sensor=false" ;
        try {
            InputStream is =  new java.net.URL(url).openStream();
            Bitmap d = BitmapFactory.decodeStream(is);
            is.close();
            return d;
        } catch (Exception e) {

            return null;
        }
    }




    private void revealMenu() {
        menuOpen = true;
        theMenu.setVisibility(View.VISIBLE);
    }


    private void hideMenu(){
        menuOpen = false;
        theMenu.setVisibility(View.INVISIBLE);
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

        File path = this.getFilesDir();
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
        ;

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

