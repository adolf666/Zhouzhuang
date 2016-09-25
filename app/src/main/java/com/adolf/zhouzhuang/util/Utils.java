package com.adolf.zhouzhuang.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;

/**
 * Created by adolf on 2016/9/11.
 */
public class Utils {
    //调用外部百度地图
    public  static void openBaiduMap(Context context, double lon, double lat, String title, String describle) {
        try {
            StringBuilder loc = new StringBuilder();
            loc.append("intent://map/direction?origin=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append("我的位置");
            loc.append("&destination=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append(describle);
            loc.append("&mode=driving");
            loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = Intent.getIntent(loc.toString());
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
    public static  void goToNaviActivity(Context context,String sourceApplication , String poiname , String lat , String lon , String dev , String style){
        StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)){
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&dev=").append(dev)
                .append("&style=").append(style);

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
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
}
