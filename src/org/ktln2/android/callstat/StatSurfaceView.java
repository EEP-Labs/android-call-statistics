package org.ktln2.android.callstat;

import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;


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

    /*
     * Return the data divided using some bins.
     *
     * http://stackoverflow.com/questions/10786465/how-to-generate-bins-for-histogram-using-apache-math-3-0-in-java
     */
    private static int[] calcHistogram(double[] data, double min, double max, int numBins) {
        final int[] result = new int[numBins];
        final double binSize = (max - min)/numBins;

        for (double d : data) {
            int bin = (int) ((d - min) / binSize);
            if (bin < 0) { /* this data is smaller than min */ }
            else if (bin >= numBins) { /* this data point is bigger than max */ }
            else {
                result[bin] += 1;
            }
        }

        return result;
    }

    public void drawHistogram(double[] durations) {
        int[] values = calcHistogram(durations, 0D, 1000D, 100);
        SurfaceHolder holder = getHolder();

        Canvas canvas = holder.lockCanvas();

        Paint paint = new Paint();
        paint.setARGB(255, 0, 255, 0);

        int step_x = 10;
        int step_y = 10;

        Rect rect = new Rect();
        rect.right = step_x;
        for (int value : values) {
            rect.offset(step_x, 0);
            rect.bottom = step_y*value;
            canvas.drawRect(
                rect, paint
            );
        }
        holder.unlockCanvasAndPost(canvas);
    }

    public void drawPie(float[] values) {
        SurfaceHolder holder = getHolder();

        Canvas canvas = holder.lockCanvas();

        Paint[] p = new Paint[]{
            new Paint(),
            new Paint()
        };
        p[0].setARGB(255, 255, 0, 0);
        p[1].setARGB(255, 0, 255, 0);

        float total = 0;
        for (int cycle = 0 ; cycle < values.length ; cycle++) {
            total += values[cycle];
        }

        float start_angle = 0;
        float end_angle = 0;
        for (int cycle = 0 ; cycle < values.length ; cycle++) {
            end_angle = (values[cycle]/total)*360;
            canvas.drawArc(
                new RectF(0, 0, mRadius, mRadius),
                start_angle,
                end_angle,
                true,
                p[cycle]
            );

            start_angle += end_angle;
        }

        holder.unlockCanvasAndPost(canvas);
    }
}
