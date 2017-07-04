package com.hci.roi.hciproject;

import android.Manifest;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.cc.roi.aircc.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
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

import static com.hci.roi.hciproject.LatLngResource.AFEKA;
import static com.hci.roi.hciproject.LatLngResource.AL_HADITAH;
import static com.hci.roi.hciproject.LatLngResource.AMMAN;
import static com.hci.roi.hciproject.LatLngResource.ARAD;
import static com.hci.roi.hciproject.LatLngResource.BEER_SHEVA;
import static com.hci.roi.hciproject.LatLngResource.GAZA_STRIP;
import static com.hci.roi.hciproject.LatLngResource.HADERA;
import static com.hci.roi.hciproject.LatLngResource.HAIFA;
import static com.hci.roi.hciproject.LatLngResource.HAIFA_SEA;
import static com.hci.roi.hciproject.LatLngResource.HERTZELIA;
import static com.hci.roi.hciproject.LatLngResource.IBRID;
import static com.hci.roi.hciproject.LatLngResource.JERUSALEM;
import static com.hci.roi.hciproject.LatLngResource.KDUMIM;
import static com.hci.roi.hciproject.LatLngResource.KFAR_SABA;
import static com.hci.roi.hciproject.LatLngResource.KFAR_YONA;
import static com.hci.roi.hciproject.LatLngResource.MAALE_ADUMIM;
import static com.hci.roi.hciproject.LatLngResource.MITZPE_RAMON;
import static com.hci.roi.hciproject.LatLngResource.NEGEV;
import static com.hci.roi.hciproject.LatLngResource.RISHON_SEA;
import static com.hci.roi.hciproject.LatLngResource.SYDNEY;
import static com.hci.roi.hciproject.LatLngResource.TEL_AVIV;
import static com.hci.roi.hciproject.LatLngResource.TIBERIAS;
import static com.hci.roi.hciproject.LatLngResource.UNKNOWN_SOUTH_EAST_OUT;

