package com.example.yadav.taskBoard;

import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.libreerp.UserViewBS;

import static android.R.attr.action;
import static android.R.attr.tunerCount;

/**
 * Created by cioc on 12/6/17.
 */

public class CustomSubTaskCardAdapter extends ArrayAdapter<SubTaskCard> {

    Context context;
    int layoutResourceId;


    public CustomSubTaskCardAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

private class ViewHolder {

    TextView txtTitle;
    ImageView status;
    ImageView imageview;
    RelativeLayout layout;
    TextView taskTitle;
}
    ViewHolder holder = null;

    public View getView(int position,View convertView, ViewGroup parent) {

        final int pos = position;
        if (convertView == null) {
            holder = new CustomSubTaskCardAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subtask_card, parent, false);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.textTitle);
            holder.taskTitle = (TextView) convertView.findViewById(R.id.tasktitle);
            holder.status = (ImageView) convertView.findViewById(R.id.subtaskStatus);
            holder.imageview = (ImageView) convertView.findViewById(R.id.popup);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.subtaskCard) ;
            convertView.setTag(holder);

        } else
            holder = (CustomSubTaskCardAdapter.ViewHolder) convertView.getTag();
        SubTaskCard card = getItem(position);
        String line2 = card.getLine2();
        String[] separated = card.getLine1().split("\n");
        holder.txtTitle.setText(separated[0]);
        holder.taskTitle.setText(line2);
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

        return convertView;
    }



}
