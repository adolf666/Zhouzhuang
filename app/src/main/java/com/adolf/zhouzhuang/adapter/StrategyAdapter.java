package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.object.Exhibit;
import com.adolf.zhouzhuang.object.StrategyObject;
import com.adolf.zhouzhuang.util.Utils;
import com.adolf.zhouzhuang.widget.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/9/25.
 */
public class StrategyAdapter extends BaseAdapter {

    private Context context;
    private List<StrategyObject> strategyObjectList;
    private DisplayImageOptions options;
    public StrategyAdapter(Context context, List<StrategyObject> strategyObjectList) {
        this.context = context;
        this.strategyObjectList = strategyObjectList;
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.icon_photo_eg) //设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_photo_eg) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_photo_eg) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(90)) // 设置成圆角、圆形图片,我这里将new RoundedBitmapDisplayer的参数设置为90,就是圆形图片，其他角度可以根据需求自行修改
                .build(); // 创建配置过得DisplayImageOption对象
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
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.strategy_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mPic = (ImageView) convertView.findViewById(R.id.img_pic);
            viewHolder.mName = (TextView)convertView.findViewById(R.id.tv_scenic_name);
            viewHolder.mPhoto = (ImageView)convertView.findViewById(R.id.img_photo);
            viewHolder.mDesc = (TextView)convertView.findViewById(R.id.tv_desc);
            viewHolder.mDate = (TextView)convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       final StrategyObject item = strategyObjectList.get(position);
        ImageLoader.getInstance().displayImage(item.getPicthumburl(), viewHolder.mPic);
        if(item.getCreatorimgurl()!=null&&!item.getCreatorimgurl().isEmpty()){
            ImageLoader.getInstance().displayImage(item.getCreatorimgurl(), viewHolder.mPhoto,options);
        }



        viewHolder.mName.setText(item.getTitle());
        viewHolder.mDesc.setText(item.getCreatorname());
        viewHolder.mName.setTypeface(Utils.getType(context,3));
        viewHolder.mDesc.setTypeface(Utils.getType(context,3));
        return convertView;
    }

    private static class ViewHolder {
        ImageView mPic;
        TextView mName;
        ImageView mPhoto;
        TextView  mDesc;
        TextView  mDate;
    }

}
