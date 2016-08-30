package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;

/**
 * Created by adolf on 2016/8/18.
 */
public class BaseActivity extends Activity {
    private TextView mLeftActionBar;
    private TextView mMiddleActionBar;
    private TextView mRightActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initActionBar(String leftActionBarText,int leftActionBarDrawable,String rightActionBarText,int rightActionBarDarwable){
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
        }
    }

    public void showToast(String content){
        Toast.makeText(BaseActivity.this,content,Toast.LENGTH_SHORT).show();
    }
}
