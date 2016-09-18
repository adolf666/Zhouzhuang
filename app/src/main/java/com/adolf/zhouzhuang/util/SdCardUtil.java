package com.adolf.zhouzhuang.util;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhl13045 on 2016/9/4.
 */
public class SdCardUtil {
    // 项目文件根目录
    public static final String FILEDIR = "/zhouzhuang";
    // 照相机照片目录
    public static final String FILEAUDIO = "/audio";
    // 应用程序图片存放
    public static final String FILEIMAGE = "/images";
    // 应用程序缓存
    public static final String FILECACHE = "/cache";
    // 用户信息目录
    public static final String FILEUSER = "user";

    /*
     * 检查sd卡是否可用
     * getExternalStorageState 获取状态
     * Environment.MEDIA_MOUNTED 直译  环境媒体登上  表示，当前sd可用
     */
    public static boolean checkSdCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            //sd卡可用
            return true;
        else
            //当前sd卡不可用
            return false;
    }

    // 在sdcard卡上创建文件
    public static File createSDFile(String filePath) throws IOException {
        File file = new File(filePath);
        file.createNewFile();
        return file;
    }

    /*
     * 获取sd卡的文件路径
     * getExternalStorageDirectory 获取路径
     */
    public static String getSdPath() {
        return Environment.getExternalStorageDirectory() +"";
    }

    /*
     * 创建一个文件夹
     */
    public static void createFileDir(String fileDir) {
        String path = getSdPath() + fileDir;
        File path1 = new File(path);
        if (!path1.exists()) {
            path1.mkdirs();
        }
    }

    /**
     * 将一个inputstream里面的数据写入SD卡中 第一个参数为目录名 第二个参数为文件名
     */
    public static File write2SDFromInput(String path, InputStream inputstream) {
        File file = null;
        OutputStream output = null;
        try {
            file = createSDFile(path);
            output = new FileOutputStream(file);
// 4k为单位，每4K写一次
            byte buffer[] = new byte[4 * 1024];
            int temp = 0;
            while ((temp = inputstream.read(buffer)) != -1) {
// 获取指定信,防止写入没用的信息
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return file;
    }
}
