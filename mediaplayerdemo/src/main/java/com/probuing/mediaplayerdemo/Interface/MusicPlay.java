package com.probuing.mediaplayerdemo.Interface;

/**
 * 服务中暴露的方法
 * Created by probuing on 2015/10/29.
 */
public interface MusicPlay {
    //TODO 播放音乐的方法
    void paly();

    //TODO 暂停播放的方法
    void pause();
    //TODO 停止播放的方法
    void stop();
    void continuePlay();
    //传递数据方法
    void seekTo(int progress);
}
