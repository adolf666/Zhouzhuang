package com.adolf.zhouzhuang.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.Favorites;
import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.activity.LoginActivity;
import com.adolf.zhouzhuang.activity.WebViewActivity;
import com.adolf.zhouzhuang.adapter.GuideListAdapter;
import com.adolf.zhouzhuang.adapter.SpotsListAdapter;
import com.adolf.zhouzhuang.databasehelper.FavoriteDataBaseHelper;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.interpolator.ExponentialOutInterpolator;
import com.adolf.zhouzhuang.util.GlideRoundTransform;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.StreamingMediaPlayer;
import com.adolf.zhouzhuang.util.Utils;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.adolf.zhouzhuang.R.id.tv_spot_title;
import static com.adolf.zhouzhuang.util.Constants.lat;

public class GuideFragmentNew extends BaseFragment implements AMap.OnMarkerClickListener , AMap.InfoWindowAdapter ,View.OnClickListener,
        AMap.OnMapClickListener, AMap.OnMapTouchListener {

    public static final int LoginRequest = 1008;
    private OnFragmentInteractionListener mListener;
    private MapView mapView;
    private AMap aMap;
    private GroundOverlay groundoverlay;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private List<Spots> mSpotsList;
    private Marker mMarker;
    private AnimationDrawable animationDrawable;
    private Spots mSpots;
    private View mGuideDialogView;
    private Button mFavoriteButton;
    private LinearLayout mGuideBgRelativeLayout;
    private RelativeLayout mBottomBarRelativeLayout;
    private TextView mSpotTitle;
    private StreamingMediaPlayer audioStreamer;
    private ImageView mFrameIV;
    private ImageButton mPause;
    private ImageView mClose;
    private RelativeLayout mNotice;
    private TextView mVocie_Prompt;
    private FavoriteDataBaseHelper mFavoriteDataBaseHelper;
    private LinearLayout mBottomBarLinearLayout;
    private View mBottomView;
    private TextView mWalkNavigationTV, mSpotsListTV;
    private ListView mSpotsListLV;
    private ListView mGuideListLV;
    private RelativeLayout mSpotsListRelativeLayout;
    private RelativeLayout mGuideListRelativeLayout;
    private List<Spots> spotsList;
    private GuideListAdapter mGuideListAdapter;
    private SpotsListAdapter mSpotsListAdapter;
    private UiSettings mUiSettings;
    private Polygon mPolygon;
    private int mHalfInfoWindowWidth = 0;//infowindow宽度一半，以px为单位
    private int mInfoWindowHeight = 0;//infoWinow高度，以px为单位
    private LatLng mMarkerLatLng;//点击的marker的坐标
    private Point mPoint;//点击的marker在屏幕中的坐标
    public GuideFragmentNew() {
    }

    private void initViews(View view){
        mapView = (MapView) view.findViewById(R.id.map);
        mFrameIV = (ImageView) view.findViewById(R.id.iv_frame);
        mPause = (ImageButton) view.findViewById(R.id.img_pause);
        mClose = (ImageView) view.findViewById(R.id.img_close);
        mNotice = (RelativeLayout) view.findViewById(R.id.rl_notice);
        mVocie_Prompt = (TextView) view.findViewById(R.id.tv_voice_prompt);

        mWalkNavigationTV = (TextView) view.findViewById(R.id.tv_walk_navigetion);
        mSpotsListTV = (TextView) view.findViewById(R.id.tv_spots_list);
        mSpotsListLV = (ListView) view.findViewById(R.id.lv_spots_list);
        mGuideListLV = (ListView) view.findViewById(R.id.lv_guide_list);
        mSpotsListRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_spots_list);
        mGuideListRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_guide_list);

        mBottomBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_bottom_bar);
        mBottomBarLinearLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_bar);

        mNotice.setVisibility(View.GONE);
        mClose.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mBottomBarRelativeLayout.setOnClickListener(this);
        mBottomBarRelativeLayout.requestLayout();
        mWalkNavigationTV.setOnClickListener(this);
        mSpotsListTV.setOnClickListener(this);

        animationDrawable = (AnimationDrawable) mFrameIV.getBackground();
        mBottomBarLinearLayout.addView(initBottomNaviView());
        hideBottomTabs();
        hideListView(mGuideListLV, mGuideListRelativeLayout, false);
        hideListView(mSpotsListLV, mSpotsListRelativeLayout, false);
        mHalfInfoWindowWidth = Utils.dip2px(getActivity(),135);
        mInfoWindowHeight = Utils.dip2px(getActivity(),170);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        audioStreamer = new StreamingMediaPlayer(getActivity(), mPause, null,  null,null);
        initMap();
        addMarksToMap();
        initPolygon();
        initGuideListViewAndSpotsListViewData();
        aMap.setOnMapClickListener(this);
        aMap.setOnMapTouchListener(this);
    }
    public static GuideFragmentNew newInstance() {
        GuideFragmentNew fragment = new GuideFragmentNew();
        return fragment;
    }

    public void setMapUISetting(){
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSpotsList();
    }
    public void getSpotsList() {
        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        mSpotsList = mSpotsDataBaseHelper.getAllSpots();
        mFavoriteDataBaseHelper = new FavoriteDataBaseHelper(getFavoriteDao());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_new, container, false);
        initViews(view);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        addOverlayToMap();
        return view;
    }

    public void initMap(){
        setMapUISetting();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void initGuideListViewAndSpotsListViewData() {
        SpotsDataBaseHelper spotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        spotsList = spotsDataBaseHelper.getAllSpots();
        if (spotsList != null && spotsList.size() > 0) {
            mGuideListAdapter = new GuideListAdapter(spotsList, getActivity());
            mSpotsListAdapter = new SpotsListAdapter(spotsList, getActivity());
            mSpotsListLV.setAdapter(mSpotsListAdapter);
            mGuideListLV.setAdapter(mGuideListAdapter);
        }
        mGuideListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSpots = spotsList.get(i);
                hideListView(mGuideListLV, mGuideListRelativeLayout, true);
                showBottomTabs();
                setTabResourceState();
            }
        });

        mSpotsListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideListView(mSpotsListLV, mSpotsListRelativeLayout, true);
                setTabResourceState();
                mSpots = spotsList.get(position);
                LatLng latlng = new LatLng(Double.parseDouble(mSpots.getLat4show()), Double.parseDouble(mSpots.getLng4show()));
                mMarker =aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(latlng).title(mSpots.getTitle()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.btn_voice_default))
                        .draggable(true).period(10));
                mMarker.showInfoWindow();
            }
        });
    }
    /**
     * 往地图上添加一个groundoverlay覆盖物
     */
    private void addOverlayToMap() {
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.115244,120.850083), 15.5f));
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(31.1249200000,120.8397900000))
                .include(new LatLng(31.1066900000,120.8595400000)).build();

        groundoverlay = aMap.addGroundOverlay(new GroundOverlayOptions()
                .anchor(0.5f, 0.5f)
                .transparency(0.1f)
                .image(BitmapDescriptorFactory
                        .fromResource(R.mipmap.layer))
                .positionFromBounds(bounds));

        aMap.setMapStatusLimits(bounds);
    }

    private void addMarksToMap(){
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.showMapText(false);
        for (int i = 0; i < mSpotsList.size(); i++) {
            if (mSpotsList.get(i).getLat4show() != null && mSpotsList.get(i).getLng4show() != null){
                LatLng latlng = new LatLng(Double.parseDouble(mSpotsList.get(i).getLat4show()), Double.parseDouble(mSpotsList.get(i).getLng4show()));
                aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(latlng).title(mSpotsList.get(i).getTitle()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.btn_voice_default))
                        .draggable(true).period(10));
            }

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        mGuideDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_guide, null);
        Button audioPlay = (Button) mGuideDialogView.findViewById(R.id.bt_audio_play);
        Button detail = (Button) mGuideDialogView.findViewById(R.id.bt_detail);
        Button navigation = (Button) mGuideDialogView.findViewById(R.id.tv_navigation_map);
        mFavoriteButton = (Button) mGuideDialogView.findViewById(R.id.bt_favorite);
        mSpotTitle = (TextView) mGuideDialogView.findViewById(tv_spot_title);
        mGuideBgRelativeLayout = (LinearLayout) mGuideDialogView.findViewById(R.id.ll_guide_bg);
        ImageView tv_close = (ImageView) mGuideDialogView.findViewById(R.id.tv_close);
        detail.setOnClickListener(this);
        audioPlay.setOnClickListener(this);
        navigation.setOnClickListener(this);
        mFavoriteButton.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        refreshGuideDialogState(mSpots);
        Glide.with(getActivity()).load(mSpots.getBriefimg())
                .asBitmap()
                .transform(new GlideRoundTransform(getActivity()))
                .into(new SimpleTarget<Bitmap>(280, 178) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        mSpotTitle.setText(mSpots.getTitle());
                        mGuideBgRelativeLayout.setBackground(new BitmapDrawable(getActivity().getResources(), resource));

                    }
                });

        return mGuideDialogView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_walk_navigetion:
                hideAndShowListView(mGuideListLV, mSpotsListLV, mGuideListRelativeLayout, mSpotsListRelativeLayout);
                break;
            case R.id.tv_spots_list:
                hideAndShowListView(mSpotsListLV, mGuideListLV, mSpotsListRelativeLayout, mGuideListRelativeLayout);
            case R.id.tv_bg_spots:
                hideListView(mSpotsListLV, mSpotsListRelativeLayout, true);
                break;
            case R.id.tv_bg_guide:
                hideListView(mGuideListLV, mGuideListRelativeLayout, true);
                break;

            case R.id.tv_close:
                mMarker.hideInfoWindow();
                break;
            case  R.id.bt_audio_play:
                playStreamAudio();
                break;
            case R.id.bt_detail:
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("URL", mSpots.getDetailUrl());
                intent.putExtra("SpotsId", mSpots.getPid());
                startActivity(intent);
                mMarker.hideInfoWindow();
                break;
            case R.id.tv_voice_prompt:
            case R.id.img_pause:
                if (audioStreamer.getMediaPlayer().isPlaying()) {
                    audioStreamer.getMediaPlayer().pause();
                    animationDrawable.stop();
                    mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_play));
                    mVocie_Prompt.setText("当前暂停播放" + mSpots.getTitle() + "语音导览");
                } else {
                    audioStreamer.getMediaPlayer().start();
                    animationDrawable.start();
                    mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_pause));
                    mVocie_Prompt.setText("正在为您播放" + mSpots.getTitle() + "语音导览...");
                }
              break;
            case R.id.img_close:
                mNotice.setVisibility(View.GONE);
                animationDrawable.stop();
                audioStreamer.getMediaPlayer().pause();
                break;
            case R.id.tv_navigation_map:
                showBottomTabs();
                mMarker.hideInfoWindow();
                break;
            case R.id.tv_open_baidu:
                if (mSpots.getLng() != null && mSpots.getLat() != null) {
                    Utils.openBaiduMap(getActivity(), Double.parseDouble(mSpots.getLng()), Double.parseDouble(mSpots.getLat()), "123", "步行导航");
                } else {
                    Toast.makeText(getActivity(), "未能获取经纬度", Toast.LENGTH_SHORT).show();
                }

                hideBottomTabs();
                break;
            case R.id.tv_open_gaode:
                Utils.goToNaviActivity(getActivity(), "test", null, mSpots.getLat(), mSpots.getLng(), "1", "2");
                hideBottomTabs();
                break;
            case R.id.btn_cancel:
                hideBottomTabs();
                break;
            case R.id.bt_favorite:
                if (!Utils.isAutoLogin(getActivity())) {
                    Intent intentLogin = new Intent().setClass(getActivity(), LoginActivity.class);
                    intentLogin.putExtra("FROM_GUIDE",1);
                    startActivityForResult(intentLogin,LoginRequest);
                } else {
                    if (!mFavoriteDataBaseHelper.isFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(getActivity(), "pid"), mSpots.getPid())) {
                        addFavorite();
                    } else {
                        cancelCollection();
                    }
                }
                break;
        }
    }

    private  void playStreamAudio(){
        try {
            if(audioStreamer.getMediaPlayer()!=null&&audioStreamer.getMediaPlayer().isPlaying()){
                audioStreamer.getMediaPlayer().reset();
            }
            audioStreamer.startStreaming(mSpots.getVideoLocation(),5208, 216);
            if(audioStreamer.getMediaPlayer()!=null){
                audioStreamer.getMediaPlayer().start();

            }
            mNotice.setVisibility(View.VISIBLE);
            animationDrawable.start();
            mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_pause));
            mVocie_Prompt.setText("正在为您播放" + mSpots.getTitle() + "语音导览...");

        } catch (IOException e) {
            Log.e(getClass().getName(), "Error starting to stream audio.", e);
        }

    }

    public void refreshGuideDialogState(final Spots spots){
        if (mGuideDialogView != null && spots != null){
            boolean isNoFavor = mFavoriteDataBaseHelper.isFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(getActivity(), "pid"), spots.getPid());
            mFavoriteButton.setBackgroundResource(isNoFavor ? R.mipmap.btn_favor2 : R.mipmap.btn_favor1);
        }
    }
    private void showBottomTabs() {
        mBottomBarRelativeLayout.setVisibility(View.VISIBLE);
        float currentY = mBottomView.getTranslationY();
        if (currentY == 600f) {//如果当前是隐藏的才会有动画展示
            ValueAnimator animator = ObjectAnimator.ofFloat(mBottomView, "translationY", 600f, 0);
            animator.setInterpolator(new ExponentialOutInterpolator());
            animator.setDuration(300);
            animator.start();
        }
    }
    private void hideBottomTabs() {
        float currentY = mBottomView.getTranslationY();//得到当前位置
        if (currentY == 0) {//如果当前位置是0,标明是展示的
            ValueAnimator animator = ObjectAnimator.ofFloat(mBottomView, "translationY", currentY, 600f);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if ((Float) animation.getAnimatedValue() >= 600f) {
                        mBottomBarRelativeLayout.setVisibility(View.GONE);
                    }
                }
            });
            animator.setDuration(300);
            animator.start();
        }
    }
    private View initBottomNaviView() {
        mBottomView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bottom_navi, null);
        //初始化控件
        TextView openBaidu = (TextView) mBottomView.findViewById(R.id.tv_open_baidu);
        TextView openGaode = (TextView) mBottomView.findViewById(R.id.tv_open_gaode);
        TextView cancel = (TextView) mBottomView.findViewById(R.id.btn_cancel);
        openBaidu.setOnClickListener(this);
        openGaode.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return mBottomView;
    }
    private void hideAndShowListView(final ListView listViewToShow, ListView listViewToHide, final RelativeLayout relativeLayoutToShow, final RelativeLayout relativeLayoutToHide) {
        float currentY = listViewToHide.getTranslationY();//得到当前位置
        //如果当前位置是0,标明是展示的
        if (currentY == 0) {
            ValueAnimator animator = ObjectAnimator.ofFloat(listViewToHide, "translationY", currentY, -1000f);
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float current = (float) animation.getAnimatedValue();
                    Log.i("AnimatedValue_Hide",current+"");
                    if (current <= -1000f) {
                        relativeLayoutToHide.setVisibility(View.GONE);

                        float currentShowY = listViewToShow.getTranslationY();//得到当前位置
                        if (currentShowY == 0f){
                            ValueAnimator animator = ObjectAnimator.ofFloat(listViewToShow, "translationY", currentShowY, -1000f);
                            animator.setDuration(300);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float current = (float) animation.getAnimatedValue();
                                    Log.i("AnimatedValue_Show",current+"");
                                    if (current <= -1000f) {
                                        relativeLayoutToShow.setVisibility(View.GONE);
//                        showListView(listViewToShow, relativeLayoutToShow);
                                    }
                                }
                            });
                            animator.start();
                        }else{
                            showListView(listViewToShow, relativeLayoutToShow);
                        }
                    }
                }
            });
            animator.start();
        }else{
            float currentShowY = listViewToShow.getTranslationY();//得到当前位置
            if (currentShowY == 0f){
                ValueAnimator animator = ObjectAnimator.ofFloat(listViewToShow, "translationY", currentShowY, -1000f);
                animator.setDuration(300);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float current = (float) animation.getAnimatedValue();
                        Log.i("AnimatedValue_Show",current+"");
                        if (current <= -1000f) {
                            relativeLayoutToShow.setVisibility(View.GONE);
//                        showListView(listViewToShow, relativeLayoutToShow);
                            if (listViewToShow.getId() == R.id.lv_spots_list) {
                                mSpotsListTV.setBackgroundResource(R.drawable.spot_list_selector);
                            } else {
                                mWalkNavigationTV.setBackgroundResource(R.drawable.navigation_selector);
                            }
                        }
                    }
                });
                animator.start();
            }else{
                showListView(listViewToShow, relativeLayoutToShow);
            }
        }

    }

    private void showListView(final ListView listView, RelativeLayout relativeLayout) {
        relativeLayout.setVisibility(View.VISIBLE);
        float currentY = listView.getTranslationY();
        if (currentY == -1000f) {
            if (listView.getId() == R.id.lv_spots_list) {
                mSpotsListTV.setBackgroundResource(R.mipmap.btn_scenicspot_focus);
                mWalkNavigationTV.setBackgroundResource(R.drawable.navigation_selector);
            } else {
                mWalkNavigationTV.setBackgroundResource(R.mipmap.btn_guide_focus);
                mSpotsListTV.setBackgroundResource(R.drawable.spot_list_selector);
            }
            ValueAnimator animator = ObjectAnimator.ofFloat(listView, "translationY", -1000f, 0);
            animator.setDuration(300);
            animator.start();
        }
//        else {
//            if (listView.getId() == R.id.lv_spots_list) {
//                mSpotsListTV.setBackgroundResource(R.drawable.spot_list_selector);
//            } else {
//                mWalkNavigationTV.setBackgroundResource(R.drawable.navigation_selector);
//            }
//            ValueAnimator animator = ObjectAnimator.ofFloat(listView, "translationY", -1000f);
//            animator.setDuration(300);
//            animator.start();
//        }
    }
    private void hideListView(final ListView listView, final RelativeLayout relativeLayout, boolean isNeedAnimation) {

        float currentY = listView.getTranslationY();
        if (currentY == 0f) {
            ValueAnimator animator = ObjectAnimator.ofFloat(listView, "translationY", -1000f);
            animator.setDuration(300);
            animator.start();
            if (isNeedAnimation) {
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float current = (float) animation.getAnimatedValue();
                        if (current <= -1000) {
                            relativeLayout.setVisibility(View.GONE);
                            if (listView.getId() == R.id.lv_spots_list) {
                                mSpotsListTV.setBackgroundResource(R.drawable.spot_list_selector);
                            } else {
                                mWalkNavigationTV.setBackgroundResource(R.drawable.navigation_selector);
                            }
                        }
                    }
                });
            } else {
                relativeLayout.setVisibility(View.GONE);
            }
        }
    }
    private void addFavorite() {
        RequestParams params = new RequestParams();
        params.put("spotId", mSpots.getPid());
        params.put("userId", SharedPreferencesUtils.getInt(getActivity(), "pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Favorites favorite = new Favorites();
                favorite.setUserId(SharedPreferencesUtils.getInt(getActivity(), "pid"));
                favorite.setSpotsId(mSpots.getPid());
                mFavoriteDataBaseHelper.addFavorite(favorite);
                refreshGuideDialogState(mSpots);
                Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), "收藏失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void cancelCollection() {
        RequestParams params = new RequestParams();
        params.put("spotId", mSpots.getPid());
        params.put("userId", SharedPreferencesUtils.getInt(getActivity(), "pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_CANCEL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Favorites favorite = mFavoriteDataBaseHelper.getFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(getActivity(), "pid"), mSpots.getPid());
                mFavoriteDataBaseHelper.deleteFavorite(favorite);
                refreshGuideDialogState(mSpots);
                Toast.makeText(getActivity(), "取消收藏成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(), "取消收藏失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LoginRequest:
                if(Utils.isAutoLogin(getActivity())) {
                    addFavorite();
                }
                break;
        }
    }
    public void setTabResourceState() {
        mSpotsListTV.setBackgroundResource(R.drawable.spot_list_selector);
        mWalkNavigationTV.setBackgroundResource(R.drawable.navigation_selector);
    }

    public void initPolygon(){
        // 绘制一个长方形
        PolygonOptions pOption = new PolygonOptions();
        pOption.add(new LatLng(31.124896,120.839916));
        pOption.add(new LatLng(31.124911,120.859629));
        pOption.add(new LatLng(31.10669,120.859568));
        pOption.add(new LatLng(31.106684,120.839868));
        mPolygon = aMap.addPolygon(pOption.strokeWidth(5).
                strokeColor(Color.argb(50, 1, 1, 1))
                        .fillColor(Color.argb(50, 1, 1, 1)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            mMarker = marker;
            mSpots = mSpotsDataBaseHelper.getSpotsByName(marker.getTitle());
            getMarkLatLng(marker);
            getMarkScreenPoint();
            MarkerOptions markerOptions = marker.getOptions();
            markerOptions.setInfoWindowOffset(setInfoWindowXOffset(),setInfoWindowYOffset());
            mMarker.setMarkerOptions(markerOptions);
            mMarker.showInfoWindow();

        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("clickPoint",latLng+"");
        if(mGuideListLV.getVisibility() ==View.VISIBLE){
            hideListView(mGuideListLV, mGuideListRelativeLayout, true);
        }
        if(mSpotsListLV.getVisibility() ==View.VISIBLE){
            hideListView(mSpotsListLV, mSpotsListRelativeLayout, true);
        }
    }

    //将点击的marker的经纬度转像素坐标
    private void getMarkScreenPoint() {
        mPoint = aMap.getProjection().toScreenLocation(mMarkerLatLng);
    }

    //获取点击的marker的经纬度
    private void getMarkLatLng(Marker marker){
        mMarkerLatLng = marker.getPosition();
    }

    //marker点击后默认的infowindow是否超出有边缘
    private boolean isInfoWindowOutOfRightlayer(){
        Point point = new Point(mPoint.x+ mHalfInfoWindowWidth,mPoint.y);
        //将point转化为经纬度
        LatLng pointLatlng = aMap.getProjection().fromScreenLocation(point);
        return mPolygon.contains(pointLatlng);
    }

    private boolean isInfoWindowOutOfUpLayer(){
        Point point = new Point(mPoint.x,mPoint.y - mInfoWindowHeight);
        //将point转化为经纬度
        LatLng pointLatlng = aMap.getProjection().fromScreenLocation(point);
        return mPolygon.contains(pointLatlng);
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {

    }

    //InfoWindow在水平方向上的偏移
    public int setInfoWindowXOffset(){
        if (isInfoWindowOutOfRightlayer()){
            return 0;
        }
        return 0 - mHalfInfoWindowWidth;
    }

    //InfoWindow在竖直方向上的偏移
    public int setInfoWindowYOffset(){
        if (isInfoWindowOutOfUpLayer()) {
            return 0;
            }
        return mInfoWindowHeight;
        }
}
