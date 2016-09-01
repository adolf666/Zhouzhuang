package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.object.SpotsObj;
import com.adolf.zhouzhuang.resBody.SpotsResponse;
import com.adolf.zhouzhuang.util.ServiceAddress;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;

public class LaunchActivity extends Activity {
//    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laynch);
        getAllSpots();
//        dialog = new ProgressDialog(this);
//
//        dialog.setIndeterminate(true);
//        dialog.show();
//        dialog.setContentView(R.layout.item_progressdialog);
        pageChange();
    }

    private void pageChange() {
        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(),3000);

    }

    class SplashHandler implements Runnable {
        public void run() {
//            dialog.dismiss();
            startActivity(new Intent(LaunchActivity.this, MainActivity.class));
            LaunchActivity.this.finish();
        }
    }

    public void getAllSpots(){
        HttpRequest.get(ServiceAddress.ALL_SPOTS, new BaseHttpRequestCallback<SpotsResponse>(){

            @Override
            protected void onSuccess(SpotsResponse spotsResponse) {
                super.onSuccess(spotsResponse);
                List<SpotsObj> spotsList = spotsResponse.getData();
                spotsList.size();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }
}