package com.adolf.zhouzhuang.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.adolf.zhouzhuang.R;

/**
 * Created by Administrator on 2016/10/17.
 */

public class LoadingView  extends ImageView{


    private LayoutInflater mLayoutInflater;
    private RelativeLayout mRLayout;


    Context mContext;
    public LoadingView(Context context) {
        super(context);


        mContext = context;
    }


    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
//        mRLayout = (RelativeLayout) mLayoutInflater.inflate(
//                R.layout.progress_dialog,
//                LoadingView.this, true);
        ImageView mLoadingImage = (ImageView)mRLayout.findViewById(R.id.loading_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImage.getDrawable();
        animationDrawable.start();
    }
}
