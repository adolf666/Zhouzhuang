package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.adolf.zhouzhuang.R;

public class WebViewActivity extends BaseActivity {
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
        initActionBar("返回",0,"","",0);
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
}
