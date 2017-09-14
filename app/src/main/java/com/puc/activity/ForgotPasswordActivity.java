package com.puc.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.puc.R;
import com.puc.databinding.ActivityForgotPasswordBinding;
import com.puc.databinding.ActivityLoginBinding;
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

public class ForgotPasswordActivity extends BaseActivity {

    private ActivityForgotPasswordBinding mBinding;
    boolean isValidation = true;
    char c;
    private String TAG = "ForgotPasswordActivity";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        mContext = ForgotPasswordActivity.this;

        prepareView();
        setOnClickListener();
        setup();


    }

    private void prepareView() {

    }

    private void setOnClickListener() {

    }

    private void setup() {


        mBinding.imgBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.edtUsernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlUsernameInput.setError(null);
            }
        });



        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isValidation = true;
                if (TextUtils.isEmpty(mBinding.tlUsernameInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlUsernameInput.setError(getString(R.string.please_provide_email_id_phone_number));
                }

                if (mBinding.edtUsernameInput.getText().length() >= 1) {


                    if ((mBinding.edtUsernameInput.getText().charAt(0) >= 'a' && mBinding.edtUsernameInput.getText().charAt(0) <= 'z') || (mBinding.edtUsernameInput.getText().charAt(0) >= 'A' && mBinding.edtUsernameInput.getText().charAt(0) <= 'Z')) {
                        if (!Utils.isValidEmail(mBinding.edtUsernameInput.getText().toString().trim())) {
                            isValidation = false;
                            mBinding.tlUsernameInput.setError(getString(R.string.please_enter_valid_email_Address));
                        }
                    }
                    /*else{
                        isValidation = true;
                    }*/
                    if (mBinding.edtUsernameInput.getText().charAt(0) >= '0' && mBinding.edtUsernameInput.getText().charAt(0) <= '9' && !Utils.isValidPhone(mBinding.edtUsernameInput.getText().toString().trim())) {
                        isValidation = false;
                        mBinding.tlUsernameInput.setError(getString(R.string.please_enter_valid_phone_number));
                    }
                    /*else{
                        isValidation = true;
                    }*/
                }



                if (isValidation) {

                    callForgotPwdAPI(mBinding.edtUsernameInput.getText().toString());
                    Utils.showProgress(mContext);
                }


            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        Pref.setValue(ForgotPasswordActivity.this, Constants.PREF_USER_ID, "");
    }

    private void callForgotPwdAPI(String emailphone) {

        ApiInterface apiService = ApiClient.getClient(ForgotPasswordActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.forgetpassword(emailphone);
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
                            startActivity(new Intent(ForgotPasswordActivity.this,OtpVerificationActivity.class).putExtra("isScreenSelection","Forgot").putExtra("lastDigitPhone",""));
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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


}
