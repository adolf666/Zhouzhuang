package com.adolf.zhouzhuang.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.databasehelper.FavoriteDataBaseHelper;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class PersonSettingActivity extends BaseActivity{

    private TextView mModifyPassword;
    private TextView mCleanStore;
    private TextView mLoginOff;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private FavoriteDataBaseHelper mFavoriteDataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        initView();
    }
    private void initView(){
        initActionBar("返回",R.drawable.back_selected,"个人设置","",0);
        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        mFavoriteDataBaseHelper = new FavoriteDataBaseHelper(getFavoriteDao());
        mModifyPassword = (TextView)findViewById(R.id.tv_modify_password);
        mCleanStore = (TextView)findViewById(R.id.clean_store);
        mLoginOff = (TextView)findViewById(R.id.login_off);

//        mCleanStore.setTypeface(Utils.getType(this,3));
//        mLoginOff.setTypeface(Utils.getType(this,3));
        TextView modifyPassword = (TextView)findViewById(R.id.tv_modify_password);
//        modifyPassword.setTypeface(Utils.getType(this,3));
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
            case R.id.tv_modify_password:
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
                Toast.makeText(this,"缓存已清除",Toast.LENGTH_LONG).show();
                break;
            case R.id.login_off:
                dialog();

                break;

        }}

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonSettingActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage("确认注销?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
               SharedPreferencesUtils.putBoolean(PersonSettingActivity.this,"AutoLogin",false);
               SharedPreferencesUtils.getString(PersonSettingActivity.this,"AccountInfo",null);
               mFavoriteDataBaseHelper.deleteAll();
               finish();


              }
           });
         builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
             public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
                 }
         });
        builder.create().show();
    }
}
