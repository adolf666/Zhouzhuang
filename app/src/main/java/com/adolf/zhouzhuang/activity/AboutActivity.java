package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;

/**
 * Created by adolf on 2016/10/23.
 */

public class AboutActivity extends BaseActivity {
    private TextView mVersionTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initActionBar("返回",R.drawable.back_selected,"关于周庄博物馆","",0);
        setVersion();
    }
    public void initView(){
        mVersionTv = (TextView)findViewById(R.id.tv_version);
    }
    public void setVersion(){
        mVersionTv.setText(getVersion());
    }
    public String getVersion() {
            try {
                   PackageManager manager = this.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                    String version = info.versionName;
                    return "版本号: V" + version;
                } catch (Exception e) {
                    e.printStackTrace();
                return "未能获取版本号";
                 }
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
