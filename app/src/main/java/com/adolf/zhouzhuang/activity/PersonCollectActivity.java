package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.adapter.PersonalCollectAdapter;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
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
    private List<String> collectList;
    private PersonalCollectAdapter mAdapter;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
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

    }
    private void initData(){
        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        getAllFavorite();
    }

    private void setListViewData(List<Spots> spotsList){
        if (spotsList != null && spotsList.size()>0) {
            mAdapter = new PersonalCollectAdapter(this, spotsList);
            mListview.setAdapter(mAdapter);
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
                setListViewData(favoriteSpotsList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PersonCollectActivity.this,"获取收藏列表失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelCollection(String spotsId){
        RequestParams params = new RequestParams();
        params.put("spotId",spotsId);
        params.put("pid",SharedPreferencesUtils.getInt(this,"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_CANCEL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(PersonCollectActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(PersonCollectActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<Spots> getSpotsListFromIdList(List<Integer> spots){
        List<Spots> spotsList = new ArrayList<>();
        for (int i = 0; i < spots.size(); i++) {
            spotsList.add(mSpotsDataBaseHelper.getSpotsById(spots.get(i)));
        }
        return spotsList;
    }

}
