package com.hci.roi.hciproject;

import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cc.roi.aircc.R;

public class HSIActivity extends AppCompatActivity {

    private HSIView mHSIView;
    public ValueAnimator va = ValueAnimator.ofInt(0, 300);
    public int mDuration = 5000; //in millis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsi);
        setLandscapeOrientation();

        mHSIView = (HSIView) findViewById(R.id.hsiView);
        mHSIView.setShowCircles(true);
        mHSIView.startAnimation();

        animHSI();


    }

    private void animHSI() {


        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mHSIView.setPivot((int)animation.getAnimatedValue());
            }
        });

        va.start();

    }

    public void setLandscapeOrientation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }
}
