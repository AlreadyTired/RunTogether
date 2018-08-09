package com.example.kimhyunwoo.runtogether;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.kimhyunwoo.runtogether.mainactivity.MainActivity;
import com.example.kimhyunwoo.runtogether.mainactivity.MainFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.Locale;

public class MapUtil {
    private final String TAG = "MapUtil TAG : ";
    private final String DEFAULT_LOCALE_STRING = "en_US";

    private Marker delete = null;

//    private LatLng startLat = null;
//    private LatLng endLat = null;
    private LatLng startLat = new LatLng(32.881033, -117.235601);;
    private LatLng endLat = new LatLng(32.8843552,-117.2338066);

    public  int zoomLevel = 18;
    private double distance = 0d;

    public void setStartLat(LatLng lat){
        this.startLat = lat;
    }

    public void setEndLat(LatLng lat){
        this.endLat = lat;
    }

    public LatLng getStartLat() {
        return startLat;
    }

    public LatLng getEndLat() {
        return endLat;
    }


    public void polylineOnMap(GoogleMap map, LatLng previousCoordinate, LatLng currentCoordinate){
        if(map == null || previousCoordinate == null || currentCoordinate == null)
        {
            Log.e(TAG, "null check!!!");
            return;
        }

        // 사용자가 운동한 경로를 선으로 연결함
        map.addPolyline(new PolylineOptions().color(0xffff0000).width(30.0f).
                geodesic(true).add(previousCoordinate).add(currentCoordinate));

    }

    public void drawMarker(){

    }

    public void deleteMarker(GoogleMap map, MarkerOptions markerOptions){

        if(delete != null)
        {
            delete.remove();
        }
        delete = map.addMarker(markerOptions);
    }

    public Configuration setLocaleResources() {
        String languageToLoad = DEFAULT_LOCALE_STRING;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        return config;
    }

    public double getDistance(){

        //  startLat, endLat이 null일 때 앱 튕김 방지
        if(this.startLat == null || this.endLat == null) {
            return distance;
        }

        distance = SphericalUtil.computeDistanceBetween(this.startLat, this.endLat);

        return distance;
    }
}
