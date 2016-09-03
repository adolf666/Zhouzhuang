package com.adolf.zhouzhuang.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.adapter.NewsAdapter;
import com.adolf.zhouzhuang.object.Exhibit;
import com.adolf.zhouzhuang.resBody.ExhibitResponse;
import com.adolf.zhouzhuang.util.ServiceAddress;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class NewsActivity extends BaseActivity {
    public ProgressDialog progressDialog;
    private ListView mNewsLv;
    private List<Exhibit> exhibitList;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initActionBar("返回",0,"新闻中心","",0);
        initViews();
        getNews();
    }
    public void initViews(){
        progressDialog = ProgressDialog.show(this, "正在获取新闻列表", "请稍候...", true, true);
        mNewsLv = (ListView)findViewById(R.id.lv_news);
    }

    public void getNews(){
        RequestParams params = new RequestParams();
        params.addFormDataPart("type","0");
        HttpRequest.post(ServiceAddress.NEWS_EXHIBITION_TEMPORARY,params,new BaseHttpRequestCallback<ExhibitResponse>(){

            @Override
            protected void onSuccess(ExhibitResponse exhibitResponse) {
                super.onSuccess(exhibitResponse);
                setNewsData(exhibitResponse.getData());
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }

    public void setNewsData(List<Exhibit> exhibitList){

        if (adapter == null){
            adapter = new NewsAdapter(NewsActivity.this,exhibitList);
        }else {
            adapter.notifyDataSetChanged();
        }
        mNewsLv.setAdapter(adapter);
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
