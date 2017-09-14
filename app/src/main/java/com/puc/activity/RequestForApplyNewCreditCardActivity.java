package com.puc.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.puc.R;
import com.puc.databinding.ActivityRequestForApplyNewCreditCardBinding;
import com.puc.databinding.ActivitySignUpBinding;
import com.puc.model.BankListdataModel;
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
import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Header;

public class RequestForApplyNewCreditCardActivity extends BaseActivity {

    boolean isValidation = true;
    private boolean is18;

    // calendar

    private String TAG="RequestForApplyNewCreditCardActivity";
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    ActivityRequestForApplyNewCreditCardBinding mBinding;
    ArrayList<BankListdataModel> bankListdataModelArrayList=new ArrayList<>();
    int bank_Id;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mBinding= DataBindingUtil.setContentView(this,R.layout.activity_request_for_apply_new_credit_card);
        mContext = RequestForApplyNewCreditCardActivity.this;
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);

        setup();

    }



    private void setup() {

       // setbankList();

        callBankListAPI();
        Utils.showProgress(mContext);
        mBinding.imgBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textInputLayouBlank();




        mBinding.txtSendVarification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isValidation = true;

                if (mBinding.spBankList.getSelectedItemPosition() == 0) {
                    isValidation = false;
                    mBinding.tlBanknameInput.setError(getString(R.string.Please_select_Bank));
                } else {
                    mBinding.tlBanknameInput.setError(null);
                }

                if (TextUtils.isEmpty(mBinding.tlFirstnameInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlFirstnameInput.setError(getString(R.string.Please_enter_firstname));
                }

                if (TextUtils.isEmpty(mBinding.tlMiddlenameInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlMiddlenameInput.setError(getString(R.string.Please_enter_middle_name));
                }

                if (TextUtils.isEmpty(mBinding.tlLastnameInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlLastnameInput.setError("Please enter lastname.");
                }

                if (TextUtils.isEmpty(mBinding.tlBirthdateInput.getEditText().getText().toString())) {
                    isValidation = false;
                    mBinding.tlBirthdateInput.setError(getString(R.string.please_provide_date_of_birth));
                } else {
                    if (!is18) {
                        isValidation = false;
                        mBinding.tlBirthdateInput.setError(getString(R.string.age_must_be_18_year));
                    } else {
                        mBinding.tlBirthdateInput.setError(null);
                    }
                }

                if (TextUtils.isEmpty(mBinding.tlSocialSecurityNumberInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlSocialSecurityNumberInput.setError(getString(R.string.please_enter_username));
                }

                if (TextUtils.isEmpty(mBinding.tlResidentialAddressInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlResidentialAddressInput.setError(getString(R.string.please_enter_username));
                }


                if (TextUtils.isEmpty(mBinding.tlZipcodeInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlZipcodeInput.setError(getString(R.string.please_enter_username));
                }

                if (TextUtils.isEmpty(mBinding.tlStateInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlStateInput.setError(getString(R.string.please_enter_username));
                }



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

                String finalBirthDate=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

                if (isValidation) {
                    Log.e("yyy", "Validation");
                    callRequestForApplyNewCardInfoPI(bank_Id,mBinding.edtFirstnameInput.getText().toString(),mBinding.edtMiddlenameInput.getText().toString(),mBinding.edtLastnameInput.getText().toString(),finalBirthDate,mBinding.edtSocialSecurityNumberInput.getText().toString(),mBinding.edtResidentialAddressInput.getText().toString(),mBinding.edtZipcodeInput.getText().toString(),mBinding.edtEmailInput.getText().toString(),mBinding.edtPhoneInput.getText().toString(),mBinding.edtStateInput.getText().toString());
                    Utils.showProgress(mContext);
                }


            }
        });

        mBinding.edtBirthdateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog();
            }
        });


    }

    public void DateDialog() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mBinding.edtBirthdateInput.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

                Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();

                minAdultAge.add(Calendar.YEAR, -18);

                if (minAdultAge.before(userAge)) {
                    is18 = false;
                } else {
                    is18 = true;
                }


            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        dpDialog.show();
    }



    private void callBankListAPI() {

        ApiInterface apiService = ApiClient.getClient(RequestForApplyNewCreditCardActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.bankList(Pref.getValue(mContext, Constants.PREF_TOKEN, ""));
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
                            JSONArray payloadJsonArray = jsonObject.getJSONArray("payload");
                            BankListdataModel[] bankListdataModels = new BankListdataModel[payloadJsonArray.length()];

                            for (int i = 0; i < payloadJsonArray.length(); i++) {
                                JSONObject payloadJSONObject = payloadJsonArray.getJSONObject(i);

                                bankListdataModels[i] = new BankListdataModel();
                                bankListdataModels[i].setId(payloadJSONObject.optString("id"));
                                bankListdataModels[i].setName(payloadJSONObject.optString("name"));


                                bankListdataModelArrayList.add(bankListdataModels[i]);
                                setbankList();
                            }
                        }
                        else if(jsonObject.optString("code").equals("500")){
                            Utils.exitApplication(RequestForApplyNewCreditCardActivity.this);
                        }
                        else {
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

    private void callRequestForApplyNewCardInfoPI(int bank_Id, String firstname, String middlename, String lastname, String finalBirthDate, String social_security_number, String address, String zipcode, String email, String home_phone_number, String state) {

        ApiInterface apiService = ApiClient.getClient(RequestForApplyNewCreditCardActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.requestForApplyNewCard(Pref.getValue(mContext, Constants.PREF_TOKEN,""), String.valueOf(bank_Id), firstname, middlename,lastname,finalBirthDate,social_security_number,address,zipcode,email,home_phone_number,state);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, "callAddCardInfoPI " + response.toString());
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.optString("code").equals("200")) {
                                Utils.showToast(RequestForApplyNewCreditCardActivity.this,jsonObject.optString("message"));
                            finish();
                        }
                        else if(jsonObject.optString("code").equals("500")){
                            Utils.exitApplication(RequestForApplyNewCreditCardActivity.this);
                        }
                        else {
                            Toast.makeText(RequestForApplyNewCreditCardActivity.this, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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

    private void setbankList() {

        mBinding.edtBanknameInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBinding.spBankList.performClick();
            }
        });

        final ArrayList<String> bankList = new ArrayList<>();

        bankList.add("Select Bank");
        for (int i = 0; i < bankListdataModelArrayList.size(); i++) {
            bankList.add(bankListdataModelArrayList.get(i).getName());
        }


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bankList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spBankList.setAdapter(aa);

        mBinding.spBankList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mBinding.edtBanknameInput.setText(bankList.get(position).toString());
                for(int i=1;i<bankList.size();i++){
                    if(i==position) {
                        Log.e("BankList", "ID " + bankListdataModelArrayList.get(position-1).getId());
                        bank_Id= Integer.parseInt(bankListdataModelArrayList.get(position-1).getId());
                    }
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void textInputLayouBlank() {
        mBinding.edtFirstnameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlFirstnameInput.setError(null);
            }
        });

        mBinding.edtMiddlenameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlMiddlenameInput.setError(null);
            }
        });

        mBinding.edtLastnameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlLastnameInput.setError(null);
            }
        });

        mBinding.edtBirthdateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlBirthdateInput.setError(null);
            }
        });


        mBinding.edtResidentialAddressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlResidentialAddressInput.setError(null);
            }
        });

        mBinding.edtBanknameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlBanknameInput.setError(null);
            }
        });

        mBinding.edtZipcodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlZipcodeInput.setError(null);
            }
        });

        mBinding.edtStateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlStateInput.setError(null);
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

        mBinding.edtSocialSecurityNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlSocialSecurityNumberInput.setError(null);
            }
        });


    }

}
