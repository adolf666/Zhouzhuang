package com.adolf.zhouzhuang.activity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class PersonalInfoActivity extends BaseActivity {

    private TextView mSaveBtn;
    private EditText mUserName;
    private EditText mUserArea;
    private String mSex = "0";
    LoginObj loginObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal_info);
        initView();
        Log.i("ddddddd","onCreate"+"PersonalInfoActivity");
    }

    private void initView() {
        initActionBar("返回", R.drawable.back_selected, "完善个人信息", "", 0);
        mSaveBtn = (TextView) findViewById(R.id.tv_save_modify);
        mUserName = (EditText) findViewById(R.id.et_username);
        mUserArea =(EditText)findViewById(R.id.et_userarea);

        Object object= SharedPreferencesUtils.readObject(this, "AccountInfo");
        loginObj = (LoginObj) object;
        if (null!=loginObj&&loginObj.getUsername() != null) {
            mUserName.setText(loginObj.getUsername());
        }
        if (null!=loginObj&&loginObj.getArea() != null) {
            mUserArea.setText(loginObj.getArea());
        }
        Editable text1 = mUserArea.getText();
        Selection.setSelection(text1, text1.length());
        Editable text = mUserName.getText();
        Selection.setSelection(text, text.length());
        TextView user_name = (TextView) findViewById(R.id.tv_user_name);
        TextView sex = (TextView) findViewById(R.id.tv_sex);
        TextView are =(TextView)findViewById(R.id.tv_user_area);
        RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);

        mUserArea.setTypeface(Utils.getType(this, 3));
        mUserName.setTypeface(Utils.getType(this, 3));
        radioMale.setTypeface(Utils.getType(this, 3));
        radioFemale.setTypeface(Utils.getType(this, 3));
        mSaveBtn.setTypeface(Utils.getType(this, 0));
        user_name.setTypeface(Utils.getType(this, 3));
        sex.setTypeface(Utils.getType(this, 3));
        are.setTypeface(Utils.getType(this, 3));
        mSaveBtn.setOnClickListener(this);
        RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
        if (null!=loginObj&&loginObj.getSex() != null) {
         if(loginObj.getSex().equals("0")){
             group.check(R.id.radioMale);
         }else {
             group.check(R.id.radioFemale);
         }
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) PersonalInfoActivity.this.findViewById(radioButtonId);
                mSex = rb.getText().equals("男") ? "0" : "1";
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.tv_save_modify:
                RequestParams params = new RequestParams();
                if(null!=loginObj){
                    params.put("pid", loginObj.getPid());
                }
                params.put("sex", mSex);
                params.put("area", mUserArea.getText().toString());
                params.setContentEncoding("UTF-8");
                AsyncHttpClientUtils.getInstance().get(ServiceAddress.UPDGRAD_USER_INFO, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        loginObj.setArea(mUserArea.getText().toString());
                        loginObj.setSex(mSex);
                        loginObj.setUsername(mUserName.getText().toString());
                        SharedPreferencesUtils.saveObject(PersonalInfoActivity.this,"AccountInfo",loginObj);
                        Toast.makeText(PersonalInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(PersonalInfoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(PersonalInfoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

}