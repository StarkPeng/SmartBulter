package com.stark.smartbutler.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.utils
 * 文件名:  ShareUtils
 * 创建者:  Stark
 * 创建时间：2017/2/8 14:47
 * 描述：   TODO
 */
public class ShareUtils {
    public static final String NAME = "config";

    public static void putString(Context mContext,String key , String value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context mContext,String key,String defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return  sp.getString(key,defValue);
    }

    public static void putInt(Context mContext,String key , int value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context mContext,String key,int defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return  sp.getInt(key,defValue);
    }

    public static void putBoolean(Context mContext,String key , boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context mContext,String key,boolean defValue){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return  sp.getBoolean(key,defValue);
    }

    public static void deleShare(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void deleAllShare(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
