package com.puc.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.puc.R;
import com.puc.activity.CancelDeactiveCardsActivity;
import com.puc.databinding.RowCanceldeactiveCardListBinding;
import com.puc.interfaces.OnClickCalltoBankCardListener;
import com.puc.interfaces.OnClickCancelCardRequestDetailListener;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by aipxperts on 17/3/17.
 */

public class CancelDeactiveCardsListAdapter extends BaseAdapter {

    Context context;
    Activity ac;
    ArrayList<SaveCardListModel> payLoadList = new ArrayList<>();
    ArrayList<SaveCardListModel> tempList = new ArrayList<>();
    private OnClickCancelCardRequestDetailListener onClickCancelCardRequestDetailListener;
    private OnClickCalltoBankCardListener onClickCalltoBankCardListener;
    TextView tvDelete;
    TextView tvEditCard;
    int checkTrue = 0;
    int deselect = 0;
    boolean isCBVisible = false;
    Dialog challengeDialog;

    public void onClickCancelCardDetailListener(OnClickCancelCardRequestDetailListener onClickCancelCardRequestDetailListener) {
        this.onClickCancelCardRequestDetailListener = onClickCancelCardRequestDetailListener;
    }


    public void onClickCalltoBankCardListener(OnClickCalltoBankCardListener onClickCalltoBankCardListener) {
        this.onClickCalltoBankCardListener = onClickCalltoBankCardListener;
    }

        public CancelDeactiveCardsListAdapter(Activity activity, ArrayList<SaveCardListModel> payLoadList, Boolean isCBVisible, TextView tvEditCard, TextView tvDelete) {
            context = activity;
            this.payLoadList = payLoadList;
            this.tvEditCard = tvEditCard;
            this.tvDelete = tvDelete;
            this.isCBVisible = isCBVisible;
            tvDelete.setEnabled(true);
            if (deselect == 0) {
                displayCount(0);
            } else {
                displayCount(payLoadList.size());
            }
        }

    public CancelDeactiveCardsListAdapter(Activity activity, ArrayList<SaveCardListModel> cardSelectionListModelArrayList, Boolean isCBVisible, TextView tvEditCard, TextView tvDelete, int deselect) {
        context = activity;
        this.payLoadList = cardSelectionListModelArrayList;
        this.tvEditCard = tvEditCard;
        this.tvDelete = tvDelete;
        this.deselect = deselect;
        this.isCBVisible = isCBVisible;

        for (int i = 0; i < payLoadList.size(); i++) {
            if (deselect == 0) {
                payLoadList.get(i).setChecked(false);
            } else {
                payLoadList.get(i).setChecked(true);
            }
        }

        tvDelete.setEnabled(true);
        if (deselect == 0) {
            displayCount(0);
        } else {
            displayCount(payLoadList.size());
        }

    }


    @Override
    public int getCount() {
        return payLoadList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final RowCanceldeactiveCardListBinding mBinding;

        if (convertView == null) {

            mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_canceldeactive_card_list, parent, false);
            convertView = mBinding.getRoot();
            convertView.setTag(mBinding);
        } else {
            mBinding = (RowCanceldeactiveCardListBinding) convertView.getTag();
        }

        if (payLoadList.get(position).status.equals("Deactivated")) {
            mBinding.lnCardDetail.setBackground(context.getDrawable(R.mipmap.cancel_card_request_bg));
            mBinding.lnDeleteCardOption.setVisibility(View.VISIBLE);
            mBinding.lnCancleCardOption.setVisibility(View.GONE);
            mBinding.cbCardSelection.setEnabled(false);
        } else {
            mBinding.cbCardSelection.setEnabled(true);
            mBinding.lnDeleteCardOption.setVisibility(View.GONE);
            mBinding.lnCancleCardOption.setVisibility(View.VISIBLE);
            mBinding.lnCardDetail.setBackground(context.getDrawable(R.mipmap.card_option_main_bg));
        }

