package com.adolf.zhouzhuang.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

/**
 * Created by adolf on 2016/10/1.
 */

public class ExponentialOutInterpolator implements Interpolator {

    public ExponentialOutInterpolator() {}

    public ExponentialOutInterpolator(Context context, AttributeSet attrs) {}

    public float getInterpolation(float input) {
        if ((input) < 0.5f) {
           return input;
        } else {
            input = input * 2 - 1;
            if (input < (1 / 2.75))
                return ((7.5625f * input * input)) * 0.5f + 1 * 0.5f;
            else if (input < (2 / 2.75))
                return ((7.5625f * (input -= (1.5f / 2.75f)) * input + 0.75f)) * 0.5f + 1 * 0.5f;
            else if (input < (2.5 / 2.75))
                return ((7.5625f * (input -= (2.25f / 2.75f)) * input + 0.9375f)) * 0.5f + 1 * 0.5f;
            else
                return ((7.5625f * (input -= (2.625f / 2.75f)) * input + 0.984375f)) * 0.5f + 1 * 0.5f;
        }
    }
}
