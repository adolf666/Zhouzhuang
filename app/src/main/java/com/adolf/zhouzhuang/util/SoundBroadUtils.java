package com.adolf.zhouzhuang.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.net.URI;

/**
 * Created by gpp on 2016/9/8 0008.
 */
public class SoundBroadUtils {

    private static SoundBroadUtils mInstance;
    private boolean is_Playing=false;
    public static SoundBroadUtils getInstance(){
        if(mInstance == null){
            mInstance = new SoundBroadUtils();
        }
        return mInstance;
    }

    private SoundBroadUtils(){}

    private MediaPlayer mp;

    private int bocast_time = 1;
    private final int MAX_BROAD_TIME = 3;


  /*  public synchronized void playSound(final Context context, final String filePath) {

        playBroadSound(context,filePath);
    }*/

    public synchronized void playSound(final Context context, int resId) {

        if(is_Playing){
            return;
        }
        is_Playing=true;
        playBroadSound(context, resId);
    }

    public void  pauseSound(boolean is_pause){
        if(mp != null){
            if(is_pause){
                mp.start();
            }else{
                mp.pause();
                is_Playing = false;
            }
        }
    }

    public void  stopSound(){
        if(mp != null){
            mp.stop();
            mp.release();
                is_Playing = false;
            }
        }

    private void playBroadSound(final Context context,final int resId){
        if(mp != null){
            mp.release();
            mp = null;
        }
        mp = MediaPlayer.create(context,resId);
        try {
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    bocast_time++;
                    if (bocast_time < MAX_BROAD_TIME + 1){
                        playBroadSound(context,resId);
                    }else{
                        bocast_time = 1;
                        is_Playing = false;
                        mp.release();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            bocast_time = 1;
            mp.release();
            is_Playing = false;
        }
    }
   /* private void playBroadSound(final Context context, final String filePath){
        if(mp != null){
            mp.release();
            mp = null;
        }
        mp = MediaPlayer.create(context, Uri.parse(filePath));
        try {
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    bocast_time++;
                    if (bocast_time < MAX_BROAD_TIME + 1){
                        playBroadSound(context,filePath);
                    }else{
                        bocast_time = 1;
                        is_palying = false;
                        mp.release();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            bocast_time = 1;
            is_palying = false;
            mp.release();
        }
    }*/

    public static String getLogTag() {
        return SoundBroadUtils.class.getSimpleName();
    }


}
