package com.hci.roi.hciproject;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cc.roi.aircc.R;

public class HSIActivity extends AppCompatActivity {

    private HSIView mHSIView;
    private LinearLayout container;
    private boolean firstOnWindowFocusChangedCall = false;
    public ValueAnimator va;// = ValueAnimator.ofFloat(0, 120);
    public int mDuration = 5000; //in millis
    private PropertyValuesHolder animatorHolder;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.e("WHERE AM I" , getApplicationContext().toString());
        super.onWindowFocusChanged(hasFocus);
        if(firstOnWindowFocusChangedCall){
            container = (LinearLayout) findViewById(R.id.hsi_container);
            Log.e("WHERE AM I" , getParentActivityIntent().toString());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(container.getHeight() , container.getHeight());
            RelativeLayout.LayoutParams cParams = new RelativeLayout.LayoutParams(container.getHeight() , container.getHeight());
            container.setLayoutParams(cParams);
            mHSIView = (HSIView) findViewById(R.id.radarView);
            mHSIView.setLayoutParams(params);
            firstOnWindowFocusChangedCall=false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsi);
        mHSIView = (HSIView) findViewById(R.id.radarView);
        setLandscapeOrientation();
        animHSI();

    }

    private void animHSI() {


        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("1", 0f, 150f);
        final PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("2", 150f, 45f);
        final PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("3", 45f, 360f);

        setAnimationProperties(pvh1);
        va.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setAnimationProperties(pvh2);
                va.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setAnimationProperties(pvh3);
                        va.start();
                    }
                }, mDuration);

            }
        }, mDuration);





    }

    private void setAnimationProperties(PropertyValuesHolder animatorHolder) {
        va = ValueAnimator.ofPropertyValuesHolder(animatorHolder);
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mHSIView.setAngle((float)animation.getAnimatedValue());
                //Log.e("ANIMATOR" ,(float)animation.getAnimatedValue()+"" ); //WORK!
            }
        });
    }


    public void setLandscapeOrientation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }
}
