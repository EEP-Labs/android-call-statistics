package org.ktln2.android.callstat;

import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


class StatSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private String TAG = "StatSurfaceView";
    private float mWidth;
    private float mHeight;
    private float mRadius;

    /*
     * http://developer.android.com/guide/topics/graphics/2d-graphics.html#on-surfaceview
     *
     * When your SurfaceView is initialized, get the SurfaceHolder by calling getHolder().
     * You should then notify the SurfaceHolder that you'd like to receive SurfaceHolder
     * callbacks (from SurfaceHolder.Callback) by calling addCallback() (pass it this).
     */
    private void init() {
        getHolder().addCallback(this);
    }

    public StatSurfaceView(Context context) {
        super(context);
        init();
    }

    public StatSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        android.util.Log.d(TAG, "surfaceDestroyed()");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        android.util.Log.d(TAG, "surfaceCreated()");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        android.util.Log.d(TAG, "surfaceChanged()");

        mWidth = width;
        mHeight = height;

        mRadius = width < height ? width : height;
    }

    public void drawPie() {
        SurfaceHolder holder = getHolder();

        Canvas canvas = holder.lockCanvas();

        Paint p = new Paint();
        p.setARGB(255, 255, 0, 0);

        canvas.drawArc(
            new RectF(0, 0, mWidth, mHeight),
            0.0F,
            90.0F,
            true,
            p
        );

        holder.unlockCanvasAndPost(canvas);
    }
}
