package com.ztiany.mediaplayer.android.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * 自动适配适配的比例
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 15:39
 */
public class MediaTextureView extends TextureView {

    private static final String TAG = MediaTextureView.class.getSimpleName();

    private int videoWidth;
    private int videoHeight;

    public MediaTextureView(Context context) {
        super(context);
    }

    public MediaTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 根据视频的size调整TextureView的size
     *
     * @param videoWidth  视频的宽度
     * @param videoHeight 视频的高度
     */
    public void adaptVideoSize(int videoWidth, int videoHeight) {
        if (this.videoWidth != videoWidth && this.videoHeight != videoHeight) {
            this.videoWidth = videoWidth;
            this.videoHeight = videoHeight;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width;
        int height;

        if (videoWidth > 0 && videoHeight > 0) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;
                //调整宽高

                //      视频的宽 / 视频的高  < view宽 / view高， view太宽了
                if (videoWidth * height < width * videoHeight) {
                    width = height * videoWidth / videoHeight;
                }
                //视频的宽 / 视频的高  > view宽 / view高，view太高了
                else if (videoWidth * height > width * videoHeight) {
                    height = width * videoHeight / videoWidth;
                }

            } else if (widthSpecMode == MeasureSpec.UNSPECIFIED && heightSpecMode == MeasureSpec.UNSPECIFIED) {
                width = videoWidth;
                height = videoHeight;
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = width * videoHeight / videoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }

            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                height = heightSpecSize;
                width = height * videoWidth / videoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }
            } else {//都是AT_MOST

                //取最小比例
                width = videoWidth;
                height = videoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }

            }
            Utils.d(TAG + " onMeasure() called with: width = [" + width + "], height = [" + height + "]");
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
