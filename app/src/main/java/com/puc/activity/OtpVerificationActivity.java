package com.puc.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.puc.R;
import com.puc.databinding.ActivityOtpVerificationBinding;
import com.puc.network.ApiClient;
import com.puc.network.ApiInterface;
import com.puc.util.Constants;
import com.puc.util.Pref;
import com.puc.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends BaseActivity {

    ActivityOtpVerificationBinding mBinding;
    Context mContext;
    private String TAG = "OtpVerificationActivity";
    String otpString;
    Intent mIntent;
    String isScreenSelection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verification);
        mContext = OtpVerificationActivity.this;
        mIntent = getIntent();
        isScreenSelection = mIntent.getStringExtra("isScreenSelection");
        prepareView();
        setOnClickListener();


    }

    private void prepareView() {
        if (isScreenSelection.equals("Signup")) {
            mBinding.tvMainDesc.setText("sent to your phone number ending in " + mIntent.getStringExtra("lastDigitPhone") + ".");
        } else {
            mBinding.tvMainDesc.setText("sent to your phone number or email.");
        }
    }


    private void setOnClickListener() {
        displayCountDown();

        mBinding.imgBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callResendOtpAPI(mContext);
                Utils.showProgress(mContext);
            }
        });

        mBinding.btnSubmitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(mBinding.edtFirstOtp.getText().toString().trim()) && !TextUtils.isEmpty(mBinding.edtSecondOtp.getText().toString().trim()) && !TextUtils.isEmpty(mBinding.edtThirdOtp.getText().toString().trim()) && !TextUtils.isEmpty(mBinding.edtFourOtp.getText().toString().trim()) && !TextUtils.isEmpty(mBinding.edtFiveOtp.getText().toString().trim()) && !TextUtils.isEmpty(mBinding.edtSixOtp.getText().toString().trim())) {
                    otpString = mBinding.edtFirstOtp.getText().toString().trim() + "" + mBinding.edtSecondOtp.getText().toString().trim() + "" + mBinding.edtThirdOtp.getText().toString().trim() + "" + mBinding.edtFourOtp.getText().toString().trim() + "" + mBinding.edtFiveOtp.getText().toString().trim() + "" + mBinding.edtSixOtp.getText().toString().trim();

                    callOTPVerificationAPI(otpString);
                    Utils.showProgress(mContext);
                } else {
                    Utils.showToast(getApplicationContext(), "Please enter remaining field.");
                }
                // }
       /* else{
            Utils.showToast(OtpVerificationActivity.this,"Otp invalid!");
        }*/
            }
        });

        mBinding.edtFirstOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtFirstOtp.getText().toString().trim().length() == 1) {

                    mBinding.edtFirstOtp.clearFocus();
                    mBinding.edtSecondOtp.requestFocus();
                    //mBinding.edtFirstOtp.setBackgroundResource(R.drawable.passcode_bg_circle);
                    //etxtPin1.setBackground(getResources().getDrawable(R.drawable.pin_txt_bg_star));
                }
            }
        });


        mBinding.edtSecondOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtSecondOtp.getText().toString().trim().length() == 1) {

                    mBinding.edtSecondOtp.clearFocus();
                    mBinding.edtThirdOtp.requestFocus();
                    // mBinding.edtSecondOtp.setBackgroundResource(R.drawable.passcode_bg_circle);
                }
            }
        });

        mBinding.edtThirdOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtThirdOtp.getText().toString().trim().length() == 1) {

                    mBinding.edtThirdOtp.clearFocus();
                    mBinding.edtFourOtp.requestFocus();

                    //mBinding.edtThirdOtp.setBackgroundResource(R.drawable.passcode_bg_circle);
                }

            }
        });

        mBinding.edtFourOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtFourOtp.getText().toString().trim().length() == 1) {

                    mBinding.edtFourOtp.clearFocus();
                    mBinding.edtFiveOtp.requestFocus();

                    //mBinding.edtThirdOtp.setBackgroundResource(R.drawable.passcode_bg_circle);
                }

            }
        });

        mBinding.edtFiveOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtFiveOtp.getText().toString().trim().length() == 1) {

                    mBinding.edtFiveOtp.clearFocus();
                    mBinding.edtSixOtp.requestFocus();

                    //mBinding.edtThirdOtp.setBackgroundResource(R.drawable.passcode_bg_circle);
                }

            }
        });

        mBinding.edtSixOtp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtSixOtp.getText().toString().trim().length() == 1) {

                    //mBinding.edtFourOtp.setBackgroundResource(R.drawable.passcode_bg_circle);

                    if (mBinding.edtFirstOtp.getText().toString().isEmpty() || mBinding.edtSecondOtp.getText().toString().isEmpty() || mBinding.edtThirdOtp.getText().toString().isEmpty() || mBinding.edtFourOtp.getText().toString().isEmpty() || mBinding.edtFiveOtp.getText().toString().isEmpty() || mBinding.edtSixOtp.getText().toString().isEmpty()) {
                        Toast.makeText(OtpVerificationActivity.this, "Please enter remaining field.", Toast.LENGTH_SHORT).show();
                    } else {

                        /*passccodeString = mBinding.edtFirstOtp.getText().toString().trim() + "" + mBinding.edtSecondOtp.getText().toString().trim() + "" + mBinding.edtThirdOtp.getText().toString().trim() + "" + mBinding.edtFourOtp.getText().toString().trim();

                        if (passccodeString.equalsIgnoreCase(Pref.getValue(WelcomeSecurityPasscodeActivity.this, Constants.PREF_PASSCODE, ""))) {
                            startActivity(new Intent(WelcomeSecurityPasscodeActivity.this, DashboardTabActivity.class));
                            finish();
                        } else {
                            mBinding.edtFirstOtp.setText("");
                            mBinding.edtSecondOtp.setText("");
                            mBinding.edtThirdOtp.setText("");
                            mBinding.edtFourOtp.setText("");
                            mBinding.edtFirstOtp.requestFocus();
                          //  mMainRl.startAnimation(shackAnimation);
                          //  mVibration.vibrate(500);

                        }*/
                        /*otpString = mBinding.edtFirstOtp.getText().toString().trim() + "" + mBinding.edtSecondOtp.getText().toString().trim() + "" + mBinding.edtThirdOtp.getText().toString().trim() + "" + mBinding.edtFourOtp.getText().toString().trim()+ "" + mBinding.edtFiveOtp.getText().toString().trim() + "" + mBinding.edtSixOtp.getText().toString().trim();
                        if(otpString.equals("1111")){
                            if(isScreenSelection.equals("Signup")) {
                                startActivity(new Intent(mContext, DashboardActivity.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(mContext,ResetPasswordActivity.class));
                                finish();
                            }
                        }
                        else{
                            Utils.showToast(OtpVerificationActivity.this,"Otp invalid!");
                        }*/
                        // callOTPVerificationAPI(otpString);
                        // Utils.showProgress(mContext);
                    }
                }

            }
        });


        mBinding.edtSecondOtp.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && OtpVerificationActivity.this.mBinding.edtSecondOtp.getText().length() == 0) {
                    mBinding.edtFirstOtp.requestFocus();
                    //mBinding.edtFirstOtp.setBackgroundResource(R.drawable.passcode_bg);
                    mBinding.edtFirstOtp.setText("");
                }

                return false;
            }
        });
        mBinding.edtThirdOtp.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && OtpVerificationActivity.this.mBinding.edtThirdOtp.getText().length() == 0) {
                    mBinding.edtSecondOtp.requestFocus();
                    // mBinding.edtSecondOtp.setBackgroundResource(R.drawable.passcode_bg);
                    mBinding.edtSecondOtp.setText("");
                }

                return false;
            }
        });

        mBinding.edtFourOtp.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && OtpVerificationActivity.this.mBinding.edtFourOtp.getText().length() == 0) {
                    mBinding.edtThirdOtp.requestFocus();
                    // mBinding.edtSecondOtp.setBackgroundResource(R.drawable.passcode_bg);
                    mBinding.edtThirdOtp.setText("");
                }

                return false;
            }
        });

        mBinding.edtFiveOtp.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && OtpVerificationActivity.this.mBinding.edtFiveOtp.getText().length() == 0) {
                    mBinding.edtFourOtp.requestFocus();
                    // mBinding.edtSecondOtp.setBackgroundResource(R.drawable.passcode_bg);
                    mBinding.edtFourOtp.setText("");
                }

                return false;
            }
        });
        mBinding.edtSixOtp.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && OtpVerificationActivity.this.mBinding.edtSixOtp.getText().length() == 0) {

                    mBinding.edtFiveOtp.requestFocus();
                    //mBinding.edtThirdOtp.setBackgroundResource(R.drawable.passcode_bg);

                    mBinding.edtFiveOtp.setText("");
                }

                return false;
            }
        });

    }

    private void displayCountDown() {

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                mBinding.tvResendOtp.setEnabled(false);
                mBinding.tvResendOtp.setText("Resend in " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mBinding.tvResendOtp.setEnabled(true);
                mBinding.tvResendOtp.setText("RESEND");
            }

        }.start();
    }

    private void callOTPVerificationAPI(String otp) {

        ApiInterface apiService = ApiClient.getClient(OtpVerificationActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.verifyotp(otp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, "response " + response.toString());
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.optString("code").equals("200")) {
                            Pref.setValue(OtpVerificationActivity.this, Constants.PREF_USER_ID, jsonObject.optString("id"));

                            if (isScreenSelection.equals("Signup")) {
                                startActivity(new Intent(mContext, DashboardActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(mContext, ResetPasswordActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(mContext, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }

                        Log.e(TAG, "cancel " + jsonObject.toString());

                    } else {
                        Log.e(TAG, "error " + response.errorBody().toString());
                        // Log.e(TAG,"Else " + response.message() + " code " +response.code());
                        String errorValue = response.errorBody().string();

                        JSONObject errorJsonObject = new JSONObject(errorValue);
                        Utils.showToast(getApplicationContext(), errorJsonObject.optString("msg"));
                        //  Log.e(TAG,"Error " + errorValue);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void callResendOtpAPI(final Context mContext) {

        ApiInterface apiService = ApiClient.getClient(OtpVerificationActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.resendotp(Pref.getValue(mContext,Constants.PREF_USER_ID,""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, "response " + response.toString());
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.optString("code").equals("200")) {
                            Utils.showToast(OtpVerificationActivity.this,"Code resend successfully.");

                        } else {
                            Toast.makeText(mContext, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Log.e(TAG, "error " + response.errorBody().toString());
                        // Log.e(TAG,"Else " + response.message() + " code " +response.code());
                        String errorValue = response.errorBody().string();

                        JSONObject errorJsonObject = new JSONObject(errorValue);
                        Utils.showToast(getApplicationContext(), errorJsonObject.optString("msg"));
                        //  Log.e(TAG,"Error " + errorValue);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
