package com.puc.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.puc.R;
import com.puc.databinding.ActivityEditcreditdebitCardDetailBinding;
import com.puc.model.BankListdataModel;
import com.puc.model.SaveCardListModel;
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

public class EditCreditCardDetailActivity extends BaseActivity {

    ActivityEditcreditdebitCardDetailBinding mBinding;
    private CardTextInputLayout expiryInputLayout;
    boolean isValidation = true;
    private int expMonth, expYear;
    private String cardPan, cardName, cvv, expDate, pin, otp;
    private String TAG = "EditCreditCardDetail";
    private Context mContext;
    ArrayList<BankListdataModel> bankListdataModelArrayList = new ArrayList<>();
    int bank_Id;
    String encryptSCardNumber;
    Intent mIntent;
    //SaveCardListModel saveCardListModel;
    ArrayList<SaveCardListModel> saveCardListModelArrayList = new ArrayList<>();
    int card_selection_position;
    int card_id;
    String cardType = "";
    String combineCardNumber = "";
    String combineExpiryDate = "";
    private Calendar cal;
    private int year, year1, year2, month1, month;
    String iscardType = "Debit";
    String iscardCategory = "Personal";
    boolean isEdit=false;
    String firstStepCardNumber="",secondStepCardNumber="",thirdStepCardNumber="",fourStepCardNumber="",monthExp="",yearExp="",ccv_num="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_editcreditdebit_card_detail);
        mContext = EditCreditCardDetailActivity.this;
        mIntent = getIntent();
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        setUp();
        mBinding.edtFirstStepNumber.setEnabled(false);
        mBinding.edtSecondStepNumber.setEnabled(false);
        mBinding.edtThirdStepNumber.setEnabled(false);
        mBinding.edtFourStepNumber.setEnabled(false);
        mBinding.edtExpiryMonth.setEnabled(false);
        mBinding.edtExpiryYear.setEnabled(false);
        mBinding.edtCcvCode.setEnabled(false);
        if(isEdit)
        {

            mBinding.ivEdit.setEnabled(false);
            mBinding.ivEdit.setTextColor(Color.GRAY);

        }else
        {
            editCardField();
            mBinding.ivEdit.setEnabled(true);
            mBinding.ivEdit.setTextColor(getResources().getColor(R.color.login_blue_color));

        }

