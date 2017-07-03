package com.hci.roi.hciproject;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.roi.aircc.R;

/**
 * Created by Roi on 03/07/2017.
 */

public class CustomDialogFragment extends Dialog  {

    private RadarView mRadarView;
    private HSIView mHSIView;
    private ADIView mADIView;
    private LinearLayout container;
    private boolean firstOnWindowFocusChangedCall = false;
    public ValueAnimator va;// = ValueAnimator.ofFloat(0, 120);
    public int mDuration = 5000; //in millis
    private PropertyValuesHolder animatorHolder;
    private int chosenViewIndex;
    //ADI-MEMBERS
    public ValueAnimator va2 = ValueAnimator.ofInt(0, 300);
    public int mDuration2 = 5000; //in millis

    public CustomDialogFragment(@NonNull Context context, int i) {
        super(context,R.style.CustomDialogStyle);
        chosenViewIndex = i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (chosenViewIndex){
            case 0:
                setContentView(R.layout.activity_hsi);
                mHSIView = (HSIView) findViewById(R.id.radarView);
                mHSIView.startAnimation();
                animHSI();
                break;
            case 1:
                setContentView(R.layout.activity_adi);
                mADIView = (ADIView) findViewById(R.id.adiView);
                mADIView.setShowCircles(true);
                mADIView.startAnimation();
                animADI();
                break;
            case 2:
                setContentView(R.layout.activity_radar);
                mRadarView = (RadarView) findViewById(R.id.radarView);
                mRadarView.setShowCircles(true);
                mRadarView.startAnimation();
                break;
            case 3:
                break;
        }
    }

    private void animADI() {


        va2.setDuration(mDuration2);
        va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mADIView.setPivot((int)animation.getAnimatedValue());
            }
        });

        va2.start();

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



}