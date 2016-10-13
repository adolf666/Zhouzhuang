package com.adolf.zhouzhuang.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.adolf.zhouzhuang.Favorites;
import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.databasehelper.FavoriteDataBaseHelper;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.util.Constants;
import com.adolf.zhouzhuang.util.SdCardUtil;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.felipecsl.gifimageview.library.GifImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LaunchActivity extends BaseActivity {
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private FavoriteDataBaseHelper mFavoriteDataBaseHelper;
    private GifImageView mLoadingImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laynch);
        mLoadingImage = (GifImageView) findViewById(R.id.loading_anim);
        /*AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImage.getDrawable();
        animationDrawable.start();*/
        mLoadingImage.setBytes(getBytes());
        mLoadingImage.startAnimation();

        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        mFavoriteDataBaseHelper = new FavoriteDataBaseHelper(getFavoriteDao());
        initFileDir();
        getAllSpots();
        getAllFavorite();
        pageChange();
    }

    private void pageChange() {
        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(),5500);

    }

    class SplashHandler implements Runnable {
        public void run() {
//            dialog.dismiss();
            startActivity(new Intent(LaunchActivity.this, MainActivity.class));
            LaunchActivity.this.finish();
        }
    }

    public void getAllSpots(){
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.ALL_SPOTS, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<Spots> spotsList = GsonUtil.jsonToList(response,"data",Spots.class);
                mSpotsDataBaseHelper.insertAllSpotsList(spotsList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    public void getAllFavorite(){

        if (!Utils.isAutoLogin(this)) {
            return;
        }

        RequestParams params = new RequestParams();
        params.put("userId", SharedPreferencesUtils.getInt(this,"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_LIST,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<Integer> favoriteList = GsonUtil.jsonToList(response,"data",Integer.class);
                setFavoriteSpots(favoriteList, SharedPreferencesUtils.getInt(LaunchActivity.this,"pid"));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void setFavoriteSpots(List<Integer> favoriteList,int userId){
        mFavoriteDataBaseHelper.deleteAll();
        for (int i = 0; i <favoriteList.size() ; i++) {
            Favorites favorite = new Favorites();
            favorite.setUserId(userId);
            favorite.setSpotsId(favoriteList.get(i));
            mFavoriteDataBaseHelper.addFavorite(favorite);
        }
    }

    public void initFileDir(){
        if(SdCardUtil.checkSdCard()==true){
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR);
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILEAUDIO);
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILEAPK);
        }else{
            System.out.println("创建文件夹失败SD卡不可用");
        }
    }

    public  byte[] getBytes() {
        InputStream in  = getResources().openRawResource(R.mipmap.gif);
        try {
            return IOUtils.toByteArray(in);
        } catch (final MalformedURLException e) {

        } catch (final OutOfMemoryError e) {

        } catch (final UnsupportedEncodingException e) {

        } catch (final IOException e) {

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException ignored) {
                }
            }
        }
        return null;
    }
}