package com.adolf.zhouzhuang.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import com.adolf.zhouzhuang.util.Constants;
import com.adolf.zhouzhuang.util.GlideRoundTransform;
import com.adolf.zhouzhuang.util.SdCardUtil;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.SoundBroadUtils;
import com.adolf.zhouzhuang.util.StreamingMediaPlayer;
import com.adolf.zhouzhuang.util.Utils;
import com.adolf.zhouzhuang.widget.LoadingProgressDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.platform.comapi.map.B;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.adolf.zhouzhuang.R.id.tv_close;
import static com.adolf.zhouzhuang.R.id.tv_spot_title;


public class GudieFragment extends BaseFragment implements View.OnClickListener {

    public static final int LoginRequest = 1008;
    private static final String SPOT_ID = "spot_id";
    public MapView mMapView = null;
    public BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListenner();
    private TextView mLoactionTV;

    private OnFragmentInteractionListener mListener;
    private GuideListAdapter mGuideListAdapter;
    private SpotsListAdapter mSpotsListAdapter;

    private TextView mWalkNavigationTV, mSpotsListTV;
    private ListView mSpotsListLV;
    private ListView mGuideListLV;
    private ImageView mFrameIV;
    private ImageButton mPause;
    private ImageView mClose;
    private RelativeLayout mNotice;
    private TextView mVocie_Prompt;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private FavoriteDataBaseHelper mFavoriteDataBaseHelper;
    private List<Spots> mSpotsList;
    private Spots mSpots;
    private boolean isPause = false;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    public BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromAssetWithDpi("btn_voice_default.png");
    private TextView mSpotsListBg, mGuideListBg;
    private List<Spots> spotsList;
    private LoadingProgressDialog  mProgressDialog ;
    private AnimationDrawable animationDrawable;
    private LinearLayout mBottomBarLinearLayout;
    private View mBottomView;
    private RelativeLayout mBottomBarRelativeLayout;
    private RelativeLayout mSpotsListRelativeLayout;
    private RelativeLayout mGuideListRelativeLayout;
    private OverlayOptions ooGround;
    private View mGuideDialogView;
    private Button mFavoriteButton;
    private LinearLayout mGuideBgRelativeLayout;
    private TextView mSpotTitle;
    private int spotsId = -1;
    private StreamingMediaPlayer audioStreamer;
    public GudieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spotsId = getArguments().getInt(SPOT_ID);
        }
        Log.i("ssssssss+spotsid", spotsId + "");
