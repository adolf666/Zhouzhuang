package com.adolf.zhouzhuang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adolf.zhouzhuang.R;
import com.adolf.zhouzhuang.util.StreamingMediaPlayer;

import java.io.IOException;

/**
 * Created by gpp on 2016/10/27 0027.
 */

public class MediaPlayerActivity extends Activity {

    private Button streamButton;
    private ImageButton playButton;
    private boolean isPlaying;
    private TextView playTime;
    private StreamingMediaPlayer audioStreamer;

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);

        setContentView(R.layout.activity_mediaplayer);
        initControls();
    }

    private void initControls() {
        playTime=(TextView) findViewById(R.id.playTime);
        streamButton = (Button) findViewById(R.id.button_stream);

        streamButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startStreamingAudio();
            }});

        playButton = (ImageButton) findViewById(R.id.button_play);
        playButton.setEnabled(false);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (audioStreamer.getMediaPlayer().isPlaying()) {
                    audioStreamer.getMediaPlayer().pause();
                    playButton.setImageResource(R.mipmap.button_play);
                } else {
                    audioStreamer.getMediaPlayer().start();
                    audioStreamer.startPlayProgressUpdater();
                    playButton.setImageResource(R.mipmap.button_pause);
                }
                isPlaying = !isPlaying;
            }});
    }

    private void startStreamingAudio() {
        try {
            final SeekBar progressBar = (SeekBar) findViewById(R.id.progress_bar);
            if ( audioStreamer != null) {
                audioStreamer.interrupt();
            }
            audioStreamer = new StreamingMediaPlayer(this, playButton, streamButton,  progressBar,playTime);
            audioStreamer.startStreaming("http://qqma.tingge123.com:83/123/2015/08/%E5%A4%B1%E6%81%8B%E9%98%B5%E7%BA%BF%E8%81%94%E7%9B%9F%E7%B2%A4%E8%AF%AD%E7%89%88-%E8%8D%89%E8%9C%A2.mp3",5208, 216);
            streamButton.setEnabled(false);
        } catch (IOException e) {
            Log.e(getClass().getName(), "Error starting to stream audio.", e);
        }

    }

}
