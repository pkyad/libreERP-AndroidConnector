package com.example.yadav.myapplication2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LOG_TAG = "GetSample";

    private CheckBox chkKeys;
    private ImageButton settBtn;
    private EditText editTextKey11;
    private EditText editTextKey12;
    private EditText editTextKey13;
    private EditText editTextKey14;
    private EditText editTextKey21;
    private EditText editTextKey22;
    private EditText editTextKey23;
    private EditText editTextKey24;
    private Button loginBtn;
    private User user;
    private File file;

    Context context;
    private CookieStore httpCookieStore;
    private AsyncHttpClient client;


    private static final String serverURL = "http://pradeepyadav.net";

    protected void login(final String username , final String password){
        client.get(serverURL+ "/login", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                System.out.println(statusCode);
                RequestParams params = new RequestParams();
                try {
                    Header hdr = headers[5];
                    String headerStr = hdr.getValue();

                    Pattern pattern = Pattern.compile("csrftoken=(.*?);");
                    Matcher matcher = pattern.matcher(headerStr);
                    String csrftoken = "";
                    while (matcher.find()) {
                        csrftoken = matcher.group(1);
                    }
                    params.put("csrfmiddlewaretoken", csrftoken);
                }catch (ArrayIndexOutOfBoundsException e){

                }

                params.put("username", username);
                params.put("password", password);

                client.post(serverURL+ "/login", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        System.out.println("on failure");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        System.out.println("on failure");
                        System.out.println(statusCode);
                    }
                    @Override
                    public void onFinish() {
                        // saving the new cookies to the SD card , in future release this
                        // should be encrypted and then saved to key storage system of the OS
                        List<Cookie> lst = httpCookieStore.getCookies();
                        Cookie csrfCookie = lst.get(0);
                        Cookie sessionCookie = lst.get(1);

                        String csrf = csrfCookie.getValue();
                        String session = sessionCookie.getValue();
                        String keys = String.format("%s\n%s",csrf , session) ;

                        // writing the new keys
                        try {
                            FileOutputStream stream = new FileOutputStream(file);
                            stream.write(keys.getBytes());
                            stream.close();
                        }catch (IOException e){

                        }

                        getUser();
                    }
                });
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("failure");
                System.out.println(statusCode);

            }
        });
    }

    protected void getUser(){
        client.get(serverURL + "/api/HR/users/?mode=mySelf", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    JSONObject usrObj = response.getJSONObject(0);
                    String username = usrObj.getString("username");
                    String firstName = usrObj.getString("first_name");
                    Integer pk = usrObj.getInt("pk");
                    String lastName = usrObj.getString("last_name");
                    JSONObject profileObj = usrObj.getJSONObject("profile");
                    String DPLink = profileObj.getString("displayPicture");

                    user = new User(username ,pk);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);


                    client.get(DPLink, new FileAsyncHttpResponseHandler(context) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            // Do something with the file `response`
                            Bitmap pp = BitmapFactory.decodeFile(file.getAbsolutePath());
                            user.setProfilePicture(pp);
                            user.saveUserToFile(context);
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers,Throwable e, File file) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            System.out.println("failure");
                            System.out.println(statusCode);
                        }
                    });

                    System.out.println(username);
                }catch (JSONException e){
                    throw  new RuntimeException(e);
                }
            }
            @Override
            public void onFinish() {
                System.out.println("finished 001");

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                System.out.println("finished failed 001");
                login("admin", "indiaerp");
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this.getApplicationContext();
        httpCookieStore = new PersistentCookieStore(context);
        client = new AsyncHttpClient();

        File path = context.getFilesDir();
        file = new File(path, ".libreerp.key");

        loginBtn = (Button) findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // reading the existing keys

                int length = (int) file.length();

                byte[] bytes = new byte[length];

                try {
                    FileInputStream in = new FileInputStream(file);
                    in.read(bytes);
                    in.close();
                }catch (FileNotFoundException e){

                }catch (IOException e){

                }

                String contents = new String(bytes);
                String[] keysArr = contents.split("\n");
                final String csrftoken = keysArr[0];
                final String sessionid = keysArr[1];




                BasicClientCookie newCsrftokenCookie = new BasicClientCookie("csrftoken", csrftoken);
                newCsrftokenCookie.setVersion(1);
                newCsrftokenCookie.setDomain(serverURL);
                newCsrftokenCookie.setPath("/");
                httpCookieStore.addCookie(newCsrftokenCookie);

                BasicClientCookie newSessionidtokenCookie = new BasicClientCookie("sessionid", sessionid);
                newSessionidtokenCookie.setVersion(1);
                newSessionidtokenCookie.setDomain(serverURL);
                newSessionidtokenCookie.setPath("/");
                httpCookieStore.addCookie(newSessionidtokenCookie);

                client.setCookieStore(httpCookieStore);


                getUser();



            }
        });

        settBtn = (ImageButton) findViewById(R.id.imageButtonSettings);

        settBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("ERP Server");
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
                builder.setView(input);

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String m_Text = input.getText().toString();
                                System.out.println(m_Text);
                            }
                        });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.setIcon(R.drawable.ic_action_gear);

                builder.show();
            }
        });

        LinearLayout passKeysLayout=(LinearLayout) findViewById(R.id.linearLayoutPassKeys);
        passKeysLayout.setVisibility(LinearLayout.GONE);

        chkKeys = (CheckBox) findViewById(R.id.passKeyCheckBox);

        if(chkKeys.isChecked()){
            chkKeys.toggle();
        }

        chkKeys.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkKeys checked?
                LinearLayout passKeysLayout=(LinearLayout) findViewById(R.id.linearLayoutPassKeys);
                if (((CheckBox) v).isChecked()) {
                    passKeysLayout.setVisibility(LinearLayout.VISIBLE);
                }else{
                    passKeysLayout.setVisibility(LinearLayout.GONE);
                }

            }
        });

        View.OnKeyListener goToNextKey = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                System.out.print(i);
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i != KeyEvent.KEYCODE_DEL ) {
                    //do something here
                    Log.i(TAG, "Keyed");
                    int id = getCurrentFocus().getNextFocusDownId();
                    System.out.println(id);
                    if(id != View.NO_ID) {
                        findViewById(id).requestFocus();
                        System.out.println("Next");
                    }
                    return false;
                }
                return false;
            }
        };

        editTextKey11 = (EditText) findViewById(R.id.editTextKey11);
        editTextKey11.setOnKeyListener(goToNextKey);
        editTextKey12 = (EditText) findViewById(R.id.editTextKey12);
        editTextKey12.setOnKeyListener(goToNextKey);
        editTextKey13 = (EditText) findViewById(R.id.editTextKey13);
        editTextKey13.setOnKeyListener(goToNextKey);
        editTextKey14 = (EditText) findViewById(R.id.editTextKey14);
        editTextKey14.setOnKeyListener(goToNextKey);

        editTextKey21 = (EditText) findViewById(R.id.editTextKey21);
        editTextKey21.setOnKeyListener(goToNextKey);
        editTextKey22 = (EditText) findViewById(R.id.editTextKey22);
        editTextKey22.setOnKeyListener(goToNextKey);
        editTextKey23 = (EditText) findViewById(R.id.editTextKey23);
        editTextKey23.setOnKeyListener(goToNextKey);
        editTextKey24 = (EditText) findViewById(R.id.editTextKey24);
        editTextKey24.setOnKeyListener(goToNextKey);


    }
}
