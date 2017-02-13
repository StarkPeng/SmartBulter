package com.stark.smartbutler.utils;

import android.util.Log;

/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.utils
 * 文件名:  L
 * 创建者:  Stark
 * 创建时间：2017/2/8 14:35
 * 描述：   Log类封装
 */
public class L {
    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "Smartbutler";
    //5个等级 DIWEF
    public static void d(String text){
        if(DEBUG){
            Log.d(TAG,text);
        }
    }

    public static void i(String text){
        if(DEBUG){
            Log.i(TAG,text);
        }
    }
    public static void w(String text){
        if(DEBUG){
            Log.w(TAG,text);
        }
    }
    public static void e(String text){
        if(DEBUG){
            Log.e(TAG,text);
        }
    }
}
