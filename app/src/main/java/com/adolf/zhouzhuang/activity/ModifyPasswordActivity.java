package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.adolf.zhouzhuang.widget.LoadingProgressDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

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
        initActionBar("返回",R.drawable.back_selected,"修改密码","",0);
        mOldPassword = (EditText)findViewById(R.id.edit_old_password);
        mNewPassword = (EditText)findViewById(R.id.edit_new_password);
        mConfirmPassword = (EditText)findViewById(R.id.edit_confirm_password);
        mBtnSave = (TextView)findViewById(R.id.btn_save);

        TextView mOlaPassword = (TextView)findViewById(R.id.tv_old_password);
        TextView mNewPassword = (TextView)findViewById(R.id.tv_new_password);
        TextView mConfirmPassword = (TextView)findViewById(R.id.tv_confirm_password);

//        mOlaPassword.setTypeface(Utils.getType(this,3));
//        mNewPassword .setTypeface(Utils.getType(this,3));
//        mConfirmPassword.setTypeface(Utils.getType(this,3));
//        mBtnSave.setTypeface(Utils.getType(this,0));
        mBtnSave.setOnClickListener(this);
     }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.btn_save:
                if( mOldPassword.getText().toString().isEmpty()){
                    Toast.makeText(ModifyPasswordActivity.this,"您输入的的旧密码为空",Toast.LENGTH_SHORT).show();
                }else if(!mNewPassword.getText().toString().isEmpty()&&!mConfirmPassword.getText().toString().isEmpty()
                        &&mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())){
                    RequestParams params = new RequestParams();
                    LoginObj obj= GsonUtil.jsonToBean(SharedPreferencesUtils.getString(this, "AccountInfo",""),LoginObj.class);
                    if (obj.getUsername() != null) {
                        params.put("pid",obj.getPid());
                    }
                    params.put("oldPassword", mOldPassword.getText().toString());
                    params.put("newPassword", mNewPassword.getText().toString());
                    progressDialog = new LoadingProgressDialog(this,"正在保存");
                    progressDialog.show();
                    AsyncHttpClientUtils.getInstance().get(ServiceAddress.UPDATE_PASSWORD,params,new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            progressDialog.dismiss();
                            Toast.makeText(ModifyPasswordActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                           finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Toast.makeText(ModifyPasswordActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
                           progressDialog.dismiss();
                        }

                    });


                }else if(mNewPassword.getText().toString().isEmpty()){
                    Toast.makeText(ModifyPasswordActivity.this,"您输入新密码为空！",Toast.LENGTH_SHORT).show();
                }else if(mConfirmPassword.getText().toString().isEmpty()){
                    Toast.makeText(ModifyPasswordActivity.this,"您输入确认密码为空！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ModifyPasswordActivity.this,"新密码和确认密码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
                }

                break;
        }}
}