        //  prepareView();
    }


    private void editCardField() {
        mBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEdit=true;
                mBinding.ivEdit.setTextColor(Color.GRAY);
                mBinding.edtFirstStepNumber.setEnabled(true);
                mBinding.edtSecondStepNumber.setEnabled(true);
                mBinding.edtThirdStepNumber.setEnabled(true);
                mBinding.edtFourStepNumber.setEnabled(true);
                mBinding.edtExpiryMonth.setEnabled(true);
                mBinding.edtExpiryYear.setEnabled(true);
                mBinding.edtCcvCode.setEnabled(true);
                mBinding.edtFirstStepNumber.setText(firstStepCardNumber);
                mBinding.edtSecondStepNumber.setText(secondStepCardNumber);
                mBinding.edtThirdStepNumber.setText(thirdStepCardNumber);
                mBinding.edtFourStepNumber.setText(fourStepCardNumber);
                mBinding.edtExpiryYear.setText(yearExp);
                mBinding.edtExpiryMonth.setText(monthExp);



            }
        });
    }

    public void openCalander() {
        hideKeyboard();
        RackMonthPicker rackMonthPicker = new RackMonthPicker(EditCreditCardDetailActivity.this)
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
                    // openCalander();
                }
                return false;
            }
        });


        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(mBinding.edtPhoneNoBankcardInput));
        mBinding.edtPhoneNoBankcardInput.addTextChangedListener(addLineNumberFormatter);

        // mBinding.edtPhoneNoBankcardInput.addTextChangedListener(new PhoneNumberTextWatcher(mBinding.edtPhoneNoBankcardInput));

       /* final ArrayList<String> month_List = new ArrayList<>();

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

        int current_year = Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2,4));
        for(int i=current_year;i<=60;i++)
        {
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


        saveCardListModelArrayList = mIntent.getParcelableArrayListExtra("card_model_detail");
        card_selection_position = mIntent.getIntExtra("position", 0);
        Log.e("Data", "$$$ " + saveCardListModelArrayList.size());
        for (int i = 0; i < saveCardListModelArrayList.size(); i++) {
            if (card_selection_position == i) {
                Log.e("Decrypt", "@@@@ " + saveCardListModelArrayList.get(i).card_number);
                String decryptedCardNumber = Utils.decrypt(saveCardListModelArrayList.get(i).card_number);
                Log.e("Decrypt", "$$$ " + decryptedCardNumber);
                String expDate = saveCardListModelArrayList.get(i).expiry_date;
                monthExp = expDate.substring(0, 2);
                yearExp = expDate.substring(3, 5);

                firstStepCardNumber = decryptedCardNumber.substring(0, 4);
                secondStepCardNumber = decryptedCardNumber.substring(4, 8);
                thirdStepCardNumber = decryptedCardNumber.substring(8, 12);
                fourStepCardNumber=decryptedCardNumber.substring(12,16);
                ccv_num=saveCardListModelArrayList.get(i).cvv_number;
                if(firstStepCardNumber.length()>0)
                {
                    mBinding.edtFirstStepNumber.setHintTextColor(getResources().getColor(R.color.black_de));
                }else
                {
                    mBinding.edtFirstStepNumber.setHintTextColor(getResources().getColor(R.color.ln_colorHint));
                }
                if(secondStepCardNumber.length()>0)
                {
                    mBinding.edtSecondStepNumber.setHintTextColor(getResources().getColor(R.color.black_de));
                }else
                {
                    mBinding.edtSecondStepNumber.setHintTextColor(getResources().getColor(R.color.ln_colorHint));
                }
                if(thirdStepCardNumber.length()>0)
                {
                    mBinding.edtThirdStepNumber.setHintTextColor(getResources().getColor(R.color.black_de));
                }else
                {
                    mBinding.edtThirdStepNumber.setHintTextColor(getResources().getColor(R.color.ln_colorHint));
                }
                if(fourStepCardNumber.length()>0)
                {
                    mBinding.edtFourStepNumber.setHintTextColor(getResources().getColor(R.color.black_de));
                }else
                {
                    mBinding.edtFourStepNumber.setHintTextColor(getResources().getColor(R.color.ln_colorHint));
                }
                //  mBinding.edtFirstStepNumber.setText(firstStepCardNumber);
                //  mBinding.edtSecondStepNumber.setText(secondStepCardNumber);
                //  mBinding.edtThirdStepNumber.setText(thirdStepCardNumber);
                mBinding.edtFourStepNumber.setText(fourStepCardNumber);
                mBinding.edtCardHolderNameInput.setText(saveCardListModelArrayList.get(i).card_name);
                mBinding.edtBanknameInput.setText(saveCardListModelArrayList.get(i).bank_name);
                mBinding.edtCardCategoryInput.setText(Utils.capitalize(saveCardListModelArrayList.get(i).card_category));
                mBinding.edtCardTypeInput.setText(Utils.capitalize(saveCardListModelArrayList.get(i).card_type));
                //  if(monthExp.length()>0&&yearExp.length()>0) {
                // mBinding.edtExpiryMonth.setText(monthExp);
                // mBinding.edtExpiryYear.setText(yearExp);
                //}
                //  mBinding.edtBillingAddressInput.setText(saveCardListModelArrayList.get(i).address);
                mBinding.edtNickNameInput.setText(saveCardListModelArrayList.get(i).nick_name);
                mBinding.edtStreetAddress1Input.setText(saveCardListModelArrayList.get(i).street_address1);
                mBinding.edtStreetAddress2Input.setText(saveCardListModelArrayList.get(i).street_address2);
                mBinding.edtCityInput.setText(saveCardListModelArrayList.get(i).city);
                mBinding.edtStateInput.setText(saveCardListModelArrayList.get(i).state);
                mBinding.edtZipCodeInput.setText(saveCardListModelArrayList.get(i).zip_code);
                mBinding.edtPhoneNoBankcardInput.setText(saveCardListModelArrayList.get(i).customer_service_number);
                mBinding.edtEmailInput.setText(saveCardListModelArrayList.get(i).email);
                mBinding.edtCcvCode.setText(ccv_num);
                mBinding.edtSecurityCodeInput.setText(saveCardListModelArrayList.get(i).security_number);
                cardType = saveCardListModelArrayList.get(i).type;
                card_id = Integer.parseInt(saveCardListModelArrayList.get(i).id);
                bank_Id = Integer.parseInt(saveCardListModelArrayList.get(i).bank_id);
                iscardCategory = saveCardListModelArrayList.get(i).card_category;
                iscardType = saveCardListModelArrayList.get(i).card_type;
               /* if(iscardCategory.equalsIgnoreCase("personal"))
                {
                    mBinding.rbPersonalCard.setChecked(true);

                }else if(iscardCategory.equalsIgnoreCase("business"))
                {
                    mBinding.rbBusinessCard.setChecked(true);

                }
                if(iscardType.equalsIgnoreCase("debit"))
                {
                    mBinding.rbDebitCard.setChecked(true);
                }else if(iscardType.equalsIgnoreCase("credit"))
                {
                    mBinding.rbCreditCard.setChecked(true);
                }*/
               /* mBinding.txtCardCategory.setText(": "+Utils.capitalize(saveCardListModelArrayList.get(i).card_category));
                mBinding.txtCardType.setText(": "+Utils.capitalize(saveCardListModelArrayList.get(i).card_type));
       */
            }
            Log.e("Data", "$$$ " + saveCardListModelArrayList.get(i).card_number);

        }

        mBinding.edtExpiryMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RackMonthPicker rackMonthPicker = new RackMonthPicker(EditCreditCardDetailActivity.this)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                Log.e("selected", "" + month + "" + year);
                                monthExp="";
                                yearExp="";
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

                //  mBinding.spMonthList.performClick();
            }
        });
        mBinding.edtExpiryYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RackMonthPicker rackMonthPicker = new RackMonthPicker(EditCreditCardDetailActivity.this)
                        .setPositiveButton(new DateMonthDialogListener() {
                            @Override
                            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                                Log.e("selected", "" + month + "" + year);
                                monthExp="";
                                yearExp="";
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
                                dialog.dismiss(); openKeyboard();
                            }
                        });


                rackMonthPicker.show();
            }
        });
        if (cardType.equals("Visa")) {
            mBinding.imgCardTypeLogo.setImageDrawable(mContext.getDrawable(R.mipmap.visa_logo_img));
        } else if (cardType.equals("MasterCard")) {
            mBinding.imgCardTypeLogo.setImageDrawable(mContext.getDrawable(R.mipmap.mastercard_logo_img));
        } else if (cardType.equals("American_Express")) {
            mBinding.imgCardTypeLogo.setImageDrawable(mContext.getDrawable(R.mipmap.americal_ex_logo_img));
        } else if (cardType.equals("Discover")) {
            mBinding.imgCardTypeLogo.setImageDrawable(mContext.getDrawable(R.mipmap.discover_logo_img));
        } else if (cardType.equals("JCB")) {
            mBinding.imgCardTypeLogo.setImageDrawable(mContext.getDrawable(R.mipmap.jcb_logo_img));
        } else if (cardType.equals("Diners_Club")) {
            mBinding.imgCardTypeLogo.setImageDrawable(mContext.getDrawable(R.mipmap.dinner_club_logo_img));
        } else {
            mBinding.imgCardTypeLogo.setImageDrawable(mContext.getDrawable(R.mipmap.default_credit_card_logo));
        }

        // callBankListAPI();
        //  Utils.showProgress(mContext);

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
                        expiryInputLayout.setHint("XX/XX");
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

                if(firstStepCardNumber.length()>0&&secondStepCardNumber.length()>0&&thirdStepCardNumber.length()>0&&fourStepCardNumber.length()>0) {
                    combineCardNumber = firstStepCardNumber.toString().trim() + "" + secondStepCardNumber.toString().trim() + "" + thirdStepCardNumber.toString().trim() + "" + fourStepCardNumber.toString().trim();
                }else
                {
                    combineCardNumber = mBinding.edtFirstStepNumber.getText().toString().trim() + "" + mBinding.edtSecondStepNumber.getText().toString().trim() + "" + mBinding.edtThirdStepNumber.getText().toString().trim() + "" + mBinding.edtFourStepNumber.getText().toString().trim();
                }
                if(monthExp.length()>0&&yearExp.length()>0)
                {
                    combineExpiryDate = monthExp.toString().trim() + "" + yearExp.toString().trim();
                }else {
                    combineExpiryDate = mBinding.edtExpiryMonth.getText().toString().trim() + "" + mBinding.edtExpiryYear.getText().toString().trim();
                }
                encryptSCardNumber = Utils.encrypt(combineCardNumber);


                if (!TextUtils.isEmpty(combineExpiryDate)) {
                    String finalYear = String.valueOf(year).substring(2);
                    if(monthExp.length()>0&&yearExp.length()>0)
                    {
                        year1 = Integer.parseInt(yearExp.toString().trim());
                        month1 = Integer.parseInt(monthExp.toString().trim());

                    }else
                    {
                        year1 = Integer.parseInt(mBinding.edtExpiryYear.getText().toString().trim());
                        month1 = Integer.parseInt(mBinding.edtExpiryMonth.getText().toString().trim());

                    }
                    year2 = Integer.parseInt(finalYear);
                    Log.e("Year", "$$$ " + finalYear);
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
                } else if (combineCardNumber.length() < 15 || cardType.equalsIgnoreCase("UNKNOWN")) {
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
                } else  if (year2 >= year1) {
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
                    //   isValidation = false;
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
                /*if (TextUtils.isEmpty(mBinding.tlBillingAddressInput.getEditText().getText().toString().trim())) {
                    isValidation = false;
                    mBinding.tlBillingAddressInput.setError(getString(R.string.Please_provide_address));
                }*/


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
                    // callAddCardInfoPI(bank_Id,encryptSCardNumber,mBinding.edtExpiryMonth.getText().toString().trim()+"/"+mBinding.edtExpiryYear.getText().toString().trim(),mBinding.edtCardHolderNameInput.getText().toString(),mBinding.edtBillingAddressInput.getText().toString(),mBinding.edtPhoneNoBankcardInput.getText().toString(),cardType);
                    if(monthExp.length()>0&&yearExp.length()>0) {
                        callEditCardInfoPI(card_id, encryptSCardNumber, monthExp + "/" + yearExp, mBinding.edtCardHolderNameInput.getText().toString(), mBinding.edtNickNameInput.getText().toString(), mBinding.edtStreetAddress1Input.getText().toString(), mBinding.edtStreetAddress2Input.getText().toString(), mBinding.edtCityInput.getText().toString(), mBinding.edtStateInput.getText().toString(), mBinding.edtZipCodeInput.getText().toString(), iscardCategory.toLowerCase(), iscardType.toLowerCase()
                                , mBinding.edtPhoneNoBankcardInput.getText().toString(), cardType, mBinding.edtCcvCode.getText().toString(), mBinding.edtEmailInput.getText().toString(), mBinding.edtSecurityCodeInput.getText().toString());
                    }else
                    {
                        callEditCardInfoPI(card_id, encryptSCardNumber, mBinding.edtExpiryMonth.getText().toString().trim() + "/" + mBinding.edtExpiryYear.getText().toString().trim(), mBinding.edtCardHolderNameInput.getText().toString(), mBinding.edtNickNameInput.getText().toString(), mBinding.edtStreetAddress1Input.getText().toString(), mBinding.edtStreetAddress2Input.getText().toString(), mBinding.edtCityInput.getText().toString(), mBinding.edtStateInput.getText().toString(), mBinding.edtZipCodeInput.getText().toString(), iscardCategory.toLowerCase(), iscardType.toLowerCase()
                            , mBinding.edtPhoneNoBankcardInput.getText().toString(), cardType, mBinding.edtCcvCode.getText().toString(), mBinding.edtEmailInput.getText().toString(), mBinding.edtSecurityCodeInput.getText().toString());

                    }
                    Utils.showProgress(mContext);
                }


            }
        });

        mBinding.imgBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                } else if (s.matches(CardPattern.AMERICAN_EXPRESS)) {

                    mBinding.edtCcvCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    mBinding.imgCardTypeLogo.setImageDrawable(getDrawable(R.mipmap.americal_ex_logo_img));
                    cardType = "American_Express";
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
                }else
                {
                    firstStepCardNumber="";
                    mBinding.edtFirstStepNumber.setHintTextColor(getResources().getColor(R.color.ln_colorHint));
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
                }else
                {
                    secondStepCardNumber="";
                    mBinding.edtSecondStepNumber.setHintTextColor(getResources().getColor(R.color.ln_colorHint));
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

                }else
                {
                    thirdStepCardNumber="";
                    mBinding.edtThirdStepNumber.setHintTextColor(getResources().getColor(R.color.ln_colorHint));
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
                    // mBinding.edtExpiryMonth.requestFocus();
                    // openCalander();
                    hideKeyboard();

                }else
                {
                    fourStepCardNumber="";
                    mBinding.edtFourStepNumber.setHintTextColor(getResources().getColor(R.color.ln_colorHint));
                }

            }
        });

        //for expriy month and year..
        mBinding.edtExpiryMonth.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && EditCreditCardDetailActivity.this.mBinding.edtExpiryYear.getText().length() == 0) {
                    mBinding.edtExpiryMonth.requestFocus();
                }

                return false;
            }
        });


        mBinding.edtSecondStepNumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && EditCreditCardDetailActivity.this.mBinding.edtSecondStepNumber.getText().length() == 0) {
                    mBinding.edtFirstStepNumber.requestFocus();
                }

                return false;
            }
        });
        mBinding.edtThirdStepNumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && EditCreditCardDetailActivity.this.mBinding.edtThirdStepNumber.getText().length() == 0) {
                    mBinding.edtSecondStepNumber.requestFocus();
                }

                return false;
            }
        });
        mBinding.edtFourStepNumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) && (paramInt == 67) && EditCreditCardDetailActivity.this.mBinding.edtFourStepNumber.getText().length() == 0) {

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


    private void callBankListAPI() {

        ApiInterface apiService = ApiClient.getClient(EditCreditCardDetailActivity.this).create(ApiInterface.class);
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

    private void callEditCardInfoPI(int bank_Id, String card_number, String expiry_date, String card_name, String nick_name, String address1, String address2, String city, String state, String zip, String card_category, String card_type, String customer_service_number, String type, String cvv, String email, String securityCode) {

        ApiInterface apiService = ApiClient.getClient(EditCreditCardDetailActivity.this).create(ApiInterface.class);
        Call<ResponseBody> call = apiService.editcardDetail(Pref.getValue(mContext, Constants.PREF_TOKEN, ""), String.valueOf(bank_Id), card_number, expiry_date, card_name, nick_name, address1, address2, city, state, zip, card_category, card_type, customer_service_number, type, cvv, email, securityCode);
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
                            Utils.showToast(EditCreditCardDetailActivity.this, jsonObject.optString("message"));
                            finish();
                        } else if (jsonObject.optString("code").equals("500")) {
                            Utils.exitApplication(EditCreditCardDetailActivity.this);
                        } else {
                            Toast.makeText(EditCreditCardDetailActivity.this, "" + jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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

                //  mBinding.spBankList.performClick();
            }
        });
        mBinding.edtCardCategoryInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mBinding.spCardCategoryList.performClick();
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


    /*    mBinding.edtBillingAddressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.tlBillingAddressInput.setError(null);
            }
        });
*/

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
