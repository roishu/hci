package com.hci.roi.hciproject;

import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cc.roi.aircc.R;

/**
 * Created by Roi on 26/06/2017.
 */


public class RadarView extends View {

    private final String LOG = "RadarView";
    private final int POINT_ARRAY_SIZE = 25;
    private final Paint pilotPaint;

    private int fps = 100;
    private boolean showCircles = true;

    float alpha = 0;
    Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
    Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];
    private Paint eraserPaint;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Paint localPaint = new Paint();
        localPaint.setColor(Color.GREEN);
        localPaint.setAntiAlias(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(1.0F);
        localPaint.setAlpha(0);

        eraserPaint = new Paint();
        eraserPaint.setColor(Color.BLACK);
        eraserPaint.setAntiAlias(true);
        eraserPaint.setStyle(Paint.Style.FILL);

        int alpha_step = 255 / POINT_ARRAY_SIZE;
        for (int i=0; i < latestPaint.length; i++) {
            latestPaint[i] = new Paint(localPaint);
            latestPaint[i].setAlpha(255 - (i* alpha_step));
        }

        pilotPaint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        pilotPaint.setColorFilter(filter);


        if(StaticBitmaps.planeBitmap!=null) StaticBitmaps.planeBitmap.recycle();
        StaticBitmaps.planeBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.plane_icon);

    }


    android.os.Handler mHandler = new android.os.Handler();
    Runnable mTick = new Runnable() {
        @Override
        public void run() {
            invalidate();
            mHandler.postDelayed(this, 0);
        }
    };


    public void startAnimation() {
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    public void stopAnimation() {
        mHandler.removeCallbacks(mTick);
    }

    public void setFrameRate(int fps) { this.fps = fps; }
    public int getFrameRate() { return this.fps; };

    public void setShowCircles(boolean showCircles) { this.showCircles = showCircles; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int width = getWidth();
        int height = getHeight();

        int r = Math.min(width, height);


        canvas.drawRect(0, 0, getWidth(), getHeight(), eraserPaint);

        int i = r / 2;
        int j = i - 1;
        Paint localPaint = latestPaint[0]; // GREEN

        if (showCircles) {
            canvas.drawCircle(i, i, j, localPaint);
            canvas.drawCircle(i, i, j, localPaint);
            canvas.drawCircle(i, i, j * 3 / 4, localPaint);
            canvas.drawCircle(i, i, j >> 1, localPaint);
            canvas.drawCircle(i, i, j >> 2, localPaint);
        }

        alpha -= 0.75;
        if (alpha < -360) alpha = 0;
        double angle = Math.toRadians(alpha);
        int offsetX =  (int) (i + (float)(i * Math.cos(angle)));
        int offsetY = (int) (i - (float)(i * Math.sin(angle)));

        latestPoint[0]= new Point(offsetX, offsetY);

        for (int x=POINT_ARRAY_SIZE-1; x > 0; x--) {
            latestPoint[x] = latestPoint[x-1];
        }



        int lines = 0;
        for (int x = 0; x < POINT_ARRAY_SIZE; x++) {
            Point point = latestPoint[x];
            if (point != null) {
                canvas.drawLine(i, i, point.x, point.y, latestPaint[x]);
            }
        }//for

        canvas.drawBitmap(StaticBitmaps.planeBitmap, null, new RectF(i-100, i-100, i+100, i+100), pilotPaint);


    }

    }


