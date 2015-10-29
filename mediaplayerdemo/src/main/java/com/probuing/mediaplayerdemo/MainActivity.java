package com.probuing.mediaplayerdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.probuing.mediaplayerdemo.Interface.MusicPlay;
import com.probuing.mediaplayerdemo.service.MediaPalyerService;

public class MainActivity extends AppCompatActivity {

    private MusicConn conn;
    MusicPlay mi;
    private Intent intent;
    private static SeekBar sb;

    /**
     * handler
     */
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int duration = data.getInt("duration");
            int currentPosition = data.getInt("currentPosition");
            sb.setMax(duration);
            sb.setProgress(currentPosition);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb = ((SeekBar) findViewById(R.id.sb));
        //TODO 混合启动服务
        intent = new Intent(this, MediaPalyerService.class);
        intent.setPackage("com.probuing.mediaplayerdemo.service");
        startService(intent);
        //绑定服务
        conn = new MusicConn();
        bindService(intent, conn, BIND_AUTO_CREATE);
        //设置SeekBar滑动监听
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mi.seekTo(progress);
            }
        });
    }



    class MusicConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mi = (MusicPlay) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }


    }

    /**
     * 按钮点击事件
     *
     * @param v
     */
    public void play(View v) {
        mi.paly();
    }

    public void pause(View v) {
        mi.pause();
    }

    public void stop(View v) {
        mi.stop();
    }

    public void continePlay(View v) {
        mi.continuePlay();
    }
    /**
     * 释放资源
     */
    public void exitPlay(View v)
    {
        unbindService(conn);
        stopService(intent);
        System.exit(0);
    }
}
