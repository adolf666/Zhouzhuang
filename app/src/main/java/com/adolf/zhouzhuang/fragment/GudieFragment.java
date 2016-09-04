package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
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
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

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
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private Marker mMarkerE;
    private Marker mMarkerF;
    private RelativeLayout relative1,relative2,relative3;
    private SelectPopupWindow mPopupWindow = null;
    private SelectPopupWindow mPopupWindow2 = null;
    private SelectPopupWindow mPopupWindow3 = null;
    private String[] Strings1 = {"双桥","沈厅","张厅","沈万三故居","全福长桥","周庄博物馆","南湖秋月园","逸飞之家"};
    private String[] Strings2 = {"双桥2","沈厅2","张厅2","沈万三故居2","全福长桥2","周庄博物馆2","南湖秋月园2","逸飞之家2"};
    private String[] Strings3 = {"双桥3","沈厅3","张厅3","沈万三故居3","全福长桥3","周庄博物馆3","南湖秋月园3","逸飞之家3"};
    private OnFragmentInteractionListener mListener;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gudie, container, false);
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mLoactionBT = (Button) view.findViewById(R.id.bt_loaction);
        relative1 = (RelativeLayout) view.findViewById(R.id.relative1);
        relative2 = (RelativeLayout) view.findViewById(R.id.relative2);
        relative3 = (RelativeLayout) view.findViewById(R.id.relative3);
        mLoactionBT.setOnClickListener(this);
        relative1.setOnClickListener(this);
        relative2.setOnClickListener(this);
        relative3.setOnClickListener(this);
        initBaiduMap();
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
            case R.id.relative1://
                if(mPopupWindow == null){
                    mPopupWindow = new SelectPopupWindow(Strings1,getActivity(),selectCategory);
                }
                if (mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }else {
                    mPopupWindow.showAsDropDown(relative1, -5, 10);
                }

                break;
            case R.id.relative2:
                if(mPopupWindow2 == null){
                    mPopupWindow2 = new SelectPopupWindow(Strings2,getActivity(),selectCategory);
                }
                if (mPopupWindow2.isShowing()){
                    mPopupWindow2.dismiss();
                }else {
                    mPopupWindow2.showAsDropDown(relative1, -5, 10);
                }
                break;
            case R.id.relative3:
                if(mPopupWindow3 == null){
                    mPopupWindow3 = new SelectPopupWindow(Strings3,getActivity(),selectCategory);
                }
                if (mPopupWindow3.isShowing()){
                    mPopupWindow3.dismiss();
                }else {
                    mPopupWindow3.showAsDropDown(relative1, -5, 10);
                }
                break;
        }
    }
    /**
     * 选择完成回调接口
     */
    private SelectPopupWindow.SelectCategory selectCategory=new SelectPopupWindow.SelectCategory() {
        @Override
        public void selectCategory(int parentSelectposition,int childrenSelectposition) {
            String parentStr=Strings1[parentSelectposition];

            Toast.makeText(getActivity(), "选择了:"+parentStr, Toast.LENGTH_SHORT).show();
        }
    };
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
    }

    private void initBaiduMap(){
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
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
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.mipmap.laier);
        LatLng southwest = new LatLng(31.111000, 120.84622);
        LatLng northeast = new LatLng(31.121222, 120.86688);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();
        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);
        addLayer();
    }

    private void addLayer(){
        LatLng llA = new LatLng(31.115492,120.85681);
        LatLng llB = new LatLng(31.114821, 120.857199);
        LatLng llC = new LatLng(31.114723, 120.857541);
        LatLng llD = new LatLng(31.384985, 120.736394);
        LatLng llE = new LatLng(31.384875, 120.735384);
        LatLng llF = new LatLng(31.384765, 120.734374);
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(9).draggable(false);
        ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        mMarkerA.setTitle("A");
        MarkerOptions ooB = new MarkerOptions().position(llB).icon(bdA).zIndex(5).draggable(false);
        ooB.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
        mMarkerB.setTitle("B");
        MarkerOptions ooC = new MarkerOptions().position(llC).icon(bdA).zIndex(5).draggable(false);
        ooC.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
        mMarkerC.setTitle("C");
        MarkerOptions ooD = new MarkerOptions().position(llD).icon(bdA).zIndex(5).draggable(false);
        ooD.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
        mMarkerD.setTitle("D");
        MarkerOptions ooE = new MarkerOptions().position(llE).icon(bdA).zIndex(5).draggable(false);
        ooE.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerE = (Marker) (mBaiduMap.addOverlay(ooE));
        mMarkerE.setTitle("E");
        MarkerOptions ooF = new MarkerOptions().position(llF).icon(bdA).zIndex(5).draggable(false);
        ooF.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerF = (Marker) (mBaiduMap.addOverlay(ooF));
        mMarkerF.setTitle("F");

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Toast.makeText(getActivity(),"点击了"+ marker.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
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
}
