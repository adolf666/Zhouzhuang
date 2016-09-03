package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.adolf.zhouzhuang.object.Exhibit;

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
        return null;
    }
}
