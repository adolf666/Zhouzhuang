package com.adolf.zhouzhuang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.adapter.StrategyAdapter;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.interfaces.OnRefreshListener;
import com.adolf.zhouzhuang.object.StrategyObject;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.widget.PullToRefreshLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/9/25.
 */
public class StrategyActivity extends BaseActivity  {


    private ListView mListView;
    private StrategyAdapter mAdapter;
    private List<StrategyObject> strategyObjectArrayList;
   // PullToRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strategy);
        initView();
        initData();
    }

    private void initView() {
        initActionBar("返回", R.drawable.back_selected, "游玩攻略", "", 0);
      //  refreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
      //  refreshLayout.setOnRefreshListener(this);
        mListView = (ListView) findViewById(R.id.lv_list_view);

    }

    private void initData() {
        RequestParams params = new RequestParams();
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.GET_STRATEGY, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                strategyObjectArrayList = GsonUtil.jsonToList(response, "data", StrategyObject.class);
                setStrategyData(strategyObjectArrayList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void setStrategyData(List<StrategyObject> strategyObjectList) {
        mAdapter = new StrategyAdapter(this, strategyObjectList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(StrategyActivity.this, WebViewActivity.class);
                intent.putExtra("URL", strategyObjectArrayList.get(position).getUrl());
                intent.putExtra(WebViewActivity.NAME, "游玩攻略");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }

   /* @Override
    public void onRefresh() {

        RequestParams params = new RequestParams();
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.GET_STRATEGY, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                strategyObjectArrayList = GsonUtil.jsonToList(response, "data", StrategyObject.class);
                setStrategyData(strategyObjectArrayList);
                refreshLayout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }*/
}
