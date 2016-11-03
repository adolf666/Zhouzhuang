package com.adolf.zhouzhuang.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.activity.WebViewActivity;
import com.adolf.zhouzhuang.databasehelper.FavoriteDataBaseHelper;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.interpolator.ExponentialOutInterpolator;
import com.adolf.zhouzhuang.util.GlideRoundTransform;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.SoundBroadUtils;
import com.adolf.zhouzhuang.util.StreamingMediaPlayer;
import com.adolf.zhouzhuang.util.Utils;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.GroundOverlay;
import com.amap.api.maps2d.model.GroundOverlayOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;
import java.util.List;

import static com.adolf.zhouzhuang.R.id.tv_spot_title;

public class GuideFragmentNew extends BaseFragment implements AMap.OnMarkerClickListener , AMap.InfoWindowAdapter ,View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private MapView mapView;
    private AMap aMap;
    private GroundOverlay groundoverlay;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private List<Spots> mSpotsList;
    private  Marker mMarker;
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
    public GuideFragmentNew() {
    }

    private void initViews(View view){
        mapView = (MapView) view.findViewById(R.id.map);
        mFrameIV = (ImageView) view.findViewById(R.id.iv_frame);
        mPause = (ImageButton) view.findViewById(R.id.img_pause);
        mClose = (ImageView) view.findViewById(R.id.img_close);
        mNotice = (RelativeLayout) view.findViewById(R.id.rl_notice);
        mVocie_Prompt = (TextView) view.findViewById(R.id.tv_voice_prompt);
        mBottomBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_bottom_bar);
        mBottomBarLinearLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_bar);
        mNotice.setVisibility(View.GONE);
        mClose.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mBottomBarRelativeLayout.setOnClickListener(this);
        mBottomBarRelativeLayout.requestLayout();
        animationDrawable = (AnimationDrawable) mFrameIV.getBackground();
        mBottomBarLinearLayout.addView(initBottomNaviView());
        hideBottomTabs();

        if (aMap == null) {
            aMap = mapView.getMap();
        }
        addMarksToMap();
        audioStreamer = new StreamingMediaPlayer(getActivity(), mPause, null,  null,null);
    }
    public static GuideFragmentNew newInstance() {
        GuideFragmentNew fragment = new GuideFragmentNew();
        return fragment;
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * 往地图上添加一个groundoverlay覆盖物
     */
    private void addOverlayToMap() {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.121956,
                120.851572), 18));// 设置当前地图显示为北京市恭王府
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(31.121998,120.842848))
                .include(new LatLng(31.109984,120.85759)).build();

        groundoverlay = aMap.addGroundOverlay(new GroundOverlayOptions()
                .anchor(0.5f, 0.5f)
                .transparency(0.1f)
                .image(BitmapDescriptorFactory
                        .fromResource(R.mipmap.layer))
                .positionFromBounds(bounds));


    }

    private void addMarksToMap(){
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        for (int i = 0; i < mSpotsList.size(); i++) {
            if (mSpotsList.get(i).getLat4show() != null && mSpotsList.get(i).getLng4show() != null){
                LatLng latlng = new LatLng(Double.parseDouble(mSpotsList.get(i).getLat4show()), Double.parseDouble(mSpotsList.get(i).getLng4show()));
                aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(latlng).title(mSpotsList.get(i).getTitle()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka))
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
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            mMarker = marker;
            mSpots = mSpotsDataBaseHelper.getSpotsByName(marker.getTitle());
            marker.showInfoWindow();
        }
        return true;
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
}
