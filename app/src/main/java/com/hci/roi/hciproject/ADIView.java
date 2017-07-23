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


/**
 * ADI - attitude directional indicator
 * shows the angle of the plane between the sky and the earth.
 * in this activity we're creating an instance of ADIView which is the canvas we want to display.
 */
public class ADIView extends View {

    private final Paint localPaint , groundPaint, skyPaint ,originPaint;
    private int skyColor = Color.parseColor("#00b0ff") ;
    private int groundColor = Color.parseColor("#795548");
    private int pivot = 0;
    private int fps = 100;
    private int anglePivot =0;

    public ADIView(Context context) {
        this(context, null);
    }

    public ADIView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ADIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //a paint to lines
        localPaint = new Paint();
        localPaint.setColor(Color.WHITE);
        localPaint.setAntiAlias(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(5);
        //a paint to sky rectangle
        skyPaint = new Paint();
        skyPaint.setColor(skyColor);
        skyPaint.setAntiAlias(true);
        skyPaint.setStyle(Paint.Style.FILL);
        //a paint to ground rectangle
        groundPaint = new Paint();
        groundPaint.setColor(groundColor);
        groundPaint.setAntiAlias(true);
        groundPaint.setStyle(Paint.Style.FILL);
        //a paint to plane "wings" rectangle
        originPaint = new Paint();
        originPaint.setColor(Color.MAGENTA);
        originPaint.setAntiAlias(true);
        originPaint.setStyle(Paint.Style.STROKE);
        originPaint.setStrokeWidth(10);

    }

    //anble re-draw (animtation)
    android.os.Handler mHandler = new android.os.Handler();
    Runnable mTick = new Runnable() {
        @Override
        public void run() {
            invalidate();
            mHandler.postDelayed(this, 1000 / fps);
        }
    };

    //animate
    public void startAnimation() {
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    //set new pivot of the plane head.
    public void setPivot(int x){
        pivot = x;
    }

    /*
     * in every iteration we're drawing the canvas again and again in a different pivots.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int r = Math.min(width, height);
        int i = r / 2;
        int j = i - 1;


        canvas.drawRect(0, -height, width, height/2+pivot, skyPaint);
        canvas.drawRect(0, height/2+pivot, width, 2*height, groundPaint);
        //center line
        canvas.drawLine(0, height/2+pivot, width, height/2+pivot, localPaint); //center
        //center vertical lines
        canvas.drawLine(width/3, height/3, 2*width/3, height/3, localPaint); //10
        canvas.drawLine(width/3, 2*height/3, 2*width/3, 2*height/3, localPaint); //10 line
        canvas.drawLine(width/3, 5*height/6, 2*width/3, 5*height/6, localPaint); //20 line
        canvas.drawLine(width/3, 1*height/6, 2*width/3, 1*height/6, localPaint); //20 line
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

        }

    }


