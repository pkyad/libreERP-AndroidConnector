package com.example.yadav.taskBoard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libreerp.Helper;
import com.example.libreerp.User;
import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yadav on 10/3/17.
 */
public class HomeHomeFragment extends Fragment {

    String serverURL;
    int pk;
    String title = new String();
    String description = new String();
    String created = new String();
    View myView;
    Context mainContext;
    DBHandler dba;
    CarouselView customCarouselView;
    private User user;
    int NUMBER_OF_PAGES = 2;
    int totalCompleted = 0;
    int totalStuck = 0;
    int totalInProgress = 0;
    int totalNotStarted = 0;
    int total = 0;
    float[] data1 = new float[4];
    RecyclerView listView;
    ArrayList<String> data2 = new ArrayList<>();
    List<SubTask> data_list = new ArrayList<>();
    private CustomSubTaskCardAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        myView = inflater.inflate(R.layout.fragment_home_sub_home, container, false);
        mainContext = myView.getContext();
        dba=new DBHandler(getActivity(),null,null,2);
        user = User.loadUser(mainContext);
        setHasOptionsMenu(true);
        listView = (RecyclerView) myView.findViewById(R.id.recent_subtask_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mainContext, 1);
        listView.setLayoutManager(gridLayoutManager);
        adapter = new CustomSubTaskCardAdapter(mainContext, data_list);
        listView.setAdapter(adapter);
        setSubtaskHome();
        customCarouselView = (CarouselView) myView.findViewById(R.id.carouselView);
        customCarouselView.setPageCount(NUMBER_OF_PAGES);
        customCarouselView.setViewListener(viewListener);
        return myView;
    }

    public void setSubtaskHome(){
        dba=new DBHandler(getActivity(),null,null,2);
        List<SubTask> subtasks = dba.getAllSubtasksList();
        for (int i = 0; i < subtasks.size(); i++) {
            if (user.getPk() == dba.getResponsible(subtasks.get(i).getPkTask())) {
                if (subtasks.get(i).getStatus().equals("stuck") || subtasks.get(i).getStatus().equals("inProgress")) {
                    data_list.add(subtasks.get(i));
                }
                if (subtasks.get(i).getStatus().equals("stuck")) {
                    totalStuck++;
                }
                if (subtasks.get(i).getStatus().equals("inProgress")) {
                    totalInProgress++;
                }
                if (subtasks.get(i).getStatus().equals("complete")) {
                    totalCompleted++;
                }
                if (subtasks.get(i).getStatus().equals("notStarted")) {
                    totalNotStarted++;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {
            View customView;
            TextView progress;

            if(position == 0) {
                customView = getActivity().getLayoutInflater().inflate(R.layout.homecard1, null);
                final ImageView userImage = (ImageView) customView.findViewById(R.id.userImageHome);
                progress = (TextView) customView.findViewById(R.id.progressbartext);
                DonutProgress circleProgress = (DonutProgress) customView.findViewById(R.id.totalprogress);
                circleProgress.setDonut_progress(100*totalCompleted/(totalCompleted + totalInProgress + totalNotStarted + totalStuck) + "");
                progress.setText(100*totalCompleted/(totalCompleted + totalInProgress + totalNotStarted + totalStuck) + "%");
                customView.invalidate();
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
                total = totalCompleted + totalInProgress + totalNotStarted + totalStuck;
                data1[0] = ((float)totalStuck*100/total);
                data1[1] = ((float)totalNotStarted*100/total);
                data1[2] = ((float)totalInProgress*100/total);
                data1[3] = ((float)totalCompleted*100/total);
                data2.add("Stuck");
                data2.add("Not Started");
                data2.add("In Progress");
                data2.add("Completed");
                setupPieChart(customView);
            }
            return customView;
        }
    };

    private void setupPieChart(View view) {
        List<PieEntry> pieEntriesX = new ArrayList<>();
        for(int i=0;i<data1.length;i++){
            pieEntriesX.add(new PieEntry(data1[i]));

        }
        PieDataSet dataSet = new PieDataSet(pieEntriesX,"Random Pie Chart");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(15);
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

