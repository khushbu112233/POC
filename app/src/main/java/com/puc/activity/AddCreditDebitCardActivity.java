package com.puc.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.puc.R;
import com.puc.databinding.ActivityAddcreditdebitCardBinding;
import com.puc.model.BankListdataModel;
import com.puc.network.ApiClient;
import com.puc.network.ApiInterface;
import com.puc.textWatcher.CardTextInputLayout;
import com.puc.textWatcher.ExpiringDateTextWatcher;
import com.puc.util.Constants;
import com.puc.util.InputFilterMinMax;
import com.puc.util.Pref;
import com.puc.util.UsPhoneNumberFormatter;
import com.puc.util.Utils;
import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import morxander.editcard.CardPattern;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCreditDebitCardActivity extends BaseActivity {

    ActivityAddcreditdebitCardBinding mBinding;
    private CardTextInputLayout expiryInputLayout;
    boolean isValidation = true;
    private int expMonth, expYear;
    private String cardPan, cardName, cvv, expDate, pin, otp;
    private String TAG = "AddCreditDebitCard";
    private Context mContext;
    ArrayList<BankListdataModel> bankListdataModelArrayList = new ArrayList<>();
    int bank_Id;
    String encryptSCardNumber;
    String cardType;
    String combineCardNumber = "";
    String combineExpiryDate = "";
    private Calendar cal;
    private int year, year1, year2, month, month1, selected_month, selected_year;
    String iscardType = "Debit";
    String iscardCategory = "Personal";

    ArrayList<String> listOfPattern = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_addcreditdebit_card);
        mContext = AddCreditDebitCardActivity.this;
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        setUp();

        //  prepareView();
    }

    public void radioCardType(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // This check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb_Personal_card:

                if (checked)
                    //Do something when radio button is clicked
                    iscardCategory = mBinding.rbPersonalCard.getText().toString();
                if (bank_Id > 0 && !iscardType.equalsIgnoreCase("")) {
                    for (int i = 0; i < bankListdataModelArrayList.size(); i++) {
                        if (bank_Id == Integer.parseInt(bankListdataModelArrayList.get(i).getId())) {
                            if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Debit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getPersonal_debit());
                            } else if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Credit")) {

                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getPersonal_credit());

                            } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Debit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getBusiness_debit());

                            } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Credit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getBusiness_credit());

                            }
                        }
                    }
                }
                break;

            case R.id.rb_business_card:
                //Do something when radio button is clicked
                iscardCategory = mBinding.rbBusinessCard.getText().toString();
                if (bank_Id > 0 && !iscardType.equalsIgnoreCase("")) {
                    for (int i = 0; i < bankListdataModelArrayList.size(); i++) {
                        if (bank_Id == Integer.parseInt(bankListdataModelArrayList.get(i).getId())) {
                            if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Debit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getPersonal_debit());
                            } else if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Credit")) {

                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getPersonal_credit());

                            } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Debit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getBusiness_debit());

                            } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Credit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getBusiness_credit());

                            }
                        }
                    }
                }
                break;


        }
    }

    public void radioCardCategory(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // This check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb_debit_card:
                if (checked)
                    //Do something when radio button is clicked

                    iscardType = mBinding.rbDebitCard.getText().toString();
                if (bank_Id > 0 && !iscardCategory.equalsIgnoreCase("")) {
                    for (int i = 0; i < bankListdataModelArrayList.size(); i++) {
                        if (bank_Id == Integer.parseInt(bankListdataModelArrayList.get(i).getId())) {
                            if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Debit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getPersonal_debit());
                            } else if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Credit")) {

                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getPersonal_credit());

                            } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Debit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getBusiness_debit());

                            } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Credit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getBusiness_credit());

                            }
                        }
                    }
                }
                break;

            case R.id.rb_credit_card:
                //Do something when radio button is clicked

                iscardType = mBinding.rbCreditCard.getText().toString();
                if (bank_Id > 0 && !iscardCategory.equalsIgnoreCase("")) {
                    for (int i = 0; i < bankListdataModelArrayList.size(); i++) {
                        if (bank_Id == Integer.parseInt(bankListdataModelArrayList.get(i).getId())) {
                            if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Debit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getPersonal_debit());
                            } else if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Credit")) {

                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getPersonal_credit());

                            } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Debit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getBusiness_debit());

                            } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Credit")) {
                                mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(i).getBusiness_credit());

                            }
                        }
                    }
                }
                break;


        }
    }

    public void openCalander() {
        hideKeyboard();
        RackMonthPicker rackMonthPicker = new RackMonthPicker(AddCreditDebitCardActivity.this)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        Log.e("selected", "" + month + "" + year);
                        if (month > 9) {
                            mBinding.edtExpiryMonth.setText(month + "");
                        } else {
                            mBinding.edtExpiryMonth.setText("0" + month + "");
                        }
                        mBinding.edtExpiryYear.setText(String.valueOf(year).substring(2));
                        openKeyboard();
                    }
                }).setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.dismiss();
                        openKeyboard();
                    }
                });


        rackMonthPicker.show();
    }

    private void setUp() {

        mBinding.edtFourStepNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    openCalander();
                }
                return false;
            }
        });

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{14}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);

        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(mBinding.edtPhoneNoBankcardInput));
        mBinding.edtPhoneNoBankcardInput.addTextChangedListener(addLineNumberFormatter);
        // mBinding.edtPhoneNoBankcardInput.addTextChangedListener(new UsPhoneNumberFormatter());

        mBinding.edtExpiryMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RackMonthPicker rackMonthPicker = new RackMonthPicker(AddCreditDebitCardActivity.this)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                Log.e("selected", "" + month + "" + year);
                                if (month > 9) {
                                    mBinding.edtExpiryMonth.setText(month + "");
                                } else {
                                    mBinding.edtExpiryMonth.setText("0" + month + "");
                                }
                                mBinding.edtExpiryYear.setText(String.valueOf(year).substring(2));
                                openCalander();

                            }
                        }).setNegativeButton(new OnCancelMonthDialogListener() {
                            @Override
                            public void onCancel(AlertDialog dialog) {
                                dialog.dismiss();
                                openCalander();
                            }
                        });


                rackMonthPicker.show();

                //  mBinding.spMonthList.performClick();
            }
        });
        mBinding.edtExpiryYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RackMonthPicker rackMonthPicker = new RackMonthPicker(AddCreditDebitCardActivity.this)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                Log.e("selected", "" + month + "" + year);
                                if (month > 9) {
                                    mBinding.edtExpiryMonth.setText(month + "");
                                } else {
                                    mBinding.edtExpiryMonth.setText("0" + month + "");
                                }
                                mBinding.edtExpiryYear.setText(String.valueOf(year).substring(2));
                                openCalander();

                            }
                        }).setNegativeButton(new OnCancelMonthDialogListener() {
                            @Override
                            public void onCancel(AlertDialog dialog) {
                                dialog.dismiss();
                                openCalander();

                            }
                        });


                rackMonthPicker.show();
            }
        });
      /*  final ArrayList<String> month_List = new ArrayList<>();

        month_List.add("01");
        month_List.add("02");
        month_List.add("03");
        month_List.add("04");
        month_List.add("05");
        month_List.add("06");
        month_List.add("07");
        month_List.add("08");
        month_List.add("09");
        month_List.add("10");
        month_List.add("11");
        month_List.add("12");


        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, month_List);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spMonthList.setAdapter(aa2);

        mBinding.spMonthList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mBinding.edtExpiryMonth.setText(month_List.get(position).toString());
                mBinding.tvExpiryValidation.setError(null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayList<String> year_List = new ArrayList<>();

        int current_year = Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2, 4));
        for (int i = current_year; i <= 60; i++) {
            year_List.add(String.valueOf(i));
        }


        ArrayAdapter aaY = new ArrayAdapter(this, android.R.layout.simple_spinner_item, year_List);
        aaY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spYearList.setAdapter(aaY);

        mBinding.spYearList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mBinding.edtExpiryYear.setText(year_List.get(position).toString());
                mBinding.tvExpiryValidation.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        callBankListAPI();
        callGetLatestCardDetailAPI();
        Utils.showProgress(mContext);

        textInputLayouBlank();
        cardValidationGetType();
        mBinding.edtExpiryMonth.setFilters(new InputFilter[]{new InputFilterMinMax("0", "12")});
        expiryInputLayout = (CardTextInputLayout) findViewById(R.id.ctil_expiry_input);
        mBinding.edtCardNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  if(mBinding.edtCardNumber.isValid()==false){
                //     mBinding.edtCardNumber.setError(null);
                //  }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        expiryInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    expiryInputLayout.setHint("Exp. Date");
                } else {
                    if (expiryInputLayout.getEditText().getText().toString().isEmpty())
                        expiryInputLayout.setHint("MM/YY");
                    else {
                        if (ExpiringDateTextWatcher.isValidExpiringDate(expiryInputLayout.getEditText().getText().toString())) {
                            expiryInputLayout.setError("");
                        } else {
                            expiryInputLayout.setError("Enter a valid expiration date");
                        }
                        expiryInputLayout.setHint("Exp. Date");
                    }
                }
            }
        });

        expiryInputLayout.post(new Runnable() {
            @Override
            public void run() {

                expiryInputLayout.getEditText().addTextChangedListener(new ExpiringDateTextWatcher(expiryInputLayout) {
                    @Override
                    protected void onValidated(boolean moveToNext, String expDateValue, int expMonthValue, int expYearValue) {
                        expDate = expDateValue;
                        expMonth = expMonthValue;
                        expYear = expYearValue;
                    }
                });
            }

        });

        mBinding.imgBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mBinding.btnSubmitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValidation = true;
                //encryptSCardNumber=Utils.encrypt(mBinding.edtCardNumber.getText().toString());

               /* byte[] encry = new byte[0];
                String decry;
                try {
                    SecretKeySpec secretKeySpec = Utils.generateKey();
                    encry = Utils.encryptMsg(mBinding.edtCardNumber.getText().toString(), secretKeySpec);
                    decry = Utils.decryptMsg(encry, secretKeySpec);
                    Log.e("EncyptDecrypt", "#$$ " + Base64.encodeToString(encry, Base64.DEFAULT));
                    Log.e("EncyptDecrypt", "#@@@@  " + new String(decry));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InvalidParameterSpecException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }*/

                combineCardNumber = mBinding.edtFirstStepNumber.getText().toString().trim() + "" + mBinding.edtSecondStepNumber.getText().toString().trim() + "" + mBinding.edtThirdStepNumber.getText().toString().trim() + "" + mBinding.edtFourStepNumber.getText().toString().trim();
                combineExpiryDate = mBinding.edtExpiryMonth.getText().toString().trim() + "" + mBinding.edtExpiryYear.getText().toString().trim();
                encryptSCardNumber = Utils.encrypt(combineCardNumber);

                if (!TextUtils.isEmpty(combineExpiryDate)) {
                    String finalYear = String.valueOf(year).substring(2);
                    year1 = Integer.parseInt(mBinding.edtExpiryYear.getText().toString().trim());
                    month1 = Integer.parseInt(mBinding.edtExpiryMonth.getText().toString().trim());
                    year2 = Integer.parseInt(finalYear);
                    Log.e("Year", "$$$ " + finalYear + " finalCurrentMonth " + month);
                }

                if (mBinding.spCardCategoryList.getSelectedItemPosition() == 0) {
//                    isValidation = false;
                    mBinding.tlCardCategoryInput.setError(getString(R.string.Please_select_card_category));
                } else {
                    mBinding.tlCardCategoryInput.setError(null);
                }

                if (mBinding.spCardTypeList.getSelectedItemPosition() == 0) {
                    // isValidation = false;
                    mBinding.tlCardTypeInput.setError(getString(R.string.Please_select_card_type));
                } else {
                    mBinding.tlCardTypeInput.setError(null);
                }

                if (mBinding.spBankList.getSelectedItemPosition() == 0) {
                    isValidation = false;
                    mBinding.tlBanknameInput.setError(getString(R.string.Please_select_Bank));
                } else {
                    mBinding.tlBanknameInput.setError(null);
                }

                if (TextUtils.isEmpty(combineCardNumber)) {
                    isValidation = false;
                    mBinding.lnCardNumberValidation.setVisibility(View.VISIBLE);
                    mBinding.tvCardNumberValidation.setText("Please provide card number.");
                } else if (combineCardNumber.length() < 15 || cardType.equals("UNKNOWN")) {
                    isValidation = false;
                    mBinding.lnCardNumberValidation.setVisibility(View.VISIBLE);
                    mBinding.tvCardNumberValidation.setText("Please provide valid card number.");
                } else {
                    mBinding.lnCardNumberValidation.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(combineExpiryDate)) {
                    isValidation = false;
                    mBinding.lnExpiryValidation.setVisibility(View.VISIBLE);
                    mBinding.tvExpiryValidation.setText("Please provide expiry date.");
                } else if (year2 >= year1) {
                    Log.e("year2", "" + year2);
                    Log.e("year1", "" + year1);
                    Log.e("month1", "" + month1);
                    if (year2 >= year1) {
                        if (month1 < month) {
                            Log.e("test", "" + month);
                            isValidation = false;
                            mBinding.lnExpiryValidation.setVisibility(View.VISIBLE);
                            mBinding.tvExpiryValidation.setText("Please provide valid expiry date.");
                        } else if (TextUtils.isEmpty(mBinding.edtCcvCode.getText().toString().trim()) || mBinding.edtCcvCode.getText().toString().trim().length() < 3) {
                            isValidation = false;
                            mBinding.lnExpiryValidation.setVisibility(View.VISIBLE);
                            mBinding.tvExpiryValidation.setText("Please provide CVV number.");
                        } else {
                            mBinding.lnExpiryValidation.setVisibility(View.GONE);
                        }
                    } else if (TextUtils.isEmpty(mBinding.edtCcvCode.getText().toString().trim()) || mBinding.edtCcvCode.getText().toString().trim().length() < 3) {
                        isValidation = false;
                        mBinding.lnExpiryValidation.setVisibility(View.VISIBLE);
                        mBinding.tvExpiryValidation.setText("Please provide CVV number.");
                    } else {
                        mBinding.lnExpiryValidation.setVisibility(View.GONE);
                    }
                } else if (TextUtils.isEmpty(mBinding.edtCcvCode.getText().toString().trim()) || mBinding.edtCcvCode.getText().toString().trim().length() < 3) {
                    isValidation = false;
                    mBinding.lnExpiryValidation.setVisibility(View.VISIBLE);
                    mBinding.tvExpiryValidation.setText("Please provide CVV number.");
                } else {
                    mBinding.lnExpiryValidation.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(mBinding.tlCardHolderNameInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlCardHolderNameInput.setError("Please provide card holder name.");
                }


                if (TextUtils.isEmpty(mBinding.tlSecurityCodeInput.getEditText().getText().toString().trim())) {
                    // isValidation = false;
                    mBinding.tlSecurityCodeInput.setError(getString(R.string.Please_provide_security_code));
                }

                if (TextUtils.isEmpty(mBinding.tlPhoneNoBankcardInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlPhoneNoBankcardInput.setError(getString(R.string.please_provide_phone_no));
                }

                if (mBinding.tlPhoneNoBankcardInput.getEditText().getText().toString().length() >= 1 && mBinding.tlPhoneNoBankcardInput.getEditText().getText().toString().length() < 14) {
                    isValidation = false;
                    mBinding.tlPhoneNoBankcardInput.setError(getString(R.string.please_enter_valid_phone_number));
                }

                if (TextUtils.isEmpty(mBinding.tlEmailInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlEmailInput.setError(getString(R.string.please_provide_email_address));
                }

                if (mBinding.tlEmailInput.getEditText().getText().toString().length() >= 1 && !Utils.isValidEmail(mBinding.edtEmailInput.getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlEmailInput.setError(getString(R.string.please_enter_valid_email_Address));
                }

                if (TextUtils.isEmpty(mBinding.tlStreetAddress1Input.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlStreetAddress1Input.setError(getString(R.string.Please_provide_street_address1));
                }
                if (TextUtils.isEmpty(mBinding.tlStreetAddress2Input.getEditText().getText().toString().trim())) {
                    //  isValidation = false;
                    mBinding.tlStreetAddress2Input.setError(getString(R.string.Please_provide_street_address2));
                }
                if (TextUtils.isEmpty(mBinding.tlCityInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlCityInput.setError(getString(R.string.Please_provide_city));
                }

                if (TextUtils.isEmpty(mBinding.tlStateInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlStateInput.setError(getString(R.string.Please_provide_state));
                }

                if (TextUtils.isEmpty(mBinding.tlZipCodeInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlZipCodeInput.setError(getString(R.string.Please_provide_zipCode));
                }
                if (mBinding.tlZipCodeInput.getEditText().getText().toString().trim().length() > 0 && mBinding.tlZipCodeInput.getEditText().getText().toString().trim().length() > 6) {


                    isValidation = false;
                    mBinding.tlZipCodeInput.setError(getString(R.string.Please_provide_valid_zipCode));

                    /*  String regex = "^[0-9]{5}(?:-[0-9]{4})?$";

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(mBinding.tlZipCodeInput.getEditText().getText().toString().trim());
                    if(!matcher.matches())
                    {
                        isValidation=false;
                        mBinding.tlZipCodeInput.setError(getString(R.string.Please_provide_valid_zipCode));
                    }*/

                }
               /* if (TextUtils.isEmpty(mBinding.edtCardName.getText().toString())) {
                    isValidation = false;
                    mBinding.edtCardName.setError(getString(R.string.Please_provice_card_name));
                }
                if (TextUtils.isEmpty(mBinding.edtResidentialAddress.getText().toString())) {
                    isValidation = false;
                    mBinding.edtResidentialAddress.setError(getString(R.string.Please_provide_residential_address));
                }
                if (TextUtils.isEmpty(mBinding.edtCustomerServiceNo.getText().toString())) {
                    isValidation = false;
                    mBinding.edtCustomerServiceNo.setError(getString(R.string.Please_provide_customer_service_no));
                }*/


                if (isValidation) {
                    Log.e("yyy", "Done");
                    callAddCardInfoPI(bank_Id, encryptSCardNumber, mBinding.edtExpiryMonth.getText().toString().trim() + "/" + mBinding.edtExpiryYear.getText().toString().trim(), mBinding.edtCardHolderNameInput.getText().toString(), mBinding.edtNickNameInput.getText().toString(), mBinding.edtStreetAddress1Input.getText().toString(), mBinding.edtStreetAddress2Input.getText().toString(), mBinding.edtCityInput.getText().toString(), mBinding.edtStateInput.getText().toString(), mBinding.edtZipCodeInput.getText().toString(), iscardCategory.toLowerCase(), iscardType.toLowerCase()
                            , mBinding.edtPhoneNoBankcardInput.getText().toString(), cardType, mBinding.edtCcvCode.getText().toString(), mBinding.edtEmailInput.getText().toString(), mBinding.edtSecurityCodeInput.getText().toString());
                    Utils.showProgress(mContext);
                }
            }
        });
    }

    private void cardValidationGetType() {


        //for enter card number...............
        mBinding.edtFirstStepNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s1, int start, int before, int count) {
                // TODO Auto-generated method stub
                String cardno = "^(5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)";
                mBinding.edtCcvCode.setText("");
                mBinding.edtCcvCode.setHint("CCV");
                mBinding.edtCcvCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                String s = mBinding.edtFirstStepNumber.getText().toString().trim();
                if (s.startsWith("4") || s.matches(CardPattern.VISA)) {
                    mBinding.imgCardTypeLogo.setImageDrawable(getDrawable(R.mipmap.visa_logo_img));
                    cardType = "Visa";
                } else if (s.matches(cardno)) {
                    mBinding.imgCardTypeLogo.setImageDrawable(getDrawable(R.mipmap.mastercard_logo_img));
                    cardType = "MasterCard";
                    Log.e("test", "" + s.toString());
                } else if (s.matches(CardPattern.AMERICAN_EXPRESS)) {
                    mBinding.imgCardTypeLogo.setImageDrawable(getDrawable(R.mipmap.americal_ex_logo_img));
                    cardType = "American_Express";
                    mBinding.edtCcvCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                } else if (s.matches(CardPattern.DISCOVER_SHORT) || s.matches(CardPattern.DISCOVER)) {
                    mBinding.imgCardTypeLogo.setImageDrawable(getDrawable(R.mipmap.discover_logo_img));
                    cardType = "Discover";
                } else if (s.matches(CardPattern.JCB_SHORT) || s.matches(CardPattern.JCB)) {
                    mBinding.imgCardTypeLogo.setImageDrawable(getDrawable(R.mipmap.jcb_logo_img));
                    cardType = "JCB";
                } else if (s.matches(CardPattern.DINERS_CLUB_SHORT) || s.matches(CardPattern.DINERS_CLUB)) {
                    mBinding.imgCardTypeLogo.setImageDrawable(getDrawable(R.mipmap.dinner_club_logo_img));
                    cardType = "Diners_Club";
                } else {
                    mBinding.imgCardTypeLogo.setImageDrawable(getDrawable(R.mipmap.default_credit_card_logo));
                    cardType = "UNKNOWN";
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s1) {
                if (mBinding.edtFirstStepNumber.getText().toString().trim().length() == 4) {


                    mBinding.edtFirstStepNumber.clearFocus();
                    mBinding.edtSecondStepNumber.requestFocus();
                }
            }
        });


        mBinding.edtSecondStepNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                Log.e("CardType", "$$$ " + cardType);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtSecondStepNumber.getText().toString().trim().length() == 4) {

                    mBinding.edtSecondStepNumber.clearFocus();
                    mBinding.edtThirdStepNumber.requestFocus();
                }
            }
        });

        mBinding.edtThirdStepNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtThirdStepNumber.getText().toString().trim().length() == 4) {

                    mBinding.edtThirdStepNumber.clearFocus();
                    mBinding.edtFourStepNumber.requestFocus();

                }

            }
        });

        mBinding.edtFourStepNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtFourStepNumber.getText().toString().trim().length() == 4) {

                    mBinding.edtFourStepNumber.clearFocus();
                    mBinding.edtExpiryMonth.requestFocus();
                    openCalander();
                    hideKeyboard();

                }

            }
        });

        //for expriy month and year..
        mBinding.edtExpiryMonth.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = String.valueOf(s);
                if (count == 1 && s1.equals("0")) {

                } else {


                }

                Log.e("MonthValue", "%%% " + s1 + " start " + start + " before " + before + " count " + count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtExpiryMonth.getText().toString().trim().length() == 2) {

                    mBinding.edtExpiryMonth.clearFocus();
                    mBinding.edtExpiryYear.requestFocus();

                }

            }
        });

        mBinding.edtExpiryYear.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.edtExpiryYear.getText().toString().trim().length() == 2) {

                    mBinding.edtExpiryYear.clearFocus();
                    mBinding.edtCcvCode.requestFocus();

                }

            }
        });


        mBinding.edtExpiryYear.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && AddCreditDebitCardActivity.this.mBinding.edtExpiryYear.getText().length() == 0) {
                    mBinding.edtExpiryMonth.requestFocus();
                }

                return false;
            }
        });


        mBinding.edtSecondStepNumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && AddCreditDebitCardActivity.this.mBinding.edtSecondStepNumber.getText().length() == 0) {
                    mBinding.edtFirstStepNumber.requestFocus();
                }

                return false;
            }
        });
        mBinding.edtThirdStepNumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && AddCreditDebitCardActivity.this.mBinding.edtThirdStepNumber.getText().length() == 0) {
                    mBinding.edtSecondStepNumber.requestFocus();
                }

                return false;
            }
        });
        mBinding.edtFourStepNumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && AddCreditDebitCardActivity.this.mBinding.edtFourStepNumber.getText().length() == 0) {

                    mBinding.edtThirdStepNumber.requestFocus();
                }

                return false;
            }
        });
    }

    private void prepareView() {
        mBinding.edtCardNumber.getCardNumber();
        mBinding.edtCardNumber.isValid();
        mBinding.edtCardNumber.getCardType();


    }

    private void callGetLatestCardDetailAPI() {
        mBinding.edtCardCategoryInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.spCardCategoryList.performClick();
            }
        });
        mBinding.edtCardTypeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.spCardTypeList.performClick();
            }
        });
        ApiInterface apiService = ApiClient.getClient(AddCreditDebitCardActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.lastcardDetail(Pref.getValue(mContext, Constants.PREF_TOKEN, ""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, "" + response.toString());
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.optString("code").equals("200")) {
                            JSONObject payloadObject = jsonObject.getJSONObject("payload");
                            mBinding.edtEmailInput.setText(payloadObject.optString("email"));
                            mBinding.edtStreetAddress1Input.setText(payloadObject.optString("street_address1"));
                            mBinding.edtStreetAddress2Input.setText(payloadObject.optString("street_address2"));
                            mBinding.edtCityInput.setText(payloadObject.optString("city"));
                            mBinding.edtStateInput.setText(payloadObject.optString("state"));
                            mBinding.edtZipCodeInput.setText(payloadObject.optString("zip_code"));
                            // mBinding.edtBillingAddressInput.setText(payloadObject.optString("address"));
                            Log.e(TAG, "$$$ " + jsonObject.toString());
                        } else if (jsonObject.optString("code").equals("401")) {

                        } else if (jsonObject.optString("code").equals("500")) {
                            Utils.exitApplication(AddCreditDebitCardActivity.this);
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

        final ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("Select Card category");
        categoryList.add("Personal");
        categoryList.add("Business");


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spCardCategoryList.setAdapter(aa);

        mBinding.spCardCategoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mBinding.edtCardCategoryInput.setText(categoryList.get(position).toString());
                for (int i = 1; i <= categoryList.size(); i++) {
                    if (i == position) {
                        iscardCategory = categoryList.get(i);
                        if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Debit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getPersonal_debit());
                        } else if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Credit")) {

                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getPersonal_credit());

                        } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Debit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getBusiness_debit());

                        } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Credit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getBusiness_credit());

                        }


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final ArrayList<String> type_List = new ArrayList<>();
        type_List.add("Select Card type");
        type_List.add("Debit");
        type_List.add("Credit");


        ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type_List);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spCardTypeList.setAdapter(aa1);

        mBinding.spCardTypeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mBinding.edtCardTypeInput.setText(type_List.get(position).toString());
                for (int i = 1; i <= type_List.size(); i++) {
                    if (i == position) {
                        iscardType = type_List.get(i);
                        if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Debit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getPersonal_debit());
                        } else if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Credit")) {

                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getPersonal_credit());

                        } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Debit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getBusiness_debit());

                        } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Credit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getBusiness_credit());

                        }


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void callBankListAPI() {

        ApiInterface apiService = ApiClient.getClient(AddCreditDebitCardActivity.this).create(ApiInterface.class);
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
                                bankListdataModels[i].setBusiness_credit(payloadJSONObject.optString("business_credit"));
                                bankListdataModels[i].setBusiness_debit(payloadJSONObject.optString("business_debit"));
                                bankListdataModels[i].setPersonal_credit(payloadJSONObject.optString("personal_credit"));
                                bankListdataModels[i].setPersonal_debit(payloadJSONObject.optString("personal_debit"));


                                bankListdataModelArrayList.add(bankListdataModels[i]);
                                setbankList();
                            }
                        } else if (jsonObject.optString("code").equals("500")) {
                            Utils.exitApplication(AddCreditDebitCardActivity.this);
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

    private void callAddCardInfoPI(int bank_Id, String card_number, String expiry_date, String card_name, String nick_name, String street_address1, String street_address2, String city, String state, String zip_code, String card_category, String card_type, String customer_service_number, String type, String cvv, String email, String securityCode) {

        ApiInterface apiService = ApiClient.getClient(AddCreditDebitCardActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.addCardDetail(Pref.getValue(mContext, Constants.PREF_TOKEN, ""), String.valueOf(bank_Id), card_number, expiry_date, card_name, nick_name, street_address1, street_address2, city, state, zip_code, card_category, card_type, customer_service_number, type, cvv, email, securityCode);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Utils.dismissProgress();
                    if (response.isSuccessful()) {
                        Log.e(TAG, "callAddCardInfoPI " + response.toString());

                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);

                        if (jsonObject.optString("code").equals("200")) {
                            Utils.showToast(AddCreditDebitCardActivity.this, jsonObject.optString("message"));
                            finish();
                        } else if (jsonObject.optString("code").equals("500")) {
                            Utils.exitApplication(AddCreditDebitCardActivity.this);
                        } else {
                            Toast.makeText(AddCreditDebitCardActivity.this, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
                mBinding.edtPhoneNoBankcardInput.setText("");
                for (int i = 1; i < bankList.size(); i++) {
                    if (i == position) {
                        Log.e("BankList", "ID " + bankListdataModelArrayList.get(position - 1).getId());
                        bank_Id = Integer.parseInt(bankListdataModelArrayList.get(position - 1).getId());
                        if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Debit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getPersonal_debit());
                        } else if (iscardCategory.equalsIgnoreCase("Personal") && iscardType.equalsIgnoreCase("Credit")) {

                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getPersonal_credit());

                        } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Debit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getBusiness_debit());

                        } else if (iscardCategory.equalsIgnoreCase("Business") && iscardType.equalsIgnoreCase("Credit")) {
                            mBinding.edtPhoneNoBankcardInput.setText(bankListdataModelArrayList.get(position - 1).getBusiness_credit());

                        }


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void textInputLayouBlank() {
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

        mBinding.edtCardHolderNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlCardHolderNameInput.setError(null);
            }
        });

        mBinding.edtSecurityCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlSecurityCodeInput.setError(null);
            }
        });

        mBinding.edtPhoneNoBankcardInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0 && s.toString().length() < 14) {
                    mBinding.tlPhoneNoBankcardInput.setError(getString(R.string.please_enter_valid_phone_number));
                } else {
                    mBinding.tlPhoneNoBankcardInput.setError(null);
                }
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


        mBinding.edtStreetAddress1Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlStreetAddress1Input.setError(null);
            }
        });

        mBinding.edtStreetAddress2Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlStreetAddress2Input.setError(null);
            }
        });
        mBinding.edtCityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlCityInput.setError(null);
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

        mBinding.edtZipCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlZipCodeInput.setError(null);
            }
        });

    }


}
