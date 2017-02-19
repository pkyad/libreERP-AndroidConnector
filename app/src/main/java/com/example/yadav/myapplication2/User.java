package com.example.yadav.myapplication2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Xml;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

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
    public String getUsername(){
        return _username;
    };
    public Bitmap getProfilePicture(){
        return profilePicture;
    };

    public Integer getPk(){
        return _pk;
    };

    public void saveUserToFile(Context context){

        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try{
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag("", "user");
            xmlSerializer.startTag("", "username");
            xmlSerializer.text(_username);
            xmlSerializer.endTag("", "username");
            xmlSerializer.startTag("", "pk");
            xmlSerializer.text(_pk.toString());
            xmlSerializer.endTag("", "pk");
            xmlSerializer.startTag("", "firstName");
            xmlSerializer.text(_firstName);
            xmlSerializer.endTag("", "firstName");
            xmlSerializer.startTag("", "lastName");
            xmlSerializer.text(_lastName);
            xmlSerializer.endTag("", "lastName");
            xmlSerializer.endTag("", "user");
            xmlSerializer.endDocument();

            String xmlUser = writer.toString();

            File path = context.getFilesDir();
            File file = new File(path, ".libreerp.user");

            try {
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(xmlUser.getBytes());
                stream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            File dpFile = new File(path , ".libreerp.dp");

            try{
                FileOutputStream fOut = new FileOutputStream(dpFile);
                profilePicture.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static User loadUser(Context context){
        File path = context.getFilesDir();
        File file = new File(path, ".libreerp.user");
        int length = (int) file.length();

        byte[] bytes = new byte[length];
        String text = "", firstName = "" , lastName = "", username= "";
        Integer pk = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            FileInputStream in = new FileInputStream(file);
            parser.setInput(in, null);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("user")) {
                            // create a new instance of employee
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(text.equals("")){
                            break;
                        }
                        if (tagname.equalsIgnoreCase("username")) {
                            username = text;
                        }else if (tagname.equalsIgnoreCase("pk")) {
                            pk = Integer.parseInt(text);
                        }  else if (tagname.equalsIgnoreCase("lastName")) {
                            lastName = text;
                        } else if (tagname.equalsIgnoreCase("firstName")) {
                            firstName = text;
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {
            e.printStackTrace();
        }
        User usr = new User(username , pk);
        usr.setFirstName(firstName);
        usr.setLastName(lastName);
        File dpFile = new File(context.getFilesDir(), ".libreerp.dp");
        length = (int) dpFile.length();

        bytes = new byte[length];
        Bitmap dpBitmap = BitmapFactory.decodeFile(dpFile.getPath());
        usr.setProfilePicture(dpBitmap);

        return usr;
        
    }

}
