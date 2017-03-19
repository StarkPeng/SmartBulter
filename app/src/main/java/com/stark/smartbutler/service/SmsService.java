package com.stark.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.WindowManager;

import com.stark.smartbutler.R;
import com.stark.smartbutler.utils.L;
import com.stark.smartbutler.utils.StaticClass;

/**
 * Created by stark on 2017/3/12.
 */

public class SmsService extends Service{

    private SmsReceiver mSmsReceiver;
    //发件人号码
    private String smsPhone;
    //发件人内容
    private String smsContent;
    //窗口管理器
    private WindowManager mWindowManger;
    //布局参数
    private WindowManager.LayoutParams mLayoutParams;
    //view
    private View mView;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        L.i("init service");
        //动态注册广播
        mSmsReceiver = new SmsReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        //添加action
        mIntentFilter.addAction(StaticClass.SMS_ACTION);
        //设置优先级
        mIntentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mSmsReceiver,mIntentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");
        unregisterReceiver(mSmsReceiver);
    }

    //短信广播
    public class SmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StaticClass.SMS_ACTION.equals(action)){
                L.i("来短信了");
                //获取短信信息
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for (Object obj : objs){
                    //把数组元素转换成短信对象
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    //发件人
                    smsPhone =sms.getOriginatingAddress();
                    //内容
                    smsContent = sms.getDisplayMessageBody();
                    L.i("短信内容 ：" + smsPhone + " " +smsContent);

                    showWindow();
                }
            }
        }
    }
    //窗口提示
    private void showWindow() {
        mWindowManger = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式 透明
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        //定义类型
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView = View.inflate(getApplicationContext(), R.layout.sms_item,null);

        //添加View到窗口
        mWindowManger.addView(mView,mLayoutParams);
    }
}
