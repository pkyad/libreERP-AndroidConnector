package com.example.yadav.myapplication2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yadav on 19/2/17.
 */
public class ProfileFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_profile, container, false);

        User usr = User.loadUser(this.getActivity().getApplicationContext());

        TextView userName = (TextView) myView.findViewById(R.id.username);
        TextView fName = (TextView) myView.findViewById(R.id.fullName);
        ImageView image =(ImageView) myView.findViewById(R.id.displayPic);
        fName.setText(usr.getName());
        userName.setText(String.format("@%s", usr.getUsername()));
        image.setImageBitmap(usr.getProfilePicture());

        return myView;
    }

}
