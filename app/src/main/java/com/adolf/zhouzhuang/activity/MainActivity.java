package com.adolf.zhouzhuang.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.Spots;
import com.adolf.zhouzhuang.adapter.ViewPagerAdapter;
import com.adolf.zhouzhuang.databasehelper.SpotsDataBaseHelper;
import com.adolf.zhouzhuang.fragment.BaseFragment;
import com.adolf.zhouzhuang.fragment.CollectionFragment;
import com.adolf.zhouzhuang.fragment.GudieFragment;
import com.adolf.zhouzhuang.fragment.PersonalCenterFragment;
import com.adolf.zhouzhuang.fragment.MuseumFragment;
import com.adolf.zhouzhuang.fragment.StrategyFragment;
import com.adolf.zhouzhuang.httpUtils.AsyncHttpClientUtils;
import com.adolf.zhouzhuang.httpUtils.GsonUtil;
import com.adolf.zhouzhuang.interfaces.MainInterface;
import com.adolf.zhouzhuang.object.AppVersionObject;
import com.adolf.zhouzhuang.util.Constants;
import com.adolf.zhouzhuang.util.SdCardUtil;
import com.adolf.zhouzhuang.util.ServiceAddress;
import com.adolf.zhouzhuang.util.SharedPreferencesUtils;
import com.adolf.zhouzhuang.util.SoundBroadUtils;
import com.adolf.zhouzhuang.util.Utils;
import com.adolf.zhouzhuang.widget.CustomViewPager;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity implements View.OnClickListener, BaseFragment.OnFragmentInteractionListener {

    public static final int QrcodeRequest = 1000;
    private TextView mMuseumTextView;
    private TextView mCollectionTextView;
    private TextView mNavigationTextView;
    private TextView mStrategyTextView;
    private ProgressDialog mProgressDialog;
    private CustomViewPager mCustomerViewPager;
    public static final String SPOTS_ID = "spot_id";
    private int spotId = 0;
    GudieFragment gudieFragment;
    String textColorDefault = "#8e8e99";
    String textColorFocus = "#333240";
    Drawable museumDefault, museumFocus, collectionDefault,
            collectionFocus, guideDefault, guideFocus, strategyDefault, strategyFocus;

    //两次返回用
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        checkUpdate();
    }
