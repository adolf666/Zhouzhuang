package com.adolf.zhouzhuang.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.adapter.GuideListAdapter;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.util.Constants;
import com.adolf.zhouzhuang.util.SdCardUtil;
import com.adolf.zhouzhuang.util.SoundBroadUtils;
import com.adolf.zhouzhuang.util.UniversalDialog;
import com.adolf.zhouzhuang.widget.SelectPopupWindow;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
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
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GudieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GudieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GudieFragment extends BaseFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
    private Button mLoactionBT;
    boolean isFirstLoc = true; // 是否首次定位

    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private Marker mMarkerE;
    private Marker mMarkerF;
    private OnFragmentInteractionListener mListener;
    private GuideListAdapter adapter;

    private TextView mWalkNavigationTV,mLineRecommend,mSpotsListTV;
    private ListView mSpotsListLV;
    private ImageView mPause;
    private boolean isSpotsListViewVisible = false;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private List<Spots> mSpotsList;
    private Spots mSpots;
    private boolean isPause = false;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_marka);

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
        initSpotsListViewData();
//        mLoactionBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
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
//
//            }
//        });
        return view;
    }

    public void initViews(View view){
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mLoactionBT = (Button) view.findViewById(R.id.bt_loaction);
        mWalkNavigationTV = (TextView) view.findViewById(R.id.tv_walk_navigetion);
        mLineRecommend = (TextView) view.findViewById(R.id.tv_recommend_line);
        mSpotsListTV = (TextView) view.findViewById(R.id.tv_spots_list);
        mSpotsListLV = (ListView) view.findViewById(R.id.lv_spots_list);
        mPause = (ImageView)view.findViewById(R.id.img_pause);
        mPause.setOnClickListener(this);
        mWalkNavigationTV.setOnClickListener(this);
        mLineRecommend.setOnClickListener(this);
        mSpotsListTV.setOnClickListener(this);
        mLoactionBT.setOnClickListener(this);
    }

    public void getSpotsList(){
        mSpotsList = mSpotsDataBaseHelper.getAllSpots();
    }

    public void initSpotsListViewData(){
        SpotsDataBaseHelper spotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        List<Spots> spotsList = spotsDataBaseHelper.getAllSpots();
        if (spotsList != null && spotsList.size()>0){
           adapter = new GuideListAdapter(spotsList,getActivity());
            mSpotsListLV.setAdapter(adapter);
        }
        mSpotsListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setmSelectedIndex(position);
                isSpotsListViewVisible = false;
                mSpotsListLV.setVisibility(View.GONE);
            }
        });
    }

    public void isShowSpotList(){
        mSpotsListLV.setVisibility(isSpotsListViewVisible ? View.GONE : View.VISIBLE);
        isSpotsListViewVisible = !isSpotsListViewVisible;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_loaction:
                // 开启定位图层
                mBaiduMap.setMyLocationEnabled(true);
                // 定位初始化
                mLocationClient = new LocationClient(getActivity());
                mLocationClient.registerLocationListener(myListener);
                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true); // 打开gps
                option.setCoorType("bd09ll"); // 设置坐标类型
                option.setScanSpan(1000);
                mLocationClient.setLocOption(option);
                mLocationClient.start();
                break;
            case R.id.bt_audio_play:
//              if (mSpots.getIsDownLoadAudio() == null || mSpots.getIsDownLoadAudio() == false){
//                    downloadAudio();
//                }else{
                    playAudio(mSpots.getVideoLocation());
