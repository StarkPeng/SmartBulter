package com.stark.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stark.smartbutler.MainActivity;
import com.stark.smartbutler.entity.MyUser;
import com.stark.smartbutler.utils.ShareUtils;
import com.stark.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import com.stark.smartbutler.R;

/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.ui
 * 文件名:  LoginActivity
 * 创建者:  Stark
 * 创建时间：2017/2/9 15:17
 * 描述：   登陆
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_registered;
    private EditText login_name;
    private EditText login_password;
    private Button btn_Login;
    private CheckBox login_keep_password;
    private TextView forget_password;
    private CustomDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        forget_password = (TextView) findViewById(R.id.forget_password);
        forget_password.setOnClickListener(this);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        login_name = (EditText) findViewById(R.id.login_name);
        login_password = (EditText) findViewById(R.id.login_password);
        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_Login.setOnClickListener(this);
        login_keep_password = (CheckBox) findViewById(R.id.login_keep_password);

        dialog = new CustomDialog(this,100,100,R.layout.dialog_loding,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        dialog.setCancelable(false);
        //设置选中的状态
        boolean isCheck = ShareUtils.getBoolean(this,"keepPass",false);
        login_keep_password.setChecked(isCheck);
        if(isCheck){
            //设置密码
            login_name.setText(ShareUtils.getString(this,"login_name",""));
            login_password.setText(ShareUtils.getString(this,"login_password",""));
        }else {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_password:startActivity(new Intent(this,ForgetPasswordActivity.class));
                int i =0;
                break;
            case R.id.btn_registered:
                startActivity(new Intent(this,RegisteredActivity.class));
                break;
            case R.id.btn_login:
                //1.获取输入框的值
                String name = login_name.getText().toString().trim();
                String password = login_password.getText().toString().trim();
                if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) {
                    //登陆
                    dialog.show();
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            dialog.dismiss();
                            //判断结果
                            if(e == null){
                                //判断邮箱是否验证
                                if(user.getEmailVerified()){
                                    //跳转
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this,"请前往邮箱验证",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(LoginActivity.this,"登陆失败: " + e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(this,"输入框不能为空！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //假设我现在输入用户名和密码，但是我不点击登陆，而直接退出了，应该保存用户名和密码
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this,"keepPass",login_keep_password.isChecked());
        //是否记住密码
        if(login_keep_password.isChecked()){
            ShareUtils.putString(this,"login_name",login_name.getText().toString().trim());
            ShareUtils.putString(this,"login_password",login_password.getText().toString().trim());
        }else {
            ShareUtils.deleShare(this,"login_name");
            ShareUtils.deleShare(this,"login_password");
        }
    }
}
