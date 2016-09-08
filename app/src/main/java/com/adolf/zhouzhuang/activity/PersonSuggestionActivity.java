package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.view.View;

import com.adolf.zhouzhuang.R;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class PersonSuggestionActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_suggestion);
        initView();
    }
    private void initView(){
        initActionBar("返回",0,"个人建议","",0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
        }}
}
