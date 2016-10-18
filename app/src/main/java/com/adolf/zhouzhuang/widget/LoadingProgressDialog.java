package com.adolf.zhouzhuang.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;

/**
 * Created by gpp on 2016/10/17 0017.
 */

public class LoadingProgressDialog extends ProgressDialog {

    private String mLoadingTips = "玩命加载中, 请稍等...";
    private ImageView mLoadingImage;

    public LoadingProgressDialog(Context context, String mLoadingTip) {
        super(context);
        this.mLoadingTips = mLoadingTip;
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
        mLoadingTips = loadingTip;
    }

    public void setContent(int resId) {
        mLoadingTips = getContext().getString(resId);
    }

    private void initView() {
        setContentView(R.layout.progress_dialog);
        mLoadingImage = (ImageView) findViewById(R.id.loading_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingImage.getDrawable();
        TextView mLoadingTip = (TextView)findViewById(R.id.loading_tip);
        mLoadingTip.setText(mLoadingTips);
        animationDrawable.start();
    }


}
