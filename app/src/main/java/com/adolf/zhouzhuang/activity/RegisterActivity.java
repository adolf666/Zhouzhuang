package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;

public class RegisterActivity extends Activity implements View.OnClickListener {
    private TextView mLeftActionbarTV;
    private TextView mMiddleActionbarTV;
    private TextView mRightActionbarTV;

    private EditText mUSernameET;
    private EditText mPasswordET;
    private EditText mConfirmET;
    private Button mRegisterBT;
    private TextView mProtoalTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    private void initView(){
        mLeftActionbarTV = (TextView) findViewById(R.id.tv_left_actionbar);
        mMiddleActionbarTV = (TextView) findViewById(R.id.tv_middle_actionbar);
        mRightActionbarTV = (TextView) findViewById(R.id.tv_rigth_actionbar);
        mUSernameET = (EditText) findViewById(R.id.et_username);
        mPasswordET = (EditText) findViewById(R.id.et_password);
        mConfirmET = (EditText) findViewById(R.id.et_confirm);
        mRegisterBT =(Button) findViewById(R.id.bt_register);
        mProtoalTV = (TextView) findViewById(R.id.tv_protoal);
        initActionBar();
        createLink(mProtoalTV);
    }

    private void initActionBar(){
        mLeftActionbarTV.setText("返回");
        mMiddleActionbarTV.setText("注册新用户");
        mRightActionbarTV.setText("登录");
        mLeftActionbarTV.setOnClickListener(this);
        mRightActionbarTV.setOnClickListener(this);
    }

    private void createLink(TextView tv) {
        // 创建一个 SpannableString对象
        SpannableString sp = new SpannableString("注册即代表您同意《周庄博物馆软件服务协议》");
        // 设置超链接
        sp.setSpan(new URLSpan("http://www.baidu.com"), 9, tv.getText().length() -1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.BLUE), 9, tv.getText().length()-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(sp);
        //设置TextView可点击
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_register:
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
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
}
