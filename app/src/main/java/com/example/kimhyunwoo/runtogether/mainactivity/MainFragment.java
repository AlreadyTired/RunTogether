package com.example.kimhyunwoo.runtogether.mainactivity;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import com.example.kimhyunwoo.runtogether.MapUtil;
import com.example.kimhyunwoo.runtogether.R;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.BluetoothChatService;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.Constants;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.DeviceListActivity;
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

    //===================================================================
    //  구글 맵 변수

    MapUtil util = null;

    LocationManager manager;

    GoogleMap map = null;
    MarkerOptions markerOptions = new MarkerOptions();

    //  TODO(db 연동 시에 db에서 마지막 위치를 받아 오도록 하자)
    //  현재는 db에 연동이 안되어 임의로 설정함
    LatLng savedCoordinate = new LatLng(32.881033, -117.235601);
    LatLng startLat = null;
    LatLng endLat = null;

    boolean sendResult = false;
    boolean exercisingFlag = false;

    Button buttonStart;
    Button buttonEnd;
    Button buttonReset;
    Button buttonCalc;

    //  TODO(그루트와 코드 번호를 정의 해보자)
    private static final int markerRequstCode = 1234;
    //  구글 맵 변수 끝
    //===================================================================

    //===================================================================
    //  블루투스 변수
    Button bt_list;

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    //  불루투스 변수 끝
    //===================================================================

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
        buttonReset = (Button)view.findViewById(R.id.btn_reset);
        buttonCalc = (Button)view.findViewById(R.id.btn_calc);

        // 리스너에 버튼을 등록함
        buttonStart.setOnClickListener(this);
        buttonEnd.setOnClickListener(this);
        buttonReset.setOnClickListener(this);
        buttonCalc.setOnClickListener(this);

        //  구글맵 쓰레드 시작ㅈ
        mapFragment.getMapAsync(this);

        //  프래그먼트가 호출된 상위 액티비티를 가져올수있음
        //  MainActivity를 호출한 액티비티를 가져옴
        //  getActivity는 MainActivity를 가지고있는 액티비티
        //  상위 액티비티의 자원을 사용하기 위해서 Activity를 가져옴
        MainActivity activity = (MainActivity) getActivity();
        manager = activity.getLocationManager();

        //===========================================================
        bt_list = (Button)view.findViewById(R.id.btn_list);
        bt_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, 2);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonStart) {
            String toastText = "";

            //  sendResult가 true면 서버로 보낼 운동 데이터가 남은 상태로 간주
            if(sendResult != false){
                toastText = "press reset button!";
            }else if(exercisingFlag != true) {
                exercisingFlag = true;
                startLat = savedCoordinate;
                toastText = "exercise start!!";
            }else{
                toastText = "already start!!";
            }

            Context context = getActivity().getApplicationContext();
            Toast toast = Toast.makeText(context,toastText, Toast.LENGTH_SHORT);
            toast.show();

        }else if (v == buttonEnd){
            String toastText = "";

            if(exercisingFlag != false) {
                exercisingFlag = false;
                sendResult = true;
                endLat = savedCoordinate;
                toastText = "exercise end!!";
            }else{
                toastText = "press start button!!";
            }

            Context context = getActivity().getApplicationContext();
            Toast toast = Toast.makeText(context,toastText, Toast.LENGTH_SHORT);
            toast.show();

        }else if (v == buttonReset) {
            Context context = getActivity().getApplicationContext();
            String toastText = "";

            //  TODO(서버로 전송 기능 구현)
            if (sendResult) {
                sendResult = false;
                map.clear();

                toastText = "Reset Google Map";
            }else{
                toastText = "No record...";
            }

            Toast toast = Toast.makeText(context,toastText, Toast.LENGTH_SHORT);
            toast.show();

        } else if (v == buttonCalc) {
            //Calculating the distance in meters
            Double distance = 0d;

            //  startLat, endLat이 null일 때 앱 튕김 방지
            if(startLat != null && endLat != null) {
                distance = SphericalUtil.computeDistanceBetween(startLat, endLat);
            }

            Context context = getActivity().getApplicationContext();
            //Displaying the distance
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context,"distance " + distance, duration);
            toast.show();
        }
    }

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
                break;
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //  현재는 사용 안함.
        //  사용하면 다시 활성화 하자.
//        //  좌표 적용
//        //  마커생성
//        markerOptions.position(savedCoordinate); //좌표
//        markerOptions.title("임시 마커");
//        //  마커를 화면에 그림
//        map.addMarker(markerOptions);
//        //  맵의 중심을 해당 좌표로 이동
//        //  savedCoordinate : 좌표
//        //  v: 줌레벨
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(savedCoordinate,util.zoomLevel));
//
//        map.setOnMarkerClickListener(this);
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

        //==========================================================================
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
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

            //  바뀐 현재 좌표
            LatLng currentCoordinate = new LatLng(currentLat ,currentLng);

            //  현재 좌표에 마커를 찍기 위해서 옵션에 저장
            markerOptions.position(currentCoordinate);

            if(exercisingFlag == true) {
                // 라인 그리기
                util.polylineOnMap(map, savedCoordinate, currentCoordinate);
                savedCoordinate = currentCoordinate;
            }

            //  마커 삭제
            util.deleteMarker(map, markerOptions);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoordinate,util.zoomLevel));

            //  디버깅 용
            Context context = getActivity().getApplicationContext();
            Toast toast = Toast.makeText(context,"lat : "+currentLat + "\nlng : "
                    + currentLng, Toast.LENGTH_SHORT);
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

    //=========================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //  Set google map util
        util = new MapUtil();

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.lv_received_data);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);

    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

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
                            setStatus(getString(R.string.title_connecting) + mConnectedDeviceName);
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ: //메세지 여기로 받음
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
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


    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }
}
