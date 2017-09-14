package com.puc.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.puc.R;
import com.puc.databinding.ActivityChangePasswordBinding;
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


public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding mBinding;
    boolean isValidation = true;
    char c;
    private String TAG = "LoginActivity";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mContext = ChangePasswordActivity.this;

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

        mBinding.edtOldPwdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlOldPwdInput.setError(null);
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

        mBinding.btnSubmitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isValidation = true;

                if (TextUtils.isEmpty(mBinding.tlOldPwdInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlOldPwdInput.setError("Please provide old password.");
                }

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
                    callchangePwdAPI(mBinding.edtOldPwdInput.getText().toString(), mBinding.edtConfirmNewPwdInput.getText().toString());
                      Utils.showProgress(mContext);
                    //Toast.makeText(mContext, "Password change successfully!", Toast.LENGTH_SHORT).show();
                   // finish();
                }


            }
        });

    }

    private void callchangePwdAPI(String old_pwd, String new_pwd) {

        ApiInterface apiService = ApiClient.getClient(((FragmentActivity)mContext)).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.changepassword(Pref.getValue(mContext, Constants.PREF_TOKEN,""),old_pwd,new_pwd);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("DeleteAdapter", "response " + response.toString());
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.optString("code").equals("200")) {
                            Utils.showToast(ChangePasswordActivity.this,jsonObject.optString("message"));
                            finish();
                        }
                        else if(jsonObject.optString("code").equals("500")){
                            Utils.exitApplication(mContext);
                        }
                        else {
                            Toast.makeText(mContext, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        // Log.e(TAG,"Else " + response.message() + " code " +response.code());
                        String errorValue = response.errorBody().string();

                        JSONObject errorJsonObject = new JSONObject(errorValue);
                        Utils.showToast(((FragmentActivity)mContext), errorJsonObject.optString("msg"));
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




