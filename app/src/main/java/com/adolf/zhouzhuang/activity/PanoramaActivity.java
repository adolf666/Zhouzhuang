package com.adolf.zhouzhuang.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;

import java.util.ArrayList;
import java.util.Collections;

public class PanoramaActivity extends BaseActivity implements GestureDetector.OnGestureListener ,View.OnTouchListener {

    private ImageView mDisplay;
    private AnimationDrawable scanAnimation;
    private ArrayList<Integer> mImgList = new ArrayList<>();
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;
    private int mCurrentIndex = 0;
    private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        initActionBar("返回",R.drawable.back_selected,"","",0);
        mDisplay = (ImageView) findViewById(R.id.iv_display);
        mGestureDetector = new GestureDetector(this);
        mDisplay.setOnTouchListener(this);
        initImgData();
    }

    private void initImgData(){
        TypedArray ar = this.getResources().obtainTypedArray(R.array.actions_images);
        int len = ar.length();
        Integer[] resIds = new Integer[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);
        ar.recycle();
        Collections.addAll(mImgList, resIds);
        mDisplay.setImageResource(mImgList.get(0));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX()-e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            if (mCurrentIndex>-1 && mCurrentIndex < mImgList.size() - 2){
                mDisplay.setImageResource(mImgList.get(++mCurrentIndex));
            }
        } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            if (mCurrentIndex>0 && mCurrentIndex < mImgList.size() - 2){
                mDisplay.setImageResource(mImgList.get(--mCurrentIndex));
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }
}
