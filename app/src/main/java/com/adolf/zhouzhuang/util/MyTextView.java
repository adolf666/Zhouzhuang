package com.adolf.zhouzhuang.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.adolf.zhouzhuang.ZhouzhuangApplication;

/**
 * Created by adolf on 2016/10/4.
 */

public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public void setTypeface(Typeface tf) {
//        super.setTypeface(tf);
//
//    }

//    public void setTypeFace(int i){
//        setTypeface(ZhouzhuangApplication.getInstace().getTypeface(i));
//    }
}
