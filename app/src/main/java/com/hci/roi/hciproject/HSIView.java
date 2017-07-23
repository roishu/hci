package com.hci.roi.hciproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
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


public class HSIView extends View {

    private final int POINT_ARRAY_SIZE = 72;
    private final Paint localPaint;
    private final Paint textPaint;
    private final Paint eraserPaint;
    private final Paint pilotPaint;
    private final Paint bgPaint;
    private float angle = 0;

    private int fps = 100;

    float alpha = 0;
    Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
    private int alphaOffset =5;

    public HSIView(Context context) {
        this(context, null);
    }

    public HSIView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HSIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //lines paint
        localPaint = new Paint();
        localPaint.setColor(Color.GREEN);
        localPaint.setAntiAlias(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(10);
        //eraser paint
        eraserPaint = new Paint();
        eraserPaint.setAlpha(0);
        eraserPaint.setColor(Color.TRANSPARENT);
        eraserPaint.setStrokeWidth(60);
        eraserPaint.setStyle(Paint.Style.FILL);
        eraserPaint.setMaskFilter(null);
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraserPaint.setAntiAlias(true);
        //text paint (different from other paints because we don't have stroke width here - we have text size)
        textPaint = new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(100);
        //bg - same as ereaser but can be changed
        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.FILL);
        pilotPaint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        pilotPaint.setColorFilter(filter);

        //in order to manage memory leak we recycle the bitmap (free space to allocation)
        if(StaticBitmaps.planeBitmap!=null) StaticBitmaps.planeBitmap.recycle();
        StaticBitmaps.planeBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.plane_icon);

    }

    //re-draw animation applying.
    android.os.Handler mHandler = new android.os.Handler();
    Runnable mTick = new Runnable() {
        @Override
        public void run() {
            invalidate();
            mHandler.postDelayed(this, 1000 / fps);
        }
    };


    public void startAnimation() {
    }

    public void setAngle(float newAngle){
        angle = newAngle;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int translateOffest = -height/2+width/2;
        canvas.save();
        //rotate the canvas without the plane
        canvas.rotate(angle,height/2,height/2);
        int r = height; //Math.min(width, height);
        //canvas.
        canvas.drawRect(0, 0, width, height, bgPaint); //clean window
        int i = r / 2;
        int j = i - 1;
        alpha = 1;
        double angle = Math.toRadians(alpha);
        int offsetX =  (int) (i + (float)(i * Math.cos(angle)));
        int offsetY = (int) (i - (float)(i * Math.sin(angle)));

        latestPoint[0]= new Point(offsetX, offsetY);

        //Lines in Circle
        for (int x=POINT_ARRAY_SIZE-1; x > -1; x--) {
            alpha+=alphaOffset;
            alpha%=360;
            angle = Math.toRadians(alpha);
            offsetX =  (int) (i + (float)(i * Math.cos(angle)));
            offsetY = (int) (i - (float)(i * Math.sin(angle)));
            latestPoint[x]= new Point(offsetX, offsetY);
        }
        //Lines in Circle
        for (int x = 1; x < POINT_ARRAY_SIZE; x+=2) {
            Point point = latestPoint[x];
            if (point != null) {
                canvas.drawLine(i, i, point.x, point.y, localPaint);
                    canvas.drawCircle(i, i, j * 5 / 6, bgPaint);
            }
        }
        //Lines in Circle
        for (int x = 0; x < POINT_ARRAY_SIZE; x+=2) {
            Point point = latestPoint[x];
            if (point != null) {
                canvas.drawLine(i, i, point.x, point.y, localPaint);
                canvas.drawCircle(i, i, j * 3 / 4, bgPaint);
                canvas.drawCircle(i, i, j * 3 / 4, bgPaint);
            }
        }

        //Letters
        canvas.drawCircle(i, i, j >> 1, localPaint);
        canvas.drawText("N" , height/2 -25,1*height/4 -25,textPaint);
        canvas.drawText("S" , height/2 -25 ,3*height/4+100,textPaint);
        canvas.drawText("E" , 3*height/4 +25 ,1*height/2+25,textPaint);
        canvas.drawText("W" , 1*height/4 -100 ,1*height/2+25,textPaint);

        //we restore in order to rotate and after that draw the plane
        canvas.restore();
        //right here we're drawing the paint after we apply the new rotation.
        canvas.drawBitmap(StaticBitmaps.planeBitmap, null, new RectF(i-100, i-100, i+100, i+100), pilotPaint);


        }

    }


