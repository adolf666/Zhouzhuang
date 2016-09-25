package com.adolf.zhouzhuang.adapter;

import android.content.Context;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/9/25.
 */
public class StrategyAdapter extends BaseAdapter {

    private Context context;
    private List<StrategyObject> strategyObjectList;

    public StrategyAdapter(Context context, List<StrategyObject> strategyObjectList) {
        this.context = context;
        this.strategyObjectList = strategyObjectList;
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
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
        ViewHolder viewHolder;
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
        StrategyObject item = strategyObjectList.get(position);
        ImageLoader.getInstance().displayImage(item.getPicthumburl(), viewHolder.mPic);
        ImageLoader.getInstance().displayImage(item.getCreatorimgurl(), viewHolder.mPhoto);
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
