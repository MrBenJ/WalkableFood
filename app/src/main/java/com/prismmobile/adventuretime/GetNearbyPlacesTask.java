package com.prismmobile.adventuretime;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This AsyncTask gets the places whenever someone drops the marker
 *
 * Created by benjunya on 9/23/14.
 */
public class GetNearbyPlacesTask extends AsyncTask<Object, Void, JSONObject> {

    private final static String TAG = GetNearbyPlacesTask.class.getSimpleName();



    @Override
    protected JSONObject doInBackground(Object... arg0) {
        int responseCode = -1;
        JSONObject jsonResponse = null;

        try {
            URL placeSearch = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                    "location=" + MapsActivity.lat + "," + MapsActivity.lng +
                    "&radius=805" +
                    "&types=food" +
                    "&key=AIzaSyBj3t25nFQsarl-_ek6pNd8RlbZnHDVrr8");

            //ANDROID KEY: not working!?? AIzaSyC-qCn2RPUtMSt5v3horE0SOzsbyKzTmi8
            // WEB KEY: AIzaSyBj3t25nFQsarl-_ek6pNd8RlbZnHDVrr8

            HttpURLConnection connection = (HttpURLConnection) placeSearch.openConnection();
            connection.connect();

            responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream);
                JsonReader jsonReader = new JsonReader(reader);
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(reader);
                gson.fromJson(element, Places.class);

                if (element.isJsonObject()) {
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

                        double latPos = location.get("lat").getAsDouble();
                        double lngPos = location.get("lng").getAsDouble();





                        /*
                        MapsActivity.mMap.addMarker(new MarkerOptions()
                        .title(name)
                        .snippet(vicinity)
                        .position(new LatLng(latPos,lngPos))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        */

                        }

                    }


            //    Places myObj = gson.fromJson(reader, Places.class);

            //    Log.v(TAG, "RESPONSE DATA:" + responseData);
                Log.v(TAG, "URL: " + placeSearch.toString());
                /*
                jsonResponse = new JSONObject(responseData);
                String status = jsonResponse.getString("status");
                Log.v(TAG, status);

                JSONArray places = jsonResponse.getJSONArray("posts");
                */
                }


            else {
                Log.i(TAG, "Unsuccessful HTTP Response Code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            Log.i(TAG, "Exception caught:" + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "Exception caught:" + e.getMessage());
        } /*catch (JSONException e) {
            Log.i(TAG, "Exception caught:" + e.getMessage());
        } */

        return jsonResponse;


    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.i(TAG, "onPostExecute Running");
    }




    /** SETTERS AND GETTERS
     *
     */




}


