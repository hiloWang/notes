package com.ztiany.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * setImageStream(getContext().getAssets().open("qm.jpg"));
 */
public class LargeImageView extends View {

    private int mImageWidth, mImageHeight;
    private Rect mRect;
    private BitmapFactory.Options mOptions;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private float mLastX, mLastY;

    public LargeImageView(Context context) {
        this(context, null);
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int left = mImageWidth / 2 - measuredWidth / 2;
        int top = mImageHeight / 2 - measuredHeight / 2;
        mRect.set(left, top, left + measuredWidth, top + measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmapRegionDecoder != null) {
            canvas.drawBitmap(mBitmapRegionDecoder.decodeRegion(mRect, mOptions), 0, 0, null);
        }
    }


    public void setImageStream(InputStream imageStream) {
        try {
            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(imageStream, false);
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init() {
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = x;
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float dx = mLastX - x;
                float dy = mLastY - y;
                onMove(dx, dy);
                mLastX = x;
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        return true;
    }

    private void onMove(float moveX, float moveY) {
        if (mImageWidth > getWidth()) {
            mRect.offset((int) moveX, 0);
            invalidate();
        }
        if (mImageHeight > getHeight()) {
            mRect.offset(0, (int) moveY);
            invalidate();
        }
    }
}