//        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        getSpotsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gudie, container, false);
        initViews(view);
        initDialogView();
        initBaiduMap();
        initGuideListViewAndSpotsListViewData();
        return view;
    }

    public void initViews(View view) {
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mLoactionTV = (TextView) view.findViewById(R.id.tv_loaction);
        mWalkNavigationTV = (TextView) view.findViewById(R.id.tv_walk_navigetion);
        mSpotsListTV = (TextView) view.findViewById(R.id.tv_spots_list);
        mSpotsListLV = (ListView) view.findViewById(R.id.lv_spots_list);
        mGuideListLV = (ListView) view.findViewById(R.id.lv_guide_list);
        mBottomBarRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_bottom_bar);
        mBottomBarLinearLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_bar);
        mFrameIV = (ImageView) view.findViewById(R.id.iv_frame);
        mPause = (ImageButton) view.findViewById(R.id.img_pause);
        mClose = (ImageView) view.findViewById(R.id.img_close);
        mNotice = (RelativeLayout) view.findViewById(R.id.rl_notice);
        mVocie_Prompt = (TextView) view.findViewById(R.id.tv_voice_prompt);
        mSpotsListRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_spots_list);
        mGuideListRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_guide_list);
        mSpotsListBg = (TextView) view.findViewById(R.id.tv_bg_spots);
        mGuideListBg = (TextView) view.findViewById(R.id.tv_bg_guide);
        mNotice.setVisibility(View.GONE);
        mClose.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mVocie_Prompt.setOnClickListener(this);
        mWalkNavigationTV.setOnClickListener(this);
        mSpotsListTV.setOnClickListener(this);
        mLoactionTV.setOnClickListener(this);
        mSpotsListBg.setOnClickListener(this);
        mGuideListBg.setOnClickListener(this);
        mBottomBarRelativeLayout.setOnClickListener(this);
        mBottomBarRelativeLayout.requestLayout();
        mSpotsListBg.requestFocus();
        mGuideListBg.requestFocus();
        mFrameIV.setBackgroundResource(R.drawable.anim_paly_audio);
        animationDrawable = (AnimationDrawable) mFrameIV.getBackground();
        mBottomBarLinearLayout.addView(initBottomNaviView());
        hideBottomTabs();
        hideListView(mGuideListLV, mGuideListRelativeLayout, false);
        hideListView(mSpotsListLV, mSpotsListRelativeLayout, false);
        audioStreamer = new StreamingMediaPlayer(getActivity(), mPause, null,  null,null);
    }

    public void getSpotsList() {
        mSpotsList = mSpotsDataBaseHelper.getAllSpots();
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

                setMapStatusLimits();


                locationToCenter(Double.parseDouble(mSpots.getLat4show()), Double.parseDouble(mSpots.getLng4show()), false,false);
                showBaiduInfoWindow(spotsList.get(position));
            }
        });
    }

    public void stopPlayAudo(){
        if (mNotice != null && mNotice.getVisibility() == View.VISIBLE){
            mNotice.setVisibility(View.GONE);
            animationDrawable.stop();
            SoundBroadUtils.getInstance().stopSound();
            isPause = false;
        }

    }

    public void checkAndDownLoadAudio(Spots spots){
        if (!isAudioExit(String.valueOf(spots.getPid()))) {
           // downloadAudio();
            playStreamAudio();
        } else {
            playAudio(Utils.getAudioFullPath(String.valueOf(spots.getPid())));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_loaction:
                locationToCenter(31.123292, 120.85481, true,true);
                mBaiduMap.hideInfoWindow();
                break;
            case R.id.bt_audio_play:
               // checkAndDownLoadAudio(mSpots);
                playStreamAudio();
                break;
            case R.id.bt_detail:
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("URL", mSpots.getDetailUrl());
                intent.putExtra("SpotsId", mSpots.getPid());
                startActivity(intent);
                mBaiduMap.hideInfoWindow();
                break;
            case R.id.tv_walk_navigetion:
                hideAndShowListView(mGuideListLV, mSpotsListLV, mGuideListRelativeLayout, mSpotsListRelativeLayout);
                break;
            case R.id.tv_navigation_map:
                showBottomTabs();
                mBaiduMap.hideInfoWindow();
                break;
            case R.id.bt_favorite:
                if (!Utils.isAutoLogin(getActivity())) {
                Intent intentLogin = new Intent().setClass(getActivity(), LoginActivity.class);
              //  startActivity(intentLogin);
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
            case R.id.tv_spots_list:
                hideAndShowListView(mSpotsListLV, mGuideListLV, mSpotsListRelativeLayout, mGuideListRelativeLayout);
                break;
            case R.id.tv_voice_prompt:
            case R.id.img_pause:
                SoundBroadUtils.getInstance().pauseSound(isPause);

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

         /*       if (isPause) {
                    mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_pause));
                    animationDrawable.start();
                    mVocie_Prompt.setText("正在为您播放" + mSpots.getTitle() + "语音导览...");
                } else {
                    mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_play));
                    animationDrawable.stop();
                    mVocie_Prompt.setText("当前暂停播放" + mSpots.getTitle() + "语音导览");
                }*/
                isPause = !isPause;
                break;
            case R.id.img_close:
                stopPlayAudo();
                break;
            case R.id.tv_close:
                mBaiduMap.hideInfoWindow();
                locationToCenter(31.123292, 120.85481, true,true);
                break;
            case R.id.tv_bg_spots:
                hideListView(mSpotsListLV, mSpotsListRelativeLayout, true);
                break;
            case R.id.tv_bg_guide:
                hideListView(mGuideListLV, mGuideListRelativeLayout, true);
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
            case R.id.rl_bottom_bar:
                hideBottomTabs();
        }
    }

    public void setTabResourceState() {
        mSpotsListTV.setBackgroundResource(R.drawable.spot_list_selector);
        mWalkNavigationTV.setBackgroundResource(R.drawable.navigation_selector);
    }

    public boolean isAudioExit(String audioName) {
        return Utils.fileIsExists(Utils.getAudioFullPath(audioName));
    }

    private  void playStreamAudio(){
        try {
        if(audioStreamer.getMediaPlayer()!=null&&audioStreamer.getMediaPlayer().isPlaying()){
                audioStreamer.getMediaPlayer().reset();
                Log.i("ssssssssssss","getMediaPlayer().pause()");
            }
            audioStreamer.startStreaming(mSpots.getVideoLocation(),5208, 216);
            if(audioStreamer.getMediaPlayer()!=null){
                audioStreamer.getMediaPlayer().start();
                Log.i("ssssssssssss","getMediaPlayer().start()");
            }
            mNotice.setVisibility(View.VISIBLE);
            animationDrawable.start();
            mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_pause));
            mVocie_Prompt.setText("正在为您播放" + mSpots.getTitle() + "语音导览...");

        } catch (IOException e) {
            Log.e(getClass().getName(), "Error starting to stream audio.", e);
        }

    }



    public void downloadAudio() {
        final String filePath = SdCardUtil.getSdPath() + SdCardUtil.FILEDIR + SdCardUtil.FILEAUDIO + "/" + mSpots.getPid() + ".mp3";
        File saveFile = new File(filePath);
        String[] allowedContentTypes = new String[]{".*"};
        AsyncHttpClientUtils.getInstance().downLoadFile(mSpots.getVideoLocation(), new BinaryHttpResponseHandler(allowedContentTypes) {

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onStart() {
                super.onStart();

                mProgressDialog = new LoadingProgressDialog(getActivity(), "正在下载语音, 请稍候...");
                mProgressDialog.show();
                mNotice.setVisibility(View.GONE);
                animationDrawable.stop();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
                if (Utils.fileIsExists(filePath)) {
                    downloadFinish(filePath);
                } else {
                    Toast.makeText(getActivity(), "未能正确下载音乐", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                InputStream inputstream = new ByteArrayInputStream(binaryData);
                if (inputstream != null) {
                    SdCardUtil.write2SDFromInput(filePath, inputstream);
                    try {
                        inputstream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                downloadFinish(filePath);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void downloadFinish(String filePath) {
        playAudio(filePath);
    }

    public void playAudio(String filePath) {

        SoundBroadUtils.getInstance().playSound(getActivity(), filePath);
        mNotice.setVisibility(View.VISIBLE);
        animationDrawable.start();
        mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_pause));
        mVocie_Prompt.setText("正在为您播放" + mSpots.getTitle() + "语音导览...");

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        mFavoriteDataBaseHelper = new FavoriteDataBaseHelper(getFavoriteDao());
    }

    private void initBaiduMap() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.getUiSettings().setCompassEnabled(false);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        mBaiduMap.showMapPoi(false);
        MyLocationData locData = new MyLocationData.Builder().accuracy(100).direction(90.0f).latitude(Constants.lat).longitude(Constants.lng).build();
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMyLocationEnabled(true);

        locationToCenter(31.123292, 120.85481, true,true);
        addLayerToMap();
//        initAndAddLayer();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLng northeast = new LatLng(31.131410, 120.86299);
                LatLng southwest = new LatLng(31.115130, 120.84670);
                LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                        .include(southwest).build();
                mBaiduMap.setMapStatusLimits(bounds);
            }
        });
    }

    //定位屏幕中心点
    public void locationToCenter(double lat, double lng, boolean isZoom,boolean isNeedToLimit) {
        LatLng ll = new LatLng(lat, lng);
        MapStatusUpdate u;
        if (isZoom) {
            u = MapStatusUpdateFactory.newLatLngZoom(ll, 16.6f);//设置缩放比例
            mBaiduMap.animateMapStatus(u);
        } else {
            u = MapStatusUpdateFactory.newLatLng(ll);//不设置缩放比例
            final MapStatusUpdate u2 = MapStatusUpdateFactory.scrollBy(0, 0 - Utils.dip2px(getActivity(), 10));
            mBaiduMap.animateMapStatus(u);
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mBaiduMap.animateMapStatus(u2);
                }
            };
            handler.postDelayed(runnable, 400);
        }
        if (isNeedToLimit){
            LatLng northeast = new LatLng(31.131410, 120.86299);
            LatLng southwest = new LatLng(31.115130, 120.84670);
            LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                    .include(southwest).build();
            mBaiduMap.setMapStatusLimits(bounds);
        }
    }

    public void addLayerToMap() {
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromBitmap(compressBitmap(R.mipmap.layer));
        LatLng northeast = new LatLng(31.131460, 120.86312);
        LatLng southwest = new LatLng(31.115123, 120.84660);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();
        if (ooGround == null){
            ooGround = new GroundOverlayOptions()
                    .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
            mBaiduMap.addOverlay(ooGround);
        }
        initAndAddLayer();
    }

    private Bitmap compressBitmap(int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inInputShareable = true;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2; // width，hight设为原来的1/3
//获取资源图片流
        InputStream is = getActivity().getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,options);
    }

    private void initAndAddLayer() {
        MarkerOptions ooA = new MarkerOptions().icon(bdA).zIndex(9).draggable(false);
        for (int i = 0; i < mSpotsList.size(); i++) {
            LatLng latlng;
            if (mSpotsList.get(i).getLat4show() != null && mSpotsList.get(i).getLng4show() != null) {
                latlng = new LatLng(Double.parseDouble(mSpotsList.get(i).getLat4show()), Double.parseDouble(mSpotsList.get(i).getLng4show()));
                ooA.position(latlng);
                ooA.perspective(true);
                Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
                marker.setTitle(mSpotsList.get(i).getTitle());
                marker.setPerspective(true);
            } else {
                Log.i("tttt", mSpotsList.get(i).getTitle());
            }
        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {

                mSpots = mSpotsDataBaseHelper.getSpotsByName(marker.getTitle());
                showBaiduInfoWindow(mSpots);
                setMapStatusLimits();
                locationToCenter(Double.parseDouble(mSpots.getLat4show()), Double.parseDouble(mSpots.getLng4show()), false,false);
                return true;
            }
        });

        if (-1 != spotsId) {
            showBaiduInfoWindow(mSpotsDataBaseHelper.getSpotsById(spotsId));

        }
    }

    public void setMapStatusLimits(){
        LatLng northeast = new LatLng(31.137760, 120.86592);
        LatLng southwest = new LatLng(31.115130, 120.84370);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();
        mBaiduMap.setMapStatusLimits(bounds);
    }

    private View initBottomNaviView() {
        mBottomView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bottom_navi, null);
        //初始化控件
        TextView openBaidu = (TextView) mBottomView.findViewById(R.id.tv_open_baidu);
        TextView openGaode = (TextView) mBottomView.findViewById(R.id.tv_open_gaode);
        TextView cancel = (TextView) mBottomView.findViewById(R.id.btn_cancel);
