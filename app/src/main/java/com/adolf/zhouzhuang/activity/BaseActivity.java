package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;

/**
 * Created by adolf on 2016/8/18.
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener{
    private TextView mLeftActionBar;
    private TextView mMiddleActionBar;
    private TextView mRightActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initActionBar(String leftActionBarText,int leftActionBarDrawable,String middleActionBarText,String rightActionBarText,int rightActionBarDarwable){
        mLeftActionBar = (TextView) findViewById(R.id.tv_left_actionbar);
        mMiddleActionBar = (TextView) findViewById(R.id.tv_middle_actionbar);
        mRightActionBar = (TextView) findViewById(R.id.tv_rigth_actionbar);
        if (mLeftActionBar != null){
            if (!TextUtils.isEmpty(leftActionBarText)){
                mLeftActionBar.setText(leftActionBarText);
            }
            if (leftActionBarDrawable>0){
                Drawable drawable = ContextCompat.getDrawable(this,leftActionBarDrawable);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                mLeftActionBar.setCompoundDrawables(drawable,null, null, null);
            }
            mLeftActionBar.setOnClickListener(this);
        }
        if (mMiddleActionBar != null && !TextUtils.isEmpty(middleActionBarText)){
            mMiddleActionBar.setText(middleActionBarText);
        }

        if (mRightActionBar != null){
            if (!TextUtils.isEmpty(rightActionBarText)){
                mRightActionBar.setText(rightActionBarText);
            }
            if (rightActionBarDarwable>0){
                Drawable drawable = ContextCompat.getDrawable(this,rightActionBarDarwable);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                mLeftActionBar.setCompoundDrawables(null, null,drawable, null);
            }
            mRightActionBar.setOnClickListener(this);
        }
    }

    public void showToast(String content){
        Toast.makeText(BaseActivity.this,content,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }
}
