package com.example.yadav.myapplication2;

import android.graphics.Bitmap;


/**
 * Created by yadav on 17/2/17.
 */
public class User {
    private String _username;
    private String _firstName;
    private String _lastName;
    private Integer _pk;
    private String _displayPictureUrl;
    private Bitmap profilePicture;

    User(String username , int pk){
        _username = username;
        _pk = pk;
    }

    public void setFirstName(String firstName){
        _firstName = firstName;
    }
    public void setLastName(String lastName){
        _lastName = lastName;
    }

    public void setProfilePicture(Bitmap pp){
        profilePicture = pp;
    }

    public String getName(){
      return String.format("%s %s", _firstName, _lastName);
    };
    public Bitmap getProfilePicture(){
        return profilePicture;
    };

    public Integer getPk(){
        return _pk;
    };


}
