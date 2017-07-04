package com.example.yadav.taskBoard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yadav on 19/2/17.
 */
public class HomeFragment extends Fragment {

    View myView;
    private FragmentManager fragmentManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame_home, new HomeHomeFragment())
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_home , container, false);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) myView.findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home_home:
                                System.out.println("home");
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame_home, new HomeHomeFragment())
                                        .commit();
                                break;
                            case R.id.action_home_todo:
                                System.out.println("todo");

                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame_home, new HomeToDoFragment())
                                        .commit();
                                break;
                            case R.id.action_home_project:
                                System.out.println("prpject");
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame_home, new HomeProjectFragment())
                                        .commit();
                                break;
                        }
                        return true;
                    }
                });

        return myView;
    }


}
