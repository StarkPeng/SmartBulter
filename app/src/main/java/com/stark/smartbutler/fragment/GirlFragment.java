package com.stark.smartbutler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.stark.smartbutler.R;
import com.stark.smartbutler.adapter.GridAdapter;
import com.stark.smartbutler.entity.GirlData;
import com.stark.smartbutler.utils.L;
import com.stark.smartbutler.utils.PicassoUtils;
import com.stark.smartbutler.utils.StaticClass;
import com.stark.smartbutler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/*
 *项目名:  SmartButler
 *包名:    com.stark.smartbutler.fragment
 *文件名:  GirlFragment
 *创建者:  Stark
 *创建时间：2017/1/21 21:28
 *描述：   TODO
 */
public class GirlFragment extends Fragment {
    private GridView mGridView;
    private List<GirlData> mList = new ArrayList<>();
    private GridAdapter mAdapter;
    //提示框
    private ImageView image_girl;

    private CustomDialog mcustomDialog;

    PhotoViewAttacher mAttacher;
    //图片地址的数据
    private List<String> mListUrl = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mGridView = (GridView) view.findViewById(R.id.mGridView);

        //初始化提示框
        mcustomDialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,R.layout.dialog_girl,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);

        image_girl = (ImageView) mcustomDialog.findViewById(R.id.image_girl);

        String welfare = null;
        try {
            //Gank升級 需要转码
            welfare = URLEncoder.encode(getString(R.string.text_welfare), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //解析
        RxVolley.get("http://gank.io/api/search/query/listview/category/"+welfare+"/count/50/page/1", new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("Girl Json:" + t);
                prasingJson(t);
            }
        });

        //监听点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                PicassoUtils.loadImageView(getActivity(),mListUrl.get(position),image_girl);
                mAttacher = new PhotoViewAttacher(image_girl);
                mAttacher.update();
                mcustomDialog.show();

            }
        });
    }

    private void prasingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray  = jsonObject.getJSONArray("results");
            for(int i = 0; i<jsonArray.length();i++){
                JSONObject json = (JSONObject) jsonArray.get(i);
                String url = json.getString("url");
                mListUrl.add(url);
                GirlData data = new GirlData();
                data.setImgUrl(url);
                mList.add(data);
            }
            mAdapter = new GridAdapter(getActivity(),mList);
            mGridView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
