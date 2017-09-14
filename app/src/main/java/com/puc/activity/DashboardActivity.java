package com.puc.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.puc.R;
import com.puc.databinding.ActivityDashboardBinding;
import com.puc.network.ApiClient;
import com.puc.network.ApiInterface;
import com.puc.util.Constants;
import com.puc.util.Pref;
import com.puc.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity {

    ActivityDashboardBinding mBinding;
    private String TAG = "DashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        mBinding.lnSavedCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, SaveCardsActivity.class));
            }
        });

        mBinding.lnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AddCreditDebitCardActivity.class));
            }
        });

        mBinding.lnRequestNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(DashboardActivity.this, RequestForApplyNewCreditCardActivity.class));

                startActivity(new Intent(DashboardActivity.this, RequestNewApplicationCardActivity.class));
            }
        });

        mBinding.lnCancelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CancelDeactiveCardsActivity.class));
            }
        });

        mBinding.tvAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AddCreditDebitCardActivity.class));
            }
        });


        mBinding.tvSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, SaveCardsActivity.class));
            }
        });

        mBinding.tvApplyNewCredtitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, RequestForApplyNewCreditCardActivity.class));
            }
        });

        mBinding.imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

        mBinding.imgChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ChangePasswordActivity.class));
            }
        });
    }

    private void logoutDialog() {

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("MyCardSecured")
                .setContentText("Are you sure you want to logout?")
                .setCustomImage(R.mipmap.logout_lock)
                .setCancelText("Cancel")
                .setConfirmText("Logout")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        /*sDialog.setTitleText("Logout!")
                               *//* .setContentText("Your imaginary file has been deleted!")*//*
                                .setConfirmText("OK")
                                .showCancelButton(false)

                                *//*.setCancelClickListener(null)*//*
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
*/
                        callLOGOUTAPI();
                        Utils.showProgress(DashboardActivity.this);
                        sDialog.dismiss();

                    }
                })
                .show();
    }


    private void callLOGOUTAPI() {

        ApiInterface apiService = ApiClient.getClient(DashboardActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.logout(Pref.getValue(getApplicationContext(), Constants.PREF_TOKEN, ""));
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
                            Pref.deleteAll(getApplicationContext());
                            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (jsonObject.optString("code").equals("401")) {

                        } else if (jsonObject.optString("code").equals("500")) {
                            Utils.exitApplication(DashboardActivity.this);
                        } else {
                            Toast.makeText(getApplicationContext(), "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
