package com.hci.roi.hciproject;

import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;

/**
 * Created by Roi on 26/06/2017.
 */


public class ADIView extends View {

    private final String LOG = "HSIView";
    private final int POINT_ARRAY_SIZE = 25;
    private final Paint localPaint , groundPaint, skyPaint ,originPaint;
    private final Context mContext;
    private int skyColor = Color.parseColor("#00b0ff") ;
    private int groundColor = Color.parseColor("#795548");
    private int pivot = 0;

    private int fps = 100;
    private boolean showCircles = true;

    float alpha = 0;
    Point latestPoint[] = new Point[POINT_ARRAY_SIZE];
    Paint latestPaint[] = new Paint[POINT_ARRAY_SIZE];
    private int anglePivot =0;

    public ADIView(Context context) {
        this(context, null);
    }

    public ADIView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ADIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        localPaint = new Paint();
        localPaint.setColor(Color.WHITE);
        localPaint.setAntiAlias(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(5);
        //
        skyPaint = new Paint();
        skyPaint.setColor(skyColor);
        skyPaint.setAntiAlias(true);
        skyPaint.setStyle(Paint.Style.FILL);
        //skyPaint.setStrokeWidth(1.0F);
        //
        groundPaint = new Paint();
        groundPaint.setColor(groundColor);
        groundPaint.setAntiAlias(true);
        groundPaint.setStyle(Paint.Style.FILL);
        //groundPaint.setStrokeWidth(1.0F);
        //
        originPaint = new Paint();
        originPaint.setColor(Color.MAGENTA);
        originPaint.setAntiAlias(true);
        originPaint.setStyle(Paint.Style.STROKE);
        originPaint.setStrokeWidth(10);

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
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    public void stopAnimation() {
        mHandler.removeCallbacks(mTick);
    }

    public void setFrameRate(int fps) { this.fps = fps; }
    public int getFrameRate() { return this.fps; };

    public void setPivot(int x){
        pivot = x;
    }

    public void setShowCircles(boolean showCircles) { this.showCircles = showCircles; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int width = getWidth();
        int height = getHeight();

        int r = Math.min(width, height);


        //canvas.drawRect(0, 0, getWidth(), getHeight(), localPaint);

        int i = r / 2;
        int j = i - 1;

       // int pivot = 0; //height/3;

        canvas.drawRect(0, -height, width, height/2+pivot, skyPaint);
        canvas.drawRect(0, height/2+pivot, width, 2*height, groundPaint);
        //center line
        canvas.drawLine(0, height/2+pivot, width, height/2+pivot, localPaint); //center
        //center vertical lines
        canvas.drawLine(width/3, height/3, 2*width/3, height/3, localPaint); //10
        //canvas.drawText(" 10", 2*width/3, height/3, localPaint);
        //canvas.drawText("10 ", width/3, height/3, localPaint);
        canvas.drawLine(width/3, 2*height/3, 2*width/3, 2*height/3, localPaint); //10
        canvas.drawLine(width/3, 5*height/6, 2*width/3, 5*height/6, localPaint); //20
        canvas.drawLine(width/3, 1*height/6, 2*width/3, 1*height/6, localPaint); //20
        //
        canvas.drawLine(5*width/12, 1*height/12, 7*width/12, 1*height/12, localPaint);
        canvas.drawLine(5*width/12, 3*height/12, 7*width/12, 3*height/12, localPaint);
        canvas.drawLine(5*width/12, 5*height/12, 7*width/12, 5*height/12, localPaint);
        canvas.drawLine(5*width/12, 7*height/12, 7*width/12, 7*height/12, localPaint);
        canvas.drawLine(5*width/12, 9*height/12, 7*width/12, 9*height/12, localPaint);
        canvas.drawLine(5*width/12, 11*height/12, 7*width/12, 11*height/12, localPaint);


        canvas.save();
        canvas.rotate(anglePivot, width/2, height/2);
        canvas.drawLine(1*width/6, height/2+pivot, 5*width/6, height/2+pivot, originPaint); //purple pilot line
        canvas.restore();



        //canvas.drawLine(0, height/2, width, height/2, localPaint);
        //canvas.drawLine(0, height/2, width, height/2, localPaint);
       // canvas.drawLine(0, height/2, width, height/2, localPaint);

//        if (showCircles) {
//            canvas.drawCircle(i, i, j, localPaint);
//            canvas.drawCircle(i, i, j, localPaint);
//            canvas.drawCircle(i, i, j * 3 / 4, localPaint);
//            canvas.drawCircle(i, i, j >> 1, localPaint);
//            canvas.drawCircle(i, i, j >> 2, localPaint);
//        }

            //  " - R:" + r + ", i=" + i +
            //  " - Size: " + width + "x" + height +
            //  " - Angle: " + angle +
            //  " - Offset: " + offsetX + "," + offsetY);
        }

    }


