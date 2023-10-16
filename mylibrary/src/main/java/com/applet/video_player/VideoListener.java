package com.applet.video_player;

import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;

/**
 * Created by Alien-super on 2023/9/9.
 */
public interface VideoListener {
    int EVENT_START = 1;        // 开始播放
    int EVENT_PREPARED = 2;     // 播放准备成功
    int EVENT_COMPLETION = 3;   // 播放完成
    int EVENT_ERROR = -1;       // 视频播放出错

    void onVideoPrepared();

    void onVideoStart();

    void onVideoCompletion();

    void onVideoInfo(InfoBean infoBean);

    void onVideoStateChanged(int newState);

    void onVideoError(ErrorInfo errorInfo);

    void onVideoRendered(long timeMs, long pts);
}
