package com.example.yadav.myapplication2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yadav on 19/2/17.
 */
public class HomeFragment extends Fragment {

    View myView;
    private FragmentManager homeFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeFragmentManager = getFragmentManager();
        homeFragmentManager.beginTransaction()
                .replace(R.id.content_frame_home, new HomeToDoFragment())
                .commit();
        // implement the on click event lister for the bottom navigation plane

//        Intent intent = new Intent(this.getContext(), TaskViewActivity.class);
//        startActivity(intent);
//        getActivity().finish(); // finish activity

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_home , container, false);

        setHasOptionsMenu(true);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                myView.findViewById(R.id.bottom_navigation);


        bottomNavigationView.getMenu().getItem(0).setChecked(false);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.getMenu().getItem(2).setChecked(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.action_home_home) {
                            System.out.println("home");
                            homeFragmentManager.beginTransaction()
                                    .replace(R.id.content_frame_home, new HomeHomeFragment())
                                    .commit();

                        }else if (id == R.id.action_home_todo){
                            System.out.println("Todo");
                            homeFragmentManager.beginTransaction()
                                    .replace(R.id.content_frame_home, new HomeToDoFragment())
                                    .commit();
                        }else if (id == R.id.action_home_project){
                            homeFragmentManager.beginTransaction()
                                    .replace(R.id.content_frame_home, new HomeProjectFragment())
                                    .commit();
                        }
                        return true;
                    }
                });

        return myView;
    }

}
