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
    int PK_Project;
    private FragmentManager fragmentManager;
    int initialCount = 0;
    boolean fromEditTask = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle= getActivity().getIntent().getExtras();
        if(bundle!=null) {
            PK_Project = bundle.getInt("PK_PROJECT");
            fromEditTask = bundle.getBoolean("GotoTODO");
        }

        HomeToDoFragment homeToDoFragment = new HomeToDoFragment();
        initialCount = new DBHandler(getActivity(),null,null,2).getTotalDBEntries_TASK();
        fragmentManager = getFragmentManager();
        if(PK_Project==0 && initialCount==0 && !fromEditTask) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_home, new HomeToDoFragment())
                    .commit();
        }
        else if (initialCount!=0 && PK_Project ==0 && !fromEditTask){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_home, new HomeHomeFragment())
                    .commit();
        }
        else if(fromEditTask){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_home, new HomeToDoFragment())
                    .commit();
        }
        else{
            bundle.putInt("PK_PROJECT", PK_Project);
            homeToDoFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_home,homeToDoFragment)
                    .commit();
        }
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
                                    getActivity().setTitle("HOME");
                                    break;
                                case R.id.action_home_todo:
                                    System.out.println("todo");
                                    Bundle bundle= getActivity().getIntent().getExtras();
                                    PK_Project = 0;
                                    if(bundle!=null)
                                        bundle = null;
                                    HomeToDoFragment homeToDoFragment = new HomeToDoFragment();
                                    homeToDoFragment.setArguments(bundle);
                                    homeToDoFragment.removeFilter = true;
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame_home, homeToDoFragment)
                                            .commit();
                                    getActivity().setTitle("TASKS");
                                    break;
                                case R.id.action_home_project:
                                    System.out.println("prpject");
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame_home, new HomeProjectFragment())
                                            .commit();
                                    getActivity().setTitle("PROJECTS");
                                    break;
                            }
                            return true;
                        }
                    });

        return myView;
    }
}
