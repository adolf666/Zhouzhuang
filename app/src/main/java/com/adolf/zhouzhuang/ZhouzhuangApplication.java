package com.adolf.zhouzhuang;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by adolf on 2016/8/21.
 */
public class ZhouzhuangApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }
}
