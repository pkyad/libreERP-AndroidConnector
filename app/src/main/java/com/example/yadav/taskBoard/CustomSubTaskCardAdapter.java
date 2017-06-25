package com.example.yadav.taskBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cioc on 12/6/17.
 */

public class CustomSubTaskCardAdapter extends ArrayAdapter<SubTaskCard> {
//    private List<SubTaskCard> cardList = new ArrayList<SubTaskCard>();
//
//    private class ViewHolder {
//        TextView line1;
//    }
//
//    public CustomSubTaskCardAdapter(Context context, int textViewResourceId) {
//        super(context, textViewResourceId);
//    }
//
//    @Override
//    public void add(SubTaskCard object) {
//        cardList.add(object);
//        super.add(object);
//    }
//
//    @Override
//    public int getCount() {
//        return this.cardList.size();
//    }
//
//    @Override
//    public SubTaskCard getItem(int index) {
//        return this.cardList.get(index);
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        ViewHolder viewHolder = null;
//        if (row == null) {
//            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row = inflater.inflate(R.layout.subtask_card, parent, false);
//            viewHolder.line1 = (TextView) row.findViewById(R.id.titleSubTask);
//            row.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) row.getTag();
//        }
//        SubTaskCard card = getItem(position);
//        viewHolder.line1.setText(card.getLine1());
//
//        try {
//            viewHolder.line1.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//
//
//                    switch (v.getId()) {
//                        case R.id.line1:
//
//                            PopupMenu popup = new PopupMenu(getApplicationContext(), v);
//                            popup.getMenuInflater().inflate(R.menu.clipboard_popup,
//                                    popup.getMenu());
//                            popup.show();
//                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                @Override
//                                public boolean onMenuItemClick(MenuItem item) {
//
//                                    switch (item.getItemId()) {
//                                        case R.id.edit:
//
//                                            //Or Some other code you want to put here.. This is just an example.
//                                            Toast.makeText(getApplicationContext(), " Install Clicked at position " + " : " + position, Toast.LENGTH_LONG).show();
//
//                                            break;
//                                        case R.id.delete:
//
//                                            Toast.makeText(getApplicationContext(), "Add to Wish List Clicked at position " + " : " + position, Toast.LENGTH_LONG).show();
//
//                                            break;
//
//                                        default:
//                                            break;
//                                    }
//
//                                    return true;
//                                }
//                            });
//
//                            break;
//
//                        default:
//                            break;
//                    }
//
//
//                }
//            });
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//
//
//
//
//
//
//        return row;
//    }



Context context;
    int layoutResourceId;


    public CustomSubTaskCardAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        //  this.listener=callback;
    }

private class ViewHolder {

    TextView txtTitle;
    ImageView status;
    ImageView imageview;

}

    ViewHolder holder = null;

    public View getView(int position, View convertView, ViewGroup parent) {
        final SubTaskCard lists = getItem(position);
        final int pos = position;
        if (convertView == null) {
            holder = new CustomSubTaskCardAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subtask_card, parent, false);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.textTitle);
            holder.status = (ImageView) convertView.findViewById(R.id.subtaskStatus);
            holder.imageview = (ImageView) convertView.findViewById(R.id.popup);
            convertView.setTag(holder);

        } else
            holder = (CustomSubTaskCardAdapter.ViewHolder) convertView.getTag();
         SubTaskCard card = getItem(position);
        String[] separated = card.getLine1().split("\n");
        holder.txtTitle.setText(separated[0]);
        if(separated[1].equals("complete")){
            holder.status.setBackgroundResource(R.drawable.ic_action_success);
        }
        else if(separated[1].equals("stuck")){
            holder.status.setBackgroundResource(R.drawable.ic_action_stuck);
        }

        else if(separated[1].equals("inProgress")){
            holder.status.setBackgroundResource(R.drawable.ic_action_progress);
        }
        else {
            holder.status.setBackgroundResource(R.drawable.ic_action_stopwatch);
        }

        try {
            holder.imageview.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                            PopupMenu popup = new PopupMenu(getContext(), v);
                            popup.getMenuInflater().inflate(R.menu.subtaskoptionsmenu,
                                    popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.inProgress:

                                            //Or Some other code you want to put here.. This is just an example.
                                            Toast.makeText(getContext(), " Install Clicked at position " + " : " + pos, Toast.LENGTH_LONG).show();

                                            break;
                                        case R.id.stuck:

                                            Toast.makeText(getContext(), "Add to Wish List Clicked at position " + " : " + pos, Toast.LENGTH_LONG).show();

                                            break;

                                        case R.id.complete:

                                            Toast.makeText(getContext(), "Add to Wish List Clicked at position " + " : " + pos, Toast.LENGTH_LONG).show();

                                            break;
                                        case R.id.delete:

                                            Toast.makeText(getContext(), "Add to Wish List Clicked at position " + " : " + pos, Toast.LENGTH_LONG).show();

                                            break;
                                        case R.id.editSubtask:

                                            Toast.makeText(getContext(), "Add to Wish List Clicked at position " + " : " + pos, Toast.LENGTH_LONG).show();

                                            break;



                                        default:
                                            break;
                                    }

                                    return true;
                                }
                            });
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        return convertView;
    }





}
