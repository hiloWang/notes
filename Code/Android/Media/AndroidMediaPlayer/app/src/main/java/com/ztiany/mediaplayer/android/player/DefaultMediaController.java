package com.ztiany.mediaplayer.android.player;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 16:04
 */
public class DefaultMediaController extends MediaController {

    public DefaultMediaController(@NonNull Context context) {
        super(context);
    }

    public DefaultMediaController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultMediaController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
