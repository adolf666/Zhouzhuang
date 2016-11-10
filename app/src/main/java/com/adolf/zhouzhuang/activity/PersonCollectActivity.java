package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adolf.zhouzhuang.Favorites;
import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.adapter.PersonalCollectAdapter;
import com.adolf.zhouzhuang.databasehelper.FavoriteDataBaseHelper;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.interfaces.AdapterOnClickListener;
import com.adolf.zhouzhuang.util.Constants;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/9/15.
 */
public class PersonCollectActivity extends BaseActivity {

    private ListView mListview;
    private PersonalCollectAdapter mAdapter;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private FavoriteDataBaseHelper mFavoriteDataBaseHelper;
    private RelativeLayout progressLayout;
    private LinearLayout mErrorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_collect);
        initView();
        initData();
    }
    private void initView(){
        initActionBar("返回", R.drawable.back_selected, "我的收藏", "", 0);
        mListview = (ListView)findViewById(R.id.lv_list_view);
        progressLayout = (RelativeLayout)findViewById(R.id.ll_progress_bar);
        progressLayout.setVisibility(View.VISIBLE);
        mErrorLayout = (LinearLayout)findViewById(R.id.lv_err_layout);
        mErrorLayout.setVisibility(View.GONE);
    }
    private void initData(){
        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        mFavoriteDataBaseHelper = new FavoriteDataBaseHelper(getFavoriteDao());
        getAllFavorite();
    }

    private void setListViewData(final List<Spots> spotsList){
        if (spotsList != null && spotsList.size()>0) {
            mAdapter = new PersonalCollectAdapter(this, spotsList);
            mListview.setAdapter(mAdapter);
            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent();
                    intent.setClass(PersonCollectActivity.this,MainActivity.class);
                    Constants.SPOTS_ID = spotsList.get(position).getPid();
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                }
            });
            mAdapter.setDeleteButtonClickListener(new AdapterOnClickListener<Spots>() {
                @Override
                public void onClick(View view, int position, Spots itemData) {
                    cancelCollection(itemData.getPid());

                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }

    public void getAllFavorite(){
        RequestParams params = new RequestParams();
        params.put("userId", SharedPreferencesUtils.getInt(this,"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_LIST,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<Integer> favoriteList = GsonUtil.jsonToList(response,"data",Integer.class);
                List<Spots> favoriteSpotsList = getSpotsListFromIdList(favoriteList);
                if(null!=favoriteSpotsList&&favoriteSpotsList.size()>0){
                    setListViewData(favoriteSpotsList);
                    mListview.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.GONE);
                }else {
                    mListview.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mListview.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
                mErrorLayout.setVisibility(View.VISIBLE);
                Toast.makeText(PersonCollectActivity.this,"获取收藏列表失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Spots> getSpotsListFromIdList(List<Integer> spots){
        List<Spots> spotsList = new ArrayList<>();
        for (int i = 0; i < spots.size(); i++) {
            //spotsList.clear();
            spotsList.add(mSpotsDataBaseHelper.getSpotsById(spots.get(i)));
        }
        return spotsList;
    }
    public void cancelCollection(final int spotsId){
        RequestParams params = new RequestParams();
        params.put("spotId",String.valueOf(spotsId));
        params.put("userId", SharedPreferencesUtils.getInt(PersonCollectActivity.this,"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_CANCEL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mFavoriteDataBaseHelper.deleteFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(PersonCollectActivity.this,"pid"),spotsId);
                List<Favorites> spotsList= mFavoriteDataBaseHelper.getFavoriteByPUserId(SharedPreferencesUtils.getInt(PersonCollectActivity.this,"pid"));
               if(null==spotsList||spotsList.size()==0){
                   mListview.setVisibility(View.GONE);
                   progressLayout.setVisibility(View.GONE);
                   mErrorLayout.setVisibility(View.VISIBLE);
               }
                Toast.makeText(PersonCollectActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PersonCollectActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
