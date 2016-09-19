package com.adolf.zhouzhuang.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.object.LoginObj;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.baidu.mapapi.map.Text;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class PersonalInfoActivity extends BaseActivity {

  private  TextView mSaveBtn;
  private EditText mUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal_info);
        initView();
    }

    private void initView() {
        initActionBar("返回", R.drawable.back_selected, "修改个人信息", "", 0);
        mSaveBtn = (TextView)findViewById(R.id.tv_save_modify);
        mUserName = (EditText)findViewById(R.id.et_username);
        Object object = SharedPreferencesUtils.readObject(this,"AccountInfo");
        LoginObj obj = (LoginObj)object;
        if(obj.getUsername()!=null){
            mUserName.setText(obj.getUsername());
        }
        Editable text = mUserName.getText();
        Selection.setSelection(text, text.length());
        TextView user_name = (TextView)findViewById(R.id.tv_user_name);
        TextView sex = (TextView)findViewById(R.id.tv_sex);
        RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);

        radioMale.setTypeface(Utils.getType(this,3));
        radioFemale.setTypeface(Utils.getType(this,3));
        mSaveBtn.setTypeface(Utils.getType(this,0));
        user_name.setTypeface(Utils.getType(this,3));
        sex.setTypeface(Utils.getType(this,3));

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


                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

}