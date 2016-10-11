package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.object.Exhibit;
import com.adolf.zhouzhuang.util.MyTextView;
import com.adolf.zhouzhuang.util.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by adolf on 2016/9/3.
 */
public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<Exhibit> exhibitList;
    public NewsAdapter(Context context, List<Exhibit> exhibitList) {
        this.context = context;
        this.exhibitList = exhibitList;
//        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.box01_image) //设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.box01_image) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.box01_image) // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
//                .build(); // 创建配置过得DisplayImageOption对象
    }

    @Override
    public int getCount() {
        return exhibitList.size();
    }

    @Override
    public Object getItem(int position) {
        return exhibitList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.title = (MyTextView) convertView.findViewById(R.id.tv_title);
            viewHolder.description = (MyTextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.mDivide = (TextView)convertView.findViewById(R.id.view_divide);
            viewHolder.mDivideEnd =(TextView)convertView.findViewById(R.id.view_divide1);
            viewHolder.title.setTypeFace(0);
            viewHolder.description.setTypeFace(3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(exhibitList.size()>2&&position == exhibitList.size()-1 ){
            viewHolder.mDivideEnd.setVisibility(View.VISIBLE);
        }
        viewHolder.title.setText(exhibitList.get(position).getTitle());
        viewHolder.description.setText(exhibitList.get(position).getBrief());
        Glide.with(context).load(exhibitList.get(position).getTitleImgLocation()).centerCrop().placeholder(R.mipmap.box01_image).crossFade().into(viewHolder.image);
        return convertView;

    }

    private static class ViewHolder {
        ImageView image;
        MyTextView title;
        MyTextView description;
        TextView mDivide;
        TextView mDivideEnd;
    }

}
