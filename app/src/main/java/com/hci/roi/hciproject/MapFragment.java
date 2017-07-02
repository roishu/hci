package com.hci.roi.hciproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends AppCompatActivity implements OnMapReadyCallback  {
    private String TAG = "ROI_YONATAN";
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private static final LatLng SYDNEY = new LatLng(-34, 151);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng AFEKA = new LatLng(32.113510, 34.818186);
    //Recycle-View-Members
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private ArrayList<DataObject> myDataset;// = {"Mission"};
    private Marker firstMarker;
    private ArrayList<Marker> markerList;
    private BitmapDescriptor planeBitmapDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.main_activity_with_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setLandscapeOrientation();
        setRecycleView();
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

        planeBitmapDrawable = BitmapDescriptorFactory.fromResource(R.drawable.plane_icon2);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
    }

    public void setLandscapeOrientation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyAdapter) mAdapter).setOnItemClickListener(new MyAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(TAG, " Clicked on Item " + position);
                if(position==3){//4
                    mRecyclerView.setLayoutParams(new LinearLayout.
                            LayoutParams(mRecyclerView.getWidth()*2,mRecyclerView.getHeight()));
                }
                if(position==2){//3
                    Intent radarIntent = new Intent(MapFragment.this, RadarActivity.class);
                    startActivityForResult(radarIntent,101);
                }
                if(position==1){//2
                    Intent radarIntent = new Intent(MapFragment.this, ADIActivity.class);
                    startActivityForResult(radarIntent,101);
                }
            }
        });
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
        // Position the map's camera near Sydney, Australia.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));

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
        setUpMarkers(googleMap);
    }

    public void replaceDemoMarker(GoogleMap googleMap , LatLng latLng){
        if (firstMarker!=null) firstMarker.remove();
        firstMarker= googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("My Title")
                .snippet("My Snippet")
                .icon(planeBitmapDrawable));
    }

    private void setUpMarkers(final GoogleMap googleMap) {
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//
//            }
//        }, 5000);
    }




}
