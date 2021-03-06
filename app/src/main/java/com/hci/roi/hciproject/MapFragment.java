package com.hci.roi.hciproject;

import android.Manifest;
import android.animation.IntEvaluator;
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
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cc.roi.aircc.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hci.roi.hciproject.client.MissionTask;

import java.util.ArrayList;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;
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
        , GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationChangeListener  , CompoundButton.OnCheckedChangeListener{
    private String TAG = "ROI_YONATAN";
    //mission
    private ArrayList<Mission> planes;
    //Recycle-View-Members
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private ArrayList<DataObject> myDataset;// = {"Mission"};
    //menu buttons
    private FloatingActionButton fab1, fab4,fab5;
    //googleMapCopy
    private GoogleMap googleMapCopy;
    //context
    public static Context context;
    private SwitchCompat switchPlanes,switchRoutes,switchMode;
    private Snackbar mySnackbar;
    private boolean mapClickToAddRoute = false;
    private boolean missionCreated = false;
    private LocationManagerHelper myLocationManager;
    private int fabColor = Color.rgb(128, 203, 196);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_with_map);
        context = this;
        planes = new ArrayList<Mission>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setLandscapeOrientation();
        setRecycleView();
    }

    //initial map functions - must !
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
        /*
        3D if needed
        */
