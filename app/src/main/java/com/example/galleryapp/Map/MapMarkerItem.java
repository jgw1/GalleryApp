package com.example.galleryapp.Map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapMarkerItem implements ClusterItem {
    private LatLng location;


    public MapMarkerItem(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
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
