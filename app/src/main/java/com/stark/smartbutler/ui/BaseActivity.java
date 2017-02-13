package com.stark.smartbutler.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 *项目名:  SmartButler
 *包名:    com.stark.smartbutler.ui
 *文件名:  BaseActivity
 *创建者:  Stark
 *创建时间：2017/1/21 19:26
 *描述：   Activity基类
 */

/**
 *主要做的事情：
 * 1、统一的熟悉
 * 2、统一的接口
 * 3、统一的方法
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
