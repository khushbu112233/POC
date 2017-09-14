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

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding mBinding;
    boolean isValidation = true;
    char c;
    private String TAG = "LoginActivity";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = LoginActivity.this;

        prepareView();
        setOnClickListener();
        setup();


    }

    private void prepareView() {

    }

    private void setOnClickListener() {

    }

    private void setup() {

        /*mBinding.edtUsernameInput.setText("test10@gmail.com");
        mBinding.edtPasswordInput.setText("testTest1!");*/


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

                if (TextUtils.isEmpty(mBinding.tlPasswordInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlPasswordInput.setError(getString(R.string.Please_provide_password));
                }

               /* if (mBinding.tlPasswordInput.getEditText().getText().toString().length()>=1 && !Utils.isValidPassword(mBinding.edtPasswordInput.getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlPasswordInput.setError("Minimum of six characters with one capital, one lowercase, and one special character.");
                }*/

                if (isValidation) {
                    callLoginAPI(mBinding.edtUsernameInput.getText().toString(), mBinding.edtPasswordInput.getText().toString());
                    Utils.showProgress(mContext);
                }


            }
        });

        mBinding.tvForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        mBinding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }

    private void callLoginAPI(String emailphone, String pwd) {

        ApiInterface apiService = ApiClient.getClient(LoginActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.login(emailphone, pwd);
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
                            JSONObject payloadJsonObject1 = jsonObject.optJSONObject("payload");
                            Pref.setValue(mContext, Constants.PREF_TOKEN, payloadJsonObject1.optString("email"));
                            Pref.setValue(mContext, Constants.PREF_TOKEN, payloadJsonObject1.optString("phone"));
                            Pref.setValue(mContext, Constants.PREF_TOKEN, jsonObject.optString("_token"));

                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
