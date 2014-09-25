package com.prismmobile.adventuretime;

/**
 * Created by benjunya on 9/24/14.
 */
public class Places {

    private String name;
    private String vicinity;
    private double lat;
    private double lng;

    /** SETTERS
     * AND GETTERS
     *
     *
     */

    public double getLatitude() {
        return lat;
    }
    public void setLatitude(double lat) {
        this.lat = lat;
    }
    public double getLongitude() {
        return lng;
    }
    public void setLongitude(double lng) {
        this.lng = lng;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
