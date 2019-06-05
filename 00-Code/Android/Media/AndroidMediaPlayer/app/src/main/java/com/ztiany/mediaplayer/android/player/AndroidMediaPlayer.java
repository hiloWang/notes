package com.ztiany.mediaplayer.android.player;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.Map;

import static com.ztiany.mediaplayer.android.player.AndroidMediaPlayer.State.*;


/**
 * 使用MediaPlayer+TextureView封装的适配播放器
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 15:37
 * @see <a href='http://www.jianshu.com/p/420f7b14d6f6'>用MediaPlayer+TextureView封装一个完美实现全屏、小窗口的视频播放器</a>，<a href='https://github.com/afollestad/easy-video-player'>easy-video-player</a>
 */
public class AndroidMediaPlayer extends FrameLayout implements IMediaPlayer {


    private MediaPlayer mMediaPlayer;//用于播放
    private MediaController mMediaController;//UI控制器
    private AudioManager mAudioManager;//用于调节音量

    private MediaTextureView mMediaTextureView;//用于承载视频
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;

    private Map<String, String> mHeaders;//请求头
    private Uri mSource;//视频源

    private VideoCallback mVideoCallback;//回调

    private int mState = State.STATE_IDLE;//当前状态

    public static class State {

        /**
         * 播放错误
         **/
        public static final int STATE_ERROR = -1;
        /**
         * 播放未开始
         **/
        public static final int STATE_IDLE = 0;
        /**
         * 播放准备中
         **/
        public static final int STATE_PREPARING = 1;
        /**
         * 播放准备就绪
         **/
        public static final int STATE_PREPARED = 2;
        /**
         * 正在播放
         **/
        public static final int STATE_PLAYING = 3;
        /**
         * 暂停播放
         **/
        public static final int STATE_PAUSED = 4;
        /**
         * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
         **/
        public static final int STATE_BUFFERING_PLAYING = 5;
        /**
         * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
         **/
        public static final int STATE_BUFFERING_PAUSED = 6;
        /**
         * 播放完成
         **/
        public static final int STATE_COMPLETED = 7;
    }

    public AndroidMediaPlayer(@NonNull Context context) {
        this(context, null);
    }

