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
import com.puc.databinding.ActivityChangePasswordBinding;
import com.puc.databinding.ActivityResetPasswordBinding;
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


public class ResetPasswordActivity extends BaseActivity {

    private ActivityResetPasswordBinding mBinding;
    boolean isValidation = true;
    char c;
    private String TAG = "LoginActivity";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        mContext = ResetPasswordActivity.this;

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


        mBinding.edtNewPwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlNewPwdInput.setError(null);
            }
        });

        mBinding.edtConfirmNewPwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlConfirmNewPwdInput.setError(null);
            }
        });

        mBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isValidation = true;


                if (TextUtils.isEmpty(mBinding.tlNewPwdInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlNewPwdInput.setError("Please provide new password.");
                }


                if (mBinding.tlNewPwdInput.getEditText().getText().toString().length() >= 1 && !Utils.isValidPassword(mBinding.edtNewPwdInput.getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlNewPwdInput.setError("Minimum of six characters with one capital, one lowercase, and one special character.");
                }

                if (TextUtils.isEmpty(mBinding.tlConfirmNewPwdInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlConfirmNewPwdInput.setError("Please provide confirm new password.");
                }


                if (mBinding.tlConfirmNewPwdInput.getEditText().getText().toString().length() >= 1 && !mBinding.edtNewPwdInput.getText().toString().trim().equals(mBinding.edtConfirmNewPwdInput.getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlConfirmNewPwdInput.setError("Both password not matched!");
                }

                if (isValidation) {
                    callResetPasswordAPI(mBinding.edtConfirmNewPwdInput.getText().toString());
                    Utils.showProgress(mContext);
                }


            }
        });

    }


    private void callResetPasswordAPI(String newPassword) {

        ApiInterface apiService = ApiClient.getClient(ResetPasswordActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.usernewpassowrd(Pref.getValue(ResetPasswordActivity.this, Constants.PREF_USER_ID, ""), newPassword);
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
                            Utils.showToast(ResetPasswordActivity.this,"New Password Changed Successfully!");
                            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                            finishAffinity();
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
}




