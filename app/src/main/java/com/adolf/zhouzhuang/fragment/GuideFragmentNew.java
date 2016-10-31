package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adolf.zhouzhuang.R;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.SupportMapFragment;

public class GuideFragmentNew extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private AMap mMap;

    public GuideFragmentNew() {
    }

    private void setUpMapIfNeeded() {
//        if (mMap == null) {
//            mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager()
//                    .findFragmentById(R.id.map)).getMap();
//        }
    }

    public static GuideFragmentNew newInstance() {
        GuideFragmentNew fragment = new GuideFragmentNew();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_new, container, false);
        setUpMapIfNeeded();
        return view;
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
