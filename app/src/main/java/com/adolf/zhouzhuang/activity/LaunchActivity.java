package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.adolf.zhouzhuang.R;

public class LaunchActivity extends Activity {
//    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laynch);
//        dialog = new ProgressDialog(this);
//
//        dialog.setIndeterminate(true);
//        dialog.show();
//        dialog.setContentView(R.layout.item_progressdialog);
        pageChange();
    }

    private void pageChange() {
        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(),3000);

    }

    class SplashHandler implements Runnable {
        public void run() {
//            dialog.dismiss();
            startActivity(new Intent(LaunchActivity.this, MainActivity.class));
            LaunchActivity.this.finish();
        }
    }
}
