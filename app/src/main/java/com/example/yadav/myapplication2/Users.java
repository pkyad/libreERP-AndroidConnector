package com.example.yadav.myapplication2;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by cioc on 10/6/17.
 */

public class Users {

    private int PkUsers;
    private String Username;
    private String FirstName;
    private String LastName;
    private int Designation;
    private int Social;
    private String ProfilePictureLink;
    private Bitmap ProfilePicture;

    public void setProfilePicture(Bitmap pp){
        ProfilePicture = pp;
    }

    public Bitmap getProfilePicture(){
        return ProfilePicture;
    };

    public String getFirstName() {
        return FirstName;
    }

    public int getDesignation() {
        return Designation;
    }

    public int getPkUsers() {
        return PkUsers;
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
    public String getDisplayPicture(){
        return ProfilePictureLink;
    }

    public void setDisplayPicture(String displayPicture) {
        ProfilePictureLink = displayPicture;
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
        PkUsers = pkUsers;
    }

    public void setSocial(int social) {
        Social = social;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Users(int PkUsers){
        this.PkUsers = PkUsers;
    }

    public void saveUserToFile(Context context){


            File path = context.getFilesDir();

            File dpFile = new File(path , getUsername());

            try{
                FileOutputStream fOut = new FileOutputStream(dpFile);
                ProfilePicture.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

    }
}
