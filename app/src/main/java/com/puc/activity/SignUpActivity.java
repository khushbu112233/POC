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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.puc.R;
import com.puc.databinding.ActivitySignUpBinding;
import com.puc.network.ApiClient;
import com.puc.network.ApiInterface;
import com.puc.util.Constants;
import com.puc.util.Pref;
import com.puc.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {


    boolean isValidation = true;

    ActivitySignUpBinding mBinding;
    private String TAG="SignUpActivity";
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mContext=SignUpActivity.this;

        setup();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Pref.setValue(SignUpActivity.this, Constants.PREF_USER_ID, "");
    }

    private void setup() {


        mBinding.imgBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.edtEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlEmailInput.setError(null);
            }
        });

        mBinding.edtPhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlPhoneInput.setError(null);
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

        mBinding.edtPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlPasswordInput.setError(null);
            }
        });


        mBinding.txtSendVarification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isValidation = true;
                if (TextUtils.isEmpty(mBinding.tlEmailInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlEmailInput.setError(getString(R.string.please_provide_email_address));
                }

                if (mBinding.tlEmailInput.getEditText().getText().toString().length()>=1 && !Utils.isValidEmail(mBinding.edtEmailInput.getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlEmailInput.setError(getString(R.string.please_enter_valid_email_Address));
                }

                if (TextUtils.isEmpty(mBinding.tlPhoneInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlPhoneInput.setError(getString(R.string.please_provide_phone_no));
                }

                if (mBinding.tlPhoneInput.getEditText().getText().toString().length()>=1 && !Utils.isValidPhone(mBinding.edtPhoneInput.getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlPhoneInput.setError(getString(R.string.please_enter_valid_phone_number));
                }



                if (TextUtils.isEmpty(mBinding.tlUsernameInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlUsernameInput.setError(getString(R.string.please_enter_username));
                }

                if (TextUtils.isEmpty(mBinding.tlPasswordInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlPasswordInput.setError(getString(R.string.Please_provide_password));
                }

                if (mBinding.tlPasswordInput.getEditText().getText().toString().length()>=1 && !Utils.isValidPassword(mBinding.edtPasswordInput.getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlPasswordInput.setError("Minimum of six characters with one capital, one lowercase, and one special character.");
                }

                if (isValidation) {
                    Log.e("yyy", "Validation");
                    callRegistrationAPI(mBinding.edtEmailInput.getText().toString(),mBinding.edtPhoneInput.getText().toString(),mBinding.edtPasswordInput.getText().toString());
                    Utils.showProgress(mContext);
                }


            }
        });


    }

    private void callRegistrationAPI(String email, String phone, String pwd) {

        ApiInterface apiService = ApiClient.getClient(SignUpActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.registration(email,phone,pwd);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, "response " + response.toString());
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if(jsonObject.optString("code").equals("200")){
                            JSONObject payloadJsonObject1=jsonObject.optJSONObject("payload");
                            Pref.setValue(mContext, Constants.PREF_TOKEN,jsonObject.optString("_token"));
                            Pref.setValue(mContext, Constants.PREF_USER_ID,jsonObject.optString("userid"));
                            String lastDegitPhoneNo=payloadJsonObject1.optString("phone");
                            startActivity(new Intent(SignUpActivity.this,OtpVerificationActivity.class).putExtra("isScreenSelection","Signup").putExtra("lastDigitPhone",lastDegitPhoneNo.substring(lastDegitPhoneNo.length()-4)));
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, ""+jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }

                        Log.e(TAG, "cancel " + jsonObject.toString());

                    }

                    else {
                        Log.e(TAG, "error " + response.errorBody().toString());
                        // Log.e(TAG,"Else " + response.message() + " code " +response.code());
                        String errorValue = response.errorBody().string();

                        JSONObject errorJsonObject = new JSONObject(errorValue);
                        Utils.showToast(getApplicationContext(),errorJsonObject.optString("msg"));
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
