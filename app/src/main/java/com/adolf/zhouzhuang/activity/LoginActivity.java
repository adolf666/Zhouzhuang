package com.adolf.zhouzhuang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.ZhouzhuangApplication;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.alibaba.fastjson.JSONObject;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

public class LoginActivity extends BaseActivity{
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mLoginBT;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }
    private void initView(){
        mUsernameET = (EditText) findViewById(R.id.et_username);
        mPasswordET = (EditText) findViewById(R.id.et_password);
        mLoginBT = (Button) findViewById(R.id.bt_login);
        mLoginBT.setOnClickListener(this);
        initActionBar("返回",0,"登录","注册",0);
        ((ZhouzhuangApplication)getApplication()).getDaoSession();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_rigth_actionbar:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }

    public void login(){
        progressDialog = ProgressDialog.show(this, "", "请稍候...", true, true);
        String userName = mUsernameET.getText().toString();
        String passWord = mPasswordET.getText().toString();
        RequestParams params = new RequestParams();
        params.addFormDataPart("username",userName);
        params.addFormDataPart("password",passWord);
        HttpRequest.post(ServiceAddress.LOGIN,params,new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                Toast.makeText(LoginActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                if (TextUtils.equals(jsonObject.getString("success"),"0")){
                    SharedPreferencesUtils.putBoolean(LoginActivity.this,"AutoLogin",true);
                    SharedPreferencesUtils.getString(LoginActivity.this,"AccountInfo",jsonObject.getString("data"));
                    finish();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                progressDialog.dismiss();
                SharedPreferencesUtils.putBoolean(LoginActivity.this,"AutoLogin",false);
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
