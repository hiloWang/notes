package com.ztiany.mediaplayer.android.player;

import android.net.Uri;
import android.support.annotation.IntRange;

import java.util.Map;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 15:34
 */
public interface IMediaPlayer {

    void setDataSource(Uri uri, Map<String, String> headers);

    void setCallback(VideoCallback callback);

    ///////////////////////////////////////////////////////////////////////////
    // 状态判断
    ///////////////////////////////////////////////////////////////////////////

    boolean isPrepared();

    boolean isPlaying();

    boolean isPaused();

    int getCurrentState();

    ///////////////////////////////////////////////////////////////////////////
    // 信息获取
    ///////////////////////////////////////////////////////////////////////////

    int getCurrentPosition();

    int getDuration();

    int getMaxVolume();

    int getVolume();

    ///////////////////////////////////////////////////////////////////////////
    // 操作
    ///////////////////////////////////////////////////////////////////////////

    void seekTo(@IntRange(from = 0, to = Integer.MAX_VALUE) int pos);

    void setVolume(int volume);

    void start();

    void pause();

    void stop();

    void release();
}
