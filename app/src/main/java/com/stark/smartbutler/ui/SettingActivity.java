package com.stark.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.stark.smartbutler.R;
import com.stark.smartbutler.service.SmsService;
import com.stark.smartbutler.utils.ShareUtils;

/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.ui
 * 文件名:  SettingActivity
 * 创建者:  Stark
 * 创建时间：2017/2/8 14:08
 * 描述：   设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    //语音播报
    private Switch sw_speak;
    //短信提醒
    private Switch sw_sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);
        boolean isSpeak = ShareUtils.getBoolean(this,"isSpeak",false);
        sw_speak.setChecked(isSpeak);

        sw_sms = (Switch) findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);
        boolean isSms = ShareUtils.getBoolean(this,"isSms",false);
        sw_sms.setChecked(isSms);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sw_speak:
                //切换相反
                sw_speak.setSelected(!sw_speak.isSelected());
                //保存状态
                ShareUtils.putBoolean(this,"isSpeak",sw_speak.isChecked());
                break;

            case R.id.sw_sms:
                //切换相反
                sw_sms.setSelected(!sw_sms.isSelected());
                //保存状态
                ShareUtils.putBoolean(this,"isSms",sw_sms.isChecked());
                if(sw_sms.isChecked()){
                    startService(new Intent(this,SmsService.class));
                }else {
                    stopService(new Intent(this,SmsService.class));
                }
                break;
        }
    }
}
