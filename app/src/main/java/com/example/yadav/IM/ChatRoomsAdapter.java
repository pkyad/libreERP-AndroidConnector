package com.example.yadav.IM;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder> {

    private HomeFragment mContext;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private static String today;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        public CircleImageView chatRoomDP;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
            chatRoomDP = (CircleImageView) view.findViewById(R.id.chatRoomDP);
        }
    }


    public ChatRoomsAdapter(HomeFragment mContext, ArrayList<ChatRoom> chatRoomArrayList, Context context) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;
        this.context = context;
        // Calendar calendar = Calendar.getInstance();
        // today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_rooms_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatRoom chatRoom = chatRoomArrayList.get(position);
        int size = chatRoomArrayList.size();
        int with_pk = chatRoom.getWith_pk();
        Users users = new Users(context);
        final String[] name = new String[1];
        final String[] username = new String[1];
        final Bitmap[] bp = new Bitmap[1];
        // Users user = new Users(dba.getPostUserPk(dba.getPostUser(comment_pk)));

        users.get(with_pk, new UserMetaHandler() {
            @Override
            public void onSuccess(UserMeta user) {
                System.out.println("yes65262626626");
                name[0] = user.getFirstName() + " " + user.getLastName();
                // set text in the layout here'
                username[0] = user.getUsername();
            }

            @Override
            public void handleDP(Bitmap dp) {
                System.out.println("dp dsda");
                bp[0] = dp;
                // set text in the layout here
            }

        });
        holder.name.setText(name[0]);
        chatRoom.setName(name[0]);
        chatRoom.setUsername(username[0]);
        chatRoomArrayList.get(position).setName(name[0]);
        holder.chatRoomDP.setImageBitmap(bp[0]);
        chatRoom.setDP(bp[0]);

//        holder.chatRoomDP.setImageBitmap(dpBitmap);


        holder.message.setText(chatRoom.getLastMessage());
        holder.timestamp.setText(getCommitDate(chatRoom.getTimestamp()));
        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }

        // holder.timestamp.setText(getTimeStamp(chatRoom.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return chatRoomArrayList.size();
    }



    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ChatRoomsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ChatRoomsAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public String getCommitDate(String timestamp) {
        String dbstring ;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss");
        try {

            date = formatter.parse(timestamp);
        } catch (ParseException e) {
            System.out.println("error while parsing");
        }
        dbstring = (date).toString();
        return dbstring ;
    }
}
