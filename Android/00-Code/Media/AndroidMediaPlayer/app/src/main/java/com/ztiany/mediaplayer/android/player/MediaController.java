package com.ztiany.mediaplayer.android.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 控制器基础行为封装
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 15:54
 */
public class MediaController extends FrameLayout {

    private static final long UPDATE_INTERVAL = 200;
    private Handler mHandler;
    private IMediaPlayer mIMediaPlayer;
    protected int mCurrentState;

    public MediaController(@NonNull Context context) {
        this(context, null);
    }

    public MediaController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new Handler(Looper.getMainLooper());
    }

    void initCurrentState(int currentState) {
        mCurrentState = currentState;
        onInitStateSet();
    }


    /**
     * Runnable used to run code on an interval to update counters and seeker，用于实时更新播放进度
     */
    private final Runnable mUpdateCounters = new Runnable() {
        @Override
        public void run() {
            if (mHandler == null || mIMediaPlayer == null || !mIMediaPlayer.isPrepared()) {
                return;
            }
            int pos = mIMediaPlayer.getCurrentPosition();
            final int dur = mIMediaPlayer.getDuration();
            if (pos > dur) {
                pos = dur;
            }
            onVideoProgressUpdate(pos, dur);
            if (mHandler != null) {
                mHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        }
    };

    public void setMediaPlayer(IMediaPlayer iMediaPlayer) {
        mIMediaPlayer = iMediaPlayer;
        mIMediaPlayer.setCallback(new VideoCallback() {
            @Override
            public void onBuffering(int percent) {
                MediaController.this.onVideoBuffering(percent);
            }

            @Override
            public void onError(Exception e) {
                mHandler.removeCallbacks(mUpdateCounters);
                MediaController.this.onError(e);
            }
        });
    }


    void onStateChanged(int state) {
        if (mCurrentState == state) {
            return;
        }
        mCurrentState = state;
        switch (mCurrentState) {
            case AndroidMediaPlayer.State.STATE_PAUSED:
                onPaused();
                break;
            case AndroidMediaPlayer.State.STATE_PLAYING:
                mHandler.post(mUpdateCounters);
                onPlaying();
                break;
            case AndroidMediaPlayer.State.STATE_IDLE:
                mHandler.removeCallbacks(mUpdateCounters);
                onIdle();
                break;
            case AndroidMediaPlayer.State.STATE_COMPLETED:
                mHandler.removeCallbacks(mUpdateCounters);
                onCompletion();
                break;
            case AndroidMediaPlayer.State.STATE_PREPARED:
                onPrepared();
                break;
            case AndroidMediaPlayer.State.STATE_PREPARING:
                onPreparing();
                break;
            case AndroidMediaPlayer.State.STATE_BUFFERING_PLAYING:
                onBufferingPlaying();
                break;
            case AndroidMediaPlayer.State.STATE_BUFFERING_PAUSED:
                onBufferPaused();
                break;
            default:
                break;
        }
    }


    //@formatter:off

        protected void onInitStateSet() {}

        protected void onRemoved() {
        mIMediaPlayer.setCallback(null);
    }
        protected void onError(Exception e) {}

  protected void onVideoBuffering(int percent) {}
  protected void onVideoProgressUpdate(int pos, int dur) {}
  protected void onPlaying() {}
  protected void onBufferPaused() {    }
  protected void onBufferingPlaying() {}
  protected void onIdle() {}
  protected void onCompletion(){}
  protected void onPaused( ){}
  protected void onPreparing( ){}
  protected void onPrepared( ){}

    //@formatter:on

}
