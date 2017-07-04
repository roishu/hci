package com.hci.roi.hciproject;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.cc.roi.aircc.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Random;

import static com.hci.roi.hciproject.LatLngResource.ISRAEL_EAST_BOUND;
import static com.hci.roi.hciproject.LatLngResource.MAALE_ADUMIM;
import static com.hci.roi.hciproject.LatLngResource.SYDNEY;

/**
 * Created by Roi on 04/07/2017.
 */

public class Mission  {

    private boolean bool = false;
    private boolean danger = false;
    private long mDuration;
    private Random random;

    public Marker getPlaneMarker() {
        return planeMarker;
    }

    public BitmapDescriptor getPlaneDrawableMarker() {
        return planeBitmapDrawable;
    }

    private Marker planeMarker;
    private ArrayList<LatLng> missionLatLng;
    private ArrayList<Polyline> missionRoutes;
    private Context context;
    private Bitmap bitmap;
    private Bitmap mutableBitmap;
    private BitmapDescriptor planeBitmapDrawable;
    private GoogleMap googleMap;
    private float angle =0;

    public Mission(Context appContext,GoogleMap googleMapCopy){
        missionLatLng = new ArrayList<LatLng>();
        missionRoutes = new ArrayList<Polyline>();
        context = appContext;
        planeBitmapDrawable = setBitmapWithDirection(0,false);
        googleMap= googleMapCopy;
        //googleMap.setOnMarkerClickListener(this);
        random = new Random();
        mDuration = (long)(random.nextInt(3000)+5000);
    }

    /**
     * line functions
     */

    public void drawPlaneLine(LatLng src, LatLng dest) {
        LatLng between_src_dest = new LatLng(src.latitude , dest.longitude);

        //add to mission
        if(missionLatLng.size()>0 && missionLatLng.get(missionLatLng.size()-1)!=src)
            missionLatLng.add(src);
        missionLatLng.add(between_src_dest);
        missionLatLng.add(dest);

        Polyline line = googleMap.addPolyline(new PolylineOptions()
                .add(src, between_src_dest , dest )
                .width(5)
                .color(Color.MAGENTA)
                .geodesic(true));

        missionRoutes.add(line);

    }

    public void hideAllPlaneLines() {
        for(int i=0; i<missionRoutes.size() ; i++)
            missionRoutes.get(i).setVisible(false);
    }

    public void showAllPlaneLines() {
        for(int i=0; i<missionRoutes.size() ; i++)
            missionRoutes.get(i).setVisible(true);
    }


    /**
     * marker functions
     */
    public Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.setTranslate(source.getWidth()/2, source.getHeight()/2);
        matrix.preRotate(angle,source.getWidth()/2, source.getHeight()/2);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public BitmapDescriptor setBitmapWithDirection(float new_angle, boolean choose)
    {
        bool = choose;
        angle = new_angle;
        Drawable d = context.getResources().getDrawable( R.drawable.plane_icon3);
        d.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        bitmap = ((BitmapDrawable) d).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 2*bitmap.getWidth()/3, 2*bitmap.getHeight()/3, false);
        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap = rotateBitmap(mutableBitmap,angle);
        Canvas myCanvas = new Canvas(mutableBitmap);
        int myColor = mutableBitmap.getPixel(0,0);
        ColorFilter filter = new LightingColorFilter(myColor, Color.GREEN);
        if(choose)
            filter = new LightingColorFilter(myColor, Color.CYAN);
        if(danger)
            filter = new LightingColorFilter(myColor, Color.RED);
        Paint pnt = new Paint();
        pnt.setColorFilter(filter);
        myCanvas.drawBitmap(mutableBitmap,0,0,pnt);
        return BitmapDescriptorFactory.fromBitmap(mutableBitmap);
    }



    public void replaceMarker(LatLng latLng){ //add DataObject
        //Log.e("DANGER" , "latLng: " + latLng.longitude + "| MAALE_ADUMIM: "+MAALE_ADUMIM.longitude + "| danger:"+danger);
        if (planeMarker!=null) planeMarker.remove();
        if(latLng.longitude > ISRAEL_EAST_BOUND.longitude && !danger){
            Log.e("DANGER" , "danger = true");
            danger = true;
            planeBitmapDrawable = setBitmapWithDirection(angle,bool);
        }
        if (latLng.longitude < ISRAEL_EAST_BOUND.longitude && danger){
            Log.e("DANGER" , "danger = false");
            danger = false;
            planeBitmapDrawable = setBitmapWithDirection(angle,bool);
        }


        planeMarker= googleMap.addMarker(new MarkerOptions()
                .position(latLng)
//                .title("My Title")
//                .snippet("My Snippet")
                .anchor(0.5f, 0.5f)
                .icon(planeBitmapDrawable));
    }

    public void rotateMarker(float angle) {
        planeBitmapDrawable = setBitmapWithDirection(angle,bool);
        replaceMarker(planeMarker.getPosition());
    }

    public void chooseMarker(boolean bool) {
        planeBitmapDrawable = setBitmapWithDirection(angle,bool);
        replaceMarker(planeMarker.getPosition());
    }

    public void animateMyPlaneOnRoute() {
//        Log.e("PLANE" , "missionLatLng size: "+missionLatLng.size()+"");
        if(missionLatLng.size()>1)
        animatePlane(missionLatLng.get(0),missionLatLng.get(1));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(missionLatLng.size()>1)
                animatePlane(missionLatLng.get(1),missionLatLng.get(2));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(missionLatLng.size()>2)
                        animatePlane(missionLatLng.get(2),missionLatLng.get(3));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(missionLatLng.size()>3)
                                animatePlane(missionLatLng.get(3),missionLatLng.get(4));
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(missionLatLng.size()>4)
                                        animatePlane(missionLatLng.get(4),missionLatLng.get(5));
                                    }
                                }, mDuration);
                            }
                        }, mDuration);
                    }
                }, mDuration);
            }
        }, mDuration);

    }

    public void animatePlane(final LatLng src, LatLng dest){
        boolean lat = false;
        ValueAnimator va;
        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("1", (float)src.longitude, (float)dest.longitude);

        if(src.latitude != dest.latitude){
            pvh1 = PropertyValuesHolder.ofFloat("1", (float)src.latitude, (float)dest.latitude);
            if(src.latitude > dest.latitude)
                rotateMarker(180);
            else
                rotateMarker(0);
            lat = true;
        }
        else{
            if(src.longitude > dest.longitude)
                rotateMarker(-90);
            else
                rotateMarker(90);
        }

        va = ValueAnimator.ofPropertyValuesHolder(pvh1);
        va.setDuration(mDuration);
        final boolean finalLat = lat;
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if(!finalLat)
                    replaceMarker(new LatLng(src.latitude,(double)((float)animation.getAnimatedValue())));
                else
                    replaceMarker(new LatLng((double)((float)animation.getAnimatedValue()),src.longitude));
            }
        });        va.start();
    }//animatePlane


}
