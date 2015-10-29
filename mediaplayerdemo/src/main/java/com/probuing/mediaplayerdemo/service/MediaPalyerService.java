package com.probuing.mediaplayerdemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;

import com.probuing.mediaplayerdemo.Interface.MusicPlay;
import com.probuing.mediaplayerdemo.MainActivity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 播放音乐的服务类
 * Created by probuing on 2015/10/29.
 */
public class MediaPalyerService extends Service{
    private MediaPlayer player;
    private String path;
    private String internetPath;
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicController();
    }

    /**
     * 生命周期方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
        //创建播放器对象
        player = new MediaPlayer();
        //指定资源
        path = Environment.getExternalStorageDirectory()+"/shuangti.mp3";
        internetPath = "http://192.168.1.102:8080/shuangti.mp3";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        if (timer != null) {
            //取消计时
            timer.cancel();
            //释放引用
            timer =null;
        }
    }

    /**
     * 中间人对象
     */
    class MusicController extends Binder implements MusicPlay
    {

        @Override
        public void paly() {
            MediaPalyerService.this.play();
        }

        @Override
        public void pause() {
            MediaPalyerService.this.pause();

        }

        @Override
        public void stop() {
            MediaPalyerService.this.stop();

        }

        @Override
        public void continuePlay() {
            MediaPalyerService.this.continuePlay();
        }

        @Override
        public void seekTo(int progress) {
            MediaPalyerService.this.seekTo(progress);
        }


    }

    private void seekTo(int progress) {

        player.seekTo(progress);
    }

    /**
     * 服务中的方法
     */
    public void play()
    {
        //重置播放器状态
        player.reset();
        try {
            /**
             * TODO 加载本地文件
             */
//            player.setDataSource(path);
//            player.prepare();
//            player.start();
            /**
             * TODO 加载网络文件
             */
            player.setDataSource(internetPath);
            //设置异步准备
            player.prepareAsync();
            //设置准备监听
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                //准备完毕后调用
                @Override
                public void onPrepared(MediaPlayer mp) {
                    System.out.println("准备完毕");
                    player.start();
                    //添加进度显示
                    addTimer();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 继续播放
     */
    public void continuePlay(){
        player.start();
    }
    public void pause()
    {
        player.pause();
    }
    public void stop()
    {
        player.stop();
    }

    /**
     * 添加计时器
     */
    public void addTimer()
    {
        if(timer == null) {
            timer = new Timer();
            //延迟5毫秒执行，每500毫秒执行一次
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //记录当前播放数据
                    //总长度
                    int duration = player.getDuration();
                    //当前播放
                    int currentPosition = player.getCurrentPosition();
                    Message msg = MainActivity.handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentPosition",currentPosition);
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
            },5,500);
        }
    }
}
