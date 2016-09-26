package com.adolf.zhouzhuang.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.Favorites;
import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.activity.WebViewActivity;
import com.adolf.zhouzhuang.adapter.GuideListAdapter;
import com.adolf.zhouzhuang.adapter.SpotsListAdapter;
import com.adolf.zhouzhuang.databasehelper.FavoriteDataBaseHelper;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.util.Constants;
import com.adolf.zhouzhuang.util.SdCardUtil;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.SoundBroadUtils;
import com.adolf.zhouzhuang.util.UniversalDialog;
import com.adolf.zhouzhuang.util.Utils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
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
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class GudieFragment extends BaseFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private BMapManager bMapManager;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListenner();
    private TextView mLoactionTV;
    boolean isFirstLoc = true; // 是否首次定位

    private OnFragmentInteractionListener mListener;
    private GuideListAdapter mGuideListAdapter;
    private SpotsListAdapter mSpotsListAdapter;

    private TextView mWalkNavigationTV,mSpotsListTV;
    private ListView mSpotsListLV;
    private ListView mGuideListLV;
    private ImageView mFrameIV;
    private ImageView mPause;
    private ImageView mClose;
    private RelativeLayout mNotice;
    private TextView mVocie_Prompt;
    private boolean isRightSpotsListViewVisible = false;
    private boolean isLeftSpotsListViewVisible = false;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private FavoriteDataBaseHelper mFavoriteDataBaseHelper;
    private List<Spots> mSpotsList;
    private Spots mSpots;
    private boolean isPause = false;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.btn_voice_default);
   private UniversalDialog dialog;
    private RelativeLayout mSpotsListLinearLayout;
    private TextView mBackgroundTV;
    private List<Spots> spotsList;
    private ProgressDialog mProgressDialog;
    private AnimationDrawable animationDrawable;
    private Bitmap mLayerBitmap;
    private List<Integer> mFavoriteForUser = new ArrayList<>();
    private UniversalDialog bottomDialog;
    public GudieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GudieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GudieFragment newInstance(String param1, String param2) {
        GudieFragment fragment = new GudieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        getSpotsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gudie, container, false);
        initViews(view);
        initBaiduMap();
        initGuideListViewAndSpotsListViewData();
        return view;
    }

    public void initViews(View view){
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mLoactionTV = (TextView) view.findViewById(R.id.tv_loaction);
        mWalkNavigationTV = (TextView) view.findViewById(R.id.tv_walk_navigetion);
        mSpotsListTV = (TextView) view.findViewById(R.id.tv_spots_list);
        mSpotsListLV = (ListView) view.findViewById(R.id.lv_spots_list);
        mGuideListLV = (ListView) view.findViewById(R.id.lv_guide_list);
        mFrameIV = (ImageView)view.findViewById(R.id.iv_frame);
        mPause = (ImageView)view.findViewById(R.id.img_pause);
        mClose = (ImageView)view.findViewById(R.id.img_close);
        mNotice = (RelativeLayout)view.findViewById(R.id.rl_notice);
        mVocie_Prompt = (TextView)view.findViewById(R.id.tv_voice_prompt);
        mSpotsListLinearLayout = (RelativeLayout) view.findViewById(R.id.ll_spots_list);
        mBackgroundTV = (TextView) view.findViewById(R.id.tv_bg);
        mNotice.setVisibility(View.GONE);
        mClose.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mWalkNavigationTV.setOnClickListener(this);
        mSpotsListTV.setOnClickListener(this);
        mLoactionTV.setOnClickListener(this);
        mBackgroundTV.setOnClickListener(this);
        mBackgroundTV.requestFocus();
        mFrameIV.setBackgroundResource(R.drawable.anim_paly_audio);
        animationDrawable =(AnimationDrawable) mFrameIV.getBackground();
    }

    public void getSpotsList(){
        mSpotsList = mSpotsDataBaseHelper.getAllSpots();
    }

    public void initGuideListViewAndSpotsListViewData(){
        SpotsDataBaseHelper spotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        spotsList = spotsDataBaseHelper.getAllSpots();
        if (spotsList != null && spotsList.size()>0){
           mGuideListAdapter = new GuideListAdapter(spotsList,getActivity());
            mSpotsListAdapter = new SpotsListAdapter(spotsList,getActivity());
            mSpotsListLV.setAdapter(mSpotsListAdapter);
            mGuideListLV.setAdapter(mGuideListAdapter);
        }
        mGuideListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Utils.openBaiduMap(getActivity(),120.85622,31.11700,"123","步行导航");
                mSpots = spotsList.get(i);
                showNaviDialog();
                setTabResourceState();
                mSpotsListLinearLayout.setVisibility(View.GONE);
                isLeftSpotsListViewVisible = false;
                mGuideListLV.setVisibility(View.GONE);
            }
        });

        mSpotsListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTabResourceState();
                mSpotsListLinearLayout.setVisibility(View.GONE);
                if (position == 0){
                    locationToZhouzhuang(31.11500, 120.85522);
                }else if(position == 1){
                    locationToZhouzhuang(31.11500, 120.85742);
                }else{
//                    locationToZhouzhuang(Double.parseDouble(((Spots)mGuideListAdapter.getItem(position)).getLat()),Double.parseDouble(((Spots)mGuideListAdapter.getItem(position)).getLng()));
                    locationToZhouzhuang(31.121492,120.85681);
                }
                isRightSpotsListViewVisible = false;
                mSpotsListLV.setVisibility(View.GONE);
                showSpotsDialog(spotsList.get(position));
            }
        });
    }

    public void isShowRightSpotList(){
        isLeftSpotsListViewVisible = false;
        mGuideListLV.setVisibility(View.GONE);
        mSpotsListLinearLayout.setVisibility(View.GONE);
        mWalkNavigationTV.setBackgroundResource(R.drawable.navigation_selector);
        final Handler handler=new Handler();
        Runnable runnable=new Runnable(){
            @Override
            public void run() {
                mSpotsListLV.setVisibility(isRightSpotsListViewVisible ? View.GONE : View.VISIBLE);
                mSpotsListLinearLayout.setVisibility(isRightSpotsListViewVisible ? View.GONE : View.VISIBLE);
                mSpotsListTV.setBackgroundResource(isRightSpotsListViewVisible?R.drawable.spot_list_selector :  R.mipmap.btn_scenicspot_focus);
                isRightSpotsListViewVisible = !isRightSpotsListViewVisible;
            }
        };
        handler.postDelayed(runnable, 100);
    }

    public void isShowLeftSpotList(){
        isRightSpotsListViewVisible = false;
        mSpotsListLV.setVisibility(View.GONE);
        mSpotsListLinearLayout.setVisibility(View.GONE);
        mSpotsListTV.setBackgroundResource(R.drawable.spot_list_selector);
        Handler handler=new Handler();
        Runnable runnable= new Runnable(){
            @Override
            public void run() {
                mGuideListLV.setVisibility(isLeftSpotsListViewVisible ? View.GONE : View.VISIBLE);
                mSpotsListLinearLayout.setVisibility(isLeftSpotsListViewVisible ? View.GONE : View.VISIBLE);
                mWalkNavigationTV.setBackgroundResource(isLeftSpotsListViewVisible ?R.drawable.navigation_selector : R.mipmap.btn_guide_focus);
                isLeftSpotsListViewVisible = !isLeftSpotsListViewVisible;
            }

        };
        handler.postDelayed(runnable, 100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_loaction:
//                // 开启定位图层
//                mBaiduMap.setMyLocationEnabled(true);
//                // 定位初始化
//                mLocationClient = new LocationClient(getActivity());
//                mLocationClient.registerLocationListener(myListener);
//                LocationClientOption option = new LocationClientOption();
//                option.setOpenGps(true); // 打开gps
//                option.setCoorType("bd09ll"); // 设置坐标类型
//                option.setScanSpan(1000);
//                mLocationClient.setLocOption(option);
//                mLocationClient.start();

                locationToZhouzhuang(31.121492,120.85681);
                break;
            case R.id.bt_audio_play:
            if (!isAudioExit(String.valueOf(mSpots.getCreateTime()))){
                    downloadAudio();
                }else{
                    playAudio(mSpots.getVideoLocation());
                }
                dialog.dismiss();
                break;
            case R.id.bt_detail:
                Intent intent  = new Intent();
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("URL",mSpots.getDetailUrl());
                intent.putExtra("SpotsId",mSpots.getPid());
                startActivity(intent);
                dialog.dismiss();
                break;
            case R.id.tv_walk_navigetion:
                isShowLeftSpotList();
                break;
            case R.id.tv_navigation_map:
                showNaviDialog();
                dialog.dismiss();
                break;
            case R.id.bt_favorite:
                if (!Utils.isAutoLogin(getActivity())){
                    Toast.makeText(getActivity(),"您还没登录，请先登录...",Toast.LENGTH_SHORT).show();
                }else{
                    if (!mFavoriteDataBaseHelper.isFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(getActivity(),"pid"),mSpots.getPid())){
                        addFavorite();
                    }else{
                        cancelCollection();
                    }
                }
                dialog.dismiss();
                break;
            case R.id.tv_spots_list:
                isShowRightSpotList();
                break;
            case R.id.img_pause:
                SoundBroadUtils.getInstance().pauseSound(isPause);
                if(isPause){
                    mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_pause));
                    animationDrawable.start();
                    mVocie_Prompt.setText("正在为您播放"+mSpots.getTitle()+"语音导览...");
                }else {
                    mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_play));
                    animationDrawable.stop();
                    mVocie_Prompt.setText("当前暂停播放"+mSpots.getTitle()+"语音导览");
                }
                isPause = !isPause;
                break;
            case R.id.img_close:
                mNotice.setVisibility(View.GONE);
                animationDrawable.stop();
                SoundBroadUtils.getInstance().stopSound();
                break;
            case R.id.tv_close:
                dialog.dismiss();
                break;
            case R.id.tv_bg:
                isRightSpotsListViewVisible = false;
                mSpotsListLV.setVisibility(View.GONE);
                mSpotsListLinearLayout.setVisibility(View.GONE);
                isLeftSpotsListViewVisible = false;
                mGuideListLV.setVisibility(View.GONE);
                setTabResourceState();
                break;
            case R.id.tv_open_baidu:
                if (mSpots.getLng() != null && mSpots.getLat() != null){
                    Utils.openBaiduMap(getActivity(),Double.parseDouble(mSpots.getLng()),Double.parseDouble(mSpots.getLat()),"123","步行导航");
                }else{
                    Toast.makeText(getActivity(),"未能获取经纬度",Toast.LENGTH_SHORT).show();
                }

                bottomDialog.dismiss();
                break;
            case R.id.tv_open_gaode:
                Utils.goToNaviActivity(getActivity(),"test",null,mSpots.getLat(),mSpots.getLng(),"1","2");
                bottomDialog.dismiss();
                break;
            case R.id.btn_cancel:
                bottomDialog.dismiss();
                break;
        }
    }

    public void setTabResourceState(){
        mSpotsListTV.setBackgroundResource(R.drawable.spot_list_selector);
        mWalkNavigationTV.setBackgroundResource(R.drawable.navigation_selector);
    }

    public boolean isAudioExit(String audioName){
       return Utils.fileIsExists(SdCardUtil.getSdPath() + SdCardUtil.FILEDIR + SdCardUtil.FILEAUDIO +"/"+audioName + ".mp3");
    }

    public void downloadAudio(){
        final String filePath = SdCardUtil.getSdPath() + SdCardUtil.FILEDIR + SdCardUtil.FILEAUDIO +"/"+mSpots.getCreateTime() + ".mp3";
        File saveFile = new File(filePath);
        String[] allowedContentTypes = new String[] { ".*" };
        AsyncHttpClientUtils.getInstance().downLoadFile(mSpots.getVideoLocation(),new BinaryHttpResponseHandler(allowedContentTypes){

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(getActivity(),"" ,"正在下载语音, 请稍候...", true, true);
                mProgressDialog.show();
                mNotice.setVisibility(View.GONE);
                animationDrawable.stop();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
                if (Utils.fileIsExists(filePath)){
                    downloadFinish(filePath);
                }else {
                    Toast.makeText(getActivity(),"未能正确下载音乐",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(),"下载失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void downloadFinish(String filePath){
        playAudio(filePath);
    }

    public void playAudio(String filePath){

      SoundBroadUtils.getInstance().playSound(getActivity(), filePath);
        mNotice.setVisibility(View.VISIBLE);
        animationDrawable.start();
        mPause.setImageDrawable(getResources().getDrawable(R.mipmap.button_pause));
        mVocie_Prompt.setText("正在为您播放" +mSpots.getTitle()+"语音导览...");

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

    private void initBaiduMap(){
        locationToZhouzhuang(31.121492,120.85681);
        addLayerToMap();

    }

    public void locationToZhouzhuang(double lat,double lng){
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        MyLocationData locData = new MyLocationData.Builder().accuracy(100) .direction(90.0f).latitude(Constants.lat).longitude(Constants.lng).build();
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng ll = new LatLng(lat,lng);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 15.8f);//设置缩放比例
        mBaiduMap.animateMapStatus(u);
    }

    public void addLayerToMap(){
        if (mLayerBitmap == null){
            mLayerBitmap = getLayerBitmap(R.mipmap.layer);
        }
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromBitmap(mLayerBitmap);
        LatLng northeast = new LatLng(31.134000, 120.87520);
        LatLng southwest = new LatLng(31.102000, 120.84510);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();
        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);
        initAndAddLayer();
    }

    private void initAndAddLayer(){
        for (int i = 0; i < mSpotsList.size(); i++) {
            LatLng latlng;
            if (mSpotsList.get(i).getLat4show() != null && mSpotsList.get(i).getLng4show() != null){
                latlng = new LatLng(Double.parseDouble(mSpotsList.get(i).getLat4show()),Double.parseDouble(mSpotsList.get(i).getLng4show()));
                MarkerOptions ooA = new MarkerOptions().position(latlng).icon(bdA).zIndex(9).draggable(false);
                ooA.animateType(MarkerOptions.MarkerAnimateType.grow);
                Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
                marker.setTitle(mSpotsList.get(i).getTitle());
            }else{
                Log.i("tttt",mSpotsList.get(i).getTitle());
            }
        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                mSpots = mSpotsDataBaseHelper.getSpotsByName(marker.getTitle());
                showSpotsDialog(mSpots);
                return true;
            }
        });
    }

    private void showNaviDialog(){
        bottomDialog = new UniversalDialog(getActivity());
        bottomDialog.show();//显示对话框

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bottom_navi, null);
        //初始化控件
        TextView openBaidu = (TextView) view.findViewById(R.id.tv_open_baidu);
        TextView openGaode = (TextView) view.findViewById(R.id.tv_open_gaode);
        TextView cancel = (TextView) view.findViewById(R.id.btn_cancel);
        openBaidu.setTypeface(Utils.getType(getActivity(),0));
        openGaode.setTypeface(Utils.getType(getActivity(),0));
        cancel.setTypeface(Utils.getType(getActivity(),0));
        openBaidu.setOnClickListener(this);
        openGaode.setOnClickListener(this);
        cancel.setOnClickListener(this);

        //将布局设置给Dialog
        bottomDialog.setContentView(view);
        bottomDialog.setDialogGravity(UniversalDialog.DialogGravity.CENTERBOTTOM);
//        bottomDialog.setBottomIn();
        bottomDialog.setWH(getActivity(),getActivity().getWindowManager());
    }

    private void showSpotsDialog(Spots spots){
        mSpots = spots;
        if (dialog == null){
            dialog = new UniversalDialog(getActivity());
        }
        dialog.show();
        dialog.setContentView(initDialogView(mSpots));
        dialog.setDialogGravity(UniversalDialog.DialogGravity.CENTER);
        dialog.setTitle(mSpots.getTitle());
    }

    private View initDialogView(Spots spots){
        View mDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_guide,null);
        Button audioPlay  = (Button) mDialogView.findViewById(R.id.bt_audio_play);
        Button detail = (Button) mDialogView.findViewById(R.id.bt_detail);
        Button navigation = (Button) mDialogView.findViewById(R.id.tv_navigation_map);
        Button favorite = (Button) mDialogView.findViewById(R.id.bt_favorite);
        TextView tv_spot_title = (TextView) mDialogView.findViewById(R.id.tv_spot_title);
        ImageView tv_close = (ImageView) mDialogView.findViewById(R.id.tv_close);
        boolean isNoFavor = mFavoriteDataBaseHelper.isFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(getActivity(),"pid"),mSpots.getPid());
        favorite.setBackgroundResource(isNoFavor?R.drawable.dialog_selector_favor:R.drawable.dialog_selector_add_favor);
        tv_spot_title.setText(spots.getTitle());
        detail.setOnClickListener(this);
        audioPlay.setOnClickListener(this);
        navigation.setOnClickListener(this);
        favorite.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        return mDialogView;
    }

    public class MyLocationListenner implements BDLocationListener{
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

            LatLng ll = new LatLng(31.121492,120.85681);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(15.8f);
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
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLayerBitmap.recycle();
    }

    private void addFavorite(){
        RequestParams params = new RequestParams();
        params.put("spotId", mSpots.getPid());
        params.put("userId", SharedPreferencesUtils.getInt(getActivity(),"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Favorites favorite = new Favorites();
                favorite.setUserId(SharedPreferencesUtils.getInt(getActivity(),"pid"));
                favorite.setSpotsId(mSpots.getPid());
                mFavoriteDataBaseHelper.addFavorite(favorite);
                Toast.makeText(getActivity(),"收藏成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(),"收藏失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap getLayerBitmap(int resourceId){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getActivity().getResources(),resourceId, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 2650*2650);
        opts.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(getActivity().getResources(),resourceId, opts);
        } catch (OutOfMemoryError err) {
        }
        return null;
    }

    public void cancelCollection(){
        RequestParams params = new RequestParams();
        params.put("spotId",mSpots.getPid());
        params.put("userId", SharedPreferencesUtils.getInt(getActivity(),"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_CANCEL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Favorites favorite = mFavoriteDataBaseHelper.getFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(getActivity(),"pid"),mSpots.getPid());
                mFavoriteDataBaseHelper.deleteFavorite(favorite);
                Toast.makeText(getActivity(),"取消收藏成功",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getActivity(),"取消收藏失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static int computeSampleSize(BitmapFactory.Options
                                                options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize
                = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize
                <= 8) {
            roundedSize
                    = 1;
            while (roundedSize
                    < initialSize) {
                roundedSize
                        <<= 1;
            }
        } else {
            roundedSize
                    = (initialSize + 7)
                    / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options
                                                        options,
                                                int minSideLength, int maxNumOfPixels) {
        double w
                = options.outWidth;
        double h
                = options.outHeight;

        int lowerBound
                = (maxNumOfPixels == -1)
                ? 1 :
                (int)
                        Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound
                = (minSideLength == -1)
                ? 128 :
                (int)
                        Math.min(Math.floor(w / minSideLength),
                                Math.floor(h
                                        / minSideLength));

        if (upperBound
                < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels
                == -1)
                &&
                (minSideLength
                        == -1)) {
            return 1;
        } else if (minSideLength
                == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
