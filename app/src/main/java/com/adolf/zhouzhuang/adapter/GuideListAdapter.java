package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.util.ListUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zhl13045 on 2016/9/6.
 */
public class GuideListAdapter extends BaseAdapter {

    private ArrayList<Spots> mSpotsList;
    private Context mContext;
    private int selectedIndex = -1;

    public GuideListAdapter(ArrayList<Spots> mSpotsList, Context mContext) {
        this.mSpotsList = mSpotsList;
        this.mContext = mContext;
    }

    public void setSelectedIndex(int index){
        if (index>=0){
            selectedIndex = index;
        }
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mSpotsList);
    }

    @Override
    public Object getItem(int i) {
        return mSpotsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_guide_list, null);
            viewHolder.image = (ImageView) view.findViewById(R.id.iv_is_selected);
            viewHolder.name = (TextView) view.findViewById(R.id.tv_spot_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(mSpotsList.get(i).getTitle());
        viewHolder.image.setVisibility(i == selectedIndex ?View.VISIBLE:View.INVISIBLE);
        return view;

    }

    private static class ViewHolder {
        ImageView image;
        TextView name;
    }

}