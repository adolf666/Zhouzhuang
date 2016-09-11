package com.adolf.zhouzhuang.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by adolf on 2016/9/11.
 */
public class Utils {
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

    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
