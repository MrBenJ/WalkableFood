package com.prismmobile.adventuretime;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
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
                    "&rankby=distance" +
                    "&types=food" +
                    "&key=AIzaSyC-qCn2RPUtMSt5v3horE0SOzsbyKzTmi8");

            HttpURLConnection connection = (HttpURLConnection) placeSearch.openConnection();
            connection.connect();

            responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream);
                int contentLength = connection.getContentLength();
                char[] charArray = new char[contentLength];
                reader.read(charArray);
                String responseData = new String(charArray);
                Log.v(TAG, responseData);
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
}
