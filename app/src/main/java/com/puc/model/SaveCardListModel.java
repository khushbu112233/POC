package com.puc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by aipxperts on 17/3/17.
 */

public class SaveCardListModel implements Parcelable {





    public boolean isChecked =false;
    public boolean isOpenOptionMenu=false;
    public String id;
    public String bank_id;
    public String bank_name;
    public String card_number;
    public String nick_name;
    public String type;
    public String expiry_date;
    public String card_name;
    public String street_address1;
    public String street_address2;
    public String city;
    public String state;
    public String zip_code;
    public String status;
    public String requestednewcard;
    public String customer_service_number;
    public String cvv_number;
    public String email;
    public String security_number;
    public String card_category;
    public String card_type;






    public boolean isOpenOptionMenu() {
        return isOpenOptionMenu;
    }

    public void setOpenOptionMenu(boolean openOptionMenu) {
        isOpenOptionMenu = openOptionMenu;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }



    protected SaveCardListModel(Parcel in) {
        id = in.readString();
        bank_id = in.readString();
        bank_name = in.readString();
        card_number = in.readString();
        card_type = in.readString();
        type = in.readString();
        expiry_date = in.readString();
        card_name = in.readString();
        nick_name=in.readString();
        street_address1 = in.readString();
        street_address2 = in.readString();
        city = in.readString();
        state = in.readString();
        zip_code = in.readString();
        status=in.readString();
        requestednewcard=in.readString();
        customer_service_number = in.readString();
        cvv_number=in.readString();
        email=in.readString();
        security_number=in.readString();
        card_category = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bank_id);
        dest.writeString(bank_name);
        dest.writeString(card_number);
        dest.writeString(card_type);
        dest.writeString(type);
        dest.writeString(expiry_date);
        dest.writeString(card_name);
        dest.writeString(nick_name);
        dest.writeString(street_address1);
        dest.writeString(street_address2);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zip_code);
        dest.writeString(status);
        dest.writeString(requestednewcard);
        dest.writeString(customer_service_number);
        dest.writeString(cvv_number);
        dest.writeString(email);
        dest.writeString(security_number);
        dest.writeString(card_category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SaveCardListModel> CREATOR = new Creator<SaveCardListModel>() {
        @Override
        public SaveCardListModel createFromParcel(Parcel in) {
            return new SaveCardListModel(in);
        }

        @Override
        public SaveCardListModel[] newArray(int size) {
            return new SaveCardListModel[size];
        }
    };
}

