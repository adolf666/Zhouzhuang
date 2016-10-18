package com.adolf.zhouzhuang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.WebViewActivity;
import com.adolf.zhouzhuang.adapter.StrategyAdapter;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.StrategyObject;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gpp on 2016/10/18 0018.
 */

public class TravelsFragment extends BaseFragment {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    private  int index;
    private ListView mListView;
    public static TravelsFragment newInstance( int Type) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, Type);
        TravelsFragment fragment = new TravelsFragment();
        fragment.setArguments(args);
        return fragment;
    }

 public TravelsFragment(){
 }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index =  getArguments().getInt(EXTRA_TYPE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_travels, container, false);
        mListView = (ListView)view.findViewById(R.id.lv_travels);
        getData(index);
        return view;
    }

    private void getData(final int index) {
        RequestParams params = new RequestParams();
        params.add("type", index+"");
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.GET_STRATEGY, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<StrategyObject> strategyObjectArrayList = GsonUtil.jsonToList(response, "data", StrategyObject.class);
                 initData(strategyObjectArrayList);


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void initData( final List<StrategyObject> strategyObjectArrayList){
        StrategyAdapter  mAdapter = new StrategyAdapter(getActivity(), strategyObjectArrayList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("URL", strategyObjectArrayList.get(position).getUrl());
                intent.putExtra(WebViewActivity.NAME, "游玩攻略");
                startActivity(intent);
            }
        });
    }


}
