package com.puc.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.puc.service.AlarmReceiver1;

import static android.content.Context.ALARM_SERVICE;

public class ApplicationLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = "Running";
    private static boolean isInBackground = false;
    Context mContext;
    AlarmManager alarmManager;
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mContext=activity;
        alarmManager=(AlarmManager) mContext.getSystemService(ALARM_SERVICE);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

        if(isInBackground){
            Log.d(TAG, "app went to foreground");
           // Pref.setValue(mContext,"isInBackground","false");
            isInBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Pref.setValue(mContext,"APP_BACKGROUND","1");
        Log.d(TAG, "app went to kill");
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i) {
        if(i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            Log.d(TAG, "app went to background");

           /* Intent alarmIntent = new Intent(mContext, AlarmReceiver1.class); // AlarmReceiver1 = broadcast receiver
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1*60*1000, pendingIntent);*/
            isInBackground = true;
        }
    }
}