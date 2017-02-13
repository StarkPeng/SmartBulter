package com.stark.smartbutler.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.stark.smartbutler.MainActivity;
import com.stark.smartbutler.R;
import com.stark.smartbutler.utils.ShareUtils;
import com.stark.smartbutler.utils.StaticClass;

/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.ui
 * 文件名:  SplashActivity
 * 创建者:  Stark
 * 创建时间：2017/2/8 23:14
 * 描述：   TODO
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * 1、延时2000ms
     * 2、判断程序是否第一次运行
     * 3、自定义字体
     * 4、Activity全屏主题
     *
     */
    private TextView tv_splash;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.HANDLER_SPLASH:
                    if(isFirst()){
                        startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }
    //初始化View
    private void initView() {
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,2000);
        tv_splash = (TextView) findViewById(R.id.tv_splash);
        //Typeface fontStyle = Typeface.createFromAsset(getAssets(),"fonts/FONT.TIF");
        //tv_splash.setTypeface(fontStyle);
    }

    private boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this,StaticClass.SHARE_IS_FIRST,true);
        if(isFirst){
            ShareUtils.putBoolean(this,StaticClass.SHARE_IS_FIRST,false);
            return true;
        }else {
            return false;
        }
    }
    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
