package com.example.libreerp;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libreerp.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * Created by yadav on 19/2/17.
 */
public class ChangePasswordFragment extends Fragment {

    View myView;
    User usr;
    private String serverURL;
    private Button changeBtn;
    private Context context;
    private Helper helper;

    private static EditText oldPasswordEdit;
    private static EditText newPasswordEdit;
    private static EditText confirmPasswordEdit;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here

        context = getActivity();
        helper = new Helper(context);
        usr = User.loadUser(context);

        MenuItem itemSort = menu.getItem(1);
        itemSort.setVisible(false);
        MenuItem itemFilter = menu.getItem(2);
        itemFilter.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void changePassword(){
        oldPasswordEdit = (EditText) myView.findViewById(R.id.oldPasswordEditText);
        String oldPasswordOrOTP =  oldPasswordEdit.getText().toString();

        newPasswordEdit = (EditText) myView.findViewById(R.id.newPasswordEditText);
        String newPassword =  newPasswordEdit.getText().toString();

        confirmPasswordEdit = (EditText) myView.findViewById(R.id.confirmPasswordEditText);
        String confirmPassword =  confirmPasswordEdit.getText().toString();

        final AsyncHttpClient client = helper.getHTTPClient();

        JSONObject settJson = helper.getSettingsJson();
        try{
            serverURL = settJson.getString("domain");
        }catch (JSONException e){
            System.out.println("Error while getting the domain from settings");
        }
        RequestParams params = new RequestParams();

        if (!confirmPassword.equals(newPassword)){
            Toast.makeText(context, "Confirm password not same as the new password!. Please check again", Toast.LENGTH_SHORT).show();
            return;
        }

        params.put("password", newPassword);
        params.put("oldPassword",oldPasswordOrOTP);




        String url = String.format("%s/%s/%s/" , serverURL, "api/HR/users" , usr.getPk());
        client.patch( url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println(statusCode);
                if (statusCode == 200){
                    confirmPasswordEdit.setText("");
                    newPasswordEdit.setText("");
                    oldPasswordEdit.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "Password changed succesfully!", Toast.LENGTH_SHORT).show();
                    File path = context.getFilesDir();
                    File file = new File(path, ".libreerp.key");
                    final boolean deleted = file.delete();
                    String url = String.format("%s/logout" , serverURL);
                    client.get(url , new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            // called when response HTTP status is "200 OK"
                            System.out.println("on success");
                            if(deleted){
                                // send a broadcast
                                Intent intent = new Intent();
                                intent.setAction("com.libreERP.RESTART_APP");
                                getActivity().sendBroadcast(intent);

                            }
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            System.out.println("on failure");

                        }

                    });



                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(statusCode);
                String res = new String(responseBody);
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.change_password_layout, container, false);
        setHasOptionsMenu(true);

        changeBtn = (Button) myView.findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        return myView;

    }

}
