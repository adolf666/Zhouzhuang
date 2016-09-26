package com.adolf.zhouzhuang.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.activity.PersonCollectActivity;
import com.adolf.zhouzhuang.activity.PersonSettingActivity;
import com.adolf.zhouzhuang.activity.PersonSuggestionActivity;
import com.adolf.zhouzhuang.activity.PersonalInfoActivity;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StrategyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StrategyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StrategyFragment extends BaseFragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView  mPersonInfo;
    private TextView  mSuggestion;
    private TextView  mSetting;
    private TextView  mCollect;
    private TextView  mUserName;

    public StrategyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StrategyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StrategyFragment newInstance(String param1, String param2) {
        StrategyFragment fragment = new StrategyFragment();
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
        View view =inflater.inflate(R.layout.activity_personal_center, container, false);
        initView(view);
        return view;
    }
    private void initView(View view){
        mUserName = (TextView)view.findViewById(R.id.tv_user_name);
        LoginObj obj= GsonUtil.jsonToBean(SharedPreferencesUtils.getString(getActivity(), "AccountInfo",""),LoginObj.class);
        if(obj != null && obj.getUsername()!=null){
            mUserName.setText(obj.getUsername());
        }
        mPersonInfo = (TextView )view.findViewById(R.id.tv_person_info);
        mCollect = (TextView)view.findViewById(R.id.tv_collect);
        mSetting = (TextView ) view.findViewById(R.id.tv_setting);
        mSuggestion = (TextView) view.findViewById(R.id.tv_suggestion);
        mUserName.setTypeface(Utils.getType(getActivity(),3));
        mPersonInfo.setTypeface(Utils.getType(getActivity(),3));
        mCollect.setTypeface(Utils.getType(getActivity(),3));
        mSetting.setTypeface(Utils.getType(getActivity(),3));
        mSuggestion.setTypeface(Utils.getType(getActivity(),3));
        mPersonInfo.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
        mCollect.setOnClickListener(this);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_person_info:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), PersonalInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_setting:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), PersonSettingActivity.class);
                startActivity(intent2);

                break;
            case R.id.tv_suggestion:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), PersonSuggestionActivity.class);
                startActivity(intent3);
                break;

            case R.id.tv_collect:
                Intent intent4 = new Intent();
                intent4.setClass(getActivity(), PersonCollectActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
