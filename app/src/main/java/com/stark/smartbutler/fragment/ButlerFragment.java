package com.stark.smartbutler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.stark.smartbutler.R;
import com.stark.smartbutler.adapter.ChatListAdapter;
import com.stark.smartbutler.entity.ChatListData;

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
    private Button btn_left,btn_right;
    private List<ChatListData> mList = new ArrayList<>();
    private ChatListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mChatListView = (ListView) view.findViewById(R.id.mChatListView);
        btn_left = (Button) view.findViewById(R.id.btn_left);
        btn_right = (Button) view.findViewById(R.id.btn_right);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        //设置适配器

         adapter = new ChatListAdapter(getActivity(),mList);
         mChatListView.setAdapter(adapter);
         addLeftItem("你好，我是小管家！");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                addLeftItem("左边");
                break;
            case R.id.btn_right:
                addRightItem("右边");
                break;
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
