package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.adapter.ViewPagerAdapter;
import com.adolf.zhouzhuang.fragment.BaseFragment;
import com.adolf.zhouzhuang.fragment.CollectionFragment;
import com.adolf.zhouzhuang.fragment.GudieFragment;
import com.adolf.zhouzhuang.fragment.LoginFragment;
import com.adolf.zhouzhuang.fragment.MuseumFragment;
import com.adolf.zhouzhuang.fragment.StrategyFragment;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.adolf.zhouzhuang.widget.CustomViewPager;
import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener,BaseFragment.OnFragmentInteractionListener {

    private TextView mMuseumTextView;
    private TextView mCollectionTextView;
    private TextView mNavigationTextView;
    private TextView mStrategyTextView;

    private CustomViewPager mCustomerViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    private void initViews(){
        mMuseumTextView = (TextView) findViewById(R.id.tv_museum);
        mCollectionTextView = (TextView) findViewById(R.id.tv_collection);
        mNavigationTextView = (TextView) findViewById(R.id.tv_navigation);
        mStrategyTextView = (TextView) findViewById(R.id.tv_strategy);
        //Typeface type = Typeface.createFromAsset (getAssets() , "fonts/FZSQKB.TTF" );
        mMuseumTextView.setTypeface(Utils.getType(this,0));
        mCollectionTextView.setTypeface(Utils.getType(this,0));
        mNavigationTextView.setTypeface(Utils.getType(this,0));
        mStrategyTextView.setTypeface(Utils.getType(this,0));
        mCustomerViewPager = (CustomViewPager) findViewById(R.id.viewpager);

        mMuseumTextView.setOnClickListener(this);
        mCollectionTextView.setOnClickListener(this);
        mNavigationTextView.setOnClickListener(this);
        mStrategyTextView.setOnClickListener(this);

        initActionBar("",R.drawable.personal_center_selector,"周庄博物馆","",R.drawable.scan_selector);
        initViewPager();
    }

    private void initViewPager(){
        List<Fragment> fragmentArrayList = new ArrayList<>();
        MuseumFragment museumFragment = new MuseumFragment();
        CollectionFragment collectionFragment = new CollectionFragment();
        GudieFragment gudieFragment = new GudieFragment();
        StrategyFragment strategyFragment = new StrategyFragment();
        LoginFragment loginFragment = new LoginFragment();
        fragmentArrayList.add(museumFragment);
        fragmentArrayList.add(collectionFragment);
        fragmentArrayList.add(gudieFragment);
        boolean isAutoLogin = SharedPreferencesUtils.getBoolean(MainActivity.this,"AutoLogin",false);
        if(isAutoLogin){
            fragmentArrayList.add(strategyFragment);
        }else {
            fragmentArrayList.add(loginFragment);
        }

        mCustomerViewPager.setScrollble(false);
        mCustomerViewPager.setOffscreenPageLimit(3);
        mCustomerViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_museum:
                setBottomBarBackground(0);
                break;
            case R.id.tv_collection:
                setBottomBarBackground(1);
                break;
            case R.id.tv_navigation:
                setBottomBarBackground(2);

                break;
            case R.id.tv_strategy:
                setBottomBarBackground(3);
                break;
            case R.id.tv_left_actionbar:
                autoLoginLogic();
                break;
            case R.id.tv_rigth_actionbar:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this,QRCodeActivity.class);
                startActivity(intent2);
                break;
        }
    }

    public void autoLoginLogic(){
        boolean isAutoLogin = SharedPreferencesUtils.getBoolean(MainActivity.this,"AutoLogin",false);
        if (!isAutoLogin){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }else{
            startActivity(new Intent(MainActivity.this,PersonalCenterActivity.class));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (TextUtils.equals(uri.toString(),"exhibit")){
            mCustomerViewPager.setCurrentItem(1);
        }
    }

    public void setBottomBarBackground(int selectedIndex){
        mCustomerViewPager.setCurrentItem(selectedIndex);
        Drawable museumDefault = getResources().getDrawable(R.mipmap.btn_menu01_default);
        Drawable museumFocus = getResources().getDrawable(R.mipmap.btn_menu01_focused);
        Drawable collectionDefault = getResources().getDrawable(R.mipmap.btn_menu02_default);
        Drawable collectionFocus = getResources().getDrawable(R.mipmap.btn_menu02_focused);
        Drawable guideDefault = getResources().getDrawable(R.mipmap.btn_menu03_default);
        Drawable guideFocus = getResources().getDrawable(R.mipmap.btn_menu03_focused);
        Drawable strategyDefault = getResources().getDrawable(R.mipmap.btn_menu04_default);
        Drawable strategyFocus = getResources().getDrawable(R.mipmap.btn_menu04_focused);
        String textColorDefault = "#8e8e99";
        String textColorFocus = "#333240";

        switch (selectedIndex){
            case 0:
                mMuseumTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,museumFocus,null, null);
                mCollectionTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,collectionDefault,null, null);
                mNavigationTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,guideDefault,null, null);
                mStrategyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,strategyDefault,null, null);
                mMuseumTextView.setTextColor(Color.parseColor(textColorFocus));
                mCollectionTextView.setTextColor(Color.parseColor(textColorDefault));
                mNavigationTextView.setTextColor(Color.parseColor(textColorDefault));
                mStrategyTextView.setTextColor(Color.parseColor(textColorDefault));
                break;
            case 1:
                mMuseumTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,museumDefault,null, null);
                mCollectionTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,collectionFocus,null, null);
                mNavigationTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,guideDefault,null, null);
                mStrategyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,strategyDefault,null, null);
                mMuseumTextView.setTextColor(Color.parseColor(textColorDefault));
                mCollectionTextView.setTextColor(Color.parseColor(textColorFocus));
                mNavigationTextView.setTextColor(Color.parseColor(textColorDefault));
                mStrategyTextView.setTextColor(Color.parseColor(textColorDefault));
                break;
            case 2:
                mMuseumTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,museumDefault,null, null);
                mCollectionTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,collectionDefault,null, null);
                mNavigationTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,guideFocus,null, null);
                mStrategyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,strategyDefault,null, null);
                mMuseumTextView.setTextColor(Color.parseColor(textColorDefault));
                mCollectionTextView.setTextColor(Color.parseColor(textColorDefault));
                mNavigationTextView.setTextColor(Color.parseColor(textColorFocus));
                mStrategyTextView.setTextColor(Color.parseColor(textColorDefault));
                break;
            case 3:
                mMuseumTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,museumDefault,null, null);
                mCollectionTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,collectionDefault,null, null);
                mNavigationTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,guideDefault,null, null);
                mStrategyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,strategyFocus,null, null);
                mMuseumTextView.setTextColor(Color.parseColor(textColorDefault));
                mCollectionTextView.setTextColor(Color.parseColor(textColorDefault));
                mNavigationTextView.setTextColor(Color.parseColor(textColorDefault));
                mStrategyTextView.setTextColor(Color.parseColor(textColorFocus));
                break;
        }

    }
}
