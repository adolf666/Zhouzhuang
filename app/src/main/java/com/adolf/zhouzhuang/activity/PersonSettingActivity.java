package com.adolf.zhouzhuang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class PersonSettingActivity extends BaseActivity{

    private RelativeLayout mModifyPassword;
    private TextView mCleanStore;
    private TextView mLoginOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        initView();
    }
    private void initView(){
        initActionBar("返回",0,"个人设置","",0);

        mModifyPassword = (RelativeLayout)findViewById(R.id.rl_modify_password);
        mCleanStore = (TextView)findViewById(R.id.clean_store);
        mLoginOff = (TextView)findViewById(R.id.login_off);
        mModifyPassword.setOnClickListener(this);
        mCleanStore.setOnClickListener(this);
        mLoginOff.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.rl_modify_password:
                Intent intent = new Intent();
                intent.setClass(PersonSettingActivity.this,ModifyPasswordActivity.class);
                startActivity(intent);
            break;
            case R.id.clean_store:
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Toast.makeText(this,"缓存已清除",Toast.LENGTH_SHORT).show();

                break;
        }}
}
