package com.example.libreerp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;
    private List<UserMeta> my_data;
    private List<Boolean> isSelected = new ArrayList<>();
    public UserListAdapter(Context context, List<UserMeta> my_data) {
        this.context = context;
        this.my_data = my_data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_card ,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserMeta um = my_data.get(position);
        isSelected.add(position,false);
        isSelected.set(position,false);
        Users users = new Users(context);
        users.get(my_data.get(position).getPkUser() , new UserMetaHandler(){
            @Override
            public void onSuccess(UserMeta user){

                holder.userName.setText(user.getFirstName() + " " + user.getLastName());
            }
            @Override
            public void handleDP(Bitmap dp){

                holder.userImage.setImageBitmap(dp);
            }

        });

        holder.user.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.libreERP.NEWCHAT");
                intent.putExtra("with_id",(my_data.get(position).getPkUser()));
                intent.putExtra("name", my_data.get(position).getFirstName() + " " + my_data.get(position).getLastName());
                context.sendBroadcast(intent);
            }
        });

        holder.user.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.selected.setVisibility(View.VISIBLE);
                if(isSelected.get(position)){
                    isSelected.set(position,false);
                    holder.selected.setChecked(isSelected.get(position));
                    holder.selected.setVisibility(View.GONE);
                }
                else {
                    isSelected.set(position, true);
                    holder.selected.setChecked(isSelected.get(position));
                }
                return true;
            }


        });

        holder.selected.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.selected.isChecked()){
                    holder.selected.setVisibility(View.GONE);
                    isSelected.set(position,false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView userName;
        ImageView userImage;
        LinearLayout user;
        CheckBox selected;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);
            user = (LinearLayout) itemView.findViewById(R.id.user);
            selected = (CheckBox) itemView.findViewById(R.id.user_select);
        }
    }
    public void clearData() {
        my_data.clear();
        notifyDataSetChanged();
    }
}