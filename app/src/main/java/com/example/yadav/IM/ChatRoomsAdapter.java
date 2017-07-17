package com.example.yadav.IM;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;
import android.support.v4.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder>  {

    private HomeFragment mContext;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private static String today;
    private Context context;
    private FragmentManager fragmentManager ;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message, timestamp, count;
        public CircleImageView chatRoomDP;
        public RelativeLayout imagelayout , chatlayout ;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            count = (TextView) view.findViewById(R.id.count);
            chatRoomDP = (CircleImageView) view.findViewById(R.id.chatRoomDP);
            imagelayout = (RelativeLayout) view.findViewById(R.id.imagelayout);
            chatlayout = (RelativeLayout) view.findViewById(R.id.chatlayout);



        }
    }
//    public void bottomSheet(View view){
//        TextView view1 = (TextView) view.findViewById(R.id.userPK);
//
//        final BottomSheetDialogFragment userViewBS = UserViewBS.newInstance(Integer.parseInt(view1.getText().toString()));
//        userViewBS.show(getActivity().getSupportFragmentManager(), userViewBS.getTag());
//
//    }

    public ChatRoomsAdapter(HomeFragment mContext, ArrayList<ChatRoom> chatRoomArrayList, Context context , FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;
        this.context = context;
        this.fragmentManager = fragmentManager ;
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
        final int index = position ;
        ChatRoom chatRoom = chatRoomArrayList.get(index);
        int size = chatRoomArrayList.size();
        final int with_pk = chatRoom.getWith_pk();
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

        String lastMsgText = chatRoom.getLastMessage().split("\n")[0];
        if (lastMsgText.length()>20){
            lastMsgText = lastMsgText.substring(0,20);
        }

        holder.message.setText(lastMsgText);
        holder.timestamp.setText(getCommitDate(chatRoom.getTimestamp()));
        if (chatRoom.getUnreadCount() > 0) {
            holder.count.setText(String.valueOf(chatRoom.getUnreadCount()));
            holder.count.setVisibility(View.VISIBLE);
        } else {
            holder.count.setVisibility(View.GONE);
        }

        // holder.timestamp.setText(getTimeStamp(chatRoom.getTimestamp()));
        holder.imagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialogFragment userViewBS = UserViewBS.newInstance(with_pk);
                userViewBS.show(fragmentManager, userViewBS.getTag());
            }
        });

        holder.chatlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatRoom chatRoom = chatRoomArrayList.get(index);

                int pk_select = chatRoom.getWith_pk();
                String name_select =  chatRoom.getName();
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("chatID", Integer.toString(chatRoom.getId()));
                intent.putExtra("with_id", Integer.toString(pk_select));
                intent.putExtra("name", name_select);
                intent.putExtra("userName" , chatRoom.getUsername());
               /* intent.putExtra("UnreadMessages", storeUnreadList);

                for (int i = storeUnreadList.size() - 1 ; i >= 0 ; i++){
                    storeUnreadList.remove(i);
                }*/

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                chatRoom.getDP().compress(Bitmap.CompressFormat.PNG, 80, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("dp", byteArray);
                mContext.startActivity(intent);
            }
        });
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

        Date date = new Date();
        Date currentDate = new Date() ;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String targetDate = date.toString() ;
        try {

            date = formatter.parse(timestamp);
            targetDate = (date).toString();
            if (date.getYear() == currentDate.getYear()){
                SimpleDateFormat formatter_yr = new SimpleDateFormat("hh:mm a , dd MMM");
                targetDate = formatter_yr.format(date);

                if (date.getDate() == currentDate.getDate()){
                    SimpleDateFormat formatter_day = new SimpleDateFormat("hh:mm a");
                    targetDate = formatter_day.format(date);

                }
            }
            else {
                SimpleDateFormat formatter_yr = new SimpleDateFormat("hh:mm a , dd|MM|yy");
                targetDate = formatter_yr.format(date);
            }
        } catch (ParseException e) {
            System.out.println("error while parsing date 2");
        }

        return targetDate ;
    }
}
