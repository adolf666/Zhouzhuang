package com.adolf.zhouzhuang.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.adolf.zhouzhuang.R;

/**
 * Created by gpp on 2016/10/17 0017.
 */

public class LoadingProgressDialog extends ProgressDialog {

    private String mLoadingTip = "玩命加载中, 请稍等...";
    private ImageView mLoadingImage;

    public LoadingProgressDialog(Context context, String mLoadingTip) {
        super(context);
        this.mLoadingTip = mLoadingTip;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initData() {
    }

    public void setContent(String loadingTip) {
        mLoadingTip = loadingTip;
    }

    public void setContent(int resId) {
        mLoadingTip = getContext().getString(resId);
    }

    private void initView() {
        setContentView(R.layout.progress_dialog);
        mLoadingImage = (ImageView) findViewById(R.id.loading_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImage.getDrawable();
        animationDrawable.start();
    }


}
