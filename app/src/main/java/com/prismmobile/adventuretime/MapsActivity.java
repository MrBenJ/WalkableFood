package com.prismmobile.adventuretime;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity  implements GetNearbyPlacesTask.OnTaskCompleted {


    // Standard
    private final static String TAG = MapsActivity.class.getSimpleName();

    //Location Variables, to be used multiple times
    public static double lat;
    public static double lng;





    public static GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        Log.i(TAG, "Listening for Location!");

    }

    @Override
    protected void onPause() {
        super.onPause();

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



    private void setupInitialLocation() {

        //Get and set the user's location. Using cached location to not deal with listener updates
        //and change around sensors
        LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        lat = myLocation.getLatitude();
        lng = myLocation.getLongitude();
        LatLng currentLocation = new LatLng(lat,lng);

        Log.i(TAG, "LOCATION IS LAT: " + lat + " LNG: " +lng);

        AddSearchMarker();

        // Starts an initial search from the user's current location.
        GetNearbyPlacesTask initialSearch = new GetNearbyPlacesTask();
        initialSearch.asyncListener = MapsActivity.this; // for onComplete interface
        initialSearch.execute();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}
            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.i(TAG, "Marker Set!");

                // Gets the position of the Search Marker
                LatLng searchMarker = marker.getPosition();
                lat = searchMarker.latitude;
                lng = searchMarker.longitude;
                Log.i(TAG, "MARKER LOCATION: LAT: " + lat + "LNG: " + lng);

                GetNearbyPlacesTask getNearbyPlacesTask = new GetNearbyPlacesTask();
                getNearbyPlacesTask.asyncListener = MapsActivity.this; // For OnTaskComplete interface
                getNearbyPlacesTask.execute();

                // Puts a loading bar on the top during Async task, so User knows
                // that a search is being conducted
                setProgressBarIndeterminateVisibility(true);

            }
        });
    }

    public static void AddSearchMarker() {

        // Adds the search marker
        mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(new LatLng(lat, lng))
                .title("Search 1/2 mile radius from here")
                .snippet("Hold and Drag me to search"));
    }

    // Called when the GetNearbyPlacesTask is done, as it immediately clears all old markers,
    // including the search marker

    public void onTaskComplete(boolean connectionSuccessful) {

        if (!connectionSuccessful) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.No_internet))
                    .setTitle(getString(R.string.uh_oh))
                    .setPositiveButton(getString(R.string.OK), null)
                    .create()
                    .show();
        }
        // Get rid of the progress bar, regardless if a connection was made
        setProgressBarIndeterminateVisibility(false);

    }











}
