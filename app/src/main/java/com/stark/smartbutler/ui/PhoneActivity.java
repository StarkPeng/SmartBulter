package com.stark.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.stark.smartbutler.R;
import com.stark.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.ui
 * 文件名:  PhoneActivity
 * 创建者:  Stark
 * 创建时间：2017/3/1 19:04
 * 描述：   TODO
 */
public class PhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_phone;
    private ImageView iv_company;
    private TextView tv_result;
    private Button btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_del,btn_query;
    //标记位
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        iv_company = (ImageView) findViewById(R.id.iv_company);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);
        
        //长按事件
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_phone.setText("");
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        /**
         * 逻辑
         * 1.获取输入框内容
         * 2.判断是否为空
         * 3.网络请求
         * 4.解析Json
         * 5.结果显示
         * ----
         * 键盘逻辑
         */
        String str = et_phone.getText().toString();
        switch (v.getId()){
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if(flag){
                    flag = false;
                    str="";
                    et_phone.setText("");
                }
                et_phone.setText(str + ((Button)v).getText());
                //移动光标
                et_phone.setSelection(str.length() + 1);
                break;
            case R.id.btn_del:
                if(!TextUtils.isEmpty(str) && str.length() > 0){
                    //每次结尾减去1
                    et_phone.setText(str.substring(0,str.length() - 1));
                    et_phone.setSelection(str.length() - 1);
                }
                break;
            case R.id.btn_query:
                if(!TextUtils.isEmpty(str)){
                    getPhone(str);
                }else {
                    Toast.makeText(this,"电话号码不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getPhone(String str) {
        String uri = "http://apis.juhe.cn/mobile/get?phone=" + str + "&key=" + StaticClass.POHNE_KEY;
        RxVolley.get(uri, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                prasingJson(t);
            }
        });
    }

    /**
     *
     * {
     *  "resultcode":"200",
     *  "reason":"Return Successd!",
     *  "result":{
     *      "province":"浙江",
     *      "city":"杭州",
     *      "areacode":"0571",
     *      "zip":"310000",
     *      "company":"中国移动",
     *      "card":"移动动感地带卡"
     *  }
     * }
     */
    private void prasingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String province = jsonResult.getString("province");
            String city = jsonResult.getString("city");
            String areacode = jsonResult.getString("areacode");
            String zip = jsonResult.getString("zip");
            String company = jsonResult.getString("company");
            String card = jsonResult.getString("card");
            tv_result.setText("归属地：" + province + city + "\n"
            +"区号：" + areacode + "\n"
            +"邮编：" + zip + "\n"
            +"运营商：" + company + "\n"
            +"类型：" + card + "\n");

            switch (company) {
                case "移动":
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "联通":
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "电信":
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }
            flag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
