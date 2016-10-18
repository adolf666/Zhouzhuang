package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.PersonCollectActivity;
import com.adolf.zhouzhuang.activity.PersonSettingActivity;
import com.adolf.zhouzhuang.activity.PersonSuggestionActivity;
import com.adolf.zhouzhuang.activity.PersonalInfoActivity;
import com.adolf.zhouzhuang.activity.StrategyActivity;
import com.adolf.zhouzhuang.activity.WebViewActivity;
import com.adolf.zhouzhuang.adapter.ExhibitAdapter;
import com.adolf.zhouzhuang.adapter.NewsAdapter;
import com.adolf.zhouzhuang.adapter.StrategyAdapter;
import com.adolf.zhouzhuang.adapter.StrategyPagerAdapter;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.object.StrategyObject;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class StrategyFragment extends BaseFragment implements View.OnClickListener{
    private ListView mListView;
    private StrategyAdapter mAdapter;
    private List<View> mViewPagerView;
    private ViewPager viewPager;
    private StrategyPagerAdapter mExhibitAdapter;
    private List<StrategyObject> strategyObjectArrayList;
    private TextView mStrategy,mGuestRoom,mRestaurant,mAdmissionTicket;

    public StrategyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_strategy, container, false);
        initViews(view);
        initData(0);
        initData(1);
        initData(2);
        initData(3);
        return view;
    }
    private void initViews(View view){
        mViewPagerView = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mViewPagerView.add(new View(getActivity()));
        }
        viewPager = (ViewPager) view.findViewById(R.id.vPager);
        mStrategy = (TextView) view.findViewById(R.id.tv_strategy);
        mGuestRoom = (TextView) view.findViewById(R.id.tv_guest_room);
        mRestaurant = (TextView) view.findViewById(R.id.tv_restaurant);
        mAdmissionTicket = (TextView)view.findViewById(R.id.tv_admission_ticket);

        mStrategy.setOnClickListener(this);
        mGuestRoom .setOnClickListener(this);
        mRestaurant.setOnClickListener(this);
        mAdmissionTicket.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                tabSwitch( position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }





    private void initData(final int index) {
        RequestParams params = new RequestParams();
        params.add("type", index+"");

        AsyncHttpClientUtils.getInstance().get(ServiceAddress.GET_STRATEGY, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                strategyObjectArrayList = GsonUtil.jsonToList(response, "data", StrategyObject.class);
                setStrategyData(strategyObjectArrayList,index);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void setStrategyData(List<StrategyObject> strategyObjectList,int index) {

        ListView mListView = new ListView(getActivity());
        mAdapter = new StrategyAdapter(getActivity(), strategyObjectList);
        mListView.setAdapter(mAdapter);
        mListView.setSelector(R.color.bg_white);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("URL", strategyObjectArrayList.get(position).getUrl());
                intent.putExtra(WebViewActivity.NAME, "游玩攻略");
                startActivity(intent);
            }
        });
        mViewPagerView.set(index,mListView);

        if (mExhibitAdapter == null){
            mExhibitAdapter = new StrategyPagerAdapter(mViewPagerView,getActivity());
            viewPager.setAdapter(mExhibitAdapter);
        }else{
            mExhibitAdapter.notifyDataSetChanged();
        }
    }
    private  void tabSwitch(int position){
        mStrategy.setBackground(position==0?getResources().getDrawable(R.mipmap.btn_strategymenu01_focus):getResources().getDrawable(R.mipmap.btn_strategymenu01_default));
        mGuestRoom.setBackground(position==1?getResources().getDrawable(R.mipmap.btn_strategymenu02_focus):getResources().getDrawable(R.mipmap.btn_strategymenu02_default));
        mRestaurant.setBackground(position==2?getResources().getDrawable(R.mipmap.btn_strategymenu03_focus):getResources().getDrawable(R.mipmap.btn_strategymenu03_default));
        mAdmissionTicket.setBackground(position==3?getResources().getDrawable(R.mipmap.btn_strategymenu04_focus):getResources().getDrawable(R.mipmap.btn_strategymenu04_default));  }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_strategy:
                viewPager.setCurrentItem(0);
                tabSwitch(0);
                break;
            case R.id.tv_guest_room:
                viewPager.setCurrentItem(1);
                tabSwitch(1);
                break;
            case R.id.tv_restaurant:
                viewPager.setCurrentItem(2);
                tabSwitch(2);
                break;
            case R.id.tv_admission_ticket:
                viewPager.setCurrentItem(3);
                tabSwitch(3);
                break;

        }
    }
}