        if (payLoadList.get(position).type.equals("Visa")) {
            mBinding.imgCardType.setImageDrawable(context.getDrawable(R.mipmap.visa_logo_img));
        } else if (payLoadList.get(position).type.equals("MasterCard")) {
            mBinding.imgCardType.setImageDrawable(context.getDrawable(R.mipmap.mastercard_logo_img));
        } else if (payLoadList.get(position).type.equals("American_Express")) {
            mBinding.imgCardType.setImageDrawable(context.getDrawable(R.mipmap.americal_ex_logo_img));
        } else if (payLoadList.get(position).type.equals("Discover")) {
            mBinding.imgCardType.setImageDrawable(context.getDrawable(R.mipmap.discover_logo_img));
        } else if (payLoadList.get(position).type.equals("JCB")) {
            mBinding.imgCardType.setImageDrawable(context.getDrawable(R.mipmap.jcb_logo_img));
        } else if (payLoadList.get(position).type.equals("Diners_Club")) {
            mBinding.imgCardType.setImageDrawable(context.getDrawable(R.mipmap.dinner_club_logo_img));
        } else {
            mBinding.imgCardType.setImageDrawable(context.getDrawable(R.mipmap.default_credit_card_logo));
        }

        if (TextUtils.isEmpty(payLoadList.get(position).nick_name)) {
            mBinding.tvNickName.setText(Utils.capitalize(payLoadList.get(position).card_category) + " " + Utils.capitalize(payLoadList.get(position).card_type) + " Card");
        } else {
            mBinding.tvNickName.setText(Utils.capitalize(payLoadList.get(position).nick_name) + "'s " + Utils.capitalize(payLoadList.get(position).card_category) + " " + Utils.capitalize(payLoadList.get(position).card_type) + " Card");
        }

