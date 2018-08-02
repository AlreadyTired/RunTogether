package com.example.kimhyunwoo.runtogether.upperactivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.BluetoothSingleton;
import com.example.kimhyunwoo.runtogether.R;
import com.example.kimhyunwoo.runtogether.UserInfo;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.DeviceListActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.LoginActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpperFragment extends Fragment {
    private static final String TAG = "BluetoothChatFragment";

    private AlertDialog dialog;

    TextView UserNicknameView;
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

        UserNicknameView = view.findViewById(R.id.UserNicknameViewText);
        buttonInfo = view.findViewById(R.id.btn_info);
        buttonBluetooth = view.findViewById(R.id.btn_bluetooth);
        buttonLogout = view.findViewById(R.id.btn_logout);

        UserNicknameView.setText(UserInfo.getUserNickname());

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
                                Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_SHORT).show();
                                UserInfo.UserDataReset();
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                getActivity().startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                dialog = builder.setMessage(message)
                                        .setNegativeButton("Try Again",null)
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
                LogoutRequest loginRequest = new LogoutRequest(reponseListener,getActivity());           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                RequestQueue queue = Volley.newRequestQueue(getActivity());            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                queue.add(loginRequest);
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
