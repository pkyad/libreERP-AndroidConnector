package com.example.yadav.myapplication2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libreerp.User;

/**
 * Created by yadav on 19/2/17.
 */
public class ProfileFragment extends Fragment {

    View myView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        MenuItem itemSort = menu.getItem(1);
        itemSort.setVisible(false);
        MenuItem itemFilter = menu.getItem(2);
        itemFilter.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
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
