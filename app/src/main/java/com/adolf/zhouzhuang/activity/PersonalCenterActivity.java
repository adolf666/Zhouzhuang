package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;

public class PersonalCenterActivity extends Activity implements View.OnClickListener{
    private TextView mLeftActionbarTV;
    private TextView mMiddleActionbarTV;
    private TextView mRightActionbarTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
    }
    private void initView(){
        mLeftActionbarTV = (TextView) findViewById(R.id.tv_left_actionbar);
        mMiddleActionbarTV = (TextView) findViewById(R.id.tv_middle_actionbar);
        mRightActionbarTV = (TextView) findViewById(R.id.tv_rigth_actionbar);
        initActionBar();
    }
    private void initActionBar(){
        mLeftActionbarTV.setText("返回");
        mMiddleActionbarTV.setText("个人中心");
        mRightActionbarTV.setText("");
        mLeftActionbarTV.setOnClickListener(this);
        mRightActionbarTV.setOnClickListener(this);
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
