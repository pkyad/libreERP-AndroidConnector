package com.example.yadav.taskBoard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.libreerp.User;
import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by cioc on 29/6/17.
 */

public class CustomFileCardAdapter extends ArrayAdapter<File> {


    static Context context;
    int layoutResourceId;
    static ProgressDialog pDialog;
    boolean called = false;
    public CustomFileCardAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        pDialog = new ProgressDialog(context);
    }

    private class ViewHolder {

        TextView uploader;
        TextView fileTitle;
        TextView time;
        ImageView fileType;
        RelativeLayout layout;

    }

    ViewHolder holder = null;

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new CustomFileCardAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fileview, parent, false);
            holder.fileTitle = (TextView) convertView.findViewById(R.id.fileTitle);
            holder.uploader = (TextView) convertView.findViewById(R.id.uploader);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.fileType = (ImageView) convertView.findViewById(R.id.fileType);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.fileCard) ;
            convertView.setTag(holder);

        } else
            holder = (CustomFileCardAdapter.ViewHolder) convertView.getTag();
        final File card = getItem(position);
        Users users = new Users(getContext());
        users.get(card.getPostedUser() , new UserMetaHandler(){
            @Override
            public void onSuccess(UserMeta user){
                holder.uploader.setText(user.getFirstName() + " " + user.getLastName());
            }

        });
        holder.fileTitle.setText(card.getName());
        DBHandler db = new DBHandler(context,null,null,1);
        Date dateCreated = db.getFileUploadDate(card.getFilePk());
        Date current = new Date();
        String formattedCreatedDate;
        if(dateCreated.getYear() == current.getYear()) {
            formattedCreatedDate = new SimpleDateFormat("dd MMM").format(dateCreated);
        }
        else {
            formattedCreatedDate = new SimpleDateFormat("dd MMM, yyyy").format(dateCreated);
        }
        holder.time.setText(formattedCreatedDate);

        String[] split = card.getName().split("\\.");
        if(split[1].equals("png") || split[1].equals("jpg") || split[1].equals("jpeg")){
            holder.fileType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_image));
        }
        else if(split[1].equals("pdf")){
            holder.fileType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_pdf));
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(card.getAttachment()));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }

        });

        return convertView;
    }

}
