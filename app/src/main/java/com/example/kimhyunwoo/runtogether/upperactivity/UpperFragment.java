package com.example.kimhyunwoo.runtogether.upperactivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kimhyunwoo.runtogether.BluetoothSingleton;
import com.example.kimhyunwoo.runtogether.R;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.DeviceListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpperFragment extends Fragment {
    private static final String TAG = "BluetoothChatFragment";

    Button buttonInfo = null;
    Button buttonBluetooth = null;
    Button buttonLogout = null;

    BluetoothSingleton btSingletion;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    public UpperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        btSingletion = BluetoothSingleton.getInstance();

        View view = inflater.inflate(R.layout.fragment_upper, container, false);
        // Inflate the layout for this fragment

        buttonInfo = view.findViewById(R.id.btn_info);
        buttonBluetooth = view.findViewById(R.id.btn_bluetooth);
        buttonLogout = view.findViewById(R.id.btn_logout);

        buttonInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
            }
        });

        buttonBluetooth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    // 다이얼이 끝나고 여기로 결과가 전송됨
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    btSingletion.connectDevice(data);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    //setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

}
