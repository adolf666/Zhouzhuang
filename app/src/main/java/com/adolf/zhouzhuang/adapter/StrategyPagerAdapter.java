package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/10/16.
 */

public class StrategyPagerAdapter extends PagerAdapter {

    private List<View> mListViews;
    private Context mContext;
    private int mChildCount = 0;
    public StrategyPagerAdapter(List<View> mListViews, Context mContext) {
        this.mListViews = mListViews;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position),position);
        return mListViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object)   {
  /*    if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }*/
        return super.getItemPosition(object);
    }
}
