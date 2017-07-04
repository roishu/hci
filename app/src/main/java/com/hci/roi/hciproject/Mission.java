package com.hci.roi.hciproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.cc.roi.aircc.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

/**
 * Created by Roi on 04/07/2017.
 */

public class Mission {
    private Marker planeMarker;
    private ArrayList<LatLng> missionLatLng;
    private ArrayList<Polyline> missionRoutes;
    private Context context;
    private Bitmap bitmap;
    private Bitmap mutableBitmap;
    private BitmapDescriptor planeBitmapDrawable;

    public Mission(Context appContext){
        missionLatLng = new ArrayList<LatLng>();
        missionRoutes = new ArrayList<Polyline>();
        context = appContext;
        planeBitmapDrawable = setBitmapWithDirection(0);
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

    public BitmapDescriptor setBitmapWithDirection(float angle)
    {
        Drawable d = context.getResources().getDrawable( R.drawable.plane_icon3);
        d.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        bitmap = ((BitmapDrawable) d).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 2*bitmap.getWidth()/3, 2*bitmap.getHeight()/3, false);
        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap = rotateBitmap(mutableBitmap,angle);
        Canvas myCanvas = new Canvas(mutableBitmap);
        int myColor = mutableBitmap.getPixel(0,0);
        ColorFilter filter = new LightingColorFilter(myColor, Color.GREEN);
        Paint pnt = new Paint();
        pnt.setColorFilter(filter);
        myCanvas.drawBitmap(mutableBitmap,0,0,pnt);
        return BitmapDescriptorFactory.fromBitmap(mutableBitmap);
    }

    public void replaceDemoMarker(GoogleMap googleMap , LatLng latLng){
        if (planeMarker!=null) planeMarker.remove();
        planeMarker= googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Title")
                .snippet("My Snippet")
                .anchor(0.5f, 0.5f)
                .icon(planeBitmapDrawable));
    }

}
