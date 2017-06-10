package com.example.yadav.myapplication2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    List<Task> SubjectNames;

    View view1;



    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {



        view1  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card, viewGroup, false);

        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder Viewholder, int i) {

        Viewholder.SubjectTextView.setText(SubjectNames.get(i).getDescription());
    }

    @Override
    public int getItemCount() {

        return SubjectNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView SubjectTextView;
        public ViewHolder(View view) {

            super(view);

            SubjectTextView = (TextView)view.findViewById(R.id.description);
        }
    }

}