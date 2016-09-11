package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class ModifyPasswordActivity extends BaseActivity {


    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private TextView mBtnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initView();
    }
    private void initView(){
        initActionBar("返回",0,"修改个人信息","",0);
        mOldPassword = (EditText)findViewById(R.id.edit_old_password);
        mNewPassword = (EditText)findViewById(R.id.edit_new_password);
        mConfirmPassword = (EditText)findViewById(R.id.edit_confirm_password);
        mBtnSave = (TextView)findViewById(R.id.btn_save);
        mBtnSave.setOnClickListener(this);
     }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.btn_save:
                if(mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())){
                    Toast.makeText(ModifyPasswordActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else {

                    Toast.makeText(ModifyPasswordActivity.this,"新密码和确认密码不一致",Toast.LENGTH_SHORT).show();
                }

                break;
        }}
}
