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
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.ZhouzhuangApplication;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.httpUtils.HttpCallBack;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cz.msebera.android.httpclient.Header;

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
        initActionBar("返回",R.drawable.back_selected,"登录","注册",0);
        ((ZhouzhuangApplication)getApplication()).getDaoSession();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                loginNew();
                break;
            case R.id.tv_rigth_actionbar:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }

    public void loginNew(){
        RequestParams params = new RequestParams();
//        AsyncHttpClient client = clientUtils.getAsyncHttpClient();
        String userName = mUsernameET.getText().toString();
        String passWord = mPasswordET.getText().toString();
        params.add("username", userName);
        params.add("password", passWord);
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.LOGIN,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SharedPreferencesUtils.putBoolean(LoginActivity.this,"AutoLogin",true);
                SharedPreferencesUtils.saveObject(LoginActivity.this,"AccountInfo",GsonUtil.jsonToBean(response,"data",LoginObj.class));
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                SharedPreferencesUtils.putBoolean(LoginActivity.this,"AutoLogin",false);
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void login(){
//        progressDialog = ProgressDialog.show(this, "", "请稍候...", true, true);
//        String userName = mUsernameET.getText().toString();
//        String passWord = mPasswordET.getText().toString();
//        RequestParams params = new RequestParams();
//        params.addFormDataPart("username",userName);
//        params.addFormDataPart("password",passWord);
//        HttpRequest.post(ServiceAddress.LOGIN,params,new JsonHttpRequestCallback() {
//            @Override
//            protected void onSuccess(JSONObject jsonObject) {
//                super.onSuccess(jsonObject);
//                Toast.makeText(LoginActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                if (TextUtils.equals(jsonObject.getString("success"),"0")){
//                    SharedPreferencesUtils.putBoolean(LoginActivity.this,"AutoLogin",true);
//                    SharedPreferencesUtils.getString(LoginActivity.this,"AccountInfo",jsonObject.getString("data"));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(int errorCode, String msg) {
//                super.onFailure(errorCode, msg);
//                progressDialog.dismiss();
//                SharedPreferencesUtils.putBoolean(LoginActivity.this,"AutoLogin",false);
//                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
