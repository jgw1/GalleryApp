package com.example.galleryapp.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;

import com.example.galleryapp.DB.DatabaseAccess;

import java.util.ArrayList;

public class Location {
    public static ArrayList<Double> GetCurrentLocation(Context context){
        ArrayList<Double> LatLng = new ArrayList<>();
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return null;
        }

        // 현재 위치 정보 저장 - 차후에 DB시 필요 / 여기에선 필요 없음
        android.location.Location location = locationManager.getLastKnownLocation(locationProvider); //현재 위치 정보
        double latitude = location.getLatitude(); //현재 위치 - 위도 값
        double longitude = location.getLongitude(); //현재 위치 - 경도 값
        Log.d("GWGW","Latitude : " + latitude);
        Log.d("GWGW","Longitude : " + longitude);
        LatLng.add(0,latitude);
        LatLng.add(1,longitude);
        return LatLng;
    }

}
