package com.stark.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stark.smartbutler.R;
import com.stark.smartbutler.entity.MyUser;
import com.stark.smartbutler.ui.CourierActivity;
import com.stark.smartbutler.ui.LoginActivity;
import com.stark.smartbutler.utils.L;
import com.stark.smartbutler.utils.ShareUtils;
import com.stark.smartbutler.utils.UtilsTools;
import com.stark.smartbutler.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

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
    //圆形头像
    private CircleImageView profile_image;
    private CustomDialog dialog;

    private Button dialog_camera;
    private Button dialog_pictrue;
    private Button dialog_cancel;
    private TextView tv_courier;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        tv_courier = (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
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

        //圆形头像
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);

        UtilsTools.getImageFromShare(getActivity(),profile_image);
        dialog = new CustomDialog(getActivity(),0,0,R.layout.dialog_photo,R.style.pop_anim_style, Gravity.BOTTOM,R.style.pop_anim_style);
        dialog.setCancelable(false);
        dialog_camera = (Button) dialog.findViewById(R.id.dialog_camera);
        dialog_camera.setOnClickListener(this);
        dialog_pictrue = (Button) dialog.findViewById(R.id.dialog_picture);
        dialog_pictrue.setOnClickListener(this);
        dialog_cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        dialog_cancel.setOnClickListener(this);

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

            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.dialog_cancel:
                dialog.dismiss();
                break;
            case R.id.dialog_camera:
                toCamera();
                break;
            case R.id.dialog_picture:
                toPictrue();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(),CourierActivity.class));
        }
    }
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int PICTURE_REQUEST_CODE =101;
    public static final int RESULT_REQUEST_CODE =102;
    private File tempFile = null;
    //跳转到相册
    private void toPictrue() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICTURE_REQUEST_CODE);
        dialog.dismiss();
    }
    //跳转到相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
        dialog.dismiss();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case PICTURE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原来的就应该删除
                        if(tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri){
        if(uri == null){
            L.e(" url == null ");
            return;
        }else {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri,"image/*");
            //设置裁剪
            intent.putExtra("crop","true");
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置图片的质量
            intent.putExtra("outputX",320);
            intent.putExtra("outputY",320);
            //发送数据
            intent.putExtra("return-data",true);
            startActivityForResult(intent,RESULT_REQUEST_CODE);
        }
    }
    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if(bundle != null){
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }
    //控制view的焦点
    private void setViewEnabled(boolean is) {
        user_name.setEnabled(is);
        user_age.setEnabled(is);
        user_sex.setEnabled(is);
        user_desc.setEnabled(is);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilsTools.putImageToShare(getActivity(),profile_image);
    }
}