//        openBaidu.setTypeface(Utils.getType(getActivity(), 0));
//        openGaode.setTypeface(Utils.getType(getActivity(), 0));
//        cancel.setTypeface(Utils.getType(getActivity(), 0));
        openBaidu.setOnClickListener(this);
        openGaode.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return mBottomView;
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

    public void showBaiduInfoWindow(final Spots spots) {
        if (spots.getLat4show() != null && spots.getLng4show() != null) {
            refreshGuideDialogState(spots);
            mSpotTitle.setText(spots.getTitle());
            Glide.with(getActivity()).load(mSpots.getBriefimg())
                    .asBitmap()
                    .transform(new GlideRoundTransform(getActivity()))
                    .into(new SimpleTarget<Bitmap>(280, 178) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            mSpotTitle.setText(mSpots.getTitle());
                            mGuideBgRelativeLayout.setBackground(new BitmapDrawable(getActivity().getResources(), resource));
                            LatLng latLng = new LatLng(Double.parseDouble(spots.getLat4show()), Double.parseDouble(spots.getLng4show()));
                            InfoWindow infoWindow = new InfoWindow(mGuideDialogView, latLng, 0);
                            mBaiduMap.showInfoWindow(infoWindow);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "获取经纬度失败", Toast.LENGTH_SHORT).show();
        }
    }

    private View initDialogView() {
        if (mGuideDialogView != null){
            return mGuideDialogView;
        }
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
        return mGuideDialogView;
    }

    public void refreshGuideDialogState(final Spots spots){
        if (mGuideDialogView != null && spots != null){
            boolean isNoFavor = mFavoriteDataBaseHelper.isFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(getActivity(), "pid"), spots.getPid());
            mFavoriteButton.setBackgroundResource(isNoFavor ? R.mipmap.btn_favor2 : R.mipmap.btn_favor1);
        }
    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            LatLng ll = new LatLng(31.123292, 120.85481);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(15.7f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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
        if (mMapView != null){
            mMapView.onResume();
        }
        refreshGuideDialogState(mSpots);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            stopPlayAudo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayAudo();
        if (mMapView !=null){
            mMapView.onDestroy();
        }
        bdA.recycle();
    }

    public void setSelectedSpotsOutSide(Spots spotsId){
            this.mSpots = spotsId;
            if (mSpots == null){
                Toast.makeText(getActivity(),"未获取该该景点信息",Toast.LENGTH_SHORT).show();
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
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LoginRequest:
            addFavorite();
                break;
        }
    }

}
