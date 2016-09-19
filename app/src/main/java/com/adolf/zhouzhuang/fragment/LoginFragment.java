package com.adolf.zhouzhuang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.MainActivity;
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

    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mLoginBT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_login, container, false);
        initView(view);
        return view;
    }
    private void initView(View view){
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                loginNew();
                break;
            case R.id.tv_rigth_actionbar:
                startActivity(new Intent(getActivity(),RegisterActivity.class));
                break;
    }
    }

    public void loginNew(){
        RequestParams params = new RequestParams();
        String userName = mUsernameET.getText().toString();
        String passWord = mPasswordET.getText().toString();
        params.add("username", userName);
        params.add("password", passWord);
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.LOGIN,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SharedPreferencesUtils.putBoolean(getActivity(),"AutoLogin",true);
                SharedPreferencesUtils.saveObject(getActivity(),"AccountInfo", GsonUtil.jsonToBean(response,"data",LoginObj.class));
                startActivity(new Intent(getActivity(),MainActivity.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                SharedPreferencesUtils.putBoolean(getActivity(),"AutoLogin",false);
                Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
