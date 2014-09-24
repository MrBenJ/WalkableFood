package com.prismmobile.adventuretime;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {


    // Debug Objects
    private final static String TAG = MapsActivity.class.getSimpleName();

    //Location Variables
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    public static double lat;
    public static double lng;
    private LatLng currentLocation = new LatLng(0,0);


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        Log.i(TAG, "Listening for Location!");
        setupInitialLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop listening for Location Updates to save battery
        Log.i(TAG, "Pausing Location Updates");
        mLocationManager.removeUpdates(mLocationListener);
    }



    /**
     * This method sets up the initial map, along with enabling the user to find their current location on the map.
     *
     * We do this check because we don't want to call getSupportFragmentManager if the map object is going to return
     * null.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                setupInitialLocation();




            }
        }
    }



    private void ListenForCurrentLocation() {


        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();

                Log.d(TAG, "LOCATION LAT: " + lat + " LNG: " + lng);
                currentLocation = new LatLng (lat,lng);
            }

            // Unrequired Methods
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}

        };

     //  mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);





    }

    private void setupInitialLocation() {

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        lat = myLocation.getLatitude();
        lng = myLocation.getLongitude();
        currentLocation = new LatLng(lat,lng);

        mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(currentLocation)
                .title("Search Here")
                .snippet("Drag me to search"));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //TODO: Search Google Places
                Log.i(TAG, "Marker has been set!!!");
                GetNearbyPlacesTask getNearbyPlacesTask = new GetNearbyPlacesTask();
                getNearbyPlacesTask.execute();


            }


        });

    }





}
