package com.example.yadav.myapplication2;

import android.content.DialogInterface;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;

import org.json.JSONObject;

import java.util.List;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginButton);



        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="http://pradeepyadav.net/login";
                JSONObject params = new JSONObject();



                final AsyncHttpClient client = new AsyncHttpClient();

                final PersistentCookieStore myCookieStore = new PersistentCookieStore(MainActivity.this);

                client.setCookieStore(myCookieStore);
                myCookieStore.clear();
                client.get("http://192.168.0.100/logout", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                        // called when response HTTP status is "200 OK"
                        System.out.println(statusCode);
                        System.out.println("on success - logout");

                        //"X-CSRFToken"
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        System.out.println("on failure - logout");
                        System.out.println(statusCode);
                    }
                });
                client.removeAllHeaders();
                client.get("http://192.168.0.100/login", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        Header hdr = headers[5];
                        Log.i("extracted" , hdr.getValue());

                        List<Cookie> cooks =  myCookieStore.getCookies();
                        Cookie c = cooks.get(0);
                        String csrfToken = c.getValue();

                        RequestParams params = new RequestParams();
                        params.put("username", "admin");
                        params.put("password", "indiaerp");
                        params.put("csrfmiddlewaretoken", csrfToken);

                        Log.i("csrf token before login" , csrfToken);

                        client.removeAllHeaders();
                        //myCookieStore.clear();
                        client.post("http://192.168.0.100/login", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                // called when response HTTP status is "200 OK"
                                System.out.println("on onSuccess");
                                System.out.println(headers.getClass());

                                Header hdr = headers[5];
                                Log.i("extracted" , hdr.getValue());
                                // i need to get the new csrf token here and update the persistant cookie store


                                //"X-CSRFToken"


                                //client.addHeader("X-CSRFToken" , csrfToken);

                                client.get("http://192.168.0.100/api/HR/users/?mode=mySelf", new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                        // called when response HTTP status is "200 OK"
                                        System.out.println(statusCode);
                                        //"X-CSRFToken"
                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                        System.out.println("on failure");
                                        System.out.println(statusCode);
                                        List<Cookie> cooks =  myCookieStore.getCookies();
                                        Cookie c = cooks.get(0);
                                        String csrfToken = c.getValue();
                                        Log.i("csrf after get faiure" , csrfToken);

                                    }
                                });
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                System.out.println("on failure");
                                System.out.println(statusCode);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        System.out.println("on failure");
                        System.out.println(statusCode);
                    }
                });

                Log.i("TAG" , "clicked");
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
