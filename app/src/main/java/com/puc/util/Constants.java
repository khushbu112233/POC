package com.puc.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by dharmesh on 20/7/16.
 */
public class Constants {
    public static Context _context;

    //login
    public static String PREF_TOKEN = "PREF_TOKEN";
    public static String PREF_USER_ID = "PREF_USER_ID";
    public static String PREF_EMAIL = "PREF_EMAIL";
    public static String PREF_PHONE = "PREF_PHONE";
    public static String Acc_id_tag = "acc_id";
    //signup
    public static String Qr_code = "Qr_Code";
    public static String Cabinet_Login_tag = "cabinet_login";
    public static String Cabinet_Password_tag = "cabinet_passwd";
    public static String Cabinet_Pin_tag = "cabinet_pin";
    public static String Trade_Acc_tag = "trade_acc";
    public static String Traiding_Password_tag = "trading_passwd";
    public static String Investor_Password_tag = "investor_passwd";
    //for request sms
    public static String Phone_CodeR = "phone_code";
    public static String Phone_NoR = "phone_no";
    public static String OperR = "oper";
    //for validate sms
    public static String Phone_CodeV = "phone_code";
    public static String Phone_NoV = "phone_no";
    public static String OperV = "oper";
    public static String CodeV="code";





}
