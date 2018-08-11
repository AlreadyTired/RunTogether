package com.example.kimhyunwoo.runtogether.mainactivity;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.AqiDataTansfer;
import com.example.kimhyunwoo.runtogether.BluetoothSingleton;
import com.example.kimhyunwoo.runtogether.BluetoothUtil;
import com.example.kimhyunwoo.runtogether.MapUtil;
import com.example.kimhyunwoo.runtogether.R;
import com.example.kimhyunwoo.runtogether.RealTimeDataTransfer;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.BluetoothChatService;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.Constants;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.DeviceListActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.FindPasswordActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.FindPasswordRequest;
import com.example.kimhyunwoo.runtogether.usermanagement.LoginActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback,
        View.OnClickListener,
        GoogleMap.OnMarkerClickListener {

    //===================================================================
    //  구글 맵 변수

    MapUtil mapUtil = null;

    LocationManager manager;

    GoogleMap map = null;
    MarkerOptions markerOptions = new MarkerOptions();

    //  TODO(db 연동 시에 db에서 마지막 위치를 받아 오도록 하자)
    //  현재는 db에 연동이 안되어 임의로 설정함
    LatLng savedCoordinate = null;
    LatLng currentCoordinate = null;

    Button buttonStart = null;
    Button buttonEnd = null;
    Button buttonReset = null;

    private AlertDialog dialog = null;

    private static final int markerRequstCode = 1000;

    double currentLng = 0d;
    double currentLat = 0d;
    //===================================================================
    boolean exercisingFlag = false;
    //  속도 계산
    double speed = 0d;
    double sumDistance = 0d;

    Handler timeHandle = null;
    double timer = 0d;

    double dist = 0d;

    //===================================================================
    //  메인 차트
    TextView textCO;
    TextView textSO2;
    TextView textNO2;
    TextView textO3;
    TextView textPM25;
    TextView textTEMP;
    TextView textHR;

    private SmileRating srAQI;
    private SmileRating srCO;
    private SmileRating srSO2;
    private SmileRating srNO2;
    private SmileRating srO3;
    private SmileRating srPM25;

    TextView textCOAQI;
    TextView textSO2AQI;
    TextView textNO2AQI;
    TextView textO3AQI;
    TextView textPM25AQI;
    TextView textTotal;

    //===================================================================
    //  블루투스 변수
    BluetoothUtil btUtil;
    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 3;

    private String mConnectedDeviceName = null;

    private ArrayAdapter<String> mConversationArrayAdapter;

    private StringBuffer mOutStringBuffer;

    BluetoothSingleton btSingletion;

    //===================================================================
    //  데이터 전송
    AqiDataTansfer aqiDataTansfer;
    RealTimeDataTransfer realTimeDataTransfer;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //  Set google map util
        mapUtil = new MapUtil();

        btSingletion = BluetoothSingleton.getInstance();
        btUtil = new BluetoothUtil();

        aqiDataTansfer = new AqiDataTansfer();
        realTimeDataTransfer = new RealTimeDataTransfer();
        // Get local Bluetooth adapter
        btSingletion.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (btSingletion.mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();

        }
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
        buttonReset = (Button)view.findViewById(R.id.btn_reset);

        // 리스너에 버튼을 등록함
        buttonStart.setOnClickListener(this);
        buttonEnd.setOnClickListener(this);
        buttonReset.setOnClickListener(this);

        //  구글맵 쓰레드 시작ㅈ
        mapFragment.getMapAsync(this);

        //  프래그먼트가 호출된 상위 액티비티를 가져올수있음
        //  MainActivity를 호출한 액티비티를 가져옴
        //  getActivity는 MainActivity를 가지고있는 액티비티
        //  상위 액티비티의 자원을 사용하기 위해서 Activity를 가져옴
        MainActivity activity = (MainActivity) getActivity();
        manager = activity.getLocationManager();

        textCO = view.findViewById(R.id.txt_co);
        textSO2 = view.findViewById(R.id.txt_so2);
        textNO2 = view.findViewById(R.id.txt_no2);
        textO3 = view.findViewById(R.id.txt_o3);
        textPM25 = view.findViewById(R.id.txt_pm25);
        textTEMP = view.findViewById(R.id.txt_temp);
        textHR = view.findViewById(R.id.txt_hr);

        textCOAQI = view.findViewById(R.id.txt_coAQI);
        textSO2AQI = view.findViewById(R.id.txt_so2AQI);
        textNO2AQI = view.findViewById(R.id.txt_no2AQI);
        textO3AQI = view.findViewById(R.id.txt_o3AQI);
        textPM25AQI = view.findViewById(R.id.txt_pm252AQI);
        textTotal = view.findViewById(R.id.txt_total);
        RealTimeDataTransfer.setTextView(textHR);

        MainAllDataChart(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!btSingletion.mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (btSingletion.mChatService == null) {
            setupChat();
        }
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
                1000, //변경사항 체크 주기 millisecond 단위임
                1, //변경사항 체크 거리 meter단위
                locationListener //locationListener 함수를 호출 함
        );

        //==========================================================================
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (btSingletion.mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (btSingletion.mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                btSingletion.mChatService.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (btSingletion.mChatService != null) {
            btSingletion.mChatService.stop();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonStart) {
            String toastText = "";

            do {
                if(currentCoordinate == null){
                    toastText = "Please wait until you get your current location.";
                    break;
                }
                //  sendResult가 true면 서버로 보낼 운동 데이터가 남은 상태로 간주
                if (exercisingFlag != true) {
                    mapUtil.setStart(currentCoordinate);

                    exercisingFlag = true;

                    timeHandle = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            timeHandle.sendEmptyMessageDelayed(0, 1000);
                            timer++;

                            if (savedCoordinate != null && currentCoordinate != null) {
                                Log.v("[INFO] ", "distance : " + sumDistance);
                                Log.v("[INFO] ", "timer : " + sumDistance);
                                Log.v("[INFO] ", "lang: " + savedCoordinate.latitude + " before long : " + savedCoordinate.longitude);

                            }

                            //  현재 좌표에 마커를 찍기 위해서 옵션에 저장
                            markerOptions.position(currentCoordinate);

                            if (savedCoordinate != null) {
                                mapUtil.polylineOnMap(map, savedCoordinate, currentCoordinate);
                            }

                            savedCoordinate = currentCoordinate;

                            //  마커 삭제
                            mapUtil.deleteMarker(map, markerOptions);

                            //  카메라 움직임
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinate, mapUtil.zoomLevel));

//                            Context context = getActivity().getApplicationContext();
//                            Toast toast = Toast.makeText(context,"now time :"+ timer, Toast.LENGTH_SHORT);
//                            toast.show();

//                        super.handleMessage(msg);
                        }
                    };

                    timeHandle.sendEmptyMessage(0);

                    toastText = "exercise start!!";

                    break;
                }

                    toastText = "already start!!";
            }while(false);

            Context context = getActivity().getApplicationContext();
            Toast toast = Toast.makeText(context,toastText, Toast.LENGTH_SHORT);
            toast.show();

        }else if (v == buttonEnd){
            String toastText = "";

            do {
                if(currentCoordinate == null){
                    toastText = "Please wait until you get your current location.";
                    break;
                }

                if (exercisingFlag != false) {
                    exercisingFlag = false;
                    mapUtil.setEnd(currentCoordinate);

                    //  속도 = 총 시간 / 총 거리
                    //  소수 2자리 계산
                    if (timer != 0) {
                        speed = sumDistance / timer;
                    }
                    timeHandle.removeMessages(0);

                    Context context = getActivity().getApplicationContext();
                    Toast toast = Toast.makeText(context, "speed :" + speed + " m/s", Toast.LENGTH_SHORT);
                    toast.show();

                    toastText = "exercise end!!";
                    break;
                }
                    toastText = "press start button!!";

            }while(false);

            Context context = getActivity().getApplicationContext();
            Toast toast = Toast.makeText(context,toastText, Toast.LENGTH_SHORT);
            toast.show();

        }else if (v == buttonReset) {
            Context context = getActivity().getApplicationContext();
            String toastText;

            do {
                //  운동이 끝났고, 전송에 성공 했으면 전송 준비 상태
                if (exercisingFlag != true && mapUtil.getSendResult() == mapUtil.SEND_SUCCESS) {

                    if(mapUtil.getStartTime() != null && mapUtil.getEndTime() != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());      // 로그인 실패로 알림을 띄움
                        dialog = builder.setMessage("Successfully save your data!! :)" +
                                "\n\nStart Time\n" +
                                "* " + mapUtil.getStartTime() +
                                "\nEnd Time\n" +
                                "* " + mapUtil.getEndTime() +
                                "\n\nSpeed\n" +
                                "* " + speed + " m/s" +
                                "\nDistance\n" +
                                "* " + dist + " M")
                                .setNegativeButton("Ok", null)
                                .create();
                        dialog.show();
                    }
                    
                    map.clear();
//                mapUtil.setSendResult(mapUtil.SEND_READY);
                    mapUtil.setReset();
                    sumDistance = 0d;
                    timer = 0d;
                    toastText = "Reset Google Map";

                    break;
                }

                //  운동이 끝났고, 전송에 실패 했으면 재전송
                if (exercisingFlag != true && mapUtil.getSendResult() == mapUtil.SEND_FAILED) {
//                mapUtil.Request(getContext());
                    toastText = "Retransmisstion!!";

                    break;
                }

                //  운동이 끝났고, 전송 대기 중이면 전송
                if (exercisingFlag != true && mapUtil.getSendResult() == mapUtil.SEND_READY) {
//                mapUtil.Request(getContext());
                    toastText = "Send exercise data";

                    break;
                }

                //  운동 중이면 전송하면 안됨
                toastText = "Press stop button!!";

            }while(false);
            Toast toast = Toast.makeText(context,toastText, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // 다이얼이 끝나고 여기로 결과가 전송됨
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case markerRequstCode:
                // ok를 눌렀을 경우
                if (resultCode == Activity.RESULT_OK) {
                    // intent 에서 id 키를 받아서 여기로 가져옴
                    String id = data.getExtras().getString("id");
                    Toast.makeText(getActivity(), "Sending Friend request!\n to "+id ,Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //  현재는 사용 안함.
        //  사용하면 다시 활성화 하자.
        //  좌표 적용
        //  마커생성
//        markerOptions.position(savedCoordinate); //좌표
//        markerOptions.title("임시 마커");
//        //  마커를 화면에 그림
//        map.addMarker(markerOptions);
//        //  맵의 중심을 해당 좌표로 이동
//        //  savedCoordinate : 좌표
//        //  v: 줌레벨
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(savedCoordinate,mapUtil.zoomLevel));
        map.setOnMarkerClickListener(this);
    }

    //GPS 사용을 위해서 좌표 리스너를 생성
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //경도
            currentLng = location.getLongitude();
            //위도
            currentLat = location.getLatitude();

            RealTimeDataTransfer.setGPS(Double.toString(currentLat),Double.toString(currentLng));
            //  바뀐 현재 좌표
            currentCoordinate = new LatLng(currentLat ,currentLng);
            //  거리 계산 후 0으로 초기화

            if(exercisingFlag == true) {
                dist = mapUtil.getDistance(savedCoordinate, currentCoordinate);
                sumDistance += dist;
                dist = 0d;

                return;
            }

            savedCoordinate = currentCoordinate;

            //  현재 좌표에 마커를 찍기 위해서 옵션에 저장
            markerOptions.position(currentCoordinate);


            //  마커 삭제
            mapUtil.deleteMarker(map, markerOptions);
            //  카메라 움직임
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinate,mapUtil.zoomLevel));

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

    @Override
    public boolean onMarkerClick(Marker marker) {
        //dialog fragment class 생성
        MarkerClickFragment newFragment = new MarkerClickFragment();
        // onActivityResult에서 1234 라는 요청 코드를 받아서 처리할 수 있도록 설정
        newFragment.setTargetFragment(this, markerRequstCode );
        //"dialog"라는 태그를 갖는 프래그먼트 생성
        newFragment.show(getFragmentManager(), "dialog");

        return true;
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        // Initialize the BluetoothChatService to perform bluetooth connections
        btSingletion.mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            try {
                                setStatus(getString(R.string.title_connecting) + mConnectedDeviceName);
                            }catch (IllegalStateException e){
                                Log.e("[ERR]Handler", e.getMessage().toString());
                            }
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            if(btSingletion.isPolarSensor){
                                Log.w("[INFO]","Polar Sensor :"+ btSingletion.getDeviceName()+ ", " + btSingletion.getDeviceAdress());
                                setStatus("Polar Sensor device info");
                                DeviceRegistryRequest();
                                btSingletion.isPolarSensor = false;
                            }else{
                                setStatus(R.string.title_connecting);
                            }

                            break;//여기
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_READ: //메세지 여기로 받음
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if(readMessage != null)
                    {
                        String parsingResult = null;
                        try {
                            parsingResult = btUtil.SortType(readMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String[] parsing = parsingResult.split(",");

                        if(parsingResult != null) {
                            if(btUtil.getType()){
                                //AQI
                                setSmileChart(srCO, Float.parseFloat(parsing[0]));
                                textCOAQI.setText(parsing[0]);
                                setSmileChart(srSO2, Float.parseFloat(parsing[1]));
                                textSO2AQI.setText(parsing[1]);
                                setSmileChart(srNO2, Float.parseFloat(parsing[2]));
                                textNO2AQI.setText(parsing[2]);
                                setSmileChart(srO3, Float.parseFloat(parsing[3]));
                                textO3AQI.setText(parsing[3]);
                                setSmileChart(srPM25, Float.parseFloat(parsing[4]));
                                textPM25AQI.setText(parsing[4]);
                                setSmileChart(srAQI, Float.parseFloat(parsing[5]));
                                textTotal.setText(parsing[5]);
                                aqiDataTansfer.Request(getContext());
                            }else{
                                //Real-Time
                                textCO.setText(parsing[0]);
                                textSO2.setText(parsing[1]);
                                textNO2.setText(parsing[2]);
                                textO3.setText(parsing[3]);
                                textPM25.setText(parsing[4]);
                                textTEMP.setText(parsing[5]);
                                RealTimeDataTransfer.ShowData();
                                realTimeDataTransfer.Request(getContext());
                            }
                        }

                    }
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        DeviceRegistryRequest();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    private void DeviceRegistryRequest(){

        // 센서 서버에 등록하는 리퀘스트,리스폰스.
        Response.Listener<String> reponseListener = new Response.Listener<String>() {

            // Volley 를 통해서 정상적으로 웹서버와 통신이 되면 실행되는 함수
            @Override
            public void onResponse(String response)
            {
                try
                {
                    // JSON 형식으로 값을 response 에 받아서 넘어온다.
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    if(message.equals("ok"))
                    {
                        Toast.makeText(getContext(), "Device registration complete!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        dialog = builder.setMessage(message)
                                .setNegativeButton("Try Again Bluetooth Connect!",null)
                                .create();
                        dialog.show();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        //  맥, 이름 날라옴(btSingletion.getDeviceAdress(),btSingletion.getDeviceName())
        SensorRegistrationRequest SensorRegistRequest = new SensorRegistrationRequest( btSingletion.getDeviceAdress(),btSingletion.getDeviceName(),reponseListener,getContext());           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
        RequestQueue queue = Volley.newRequestQueue(getContext());            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
        queue.add(SensorRegistRequest);
    }

    public void MainAllDataChart(View v){

        //smile
        srAQI= v.findViewById(R.id.rv_total);
        srAQI.setSelectedSmile(srAQI.OKAY);
        srAQI.setIndicator(true);
        srAQI.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

        srCO = v.findViewById(R.id.rv_co);
        srCO.setSelectedSmile(srCO.OKAY);
        srCO.setIndicator(true);
        srCO.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

        srSO2 = v.findViewById(R.id.rv_so2);
        srSO2.setSelectedSmile(srSO2.OKAY);
        srSO2.setIndicator(true);
        srSO2.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

        srNO2 = v.findViewById(R.id.rv_no2);
        srNO2.setSelectedSmile(srNO2.OKAY);
        srNO2.setIndicator(true);
        srNO2.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

        srO3 = v.findViewById(R.id.rv_o3);
        srO3.setSelectedSmile(srO3.OKAY);
        srO3.setIndicator(true);
        srO3.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

        srPM25 = v.findViewById(R.id.rv_pm25);
        srPM25.setSelectedSmile(srPM25.OKAY);
        srPM25.setIndicator(true);
        srPM25.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        break;
                }
            }
        });
        //smile end
    }

    public void setSmileChart(SmileRating srData, Float airData){
        if(airData > 200){
            srData.setSelectedSmile(srData.TERRIBLE, true);
        }else if(airData > 150){
            srData.setSelectedSmile(srData.BAD, true);
        }else if(airData > 101){
            srData.setSelectedSmile(srData.OKAY, true);
        }else if(airData > 50){
            srData.setSelectedSmile(srData.GOOD, true);
        }else{
            srData.setSelectedSmile(srData.GREAT, true);
        }
    }
}
