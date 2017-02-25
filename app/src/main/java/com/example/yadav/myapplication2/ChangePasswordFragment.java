package com.example.yadav.myapplication2;

import android.content.Context;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yadav on 19/2/17.
 */
public class ChangePasswordFragment extends Fragment {

    View myView;
    User usr;
    private String serverURL;
    private Button changeBtn;
    private HomeActivity homeAct;

    private static EditText oldPasswordEdit;
    private static EditText newPasswordEdit;
    private static EditText confirmPasswordEdit;


    protected void changePassword(Context context){
        oldPasswordEdit = (EditText) myView.findViewById(R.id.oldPasswordEditText);
        String oldPasswordOrOTP =  oldPasswordEdit.getText().toString();

        newPasswordEdit = (EditText) myView.findViewById(R.id.newPasswordEditText);
        String newPassword =  newPasswordEdit.getText().toString();

        confirmPasswordEdit = (EditText) myView.findViewById(R.id.confirmPasswordEditText);
        String confirmPassword =  confirmPasswordEdit.getText().toString();

        AsyncHttpClient client = MainActivity.getHTTPClient(context);

        JSONObject settJson = MainActivity.getSettingsJson(context);
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
                    homeAct.logout(getActivity().getApplicationContext());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(statusCode);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.change_password_layout, container, false);

        homeAct = (HomeActivity) getActivity();
        usr = homeAct.usr;

        changeBtn = (Button) myView.findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword(homeAct.getApplicationContext());
            }
        });

        return myView;

    }

}
