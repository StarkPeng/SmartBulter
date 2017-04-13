package com.stark.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.stark.smartbutler.R;
import com.stark.smartbutler.adapter.WeChatAdapter;
import com.stark.smartbutler.entity.WeChatData;
import com.stark.smartbutler.ui.WebViewActivity;
import com.stark.smartbutler.utils.L;
import com.stark.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 *项目名:  SmartButler
 *包名:    com.stark.smartbutler.fragment
 *文件名:  WechatFragment
 *创建者:  Stark
 *创建时间：2017/1/21 21:29
 *描述：   TODO
 */
public class WechatFragment extends Fragment {

    private ListView mListView;
    private SwipeRefreshLayout mListRefresh;
    private List<WeChatData> mList = new ArrayList<>();
    private List<String> mListTitle = new ArrayList<>();
    private List<String> mListUrl = new ArrayList<>();
    private WeChatAdapter adapter;
    private int flag = 0;
    private int pno =2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat,null);
        findView(view);
        return view;
    }
    //初始化view
    private void findView(View view) {
        mListView = (ListView) view.findViewById(R.id.mListView);
        mListRefresh = (SwipeRefreshLayout) view.findViewById(R.id.mListRefresh);
        //解析接口
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY +"&pno=1&ps=10";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(getActivity(),t,Toast.LENGTH_SHORT).show();
                //super.onSuccess(t);
                //解析JSON
                parsingJson(t);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("Position:" + position);
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("title",mListTitle.get(position));
                intent.putExtra("url",mListUrl.get(position));
                startActivity(intent);
            }
        });

        mListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //解析接口
                String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY +"&pno="+pno+"&ps=10";
                pno++;
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        //Toast.makeText(getActivity(),t,Toast.LENGTH_SHORT).show();
                        //super.onSuccess(t);
                        //解析JSON
                        updateJson(t);
                    }


                });
                //关闭刷新动画
                mListRefresh.setRefreshing(false);
            }


        });
    }

    private void updateJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonList = jsonResult.getJSONArray("list");
            for (int i = 0 ;i < jsonList.length() ;i++) {
                JSONObject json = (JSONObject) jsonList.get(i);
                WeChatData data = new WeChatData();

                String title = json.getString("title");
                String url = json.getString("url");

                data.setTitle(title);
                data.setSource(json.getString("source"));
                data.setImgUrl(json.getString("firstImg"));
                L.i("title" + title);
                flag = 0;
                for(int j = 0;j<mList.size();j++){
                   // L.i("mList :" + mList.get(j).getTitle());
                    if (mList.get(j).getTitle().equals(title)) {
                        flag++ ;
                        L.i("flag :" + flag);
                    }
                }
                if (flag == 0) {
                    mList.add(0,data);
                    mListTitle.add(0,title);
                    mListUrl.add(0,url);
                    adapter.notifyDataSetChanged();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonList = jsonResult.getJSONArray("list");
            for (int i = 0 ;i < jsonList.length() ;i++) {
                JSONObject json = (JSONObject) jsonList.get(i);
                WeChatData data = new WeChatData();

                String title = json.getString("title");
                String url = json.getString("url");

                data.setTitle(title);
                data.setSource(json.getString("source"));
                data.setImgUrl(json.getString("firstImg"));
                L.i("firstImg : "+ json.getString("firstImg"));
                mList.add(data);

                mListTitle.add(title);
                mListUrl.add(url);
            }
            adapter = new WeChatAdapter(getActivity(),mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
