package com.example.libreerp;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.libreerp.Helper;
import com.example.libreerp.UserMeta;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.support.v4.view.MenuItemCompat.setOnActionExpandListener;

public class NewChat extends AppCompatActivity {

    RecyclerView recyclerView;
    UserListAdapter userListAdapter;
    private GridLayoutManager gridLayoutManager;
    List<UserMeta> user_list;
    TextView text;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homefragment_menu,menu);
        MenuItem item = menu.findItem(R.id.search_chat);
        SearchView searchview = (SearchView) item.getActionView();
        searchview.isIconified();
        item.expandActionView();
        searchview.setIconifiedByDefault(true);
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Write your code here
                return true;
            }
        });

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                text.setVisibility(View.GONE);
                System.out.println("query = " + query);
                query = query.toLowerCase();

                    user_list.clear();
                    userListAdapter.clearData();
                    Helper helper = new Helper(getApplicationContext());
                    AsyncHttpClient client = helper.getHTTPClient();
                    RequestParams params = new RequestParams();
                    params.put("username__contains", query);

                    final String url = String.format("%s/%s/", helper.serverURL, "/api/HR/userSearch");
                    client.get(url, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject userMeta = response.getJSONObject(i);
                                    int userPk = userMeta.getInt("pk");
                                    UserMeta um = new UserMeta(userPk);
                                    um.setFirstName(userMeta.getString("first_name"));
                                    um.setLastName(userMeta.getString("last_name"));
                                    um.setUsername(userMeta.getString("username"));
                                    JSONObject profile = userMeta.getJSONObject("profile");
                                    um.setProfilePictureLink(profile.getString("displayPicture"));
                                    user_list.add(um);

                                } catch (JSONException e) {
                                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                                }

                            }
                            userListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            System.out.println("failure");
                            System.out.println(statusCode);
                        }
                    });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text.setVisibility(View.GONE);
                if(newText.equals("")){
                    user_list.clear();
                    userListAdapter.clearData();
                    text.setVisibility(View.VISIBLE);
                }
                return false;
            }

        });
        item.setVisible(true);
        userListAdapter.clearData();
        user_list.clear();
        return true;
    }

//http://pradeepyadav.net/api/HR/userSearch/?username__contains=d
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        user_list = new ArrayList<UserMeta>();
        text = (TextView) findViewById(R.id.text);
        recyclerView = (RecyclerView) findViewById(R.id.contactList);
        userListAdapter = new UserListAdapter(getApplicationContext(),user_list);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(userListAdapter);
        if(user_list.size() != 0){
            text.setVisibility(View.GONE);
        }
        else
            text.setVisibility(View.VISIBLE);
    }
}
