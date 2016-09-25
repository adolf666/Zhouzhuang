package com.adolf.zhouzhuang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.adolf.zhouzhuang.R;

public class WebViewActivity extends BaseActivity implements View.OnClickListener{
    private WebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initViews();
        initBundle();
        setWebViewInfo();
    }
    public void initViews(){
        mWebView = (WebView)findViewById(R.id.webview);
        initActionBar("返回",R.drawable.back_selected,"苏州乐园","",0);
    }
    public void initBundle(){
        mUrl = getIntent().getStringExtra("URL");
    }
    public void setWebViewInfo(){
        initWebViewSetting();
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void initWebViewSetting(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        // 全屏显示
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }
}