//
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(JERUSALEM) // Sets the center of the map to
//                .zoom(8)                   // Sets the zoom
//                //.bearing(location.getBearing()) // Sets the orientation of the camera to east
//                .tilt(30)    // Sets the tilt of the camera to 30 degrees
//                .build();    // Creates a CameraPosition from the builder
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
//                cameraPosition));
//      Position the map's camera near JERUSALEM.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(JERUSALEM));
        googleMapCopy = googleMap;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if (mapClickToAddRoute) {
                    planes.get(planes.size() - 1).drawSelfPlaneLine(latLng);
                }
                Log.e("LatLng", latLng.toString());
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                myLocationManager = new LocationManagerHelper(context);
                myLocationManager.doTest(latLng.latitude, latLng.longitude);
            }
        });

        // Create a LatLngBounds that includes the cities BEER_SHEVA and JERUSALEM.
        final LatLngBounds ADELAIDE = new LatLngBounds(BEER_SHEVA, JERUSALEM);

        googleMap.setLatLngBoundsForCameraTarget(ADELAIDE);


        googleMap.setOnMyLocationChangeListener(this);
        googleMap.setOnMarkerClickListener(this);

        initGUI();
        initMissions();
    }

    private void setLocationProperties() {//location properties
        askLocationPremission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMapCopy.setMyLocationEnabled(true);
        googleMapCopy.getUiSettings().setMyLocationButtonEnabled(false);
    }

    //override user premission in order to fake GPS location (simulations work with that)
    private void askLocationPremission() {
        int permissionCheck = ContextCompat.checkSelfPermission(MapFragment.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // ask permissions here using below code
            ActivityCompat.requestPermissions(MapFragment.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        }
    }

    //choose marker functionality on map
    @Override
    public boolean onMarkerClick(Marker marker) {
        switchRoutes.setChecked(false);
        for (int i = 0; i < planes.size(); i++) {
            if (marker.equals(planes.get(i).getPlaneMarker())) {
                if (planes.get(i).getChoose()) {
                    planes.get(i).chooseMarker(false);
                    planes.get(i).hideAllPlaneLines();
                } else {
                    planes.get(i).chooseMarker(true);
                    planes.get(i).showAllPlaneLines();
                }

            } else {
                planes.get(i).chooseMarker(false);
                planes.get(i).hideAllPlaneLines();
            }
        }
        return true;
    }

    //simulations
    private void initMissions() {
        planes.add(new Mission(this, googleMapCopy));
        planes.get(0).replaceMarker(HAIFA);
        planes.get(0).drawPlaneLine(HAIFA, HADERA);
        planes.get(0).drawPlaneLine(HADERA, TEL_AVIV);
        planes.get(0).drawPlaneLine(TEL_AVIV, JERUSALEM);
        planes.get(0).drawPlaneLine(JERUSALEM, BEER_SHEVA);
        //
        planes.add(new Mission(this, googleMapCopy));
        planes.get(1).replaceMarker(ARAD);
        planes.get(1).drawPlaneLine(ARAD, MAALE_ADUMIM);
        planes.get(1).drawPlaneLine(MAALE_ADUMIM, IBRID);
        planes.get(1).drawPlaneLine(IBRID, AMMAN);
        //
        planes.add(new Mission(this, googleMapCopy));
        planes.get(2).replaceMarker(TIBERIAS);
        planes.get(2).drawPlaneLine(TIBERIAS, HAIFA_SEA);
        planes.get(2).drawPlaneLine(HAIFA_SEA, RISHON_SEA);
        planes.get(2).drawPlaneLine(RISHON_SEA, GAZA_STRIP);
        planes.get(2).drawPlaneLine(GAZA_STRIP, MITZPE_RAMON);
        planes.get(2).drawPlaneLine(MITZPE_RAMON, ARAD);
        //
        planes.add(new Mission(this, googleMapCopy));
        planes.get(3).replaceMarker(AL_HADITAH);
        planes.get(3).drawPlaneLine(AL_HADITAH, AL_HADITAH);
        planes.get(3).drawPlaneLine(AL_HADITAH, UNKNOWN_SOUTH_EAST_OUT);
        planes.get(3).drawPlaneLine(UNKNOWN_SOUTH_EAST_OUT, NEGEV);
        planes.get(3).drawPlaneLine(NEGEV, BEER_SHEVA);

        hideAllPlaneLines();
    }

    //init buttons and switches
    private void initGUI() {
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab5 = (FloatingActionButton) findViewById(R.id.fab5);
        fab1.setOnClickListener(this);
        fab4.setOnClickListener(this);
        fab5.setOnClickListener(this);

        switchPlanes = (SwitchCompat) findViewById(R.id.switch1);
        switchRoutes = (SwitchCompat) findViewById(R.id.switch2);
        switchMode = (SwitchCompat) findViewById(R.id.switch3);

        switchPlanes.setOnCheckedChangeListener(this);
        switchRoutes.setOnCheckedChangeListener(this);
        switchMode.setOnCheckedChangeListener(this);
        switchPlanes.setChecked(true);
        switchRoutes.setChecked(false);
        switchMode.setChecked(true);


    }

    //set cards in recycle view
    private void setRecycleView() {
        DataObject do1 = new DataObject("1", "1");
        DataObject do2 = new DataObject("2", "2");
        DataObject do3 = new DataObject("3", "3");
        DataObject do4 = new DataObject("4", "4");
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

    //we must set orientation to landscape orientation in every activity in order to unable roatation.
    public void setLandscapeOrientation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    //hide all plane's lines
    private void hideAllPlaneLines() {
        for (int i = 0; i < planes.size(); i++)
            planes.get(i).hideAllPlaneLines();
        switchRoutes.setChecked(false);
    }

    //display all plane's lines
    private void showAllPlaneLines() {
        for (int i = 0; i < planes.size(); i++)
            planes.get(i).showAllPlaneLines();
    }

    //hide all plane
    private void hideAllPlanes() {
        for (int i = 0; i < planes.size(); i++)
            planes.get(i).getPlaneMarker().setVisible(false);
    }
    //hide all plane
    private void showAllPlanes() {
        for (int i = 0; i < planes.size(); i++)
            planes.get(i).getPlaneMarker().setVisible(true);
    }

    //setOnItemClick to the cards - on click open the card view in a new dialog
    @Override
    protected void onResume() {
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

    //send missions to the server
    public void sendToServer(String str) {
        new MissionTask(str).execute();
    }

    //create a new dialog which's equal to the card that's been clicked.
    private void openDialogForResult(int i) {
        CustomDialogFragment editNameDialog = new CustomDialogFragment(context, i);
        int width = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        ((ViewGroup) editNameDialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
                context, R.anim.out));
        editNameDialog.show();
        editNameDialog.getWindow().setLayout(width, height);
    }

    //switches functionality
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //Switch Listener
        switch (buttonView.getId()) {
            case R.id.switch1: //planes
                if(isChecked){
                    showAllPlanes();
                }
                else{
                    hideAllPlanes();
                }
                break;
            case R.id.switch2: //routes
                if(isChecked){
                    showAllPlaneLines();
                }
                else{
                    hideAllPlaneLines();
                }
                break;
            case R.id.switch3: //mode
                if(isChecked){
                    googleMapCopy.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style_json));
                }
                else{
                    googleMapCopy.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.day_style_json));
                }
                break;
        }
    }

    //button functionality
    @Override
    public void onClick(View v) {
        //fabOnClick
        switch (v.getId()) {
            case R.id.fab1: //play
                for (int i = 0; i < planes.size(); i++) {
                    if (planes.get(i).handler != null)
                        planes.get(i).handler = null;
                    planes.get(i).animateMyPlaneOnRoute();
                }

                break;
            case R.id.fab4: //create new mission
                if (mySnackbar != null && mySnackbar.isShown()) {
                    //finish creating mission-
                    mySnackbar.dismiss();
                    returnAllFabs();
                    switchPlanes.setChecked(false);
                    showAllPlanes();
                    switchRoutes.setChecked(false);
                    planes.get(planes.size() - 1).hideAllPlaneLines();
                    mapClickToAddRoute = false;
                } else if (!missionCreated) {
                    missionCreated = true;
                    showMissionConfirmDialog();
                    cancelAllFabs();
                } else if (missionCreated) {
                    showMissionConfirmDialog();
                    cancelAllFabs();
                }
                break;
            case R.id.fab5: //print log to server
                showServerConfirmDialog();
                break;
        }
    }

    //print log to console
    private void printMissionsToLog() {
        for (int i = 0; i < planes.size(); i++) {
            for (int j = 0; j < planes.get(i).getMissionLatLng().size(); j++) {
                LatLng mission = planes.get(i).getMissionLatLng().get(j);
                String str = "planeID:" + i + " Latitude" + mission.latitude + " Longitude" + mission.longitude;
                Log.d("MISSION", str);
                sendToServer(str);
            }
            Log.d("MISSION", "__________________________________________");
        }
        Toast.makeText(this,"Log is being sent to the server",Toast.LENGTH_LONG).show();
    }


    //confirm a new mission
    private void showMissionConfirmDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Mission Create")
                .setMessage("Are you sure you want to make a new mission?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (missionCreated) {
                            planes.get(planes.size() - 1).delete();
                            planes.remove(planes.size() - 1);
                        }
                        setLocationProperties();
                        hideAllPlaneLines();
                        switchRoutes.setChecked(false);
                        hideAllPlanes();
                        snackOfMissionCreation();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        returnAllFabs();
                    }

                })
                .show();
    }

    //confirm a send of logs to the server
    private void showServerConfirmDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Mission Log Export")
                .setMessage("Are you sure you want to export missions ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        printMissionsToLog();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    //snack instructions
    private void snackOfMissionCreation() {
        mySnackbar = Snackbar.make(findViewById(R.id.app_main_layout),
                "Click to add route, to finish click on + button.", Snackbar.LENGTH_INDEFINITE);
        mySnackbar.show();
    }

    //cancel all button click listeners
    private void cancelAllFabs() {
        fab1.setEnabled(false);
        fab1.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        fab5.setEnabled(false);
        fab5.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        switchPlanes.setEnabled(false);
        switchRoutes.setEnabled(false);
        switchMode.setEnabled(false);
    }

    //return all button listeners and colors.
    private void returnAllFabs() {
        fab1.setEnabled(true);
        fab1.setBackgroundTintList(ColorStateList.valueOf(fabColor));
        fab5.setEnabled(true);
        fab5.setBackgroundTintList(ColorStateList.valueOf(fabColor));
        switchPlanes.setEnabled(true);
        switchRoutes.setEnabled(true);
        switchMode.setEnabled(true);
    }

    //get current location from user GPS
    @Override
    public void onMyLocationChange(Location location) {
        planes.add(new Mission(this, googleMapCopy));
        planes.get(planes.size() - 1).replaceMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        planes.get(planes.size() - 1).setSelf();
        mapClickToAddRoute = true;
    }

    //toast the elevation from the http-task
    public static void printElevation(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}
