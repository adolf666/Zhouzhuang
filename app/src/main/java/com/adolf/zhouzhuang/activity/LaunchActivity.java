package com.adolf.zhouzhuang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.util.SdCardUtil;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LaunchActivity extends BaseActivity {
//    private ProgressDialog dialog;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laynch);
        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        initFileDir();
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
        if (mSpotsDataBaseHelper.getSpotsCount() > 0){
            return;
        }
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.ALL_SPOTS, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<Spots> spotsList = GsonUtil.jsonToList(response,"data",Spots.class);
                mSpotsDataBaseHelper.insertAllSpotsList(spotsList);
                getAllFavorite();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    public void getAllFavorite(){
        RequestParams params = new RequestParams();
        params.put("userId", SharedPreferencesUtils.getInt(this,"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_LIST,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<Integer> favoriteList = GsonUtil.jsonToList(response,"data",Integer.class);
                setFavoriteSpots(favoriteList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void setFavoriteSpots(List<Integer> favoriteList){
        List<Spots> favoriteSpotsList = getSpotsListFromIdList(favoriteList);
        for (int i = 0; i < favoriteSpotsList.size(); i++) {
            Spots spots = favoriteSpotsList.get(i);
            spots.setIsFavorite(true);
            mSpotsDataBaseHelper.updateSpots(spots);
        }
    }

    private List<Spots> getSpotsListFromIdList(List<Integer> spots){
        List<Spots> spotsList = new ArrayList<>();
        for (int i = 0; i < spots.size(); i++) {
            spotsList.add(mSpotsDataBaseHelper.getSpotsById(spots.get(i)));
        }
        return spotsList;
    }

    public void initFileDir(){
        if(SdCardUtil.checkSdCard()==true){
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR);
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILEAUDIO);
        }else{
            System.out.println("创建文件夹失败SD卡不可用");
        }

    }
}