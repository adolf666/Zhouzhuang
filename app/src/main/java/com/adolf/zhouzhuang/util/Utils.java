package com.adolf.zhouzhuang.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by adolf on 2016/9/11.
 */
public class Utils {
    //调用外部百度地图

    public  static void openBaiduMap(Context context,String locationinfo, double lon, double lat,  String describle) {
        try {



           /* AMapLocationClientOption onceOption = new AMapLocationClientOption();
            onceOption.setOnceLocation(true);
            AMapLocationClient onceClient = new AMapLocationClient(context);
            onceClient.setLocationOption(onceOption);
            onceClient.startLocation();
            onceClient.setLocationListener(new AMapLocationListener(){

                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation != null) {
                        double LocationLat = aMapLocation.getLatitude();
                      Log.i("qqqqq",aMapLocation.getLatitude()+"");
                      Log.i("qqqqq",aMapLocation.getLongitude()+"");
                    }
                }
            });*/

            StringBuilder loc = new StringBuilder();
            loc.append("baidumap://map/walknavi?origin=");
            loc.append(locationinfo);
            loc.append("&destination=");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            Intent intent = new Intent();
            intent.setData(Uri.parse(loc.toString()));
            Log.i("qqqqqq",loc.toString());
           // intent.setData(Uri.parse("baidumap://map/walknavi?origin=40.057406655722,116.2964407172&destination=39.91441,116.40405"));
            if (isInstallPackage("com.baidu.BaiduMap")) {
                context.startActivity(intent); //启动调用

                Log.e("GasStation", "百度地图客户端已经安装");
            } else {
                Toast.makeText(context,"没有安装百度地图客户端",Toast.LENGTH_SHORT).show();
                Log.e("GasStation", "没有安装百度地图客户端");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动高德App进行导航
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:58
     * <h3>UpdateTime</h3> 2016/6/27,13:58
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param sourceApplication 必填 第三方调用应用名称。如 amap
     * @param poiname 非必填 POI 名称
     * @param lat 必填 纬度
     * @param lon 必填 经度
     * @param dev 必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style 必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    public static  void goToNaviActivity(Context context,String sourceApplication , String poiname , String lat , String lon , String dname , String locationInfo){
      /*  StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)){
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&dev=").append(dev)
                .append("&style=").append(style);*/

        StringBuffer stringBuffer  = new StringBuffer("androidamap://route?sourceApplication=softname&");
        stringBuffer.append(locationInfo).append("&dlat="+lat).append("&dlon="+lon).append("&dname="+dname).append("&dev=0&t=4");

        Log.i("qqqqq",stringBuffer.toString());

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));



       // Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://route?sourceApplication=softname&slat=36.2&slon=116.1&sname=abc&dlat=36.3&dlon=116.2&dname=def&dev=0&t=4"));
        intent.setPackage("com.autonavi.minimap");

        if (isInstallPackage("com.autonavi.minimap")) {
            context.startActivity(intent); //启动调用

            Log.e("GasStation", "高德地图客户端已经安装");
        } else {
            Toast.makeText(context,"没有安装高德地图客户端",Toast.LENGTH_SHORT).show();
            Log.e("GasStation", "没有安装高德地图客户端");
        }

    }

    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    //判断网络状态
    public static boolean isRequestOK(int statusCode){
        return statusCode == Constants.STATUS_OK;
    }

    public static  Typeface getType(Context context,int i){
         Typeface type = null;
        switch (i){
            case 0:
                type = Typeface.createFromAsset (context.getAssets() , "fonts/FZSQKB.TTF" );
             break;
            case 1:
                type = Typeface.createFromAsset (context.getAssets() , "fonts/FZS1JW.TTF" );
                break;
            case  3:
                type = Typeface.createFromAsset (context.getAssets() , "fonts/FZS3JW.TTF" );
                break;
        }
        return type;
    }

    //判断是否自动登录
    public static boolean isAutoLogin(Context context){
        return SharedPreferencesUtils.getBoolean(context,"AutoLogin",false);
    }

    //显示和隐藏输入法
    public static void displayOrHideIM(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    public static Drawable loadImageFromUrl(String imageUrl)
    {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }
        return drawable ;
    }

    public static int getDiaplayWidth(Activity activity){
        WindowManager wm = activity.getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public static String getAudioFullPath(String audioName){
        return SdCardUtil.getSdPath() + SdCardUtil.FILEDIR + SdCardUtil.FILEAUDIO + "/" + audioName + ".mp3";
    }

    public static int getScreenWidth(Activity activity){
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return  dm.widthPixels;
    }

    public static int getScreenHeight(Activity activity){
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
