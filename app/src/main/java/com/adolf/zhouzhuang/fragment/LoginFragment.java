package com.adolf.zhouzhuang.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.MainActivity;
import com.adolf.zhouzhuang.activity.PersonCollectActivity;
import com.adolf.zhouzhuang.activity.PersonSettingActivity;
import com.adolf.zhouzhuang.activity.PersonSuggestionActivity;
import com.adolf.zhouzhuang.activity.PersonalInfoActivity;
import com.adolf.zhouzhuang.activity.RegisterActivity;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gpp on 2016/9/19 0019.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener{

    //*********************************登录相关******************************
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mLoginBT;
    public ProgressDialog progressDialog;

    //********************************个人中心相关****************************
    private TextView  mPersonInfo;
    private TextView  mSuggestion;
    private TextView  mSetting;
    private TextView  mCollect;
    private TextView  mUserName;
    private LinearLayout mLoginLinearLayout;
    private LinearLayout mPersonalCenterinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_login, container, false);
        initViewLogin(view);
        initViewPersonalCenter(view);
        updateInterface(Utils.isAutoLogin(getActivity()));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewLogin(View view){
        mPersonalCenterinearLayout = (LinearLayout) view.findViewById(R.id.ll_personal_center);
        mLoginLinearLayout = (LinearLayout) view.findViewById(R.id.ll_login);
        mUsernameET = (EditText)view.findViewById(R.id.et_username);
        mPasswordET = (EditText) view.findViewById(R.id.et_password);
        mLoginBT = (Button) view.findViewById(R.id.bt_login);
        TextView user_name = (TextView)view.findViewById(R.id.tv_user_name);
        TextView password = (TextView)view.findViewById(R.id.tv_password);
        mLoginBT.setTypeface(Utils.getType(getActivity(),0));
        user_name.setTypeface(Utils.getType(getActivity(),3));
        password.setTypeface(Utils.getType(getActivity(),3));
        mLoginBT.setOnClickListener(this);
    }

    private void initViewPersonalCenter(View view){
        mUserName = (TextView)view.findViewById(R.id.tv_name);
        mPersonInfo = (TextView ) view.findViewById(R.id.tv_person_info);
        mCollect = (TextView)view.findViewById(R.id.tv_collect);
        mSetting = (TextView ) view.findViewById(R.id.tv_setting);
        mSuggestion = (TextView) view.findViewById(R.id.tv_suggestion);
        TextView mStrategy = (TextView)view.findViewById(R.id.tv_strategy);
        mUserName.setTypeface(Utils.getType(getActivity(),3));
        mPersonInfo.setTypeface(Utils.getType(getActivity(),3));
        mCollect.setTypeface(Utils.getType(getActivity(),3));
        mSetting.setTypeface(Utils.getType(getActivity(),3));
        mSuggestion.setTypeface(Utils.getType(getActivity(),3));
        mStrategy.setTypeface(Utils.getType(getActivity(),3));

        mPersonInfo.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
        mCollect.setOnClickListener(this);
    }

    private void setPersonalInfo(){
        Object object = SharedPreferencesUtils.readObject(getActivity(),"AccountInfo");
        LoginObj obj = (LoginObj)object;
        mUserName.setText(obj.getUsername());
    }

    public void updateInterface(boolean isLogin){
        Utils.displayOrHideIM(getActivity());
        if (isLogin){
            mLoginLinearLayout.setVisibility(View.GONE);
            mPersonalCenterinearLayout.setVisibility(View.VISIBLE);
            setPersonalInfo();
        }else {
            mLoginLinearLayout.setVisibility(View.VISIBLE);
            mPersonalCenterinearLayout.setVisibility(View.GONE);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                loginNew();
                break;
            case R.id.tv_person_info:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), PersonalInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_setting:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), PersonSettingActivity.class);
                startActivity(intent2);

                break;
            case R.id.tv_suggestion:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), PersonSuggestionActivity.class);
                startActivity(intent3);
                break;

            case R.id.tv_collect:
                Intent intent4 = new Intent();
                intent4.setClass(getActivity(), PersonCollectActivity.class);
                startActivity(intent4);
                break;
        }
    }

    public void loginNew(){
        progressDialog = ProgressDialog.show(getActivity(), "", "正在登录...", true, true);
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
                SharedPreferencesUtils.putBoolean(getActivity(),"AutoLogin",true);
                SharedPreferencesUtils.saveObject(getActivity(),"AccountInfo", GsonUtil.jsonToBean(response,"data",LoginObj.class));
                updateInterface(SharedPreferencesUtils.getBoolean(getActivity(),"AutoLogin",false));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                SharedPreferencesUtils.putBoolean(getActivity(),"AutoLogin",false);
                Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
