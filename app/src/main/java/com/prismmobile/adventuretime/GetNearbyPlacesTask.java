package com.prismmobile.adventuretime;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This AsyncTask gets the places whenever someone drops the marker.
 *
 * Created by benjunya on 9/23/14.
 */
public class GetNearbyPlacesTask extends AsyncTask<Object, Void, JSONObject> {

    private final static String TAG = GetNearbyPlacesTask.class.getSimpleName();
    private List<Places> places = new ArrayList<Places>();
    public OnTaskCompleted asyncListener = null;
    static boolean connectionSuccessful;


    /**
    This interface is implemented in the Maps activity in order to listen for when the Async task
     is finished.
     */
    public interface OnTaskCompleted {
        void onTaskComplete(boolean connectionSuccessful);
    }


    @Override
    protected JSONObject doInBackground(Object... arg0) {

        JSONObject jsonResponse = null; // For semantics

        try {
            URL placeSearch = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=" + MapsActivity.lat + "," + MapsActivity.lng +
                    "&radius=805" +
                    "&types=food" +
                    "&key=AIzaSyBj3t25nFQsarl-_ek6pNd8RlbZnHDVrr8");

            // Talk to Google Places - find places nearby
            HttpURLConnection connection = (HttpURLConnection) placeSearch.openConnection();
            connection.connect();


            int responseCode = connection.getResponseCode();

            // Check to make sure a connection is properly established. Otherwise,
            // report bad connection and show display error dialog
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(reader);
                gson.fromJson(element, Places.class);

                if (element.isJsonObject()) {
                    connectionSuccessful = true;
                    JsonObject placeResults = element.getAsJsonObject();
                    JsonArray results = placeResults.getAsJsonArray("results");
                    for (int i = 0; i < results.size(); i++) {
                        JsonObject place = results.get(i).getAsJsonObject();

                        // Root level JSON, get the name and address (vicinity)
                        String name = place.get("name").getAsString();
                        String vicinity = place.get("vicinity").getAsString();

                        // Go a few more levels down to get Lat/Lng
                        JsonElement geometryElement = place.get("geometry");
                        JsonObject geometry = geometryElement.getAsJsonObject();

                        JsonElement locationElement = geometry.get("location");
                        JsonObject location = locationElement.getAsJsonObject();

                        // For Readability and clarity, defining variables and inputting
                        // into mMap.
                        double latPos = location.get("lat").getAsDouble();
                        double lngPos = location.get("lng").getAsDouble();
                        LatLng coordinates = new LatLng(latPos,lngPos);

                        Places thisPlace = new Places();
                        thisPlace.setName(name);
                        thisPlace.setVicinity(vicinity);
                        thisPlace.setCoordinates(coordinates);

                        places.add(i, thisPlace);

                        }

                    }

                }

            else {
                Log.i(TAG, "Unsuccessful HTTP Response Code: " + responseCode);
                connectionSuccessful = false;
            }
        } catch (MalformedURLException e) {
            Log.i(TAG, "Exception caught:" + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "Exception caught:" + e.getMessage());
        }

    return jsonResponse;

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.i(TAG, "onPostExecute Running");

        if (connectionSuccessful) {
            MapsActivity.mMap.clear();


            for (int i = 0; i < places.size(); i++) {

                String name = places.get(i).getName();
                String vicinity = places.get(i).getVicinity();
                LatLng coordinates = places.get(i).getCoordinates();

                MapsActivity.mMap.addMarker(new MarkerOptions()
                        .title(name)
                        .snippet(vicinity)
                        .position(coordinates)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            }
            MapsActivity.AddSearchMarker();
            asyncListener.onTaskComplete(connectionSuccessful);
        }

        else {
            asyncListener.onTaskComplete(false);
        }






    }









}


