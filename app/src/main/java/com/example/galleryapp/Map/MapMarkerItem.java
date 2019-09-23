package com.example.galleryapp.Map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapMarkerItem implements ClusterItem {
    private LatLng location;
    private String address;

    public MapMarkerItem(LatLng location, String address) {
        this.location = location;
        this.address = address;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat(){
        return location.latitude;
    }

    public double getLon(){
        return location.longitude;
    }
    @Override
    public LatLng getPosition() {
        return location;
    }
}
