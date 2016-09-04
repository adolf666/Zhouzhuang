package com.adolf.zhouzhuang.activity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        scanAnimation.stop();
        for (int i = 0; i < scanAnimation.getNumberOfFrames(); ++i){
            Drawable frame = scanAnimation.getFrame(i);
            if (frame instanceof BitmapDrawable) {
                ((BitmapDrawable)frame).getBitmap().recycle();
            }
            frame.setCallback(null);
        }
        scanAnimation.setCallback(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDisplay.setBackgroundResource(R.drawable.anim1);
        scanAnimation = (AnimationDrawable) mDisplay.getBackground();
        scanAnimation.start();
    }
}
