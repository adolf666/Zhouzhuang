package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Vibrator;

import android.os.Bundle;

import android.view.View;

import com.adolf.zhouzhuang.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QRCodeActivity extends BaseActivity implements QRCodeView.Delegate{
    private QRCodeView mQRCodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initView();
    }
    private void initView(){
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot();
        initActionBar("返回",R.drawable.back_selected,"二维码扫描","",0);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        Intent intent = new Intent();
        intent.setClass(QRCodeActivity.this,MainActivity.class);
        String s1 = result.substring(0, result.lastIndexOf("?")+1);
        String s2 = result.replaceAll(s1,"");
        String s3 =s2.substring(0,s2.lastIndexOf("=")+1);
        String s4 =s2.replace(s3,"");
        intent.putExtra(MainActivity.SPOTS_ID,Integer.valueOf(s4));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showToast("打开相机失败");
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
