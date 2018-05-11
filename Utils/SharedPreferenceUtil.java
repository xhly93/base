package com.ssj.user.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 王矩龙 on 2018/3/26.
 */

public class SharedPreferenceUtil {
    private static final String TAG = "SharedPreferenceUtil";
    private static SharedPreferences mSharedPreferences;
    private static final String JIGUANG_REGISTRATION = "jiguang_registration";

    public static void initSharedPreference(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static void setToken(String token) {
        mSharedPreferences.edit().putString(Contants.JsobContants.TOKEN, token).apply();
    }

    public static String getToken() {
        return mSharedPreferences.getString(Contants.JsobContants.TOKEN, "");
    }
    public static void setJIGuangRegistrationID(String id) {
        mSharedPreferences.edit().putString(JIGUANG_REGISTRATION, id).apply();
    }

    public static String getJIGuangRegistrationID() {
        return mSharedPreferences.getString(JIGUANG_REGISTRATION, "");
    }
    public static void putValue(String key,String value) {
        //获取SharePreference对象的编辑对象，才能进行数据的存储
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public static void putValues(ContentValues contentValues) {
        //获取SharePreference对象的编辑对象，才能进行数据的存储
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (String key:contentValues.keySet()){
            editor.putString(key, contentValues.getAsString(key));
        }
        editor.apply();
    }

    public static String getValue(String key) {
        //获取SharePreference对象的编辑对象，才能进行数据的存储
        return mSharedPreferences.getString(key,"");
    }

}
