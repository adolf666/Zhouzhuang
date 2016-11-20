package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.util.Utils;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class PersonSuggestionActivity extends BaseActivity{

    private TextView mSubmit;
    private EditText mSuggestEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_suggestion);
        initView();
    }
    private void initView(){
        initActionBar("返回",R.drawable.back_selected,"意见反馈","",0);
        mSubmit = (TextView)findViewById(R.id.tv_submit);
        mSubmit.setOnClickListener(this);
        mSuggestEdit = (EditText)findViewById(R.id. suggest_edit);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.tv_submit:
                if(TextUtils.isEmpty(mSuggestEdit.getText().toString().trim())){
                    Toast.makeText(this,"没有意见可提交，请编辑后再次提交",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }}
}
