package com.rao.com.googlemapschargedup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rao.com.googlemapschargedup.directionHelpers.FetchURL;
import com.rao.com.googlemapschargedup.directionHelpers.TaskLoadedCallback;

public class MainMapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        TaskLoadedCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Polyline currentPolyline;

    private static final LatLng MAIN_LOCATION = new LatLng(51.440088, -0.944224);
    private static final LatLng LOCATION_ONE = new LatLng(51.433962, -0.957098);
    private static final LatLng LOCATION_TWO = new LatLng(51.449263, -0.947819);

    private Marker mMainLocation;
    private Marker mLocationOne;
    private Marker mLocationtwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");

        // Add some markers to the map, and add a data object to each marker.
        mMainLocation = mMap.addMarker(new MarkerOptions()
                .position(MAIN_LOCATION)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Main location"));
        mMainLocation.setTag(0);

        mLocationOne = mMap.addMarker(new MarkerOptions()
                .position(LOCATION_ONE)
                .title("Location one"));
        mLocationOne.setTag(0);

        mLocationtwo = mMap.addMarker(new MarkerOptions()
                .position(LOCATION_TWO)
                .title("Location two"));
        mLocationtwo.setTag(0);

        mMap.setOnMarkerClickListener(this);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
        new FetchURL(MainMapsActivity.this)
                .execute(getUrl(MAIN_LOCATION, marker.getPosition(), "driving"), "driving");
        return false;
    }
}
