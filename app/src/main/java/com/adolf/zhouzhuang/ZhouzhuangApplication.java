package com.adolf.zhouzhuang;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

import com.adolf.zhouzhuang.util.Constants;
import com.baidu.mapapi.SDKInitializer;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;


/**
 * Created by adolf on 2016/8/21.
 */
public class ZhouzhuangApplication extends Application {
    public DaoSession daoSession;
    public SQLiteDatabase db;
    public DaoMaster.DevOpenHelper helper;
    public DaoMaster daoMaster;
//    private Typeface typeface1;
//    private Typeface typeface2;
//    private Typeface typeface3;
    private static ZhouzhuangApplication _instance;
    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        setupDatabase();
        _instance = (ZhouzhuangApplication) getApplicationContext();
//        typeface1 = getType(_instance, 0);
//        typeface2 = getType(_instance, 1);
//        typeface3 = getType(_instance, 3);

        //初始化过度绘制检测
       // BlockCanary.install(this, new AppBlockCanaryContext()).start();
        //初始化内存泄漏检测
        LeakCanary.install(this);

        //初始化tbs x5 webview
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
            }

            @Override
            public void onViewInitFinished(boolean b) {
            }
        });
    }

    public static  ZhouzhuangApplication getInstace() {
        return _instance;
    }

    private void setupDatabase() {
        helper = new DaoMaster.DevOpenHelper(this, Constants.DB_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }


    public SQLiteDatabase getDb() {
        return db;
    }




//    public static  Typeface getType(Context context,int i){
//        Typeface type = null;
//        switch (i){
//            case 0:
//                type = Typeface.createFromAsset (context.getAssets() , "fonts/FZSQKB.TTF" );
//                break;
//            case 1:
//                type = Typeface.createFromAsset (context.getAssets() , "fonts/FZS1JW.TTF" );
//                break;
//            case  3:
//                type = Typeface.createFromAsset (context.getAssets() , "fonts/FZS3JW.TTF" );
//                break;
//        }
//        return type;
//    }

//    public Typeface getTypeface(int i) {
//        switch (i){
//            case 0:
//                return typeface1;
//            case 1:
//                return typeface2;
//            case 3:
//                return typeface3;
//        }
//        return typeface1;
//    }
}
