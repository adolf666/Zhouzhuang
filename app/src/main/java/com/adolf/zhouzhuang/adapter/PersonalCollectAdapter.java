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
    private FavoriteDataBaseHelper mFavoriteDataBaseHelper;

    public PersonalCollectAdapter(Context context, List<Spots> mList,FavoriteDataBaseHelper mFavoriteDataBaseHelper) {
        this.context = context;
        this.collectList = mList;
        this.mFavoriteDataBaseHelper = mFavoriteDataBaseHelper;

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
            viewHolder.mName.setTypeface(Utils.getType(context, 3));
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
        /*viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCollection(String.valueOf(collectList.get(position).getPid()));
                collectList.remove(position);
                notifyDataSetChanged();
            }
        });*/
        return convertView;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.img_delete_collect){
            int position =(Integer) view.getTag();
            cancelCollection(collectList.get(position).getPid());
            collectList.remove(position);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView mName;
    }

    public void cancelCollection(final int spotsId){
        RequestParams params = new RequestParams();
        params.put("spotId",String.valueOf(spotsId));
        params.put("userId", SharedPreferencesUtils.getInt(context,"pid"));
        AsyncHttpClientUtils.getInstance().get(ServiceAddress.COLLECTION_CANCEL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mFavoriteDataBaseHelper.deleteFavoriteByUserIdAndSpotsId(SharedPreferencesUtils.getInt(context,"pid"),spotsId);
                Toast.makeText(context,"取消收藏成功",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context,"取消收藏失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
