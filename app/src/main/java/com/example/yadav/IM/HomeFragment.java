package com.example.yadav.IM;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.libreerp.Helper;
import com.example.libreerp.User;
import com.example.libreerp.UserMeta;
import com.example.libreerp.UserMetaHandler;
import com.example.libreerp.Users;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import ws.wamp.jawampa.WampClient;

import static android.provider.LiveFolders.INTENT;

/**
 * Created by yadav on 19/2/17.
 */
public class HomeFragment extends Fragment {

    View myView;
    ArrayAdapter<String> searchAdapter ;
    private static ArrayList<ChatRoom> chatRoomArrayList;
    private static ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    private static Context context ;
    private static  HomeFragment mContext ;
    private Boolean connected;
    private String chennel;
    private User login;
    private AsyncHttpClient httpClient;
    static DBHandler dba;
    private BroadcastReceiver mReceiver;
    private Helper helper;
    private BroadcastReceiver newChatReceiver;
    private TextView typing ;
    private boolean isType = false ;
    private int typing_id ;
    private String username ;
    private View mCustomView;
    private WampClient client;
    private static LinearLayoutManager layoutManager ;
    private ArrayList<Integer> ignore_id = new ArrayList<Integer>();;
    private ArrayList<ChatRoomTable> storeUnreadList = new ArrayList<ChatRoomTable>();
    static Boolean isReceive;
    static Bundle bundle;
    static boolean isEmpty = false ;
    static int withID = 0 ;
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("com.libreERP.TYPING");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(final Context context, Intent intent) {
                //extract our message from intent
//                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                String is_typing = intent.getStringExtra("type");
                String message = intent.getStringExtra("new_message");
                final String type_user = intent.getStringExtra("type_user");

                Helper helper = new Helper(context);
                httpClient = helper.getHTTPClient();

                 if (is_typing.equals("M")){
                    int msgPK = Integer.parseInt(intent.getStringExtra("msgPK"));

                    String url = String.format("%s/%s/%s/?mode=" , helper.serverURL, "api/PIM/chatMessage" , msgPK );


                    // if the username is not prsent in chatroom then create new chatRoom else update the last Messsage;


                    httpClient.get(url,  new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            String message;
                            try {

                                String attachement;
                                int pkOriginator;
                                String created;
                                boolean read;
                                int pkUser;
                                int with_pk ;
                                int msgPk = response.getInt("pk");
                                message = response.getString("message");
                                attachement = response.getString("attachment");
                                pkOriginator = response.getInt("originator");
                                created = response.getString("created").replace("Z", "").replace("T", " ");
                                read = response.getBoolean("read");
                                pkUser = response.getInt("user");
                                login = User.loadUser(context);
                                with_pk = pkOriginator ;
                                if (login.getPk() == pkOriginator){
                                    with_pk = pkUser ;
                                }
                                ChatRoomTable chatRoomTable = new ChatRoomTable();

                                chatRoomTable.setAttachement(attachement);
                                chatRoomTable.setCreated(created);
                                chatRoomTable.setMessage(message);
                                chatRoomTable.setPkMessage(msgPk);
                                chatRoomTable.setPkOriginator(pkOriginator);
                                chatRoomTable.setPkUser(pkUser);
                                chatRoomTable.setOtherPk(with_pk);
                                chatRoomTable.setTotal_UnRead(1);
                                //storeUnreadList.add(chatRoomTable);


                                if (!dba.CheckIfPKAlreadyInDBorNot(with_pk)) { // check in table of ChatRoom , new chatroom it is
                                    dba.insertTableChatRoom(chatRoomTable);

                                    ignore_id.add(with_pk);
                                    ChatRoom chatRoom = new ChatRoom(chatRoomTable.getMessage(),chatRoomTable.getCreated(),chatRoomTable.getTotal_unread());
                                    chatRoom.setWith_pk(with_pk);

                                    chatRoomArrayList.add(chatRoom);
                                    mAdapter.notifyDataSetChanged();
                                }
                                else { // just update last message and total unread and timestamp of last message


                                    for (int i = 0 ; i < chatRoomArrayList.size() ; i++){
                                        if (chatRoomArrayList.get(i).getUsername().equals(type_user) || (chatRoomArrayList.get(i).getWith_pk() == with_pk)){
                                            int initial_unread = dba.getUnREADFromWithPk(with_pk);

                                            if (login.getUsername().equals(type_user)){
                                                dba.updateMessageTableChatRoom(chatRoomArrayList.get(i).getWith_pk() ,message ,initial_unread,created);
                                                chatRoomArrayList.get(i).setUnreadCount(initial_unread);
                                            }
                                            else {
                                                dba.updateMessageTableChatRoom(chatRoomArrayList.get(i).getWith_pk() ,message ,initial_unread + 1,created);
                                                chatRoomArrayList.get(i).setUnreadCount(initial_unread+ 1);
                                            }

                                            //dba.updateMessageTableChatRoom(chatRoomArrayList.get(i).getWith_pk() ,message ,0,created);
                                            chatRoomArrayList.get(i).setLastMessage(message);
                                            int unreadCount = chatRoomArrayList.get(i).getUnreadCount() ;

                                            chatRoomArrayList.get(i).setTimestamp(created);
                                            mAdapter.notifyDataSetChanged();
                                            ignore_id.add(with_pk); // also update in db ;
                                            break ;
                                        }


                                    }
                                }

                                // now insert that new messge in database
                                if (!dba.CheckIfMessagePKAlreadyInDBorNot(msgPk)) { // check in table of Message
                                    // search for chatRoomID
                                    for (int i = 0 ; i < chatRoomArrayList.size() ; i++){
                                        if (chatRoomArrayList.get(i).getWith_pk() == with_pk){
                                            int a = 1;
                                            chatRoomTable.setChatRoomID(dba.getIDFromWithPk(with_pk));
                                            break ;
                                        }
                                    }


                                    dba.insertTableMessage(chatRoomTable);
                                }


                            } catch (JSONException e) {

                            }
                            // load_data_from_database(0);

                            // recyclerView.scrollToPosition(messageArrayList.size()-1);

                            // layoutManager.setStackFromEnd(true);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            System.out.println("failure");
                            System.out.println(statusCode);
                        }
                    });;



                }

            }
        };
        //registering our receiver
        context.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        context.unregisterReceiver(this.mReceiver);
    }





    public void clearData(){
        if(chatRoomArrayList.size() != 0)
        chatRoomArrayList.clear();

    }









    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_home , container, false);
        mContext = this ;
        dba = new DBHandler(getContext(), null, null, 1);
        mContext = this ;
        chatRoomArrayList = new ArrayList<>();
        context = getActivity().getApplicationContext();
       // load_data_from_database(0);

        isReceive = false ;
        bundle = getActivity().getIntent().getExtras();
        if(bundle!=null) {
            isReceive = bundle.getBoolean("isReceive");
            isEmpty = bundle.getBoolean("isEmpty");
            withID = (bundle.getInt("withId"));

        }

        setHasOptionsMenu(true);
        recyclerView = (RecyclerView)  myView.findViewById(R.id.chatList_recycler_view);
        FloatingActionButton newChat = (FloatingActionButton) myView.findViewById(R.id.newChat);
        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),com.example.libreerp.NewChat.class);
                startActivity(intent);
            }
        });
        User usr = User.loadUser(getActivity());






        //clearData();


        mAdapter = new ChatRoomsAdapter(mContext, chatRoomArrayList,context,getActivity().getSupportFragmentManager());
        int size = chatRoomArrayList.size();
        layoutManager = new LinearLayoutManager( getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity()
        ));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchChatRooms();

        return myView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.homefragment_menu,menu);
        MenuItem item = menu.findItem(R.id.search_chat);
        SearchView searchview = (SearchView) item.getActionView();

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                ArrayList<ChatRoom>  filterData = new ArrayList<ChatRoom>();
                for (int i = 0 ; i < chatRoomArrayList.size() ; i++){
                    String chatRoomName = chatRoomArrayList.get(i).getName();
                    if (chatRoomName.contains(query)){
                        filterData.add(chatRoomArrayList.get(i));
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mAdapter = new ChatRoomsAdapter(mContext,filterData,context,getActivity().getSupportFragmentManager());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                    String query = newText.toLowerCase();
                    System.out.print(query);
                    ArrayList<ChatRoom> filterData = new ArrayList<ChatRoom>();
                    for (int i = 0; i < chatRoomArrayList.size(); i++) {
                        String chatRoomName = chatRoomArrayList.get(i).getName();
                        if (chatRoomName.toLowerCase().contains(query)) {
                            System.out.print("query is  " + chatRoomName + " \n");
                            filterData.add(chatRoomArrayList.get(i));
                        }
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mAdapter = new ChatRoomsAdapter(mContext, filterData, context,getActivity().getSupportFragmentManager());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                return false;
            }
        });

    }

    private void fetchChatRooms() {
        System.out.print("yess");


        Helper helper = new Helper(context);

        AsyncHttpClient client = helper.getHTTPClient();

        String url = String.format("%s/%s/", helper.serverURL, "api/PIM/chatMessage");

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                System.out.println("success 001xzc");
                try {
//                                JSONArray response = tasks.getJSONArray("results");
                    int pkMessage;
                    String message;
                    String attachement;
                    int pkOriginator;
                    String created;
                    boolean read;
                    int pkUser;

                    String category;
                    String text;
                    int pkCommit;
                    String commitMessage;
                    int user;
                    String CommitDate;
                    String CommitBranch;
                    String CommitCode;


                    login = User.loadUser(context);
                    int login_pk = login.getPk();

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject c = response.getJSONObject(i);
                        pkMessage = c.getInt("pk");
                        message = c.getString("message");
                        attachement = c.getString("attachment");
                        pkOriginator = c.getInt("originator");
                        created =  c.getString("created").replace("Z","").replace("T"," ");
                        read = c.getBoolean("read");
                        pkUser = c.getInt("user");

                        int other_pk = pkOriginator;
                        if (other_pk == login_pk) {
                            other_pk = pkUser;
                        }

                        ChatRoomTable chatRoomTable = new ChatRoomTable();
                        chatRoomTable.setAttachement(attachement);
                        chatRoomTable.setCreated(created);
                        chatRoomTable.setMessage(message);
                        chatRoomTable.setPkMessage(pkMessage);
                        chatRoomTable.setPkOriginator(pkOriginator);
                        chatRoomTable.setPkUser(pkUser);
                        chatRoomTable.setOtherPk(other_pk);

                        if (read == false){
                            chatRoomTable.setIsReadStatus(0); // means it is unnread
                        }
                        else {
                            chatRoomTable.setIsReadStatus(1);
                        }
                       if (searchInIgnoreIdArray(ignore_id,other_pk) == 0 ) {
                           // if not found in database insert it otherwise update it
                           int total_unread = 0;
                           for (int j = i ; j< response.length() ; j++){ // loop for counting unread
                               int pkOriginator_copy ,pkUser_copy;
                               boolean read_copy;
                               JSONObject d = response.getJSONObject(j);

                               pkOriginator_copy = d.getInt("originator");

                               read_copy = d.getBoolean("read");
                               pkUser_copy = d.getInt("user");

                               int other = pkOriginator_copy;
                               if (other == login_pk) {
                                   other = pkUser_copy;
                               }
                               if (other_pk == other && read_copy == false){
                                   total_unread ++ ;
                               }

                           }

                           chatRoomTable.setTotal_UnRead(total_unread);
                           if (!dba.CheckIfPKAlreadyInDBorNot(other_pk)) { // check in table for chatroom
                               dba.insertTableChatRoom(chatRoomTable);
                           } else { // update it
                               dba.updateMessageTableChatRoom(chatRoomTable.getOtherPk() ,chatRoomTable.getMessage() ,0,chatRoomTable.getCreated()); // check 1 more time
                           }

                       }
                       // every messages will be inserted
                        if (!dba.CheckIfMessagePKAlreadyInDBorNot(pkMessage)) { // check in table of Message
                           dba.insertTableMessage(chatRoomTable);
                        }



                        ignore_id.add(other_pk);
                    }
                    chatRoomArrayList.clear();
                    load_data_from_database(0);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatRoomArrayList.size() - 1);
                   // layoutManager.setStackFromEnd(true);
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());

                }


            }

            @Override
            public void onFinish() {
                System.out.println("finished 001cxczdfhgfg");
                // retrieve all the db entries

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println(statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                load_data_from_database(0);
                System.out.println("finished failed 001xczxc");
            }
        });
    }

    private int searchInIgnoreIdArray(ArrayList<Integer> a , int key){
        for (int i = 0 ; i < a.size() ; i++){
            if (a.get(i) == key){
               return 1 ;
            }
        }
        return 0 ;
    }
    private static void createnewChatroom(){
        if(isReceive){

            int newWithPk = bundle.getInt("with_PK");
            String chatRoomName = bundle.getString("name");
            byte[] newbyteArray = bundle.getByteArray("chatRoomDP");
            Bitmap newchatRoomDP;
            if(newbyteArray !=null) {
                newchatRoomDP  = BitmapFactory.decodeByteArray(newbyteArray, 0, newbyteArray.length);
            }
            Boolean newchatRoomExist = false ;

            for (int i = 0 ; i < chatRoomArrayList.size() ; i++){
                if (chatRoomArrayList.get(i).getWith_pk() == newWithPk ){
                    newchatRoomExist = true ;
                    ChatRoom chatRoom = chatRoomArrayList.get(i);

                    int pk_select = chatRoom.getWith_pk();
                    String name_select =  chatRoom.getName();

                    Intent intent = new Intent(context, ChatRoomActivity.class);
                    intent.putExtra("chatID", Integer.toString(chatRoom.getId()));
                    intent.putExtra("with_id", Integer.toString(pk_select));
                    intent.putExtra("name", chatRoomName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    break ;
                }

            }
            System.out.println("newChatRoomExist = " + newchatRoomExist);
            if (!newchatRoomExist){ // now to create new chatroom
                ChatRoomTable chatRoomTable = new ChatRoomTable();
                chatRoomTable.setOtherPk(newWithPk);
                chatRoomTable.setPkMessage(0);
                chatRoomTable.setMessage("");
                chatRoomTable.setCreated("");
                chatRoomTable.setTotal_UnRead(0);
                dba.insertTableChatRoom(chatRoomTable);

                ChatRoom chatRoom = new ChatRoom(chatRoomTable.getMessage(),chatRoomTable.getCreated(),chatRoomTable.getTotal_unread());
                //chatRoom.setDP(newchatRoomDP);
                chatRoom.setName(chatRoomName);
                chatRoom.setId(dba.getIDFromWithPk(newWithPk));
                chatRoom.setWith_pk(newWithPk);
                chatRoomArrayList.add(chatRoom);
//                mAdapter.notifyDataSetChanged();

                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("chatID", Integer.toString(chatRoom.getId()));
                intent.putExtra("with_id", Integer.toString(chatRoom.getWith_pk()));
                intent.putExtra("name", chatRoom.getName());
                intent.putExtra("userName" , chatRoom.getUsername());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }


        }
    }

    private static void load_data_from_database(int id) {

        final AsyncTask<Integer, Void, Void> comment = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {

                final DBHandler dba = new DBHandler(context, null, null, 1); // see this
                //System.out.println("pkTask = "+pkTask);
                int entries_database_chatRoom = dba.getTotalDBEntries_CHATROOM();
                for (int i = 0; i < entries_database_chatRoom ; i++) {
                    final ChatRoomTable data = new ChatRoomTable();
                    data.setOtherPk(dba.getWithPK(i));
                    data.setPkMessage(dba.getMessagePK(i));
                    data.setMessage(dba.getLastMessage(i));
                    String date = dba.getDate(i);
                    data.setCreated(date);
                    data.setTotal_UnRead(dba.getUnRead(i));
                    data.setChatRoomID(dba.getID(i));
                     String messageDate;
                     //messageDate = new SimpleDateFormat("dd MMM, yyyy").format(date);

                    final String[] name = new String[1];
                    final Bitmap[] bp = new Bitmap[1];
                    int with_pk = dba.getWithPK(i);

                    ChatRoom chatRoom = new ChatRoom(data.getMessage(),date,data.getTotal_unread());
                    chatRoom.setId(data.getChatRoomID());
                    chatRoom.setWith_pk(with_pk);

                    chatRoomArrayList.add(chatRoom);
                }

                createnewChatroom();
                if (isEmpty){
                    for (int i = 0 ; i < chatRoomArrayList.size() ;i++){
                        if (chatRoomArrayList.get(i).getWith_pk() == withID){
                            chatRoomArrayList.remove(i);
                            dba.deleteChatRoom(withID);
                            break;
                        }
                    }

                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        };

        comment.execute(id);
    }


}
