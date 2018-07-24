package com.example.kimhyunwoo.runtogether;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback,
        View.OnClickListener,
        GoogleMap.OnMarkerClickListener {

    LocationManager manager;

    GoogleMap map = null;
    MarkerOptions markerOptions = new MarkerOptions();

    LatLng savedCoordinate = new LatLng(32.881033, -117.235601);
    LatLng startLat = new LatLng(32.881033, -117.235601);
    LatLng endLat = new LatLng(32.881033, -117.235601);

    Button buttonStart;
    Button buttonEnd;
    Button buttonCalc;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        // 프래그먼트 안에서 프래그먼트를 가져올 때 사용
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_google);
        // 맵이 사용할 준비가 되면 onMapReady 함수를 자동으로 호출

        // 버튼을 만들기 위해서 생성
        buttonStart = (Button)view.findViewById(R.id.btn_start);
        buttonEnd = (Button)view.findViewById(R.id.btn_end);
        buttonCalc = (Button)view.findViewById(R.id.btn_calc);

        // 리스너에 버튼을 등록함
        buttonStart.setOnClickListener(this);
        buttonEnd.setOnClickListener(this);
        buttonCalc.setOnClickListener(this);

        //  구글맵 쓰레드 시작ㅈ
        mapFragment.getMapAsync(this);

        //  프래그먼트가 호출된 상위 액티비티를 가져올수있음
        //  MainActivity를 호출한 액티비티를 가져옴
        //  getActivity는 MainActivity를 가지고있는 액티비티
        //  상위 액티비티의 자원을 사용하기 위해서 Activity를 가져옴
        MainActivity activity = (MainActivity) getActivity();
        manager = activity.getLocationManager();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonStart) {
            startLat = savedCoordinate;

            Context context = getActivity().getApplicationContext();
            Toast toast = Toast.makeText(context,"startLat : "+startLat.latitude + "\nstartLng : " + startLat.longitude, Toast.LENGTH_SHORT);
            toast.show();
        } else if (v == buttonEnd) {
            endLat = savedCoordinate;

            Context context = getActivity().getApplicationContext();
            Toast toast = Toast.makeText(context,"endLat : "+ endLat.latitude+ "\nendLng : " + endLat.longitude, Toast.LENGTH_SHORT);
            toast.show();

        } else if (v == buttonCalc) {
            //Calculating the distance in meters
            Double distance = SphericalUtil.computeDistanceBetween(startLat, endLat);

            Context context = getActivity().getApplicationContext();
            //Displaying the distance
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context,"distance " + distance, duration);
            toast.show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //  좌표 적용
        //  마커생성
        markerOptions.position(savedCoordinate); //좌표
        markerOptions.title("임시 마커");
        //  마커를 화면에 그림
        map.addMarker(markerOptions);
        //  맵의 중심을 해당 좌표로 이동
        //  savedCoordinate : 좌표
        //  v: 줌레벨
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(savedCoordinate,16));

        map.setOnMarkerClickListener(this);
    }

    // 현재 프래그먼트가 러닝직전
    // 생명주기를 생각하면 onResume
    @Override
    public void onResume() {
        super.onResume();

        //마시멜로 이상버전에서는 런타임 권한 체크여부를 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // GPS 사용을 위한 권한 휙득이 되어 있지 않으면 리스너 해제
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        // GPS 리스너 등록
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, //위치제공자
                3000, //변경사항 체크 주기 millisecond 단위임
                1, //변경사항 체크 거리 meter단위
                locationListener //locationListener 함수를 호출 함
        );

    }

    //GPS 사용을 위해서 좌표 리스너를 생성
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //경도
            double currentLng = location.getLongitude();
            //위도
            double currentLat = location.getLatitude();
            // 현재는 사용하지 않음 아마도 사용 안할듯?
//            //고도
//            double alt = location.getAltitude();
//            //정확도
//            float acc = location.getAccuracy();
//            //위치제공자(ISP)
//            String provider = location.getProvider();


            //  맵의 중심을 해당 좌표로 이동
            //  savedCoordinate : 좌표
            //  v: 줌레벨
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(savedCoordinate,16));

            Context context = getActivity().getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context,"lat : "+currentLat + "\nlng : "
                    + currentLng, duration);
            toast.show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 위치 공급자의 상태가 바뀔 때 호출
        }

        @Override
        public void onProviderEnabled(String provider) {
            // 위치 공급자가 사용 가능해질(enabled) 때 호출
        }

        @Override
        public void onProviderDisabled(String provider) {
            //  위치 공급자가 사용 불가능해질(disabled) 때 호출
        }
    };

}
