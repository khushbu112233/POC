package com.puc.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.puc.R;
import com.puc.adapter.RequestNewApplicationCardListAdapter;
import com.puc.adapter.SavedCardsListAdapter;
import com.puc.databinding.ActivityRequestNewapplicationCardBinding;
import com.puc.databinding.ActivitySaveCardsBinding;
import com.puc.interfaces.OnClickCalltoBankCardListener;
import com.puc.interfaces.OnClickDeleteCardDetailListener;
import com.puc.interfaces.OnClickEditCardDetailListener;
import com.puc.model.RequestNewCardListModel;
import com.puc.model.SaveCardListModel;
import com.puc.network.ApiClient;
import com.puc.network.ApiInterface;
import com.puc.util.Constants;
import com.puc.util.Pref;
import com.puc.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestNewApplicationCardActivity extends BaseActivity {

    ActivityRequestNewapplicationCardBinding mBinding;
    RequestNewApplicationCardListAdapter requestNewApplicationCardListAdapter;
    Context mContext;
    ArrayList<RequestNewCardListModel> cardSelectionListModelArrayList = new ArrayList<>();
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private String TAG = "RequestNewApplicationCardActivity";
    private int PEMISION_REQUEST_CALL_PHONE= 11;
    String callNumber="";
    Dialog challengeDialog;
    Dialog listDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_newapplication_card);

        mContext = RequestNewApplicationCardActivity.this;


        prepareView();
        setOnClickLiestner();



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Resume","Call");
        callsaveCardListAPI();
        Utils.showProgress(mContext);

    }

    private void prepareView() {

        //for this selectall use is to check user open edit and delete function for that layout...

    }

    private void setOnClickLiestner() {


        mBinding.imgBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    finish();
            }
        });


        mBinding.tvInfoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    infoDialog(mContext);
            }
        });
        mBinding.menuGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestNewApplicationCardActivity.this, AddCreditDebitCardActivity.class));
            }
        });
    }

    /**
     * dialog for option of create unlinked with device funOs and smart watch
     */
    private void infoDialog(Context context) {

        listDialog = new Dialog(context);
        final LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        listDialog.setContentView(R.layout.cust_information_card_dialog);
        listDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = listDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.copyFrom(window.getAttributes());


        listDialog.getWindow().setGravity(Gravity.BOTTOM);
        listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView mTitleTv=(TextView)listDialog.findViewById(R.id.tv_desc_title);
        TextView mOkTv=(TextView)listDialog.findViewById(R.id.tv_ok);

        mTitleTv.setText(Html.fromHtml("* You can request new card on your existing cards. Once you receive new card, add that manually and delete existing card." + "<br />" + "* New requested cards are highlighted with"+"<font color=#76bd44>" + "<big>"
                + " Green" + "</big>" + "</font>" + ""+" color." +"<br />" + "* Contact respective bank by clicking on call icon in case of any concern/query."));

        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
            }
        });


        listDialog.show();
    }

    private void callsaveCardListAPI() {

        ApiInterface apiService = ApiClient.getClient(RequestNewApplicationCardActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.saveCardList(Pref.getValue(mContext, Constants.PREF_TOKEN, ""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, "response " + response.toString());
                    cardSelectionListModelArrayList.clear();
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.optString("code").equals("200")) {
                            JSONArray payloadJsonArray = jsonObject.getJSONArray("payload");

                            if(payloadJsonArray.length()==0){
                                mBinding.lnTapHereMsg.setVisibility(View.VISIBLE);
                            }else{
                                mBinding.lnTapHereMsg.setVisibility(View.GONE);
                            }


                            Gson gson = new Gson();
                            for (int i = 0; i < payloadJsonArray.length(); i++) {
                                JSONObject payloadJSONObject = payloadJsonArray.getJSONObject(i);

                                RequestNewCardListModel RequestNewCardListModel = gson.fromJson(payloadJSONObject.toString(), RequestNewCardListModel.class);
                                Collections.sort(cardSelectionListModelArrayList, new Comparator<RequestNewCardListModel>() {
                                    @Override
                                    public int compare(RequestNewCardListModel sp1, RequestNewCardListModel sp2) {
                                        return sp1.requestednewcard.compareTo(sp2.requestednewcard);

                                    }
                                });
                                if(RequestNewCardListModel.status.equals("Active")) {

                                    cardSelectionListModelArrayList.add(RequestNewCardListModel);
                                }
                            }

                            requestNewApplicationCardListAdapter = new RequestNewApplicationCardListAdapter(RequestNewApplicationCardActivity.this, cardSelectionListModelArrayList, false, mBinding.tvEditCard, mBinding.tvDelete);
                            requestNewApplicationCardListAdapter.onClickCalltoBankCardListener((onClickCalltoBankCardListener));
                            mBinding.lvSaveCard.setAdapter(requestNewApplicationCardListAdapter);
                        } else if(jsonObject.optString("code").equals("500")){
                            Utils.exitApplication(RequestNewApplicationCardActivity.this);
                        }
                        else if(jsonObject.optString("code").equals("401")){
                            mBinding.tvEditCard.setVisibility(View.GONE);
                            mBinding.lnTapHereMsg.setVisibility(View.VISIBLE);
                        }
                        else {
                            mBinding.tvEditCard.setVisibility(View.VISIBLE);
                            mBinding.lnTapHereMsg.setVisibility(View.GONE);
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

    OnClickCalltoBankCardListener onClickCalltoBankCardListener=new OnClickCalltoBankCardListener() {
        @Override
        public void onclickcalltobankcard(String position) {
            callNumber=position;
            checkRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, PEMISION_REQUEST_CALL_PHONE);
        }
    };


    private void checkRunTimePermission(String[] permissionArrays, int REQUEST) {
        // String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, REQUEST);
        } else {

            if (REQUEST == PEMISION_REQUEST_CALL_PHONE) {
                confirmationforCalltoBank(this);
            }


            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        boolean isPermitted = false;

        for (int i = 0; i < grantResults.length; i++) {
            String permission = permissions[i];

            isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                if (!showRationale) {
                    //execute when 'never Ask Again' tick and permission dialog not show
                } else {
                    if (openDialogOnce) {
                        // checkRunTimePermission();
                        if (requestCode == PEMISION_REQUEST_CALL_PHONE) {
                            Toast.makeText(getApplicationContext(), "Please Grant Permissions to phone call", Toast.LENGTH_LONG).show();
                        } else if (requestCode == PEMISION_REQUEST_CALL_PHONE) {
                            Toast.makeText(getApplicationContext(), "Please Grant Permissions to phone call", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

        if (isPermitted) {

            if (requestCode == PEMISION_REQUEST_CALL_PHONE) {
                confirmationforCalltoBank(this);
            }

        }
        //  selectImage();

    }

    /**
     * dialog for call bank to open popup
     */
    private void confirmationforCalltoBank(final Context context) {

        challengeDialog = new Dialog(context,R.style.PauseDialog);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        challengeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        challengeDialog.setContentView(R.layout.confirmation_for_calltobank_card);
        challengeDialog.setCanceledOnTouchOutside(false);
        challengeDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(challengeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        challengeDialog.show();
        challengeDialog.getWindow().setAttributes(lp);

        challengeDialog.getWindow().setGravity(Gravity.CENTER);
        challengeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Button mOkbtn = (Button) challengeDialog.findViewById(R.id.btn_proceed);
        Button mCancelGameBtn=(Button)challengeDialog.findViewById(R.id.btn_cancel_state);
        TextView mChallengeDesc = (TextView) challengeDialog.findViewById(R.id.tv_challenge_desc);
        mChallengeDesc.setText(Html.fromHtml("Call your bank on "+"<font color=#76bd44>" + "<big>"
                + callNumber + "</big>" + "<br />" + "</font>" + ""+"for any query."));
        mCancelGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengeDialog.dismiss();
            }
        });
        mOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                if(!TextUtils.isEmpty(callNumber)) {
                    callIntent.setData(Uri.parse("tel:" + callNumber));
                }
                startActivity(callIntent);
                challengeDialog.dismiss();

            }
        });
        challengeDialog.show();
    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();
            finish();
    }



}
