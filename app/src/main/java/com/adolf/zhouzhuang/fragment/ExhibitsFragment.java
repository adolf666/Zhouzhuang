package com.adolf.zhouzhuang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.WebViewActivity;
import com.adolf.zhouzhuang.adapter.NewsAdapter;
import com.adolf.zhouzhuang.adapter.PanoramaAdapter;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.Exhibit;
import com.adolf.zhouzhuang.object.PanoramaObject;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gpp on 2016/10/19 0019.
 */

public class ExhibitsFragment extends BaseFragment {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    private int index;
    private ListView mListView;
    private RelativeLayout mProgressLayout;
    private LinearLayout mErrLayout;
    List<PanoramaObject> panoramaObjectList;

    public static ExhibitsFragment newInstance(int Type) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, Type);
        ExhibitsFragment fragment = new ExhibitsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(EXTRA_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exhibits, container, false);
        mListView = (ListView) view.findViewById(R.id.lv_exhibit);
        mProgressLayout = (RelativeLayout) view.findViewById(R.id.ll_progress_bar);
        mProgressLayout.setVisibility(View.VISIBLE);
        mErrLayout = (LinearLayout) view.findViewById(R.id.lv_err_layout);
        mErrLayout.setVisibility(View.GONE);
        if (index == 3) {
            getPanoramaData();
        } else {
            getData(index);

        }
        return view;
    }

    //获取陈列信息，type的值为0:新闻列表 ，1:临时展馆，2:陈列展馆
    private void getData(final int index) {

        RequestParams params = new RequestParams();
        params.add("type", index + "");
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.NEWS_EXHIBITION_TEMPORARY, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<Exhibit> exhibits = GsonUtil.jsonToList(response, "data", Exhibit.class);
                if (null != exhibits && exhibits.size() > 0) {
                    initData(exhibits);
                    mProgressLayout.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                } else {
                    mProgressLayout.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    mErrLayout.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mProgressLayout.setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
                mErrLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData(final List<Exhibit> exhibits) {
        NewsAdapter mAdapter = new NewsAdapter(getActivity(), exhibits);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("URL", exhibits.get(position).getDetailUrl());
                if (1 == index) {
                    intent.putExtra(WebViewActivity.NAME, exhibits.get(position).getTitle());
                } else if (2 == index) {
                    intent.putExtra(WebViewActivity.NAME, "陈列展览");
                }
                startActivity(intent);
            }
        });
    }

    private void getPanoramaData() {
        mProgressLayout.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        panoramaObjectList = new ArrayList<>();
        PanoramaObject panoramaObject1 = new PanoramaObject();
        PanoramaObject panoramaObject2 = new PanoramaObject();
        PanoramaObject panoramaObject3 = new PanoramaObject();
        PanoramaObject panoramaObject4 = new PanoramaObject();
        PanoramaObject panoramaObject5 = new PanoramaObject();
        PanoramaObject panoramaObject6 = new PanoramaObject();
        PanoramaObject panoramaObject7 = new PanoramaObject();
        PanoramaObject panoramaObject8 = new PanoramaObject();
        PanoramaObject panoramaObject9 = new PanoramaObject();

        panoramaObject1.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian01));
        panoramaObject2.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian02));
        panoramaObject3.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian03));
        panoramaObject4.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian04));
        panoramaObject5.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian05));
        panoramaObject6.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian06));
        panoramaObject7.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian07));
        panoramaObject8.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian08));
        panoramaObject9.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian09));


        panoramaObject1.setName("飞禽纹黑皮陶双耳壶");
        panoramaObject1.setDesc("此罐又称贯耳壶，通体乌黑，从罐腹至腔，手工绘制水禽飞鸟五十只。其单线造型简练明快，似水鸟隔水而飞，生动活泼。1995年被定为国家一级文物。");

        panoramaObject2.setName("良渚文化贯耳陶壶");
        panoramaObject2.setDesc("黑衣贯耳壶pottery pot 为良渚显贵先民之用具");

        panoramaObject3.setName("良渚文化陶鼎");
        panoramaObject3.setDesc("陶鼎 pottery pot 此鼎浅足，形小，腹部微鼓，是良渚较早的炊器，腹部有后期人工开凿的一个孔，不知何因，待考证。");

        panoramaObject4.setName("春秋陶罐");
        panoramaObject4.setDesc("硬陶罐hard pottery pot 此陶罐是西周春秋时代贮器，地平有细边，小口沿体形扁圆，罐体上部斜折纹，下部是回纹图案，酱紫色十分古朴美观。");

        panoramaObject5.setName("良渚文化陶壶");
        panoramaObject5.setDesc("陶盉pottery container为良渚炊具。");

        panoramaObject6.setName("良渚文化石犁");
        panoramaObject6.setDesc("石犁 stone plough:an important tool in ploughing land,made of black stone 黑色页岩制成，为良渚时期重要的耕田工具。");

        panoramaObject7.setName("良渚文化石斧2块");
        panoramaObject7.setDesc("石斧stone axe 用于砍劈、剁、砍肉类和野菜等。");

        panoramaObject8.setName("良渚文化石镞3块");
        panoramaObject8.setDesc("石镞stone weapon 为先民狩猎和防御的重要工具。");

        panoramaObject9.setName("甲骨文吴字陶罐");
        panoramaObject9.setDesc("");

        panoramaObjectList.add(panoramaObject1);
        panoramaObjectList.add(panoramaObject2);
        panoramaObjectList.add(panoramaObject3);
        panoramaObjectList.add(panoramaObject4);
        panoramaObjectList.add(panoramaObject5);
        panoramaObjectList.add(panoramaObject6);
        panoramaObjectList.add(panoramaObject7);
        panoramaObjectList.add(panoramaObject8);
        panoramaObjectList.add(panoramaObject9);

        PanoramaAdapter panoramaAdapter = new PanoramaAdapter(getActivity(), panoramaObjectList);
        mListView.setAdapter(panoramaAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebViewActivity.class);
                if (i == 0) {
                    intent.putExtra("URL", "file:///android_asset/360/001.html");
                } else if (i == 1) {
                    intent.putExtra("URL", "file:///android_asset/360/002.html");
                } else if (i == 2) {
                    intent.putExtra("URL", "file:///android_asset/360/003.html");
                } else if (i == 3) {
                    intent.putExtra("URL", "file:///android_asset/360/004.html");
                } else if (i == 4) {
                    intent.putExtra("URL", "file:///android_asset/360/005.html");
                } else if (i == 5) {
                    intent.putExtra("URL", "file:///android_asset/360/006.html");
                } else if (i == 6) {
                    intent.putExtra("URL", "file:///android_asset/360/007.html");
                } else if (i == 7) {
                    intent.putExtra("URL", "file:///android_asset/360/008.html");
                } else {
                    intent.putExtra("URL", "file:///android_asset/360/009.html");
                }
                intent.putExtra(WebViewActivity.NAME, "360展示");
                startActivity(intent);
            }
        });
    }

}
