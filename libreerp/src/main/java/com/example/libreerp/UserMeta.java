package com.example.libreerp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by cioc on 10/6/17.
 */

public class UserMeta {

    private int pk;
    private String Username;
    private String FirstName;
    private String LastName;
    private int Designation;
    private int Social;
    private String ProfilePictureLink;
    private Bitmap ProfilePicture;

    private Bitmap getProfilePicture(){
        return ProfilePicture;
    };

    public String getFirstName() {
        return FirstName;
    }

    public int getDesignation() {
        return Designation;
    }

    public int getPkUser() {
        return pk;
    }

    public int getSocial() {
        return Social;
    }


    public String getLastName() {
        return LastName;
    }

    public String getUsername() {
        return Username;
    }
    public String getDisplayPictureLink(){
        return ProfilePictureLink;
    }

    public void setProfilePictureLink(String link) {
        ProfilePictureLink = link;
    }

    public void setDesignation(int designation) {
        Designation = designation;
    }


    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setPkUsers(int pkUsers) {
        pk = pkUsers;
    }

    public void setSocial(int social) {
        Social = social;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public UserMeta(int pk){
        this.pk = pk;
    }

    public void saveDPOnSD(Context context, Bitmap pp, String dpFileName){

        File DPFolder = new File(context.getFilesDir() , "DPs");
        if (!DPFolder.exists()){
            DPFolder.mkdir();
        }

        File dpFile = new File(DPFolder, dpFileName);

        try{
            FileOutputStream fOut = new FileOutputStream(dpFile);
            pp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}