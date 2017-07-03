package com.hci.roi.hciproject;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.cc.roi.aircc.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapFragment extends AppCompatActivity implements OnMapReadyCallback ,View.OnClickListener {
    private String TAG = "ROI_YONATAN";
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private static final LatLng SYDNEY = new LatLng(-34, 151);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng AFEKA = new LatLng(32.113510, 34.818186);
    private static final LatLng HAIFA = new LatLng(32.79862664998465,34.98749814927578);
    private static final LatLng HADERA = new LatLng(32.458764475370785,34.95043504983187);
    private static final LatLng BEER_SHEVA = new LatLng(31.252973, 34.791462);
    private static final LatLng JERUSALEM = new LatLng(31.77647118609461,35.215819515287876);
    private static final LatLng TEL_AVIV = new LatLng(32.09132560608773,34.785292111337185);
    //mission
    private ArrayList<LatLng> missionLatLng;
    //Recycle-View-Members
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private ArrayList<DataObject> myDataset;// = {"Mission"};
    private Marker firstMarker;
    private ArrayList<Marker> markerList;
    private BitmapDescriptor planeBitmapDrawable;
    //menu buttons
    private FloatingActionButton fab1,fab2,fab3,fab4;
    private Bitmap b;
    private Bitmap mutableBitmap;
    //googleMapCopy
    private GoogleMap googleMapCopy;
    /*

    getApplication().setTheme(Theme.Holo)

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_with_map);
        //members-init
        missionLatLng = new ArrayList<LatLng>();
        initFab();
        //
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setLandscapeOrientation();
        setRecycleView();
    }

    private void initFab() {
        fab1= (FloatingActionButton) findViewById(R.id.fab1);
        fab2= (FloatingActionButton) findViewById(R.id.fab2);
        fab3= (FloatingActionButton) findViewById(R.id.fab3);
        fab4= (FloatingActionButton) findViewById(R.id.fab4);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
    }

    private void setRecycleView() {
        DataObject do1 = new DataObject("1" , "1");
        DataObject do2 = new DataObject("2" , "2");
        DataObject do3 = new DataObject("3" , "3");
        DataObject do4 = new DataObject("4" , "4");
        myDataset = new ArrayList<DataObject>();
        myDataset.add(do1);
        myDataset.add(do2);
        myDataset.add(do3);
        myDataset.add(do4);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);



        setBitmapWithDirection(0);
        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
    }

    public void setLandscapeOrientation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }



    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject("Some Primary Text " + index,
                    "Secondary " + index);
            results.add(index, obj);
        }
        return results;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        // Position the map's camera near JERUSALEM.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(JERUSALEM));

//        firstMarker = new MarkerOptions().position(SYDNEY)
//                .title("My First Mission");
//        googleMap.addMarker(firstMarker);

        firstMarker= googleMap.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .title("My Title")
                .snippet("My Snippet")
                .icon(planeBitmapDrawable));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("LatLng" , latLng.toString());
                replaceDemoMarker(googleMap,latLng);
            }
        });

        // Create a LatLngBounds that includes the city of Adelaide in Australia.
        final LatLngBounds ADELAIDE = new LatLngBounds(BEER_SHEVA,JERUSALEM);

        //googleMap.addMarker(new MarkerOptions().position(HAIFA));
        //googleMap.addMarker(new MarkerOptions().position(EAST_HAIFA));

        drawPlaneLine(googleMap , HAIFA ,HADERA);
        drawPlaneLine(googleMap , HADERA ,TEL_AVIV);
        drawPlaneLine(googleMap , TEL_AVIV ,JERUSALEM);
        drawPlaneLine(googleMap , JERUSALEM ,BEER_SHEVA);

        addMarkersByMission(googleMap);

        // Constrain the camera target to the Adelaide bounds.
        googleMap.setLatLngBoundsForCameraTarget(ADELAIDE);
        //googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMapCopy = googleMap;

    }

    private void drawPlaneLine(GoogleMap googleMap,LatLng src, LatLng dest) {
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

    }

    private void addMarkersByMission(GoogleMap googleMap) {
       for(int i=0 ; i<missionLatLng.size() ; i++)
           Log.e("PLANE", ""+ missionLatLng.get(i).longitude + "," + missionLatLng.get(i).latitude);
       // googleMap.addMarker(new MarkerOptions().position(missionLatLng.get(i)));
        animatePlane(googleMap,missionLatLng.get(3),missionLatLng.get(4));
    }

    public void replaceDemoMarker(GoogleMap googleMap , LatLng latLng){
        if (firstMarker!=null) firstMarker.remove();
        firstMarker= googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Title")
                .snippet("My Snippet")
                .anchor(0.5f, 0.5f)
                .icon(planeBitmapDrawable));
    }

    public void animatePlane(final GoogleMap googleMap, final LatLng src, LatLng dest){

        boolean lat = false;
        ValueAnimator va;
        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("1", (float)src.longitude, (float)dest.longitude);

        if(src.latitude != dest.latitude){
            pvh1 = PropertyValuesHolder.ofFloat("1", (float)src.latitude, (float)dest.latitude);
            if(src.latitude > dest.latitude)
                setBitmapWithDirection(180);
            else
                setBitmapWithDirection(0);
            lat = true;
        }
        else{
            if(src.longitude > dest.longitude)
                setBitmapWithDirection(-90);
            else
                setBitmapWithDirection(90);
        }

        //Log.e("PLANE", "SRC"+ src.longitude + "," + src.latitude);
        //Log.e("PLANE", "DEST"+ dest.longitude + "," + dest.latitude);
        va = ValueAnimator.ofPropertyValuesHolder(pvh1);
        va.setDuration(6000);
        final boolean finalLat = lat;
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if(!finalLat)
                replaceDemoMarker(googleMap, new LatLng(src.latitude,(double)((float)animation.getAnimatedValue())));
                else
                    replaceDemoMarker(googleMap, new LatLng((double)((float)animation.getAnimatedValue()),src.longitude));
                //Log.e("PLANE", (double)((float)animation.getAnimatedValue())+"");
            }
        });        va.start();



//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setAnimationProperties(pvh3);
//                va.start();
//            }
//        }, 3000);
    }

    public Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        //matrix.postRotate(angle);
        //return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        matrix.setTranslate(source.getWidth()/2, source.getHeight()/2);
        matrix.preRotate(angle,source.getWidth()/2, source.getHeight()/2);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);


    }

    public void setBitmapWithDirection(float angle)
    {
        Drawable d = getResources().getDrawable( R.drawable.plane_icon3);
        d.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

        b = ((BitmapDrawable) d).getBitmap();
        mutableBitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap = rotateBitmap(mutableBitmap,angle);
        Canvas myCanvas = new Canvas(mutableBitmap);

        int myColor = mutableBitmap.getPixel(0,0);
        ColorFilter filter = new LightingColorFilter(myColor, Color.GREEN);

        Paint pnt = new Paint();
        pnt.setColorFilter(filter);
        myCanvas.drawBitmap(mutableBitmap,0,0,pnt);

        planeBitmapDrawable = BitmapDescriptorFactory.fromBitmap(mutableBitmap);
    }

    @Override
    protected void onResume() {
        //mRecyclerView.setLayoutParams(new LinearLayout.
        //LayoutParams(mRecyclerView.getWidth()*2,mRecyclerView.getHeight()));
        super.onResume();
        ((MyAdapter) mAdapter).setOnItemClickListener(new MyAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(TAG, " Clicked on Item " + position);
        switch (position){
            case 0:
                Intent HSIIntent = new Intent(MapFragment.this, HSIActivity.class);
                startActivityForResult(HSIIntent,101);
                break;
            case 1:
                Intent ADIIntent = new Intent(MapFragment.this, ADIActivity.class);
                startActivityForResult(ADIIntent,101);
                break;
            case 2:
                Intent radarIntent = new Intent(MapFragment.this, RadarActivity.class);
                startActivityForResult(radarIntent,101);
                break;
            case 3:
//                Intent radarIntent = new Intent(MapFragment.this, ADIActivity.class);
//                startActivityForResult(radarIntent,101);
                mRecyclerView.setLayoutParams(new LinearLayout.
                LayoutParams(mRecyclerView.getWidth()*2,mRecyclerView.getHeight()));
                break;
        }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab1:
                animatePlane(googleMapCopy,missionLatLng.get(2),missionLatLng.get(3));
                break;
            case R.id.fab2:
                animatePlane(googleMapCopy,missionLatLng.get(3),missionLatLng.get(4));
                break;
            case R.id.fab3:
                animatePlane(googleMapCopy,missionLatLng.get(4),missionLatLng.get(5));
                break;
            case R.id.fab4:
                animatePlane(googleMapCopy,missionLatLng.get(5),missionLatLng.get(6));
                break;
        }
    }
}
