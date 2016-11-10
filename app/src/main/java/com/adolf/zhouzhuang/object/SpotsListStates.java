package com.adolf.zhouzhuang.object;

/**
 * Created by adolf on 2016/11/9.
 */

public class SpotsListStates {
    public boolean isImageShow = false;
    public boolean isExpand  = false;

    public void setOppositeState(){
        isImageShow = !isImageShow;
        isExpand = !isExpand;
    }

    public void setExpandState(){
        isImageShow = true;
        isExpand = true;
    }

    public void setHideState(){
        isImageShow = false;
        isExpand = false;
    }
}
