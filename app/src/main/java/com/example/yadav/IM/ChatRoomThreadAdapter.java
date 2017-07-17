package com.example.yadav.IM;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.yadav.IM.R.drawable.bg_bubble_gray;


public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private int userId;
    private int SELF = 100;
    private static String today;

   
    private boolean margin = false;
    private Context mContext;
    private ArrayList<Message> messageArrayList;
    private static final int CAMERA_REQUEST = 1 ;
    private Bitmap bm = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  message , timestamp;
        private ImageView location ;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.time);
            location = (ImageView) itemView.findViewById(R.id.image_location);
        }
    }


    public ChatRoomThreadAdapter(Context mContext, ArrayList<Message> messageArrayList , int userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        TextView message ;
        ImageView card ;
        ImageView attachment ;
        TextView timestamp ;
        int position = viewType ;
       
        String type =  messageType(position);
        Boolean self = isSelf(position);
        Boolean margin = giveMargin(position); 
        
        if (type.equals("MSG")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item, parent, false);
            RelativeLayout relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);
            // view type is to identify where to render the chat message
            // left or right
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.time);

            if (self) {
                // self message
                if (margin == true) {
                    RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    int sizeInDP = 40;

                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, sizeInDP, mContext.getResources().getDisplayMetrics());
                    params.setMargins(params.leftMargin, marginInDp, params.rightMargin, params.bottomMargin);
                    // relativeLayout.setLayoutParams(params);
                    margin = false ;
                }

                ((RelativeLayout) itemView).setGravity(Gravity.RIGHT);
                message.setBackgroundResource(R.drawable.bg_bubble_gray);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)message.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                message.setLayoutParams(params);

                RelativeLayout.LayoutParams params_time = (RelativeLayout.LayoutParams)timestamp.getLayoutParams();
                params_time.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                timestamp.setLayoutParams(params_time);
                // we can change margina and align parent right by adding in params


            } else {
                // others message
                if (margin == true) {
                    RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    int sizeInDP = 40;

                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, sizeInDP, mContext.getResources().getDisplayMetrics());
                    params.setMargins(params.leftMargin, marginInDp, params.rightMargin, params.bottomMargin);
                    // relativeLayout.setLayoutParams(params);
                    margin = false ;
                }
                //itemView = LayoutInflater.from(parent.getContext())
                //      .inflate(R.layout.chat_item, parent, false);
                // timestamp.setGravity(Gravity.LEFT);
            }
        }
        else { // given message is card message
            ;
            if (type.equals("GPS")) {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_location, parent, false);
                // view type is to identify where to render the chat message
                // left or right
                card = (ImageView) itemView.findViewById(R.id.image_location);

                card.setImageResource(R.drawable.ic_location_on_black_24dp);
            }
            else if (type.equals("IMG")){
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_location, parent, false);
                // view type is to identify where to render the chat message
                // left or right
                card = (ImageView) itemView.findViewById(R.id.image_location);
                card.setImageBitmap(bm);
            }
            else{ // attachment is file
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_files, parent, false);
                // view type is to identify where to render the chat message
                // left or right
                card = (ImageView) itemView.findViewById(R.id.image_location);
                if (type.equals("PDF")){ // file is pdf
                    card.setImageResource(R.drawable.ic_picture_as_pdf_black_24dp);
                }
                else {
                    card.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
                }
            }
            if (self) {
                // self message

                ((RelativeLayout) itemView).setGravity(Gravity.RIGHT);
                card.setBackgroundResource(R.drawable.bg_bubble_gray);
                // we can change margina and align parent right by adding in params


            } else {
                // others message
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_item, parent, false);
            }

        }




        return new ViewHolder(itemView);
    }
    /* public void onReceive(Context context, Intent intent) {
         String action = intent.getAction();

         Log.i("Receiver", "Broadcast received: " + action);

         if(action.equals("com.example.broadcast.MY_NOTIFICATION")){
             char isType = intent.getExtras().getChar("isTyping");
             String type_user = intent.getExtras().getString("type_user");

         }
     }*/
    private String messageType(int position){
        Message msg = messageArrayList.get(position);
        String msgText = msg.getMessage();
        String attachment = msg.getAttachment();
        String toReturn="MSG";
        if (msgText.startsWith("GPS://")){
            toReturn = "GPS";
        }else if (!attachment.equals(null) && !attachment.equals("null")){
            String extension = attachment.substring(attachment.length()-3);
            if (extension.equals("pdf") || extension.equals("PDF")){
                toReturn = "PDF";
            }else if (extension.equals("JPG") || extension.equals("jpg") || extension.equals("PNG") || extension.equals("png")){
                toReturn = "IMG";
            }else {
                toReturn="FILE";
            }
        }

        return toReturn ;
    }



    @Override
    public int getItemViewType(int position) {
        return position ;
    }
    
    public Boolean giveMargin(int position) {
       if (position + 1 < messageArrayList.size()){
           Message msg1 = messageArrayList.get(position);
           Message msg2 = messageArrayList.get(position + 1);
           if (msg1.getUser().getPkUser() != msg2.getUser().getPkUser()){
               return true ;
           }
       }
        return false ;
    }

    public void getAttachBitmap(int position) {
        Message message = messageArrayList.get(position);
        bm =  message.getBm();
    }

    private Boolean isSelf(int position){
        Message msg = messageArrayList.get(position);
        if (msg.getUser().getPkUser() == userId){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        ((ViewHolder) holder).message.setText(message.getMessage());



        //String timestamp = getTimeStamp(message.getCreatedAt());
        String timestamp = getCommitDate(message.getCreatedAt());
        ((ViewHolder) holder).timestamp.setText(timestamp);
        if (!giveMargin(position)){
            ((ViewHolder) holder).timestamp.setVisibility(View.GONE);
        }
        else{
            ((ViewHolder) holder).timestamp.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    public String getCommitDate(String timestamp) {



        Date date = new Date();
        Date currentDate = new Date() ;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String targetDate =date.toString();
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
            System.out.println("error while parsing date 3");
        }

        return targetDate ;
    }
}