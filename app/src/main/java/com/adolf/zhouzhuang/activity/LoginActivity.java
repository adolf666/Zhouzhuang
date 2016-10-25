package com.adolf.zhouzhuang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.Favorites;
import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.ZhouzhuangApplication;
import com.adolf.zhouzhuang.databasehelper.FavoriteDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.Constants;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.adolf.zhouzhuang.widget.LoadingProgressDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity{
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mLoginBT;
    private int mGoToActivity = -1;
    private FavoriteDataBaseHelper mFavoriteDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initBundle();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUsernameET.setText("");
        mPasswordET.setText("");
    }

    private void initView(){
        mUsernameET = (EditText) findViewById(R.id.et_username);
        mPasswordET = (EditText) findViewById(R.id.et_password);
        mLoginBT = (Button) findViewById(R.id.bt_login);
        TextView user_name = (TextView)findViewById(R.id.tv_user_name);
        TextView password = (TextView)findViewById(R.id.tv_password);
//        mLoginBT.setTypeface(Utils.getType(this,0));
//        user_name.setTypeface(Utils.getType(this,3));
//        password.setTypeface(Utils.getType(this,3));
        mLoginBT.setOnClickListener(this);
        initActionBar("返回",R.drawable.back_selected,"登录","注册",0);
        ((ZhouzhuangApplication)getApplication()).getDaoSession();
        mFavoriteDataBaseHelper = new FavoriteDataBaseHelper(getFavoriteDao());
    }

    private void initBundle(){
        mGoToActivity = getIntent().getIntExtra("GOTO_ACTIVITY",-1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                loginNew();
                break;
            case R.id.tv_rigth_actionbar:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }

    public void loginNew(){
        progressDialog = new LoadingProgressDialog(this,"正在登录...");
        progressDialog.show();
        RequestParams params = new RequestParams();
        String userName = mUsernameET.getText().toString();
        String passWord = mPasswordET.getText().toString();
        params.add("username", userName);
        params.add("password", passWord);
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.LOGIN,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                LoginObj loginObj = GsonUtil.jsonToBean(response,"data",LoginObj.class);
                String message  = GsonUtil.getExtendJson(response,"message");
               if(null!=loginObj){
                   SharedPreferencesUtils.putBoolean(LoginActivity.this,"AutoLogin",true);
                   SharedPreferencesUtils.putString(LoginActivity.this,"AccountInfo", GsonUtil.oBjToJson(loginObj));
                   SharedPreferencesUtils.putInt(LoginActivity.this,"pid",loginObj.getPid());
                }
                getAllFavorite();
                Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                SharedPreferencesUtils.putBoolean(LoginActivity.this,"AutoLogin",false);
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToActivity(){
        Intent intent = new Intent();
        switch (mGoToActivity){
            case Constants.PERSONAL_INFO_PAGE:
                intent.setClass(LoginActivity.this,PersonalInfoActivity.class);
                break;
            case Constants.SETTING_PAGE:
                intent.setClass(LoginActivity.this,PersonalInfoActivity.class);
                break;
            case Constants.COLLECTION_LIST_PAGE:

                break;
            default:
                break;
        }
        startActivity(intent);
        finish();
    }


    public void getAllFavorite(){

        if (!Utils.isAutoLogin(this)) {
            return;
        }

        RequestParams params = new RequestParams();
        params.put("userId", SharedPreferencesUtils.getInt(this,"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_LIST,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<Integer> favoriteList = GsonUtil.jsonToList(response,"data",Integer.class);
                setFavoriteSpots(favoriteList, SharedPreferencesUtils.getInt(LoginActivity.this,"pid"));
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void setFavoriteSpots(List<Integer> favoriteList,int userId){
        mFavoriteDataBaseHelper.deleteAll();
        for (int i = 0; i <favoriteList.size() ; i++) {
            Favorites favorite = new Favorites();
            favorite.setUserId(userId);
            favorite.setSpotsId(favoriteList.get(i));
            mFavoriteDataBaseHelper.addFavorite(favorite);
        }
    }
}
