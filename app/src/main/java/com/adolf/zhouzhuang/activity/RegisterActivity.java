package com.adolf.zhouzhuang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText mUSernameET;
    private EditText mPasswordET;
    private EditText mConfirmET;
    private Button mRegisterBT;
    private TextView mProtoalTV;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView(){
        mUSernameET = (EditText) findViewById(R.id.et_username);
        mPasswordET = (EditText) findViewById(R.id.et_password);
        mConfirmET = (EditText) findViewById(R.id.et_confirm);
        mRegisterBT =(Button) findViewById(R.id.bt_register);
        mProtoalTV = (TextView) findViewById(R.id.tv_protoal);
        mRegisterBT.setOnClickListener(this);
        initActionBar("返回",R.drawable.back_selected,"注册新用户","登录",0);
        createLink(mProtoalTV);
    }

    private void createLink(TextView tv) {
        SpannableString sp = new SpannableString("注册即代表您同意《周庄博物馆软件服务协议》");
        sp.setSpan(new URLSpan("http://www.baidu.com"), 9, tv.getText().length() -1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.BLUE), 9, tv.getText().length()-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(sp);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_register:
                register();
                break;
            case R.id.tv_rigth_actionbar:
                Intent intent2 = new Intent();
                intent2.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }

    public void register(){
        progressDialog = ProgressDialog.show(this, "", "请稍候...", true, true);
        String userName = mUSernameET.getText().toString();
        String passWord = mPasswordET.getText().toString();
        String confirmPassWord = mConfirmET.getText().toString();
        if (!TextUtils.equals(passWord,confirmPassWord)){
            return;
        }
        RequestParams params = new RequestParams();
        params.add("username",userName);
        params.add("nickName","风驰天下");
        params.add("password",passWord);

        AsyncHttpClientUtils.getInstance().get(ServiceAddress.REGISTER,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                showToast("注册成功");
                progressDialog.dismiss();
                if (Utils.isRequestOK(statusCode) && response != null){
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                showToast("注册失败");
            }
        });

//        HttpRequest.post(ServiceAddress.REGISTER,params,new JsonHttpRequestCallback() {
//            @Override
//            protected void onSuccess(JSONObject jsonObject) {
//                super.onSuccess(jsonObject);
//                Toast.makeText(RegisterActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                if (TextUtils.equals(jsonObject.getString("success"),"0")){
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(int errorCode, String msg) {
//                super.onFailure(errorCode, msg);
//                progressDialog.dismiss();
//                showToast("注册失败");
//            }
//        });
    }
}
