package com.example.yadav.IM;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cioc on 18/7/17.
 */

public class NewChatReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        int withPK = 0;
        withPK = intent.getIntExtra("with_id",withPK);
        String name = intent.getStringExtra("name");
        Intent mainIntent = new Intent(context, HomeActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.putExtra("isReceive",true);
        mainIntent.putExtra("with_PK",withPK);
        mainIntent.putExtra("name",name);
        context.startActivity(mainIntent);
    }
}
