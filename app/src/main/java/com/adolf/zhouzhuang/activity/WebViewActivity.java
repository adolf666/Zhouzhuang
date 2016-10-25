package com.adolf.zhouzhuang.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;


import android.widget.RelativeLayout;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class WebViewActivity extends BaseActivity implements View.OnClickListener{
    public static final String  NAME = "name";
    private WebView mWebView;
    private String mUrl;
    private int mSpotId;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private String mTitle="";
    private RelativeLayout mProgressLayout;
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
        mProgressLayout = (RelativeLayout) findViewById(R.id.ll_progress_bar);
        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
    }
    public void initBundle(){
        mSpotId = getIntent().getIntExtra("SpotsId",0);
        mUrl = getIntent().getStringExtra("URL");
        mTitle = getIntent().getStringExtra("name");
        if(mSpotId != 0){
            initActionBar("返回",R.drawable.back_selected,mSpotsDataBaseHelper.getSpotsById(mSpotId).getTitle(),"",0);
        }else if(null!=mTitle&&!mTitle.isEmpty()){
            initActionBar("返回", R.drawable.back_selected, mTitle, "", 0);
        } else  {
            initActionBar("返回", R.drawable.back_selected, "苏州乐园", "", 0);
        }
    }
    public void setWebViewInfo(){
        initWebViewSetting();
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebView.setVisibility(View.VISIBLE);
                mProgressLayout.setVisibility(View.GONE);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        mWebView.loadUrl(mUrl);
    }
    public void loadAssetsFiles(String url){
        mWebView.loadUrl(url);
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

       mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//默认使用缓存
        mWebView.getSettings().setAppCacheMaxSize(8*1024*1024);//缓存最多可以有8M
        mWebView.getSettings().setAllowFileAccess(true);//可以读取文件缓存(manifest生效)
        mWebView.getSettings().setAppCacheEnabled(true);//应用可以有缓存
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onPause ()
    {
        mWebView.reload ();

        super.onPause ();
    }
}