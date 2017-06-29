package com.example.yadav.IM;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libreerp.UserMeta;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;

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

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

import static android.R.attr.path;


public class ChatRoomActivity extends AppCompatActivity {

    private String TAG = ChatRoomActivity.class.getSimpleName();

    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private Button btnSend;
    private Button btnAttach;
    private GoogleMap mMap;
    private ImageView card_location_image;
    private boolean menuOpen = false;

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
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    private static final int CAMERA_REQUEST = 1024 ;
    private static final int GALLERY_REQUEST = 2134 ;
    private static final int CHOOSE_FILE_REQUESTCODE = 4512 ;
    private static final int PLACE_PICKER_REQUEST = 1000;
    UserMeta user1 = new UserMeta(1);


    UserMeta user2 = new UserMeta(2);

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
        user1.setFirstName("Chandler");
        user1.setLastName("Bing");

        user2.setFirstName("Joey");
        user2.setLastName("tri");

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
        chatRoomId = intent.getStringExtra("chat_room_id");
        String title = intent.getStringExtra("name");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();
        int i;

        // self user id is to identify the message owner
        // String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();
        int selfUserId = 1;
        Bitmap bm = null ;
        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList ,selfUserId);
        mAdapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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

        fetchChatThread();*/
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


                Message dummy = new Message("1","","1 am" ,user1);
                dummy.setLocation(1); // 1 is for location
                messageArrayList.add(dummy) ;
                mAdapter.notifyDataSetChanged();



            }

        }
        Bitmap bm = null ;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bm = (Bitmap) data.getExtras().get("data");




            Message dummy = new Message("1","","1 am" ,user1);
            dummy.setLocation(3);// 3 is for camera
            dummy.setBm(bm);
            messageArrayList.add(dummy) ;
            mAdapter.notifyDataSetChanged();
            saveImageTOExternalStorage(bm);

        }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    card_location_image.setImageBitmap(bm);

                    Message dummy = new Message("1","","1 am" ,user1);
                    dummy.setLocation(2); // 2 is for gallery
                    dummy.setBm(bm);
                    messageArrayList.add(dummy) ;
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
            Message dummy = new Message("1",filename,"1 am" ,user1);

            if (extension.equals("pdf") == true || extension.equals("PDF") == true || extension.equals(".pdf") == true ||  extension.equals(".PDF") == true ){
                dummy.setLocation(5); // 5 is for pdf
            }
            else {
                dummy.setLocation(6); // 6 is for other document
            }
            messageArrayList.add(dummy) ;
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