//                }
                break;
            case R.id.bt_detail:
                Intent intent  = new Intent();
                intent.putExtra("URL",mSpots.getDetailUrl());
                startActivity(intent);
                break;
            case R.id.tv_walk_navigetion:

                break;
            case R.id.tv_recommend_line:

                break;
            case R.id.tv_spots_list:
                isShowSpotList();
                break;
            case R.id.img_pause:
                SoundBroadUtils.getInstance().pauseSound(isPause);
                isPause = !isPause;
                break;

        }
    }

    public void downloadAudio(){
        final String filename = SdCardUtil.getSdPath() + SdCardUtil.FILEDIR + SdCardUtil.FILEAUDIO +"/"+mSpots.getCreateTime() + ".mp3";
        File saveFile = new File(filename);
        HttpRequest.download(mSpots.getVideoLocation(),saveFile,new FileDownloadCallback(){

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onProgress(int progress, long networkSpeed) {
                super.onProgress(progress, networkSpeed);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onDone() {
                super.onDone();
                downloadFinish(filename);
            }
        });
    }

    public void downloadFinish(String filePath){
       // mSpots.setIsDownLoadAudio(true);
        mSpots.setVideoLocation(filePath);
        mSpotsDataBaseHelper.updateSpots(mSpots);
        playAudio(filePath);
    }

    public void playAudio(String filePath){

      SoundBroadUtils.getInstance().playSound(getActivity(), 0);


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
    }

    private void initBaiduMap(){
        locationToZhouzhuang();
        addLayerToMap();
//        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);
//        // 定位初始化
//        mLocationClient = new LocationClient(getActivity());
//        mLocationClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocationClient.setLocOption(option);
//        mLocationClient.start();

    }

    public void locationToZhouzhuang(){
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        MyLocationData locData = new MyLocationData.Builder().accuracy(100) .direction(90.0f).latitude(Constants.lat).longitude(Constants.lng).build();
        ;mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng ll = new LatLng(31.121492,120.85681);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16.3f);//设置缩放比例
        mBaiduMap.animateMapStatus(u);
    }

    public void addLayerToMap(){
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.mipmap.laier);
        LatLng southwest = new LatLng(31.111000, 120.84622);
        LatLng northeast = new LatLng(31.121222, 120.86688);
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
            if (i == 1){
                latlng = new LatLng(31.11700, 120.85622);
            }else{
                latlng = new LatLng(Double.parseDouble(mSpotsList.get(i).getLng()),Double.parseDouble(mSpotsList.get(i).getLat()));
            }
            MarkerOptions ooA = new MarkerOptions().position(latlng).icon(bdA).zIndex(9).draggable(false);
            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
            Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
            marker.setTitle(mSpotsList.get(i).getTitle());
        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                mSpots = mSpotsDataBaseHelper.getSpotsByName(marker.getTitle());
                UniversalDialog dialog = new UniversalDialog(getActivity());
                dialog.show();
                dialog.setContentView(initDialogView(mSpots));
                dialog.setDialogGravity(UniversalDialog.DialogGravity.CENTER);
                dialog.setTitle(mSpots.getTitle());
//                 dialog();
                Toast.makeText(getActivity(),"点击了"+ marker.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private View initDialogView(Spots spots){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_guide,null);
        ImageView soptIV = (ImageView) view.findViewById(R.id.iv_spots_img);
        Button audioPlay  = (Button) view.findViewById(R.id.bt_audio_play);
        Button detail = (Button) view.findViewById(R.id.bt_detail);
        TextView spotInfo = (TextView) view.findViewById(R.id.tv_spot_info);
        audioPlay.setOnClickListener(this);
        spotInfo.setText(spots.getBrief());
        return view;
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
            builder.target(ll).zoom(16.3f);
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

    }


    private void dialog(){
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        Toast.makeText(getActivity(), "导航" , Toast.LENGTH_SHORT).show();
                        //soundPool.pause(soundPool.play(1,1, 1, 0, 0, 1));
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        Toast.makeText(getActivity(), "详细" , Toast.LENGTH_SHORT).show();

                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        Toast.makeText(getActivity(), "语音", Toast.LENGTH_SHORT).show();
                        SoundBroadUtils.getInstance().playSound(getActivity(), 0);
                        break;
                }
            }
        };
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());  //先得到构造器
        builder.setTitle("沈厅"); //设置标题
        builder.setMessage("沈厅位于周庄富安桥东堍南侧的南市街上，坐东朝西，七进五门楼"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("导航",dialogOnclicListener);
        builder.setNeutralButton(" 语音", dialogOnclicListener);
        builder.setNegativeButton("详细", dialogOnclicListener);
        builder.create().show();
    }

}
