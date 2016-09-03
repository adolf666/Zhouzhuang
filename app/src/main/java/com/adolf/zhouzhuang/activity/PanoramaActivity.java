package com.adolf.zhouzhuang.activity;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.adolf.zhouzhuang.R;

public class PanoramaActivity extends BaseActivity {

    private ImageView mDisplay;
    private AnimationDrawable scanAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        mDisplay = (ImageView) findViewById(R.id.iv_display);
        mDisplay.setBackgroundResource(R.drawable.anim1);
        scanAnimation = (AnimationDrawable) mDisplay.getBackground();
        scanAnimation.start();
    }
}
