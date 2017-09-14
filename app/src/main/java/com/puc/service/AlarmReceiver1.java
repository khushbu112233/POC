package com.puc.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.puc.util.Pref;

/**
 * Created by aipxperts on 1/8/16.
 */
public class AlarmReceiver1 extends BroadcastReceiver {

    String name;
    String beam_beacon_message;
    Notification myNotication;
    Context context;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context=context;

            Log.v("inside", "555555555-----------------");
            sendMessageToActivity(context, "1");

    }

    private void sendMessageToActivity(Context context, String simple_notify) {
        Log.e("Background","4444 " +Pref.getValue(context,"isInBackground",""));
        Intent intent = new Intent("Simple_notification");
// You can also include some extra data.
        intent.putExtra("simple_notify", simple_notify);
        Pref.setValue(context,"isInBackground","true");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}