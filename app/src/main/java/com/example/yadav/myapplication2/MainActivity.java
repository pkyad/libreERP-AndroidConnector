package com.example.yadav.myapplication2;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.view.View;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
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
                
// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                String toFind = "csrfmiddlewaretoken";
                                int csrfStart = response.indexOf(toFind);
                                String csrfTocken = response.substring(csrfStart+28, csrfStart+60);
                                System.out.println(csrfTocken);

                                // send multi part form data


                                // send multi part form data ends here

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("res" , error.getMessage());

                    }
                });
// Add the request to the RequestQueue.
                queue.add(stringRequest);
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
