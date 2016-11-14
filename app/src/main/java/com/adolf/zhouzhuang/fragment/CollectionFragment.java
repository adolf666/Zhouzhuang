package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.WebViewActivity;
import com.adolf.zhouzhuang.adapter.ExhibitAdapter;
import com.adolf.zhouzhuang.adapter.NewsAdapter;
import com.adolf.zhouzhuang.adapter.PanoramaAdapter;
import com.adolf.zhouzhuang.adapter.ViewPagerAdapter;
import com.adolf.zhouzhuang.object.Exhibit;
import com.adolf.zhouzhuang.object.PanoramaObject;
import com.adolf.zhouzhuang.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionFragment extends BaseFragment implements View.OnClickListener{
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
    private CustomViewPager viewPager;
    private TextView tempExhibit;
    private TextView displayExhibit;
    private TextView spotsExhibit;
    private List<String> mWenWuTitle = new ArrayList<>();
    private List<String> mWenWuDesc = new ArrayList<>();
    private List<String> mWenWuPic = new ArrayList<>();
    private List<String> mWenWuDetailUrl = new ArrayList<>();
    List<PanoramaObject> panoramaObjectList;

    public CollectionFragment() {
        // Required empty public constructor
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
        initViewPager();
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
        viewPager = (CustomViewPager) view.findViewById(R.id.vPager);
        tempExhibit = (TextView) view.findViewById(R.id.text1);
        displayExhibit = (TextView) view.findViewById(R.id.text2);
        spotsExhibit = (TextView) view.findViewById(R.id.text3);

        tempExhibit.setOnClickListener(this);
        displayExhibit.setOnClickListener(this);
        spotsExhibit.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                tabSwitch( position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initViewPager() {
        List<Fragment> fragmentArrayList = new ArrayList<>();
        ExhibitsFragment exhibitsFragment1= ExhibitsFragment.newInstance(2) ;
        ExhibitsFragment exhibitsFragment2 = ExhibitsFragment.newInstance(3) ;
        ExhibitsFragment exhibitsFragment3= ExhibitsFragment.newInstance(1) ;

        fragmentArrayList.add(exhibitsFragment1);
        fragmentArrayList.add(exhibitsFragment2);
        fragmentArrayList.add(exhibitsFragment3);
        viewPager.setScrollble(true);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragmentArrayList));
    }



    public void initViewPagerViews(final List<Exhibit> exhibits ,final int index){

        ListView lv = new ListView(getActivity());
        NewsAdapter adapter = new NewsAdapter(getActivity(),exhibits);
        lv.setAdapter(adapter);
        lv.setSelector(R.mipmap.banner_default);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent  = new Intent();
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("URL",exhibits.get(position).getDetailUrl());
                if(2==index){
                    intent.putExtra(WebViewActivity.NAME,exhibits.get(position).getTitle());
                }else if(0==index){
                    intent.putExtra(WebViewActivity.NAME,"陈列展览");
                }
                startActivity(intent);
            }
        });
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private  void tabSwitch(int position){
        tempExhibit.setBackground(position==0?getResources().getDrawable(R.mipmap.tab01_focus):getResources().getDrawable(R.mipmap.tab01_default));
        displayExhibit.setBackground(position==1?getResources().getDrawable(R.mipmap.tab02_focus):getResources().getDrawable(R.mipmap.tab02_default));
        spotsExhibit.setBackground(position==2?getResources().getDrawable(R.mipmap.tab03_focus):getResources().getDrawable(R.mipmap.tab03_default));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text1:
                viewPager.setCurrentItem(0);
                tabSwitch(0);
                break;
            case R.id.text2:
                viewPager.setCurrentItem(1);
                tabSwitch(1);
                break;
            case R.id.text3:
                viewPager.setCurrentItem(2);
                tabSwitch(2);
        }
    }
    private void getPanoramaData(){


        panoramaObjectList = new ArrayList<PanoramaObject>() ;
        PanoramaObject panoramaObject1 = new PanoramaObject();
        PanoramaObject panoramaObject2 = new PanoramaObject();
        PanoramaObject panoramaObject3 = new PanoramaObject();
        PanoramaObject panoramaObject4 = new PanoramaObject();
        PanoramaObject panoramaObject5 = new PanoramaObject();
        PanoramaObject panoramaObject6 = new PanoramaObject();
        PanoramaObject panoramaObject7 = new PanoramaObject();
        PanoramaObject panoramaObject8 = new PanoramaObject();
        PanoramaObject panoramaObject9 = new PanoramaObject();

        panoramaObject1.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian01));
        panoramaObject2.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian02));
        panoramaObject3.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian03));
        panoramaObject4.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian04));
        panoramaObject5.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian05));
        panoramaObject6.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian06));
        panoramaObject7.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian07));
        panoramaObject8.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian08));
        panoramaObject9.setImage(getActivity().getResources().getDrawable(R.mipmap.fengmian09));



        panoramaObject1.setName("飞禽纹黑皮陶双耳壶");
        panoramaObject1.setDesc("此罐又称贯耳壶，通体乌黑，从罐腹至腔，手工绘制水禽飞鸟五十只。其单线造型简练明快，似水鸟隔水而飞，生动活泼。1995年被定为国家一级文物。");

        panoramaObject2.setName("良渚文化贯耳陶壶");
        panoramaObject2.setDesc("黑衣贯耳壶pottery pot 为良渚显贵先民之用具");

        panoramaObject3.setName("良渚文化陶鼎");
        panoramaObject3.setDesc("陶鼎 pottery pot 此鼎浅足，形小，腹部微鼓，是良渚较早的炊器，腹部有后期人工开凿的一个孔，不知何因，待考证。");

        panoramaObject4.setName("春秋陶罐");
        panoramaObject4.setDesc("硬陶罐hard pottery pot 此陶罐是西周春秋时代贮器，地平有细边，小口沿体形扁圆，罐体上部斜折纹，下部是回纹图案，酱紫色十分古朴美观。");

        panoramaObject5.setName("良渚文化陶壶");
        panoramaObject5.setDesc("陶盉pottery container为良渚炊具。");

        panoramaObject6.setName("良渚文化石犁");
        panoramaObject6.setDesc("石犁 stone plough:an important tool in ploughing land,made of black stone 黑色页岩制成，为良渚时期重要的耕田工具。");

        panoramaObject7.setName("良渚文化石斧2块");
        panoramaObject7.setDesc("石斧stone axe 用于砍劈、剁、砍肉类和野菜等。");

        panoramaObject8.setName("良渚文化石镞3块");
        panoramaObject8.setDesc("石镞stone weapon 为先民狩猎和防御的重要工具。");

        panoramaObject9.setName("甲骨文吴字陶罐");
        panoramaObject9.setDesc("");

        panoramaObjectList.add(panoramaObject1);
        panoramaObjectList.add(panoramaObject2);
        panoramaObjectList.add(panoramaObject3);
        panoramaObjectList.add(panoramaObject4);
        panoramaObjectList.add(panoramaObject5);
        panoramaObjectList.add(panoramaObject6);
        panoramaObjectList.add(panoramaObject7);
        panoramaObjectList.add(panoramaObject8);
        panoramaObjectList.add(panoramaObject9);

        ListView listview = new ListView(getActivity());
        PanoramaAdapter panoramaAdapter = new PanoramaAdapter(getActivity(),panoramaObjectList);
        listview .setAdapter(panoramaAdapter);
        listview .setSelector(R.mipmap.banner_default);
        mViewPagerViews.set(1,listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent();
                intent.setClass(getActivity(),WebViewActivity.class);
                if(i==0){
                    intent.putExtra("URL","file:///android_asset/360/001.html");
                }else if (i==1){
                    intent.putExtra("URL","file:///android_asset/360/002.html");
                }else if (i==2){
                    intent.putExtra("URL","file:///android_asset/360/003.html");
                }else if (i==3){
                    intent.putExtra("URL","file:///android_asset/360/004.html");
                }else if (i==4){
                    intent.putExtra("URL","file:///android_asset/360/005.html");
                }else if (i==5){
                    intent.putExtra("URL","file:///android_asset/360/006.html");
                }else if (i==6){
                    intent.putExtra("URL","file:///android_asset/360/007.html");
                }else if (i==7){
                    intent.putExtra("URL","file:///android_asset/360/008.html");
                }else {
                    intent.putExtra("URL","file:///android_asset/360/009.html");
                }
                intent.putExtra(WebViewActivity.NAME,"360展示");
                startActivity(intent);
            }
        });
    }
}