public class MapFragment extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener
        , GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationChangeListener {
    private String TAG = "ROI_YONATAN";
    //mission
    private ArrayList<Mission> planes;
    //Recycle-View-Members
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private ArrayList<DataObject> myDataset;// = {"Mission"};
    //menu buttons
    private FloatingActionButton fab1, fab2, fab3, fab4;
    //googleMapCopy
    private GoogleMap googleMapCopy;
    //context
    private Context context;
    private FloatingActionButton fab5;
    private FloatingActionButton fab6;
    private FloatingActionButton fab7;
    private FloatingActionButton fab8;
    private FloatingActionButton fab9;
    private Snackbar mySnackbar;
    private Object mCircle;
    /*

    getApplication().setTheme(Theme.Holo)

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_with_map);
        context = this;
        //members-init
        planes = new ArrayList<Mission>();

        initFab();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setLandscapeOrientation();
        setRecycleView();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        try {
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
        googleMapCopy = googleMap;
//        firstMarker = new MarkerOptions().position(SYDNEY)
//                .title("My First Mission");
//        googleMap.addMarker(firstMarker);


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("LatLng", latLng.toString());
//                replaceDemoMarker(googleMap,latLng);
//                    replaceDemoMarker(googleMap, missionLatLng.get(missionLatLng.size()-1));
//                    drawPlaneLine(googleMap , missionLatLng.get(missionLatLng.size()-1) ,latLng);
                //animatePlane(googleMap , missionLatLng.get(missionLatLng.size()-1) ,latLng);
            }
        });

        // Create a LatLngBounds that includes the city of Adelaide in Australia.
        final LatLngBounds ADELAIDE = new LatLngBounds(BEER_SHEVA, JERUSALEM);

        googleMap.setLatLngBoundsForCameraTarget(ADELAIDE);


        setLocationProperties();
        googleMap.setOnMyLocationChangeListener(this);
        googleMap.setOnMarkerClickListener(this);



        initMissions();
    }

    private void setLocationProperties() {//location
        askLocationPremission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        googleMapCopy.setMyLocationEnabled(true);
        googleMapCopy.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void askLocationPremission() {
        int permissionCheck = ContextCompat.checkSelfPermission(MapFragment.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // ask permissions here using below code
            ActivityCompat.requestPermissions(MapFragment.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(int i=0 ; i<planes.size() ; i++){
            if(marker.equals(planes.get(i).getPlaneMarker())) {
                planes.get(i).chooseMarker(true);
                planes.get(i).showAllPlaneLines();
            }
            else {
                planes.get(i).chooseMarker(false);
                planes.get(i).hideAllPlaneLines();
            }
        }
        return true;
    }

    private void initMissions() {
        planes.add(new Mission(this,googleMapCopy));
        planes.get(0).replaceMarker(HAIFA);
        planes.get(0).drawPlaneLine(HAIFA ,HADERA);
        planes.get(0).drawPlaneLine(HADERA ,TEL_AVIV);
        planes.get(0).drawPlaneLine(TEL_AVIV ,JERUSALEM);
        planes.get(0).drawPlaneLine(JERUSALEM ,BEER_SHEVA);
        //
        planes.add(new Mission(this,googleMapCopy));
        planes.get(1).replaceMarker(HADERA);
        planes.get(1).drawPlaneLine(HADERA ,MAALE_ADUMIM);
        planes.get(1).drawPlaneLine(MAALE_ADUMIM ,IBRID);
        planes.get(1).drawPlaneLine(IBRID ,AMMAN);
        //
        planes.add(new Mission(this,googleMapCopy));
        planes.get(2).replaceMarker(TIBERIAS);
        planes.get(2).drawPlaneLine(TIBERIAS ,HAIFA_SEA);
        planes.get(2).drawPlaneLine(HAIFA_SEA ,RISHON_SEA);
        planes.get(2).drawPlaneLine(RISHON_SEA ,GAZA_STRIP);
        planes.get(2).drawPlaneLine(GAZA_STRIP ,MITZPE_RAMON);
        planes.get(2).drawPlaneLine(MITZPE_RAMON ,ARAD);
        //
        planes.add(new Mission(this,googleMapCopy));
        planes.get(3).replaceMarker(AL_HADITAH);
        planes.get(3).drawPlaneLine(AL_HADITAH ,AL_HADITAH);
        planes.get(3).drawPlaneLine(AL_HADITAH ,UNKNOWN_SOUTH_EAST_OUT);
        planes.get(3).drawPlaneLine(UNKNOWN_SOUTH_EAST_OUT ,NEGEV);
        planes.get(3).drawPlaneLine(NEGEV ,BEER_SHEVA);

        hideAllPlaneLines();
    }

    private void initFab() {
        fab1= (FloatingActionButton) findViewById(R.id.fab1);
        fab2= (FloatingActionButton) findViewById(R.id.fab2);
        fab3= (FloatingActionButton) findViewById(R.id.fab3);
        fab4= (FloatingActionButton) findViewById(R.id.fab4);
        fab5= (FloatingActionButton) findViewById(R.id.fab5);
        fab6= (FloatingActionButton) findViewById(R.id.fab6);
        fab7= (FloatingActionButton) findViewById(R.id.fab7);
        fab8= (FloatingActionButton) findViewById(R.id.fab8);
        fab9= (FloatingActionButton) findViewById(R.id.fab9);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
        fab5.setOnClickListener(this);
        fab6.setOnClickListener(this);
        fab7.setOnClickListener(this);
        fab8.setOnClickListener(this);
        fab9.setOnClickListener(this);


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
    }

    public void setLandscapeOrientation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    private void hideAllPlaneLines() {
        for(int i=0; i<planes.size() ; i++)
            planes.get(i).hideAllPlaneLines();
    }

    private void showAllPlaneLines() {
        for(int i=0; i<planes.size() ; i++)
            planes.get(i).showAllPlaneLines();
    }

    private void hideAllPlanes() {
        for(int i=0; i<planes.size() ; i++)
            planes.get(i).getPlaneMarker().setVisible(false);
    }

    private void showAllPlanes() {
        for(int i=0; i<planes.size() ; i++)
            planes.get(i).getPlaneMarker().setVisible(true);
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
                openDialogForResult(position);
            }
        });
    }

    private void openDialogForResult(int i) {
        CustomDialogFragment editNameDialog = new CustomDialogFragment(context,i);
        int width = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        ((ViewGroup)editNameDialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
                context,R.anim.out));
        editNameDialog.show();
        editNameDialog.getWindow().setLayout(width, height);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab1:
                for(int i=0 ; i<planes.size() ; i++)
                    planes.get(i).animateMyPlaneOnRoute();
                break;
            case R.id.fab2:
                showAllPlaneLines();
                break;
            case R.id.fab3:
                hideAllPlaneLines();
                break;
            case R.id.fab4:
                if(mySnackbar!=null && mySnackbar.isShown())
                    mySnackbar.dismiss();
                else{
                    showConfirmDialog();
                    cancelAllFabs();
                }
                break;
            case R.id.fab5:
                break;
            case R.id.fab6:
                break;
            case R.id.fab7:
                break;
            case R.id.fab8:
                break;
            case R.id.fab9:
                break;
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Mission Create")
                .setMessage("Are you sure you want to make a new mission?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideAllPlaneLines();
                        hideAllPlanes();
                        startMissionCreation();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void startMissionCreation() {
         mySnackbar = Snackbar.make(findViewById(R.id.app_main_layout),
                "Click to add route, to finish click on 4 button.", Snackbar.LENGTH_INDEFINITE);
        mySnackbar.show();
    }

    private void cancelAllFabs() {
        fab1.setEnabled(false);
        fab1.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        fab2.setEnabled(false);
        fab2.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        fab3.setEnabled(false);
        fab3.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        fab5.setEnabled(false);
        fab5.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        fab6.setEnabled(false);
        fab6.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        fab7.setEnabled(false);
        fab7.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        fab8.setEnabled(false);
        fab8.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        fab9.setEnabled(false);
        fab9.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
    }

    @Override
    public void onMyLocationChange(Location location) {

    }
}
