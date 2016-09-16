package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener{


    private TextView  mPersonInfo;
    private TextView  mSuggestion;
    private TextView  mSetting;
    private TextView  mCollect;
    private TextView  mUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
    }
    private void initView(){
        initActionBar("返回",R.drawable.back_selected,"个人中心","",0);
        mUserName = (TextView)findViewById(R.id.tv_user_name);
        Object object = SharedPreferencesUtils.readObject(this,"AccountInfo");
        LoginObj obj = (LoginObj)object;
        mUserName.setText(obj.getUsername());
        mPersonInfo = (TextView ) findViewById(R.id.tv_person_info);
        mCollect = (TextView)findViewById(R.id.tv_collect);
        mSetting = (TextView ) findViewById(R.id.tv_setting);
        mSuggestion = (TextView) findViewById(R.id.tv_suggestion);
        mUserName.setTypeface(Utils.getType(this,3));
        mPersonInfo.setTypeface(Utils.getType(this,3));
        mCollect.setTypeface(Utils.getType(this,3));
        mSetting.setTypeface(Utils.getType(this,3));
        mSuggestion.setTypeface(Utils.getType(this,3));

        mPersonInfo.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
        mCollect.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.tv_person_info:
                Intent intent1 = new Intent();
                intent1.setClass(PersonalCenterActivity.this, PersonalInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_setting:
                Intent intent2 = new Intent();
                intent2.setClass(PersonalCenterActivity.this, PersonSettingActivity.class);
                startActivity(intent2);

                break;
            case R.id.tv_suggestion:
                Intent intent3 = new Intent();
                intent3.setClass(PersonalCenterActivity.this, PersonSuggestionActivity.class);
                startActivity(intent3);
                break;

            case R.id.tv_collect:
                Intent intent4 = new Intent();
                intent4.setClass(PersonalCenterActivity.this, PersonCollectActivity.class);
                startActivity(intent4);
                break;

        }
    }
}
