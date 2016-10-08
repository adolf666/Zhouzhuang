package com.adolf.zhouzhuang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;

public class WebViewActivity extends BaseActivity implements View.OnClickListener{
    public static final String  NAME = "name";
    private WebView mWebView;
    private String mUrl;
    private int mSpotId;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private String mTitle="";
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
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
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