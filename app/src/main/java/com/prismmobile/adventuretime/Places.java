package com.prismmobile.adventuretime;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 * Custom object that holds a place
 * Created by benjunya on 9/24/14.
 */
public class Places {

    private String name;
    private String vicinity;
    private LatLng coordinates;

    /** SETTERS
     * AND GETTERS
     *
     *
     */


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVicinity() {
        return vicinity;
    }
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

}
