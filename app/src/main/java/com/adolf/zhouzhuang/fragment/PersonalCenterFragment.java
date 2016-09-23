package com.adolf.zhouzhuang.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.LoginActivity;
import com.adolf.zhouzhuang.activity.MainActivity;
import com.adolf.zhouzhuang.activity.PersonCollectActivity;
import com.adolf.zhouzhuang.activity.PersonSettingActivity;
import com.adolf.zhouzhuang.activity.PersonSuggestionActivity;
import com.adolf.zhouzhuang.activity.PersonalInfoActivity;
import com.adolf.zhouzhuang.activity.RegisterActivity;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.Constants;
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
public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener{


    //********************************个人中心相关****************************
    private TextView  mPersonInfo;
    private TextView  mSuggestion;
    private TextView  mSetting;
    private TextView  mCollect;
    private TextView  mUserName;
    private TextView  mStrategy;
    private ImageView mPortrait;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_personal_center, container, false);
        initViewPersonalCenter(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    private void initViewPersonalCenter(View view){
        mPortrait = (ImageView)view.findViewById(R.id.iv_portrait);
        mUserName = (TextView)view.findViewById(R.id.tv_name);
        mPersonInfo = (TextView ) view.findViewById(R.id.tv_person_info);
        mCollect = (TextView)view.findViewById(R.id.tv_collect);
        mSetting = (TextView ) view.findViewById(R.id.tv_setting);
        mSuggestion = (TextView) view.findViewById(R.id.tv_suggestion);
        mStrategy = (TextView)view.findViewById(R.id.tv_strategy);
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
        mPersonInfo.setVisibility(View.VISIBLE);
    }

    private void isLoginFirstAndGoToActivity(Class<?> classToGo,int flagToGo){
        Intent intent = new Intent();
        if (Utils.isAutoLogin(getActivity())){
            intent.setClass(getActivity(),classToGo);
        }else{
            intent.setClass(getActivity(), LoginActivity.class);
            intent.putExtra("GOTO_ACTIVITY",flagToGo);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_person_info:
                Intent intentPerson = new Intent();
                if (Utils.isAutoLogin(getActivity())){
                    intentPerson.setClass(getActivity(),PersonalInfoActivity.class);
                    startActivity(intentPerson);
                }else{
                    intentPerson.setClass(getActivity(), LoginActivity.class);
                }
                startActivity(intentPerson);
                break;
            case R.id.tv_setting:
                isLoginFirstAndGoToActivity(PersonSettingActivity.class,Constants.SETTING_PAGE);
                break;
            case R.id.tv_suggestion:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), PersonSuggestionActivity.class);
                startActivity(intent3);
                break;
            case R.id.tv_collect:
                isLoginFirstAndGoToActivity(PersonCollectActivity.class,Constants.COLLECTION_LIST_PAGE);
                break;
            case R.id.iv_portrait:
            case R.id.tv_name:
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isAutoLogin(getActivity())){
            setPersonalInfo();
        }else {
            mUserName.setOnClickListener(this);
            mPortrait.setOnClickListener(this);
            mPersonInfo.setVisibility(View.GONE);
            mUserName.setText("登录/注册");
        }
    }
}
