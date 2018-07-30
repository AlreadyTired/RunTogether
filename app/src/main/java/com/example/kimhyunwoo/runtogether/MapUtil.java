package com.example.kimhyunwoo.runtogether;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapUtil {
    private String TAG = "MapUtil TAG : ";

    public  int zoomLevel = 16;

    Marker delete = null;

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
}
