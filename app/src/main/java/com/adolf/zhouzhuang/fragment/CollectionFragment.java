package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.adapter.ExhibitAdapter;
import com.adolf.zhouzhuang.adapter.NewsAdapter;
import com.adolf.zhouzhuang.object.Exhibit;
import com.adolf.zhouzhuang.resBody.ExhibitResponse;
import com.adolf.zhouzhuang.util.ServiceAddress;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<View> mViewPagerViews;
    private ExhibitAdapter mExhibitAdapter;
    private ViewPager viewPager;

    public CollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionFragment newInstance(String param1, String param2) {
        CollectionFragment fragment = new CollectionFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection,container,false);
        initViews(view);
        getExhibits("1");
        getExhibits("2");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void initViews(View view){
        mViewPagerViews = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mViewPagerViews.add(new View(getActivity()));
        }
        viewPager = (ViewPager) view.findViewById(R.id.vPager);

    }

    public void initViewPagerViews(List<Exhibit> exhibits ,int index){
        ListView lv = new ListView(getActivity());
        NewsAdapter adapter = new NewsAdapter(getActivity(),exhibits);
        lv.setAdapter(adapter);
        mViewPagerViews.set(index,lv);
        if (mExhibitAdapter == null){
            mExhibitAdapter = new ExhibitAdapter(mViewPagerViews,getActivity());
            viewPager.setAdapter(mExhibitAdapter);
        }else{
            mExhibitAdapter.notifyDataSetChanged();
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
    //获取陈列信息，type的值为0:新闻列表 ，1:临时展馆，2:陈列展馆
    public void getExhibits(final String types){
        RequestParams params = new RequestParams();
        params.addFormDataPart("type",types);
        HttpRequest.post(ServiceAddress.NEWS_EXHIBITION_TEMPORARY,params,new BaseHttpRequestCallback<ExhibitResponse>(){

            @Override
            protected void onSuccess(ExhibitResponse exhibitResponse) {
                super.onSuccess(exhibitResponse);
                initViewPagerViews(exhibitResponse.getData(), TextUtils.equals(types,"1")?0:1);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
