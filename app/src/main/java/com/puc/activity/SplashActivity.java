package com.puc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.puc.R;
import com.puc.util.Constants;
import com.puc.util.Pref;
import com.puc.util.Utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by dharmesh on 3/8/17.
 */
public class SplashActivity extends BaseActivity {


    boolean isFinish = false;
    int _splashTime = 3000;

    LinearLayout ll_splash_main;
    LinearLayout splash_layout;
    Handler mSplashHandler;
    String decry;
    private Runnable mSplashRunnable = new Runnable() {
        @Override
        public void run() {

            isFinish = true;
            if(!Pref.getValue(SplashActivity.this, Constants.PREF_TOKEN,"").equals("")){
                startActivity(new Intent(SplashActivity.this,DashboardActivity.class));
                finish();
            }
            else{
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_layout = (LinearLayout) findViewById(R.id.splash_layout);
        ll_splash_main = (LinearLayout) findViewById(R.id.ll_splash_main);

        StartAnimations();


    }


    @Override
    protected void onResume() {
        super.onResume();

        mSplashHandler = new Handler();
        mSplashHandler.postDelayed(mSplashRunnable, _splashTime);



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isFinish) {
            if (mSplashRunnable != null) {
                if (mSplashHandler != null) {
                    mSplashHandler.removeCallbacks(mSplashRunnable);
                }
            }
        }

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        splash_layout.clearAnimation();
        splash_layout.startAnimation(anim);

       /* anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // mScannerView.stopCamera();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        SplashActivity.this.overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }


}
