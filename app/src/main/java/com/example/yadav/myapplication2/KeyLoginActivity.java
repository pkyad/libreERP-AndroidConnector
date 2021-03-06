package com.example.yadav.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.Inet4Address;

public class KeyLoginActivity extends AppCompatActivity {
    private EditText editTextKey11;
    private EditText editTextKey12;
    private EditText editTextKey13;
    private EditText editTextKey14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_login);



        View.OnKeyListener goToNextKey = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                System.out.print(i);
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i != KeyEvent.KEYCODE_DEL ) {
                    //do something here
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
        editTextKey14.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                String key11 = editTextKey11.getText().toString();
                String key12 = editTextKey12.getText().toString();
                String key13 = editTextKey13.getText().toString();
                String key14 = editTextKey14.getText().toString();


                if (String.format("%s%s%s", key11,key12, key13).length() <3){
                    return true;
                }
                String keyText = String.format("%s%s%s%s", key11,key12, key13,(char) keyEvent.getUnicodeChar());

                Context context = KeyLoginActivity.this.getApplicationContext();

                JSONObject settJson = MainActivity.getSettingsJson(context);
                try{
                    String storedKeyText = settJson.getString("keyText");
                    if (storedKeyText.equals(keyText)){
                        Intent intent = new Intent(context, HomeActivity.class);
                        startActivity(intent);
                    }else {
//                        Toast.makeText(KeyLoginActivity.this, "Incorrect Key!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }catch (JSONException e){

                }
                return true;
            };
        });

//        InputMethodManager inputMethodManager =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInputFromWindow(editTextKey11.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//        editTextKey11.requestFocus();

    }
}
