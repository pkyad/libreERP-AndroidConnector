package com.example.libreerp;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by yadav on 12/6/17.
 */
public class Helper {
    private static File keyFile;
    private static File settingsFile;
    public static String settingsFileName = ".libreerp.settings";
    public static String keyFileName = ".libreerp.key";

    public static String serverURL = "";//http://pradeepyadav.net
    private static Context context;

    public Helper(Context cntx){

        File path = cntx.getFilesDir();
        settingsFile = new File(path, settingsFileName);
        keyFile = new File(path, keyFileName);
        context = cntx;
        loadConfigFile(cntx);
    }

    protected void loadConfigFile(Context context){
        int length = (int) settingsFile.length();
        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(settingsFile);
            in.read(bytes);
            in.close();
        }catch (FileNotFoundException e){
            // write the configuration here
        }catch (IOException e){

        }
        String contents = new String(bytes);

        try{
            JSONObject settJson = new JSONObject(contents);
            serverURL = settJson.getString("domain");
        }catch (JSONException e){
            Toast.makeText(context, "Settings not found!", Toast.LENGTH_SHORT).show();
        }
    }


    public AsyncHttpClient getHTTPClient(){
        // reading the existing keys

        int length = (int) keyFile.length();

        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(keyFile);
            in.read(bytes);
            in.close();
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }

        String contents = new String(bytes);
        String[] keysArr = contents.split("\n");
        final String csrftoken = keysArr[0];
        final String sessionid = keysArr[1];

        CookieStore httpCookieStoreSt = new PersistentCookieStore(context.getApplicationContext());
        httpCookieStoreSt.clear();
        AsyncHttpClient clientSt = new AsyncHttpClient();

        String slimedUrl = serverURL.replace("http://", "").replace("https://", "");

        BasicClientCookie newCsrftokenCookie = new BasicClientCookie("csrftoken", csrftoken);
        newCsrftokenCookie.setDomain(slimedUrl);
        newCsrftokenCookie.setPath("/");
        httpCookieStoreSt.addCookie(newCsrftokenCookie);
        BasicClientCookie newSessionidtokenCookie = new BasicClientCookie("sessionid", sessionid);
        newSessionidtokenCookie.setDomain(slimedUrl);
        newSessionidtokenCookie.setPath("/");
        httpCookieStoreSt.addCookie(newSessionidtokenCookie);
        clientSt.addHeader("X-CSRFTOKEN" , csrftoken);
        clientSt.setCookieStore(httpCookieStoreSt);

        return clientSt;
    };

    protected void writeConfigFile(String serverURL , String keyText){

        JSONObject settJson = new JSONObject();
        try{
            settJson.put("domain" , serverURL );
            settJson.put("keyText" , keyText );
        }catch (JSONException e){
        };

        try {
            FileOutputStream stream = new FileOutputStream(settingsFile);
            stream.write(settJson.toString().getBytes());
            stream.close();
        }catch (IOException e){
        }

    };

    public static JSONObject getSettingsJson(){

        int length = (int) settingsFile.length();
        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(settingsFile);
            in.read(bytes);
            in.close();
        }catch (IOException e){

        }
        String contents = new String(bytes);
        try{
            JSONObject settJson = new JSONObject(contents);
            return settJson;
        }catch (JSONException e){
            return new JSONObject();
        }
    }

}
