package com.adolf.zhouzhuang.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener{


    private TextView  mPersonInfo;
    private TextView  mSuggestion;
    private TextView  mSetting;
    private TextView  mCollect;
    private TextView  mUserName;
    private TextView  mStrategy;
    private ImageView mPortrait;
    private TextView mAboutTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
    }
    private void initView(){
        initActionBar("返回",R.drawable.back_selected,"个人中心","",0);
        mPortrait = (ImageView)findViewById(R.id.iv_portrait);
        mUserName = (TextView)findViewById(R.id.tv_name);
        mPersonInfo = (TextView ) findViewById(R.id.tv_person_info);
        mCollect = (TextView)findViewById(R.id.tv_collect);
        mSetting = (TextView ) findViewById(R.id.tv_setting);
        mSuggestion = (TextView) findViewById(R.id.tv_suggestion);
        mAboutTv = (TextView) findViewById(R.id.tv_about);
      //  mStrategy = (TextView)findViewById(R.id.tv_strategy);
//        mUserName.setTypeface(Utils.getType(this,3));
//        mPersonInfo.setTypeface(Utils.getType(this,3));
//        mCollect.setTypeface(Utils.getType(this,3));
//        mSetting.setTypeface(Utils.getType(this,3));
//        mSuggestion.setTypeface(Utils.getType(this,3));
      //  mStrategy.setTypeface(Utils.getType(this,3));
     //   mStrategy.setOnClickListener(this);
        mPersonInfo.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
        mCollect.setOnClickListener(this);
        mAboutTv.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isAutoLogin(this)){
            setPersonalInfo();
        }else {
            mUserName.setOnClickListener(this);
            mPortrait.setOnClickListener(this);
            mPersonInfo.setVisibility(View.GONE);
            mUserName.setText("登录/注册");
        }
    }
    private void setPersonalInfo(){
        LoginObj obj= GsonUtil.jsonToBean(SharedPreferencesUtils.getString(this, "AccountInfo",""),LoginObj.class);
        mUserName.setText(obj.getUsername());
        mPersonInfo.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.tv_person_info:
                Intent intentPerson = new Intent();
                if (Utils.isAutoLogin(PersonalCenterActivity.this)){
                    intentPerson.setClass(PersonalCenterActivity.this,PersonalInfoActivity.class);
                }else{
                    intentPerson.setClass(PersonalCenterActivity.this, LoginActivity.class);
                }
                startActivity(intentPerson);
                break;
            case R.id.tv_setting:
                Intent intentSetting = new Intent();
                if (Utils.isAutoLogin(PersonalCenterActivity.this)){
                    intentSetting.setClass(PersonalCenterActivity.this,PersonSettingActivity.class);
                }else{
                    intentSetting.setClass(PersonalCenterActivity.this, LoginActivity.class);
                }
                startActivity(intentSetting);
                break;
            case R.id.tv_suggestion:
                Intent intentSuggestion = new Intent();
                intentSuggestion.setClass(PersonalCenterActivity.this, PersonSuggestionActivity.class);
                startActivity(intentSuggestion);
                break;
            case R.id.tv_collect:
                Intent intentCollect = new Intent();
                if (Utils.isAutoLogin(PersonalCenterActivity.this)){
                    intentCollect.setClass(PersonalCenterActivity.this,PersonCollectActivity.class);
                    startActivityForResult(intentCollect, 10086);
                    //startActivity(intentCollect);
                }else{
                    intentCollect.setClass(PersonalCenterActivity.this, LoginActivity.class);
                    startActivity(intentCollect);
                }

                break;
            /*case R.id.tv_strategy:
                Intent intentStrategy = new Intent();
                intentStrategy.setClass(PersonalCenterActivity.this,StrategyActivity.class);
                startActivity(intentStrategy);
                break;*/
            case R.id.iv_portrait:
            case R.id.tv_name:
                if(!Utils.isAutoLogin(PersonalCenterActivity.this)){
                    Intent intent = new Intent();
                    intent.setClass(PersonalCenterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_about:
                startActivity(new Intent(PersonalCenterActivity.this,AboutActivity.class));
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10086:
                if(null!=data){
                    int spotId = data.getIntExtra(MainActivity.SPOTS_ID,0);
                    // MainActivity.setSpotId(spotId);
                    finish();
                }

                break;
        }
    }
}
