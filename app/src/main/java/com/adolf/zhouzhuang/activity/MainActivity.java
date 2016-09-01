package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.adapter.ViewPagerAdapter;
import com.adolf.zhouzhuang.fragment.BaseFragment;
import com.adolf.zhouzhuang.fragment.CollectionFragment;
import com.adolf.zhouzhuang.fragment.GudieFragment;
import com.adolf.zhouzhuang.fragment.MuseumFragment;
import com.adolf.zhouzhuang.fragment.StrategyFragment;
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

        mCustomerViewPager = (CustomViewPager) findViewById(R.id.viewpager);

        mMuseumTextView.setOnClickListener(this);
        mCollectionTextView.setOnClickListener(this);
        mNavigationTextView.setOnClickListener(this);
        mStrategyTextView.setOnClickListener(this);

        initActionBar("个人",0,"周庄博物馆","扫描",0);
        initViewPager();
    }

    private void initViewPager(){
        List<Fragment> fragmentArrayList = new ArrayList<>();
        MuseumFragment museumFragment = new MuseumFragment();
        CollectionFragment collectionFragment = new CollectionFragment();
        GudieFragment gudieFragment = new GudieFragment();
        StrategyFragment strategyFragment = new StrategyFragment();
        fragmentArrayList.add(museumFragment);
        fragmentArrayList.add(collectionFragment);
        fragmentArrayList.add(gudieFragment);
        fragmentArrayList.add(strategyFragment);
        mCustomerViewPager.setScrollble(false);
        mCustomerViewPager.setOffscreenPageLimit(3);
        mCustomerViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_museum:
                mCustomerViewPager.setCurrentItem(0);
                break;
            case R.id.tv_collection:
                mCustomerViewPager.setCurrentItem(1);
                break;
            case R.id.tv_navigation:
                mCustomerViewPager.setCurrentItem(2);
                break;
            case R.id.tv_strategy:
                mCustomerViewPager.setCurrentItem(3);
                break;
            case R.id.tv_left_actionbar:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_rigth_actionbar:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this,QRCodeActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private boolean isLogin(){
        boolean isLogin = false;
        SharedPreferences sharedPreferences= getSharedPreferences("LOGIN",
                Activity.MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isAutoLogin",false);
        return isLogin;
    }
}
