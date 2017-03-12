package com.stark.smartbutler.application;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.stark.smartbutler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

/**
 *项目名:  SmartButler
 *包名:    com.stark.smartbutler.application
 *文件名:  BaseApplication
 *创建者:  Stark
 *创建时间：2017/1/21 19:22
 *描述：   Application
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);

        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=" + StaticClass.VOICE_KEY);

    }
}
