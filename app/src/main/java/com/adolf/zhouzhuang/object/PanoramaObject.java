package com.adolf.zhouzhuang.object;

import android.graphics.drawable.Drawable;

/**
 * Created by gpp on 2016/10/9 0009.
 */

public class PanoramaObject {
    public Drawable image;
    public String name;
    public String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
