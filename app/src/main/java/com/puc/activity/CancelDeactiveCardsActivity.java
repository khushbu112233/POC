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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.puc.R;
import com.puc.adapter.CancelDeactiveCardsListAdapter;
import com.puc.databinding.ActivityCanceldeactiveCardsBinding;
import com.puc.databinding.ActivitySaveCardsBinding;
import com.puc.interfaces.OnClickCalltoBankCardListener;
import com.puc.interfaces.OnClickCancelCardRequestDetailListener;
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

public class CancelDeactiveCardsActivity extends BaseActivity {

    ActivityCanceldeactiveCardsBinding mBinding;
    CancelDeactiveCardsListAdapter cancelCardsListAdapter;
    Context mContext;
    ArrayList<SaveCardListModel> cardSelectionListModelArrayList = new ArrayList<>();
    SaveCardListModel[] cardSelectionListModels = new SaveCardListModel[15];
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private String TAG = "CancelDeactiveCards";
    private int PEMISION_REQUEST_CALL_PHONE= 11;
    String callNumber="";
    Dialog challengeDialog;
    Dialog listDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_canceldeactive_cards);

        mContext = CancelDeactiveCardsActivity.this;


        prepareView();
        setOnClickLiestner();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.tvEditCard.setVisibility(View.GONE);
      /*  if(mBinding.menuGreen.isOpened()){
            mBinding.menuGreen.close(true);
        }*/
        callsaveCardListAPI();
        Utils.showProgress(mContext);
    }

    private void prepareView() {

        //for this selectall use is to check user open edit and delete function for that layout...
        Pref.setValue(mContext, "selectall", "2");
      /*  for (int i = 0; i < 10; i++) {
            cardSelectionListModels[i] = new SaveCardListModel();
            cardSelectionListModels[i].setChecked(false);
            cardSelectionListModelArrayList.add(cardSelectionListModels[i]);
        }
        cancelCardsListAdapter = new cancelCardsListAdapter(SaveCardsActivity.this, cardSelectionListModelArrayList, false, mBinding.tvEditCard, mBinding.tvDelete);
        mBinding.lvSaveCard.setAdapter(cancelCardsListAdapter);*/

     /*   if (Pref.getValue(mContext, "selectall", "").equals("2")) {
            mBinding.menuGreen.setVisibility(View.VISIBLE);
        } else {
            mBinding.menuGreen.setVisibility(View.GONE);
        }*/

       // menus.add(mBinding.menuGreen);
        //createCustomAnimation();
    }

    private void setOnClickLiestner() {
        mBinding.tvEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mBinding.menuGreen.setVisibility(View.INVISIBLE);
               // mBinding.menuGreen.hideMenu(true);
                if (Pref.getValue(mContext, "selectall", "").equals("2")) {

                    cancelCardsListAdapter = new CancelDeactiveCardsListAdapter(CancelDeactiveCardsActivity.this, cardSelectionListModelArrayList, true, mBinding.tvEditCard, mBinding.tvDelete, 0);
                    cancelCardsListAdapter.onClickCancelCardDetailListener(onClickCancelCardRequestDetailListener);
                    cancelCardsListAdapter.onClickCalltoBankCardListener((onClickCalltoBankCardListener));
                    mBinding.lvSaveCard.setAdapter(cancelCardsListAdapter);
                } else if (Pref.getValue(mContext, "selectall", "").equals("0")) {
                   // cancelCardsListAdapter = new CancelDeactiveCardsListAdapter(CancelDeactiveCardsActivity.this, cardSelectionListModelArrayList, true, mBinding.tvEditCard, mBinding.tvDelete, 1);
                   // mBinding.lvSaveCard.setAdapter(cancelCardsListAdapter);
                    //isUsercanEditOpen();
                    Pref.setValue(mContext, "selectall", "2");
                } else if (Pref.getValue(mContext, "selectall", "").equals("1")) {
                  //  cancelCardsListAdapter = new CancelDeactiveCardsListAdapter(CancelDeactiveCardsActivity.this, cardSelectionListModelArrayList, true, mBinding.tvEditCard, mBinding.tvDelete, 0);
                  //  mBinding.lvSaveCard.setAdapter(cancelCardsListAdapter);
                    //isUsercanEditOpen();
                    Pref.setValue(mContext, "selectall", "2");
                }


            }
        });

        mBinding.imgBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (Pref.getValue(mContext, "selectall", "").equals("0") || Pref.getValue(mContext, "selectall", "").equals("1")) {

                    isUsercanEditOpen();
                } else {*/
                    finish();
               // }
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
            public void onClick(View v) {
                startActivity(new Intent(CancelDeactiveCardsActivity.this, AddCreditDebitCardActivity.class));
            }
        });

    /*    mBinding.fbAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CancelDeactiveCardsActivity.this, AddCreditDebitCardActivity.class));
            }
        });*/

        mBinding.fbRequestNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CancelDeactiveCardsActivity.this, RequestNewApplicationCardActivity.class));
            }
        });
    }

 /*   private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(mBinding.menuGreen.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(mBinding.menuGreen.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(mBinding.menuGreen.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(mBinding.menuGreen.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mBinding.menuGreen.getMenuIconView().setImageResource(mBinding.menuGreen.isOpened()
                        ? R.mipmap.ic_close_float : R.mipmap.plus_float_img);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        mBinding.menuGreen.setIconToggleAnimatorSet(set);
    }*/

    private void callsaveCardListAPI() {

        ApiInterface apiService = ApiClient.getClient(CancelDeactiveCardsActivity.this).create(ApiInterface.class);
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

                                SaveCardListModel saveCardListModel = gson.fromJson(payloadJSONObject.toString(), SaveCardListModel.class);
                                if(saveCardListModel.requestednewcard.equals("N")) {
                                    cardSelectionListModelArrayList.add(saveCardListModel);
                                }
                            }

                            Collections.sort(cardSelectionListModelArrayList, new Comparator<SaveCardListModel>() {
                                @Override
                                public int compare(SaveCardListModel sp1, SaveCardListModel sp2) {
                                    return sp1.status.compareTo(sp2.status);

                                }
                            });


                            cancelCardsListAdapter = new CancelDeactiveCardsListAdapter(CancelDeactiveCardsActivity.this, cardSelectionListModelArrayList, false, mBinding.tvEditCard, mBinding.tvDelete);
                            cancelCardsListAdapter.onClickCancelCardDetailListener(onClickCancelCardRequestDetailListener);
                            cancelCardsListAdapter.onClickCalltoBankCardListener((onClickCalltoBankCardListener));
                            mBinding.lvSaveCard.setAdapter(cancelCardsListAdapter);
                        }
                        else if(jsonObject.optString("code").equals("500")){
                            Utils.exitApplication(CancelDeactiveCardsActivity.this);
                        }
                        else if(jsonObject.optString("code").equals("401")){
                            mBinding.tvEditCard.setVisibility(View.GONE);
                            mBinding.lnTapHereMsg.setVisibility(View.VISIBLE);
                        }
                        else {
                            mBinding.tvEditCard.setVisibility(View.GONE);
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

    OnClickCancelCardRequestDetailListener onClickCancelCardRequestDetailListener=new OnClickCancelCardRequestDetailListener() {
        @Override
        public void onClickCancelCardListener(JSONArray card_id, int position) {
            String card_id1=card_id.toString();
            callcancelCardListAPI(card_id1,position);
            Utils.showProgress(mContext);
        }
    };

    OnClickDeleteCardDetailListener onClickDeleteCardDetailListener=new OnClickDeleteCardDetailListener() {
        @Override
        public void onClickCancelSignalMessage(JSONArray card_Id_Array,int position) {
            String card_id=card_Id_Array.toString();
            callcancelCardListAPI(card_id,position);
            Utils.showProgress(mContext);
        }
    };


    OnClickEditCardDetailListener onClickEditCardDetailListener=new OnClickEditCardDetailListener() {
        @Override
        public void onClickCancelSignalMessage(ArrayList<SaveCardListModel> model, int position) {
            startActivity(new Intent(CancelDeactiveCardsActivity.this,EditCreditCardDetailActivity.class).putExtra("card_model_detail",model).putExtra("position",position));
        }
    };

    OnClickCalltoBankCardListener onClickCalltoBankCardListener=new OnClickCalltoBankCardListener() {
        @Override
        public void onclickcalltobankcard(String position) {
            callNumber=position;
            checkRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, PEMISION_REQUEST_CALL_PHONE);
        }
    };

    private void callcancelCardListAPI(String card_id, final int position) {

        ApiInterface apiService = ApiClient.getClient(((FragmentActivity)mContext)).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.cancelcardDeactive(Pref.getValue(mContext,Constants.PREF_TOKEN,""),card_id,"");
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
                            mContext.startActivity(new Intent(mContext,CancelDeactiveCardsActivity.class));
                            ((FragmentActivity) mContext).overridePendingTransition(0, 0);
                            ((FragmentActivity) mContext).finish();
                        }
                        else if(jsonObject.optString("code").equals("500")){
                            Utils.exitApplication(mContext);
                        }
                        else {
                            Toast.makeText(mContext, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }

                        Log.e("DeleteAdapter", "cancel " + jsonObject.toString());

                    } else {
                        Log.e("DeleteAdapter", "error " + response.errorBody().toString());
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

    /**
     * dialog for expired signal when user click on expire signal then popup
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
        // super.onBackPressed();
       /* if (Pref.getValue(mContext, "selectall", "").equals("0") || Pref.getValue(mContext, "selectall", "").equals("1")) {
            isUsercanEditOpen();
        } else {*/
            finish();
       // }
    }


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

        mTitleTv.setText(Html.fromHtml("* You can cancel single/multiple cards at a time. " + "<br />" + "* Cancelled cards are highlighted with "+"<font color=#606060>" + "<big>"
                + " Grey" + "</big>" + "</font>" + ""+" color." +"<br />" + "* Contact respective bank by clicking on call icon in case of any concern/query/immergancy."));

        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
            }
        });


        listDialog.show();
    }


    /**
     * for check on back presss to view refresh and adapter refresh..
     */
    private void isUsercanEditOpen() {
        Pref.setValue(mContext, "selectall", "2");
        mBinding.tvEditCard.setText("Edit");
        mBinding.menuGreen.setVisibility(View.VISIBLE);
        mBinding.tvDelete.setVisibility(View.GONE);
        cancelCardsListAdapter = new CancelDeactiveCardsListAdapter(CancelDeactiveCardsActivity.this, cardSelectionListModelArrayList, false, mBinding.tvEditCard, mBinding.tvDelete);
        cancelCardsListAdapter.onClickCancelCardDetailListener(onClickCancelCardRequestDetailListener);
        cancelCardsListAdapter.onClickCalltoBankCardListener((onClickCalltoBankCardListener));
        mBinding.lvSaveCard.setAdapter(cancelCardsListAdapter);
    }
}
