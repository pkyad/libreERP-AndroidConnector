package com.example.yadav.myapplication2;

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
    private String DisplayPicture;
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
        return DisplayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        DisplayPicture = displayPicture;
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

    public Users(int pkUsers){
        PkUsers = pkUsers;
    }
}
