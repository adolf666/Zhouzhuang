package com.adolf.zhouzhuang.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;

import java.util.Locale;

/**
 * Created by Administrator on 2016/9/4.
 */
public class SelectPopupWindow extends PopupWindow{
    private SelectCategory selectCategory;

    private String[] parentStrings;
    private Activity mActivity;
    private ListView lvParentCategory = null;
    private ParentCategoryAdapter parentCategoryAdapter = null;
    private TextToSpeech mTts;
    /**
     * @param parentStrings   字类别数据
     * @param activity
     * @param selectCategory  回调接口注入
     */
    public SelectPopupWindow(String[] parentStrings,  Activity activity, SelectCategory selectCategory) {
        this.selectCategory=selectCategory;
        this.parentStrings=parentStrings;
        this.mActivity = activity;
        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_quyu_choose_view, null);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小

        this.setContentView(contentView);
        this.setWidth(dm.widthPixels);
        this.setHeight(dm.heightPixels*5/10);

        /* 设置背景显示 */
//        setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.pop_bg));
        /* 设置触摸外面时消失 */
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(false); /*设置点击menu以外其他地方以及返回键退出 */

        /**
         * 1.解决再次点击MENU键无反应问题
         */
        contentView.setFocusableInTouchMode(true);

        //父类别适配器
        lvParentCategory= (ListView) contentView.findViewById(R.id.lv_parent_category);
        parentCategoryAdapter = new ParentCategoryAdapter(activity,parentStrings);
        lvParentCategory.setAdapter(parentCategoryAdapter);


        lvParentCategory.setOnItemClickListener(parentItemClickListener);
    }

    /**
     * 子类别点击事件
     */
    private AdapterView.OnItemClickListener childrenItemClickListener=new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            if(selectCategory!=null){
                selectCategory.selectCategory(parentCategoryAdapter.getPos(),position);
            }
            dismiss();
        }
    };

    /**
     * 父类别点击事件
     */
    private AdapterView.OnItemClickListener parentItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//            childrenCategoryAdapter.setDatas(childrenStrings[position]);
//            childrenCategoryAdapter.notifyDataSetChanged();
            Toast.makeText(mActivity,parentStrings[position],Toast.LENGTH_SHORT).show();
            parentCategoryAdapter.setSelectedPosition(position);
            parentCategoryAdapter.notifyDataSetChanged();
            dismiss();
            CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
            builder.setMessage("这个是关于"+parentStrings[position]+"的详细介绍，可以转成语音。");
            builder.setTitle(parentStrings[position]);
            builder.setPositiveButton("导航", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //设置你的操作事项
                    Toast.makeText(mActivity,"导航",Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("语音",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mTts =  new TextToSpeech(mActivity,onInitListener);
                            Toast.makeText(mActivity,"语音",Toast.LENGTH_SHORT).show();
                        }
                    });

            builder.create().show();
        }
    };
    TextToSpeech.OnInitListener onInitListener= new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
//状态成功
            if (status == TextToSpeech.SUCCESS) {
                //设置语言
                int result = mTts.setLanguage(Locale.US);
                //（我试了中文，不好使？？？？）
//              int result = mTts.setLanguage(Locale.CHINA);

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // 如果不支持该语言
                    Log.e("", "Language is not available.");
                } else {
                    // TTS 引擎已经成功初始化
                    //按钮设置为可点击
//                    mAgainButton.setEnabled(true);
                    //发音朗读
                    mTts.speak("You’ll find all the projects you’re working on listed in the sidebar.", TextToSpeech.QUEUE_FLUSH, null);
                }
            } else {
                // 初始化失败
                Log.e("", "Could not initialize TextToSpeech.");
            }
        }
    };
    /**
     * 选择成功回调
     * @author apple
     *
     */
    public interface SelectCategory{
        /**
         * 把选中的下标通过方法回调回来
         * @param parentSelectposition  父类别选中下标
         * @param childrenSelectposition 子类别选中下标
         */
        public void selectCategory(int parentSelectposition, int childrenSelectposition);
    }
}
