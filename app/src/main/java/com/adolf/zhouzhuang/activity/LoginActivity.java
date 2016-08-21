package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;

public class LoginActivity extends Activity implements View.OnClickListener{
    private TextView mLeftActionbarTV;
    private TextView mMiddleActionbarTV;
    private TextView mRightActionbarTV;
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mLoginBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }
    private void initView(){
        mLeftActionbarTV = (TextView) findViewById(R.id.tv_left_actionbar);
        mMiddleActionbarTV = (TextView) findViewById(R.id.tv_middle_actionbar);
        mRightActionbarTV = (TextView) findViewById(R.id.tv_rigth_actionbar);
        mUsernameET = (EditText) findViewById(R.id.et_username);
        mPasswordET = (EditText) findViewById(R.id.et_password);
        mLoginBT = (Button) findViewById(R.id.bt_login);
        mLoginBT.setOnClickListener(this);
        initActionBar();
    }
    private void initActionBar(){
        mLeftActionbarTV.setText("返回");
        mMiddleActionbarTV.setText("登陆");
        mRightActionbarTV.setText("注册");
        mLeftActionbarTV.setOnClickListener(this);
        mRightActionbarTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_rigth_actionbar:
                Intent intent2 = new Intent();
                intent2.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }
}
