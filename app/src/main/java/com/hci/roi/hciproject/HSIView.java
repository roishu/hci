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

    private final String LOG = "HSIView";
    private final int POINT_ARRAY_SIZE = 72;
    private final Paint localPaint;
    private final Paint textPaint;
    private final Paint eraserPaint;
    private final Paint pilotPaint;
    private Bitmap background;
    private float angle = 0;

    private int fps = 100;
    private boolean showCircles = true;

    float alpha = 0;
    Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
    Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];
    private int alphaOffset =5;

    public HSIView(Context context) {
        this(context, null);
    }

    public HSIView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HSIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        localPaint = new Paint();
        localPaint.setColor(Color.GREEN);
        localPaint.setAntiAlias(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(10);

        eraserPaint = new Paint();
        eraserPaint.setAlpha(0);
        eraserPaint.setColor(Color.TRANSPARENT);
        eraserPaint.setStrokeWidth(60);
        eraserPaint.setStyle(Paint.Style.FILL);
        eraserPaint.setMaskFilter(null);
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraserPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(100);

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
            mHandler.postDelayed(this, 1000 / fps);
        }
    };


    public void startAnimation() {
       // mHandler.removeCallbacks(mTick);
       // mHandler.post(mTick);
    }

    public void setAngle(float newAngle){
        angle = newAngle;
        invalidate();
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
        int translateOffest = -height/2+width/2;
        canvas.save();
        canvas.rotate(angle,height/2,height/2);
        //Log.e("ANGLE" ,angle+"" );


        int r = height; //Math.min(width, height);

        //canvas.
        //canvas.drawRect(0, 0, width, height, eraserPaint); //clean window
        int i = r / 2;
        int j = i - 1;



        alpha = 1;
        double angle = Math.toRadians(alpha);
        int offsetX =  (int) (i + (float)(i * Math.cos(angle)));
        int offsetY = (int) (i - (float)(i * Math.sin(angle)));

        latestPoint[0]= new Point(offsetX, offsetY);

        for (int x=POINT_ARRAY_SIZE-1; x > -1; x--) {
            alpha+=alphaOffset;
            alpha%=360;
            angle = Math.toRadians(alpha);
            offsetX =  (int) (i + (float)(i * Math.cos(angle)));
            offsetY = (int) (i - (float)(i * Math.sin(angle)));
            latestPoint[x]= new Point(offsetX, offsetY);
        }
        for (int x = 1; x < POINT_ARRAY_SIZE; x+=2) {
            Point point = latestPoint[x];
            if (point != null) {
                canvas.drawLine(i, i, point.x, point.y, localPaint);
                    canvas.drawCircle(i, i, j * 5 / 6, eraserPaint);
            }
        }
        for (int x = 0; x < POINT_ARRAY_SIZE; x+=2) {
            Point point = latestPoint[x];
            if (point != null) {
                canvas.drawLine(i, i, point.x, point.y, localPaint);
                canvas.drawCircle(i, i, j * 3 / 4, eraserPaint);
                canvas.drawCircle(i, i, j * 3 / 4, eraserPaint);
            }
        }

        canvas.drawCircle(i, i, j >> 1, localPaint);
        canvas.drawText("N" , height/2 -25,1*height/4 -25,textPaint);
        canvas.drawText("S" , height/2 -25 ,3*height/4+100,textPaint);
        canvas.drawText("E" , 3*height/4 +25 ,1*height/2+25,textPaint);
        canvas.drawText("W" , 1*height/4 -100 ,1*height/2+25,textPaint);


        canvas.restore();
        canvas.drawBitmap(StaticBitmaps.planeBitmap, null, new RectF(i-100, i-100, i+100, i+100), pilotPaint);



        //canvas.drawBitmap(StaticBitmaps.planeBitmap ,i,i,localPaint);
        //canvas.drawText("2" , latestPoint[18].x ,latestPoint[17].y -300,textPaint );
        //canvas.drawText("3" , latestPoint[36].x+300 ,latestPoint[35].y ,textPaint );
       // canvas.drawText("4" , latestPoint[54].x ,latestPoint[53].y +300,textPaint );

        if (showCircles) {
//            canvas.drawCircle(i, i, j, eraserPaint);
//            canvas.drawCircle(i, i, j, eraserPaint);
//            canvas.drawCircle(i, i, j * 3 / 4, eraserPaint);
//            canvas.drawCircle(i, i, j >> 1, eraserPaint);
//            canvas.drawCircle(i, i, j >> 2, eraserPaint);
//            invalidate();
        }


//        lines = 0;
//        for (Point p : latestPoint) if (p != null) lines++;
//
//        boolean debug = false;
//        if (debug) {
//            StringBuilder sb = new StringBuilder(" >> ");
//            for (Point p : latestPoint) {
//                if (p != null) sb.append(" (" + p.x + "x" + p.y + ")");
//            }
//
//            Log.d(LOG, sb.toString());
            //  " - R:" + r + ", i=" + i +
            //  " - Size: " + width + "x" + height +
            //  " - Angle: " + angle +
            //  " - Offset: " + offsetX + "," + offsetY);
        }

    }


