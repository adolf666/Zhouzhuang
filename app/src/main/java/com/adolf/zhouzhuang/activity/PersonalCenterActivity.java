package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;

public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
    }
    private void initView(){
        initActionBar("返回",0,"个人中心","",0);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }
}
