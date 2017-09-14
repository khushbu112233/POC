package com.puc.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.puc.R;
import com.puc.adapter.SavedCardsListAdapter;
import com.puc.databinding.ActivitySaveCardsBinding;
import com.puc.interfaces.OnClickDeleteCardDetailListener;
import com.puc.interfaces.OnClickEditCardDetailListener;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveCardsActivity extends BaseActivity {

    ActivitySaveCardsBinding mBinding;
    SavedCardsListAdapter savedCardsListAdapter;
    Context mContext;
    ArrayList<SaveCardListModel> cardSelectionListModelArrayList = new ArrayList<>();
    SaveCardListModel[] cardSelectionListModels = new SaveCardListModel[15];
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private String TAG = "SaveCardsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_save_cards);

        mContext = SaveCardsActivity.this;


        //prepareView();
        setOnClickLiestner();



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Resume","Call");
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
        savedCardsListAdapter = new SavedCardsListAdapter(SaveCardsActivity.this, cardSelectionListModelArrayList, false, mBinding.tvEditCard, mBinding.tvDelete);
        mBinding.lvSaveCard.setAdapter(savedCardsListAdapter);*/

        if (Pref.getValue(mContext, "selectall", "").equals("2")) {
            mBinding.menuGreen.setVisibility(View.VISIBLE);
        } else {
            mBinding.menuGreen.setVisibility(View.GONE);
        }

       // menus.add(mBinding.menuGreen);
       // createCustomAnimation();
    }

    private void setOnClickLiestner() {
        mBinding.tvEditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.menuGreen.setVisibility(View.INVISIBLE);
              //  mBinding.menuGreen.hideMenu(true);
                if (Pref.getValue(mContext, "selectall", "").equals("2")) {

                    savedCardsListAdapter = new SavedCardsListAdapter(SaveCardsActivity.this, cardSelectionListModelArrayList, true, mBinding.tvEditCard, mBinding.tvDelete, 0);
                    savedCardsListAdapter.onClickEditCardDetailListener(onClickEditCardDetailListener);

                    mBinding.lvSaveCard.setAdapter(savedCardsListAdapter);
                } else if (Pref.getValue(mContext, "selectall", "").equals("0")) {
                   // savedCardsListAdapter = new SavedCardsListAdapter(SaveCardsActivity.this, cardSelectionListModelArrayList, true, mBinding.tvEditCard, mBinding.tvDelete, 1);
                   // mBinding.lvSaveCard.setAdapter(savedCardsListAdapter);
                    isUsercanEditOpen();
                    Pref.setValue(mContext, "selectall", "2");
                } else if (Pref.getValue(mContext, "selectall", "").equals("1")) {
                  //  savedCardsListAdapter = new SavedCardsListAdapter(SaveCardsActivity.this, cardSelectionListModelArrayList, true, mBinding.tvEditCard, mBinding.tvDelete, 0);
                  //  mBinding.lvSaveCard.setAdapter(savedCardsListAdapter);
                    isUsercanEditOpen();
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
/*
        mBinding.tvAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SaveCardsActivity.this, AddCreditDebitCardActivity.class));
            }
        });

        mBinding.fbAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SaveCardsActivity.this, AddCreditDebitCardActivity.class));
            }
        });*/

     /*   mBinding.fbRequestNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SaveCardsActivity.this, RequestNewApplicationCardActivity.class));
            }
        });*/
        mBinding.menuGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SaveCardsActivity.this, AddCreditDebitCardActivity.class));
            }
        });
    }

   /* private void createCustomAnimation() {
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
    }
*/
    private void callsaveCardListAPI() {

        ApiInterface apiService = ApiClient.getClient(SaveCardsActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.saveCardList(Pref.getValue(mContext, Constants.PREF_TOKEN, ""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, "response" + response.toString());
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

                                Log.e("SaveCard####", "&&& " +payloadJSONObject.toString());

                                SaveCardListModel saveCardListModel = gson.fromJson(payloadJSONObject.toString(), SaveCardListModel.class);
                                if(saveCardListModel.status.equals("Active") && saveCardListModel.requestednewcard.equals("N")) {
                                    cardSelectionListModelArrayList.add(saveCardListModel);
                                }
                            }
                            Log.e("response11",""+cardSelectionListModelArrayList);
                            savedCardsListAdapter = new SavedCardsListAdapter(SaveCardsActivity.this, cardSelectionListModelArrayList, false, mBinding.tvEditCard, mBinding.tvDelete);
                            savedCardsListAdapter.onClickEditCardDetailListener(onClickEditCardDetailListener);
                            savedCardsListAdapter.onClickDeleteCardDetailListener(onClickDeleteCardDetailListener);
                            mBinding.lvSaveCard.setAdapter(savedCardsListAdapter);
                        } else if(jsonObject.optString("code").equals("500")){
                            Utils.exitApplication(SaveCardsActivity.this);
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

    OnClickDeleteCardDetailListener onClickDeleteCardDetailListener=new OnClickDeleteCardDetailListener() {
        @Override
        public void onClickCancelSignalMessage(JSONArray card_Id_Array,int position) {
            Log.e("Delete","$$$ " +card_Id_Array.toString());
            String card_id=card_Id_Array.toString();
           // callDeleteCardListAPI(card_id,position);
           // Utils.showProgress(mContext);
        }
    };

    OnClickEditCardDetailListener onClickEditCardDetailListener=new OnClickEditCardDetailListener() {
        @Override
        public void onClickCancelSignalMessage(ArrayList<SaveCardListModel> model, int position) {


            startActivity(new Intent(SaveCardsActivity.this,EditCreditCardDetailActivity.class).putExtra("card_model_detail",model).putExtra("position",position));
            cardSelectionListModelArrayList.clear();
            savedCardsListAdapter.notifyDataSetChanged();
        }
    };
    private void callDeleteCardListAPI(String card_id, final int position) {

        ApiInterface apiService = ApiClient.getClient(SaveCardsActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.deleteCardList(Pref.getValue(SaveCardsActivity.this,Constants.PREF_TOKEN,""),card_id);
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
                            cardSelectionListModelArrayList.remove(position);
                            savedCardsListAdapter.notifyDataSetChanged();
                        }
                        else if(jsonObject.optString("code").equals("500")){
                            Utils.exitApplication(SaveCardsActivity.this);
                        }
                        else {
                            Toast.makeText(SaveCardsActivity.this, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
        // super.onBackPressed();
       /* if (Pref.getValue(mContext, "selectall", "").equals("0") || Pref.getValue(mContext, "selectall", "").equals("1")) {
            isUsercanEditOpen();
        } else {*/
            finish();
      //  }
    }


    /**
     * for check on back presss to view refresh and adapter refresh..
     */
    private void isUsercanEditOpen() {
        Pref.setValue(mContext, "selectall", "2");
        mBinding.tvEditCard.setText("Edit");
        mBinding.menuGreen.setVisibility(View.VISIBLE);
        mBinding.tvDelete.setVisibility(View.GONE);
        savedCardsListAdapter = new SavedCardsListAdapter(SaveCardsActivity.this, cardSelectionListModelArrayList, false, mBinding.tvEditCard, mBinding.tvDelete);
        savedCardsListAdapter.onClickEditCardDetailListener(onClickEditCardDetailListener);
        savedCardsListAdapter.onClickDeleteCardDetailListener(onClickDeleteCardDetailListener);
        mBinding.lvSaveCard.setAdapter(savedCardsListAdapter);
    }
}
