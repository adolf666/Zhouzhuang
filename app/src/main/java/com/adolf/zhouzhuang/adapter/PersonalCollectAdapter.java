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
import com.adolf.zhouzhuang.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/15.
 */
public class PersonalCollectAdapter extends BaseAdapter {
    private Context context;
    private List<Spots> collectList;

    public PersonalCollectAdapter(Context context, List<Spots> mList) {
        this.context = context;
        this.collectList = mList;
    }

    @Override
    public int getCount() {
        return collectList.size();
    }

    @Override
    public Object getItem(int position) {
        return collectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_personal_collect, null);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.tv_collect_name);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_delete_collect);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (0 == position) {
            convertView.setBackgroundResource(R.mipmap.bg_threeinput01);
        } else if (collectList.size() - 1 == position) {
            convertView.setBackgroundResource(R.mipmap.bg_threeinput03);
        } else {
            convertView.setBackgroundResource(R.mipmap.bg_threeinput02);
        }
        viewHolder.mName.setText(collectList.get(position).getTitle());
        viewHolder.mName.setTypeface(Utils.getType(context, 3));
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


    private static class ViewHolder {
        ImageView imageView;
        TextView mName;
    }

}
