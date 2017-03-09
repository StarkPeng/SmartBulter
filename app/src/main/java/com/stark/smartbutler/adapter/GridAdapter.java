package com.stark.smartbutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.stark.smartbutler.R;
import com.stark.smartbutler.entity.GirlData;
import com.stark.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.adapter
 * 文件名:  GridAdapter
 * 创建者:  Stark
 * 创建时间：2017/3/6 11:14
 * 描述：   TODO
 */
public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private List<GirlData> mList;
    private LayoutInflater inflater;
    private GirlData data;
    private int width;
    private WindowManager mWindowManager;

    public GridAdapter(Context mContext,List<GirlData> mList){
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //获取屏幕宽和高
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.girl_item,null);
            viewHodler.imageView = (ImageView) convertView.findViewById(R.id.girl_imageView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        data = mList.get(position);
        String url = data.getImgUrl();
        //加载图片
        //if (!TextUtils.isEmpty(data.getImgUrl()))
            PicassoUtils.loadImageViewSize(mContext,url,width/2,600,viewHodler.imageView);
        return convertView;
    }

    class ViewHodler{
        private ImageView imageView;
    }
}
