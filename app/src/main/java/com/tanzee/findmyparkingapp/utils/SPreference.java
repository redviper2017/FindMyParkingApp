package com.tanzee.findmyparkingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;



public class SPreference{

    protected static final String PHONE_NUMBER = "phone_number";

    private Context context;
    private SharedPreferences sp;

    public SPreference(Context context){
        this.context = context;
        sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public String getPhoneNumber(){
        return sp.getString(PHONE_NUMBER, "na");
    }

    public void setPhoneNumber(String number){
        sp.edit().putString(PHONE_NUMBER, number).commit();
    }
}
