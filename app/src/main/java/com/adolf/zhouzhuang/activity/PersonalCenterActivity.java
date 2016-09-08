package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener{


    private RelativeLayout mPersonInfo;
    private RelativeLayout mSuggestion;
    private RelativeLayout mSetting;
    private RelativeLayout mZhouzhuangLib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
    }
    private void initView(){
        initActionBar("返回",0,"个人中心","",0);

        mPersonInfo = (RelativeLayout) findViewById(R.id.rl_person_info);
        mSetting = (RelativeLayout) findViewById(R.id.rl_setting);
        mSuggestion = (RelativeLayout) findViewById(R.id.rl_suggestion);
        mZhouzhuangLib = (RelativeLayout) findViewById(R.id.rl_zhouzhuang_lib);

        mPersonInfo.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
        mZhouzhuangLib.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.rl_person_info:
                Intent intent1 = new Intent();
                intent1.setClass(PersonalCenterActivity.this, PersonalInfoActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.rl_setting:
                Intent intent2 = new Intent();
                intent2.setClass(PersonalCenterActivity.this, PersonSettingActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.rl_suggestion:
                Intent intent3 = new Intent();
                intent3.setClass(PersonalCenterActivity.this, PersonSuggestionActivity.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.rl_zhouzhuang_lib:
                Intent intent4 = new Intent();
                intent4.setClass(PersonalCenterActivity.this, ZhouzhuangLibActivity.class);
                startActivity(intent4);
                finish();
                break;

        }
    }
}
