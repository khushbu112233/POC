package com.puc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aipxperts on 17/3/17.
 */

public class RequestNewCardListModel implements Parcelable {





    public boolean isChecked =false;
    public boolean isOpenOptionMenu=false;
    public String id;
    public String city;
    public String state;
    public String zip_code;
    public String bank_id;
    public String bank_name;
    public String card_number;
    public String card_type;
    public String street_address1;
    public String type;
    public String card_category;
    public String expiry_date;
    public String card_name;
    public String address;
    public String status;
    public String requestednewcard;
    public String customer_service_number;
    public String cvv_number;
    public String email;
    public String security_number;
    public String nick_name;






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



    protected RequestNewCardListModel(Parcel in) {
        id = in.readString();
        bank_id = in.readString();
        bank_name = in.readString();
        card_number = in.readString();
        card_type = in.readString();
        type = in.toString();
        card_category = in.toString();
        expiry_date = in.readString();
        card_name = in.readString();
        address = in.readString();
        status=in.readString();
        requestednewcard=in.readString();
        customer_service_number = in.readString();
        cvv_number=in.readString();
        email=in.readString();
        security_number=in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bank_id);
        dest.writeString(bank_name);
        dest.writeString(card_number);
        dest.writeString(card_type);
        dest.writeString(type);
        dest.writeString(card_category);
        dest.writeString(expiry_date);
        dest.writeString(card_name);
        dest.writeString(address);
        dest.writeString(status);
        dest.writeString(requestednewcard);
        dest.writeString(customer_service_number);
        dest.writeString(cvv_number);
        dest.writeString(email);
        dest.writeString(security_number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RequestNewCardListModel> CREATOR = new Creator<RequestNewCardListModel>() {
        @Override
        public RequestNewCardListModel createFromParcel(Parcel in) {
            return new RequestNewCardListModel(in);
        }

        @Override
        public RequestNewCardListModel[] newArray(int size) {
            return new RequestNewCardListModel[size];
        }
    };
}

