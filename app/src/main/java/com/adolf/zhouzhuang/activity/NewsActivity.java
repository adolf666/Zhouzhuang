package com.adolf.zhouzhuang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.adapter.NewsAdapter;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.Exhibit;
import com.adolf.zhouzhuang.resBody.ExhibitResponse;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.widget.LoadingProgressDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewsActivity extends BaseActivity {
    private ListView mNewsLv;
    private List<Exhibit> exhibitList;
    private NewsAdapter adapter;
    RelativeLayout progressLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initActionBar("返回",R.drawable.back_selected,"新闻中心","",0);
        initViews();
        getNews();
    }
    public void initViews(){

        mNewsLv = (ListView)findViewById(R.id.lv_news);
        progressLayout = (RelativeLayout) findViewById(R.id.ll_progress_bar);
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void getNews(){
        RequestParams params = new RequestParams();
        params.add("type","0");
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.NEWS_EXHIBITION_TEMPORARY,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                setNewsData(GsonUtil.jsonToList(response,"data",Exhibit.class));
                progressLayout.setVisibility(View.GONE);
                mNewsLv.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void setNewsData(final List<Exhibit> exhibitList){

        if (adapter == null){
            adapter = new NewsAdapter(NewsActivity.this,exhibitList);
        }else {
            adapter.notifyDataSetChanged();
        }
        mNewsLv.setAdapter(adapter);
        mNewsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent =new Intent();
                intent.setClass(NewsActivity.this,WebViewActivity.class);
                intent.putExtra("URL",exhibitList.get(position).getDetailUrl());
                intent.putExtra(WebViewActivity.NAME,"新闻中心");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
        }

    }
}
