package com.adolf.zhouzhuang.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.adolf.zhouzhuang.R;

/**
 * Created by adolf on 2016/9/7.
 */
public class UniversalDialog extends AlertDialog {

    public static enum DialogGravity {
        LEFTTOP, RIGHTTOP, CENTERTOP, CENTER, LEFTBOTTOM, RIGHTBOTTOM, CENTERBOTTOM
    }

    private Window dialogWindow;
    private WindowManager.LayoutParams dialogLayoutParams;

    public UniversalDialog(Context context) {
        this(context, R.style.Dialog);
        // TODO Auto-generated constructor stub
    }

    public UniversalDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        initDialog();
    }


    private void initDialog() {
        // TODO Auto-generated method stub
        dialogWindow = getWindow();
        dialogLayoutParams = dialogWindow.getAttributes();
        setCanceledOnTouchOutside(true);
    }

    public void setLayoutView(View layoutView) {
        dialogWindow.setContentView(layoutView);
    }

    public void setDialogGravity(DialogGravity dialogGravity) {

        switch (dialogGravity) {
            case LEFTTOP:
                dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
                break;
            case RIGHTTOP:
                dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
                break;
            case CENTERTOP:
                dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                break;
            case CENTER:
                dialogWindow.setGravity(Gravity.CENTER);
                break;
            case LEFTBOTTOM:
                dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                break;
            case RIGHTBOTTOM:
                dialogWindow.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                break;
            case CENTERBOTTOM:
                dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                break;
            default:
                break;
        }
    }


    public void setLayoutGravity(View layoutView, DialogGravity dialogGravity) {
        setLayoutView(layoutView);
        setDialogGravity(dialogGravity);
    }


    public void setLayout(View layoutView, DialogGravity dialogGravity,
                          int height, int width) {
        setLayoutGravity(layoutView, dialogGravity);
        setLayoutHeightWidth(height, width);
    }


    public void setLayoutXY(int x, int y) {

        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        dialogLayoutParams.x = x;
        dialogLayoutParams.y = y;
        dialogWindow.setAttributes(dialogLayoutParams);
    }

    public void setLayoutHeightWidth(int height, int width) {

        dialogLayoutParams.height = height;
        dialogLayoutParams.width = width;
        dialogWindow.setAttributes(dialogLayoutParams);
    }

    public void setWH(Context context,WindowManager windowManager){
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        this.getWindow().setAttributes(lp);
    }
}