    public AndroidMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AndroidMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initMediaController();
    }

    ///////////////////////////////////////////////////////////////////////////
    // MediaController
    ///////////////////////////////////////////////////////////////////////////

    private void initMediaController() {
        if (mMediaController == null) {
            mMediaController = new DefaultMediaController(getContext());
            addView(mMediaController, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void setMediaController(MediaController mediaController) {
        if (mMediaController != null) {
            removeView(mMediaController);
            mMediaController.onRemoved();
        }
        mMediaController = mediaController;
        mMediaController.initCurrentState(mState);
        addMediaController(mMediaController);
    }

    private void addMediaController(MediaController mediaController) {
        addView(mediaController, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mediaController.setMediaPlayer(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // init
    ///////////////////////////////////////////////////////////////////////////

    private void init() {
        //设置黑色背景
        setBackgroundColor(Color.BLACK);
        //初始化TextureView
        mMediaTextureView = new MediaTextureView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //后面会根据视频的size调整Texture的Size，所以这里需要设置为居中
        params.gravity = Gravity.CENTER;
        addView(mMediaTextureView, params);
        mMediaTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        //初始化MediaPlayer
        mMediaPlayer = new MediaPlayer();
        setupMediaPlayer();
        //获取AudioManager
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    private void setupMediaPlayer() {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mMediaPlayer.setOnInfoListener(mOnInfoListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Impl
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void setDataSource(Uri uri, Map<String, String> headers) {
        mSource = uri;
        mHeaders = headers;
    }

    @Override
    public void setCallback(@NonNull VideoCallback callback) {
        mVideoCallback = callback;
    }

    @Override
    public boolean isPrepared() {
        return mState == State.STATE_PREPARED;
    }

    @Override
    public boolean isPlaying() {
        return mState == State.STATE_PLAYING;
    }

    @Override
    public boolean isPaused() {
        return mState == State.STATE_PAUSED || mState == State.STATE_BUFFERING_PAUSED;
    }

    @Override
    public int getCurrentState() {
        return mState;
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getDuration();
    }

    /**
     * 播放流程：1：{@link #setDataSource(Uri, Map)}，2：start()
     */
    @Override
    public void start() {
        if (mSource == null) {
            Utils.e("source is null");
            return;
        }
        if (mSurfaceTexture == null) {
            Utils.e("mSurfaceTexture is null");
            return;
        }
        //屏幕常亮
        setKeepScreenOn(true);
        if (mState == State.STATE_IDLE || mState == State.STATE_ERROR) {
            prepare();
        } else if (mState == State.STATE_PREPARED || mState == State.STATE_PAUSED) {
            mMediaPlayer.start();
            mState = STATE_BUFFERING_PLAYING;
            notifyStateChanged();
        } else if (mState == State.STATE_BUFFERING_PAUSED) {
            mMediaPlayer.start();
            mState = STATE_BUFFERING_PLAYING;
            notifyStateChanged();
        } else if (mState == STATE_BUFFERING_PLAYING) {
            // no need op
            Utils.e("no need start, state = " + mState);
        } else {
            Utils.e("can not start, state = " + mState);
        }
    }

    private void prepare() {
        try {
            if (mSurface == null) {
                mSurface = new Surface(mSurfaceTexture);
            }
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setDataSource(getContext(), mSource, mHeaders);
            mMediaPlayer.prepareAsync();
            mState = State.STATE_PREPARING;
            notifyStateChanged();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.e("prepare error：" + e);
        }
    }

    @Override
    public void seekTo(@IntRange(from = 0, to = Integer.MAX_VALUE) int pos) {
        if (mMediaPlayer != null) {
            if ((mState == State.STATE_PLAYING || mState == State.STATE_PAUSED)) {
                mMediaPlayer.seekTo(pos);
            }
        }
    }

    @Override
    public void setVolume(int volume) {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

    @Override
    public void pause() {
        if (mState == State.STATE_PLAYING) {
            mMediaPlayer.pause();
            mState = State.STATE_PAUSED;
            notifyStateChanged();
        } else if (mState == State.STATE_BUFFERING_PLAYING) {
            mMediaPlayer.pause();
            mState = State.STATE_BUFFERING_PAUSED;
            notifyStateChanged();
        } else {
            Utils.e("can not pause, state = " + mState);
        }
    }

    @Override
    public int getMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }

    @Override
    public int getVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            //stop 则进入空闲状态
            mState = State.STATE_IDLE;
            notifyStateChanged();
        }
    }

    @Override
    public void release() {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(null);
            mAudioManager = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mState = STATE_IDLE;
        setKeepScreenOn(false);
        notifyStateChanged();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 各种监听
    ///////////////////////////////////////////////////////////////////////////

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        /*
         * 当mContainer移除重新添加后，mContainer及其内部的mTextureView和mController都会重绘，mTextureView重绘后，
         * 会重新new一个SurfaceTexture，并重新回调onSurfaceTextureAvailable方法，这样mTextureView的数据通道SurfaceTexture发生了变化，
         * 但是mMediaPlayer还是持有原先的mSurfaceTexture，所以在切换全屏之前要保存之前的mSurfaceTexture，
         * 当切换到全屏后重新调用onSurfaceTextureAvailable时，将之前的mSurfaceTexture重新设置给mTextureView。这样就保证了切换时视频播放的无缝衔接。
         */
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            if (mSurfaceTexture == null) {
                mSurfaceTexture = surface;
            } else {
                mMediaTextureView.setSurfaceTexture(mSurfaceTexture);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // no op
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            // no op
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            // no op
        }
    };


    /**
     * 准备就绪的回调
     */
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mState = State.STATE_PREPARED;
            notifyStateChanged();
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            //no op
            Utils.d("onVideoSizeChanged() called with: mp = [" + mp + "], width = [" + width + "], height = [" + height + "]");
            if (mMediaTextureView != null) {
                mMediaTextureView.adaptVideoSize(width, height);
            }
        }
    };

    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            //no op
        }
    };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mState = State.STATE_COMPLETED;
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (mVideoCallback != null) {
                mVideoCallback.onBuffering(percent);
            }
        }
    };

    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            mState = State.STATE_ERROR;
            notifyStateChanged();
            int samSungErrorCode = -38;
            if (what == samSungErrorCode) {
                // Error code -38 happens on some SamSung devices
                // Just ignore it
                return false;
            }
            String errorMsg = "Preparation/playback error (" + what + "): ";
            switch (what) {
                default:
                    errorMsg += "Unknown error";
                    break;
                case MediaPlayer.MEDIA_ERROR_IO:
                    errorMsg += "I/O error";
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                    errorMsg += "Malformed";
                    break;
                case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    errorMsg += "Not valid for progressive playback";
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    errorMsg += "Server died";
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    errorMsg += "Timed out";
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                    errorMsg += "Unsupported";
                    break;
            }
            if (mVideoCallback != null) {
                mVideoCallback.onError(new Exception(errorMsg));
            }
            return true;
        }
    };

    private void notifyStateChanged() {
        mMediaController.onStateChanged(mState);
    }

    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            // 播放器开始渲染
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                mState = State.STATE_PLAYING;
                Utils.d("onInfo ——> MEDIA_INFO_VIDEO_RENDERING_START：STATE_PLAYING");
                notifyStateChanged();
            }
            // MediaPlayer暂时不播放，以缓冲更多的数据
            else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                if (mState == State.STATE_PAUSED || mState == State.STATE_BUFFERING_PAUSED) {
                    mState = State.STATE_BUFFERING_PAUSED;
                    Utils.d("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PAUSED");
                } else {
                    mState = STATE_BUFFERING_PLAYING;
                    Utils.d("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PLAYING");
                }
                notifyStateChanged();
            }
            // 填充缓冲区后，MediaPlayer恢复播放/暂停
            else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                if (mState == State.STATE_BUFFERING_PLAYING) {
                    mState = State.STATE_PLAYING;
                    Utils.d("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PLAYING");
                }
                if (mState == State.STATE_BUFFERING_PAUSED) {
                    mState = State.STATE_PAUSED;
                    Utils.d("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PAUSED");
                }
                notifyStateChanged();
            } else if (what == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
                Utils.d("视频不能seekTo");
            } else {
                Utils.d("onInfo ——> what：" + what);
            }
            return true;
        }
    };

}
