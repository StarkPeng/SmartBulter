package com.stark.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stark.smartbutler.R;
import com.stark.smartbutler.entity.MyUser;
import com.stark.smartbutler.ui.LoginActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 *项目名:  SmartButler
 *包名:    com.stark.smartbutler.fragment
 *文件名:  UserFragment
 *创建者:  Stark
 *创建时间：2017/1/21 21:15
 *描述：   个人中心
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    private Button user_exit;
    private Button btn_confirm;
    private TextView edit_user;
    private EditText user_name;
    private EditText user_age;
    private EditText user_sex;
    private EditText user_desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        user_exit = (Button) view.findViewById(R.id.user_exit);
        user_exit.setOnClickListener(this);
        edit_user = (TextView) view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        user_name = (EditText) view.findViewById(R.id.user_name);
        user_age = (EditText) view.findViewById(R.id.user_age);
        user_sex = (EditText) view.findViewById(R.id.user_sex);
        user_desc = (EditText) view.findViewById(R.id.user_desc);

        setViewEnabled(false);

        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        user_name.setText(userInfo.getUsername());
        user_age.setText(userInfo.getAge() + "");
        user_sex.setText(userInfo.isSex() ? "男" : "女");
        user_desc.setText(userInfo.getDesc());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_exit:
                //清除缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.edit_user:
                btn_confirm.setVisibility(View.VISIBLE);
                setViewEnabled(true);
                break;
            case R.id.btn_confirm:
                //1、拿到输入框的值
                String name = user_name.getText().toString();
                String age = user_age.getText().toString();
                String sex = user_sex.getText().toString();
                String desc = user_desc.getText().toString();

                //1、判断是否为空
                if (!TextUtils.isEmpty(name) &
                        !TextUtils.isEmpty(age) &
                        !TextUtils.isEmpty(sex)) {
                    //3、更新属性
                    MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setAge(Integer.parseInt(age));
                    if(sex.equals("man")) {
                        user.setSex(true);
                    }else {
                        user.setSex(false);
                    }
                    if(!TextUtils.isEmpty(desc)){
                        user.setDesc(desc);
                    }else {
                        user.setDesc("这个人很懒，什么都没有留下");
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                setViewEnabled(false);
                                btn_confirm.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "修改资料成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "修改资料失败: "+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //控制view的焦点
    private void setViewEnabled(boolean is) {
        user_name.setEnabled(is);
        user_age.setEnabled(is);
        user_sex.setEnabled(is);
        user_desc.setEnabled(is);
    }
}
