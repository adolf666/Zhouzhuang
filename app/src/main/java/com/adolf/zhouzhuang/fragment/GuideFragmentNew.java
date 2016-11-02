package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
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

import java.util.List;

public class GuideFragmentNew extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private MapView mapView;
    private AMap aMap;
    private GroundOverlay groundoverlay;
    private SpotsDataBaseHelper mSpotsDataBaseHelper;
    private List<Spots> mSpotsList;
    public GuideFragmentNew() {
    }

    private void initViews(View view){
        mapView = (MapView) view.findViewById(R.id.map);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
    }

    public void getSpotsList() {
        mSpotsList = mSpotsDataBaseHelper.getAllSpots();
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
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(false);
        for (int i = 0; i < mSpotsList.size(); i++) {
            if (mSpotsList.get(i).getLat4show() != null && mSpotsList.get(i).getLng4show() != null){
                LatLng latlng = new LatLng(Double.parseDouble(mSpotsList.get(i).getLat4show()), Double.parseDouble(mSpotsList.get(i).getLng4show()));
                markerOption.position(latlng);
                Marker marker = aMap.addMarker(new MarkerOptions().title(mSpotsList.get(i).getTitle()).
                        icon(BitmapDescriptorFactory.fromAsset("btn_voice_default.png")).draggable(false));
            }

        }



        aMap.addMarker(markerOption);
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
}
