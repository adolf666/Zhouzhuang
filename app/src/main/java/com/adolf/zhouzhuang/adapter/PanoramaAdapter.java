package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.object.PanoramaObject;
import com.adolf.zhouzhuang.util.MyTextView;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by gpp on 2016/10/9 0009.
 */

public class PanoramaAdapter extends BaseAdapter {
    private Context context;
    private List<PanoramaObject> panoramaList;

    public PanoramaAdapter(Context context ,List<PanoramaObject> panoramaList) {
        this.context = context;
        this.panoramaList = panoramaList;
    }

    @Override
    public int getCount() {
        return panoramaList.size();
    }

    @Override
    public Object getItem(int position) {
        return panoramaList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_panorama, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.name = (MyTextView)convertView.findViewById(R.id.tv_name);
            viewHolder.description = (MyTextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.mDivide = (TextView)convertView.findViewById(R.id.view_divide);
            viewHolder.mDivideEnd =(TextView)convertView.findViewById(R.id.view_divide1);
            viewHolder.name.setTypeFace(0);
            viewHolder.description.setTypeFace(3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position ==panoramaList.size()-1 ){
            viewHolder.mDivideEnd.setVisibility(View.VISIBLE);
        }
        viewHolder.image.setImageDrawable(panoramaList.get(position).getImage());
        viewHolder.name.setText(panoramaList.get(position).getName());
        if(panoramaList.get(position).getDesc()!=null){
            viewHolder.description.setText(panoramaList.get(position).getDesc());
        }
        return convertView;
    }
    private static class ViewHolder {
        ImageView image;
        MyTextView name;
        MyTextView description;
        TextView mDivide;
        TextView mDivideEnd;
    }
}
