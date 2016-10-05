package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.object.StrategyObject;
import com.adolf.zhouzhuang.util.GlideRoundTransform;
import com.adolf.zhouzhuang.util.MyTextView;
import com.adolf.zhouzhuang.util.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2016/9/25.
 */
public class StrategyAdapter extends BaseAdapter {

    private Context context;
    private List<StrategyObject> strategyObjectList;
    public StrategyAdapter(Context context, List<StrategyObject> strategyObjectList) {
        this.context = context;
        this.strategyObjectList = strategyObjectList;
//        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.icon_photo_eg) //设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.icon_photo_eg) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.icon_photo_eg) // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
//                .displayer(new RoundedBitmapDisplayer(90)) // 设置成圆角、圆形图片,我这里将new RoundedBitmapDisplayer的参数设置为90,就是圆形图片，其他角度可以根据需求自行修改
//                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
//                .build(); // 创建配置过得DisplayImageOption对象
//
//        optionsBig = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.bg_strategy) //设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.bg_strategy) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.bg_strategy) // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
//                .displayer(new RoundedBitmapDisplayer(15)) // 设置成圆角、圆形图片,我这里将new RoundedBitmapDisplayer的参数设置为90,就是圆形图片，其他角度可以根据需求自行修改
//                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
//                .build(); // 创建配置过得DisplayImageOption对象
    }
    @Override
    public int getCount() {
        return strategyObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return strategyObjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final ViewHolder viewHolder;
        final StrategyObject item = strategyObjectList.get(position);
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.strategy_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mPic = (ImageView) convertView.findViewById(R.id.img_pic);
            viewHolder.mName = (MyTextView)convertView.findViewById(R.id.tv_scenic_name);
            viewHolder.mPhoto = (ImageView)convertView.findViewById(R.id.img_photo);
            viewHolder.mDesc = (MyTextView)convertView.findViewById(R.id.tv_desc);
            viewHolder.mDate = (TextView)convertView.findViewById(R.id.tv_date);
            viewHolder.mName.setTypeFace(3);
            viewHolder.mDesc.setTypeFace(3);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (item != null && !TextUtils.isEmpty(item.getPicthumburl())){
            Glide.with(context).load(item.getPicthumburl()).centerCrop().placeholder(R.mipmap.bg_strategy).crossFade().into(viewHolder.mPic);
        }

        if(item.getCreatorimgurl()!=null && !TextUtils.isEmpty(item.getCreatorimgurl())){
            Glide.with(context).load(item.getCreatorimgurl()).centerCrop().placeholder(R.mipmap.icon_photo_eg).crossFade().transform(new GlideRoundTransform(context,30)).into(viewHolder.mPhoto);
        }

        viewHolder.mName.setText(item.getTitle());
        viewHolder.mDesc.setText(item.getCreatorname());
        return convertView;
    }

    private boolean isNeedToLoadImage(String url,ImageView imageView){
        if (imageView.getTag() == null){
            return true;
        }else if (TextUtils.equals(imageView.getTag().toString(),url)){
            return false;
        }else {
            return true;
        }
    }

    private static class ViewHolder {
        ImageView mPic;
        MyTextView mName;
        ImageView mPhoto;
        MyTextView  mDesc;
        TextView  mDate;
    }

}
