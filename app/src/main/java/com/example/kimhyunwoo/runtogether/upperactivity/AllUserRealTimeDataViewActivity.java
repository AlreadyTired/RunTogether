package com.example.kimhyunwoo.runtogether.upperactivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.R;
import com.example.kimhyunwoo.runtogether.UserInfo;
import com.example.kimhyunwoo.runtogether.usermanagement.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllUserRealTimeDataViewActivity extends AppCompatActivity {
    private int DataSelectCheckCount;
    private AlertDialog dialog;
    private Spinner UserListSpinner;
    private ArrayAdapter UserListSpinnerAdapter;
    private ArrayList<String> UserArrayList,UserEmailList;
    private String SelectedUserName,SelectedUserEmail;
    private boolean DataSelectFlag;
    private DataGetThread getDataThread;
    private TextView HRTextView,SO2TextView,NO2TextView,O3TextView,COTextView,TempTextView,PM25TextView,TotalAQITextView,COAQITextView,SO2AQITextView,NO2AQITextView,O3AQITextView,PM25AQITextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_real_time_data_view);
        DataSelectFlag = false;
        DataSelectCheckCount = 0;
        HRTextView = (TextView)findViewById(R.id.HRValueTextView);      TotalAQITextView = (TextView)findViewById(R.id.TotalAqiValueTextView);
        SO2TextView = (TextView)findViewById(R.id.So2ValueTextView);    COAQITextView = (TextView)findViewById(R.id.CoAqiValueTextView);
        NO2TextView = (TextView)findViewById(R.id.No2ValueTextView);    SO2AQITextView = (TextView)findViewById(R.id.So2AqiValueTextView);
        O3TextView = (TextView)findViewById(R.id.O3ValueTextView);      NO2AQITextView = (TextView)findViewById(R.id.No2AqiValueTextView);
        COTextView = (TextView)findViewById(R.id.CoValueTextView);      O3AQITextView = (TextView)findViewById(R.id.O3AqiValueTextView);
        TempTextView = (TextView)findViewById(R.id.TempValueTextView);  PM25AQITextView = (TextView)findViewById(R.id.PM25AqiValueTextView);
        PM25TextView = (TextView)findViewById(R.id.PM25ValueTextView);

        UserListSpinner = (Spinner)findViewById(R.id.UserListSpinner);
        UserArrayList = new ArrayList<>();
        UserEmailList = new ArrayList<>();
        UserArrayList.add("SelectUserID");
        UserEmailList.add("SelectUserID");

        // UserList Request to Server To Do Here
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
                        JSONArray array = jsonResponse.getJSONArray("data");

                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject object = array.getJSONObject(i);
                            String Emaildata = object.getString("email");
                            String Nicknamedata = object.getString("nickname");
                            String data = Nicknamedata+" "+Emaildata;
                            UserArrayList.add(data);
                            UserEmailList.add(Emaildata);
                        }

                        Toast.makeText(AllUserRealTimeDataViewActivity.this, "Get UserListSuccess", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AllUserRealTimeDataViewActivity.this);
                        dialog = builder.setMessage(message)
                                .setNegativeButton("Return to LastPage", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                    }
                                })
                                .create();
                        dialog.show();
                        onBackPressed();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        UserListRequest UserlistRequest = new UserListRequest(reponseListener,AllUserRealTimeDataViewActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
        RequestQueue queue = Volley.newRequestQueue(AllUserRealTimeDataViewActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
        queue.add(UserlistRequest);

        // 임시///////////////////////////////////////////////////////////////////////////////////지워야해!~!!!~!!@#%^%$#@!#%^&^%$#@#%^&*&^%$#@#%*
        UserArrayList.add("HI123123");
        UserArrayList.add("HI456456");
        UserEmailList.add("HI123123");
        UserEmailList.add("HI456456");

        UserListSpinnerAdapter = new ArrayAdapter(AllUserRealTimeDataViewActivity.this,R.layout.support_simple_spinner_dropdown_item,UserArrayList);
        UserListSpinner.setAdapter(UserListSpinnerAdapter);
        UserListSpinner.setSelection(0,false);
        UserListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                SelectedUserEmail = UserEmailList.get(position);
                if(DataSelectCheckCount>=0)
                {
                    DataSelectFlag = true;
                    Log.v("User's Log","I'm in the onItemSelectedListner and I'm Change DataSelectFlag");
                }
                DataSelectCheckCount++;
                Log.v("User's Log", Integer.toString(DataSelectCheckCount));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("User's Log","I'm in the onItemSelectedListner and NothingSelected");
            }
        });
        Log.v("User's Log",DataSelectFlag==true?"DataSelectFlag = true":"DataSelectFlag = false");
        DataSelectFlag = false;
        getDataThread = new DataGetThread();
        getDataThread.start();
        Log.v("User's Log",DataSelectFlag==true?"DataSelectFlag = true":"DataSelectFlag = false");
    }
    public class DataGetThread extends Thread
    {
        public boolean ThreadFlag;
        public DataGetThread()
        {
            // 초기화 작업
            ThreadFlag = false;
        }

        public void run()
        {
            while(true)
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.v("User's Log",DataSelectFlag==true?"In Thread DataSelectFlag = true":"In Thread DataSelectFlag = false");
                if(ThreadFlag)
                {
                    break;
                }
                if(DataSelectFlag)
                {
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
                                    JSONObject dataObject = jsonResponse.getJSONObject("data");
                                    HRTextView.setText(dataObject.getString("heart"));
                                    SO2TextView.setText(dataObject.getString("so2"));
                                    NO2TextView.setText(dataObject.getString("no2"));
                                    O3TextView.setText(dataObject.getString("o3"));
                                    COTextView.setText(dataObject.getString("Co"));
                                    TempTextView.setText(dataObject.getString("temp"));
                                    PM25TextView.setText(dataObject.getString("pm25"));
                                    TotalAQITextView.setText(dataObject.getString("totalaqi"));
                                    COAQITextView.setText(dataObject.getString("coaqi"));
                                    SO2AQITextView.setText(dataObject.getString("so2aqi"));
                                    NO2AQITextView.setText(dataObject.getString("no2aqi"));
                                    O3AQITextView.setText(dataObject.getString("o3aqi"));
                                    PM25AQITextView.setText(dataObject.getString("pm25aqi"));

                                    Toast.makeText(AllUserRealTimeDataViewActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AllUserRealTimeDataViewActivity.this);
                                    dialog = builder.setMessage(message)
                                            .setNegativeButton("OK", null)
                                            .create();
                                    dialog.show();
                                    onBackPressed();
                                }
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };
                    SpecificUserDataRequest DataGetRequest = new SpecificUserDataRequest(SelectedUserEmail,reponseListener,AllUserRealTimeDataViewActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                    RequestQueue queue = Volley.newRequestQueue(AllUserRealTimeDataViewActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                    queue.add(DataGetRequest);

                    Log.v("Thread is still alive","YEEEEEEEEEEAAAA");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Response.Listener<String> ThreadreponseListener = new Response.Listener<String>() {
                        // Volley 를 통해서 정상적으로 웹서버와 통신이 되면 실행되는 함수

                        @Override
                        public void onResponse(String response)
                        {
                            UserArrayList.clear();
                            UserEmailList.clear();
                            try
                            {
                                // JSON 형식으로 값을 response 에 받아서 넘어온다.
                                JSONObject jsonResponse = new JSONObject(response);
                                String message = jsonResponse.getString("message");
                                if(message.equals("ok"))
                                {
                                    JSONArray array = jsonResponse.getJSONArray("data");

                                    for(int i=0;i<array.length();i++)
                                    {
                                        JSONObject object = array.getJSONObject(i);
                                        String Emaildata = object.getString("email");
                                        String Nicknamedata = object.getString("nickname");
                                        String data = Nicknamedata+" "+Emaildata;
                                        UserArrayList.add(data);
                                        UserEmailList.add(Emaildata);
                                    }

                                    Toast.makeText(AllUserRealTimeDataViewActivity.this, "Get UserListSuccess", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(AllUserRealTimeDataViewActivity.this, "Get UserList Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };
                    UserListRequest UserlistRequest = new UserListRequest(ThreadreponseListener,AllUserRealTimeDataViewActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                    RequestQueue Threadqueue = Volley.newRequestQueue(AllUserRealTimeDataViewActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                    Threadqueue.add(UserlistRequest);
                    Log.v("Thread is still alive","YEEEEEEEEEEAAAA");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        getDataThread.ThreadFlag = true;
        Log.v("DestroyActivity",getDataThread.ThreadFlag==true?"true":"false");
        super.onDestroy();
    }
}
