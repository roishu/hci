package com.hci.roi.hciproject;

import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cc.roi.aircc.R;

/**
 * ADI - attitude directional indicator
 * shows the angle of the plane between the sky and the earth.
 * in this activity we're creating an instance of ADIView which is the canvas we want to display.
 */
public class ADIActivity extends AppCompatActivity {

    private ADIView adiView;
    public ValueAnimator va = ValueAnimator.ofInt(0, 300);
    public int mDuration = 5000; //in millis

    //initial part
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adi);
        setLandscapeOrientation();
        adiView = (ADIView) findViewById(R.id.adiView);
        adiView.startAnimation();
        animADI();
    }

    //animate an ADI simulation.
    private void animADI() {
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                adiView.setPivot((int)animation.getAnimatedValue());
            }
        });

        va.start();
    }

    //we must set orientation to landscape orientation in every activity in order to unable roatation.
    public void setLandscapeOrientation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }
}