        mBinding.tvCardName.setText(payLoadList.get(position).card_name);
        mBinding.tvBankName.setText(payLoadList.get(position).bank_name);
        try {
            String card_number = Utils.decrypt(payLoadList.get(position).card_number);
            String final_card_number = card_number.substring(12);
            mBinding.tvCardNumber.setText("XXXX-XXXX-XXXX-" + final_card_number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (payLoadList.get(position).isChecked) {
            mBinding.cbCardSelection.setChecked(true);
        } else {
            mBinding.cbCardSelection.setChecked(false);
        }
        mBinding.swipe.setLeftSwipeEnabled(false);
        mBinding.swipe.setRightSwipeEnabled(false);


        //for full swipe code..
        mBinding.swipe.addDrag(SwipeLayout.DragEdge.Top, mBinding.starbott.findViewWithTag("Bottom3"));
        mBinding.swipe.addRevealListener(R.id.bottom_wrapper_child1, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(View child, SwipeLayout.DragEdge edge, float fraction, int distance) {
                //  warning(String.valueOf(position));
            }
        });

        mBinding.swipe.findViewById(R.id.tv_no_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.swipe.close();
            }
        });

        mBinding.swipe.findViewById(R.id.tv_yes_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  callCancelCardListAPI(context, payLoadList.get(position).id, position);
                //  Utils.showProgress(context);
            }
        });

        if (isCBVisible == true) {
            mBinding.cbCardSelection.setVisibility(View.VISIBLE);

        } else {
            mBinding.cbCardSelection.setVisibility(View.VISIBLE);
            // mBinding.swipe.addDrag(SwipeLayout.DragEdge.Left, mBinding.bottomWrapper);
            // mBinding.swipe.addDrag(SwipeLayout.DragEdge.Right, mBinding.bottomWrapper2);
        }


        //for check option menu open or not..
        if (payLoadList.get(position).isOpenOptionMenu) {
            mBinding.lnCardDetail.setVisibility(View.GONE);
            mBinding.lnCardDetailChangeOption.setVisibility(View.VISIBLE);
        } else {
            mBinding.lnCardDetail.setVisibility(View.VISIBLE);
            mBinding.lnCardDetailChangeOption.setVisibility(View.GONE);
        }

        mBinding.lnCancleCardOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();
                Log.e("Cancelcard", "### " + payLoadList.get(position).id);
                jsonArray.put(payLoadList.get(position).id);
                String card_number = Utils.decrypt(payLoadList.get(position).card_number);
                jsonArray1.put(card_number.substring(12));
                confirmationforChallengeGame(context, jsonArray, position, "single",jsonArray1);

            }
        });

        mBinding.lnCallCardOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickCalltoBankCardListener.onclickcalltobankcard(payLoadList.get(position).customer_service_number);

            }

        });

        mBinding.lnCloseOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.lnCardDetail.setVisibility(View.VISIBLE);
                mBinding.lnCardDetailChangeOption.setVisibility(View.GONE);
                payLoadList.get(position).setOpenOptionMenu(false);
            }
        });


        mBinding.lnDeleteCardOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(payLoadList.get(position).id);
                warningsingle(context, jsonArray.toString(), position);
            }
        });


        mBinding.cbCardSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payLoadList.get(position).isChecked) {
                    payLoadList.get(position).setChecked(false);

                    int count = 0;
                    for (int i = 0; i < payLoadList.size(); i++) {
                        if (payLoadList.get(i).isChecked) {
                            count++;
                        }
                    }
                    checkTrue = count;
                    displayCount(checkTrue);
                    notifyDataSetChanged();
                } else {
                    payLoadList.get(position).setChecked(true);
                    int count = 0;
                    for (int i = 0; i < payLoadList.size(); i++) {
                        if (payLoadList.get(i).isChecked) {
                            count++;
                        }
                    }
                    checkTrue = count;
                    displayCount(checkTrue);
                    notifyDataSetChanged();
                }
            }
        });


        return convertView;
    }


    public void displayCount(int checkTrue) {
        Log.e("Adapter", "checkTrue  " + checkTrue);
        if (checkTrue == 0) {
            // tvEditCard.setText("Cancel");
            Pref.setValue(context, "selectall", "0");
            tvDelete.setVisibility(View.VISIBLE);
            // tvDelete.setEnabled(false);
            tvDelete.setEnabled(true);
        } else {
            // tvEditCard.setText("Cancel");
            Pref.setValue(context, "selectall", "1");
            tvDelete.setVisibility(View.VISIBLE);
            tvDelete.setEnabled(true);
        }

        // tvDelete.setText("" + checkTrue + " Delete");


        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = false;
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();
                for (int j = 0; j < payLoadList.size(); j++) {
                    if (payLoadList.get(j).isChecked) {
                        isChecked = true;
                        Log.e("DeleteCard", "### " + payLoadList.get(j).id);
                        jsonArray.put(payLoadList.get(j).id);
                        String card_number = Utils.decrypt(payLoadList.get(j).card_number);
                        jsonArray1.put(card_number.substring(12));
                        Log.e("DeleteCard", "$$$$ " + jsonArray.toString());
                        // onClickDeleteCardDetailListener.onClickCancelSignalMessage(jsonArray, j);

                    }
                }
                if (!isChecked) {
                    Toast.makeText(context, "Please select one or multiple cards to cancel", Toast.LENGTH_SHORT).show();

                } else {

                    confirmationforChallengeGame(context, jsonArray, 0, "multiple",jsonArray1);
                }
            }
        });
    }

    /**
     * dialog for expired signal when user click on expire signal then popup
     */
    private void confirmationforChallengeGame(final Context context, final JSONArray card_id, final int position, final String typeSelection,final JSONArray last_four_digit) {

        challengeDialog = new Dialog(context, R.style.PauseDialog);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        challengeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        challengeDialog.setContentView(R.layout.confirmation_for_canceldeactive_card);
        challengeDialog.setCanceledOnTouchOutside(false);
        challengeDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        /*Window window = challengeDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        window.setAttributes(lp);*/
        lp.copyFrom(challengeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        challengeDialog.show();
        challengeDialog.getWindow().setAttributes(lp);

        challengeDialog.getWindow().setGravity(Gravity.CENTER);
        challengeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Button mOkbtn = (Button) challengeDialog.findViewById(R.id.btn_proceed);
        Button mCancelGameBtn = (Button) challengeDialog.findViewById(R.id.btn_cancel_state);
        TextView mChallengeDesc = (TextView) challengeDialog.findViewById(R.id.tv_challenge_desc);
        final TextInputLayout pwdTextInputLayout = (TextInputLayout) challengeDialog.findViewById(R.id.tl_pwd_input);
        final EditText pwdEdt = (EditText) challengeDialog.findViewById(R.id.edt_pwd_input);

        if (typeSelection.equals("single")) {
            mChallengeDesc.setText("Enter account password to complete the cancellation process.");
        } else {
            mChallengeDesc.setText("Enter account password to complete the cancellation process of selected cards.");
        }

        mCancelGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengeDialog.dismiss();
            }
        });
        mOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Utils.showToast(context,"Work in progress. " +card_id.toString());
                if (!TextUtils.isEmpty(pwdTextInputLayout.getEditText().getText().toString().trim())) {
                    callpwdVerficationListAPI(context, card_id, position, pwdEdt.getText().toString().trim(),last_four_digit);

                } else {
                    pwdTextInputLayout.setError(context.getString(R.string.Please_provide_password));
                }


            }
        });
        challengeDialog.show();
    }


    private void callpwdVerficationListAPI(final Context mContext, final JSONArray card_id, final int position, String pwd,final JSONArray last_four_digit) {

        ApiInterface apiService = ApiClient.getClient(((FragmentActivity) mContext)).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.checkpassword(Pref.getValue(mContext, Constants.PREF_TOKEN, ""), pwd);
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
                            challengeDialog.dismiss();
                            // onClickCancelCardRequestDetailListener.onClickCancelCardListener(card_id, position);
                            String card_id1 = card_id.toString();

                            try {
                                String final_card_number = last_four_digit.toString();
                                callcancelCardListAPI(mContext, card_id1, position,final_card_number,final_card_number);
                                Utils.showProgress(mContext);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (jsonObject.optString("code").equals("500")) {
                            Utils.exitApplication(mContext);
                        } else {
                            Toast.makeText(mContext, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }

                        Log.e("DeleteAdapter", "cancel " + jsonObject.toString());

                    } else {
                        Log.e("DeleteAdapter", "error " + response.errorBody().toString());
                        // Log.e(TAG,"Else " + response.message() + " code " +response.code());
                        String errorValue = response.errorBody().string();

                        JSONObject errorJsonObject = new JSONObject(errorValue);
                        Utils.showToast(((FragmentActivity) mContext), errorJsonObject.optString("msg"));
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


    public void warningsingle(final Context context, final String card_id, final int position) {


        challengeDialog = new Dialog(context, R.style.PauseDialog);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        challengeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        challengeDialog.setContentView(R.layout.confirmation_for_delete_card);
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
        Button mCancelGameBtn = (Button) challengeDialog.findViewById(R.id.btn_cancel_state);
        TextView mOkTv = (TextView) challengeDialog.findViewById(R.id.tv_proceed);
        TextView mDescTv = (TextView) challengeDialog.findViewById(R.id.tv_challenge_desc);

        mDescTv.setText("Are you sure you want to delete this card?");


        mCancelGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengeDialog.dismiss();
            }
        });
        mOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDeleteCardListAPI(context, card_id, position);
                Utils.showProgress(context);
                challengeDialog.dismiss();
            }
        });
        challengeDialog.show();


    }


    private void callDeleteCardListAPI(final Context mContext, String card_id, final int position) {

        ApiInterface apiService = ApiClient.getClient(((FragmentActivity) mContext)).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.deleteCardList(Pref.getValue(mContext, Constants.PREF_TOKEN, ""), card_id);
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
                            notifyDataSetChanged();
                            mContext.startActivity(new Intent(mContext, CancelDeactiveCardsActivity.class));
                            ((FragmentActivity) mContext).overridePendingTransition(0, 0);
                            ((FragmentActivity) mContext).finish();

                        } else if (jsonObject.optString("code").equals("500")) {
                            Utils.exitApplication(mContext);
                        } else {
                            Toast.makeText(mContext, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }

                        Log.e("DeleteAdapter", "cancel " + jsonObject.toString());

                    } else {
                        Log.e("DeleteAdapter", "error " + response.errorBody().toString());
                        // Log.e(TAG,"Else " + response.message() + " code " +response.code());
                        String errorValue = response.errorBody().string();

                        JSONObject errorJsonObject = new JSONObject(errorValue);
                        Utils.showToast(((FragmentActivity) mContext), errorJsonObject.optString("msg"));
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

    private void callcancelCardListAPI(final Context mContext, String card_id, final int position,String four_digit,String last_four_digit) {

        ApiInterface apiService = ApiClient.getClient(((FragmentActivity) mContext)).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.cancelcardDeactive(Pref.getValue(mContext, Constants.PREF_TOKEN, ""), card_id,four_digit);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("CanCleCard", "response " + response.toString());
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.optString("code").equals("200")) {
                            mContext.startActivity(new Intent(mContext, CancelDeactiveCardsActivity.class));
                            ((FragmentActivity) mContext).overridePendingTransition(0, 0);
                            ((FragmentActivity) mContext).finish();
                        } else if (jsonObject.optString("code").equals("500")) {
                            Utils.exitApplication(mContext);
                        } else {
                            Toast.makeText(mContext, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        }

                        Log.e("CanCleCard", "cancel " + jsonObject.toString());

                    } else {
                        Log.e("CanCleCard", "error " + response.errorBody().toString());
                        // Log.e(TAG,"Else " + response.message() + " code " +response.code());
                        String errorValue = response.errorBody().string();

                        JSONObject errorJsonObject = new JSONObject(errorValue);
                        Utils.showToast(((FragmentActivity) mContext), errorJsonObject.optString("msg"));
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