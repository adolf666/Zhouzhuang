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
import com.adolf.zhouzhuang.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
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
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.mDivide = (TextView)convertView.findViewById(R.id.view_divide);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==0){
            viewHolder.mDivide.setVisibility(View.VISIBLE);
        }else {
            viewHolder.mDivide.setVisibility(View.GONE);
        }
        viewHolder.title.setText(exhibitList.get(position).getTitle());
        viewHolder.description.setText(exhibitList.get(position).getBrief());
        viewHolder.title.setTypeface(Utils.getType(context,0));

        viewHolder.description.setTypeface(Utils.getType(context,3));
        ImageLoader.getInstance().displayImage(exhibitList.get(position).getTitleImgLocation(), viewHolder.image);

        return convertView;

    }

    private static class ViewHolder {
        ImageView image;
        TextView title;
        TextView description;
        TextView mDivide;
    }

}
