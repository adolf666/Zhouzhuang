package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.adapter.PersonalCollectAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/15.
 */
public class PersonCollectActivity extends BaseActivity {

    private ListView mListview;
    private List<String> collectList;
    PersonalCollectAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_collect);
        initView();
        initData();
    }
    private void initView(){
        initActionBar("返回", R.drawable.back_selected, "我的收藏", "", 0);
        mListview = (ListView)findViewById(R.id.lv_list_view);

    }
    private void initData(){
        collectList = new ArrayList<>();
        collectList.add("双桥");
        collectList.add("逸飞之家");
        collectList.add("沈厅");
        mAdapter = new PersonalCollectAdapter(this,collectList);
        mListview.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
        }
    }
}
