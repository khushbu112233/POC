package com.puc.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.puc.R;


/**
 * Created by aipxperts on 5/7/17.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StatusBar();

    }

    public void openKeyboard()
    {
         Handler mHandler= new Handler();
        final View view = getCurrentFocus();
        view.requestFocus();
        view.postDelayed(new Runnable(){
                               @Override public void run(){
                                   InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(view,0);
                               }
                           }
                ,200);

    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public  void StatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_dark_color));
        }

    }

   /* @Override
    protected void onResume() {
        super.onResume();
        Log.e("Background","000 " +Pref.getValue(getApplicationContext(),"isInBackground",""));
        if(Pref.getValue(getApplicationContext(),"isInBackground","").equals("true")) {
            Log.e("Background","111 " +Pref.getValue(getApplicationContext(),"isInBackground",""));
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver1,
                    new IntentFilter("Simple_notification"));
        }

    }

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Background","8888 ");
            Utils.exitApplication(context);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Background","222 " +Pref.getValue(getApplicationContext(),"isInBackground",""));
        if(Pref.getValue(getApplicationContext(),"isInBackground","").equals("true")) {
            Log.e("Background","333 " +Pref.getValue(getApplicationContext(),"isInBackground",""));
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mMessageReceiver1);
        }

    }*/
}
