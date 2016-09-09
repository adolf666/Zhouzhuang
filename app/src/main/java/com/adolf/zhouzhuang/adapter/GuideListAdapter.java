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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adolf on 2016/8/26.
 */
public class GuideListAdapter extends BaseAdapter {
    private List<Spots> mList;
    private Context mContext;
    private int mSelectedIndex = -1;

    public GuideListAdapter(List<Spots> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setmSelectedIndex(int index){
        if (index>=0){
            mSelectedIndex = index;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_guide_list, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_selected_scenery);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_scenery);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mList.get(position).getTitle());
        viewHolder.image.setVisibility(mSelectedIndex == position ? View.VISIBLE:View.INVISIBLE);
        return convertView;

    }

    private static class ViewHolder {
        ImageView image;
        TextView title;
    }
}