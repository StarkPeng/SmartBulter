package com.stark.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stark.smartbutler.R;
import com.stark.smartbutler.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.ui
 * 文件名:  ForgetPasswordActivity
 * 创建者:  Stark
 * 创建时间：2017/2/10 11:04
 * 描述：   TODO
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_forget_password;
    private EditText find_password_email;

    private EditText old_password;
    private EditText new_password;
    private EditText new_config_password;
    private Button btn_update_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(this);
        find_password_email = (EditText) findViewById(R.id.find_password_email);

        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        new_config_password = (EditText) findViewById(R.id.new_config_password);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);
        btn_update_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forget_password:
                //1.获取输入框的邮箱
                final String email = find_password_email.getText().toString().trim();
                if(!TextUtils.isEmpty(email)){
                    //发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Toast.makeText(ForgetPasswordActivity.this,"验证信息已经发送至邮箱 ： "+email,Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ForgetPasswordActivity.this,"邮箱发送失败"+email,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(this,"邮箱不能为空！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update_password:
                String oldPassword = old_password.getText().toString().trim();
                String newPassword = new_password.getText().toString().trim();
                String newConfigPassword = new_config_password.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(oldPassword) & !TextUtils.isEmpty(newPassword) &
                        !TextUtils.isEmpty(newConfigPassword)) {
                    if(newPassword.equals(newConfigPassword)) {
                        //重置密码
                        MyUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e == null) {
                                    Toast.makeText(ForgetPasswordActivity.this,"重置密码成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(ForgetPasswordActivity.this,"重置密码失败: "+e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(ForgetPasswordActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(ForgetPasswordActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