private void checkUpdate(){
    RequestParams params = new RequestParams();
    params.add("os","android");
    AsyncHttpClientUtils.getInstance().get(ServiceAddress.GET_APP_VERSION,params,new JsonHttpResponseHandler(){

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            AppVersionObject appVersionObject = GsonUtil.jsonToBean(response,"data",AppVersionObject.class);
             int  version = getVersionCode();
               if(appVersionObject!=null&&appVersionObject.appid>version&&!TextUtils.isEmpty(appVersionObject.upgradecontent)){
                   promptDialog(appVersionObject.upgradecontent,appVersionObject.downloadurl);
               }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            Toast.makeText(MainActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
        }
    });
}


    private void initViews() {
        mMuseumTextView = (TextView) findViewById(R.id.tv_museum);
        mCollectionTextView = (TextView) findViewById(R.id.tv_collection);
        mNavigationTextView = (TextView) findViewById(R.id.tv_navigation);
        mStrategyTextView = (TextView) findViewById(R.id.tv_strategy);
        mMuseumTextView.setTypeface(Utils.getType(this, 0));
        mCollectionTextView.setTypeface(Utils.getType(this, 0));
        mNavigationTextView.setTypeface(Utils.getType(this, 0));
        mStrategyTextView.setTypeface(Utils.getType(this, 0));
        mCustomerViewPager = (CustomViewPager) findViewById(R.id.viewpager);

        mMuseumTextView.setOnClickListener(this);
        mCollectionTextView.setOnClickListener(this);
        mNavigationTextView.setOnClickListener(this);
        mStrategyTextView.setOnClickListener(this);
        museumDefault = getResources().getDrawable(R.mipmap.btn_menu01_default);
        museumFocus = getResources().getDrawable(R.mipmap.btn_menu01_focused);
        collectionDefault = getResources().getDrawable(R.mipmap.btn_menu02_default);
        collectionFocus = getResources().getDrawable(R.mipmap.btn_menu02_focused);
        guideDefault = getResources().getDrawable(R.mipmap.btn_menu03_default);
        guideFocus = getResources().getDrawable(R.mipmap.btn_menu03_focused);
        strategyDefault = getResources().getDrawable(R.mipmap.btn_menu04_default);
        strategyFocus = getResources().getDrawable(R.mipmap.btn_menu04_focused);
        initActionBar("", R.drawable.personal_center_selector, "周庄博物馆", "", R.drawable.scan_selector);
        initViewPager();
    }

    private void initViewPager() {
        List<Fragment> fragmentArrayList = new ArrayList<>();
        MuseumFragment museumFragment = new MuseumFragment();
        CollectionFragment collectionFragment = new CollectionFragment();
        gudieFragment = new GudieFragment();
        // PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        StrategyFragment strategyFragment = new StrategyFragment();
        fragmentArrayList.add(museumFragment);
        fragmentArrayList.add(collectionFragment);
        fragmentArrayList.add(gudieFragment);
        fragmentArrayList.add(strategyFragment);
        mCustomerViewPager.setScrollble(false);
        mCustomerViewPager.setOffscreenPageLimit(3);
        mCustomerViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_museum:
                setBottomBarBackground(0);
                break;
            case R.id.tv_collection:
                setBottomBarBackground(1);
                break;
            case R.id.tv_navigation:
                setBottomBarBackground(2);
                break;
            case R.id.tv_strategy:
                setBottomBarBackground(3);
                break;
            case R.id.tv_left_actionbar:
                startActivity(new Intent(MainActivity.this, PersonalCenterActivity.class));
                break;
            case R.id.tv_rigth_actionbar:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, QRCodeActivity.class);
                startActivityForResult(intent2, QrcodeRequest);
                break;
        }
    }

    public void autoLoginLogic() {
        boolean isAutoLogin = SharedPreferencesUtils.getBoolean(MainActivity.this, "AutoLogin", false);
        if (!isAutoLogin) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, PersonalCenterActivity.class));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (TextUtils.equals(uri.toString(), "exhibit")) {
            mCustomerViewPager.setCurrentItem(1);
        }
    }

    public void setBottomBarBackground(int selectedIndex) {
        mCustomerViewPager.setCurrentItem(selectedIndex);
        mMuseumTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, selectedIndex == 0 ? museumFocus : museumDefault,
                null, null);
        mCollectionTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, selectedIndex == 1 ? collectionFocus : collectionDefault,
                null, null);
        mNavigationTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, selectedIndex == 2 ? guideFocus : guideDefault,
                null, null);
        mStrategyTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, selectedIndex == 3 ? strategyFocus : strategyDefault,
                null, null);
        mMuseumTextView.setTextColor(selectedIndex == 0 ? Color.parseColor(textColorFocus) : Color.parseColor(textColorDefault));
        mCollectionTextView.setTextColor(selectedIndex == 1 ? Color.parseColor(textColorFocus) : Color.parseColor(textColorDefault));
        mNavigationTextView.setTextColor(selectedIndex == 2 ? Color.parseColor(textColorFocus) : Color.parseColor(textColorDefault));
        mStrategyTextView.setTextColor(selectedIndex == 3 ? Color.parseColor(textColorFocus) : Color.parseColor(textColorDefault));

        String title = "周庄博物馆";
        switch (selectedIndex) {
            case 0:
                title = "周庄博物馆";
                break;
            case 1:
                title = "馆藏珍品";
                break;
            case 2:
                title = "周庄导览";
                break;
            case 3:
                title = "游玩攻略";
                break;
        }
        initActionBar("", R.drawable.personal_center_selector, title, "", R.drawable.scan_selector);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            gudieFragment.stopPlayAudo();
            return;
        } else {
            Toast.makeText(getBaseContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
   @Override
   public void onResume() {
       super.onResume();
       if(Constants.SPOTS_ID != -1){
           setSpotId(Constants.SPOTS_ID);
           Constants.SPOTS_ID = -1;
       }


    }

    public void setSpotId(int spotId) {
        mCustomerViewPager.setCurrentItem(2);
        setBottomBarBackground(2);
        SpotsDataBaseHelper mSpotsDataBaseHelper = new SpotsDataBaseHelper(getSpotsDao());
        if (0 != spotId) {
            Spots spots = mSpotsDataBaseHelper.getSpotsById(spotId);
            if (spots != null) {
                gudieFragment.setSelectedSpotsOutSide(spots);
                gudieFragment.showBaiduInfoWindow(spots);
                gudieFragment.setMapStatusLimits();
                gudieFragment.locationToCenter(Double.parseDouble(spots.getLat4show()), Double.parseDouble(spots.getLng4show()), false,false);
                gudieFragment.checkAndDownLoadAudio(spots);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case QrcodeRequest:
                if (null != data) {
                    int spotId = data.getIntExtra(SPOTS_ID, 0);
                    setSpotId(spotId);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gudieFragment.bdA.recycle();
        gudieFragment.stopPlayAudo();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        System.exit(0);
    }

    protected void promptDialog(String message , final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage(message);
        builder.setTitle("版本更新");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(!TextUtils.isEmpty(url)){
                    downLoadApk(url);
                }


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void downLoadApk(String url) {
        final String filePath = SdCardUtil.getSdPath() + SdCardUtil.FILEDIR + SdCardUtil.FILEAPK+ "/"+"zhouzhuang" + ".apk";
        String[] allowedContentTypes = new String[]{".*"};
        AsyncHttpClientUtils.getInstance().downLoadFile(url, new BinaryHttpResponseHandler(allowedContentTypes) {

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(MainActivity.this, "", "正在下载软件, 请稍候...", true, true);
               // mProgressDialog.setCanceledOnTouchOutside（false）;
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
                if (Utils.fileIsExists(filePath)) {
                    updateDialog(filePath);
                } else {
                    Toast.makeText(MainActivity.this, "未能正确下载更新下文件", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                InputStream inputstream = new ByteArrayInputStream(binaryData);
                if (inputstream != null) {
                    SdCardUtil.write2SDFromInput(filePath, inputstream);
                    try {
                        inputstream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void updateDialog(final String filePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage("是否立刻安装");
        builder.setTitle("下载成功");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openFile(new File(filePath));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void openFile(File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
    /**
     2  * 获取版本号
     3  * @return 当前应用的版本号
     4  */
    public int getVersionCode() {
            try {
                    PackageManager manager = this.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                    int versionCode= info.versionCode;
                   return versionCode;
               } catch (Exception e) {
                    e.printStackTrace();
                return 0;
               }

        }

}
