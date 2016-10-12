package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.util.MyTextView;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class ZhouzhuangLibActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhouzhanglib);
        initView();
    }
    private void initView(){
        initActionBar("返回",R.drawable.back_selected,"周庄博物馆","",0);
        MyTextView museum_desc1 = (MyTextView)findViewById(R.id.museum_desc1);
        MyTextView museum_desc2 = (MyTextView)findViewById(R.id.museum_desc2);
        MyTextView museum_desc3 = (MyTextView)findViewById(R.id.museum_desc3);
        MyTextView museum_desc4 = (MyTextView)findViewById(R.id.museum_desc4);
        MyTextView museum_desc5 = (MyTextView)findViewById(R.id.museum_desc5);
        MyTextView museum_desc6 = (MyTextView)findViewById(R.id.museum_desc6);

        museum_desc1.setText("\u3000\u3000"+getResources().getString(R.string.museum_desc1));
        museum_desc2.setText("\u3000\u3000"+getResources().getString(R.string.museum_desc2));
        museum_desc3.setText("\u3000\u3000"+getResources().getString(R.string.museum_desc3));
        museum_desc4.setText("\u3000\u3000"+getResources().getString(R.string.museum_desc4));
        museum_desc5.setText("\u3000\u3000"+getResources().getString(R.string.museum_desc5));
        museum_desc6.setText("\u3000\u3000"+getResources().getString(R.string.museum_desc6));
        museum_desc1.setTypeFace(3);
        museum_desc2.setTypeFace(3);
        museum_desc3.setTypeFace(3);
        museum_desc4.setTypeFace(3);
        museum_desc5.setTypeFace(3);
        museum_desc6.setTypeFace(3);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left_actionbar:
                finish();
                break;
        }}
}
