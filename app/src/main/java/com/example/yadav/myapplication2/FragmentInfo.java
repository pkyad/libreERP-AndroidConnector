package com.example.yadav.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cioc on 1/6/17.
 */

public class FragmentInfo extends Fragment{
    View myView;
    TextView textView;
    private FragmentManager fragmentManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

public FragmentInfo(){

}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_info , container, false);

        return myView;
    }

}
