package com.adolf.zhouzhuang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.ZhouzhuangApplication;
import com.adolf.zhouzhuang.object.StrategyObject;
import com.adolf.zhouzhuang.util.GlideRoundTransform;
import com.adolf.zhouzhuang.util.MyTextView;
import com.adolf.zhouzhuang.util.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by adolf on 2016/10/4.
 */

public class StrategyRecylerViewAdapter extends RecyclerView.Adapter<StrategyRecylerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<StrategyObject> strategyObjectList;

    public StrategyRecylerViewAdapter(Context mContext, List<StrategyObject> strategyObjectList) {
        this.mContext = mContext;
        this.strategyObjectList = strategyObjectList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.strategy_item, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final StrategyObject item = strategyObjectList.get(position);
        if (item != null && !TextUtils.isEmpty(item.getPicthumburl())){
            Glide.with(mContext).load(item.getPicthumburl()).centerCrop().placeholder(R.mipmap.bg_strategy).crossFade().into(holder.mPic);
        }

        if(item.getCreatorimgurl()!=null && !TextUtils.isEmpty(item.getCreatorimgurl())){
            Glide.with(mContext).load(item.getCreatorimgurl()).centerCrop().placeholder(R.mipmap.icon_photo_eg).crossFade().transform(new GlideRoundTransform(mContext,30)).into(holder.mPhoto);
        }

        holder.mName.setText(item.getTitle());
        holder.mDesc.setText(item.getCreatorname());
    }

    @Override
    public int getItemCount() {
        if (strategyObjectList != null){
            return strategyObjectList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mPic;
        MyTextView mName;
        ImageView mPhoto;
        MyTextView  mDesc;
        TextView  mDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            mPic = (ImageView) itemView.findViewById(R.id.img_pic);
            mName = (MyTextView)itemView.findViewById(R.id.tv_scenic_name);
            mPhoto = (ImageView)itemView.findViewById(R.id.img_photo);
            mDesc = (MyTextView)itemView.findViewById(R.id.tv_desc);
            mDate = (TextView)itemView.findViewById(R.id.tv_date);
            mName.setTypeFace(3);
            mDesc.setTypeFace(3);
//            mName.setTypeface(Utils.getType(mContext,3));
//            mDesc.setTypeface(Utils.getType(mContext,3));
        }
    }
}
