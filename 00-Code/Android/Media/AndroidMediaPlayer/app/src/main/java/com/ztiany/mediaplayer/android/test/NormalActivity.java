package com.ztiany.mediaplayer.android.test;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ztiany.mediaplayer.android.R;
import com.ztiany.mediaplayer.android.Source;
import com.ztiany.mediaplayer.android.Video;
import com.ztiany.mediaplayer.android.player.AndroidMediaPlayer;

import java.util.List;
import java.util.Random;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 17:18
 */
public class NormalActivity extends AppCompatActivity {

    private static final String TAG = NormalActivity.class.getSimpleName();

    private AndroidMediaPlayer mAndroidMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        mAndroidMediaPlayer = (AndroidMediaPlayer) findViewById(R.id.media_player);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Video> videoListData = Source.getVideoListData();
                Video video = videoListData.get(new Random().nextInt(videoListData.size()-1));
                mAndroidMediaPlayer.setDataSource(Uri.parse(video.getVideoUrl()), null);
                mAndroidMediaPlayer.start();
            }
        }, 3000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mAndroidMediaPlayer.isPaused()) {
            mAndroidMediaPlayer.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        mAndroidMediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAndroidMediaPlayer.release();
    }
}
