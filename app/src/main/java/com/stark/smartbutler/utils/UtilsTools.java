package com.stark.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *项目名:   SmartButler
 *包名:     com.stark.smartbutler.utils
 *文件名:   UtilsTools
 *创建者:   Stark
 *创建时间：2017/1/21 19:39
 *描述：   工具类
 */
public class UtilsTools {

    public static void putImageToShare(Context context, ImageView imageView){
        //保存头像数据
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //1.将Bitmap亚索成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byStream);
        //2.利用Base64将我们的字节数组输出流转换成String
        byte[] byteArray = byStream.toByteArray();
        String imgString = new String(Base64.encodeToString(byteArray,Base64.DEFAULT));
        //3.将String保存到shareUtils
        ShareUtils.putString(context,"image_title",imgString);
    }

    public static void getImageFromShare(Context context,ImageView imageView){
        //1.拿到String
        String imgString = ShareUtils.getString(context,"image_title","");
        if(!imgString.equals("")){
            //2.利用Base64将我们String转换
            byte[] byteArray = Base64.decode(imgString,Base64.DEFAULT);
            ByteArrayInputStream byStream = new ByteArrayInputStream(byteArray);
            //3.生成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byStream);
            imageView.setImageBitmap(bitmap);
        }
    }
}
