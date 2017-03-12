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

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
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
    //TTS
    private SpeechSynthesizer mTts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
         mTts= SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

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
        boolean isSpeak = ShareUtils.getBoolean(getActivity(),"isSpeak",false);
        if(isSpeak) {
            startSpeak(text);
        }
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

    //說話的方法
    private void startSpeak(String text) {
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
    }
        //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener(){
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {

        }
        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {

        }
        //开始播放
        public void onSpeakBegin() {

        }
        //暂停播放
        public void onSpeakPaused() {

        }
        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }
        //恢复播放回调接口
        public void onSpeakResumed() {

        }
        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

        }
    };
}

