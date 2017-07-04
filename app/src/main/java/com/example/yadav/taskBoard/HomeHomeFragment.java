package com.example.yadav.taskBoard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.libreerp.Helper;
import com.example.libreerp.User;
import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yadav on 10/3/17.
 */
public class HomeHomeFragment extends Fragment {
    float data1[] = {10f,2f,29.5f,23f};

    View myView;
    Context mainContext;
    CarouselView customCarouselView;
    int NUMBER_OF_PAGES = 2;
    private CustomSubTaskCardAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        myView = inflater.inflate(R.layout.fragment_home_sub_home, container, false);

        customCarouselView = (CarouselView) myView.findViewById(R.id.carouselView);
        customCarouselView.setPageCount(NUMBER_OF_PAGES);
        // set ViewListener for custom view
        customCarouselView.setViewListener(viewListener);
        final DBHandler dba=new DBHandler(getActivity(),null,null,1);
        mainContext = myView.getContext();
        setHasOptionsMenu(true);
        ListView listView = (ListView) myView.findViewById(R.id.recent_subtask_view);
        List<SubTask> subtasks = dba.getAllSubtasksList();
        adapter = new CustomSubTaskCardAdapter(mainContext, R.layout.list_subtask);

        for (int i = 0; i < subtasks.size(); i++) {
            if(subtasks.get(i).getStatus().equals("stuck") || subtasks.get(i).getStatus().equals("inProgress")) {
                SubTaskCard card = new SubTaskCard(subtasks.get(i).getTitle() + "\n" + subtasks.get(i).getStatus(),dba.getTitleFromPk(subtasks.get(i).getPkTask()));
                adapter.add(card);
            }
        }
        listView.setAdapter(adapter);
        return myView;
    }
    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {
            View customView;
            if(position == 0) {
                customView = getActivity().getLayoutInflater().inflate(R.layout.homecard1, null);
                final ImageView userImage = (ImageView) customView.findViewById(R.id.userImageHome);
                TextView progress = (TextView) customView.findViewById(R.id.progressbartext);
                DonutProgress circleProgress = (DonutProgress) customView.findViewById(R.id.totalprogress);
                progress.setText(((int) circleProgress.getProgress()) + "%");
                Users users = new Users(getContext());
                User user = User.loadUser(getContext());
                users.get(user.getPk() , new UserMetaHandler(){
                    @Override
                    public void onSuccess(UserMeta user){

                        // set text in the layout here
                    }
                    @Override
                    public void handleDP(Bitmap dp){
                        userImage.setImageBitmap(dp);
                        // set text in the layout here
                    }

                });
            }
            else {
                customView = getActivity().getLayoutInflater().inflate(R.layout.homecard2,null);
                setupPieChart(customView);
            }
            return customView;
        }
    };

    private void setupPieChart(View view) {
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i=0;i<data1.length;i++){
            pieEntries.add(new PieEntry(data1[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"Random Pie Chart");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        PieChart chart = (PieChart) view.findViewById(R.id.chart1);
        chart.setRotationEnabled(false);
        Legend legend = chart.getLegend();
        Description des = chart.getDescription();
        des.setEnabled(false);
        legend.setEnabled(false);
        chart.setData(data);
        chart.invalidate();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        MenuItem homeSort = menu.getItem(0);
        homeSort.setVisible(false);
        MenuItem itemSort = menu.getItem(1);
        itemSort.setVisible(false);
        MenuItem itemFilter = menu.getItem(2);
        itemFilter.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

