package com.adolf.zhouzhuang.object;

import android.graphics.drawable.Drawable;

/**
 * Created by gpp on 2016/10/9 0009.
 */

public class PanoramaObject {
    public Drawable image;
    public String name;
    public String title;
    public String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(Drawable image){
        this.image = image;
    }

    public Drawable getImage(){
        return image;
    }
}
