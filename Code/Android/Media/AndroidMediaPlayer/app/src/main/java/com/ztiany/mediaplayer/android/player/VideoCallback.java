package com.ztiany.mediaplayer.android.player;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 15:34
 */
interface VideoCallback {

    void onBuffering(int percent);

    void onError(Exception e);
}
