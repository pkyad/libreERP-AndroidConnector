package com.example.libreerp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libreerp.Helper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yadav on 12/6/17.
 */






public class Users {
    private Context context;
    private DBHandler dbHandler;
    private Helper helper;
    public Users(Context cntxt){
        context = cntxt;
        dbHandler = new DBHandler(context,null,null,1);
        helper = new Helper(context);
    }


    public void cleanDB(){
        dbHandler.cleanUsers();
    }

    public void get(int pk ,final UserMetaHandler umh){

        //cleanDB();

        // check in the DB
        if(!dbHandler.CheckIfUserIsInDatabase(pk)){
            final AsyncHttpClient client = helper.getHTTPClient();

            String url = String.format("%s/%s/%s/" , helper.serverURL, "api/HR/userSearch" , pk);
            client.get( url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    System.out.println(statusCode);
                    if (statusCode == 200){
                        try {

                            int userpk;
                            String username;
                            String firstName;
                            String lastName;
                            int designation;
                            int social;
                            final String displaypicture;

                            userpk = response.getInt("pk");
                            username = response.getString("username");
                            firstName = response.getString("first_name");
                            lastName = response.getString("last_name");
                            designation = response.getInt("designation");
                            social = response.getInt("social");
                            JSONObject profile = response.getJSONObject("profile");
                            displaypicture = profile.getString("displayPicture");

                            final UserMeta userMeta = new UserMeta(userpk);
                            userMeta.setDesignation(designation);
                            userMeta.setProfilePictureLink(displaypicture);
                            userMeta.setFirstName(firstName);
                            userMeta.setLastName(lastName);
                            userMeta.setPkUsers(userpk);
                            userMeta.setSocial(social);
                            userMeta.setUsername(username);

                            if (displaypicture.equals("null")){
                                File defaultDPFile = new File(context.getFilesDir(), "defaultDP.png");
                                if(!defaultDPFile.exists()){
                                    String defaultDPURL = String.format("%s/%s" , helper.serverURL, "static/images/userIcon.png");
                                    client.get(defaultDPURL, new FileAsyncHttpResponseHandler(context) {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, File file) {
                                            // Do something with the file `response`
                                            Bitmap pp = BitmapFactory.decodeFile(file.getAbsolutePath());

                                            String dpFileName = displaypicture.split("/")[displaypicture.split("/").length-1];

                                            userMeta.saveDPOnSD(context, pp, dpFileName);
                                            umh.handleDP(pp);
                                        }
                                        @Override
                                        public void onFailure(int statusCode, Header[] headers,Throwable e, File file) {
                                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                            System.out.println("failure");
                                            System.out.println(statusCode);
                                        }
                                    });;




                                }else{
                                    Bitmap dpBitmap = BitmapFactory.decodeFile(defaultDPFile.getPath());
                                    umh.handleDP(dpBitmap);
                                }

                            }else{// get the default image for the User

                                client.get(displaypicture, new FileAsyncHttpResponseHandler(context) {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, File file) {
                                        // Do something with the file `response`
                                        Bitmap pp = BitmapFactory.decodeFile(file.getAbsolutePath());

                                        String dpFileName = displaypicture.split("/")[displaypicture.split("/").length-1];

                                        userMeta.saveDPOnSD(context, pp, dpFileName);
                                        umh.handleDP(pp);
                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers,Throwable e, File file) {
                                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                        System.out.println("failure");
                                        System.out.println(statusCode);
                                    }
                                });;
                            }

                            dbHandler.insertTableUsers(userMeta);

                            umh.onSuccess(userMeta);

                            // set it here
                            System.out.println("Inserted users successfully");
                        } catch (final JSONException e) {
                            Log.e("TAG", "Json parsing error: " + e.getMessage());

                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                    System.out.println(statusCode);
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            UserMeta userMeta = dbHandler.getUser(pk);
            umh.onSuccess(userMeta);

            File DPFolder = new File(context.getFilesDir() , "DPs");
            String dpFileName = userMeta.getDisplayPictureLink().split("/")[userMeta.getDisplayPictureLink().split("/").length-1];
            File dpFile = new File(DPFolder, dpFileName);
            Bitmap dpBitmap = BitmapFactory.decodeFile(dpFile.getPath());
            umh.handleDP(dpBitmap);


        };

    };

}
