package com.puc.model;

import java.io.Serializable;

/**
 * Created by aipxperts on 17/3/17.
 */

public class BankListdataModel implements Serializable {


    public String id;
    public String name;
    public String phone_number;
    public String personal_debit;
    public String personal_credit;
    public String business_debit;
    public String business_credit;

    public String getPersonal_debit() {
        return personal_debit;
    }

    public void setPersonal_debit(String personal_debit) {
        this.personal_debit = personal_debit;
    }

    public String getPersonal_credit() {
        return personal_credit;
    }

    public void setPersonal_credit(String personal_credit) {
        this.personal_credit = personal_credit;
    }

    public String getBusiness_debit() {
        return business_debit;
    }

    public void setBusiness_debit(String business_debit) {
        this.business_debit = business_debit;
    }

    public String getBusiness_credit() {
        return business_credit;
    }

    public void setBusiness_credit(String business_credit) {
        this.business_credit = business_credit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}

