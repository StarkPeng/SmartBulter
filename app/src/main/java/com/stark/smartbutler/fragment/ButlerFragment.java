package com.stark.smartbutler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.stark.smartbutler.R;
import com.stark.smartbutler.adapter.ChatListAdapter;
import com.stark.smartbutler.entity.ChatListData;
import com.stark.smartbutler.utils.ShareUtils;
import com.stark.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 *项目名:  SmartButler
 *包名:    com.stark.smartbutler.fragment
 *文件名:  ButlerFragment
 *创建者:  Stark
 *创建时间：2017/1/21 21:27
 *描述：   TODO
 */
public class ButlerFragment extends Fragment implements View.OnClickListener {
    private ListView mChatListView;
    private List<ChatListData> mList = new ArrayList<>();
    private ChatListAdapter adapter;
    //输入框
    private EditText et_text;
    //发送按键
    private Button btn_send;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mChatListView = (ListView) view.findViewById(R.id.mChatListView);
        et_text = (EditText) view.findViewById(R.id.et_text);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        //设置适配器

         adapter = new ChatListAdapter(getActivity(),mList);
         mChatListView.setAdapter(adapter);
         addLeftItem("你好，我是小管家！");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                /**
                 *逻辑
                 * 1、获取输入框的内容
                 * 2、判断是否为空
                 * 3、判断长度不能大于30
                 * 4、清空当前的输入框
                 * 5、添加你输入的内容到right item
                 * 6、发送给机器人请求返回内容
                 * 7、拿到机器人的返回值添加到left item
                 */
                //1、获取输入框的内容
                String mText = et_text.getText().toString();
                //2、判断是否为空
                if (!TextUtils.isEmpty(mText)) {
                    //3、判断长度不能大于30
                    if(mText.length() > 30){
                        Toast.makeText(getActivity(),"输入长度超出限制",Toast.LENGTH_SHORT).show();
                    }else {
                        //4、清空当前的输入框
                        et_text.setText("");
                        //5、添加你输入的内容到right item
                        addRightItem(mText);
                        //6、发送给机器人请求返回内容
                        String url = "http://op.juhe.cn/robot/index?info="+ mText + "&key=" + StaticClass.CHAT_LIST_KEY;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                               // Toast.makeText(getActivity(),"json:" + t,Toast.LENGTH_SHORT).show();
                               parsingJson(t); 
                            }
                        });
                        //7、拿到机器人的返回值添加到left item
                    }
                }else {
                    Toast.makeText(getActivity(),"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void parsingJson(String t) {
        try {
            /**
             *{
             *  "reason":"成功的返回",
             *  "result": 根据code值的不同，返回的字段有所不同
             *      {
             *           "code":100000, 返回的数据类型，请根据code的值去数据类型API查询
             *          "text":"你好啊，希望你今天过的快乐"
             *     },
             *  "error_code":0
             *}
             **/
            JSONObject mJSONObject = new JSONObject(t);
            JSONObject mJSONResult = mJSONObject.getJSONObject("result");
            String mStringText = mJSONResult.getString("text");
            //7、拿到机器人的返回值添加到left item
            addLeftItem(mStringText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text){
        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //添加左边文本
    private void addRightItem(String text){
        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());

    }
}
