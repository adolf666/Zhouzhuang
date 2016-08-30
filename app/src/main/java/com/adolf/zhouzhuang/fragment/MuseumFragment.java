package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.object.BannerObj;
import com.adolf.zhouzhuang.resBody.BannerResponse;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuseumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuseumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuseumFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Banner mBanner;
    private String[] mImages,mTitles;

    private OnFragmentInteractionListener mListener;

    public MuseumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuseumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MuseumFragment newInstance(String param1, String param2) {
        MuseumFragment fragment = new MuseumFragment();
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
        getBannerInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_museum, container, false);
        mBanner = (Banner) view.findViewById(R.id.banner);
        return view;
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
    }

    public void getBannerInfo(){
        HttpRequest.post(ServiceAddress.BANNER,new BaseHttpRequestCallback<BannerResponse>(){

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            protected void onSuccess(BannerResponse baseApiResponse) {
                super.onSuccess(baseApiResponse);
                Toast.makeText(getActivity(),"ewef",Toast.LENGTH_SHORT).show();
                initBannnerData(baseApiResponse.getData());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void initBannnerData(List<BannerObj> bannerObjs){
        mImages = new String[bannerObjs.size()];
        mTitles = new String[bannerObjs.size()];
        for (int i = 0; i <bannerObjs.size() ; i++) {
            mImages[i] = bannerObjs.get(i).getImgLocation();
            mTitles[i] = bannerObjs.get(i).getName();
        }
        //显示圆形指示器和标题
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setImages(mImages);
        mBanner.setBannerTitle(mTitles);
        mBanner.setDelayTime(2500);
    }
}