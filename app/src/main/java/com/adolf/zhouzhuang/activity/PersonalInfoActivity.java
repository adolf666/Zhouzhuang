package com.adolf.zhouzhuang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.baidu.mapapi.map.Text;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class PersonalInfoActivity extends BaseActivity {

private  TextView mSaveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal_info);
        initView();
    }

    private void initView() {
        initActionBar("返回", 0, "修改个人信息", "", 0);
        mSaveBtn = (TextView)findViewById(R.id.tv_save_modify);
        RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) PersonalInfoActivity.this.findViewById(radioButtonId);
                Toast.makeText(PersonalInfoActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left_actionbar:
                finish();
                break;
            case R.id.tv_save_modify:

                break;
        }
    }

}