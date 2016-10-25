package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.databasehelper.FavoriteDataBaseHelper;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.interfaces.AdapterOnClickListener;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/9/15.
 */
public class PersonalCollectAdapter extends BaseAdapter implements View.OnClickListener {
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
//            viewHolder.mName.setTypeface(Utils.getType(context, 3));
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
        viewHolder.imageView.setTag(position);
        viewHolder.imageView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.img_delete_collect){
            int position =(Integer) view.getTag();
            if (mDeleteButtonClickListener != null) {
                mDeleteButtonClickListener.onClick(view, position, collectList.get(position));
            }
           collectList.remove(position);
            notifyDataSetChanged();
        }
    }
    AdapterOnClickListener<Spots> mDeleteButtonClickListener;

    public void setDeleteButtonClickListener(AdapterOnClickListener<Spots> listener) {
        this.mDeleteButtonClickListener = listener;
    }
    private static class ViewHolder {
        ImageView imageView;
        TextView mName;
    }

}
