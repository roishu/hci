package com.hci.roi.hciproject;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cc.roi.aircc.R;

public class RadarActivity extends AppCompatActivity {

    private RadarView mRadarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        setLandscapeOrientation();

        mRadarView = (RadarView) findViewById(R.id.radarView);
        mRadarView.setShowCircles(true);
        mRadarView.startAnimation();

    }

    public void setLandscapeOrientation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

}
