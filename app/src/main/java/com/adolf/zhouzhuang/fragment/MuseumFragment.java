package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.MainActivity;
import com.adolf.zhouzhuang.activity.NewsActivity;
import com.adolf.zhouzhuang.activity.WebViewActivity;
import com.adolf.zhouzhuang.activity.ZhouzhuangLibActivity;
import com.adolf.zhouzhuang.banner.Banner;
import com.adolf.zhouzhuang.banner.BannerConfig;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.BannerObj;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuseumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuseumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuseumFragment extends BaseFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Banner mBanner;
    private String[] mImages,mTitles;
    private TextView mNewsTv;
    private static final String excihbitURL01 = "http://www.gcmai.com/360/001.html";
    private static final String excihbitURL02 = "http://www.gcmai.com/360/006.html";
    private static final String excihbitURL03 = "http://www.gcmai.com/360/009.html";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_museum, container, false);
        mBanner = (Banner) view.findViewById(R.id.banner);
        TextView tv_info = (TextView)view.findViewById(R.id.tv_info);
        mNewsTv = (TextView) view.findViewById(R.id.tv_news);
        TextView tv_exhibition = (TextView) view.findViewById(R.id.tv_exhibition);
        ImageView image_pic1 = (ImageView) view.findViewById(R.id.image_pic1);
        ImageView image_pic2 = (ImageView) view.findViewById(R.id.image_pic2);
        ImageView image_pic3 = (ImageView) view.findViewById(R.id.image_pic3);
        tv_exhibition.setOnClickListener(this);
        tv_info.setOnClickListener(this);
        mNewsTv.setOnClickListener(this);
        image_pic1.setOnClickListener(this);
        image_pic2.setOnClickListener(this);
        image_pic3.setOnClickListener(this);
        initBannerDefault();
        getBannerInfo();
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
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.BANNER,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                initBannnerData(GsonUtil.jsonToList(response,"data",BannerObj.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
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
        mBanner.setBackgroundResource(R.mipmap.bg_bannername);
        mBanner.setDelayTime(2500);
    }

    private void initBannerDefault(){
        Integer[] imagets = new Integer[]{R.mipmap.banner_default};
        String[] titles = new String[]{""};
        //显示圆形指示器和标题
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setImages(imagets);
        mBanner.setBannerTitle(titles);
        mBanner.setBackgroundResource(R.mipmap.bg_bannername);
        mBanner.setDelayTime(2500);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_info:
                startActivity(new Intent(getActivity(), ZhouzhuangLibActivity.class));
                break;
            case R.id.tv_news:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;
            case R.id.tv_exhibition:
                mListener.onFragmentInteraction(Uri.parse("exhibit"));
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.setBottomBarBackground(1);
                break;
            case  R.id.image_pic1:
                Intent intent =new Intent();
                intent.setClass(getActivity(),WebViewActivity.class);
                intent.putExtra("URL","file:///android_asset/360/001.html");
                intent.putExtra(WebViewActivity.NAME,"360展示");
                startActivity(intent);
                break;
            case R.id.image_pic2:
                Intent intent2 =new Intent();
                intent2.setClass(getActivity(),WebViewActivity.class);
                intent2.putExtra("URL","file:///android_asset/360/006.html");
                intent2.putExtra(WebViewActivity.NAME,"360展示");
                startActivity(intent2);
                break;
            case R.id.image_pic3:
                Intent intent3 =new Intent();
                intent3.setClass(getActivity(),WebViewActivity.class);
                intent3.putExtra("URL","file:///android_asset/360/009.html");
                intent3.putExtra(WebViewActivity.NAME,"360展示");
                startActivity(intent3);
                break;
        }
    }
}