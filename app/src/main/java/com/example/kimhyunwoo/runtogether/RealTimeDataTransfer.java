package com.example.kimhyunwoo.runtogether;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RealTimeDataTransfer {
    private AlertDialog dialog;
    private static String CO = "",HeartRate="70",SO2= "",NO2= "",O3= "",PM25= "",Temp= "",LANG= "32.881033",LONG= "-117.235601",TimeStamp="";
    private static Queue<ArrayList> DataQueue = new LinkedList<>();
    public static boolean RealTimeErrorFlag = false;
    static int RealTimeErrorCount = 0;

    private static TextView TextHeartRate = null;

    public static void setAirData(String Co,String So2, String No2,String o3,String Pm25,String TEMP,String TIMESTAMP)
    {
        CO = Co;
        SO2 = So2;
        NO2 = No2;
        O3 = o3;
        PM25 = Pm25;
        Temp = TEMP;
        TimeStamp = TIMESTAMP;
    }
    public static void setHeartRate(String heart)
    {
        HeartRate = heart;
    }

    public static String getHeartRate(){
        return HeartRate;
    }

    public static void setTextView(TextView hr){
        TextHeartRate = hr;
    }

    public static void setTextHeartrate() {
        if (TextHeartRate != null) {
            TextHeartRate.setText(HeartRate);
        }
    }
    public static void ShowData(){
        Log.v("[INFO] RealTimeDataTransfer ", CO + "," + SO2+ "," +NO2+ "," +O3 + "," +PM25+ "," +Temp+ "," +LANG+ "," +LONG+ "," +HeartRate+ "," +TimeStamp);
    }

    public static void setGPS(String Lang, String Long)
    {
        LANG = Lang;
        LONG = Long;
    }

    public void Request(final Context context)
    {
        ArrayList<String> DataList = new ArrayList<>();
        DataList.add(CO);
        DataList.add(SO2);
        DataList.add(NO2);
        DataList.add(O3);
        DataList.add(PM25);
        DataList.add(Temp);
        DataList.add(LANG);
        DataList.add(LONG);
        DataList.add(HeartRate);
        DataList.add(TimeStamp);

        if(DataQueue.size()>5)
        {
            DataQueue.poll();
        }
        DataQueue.add(DataList);

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
                    Log.v("Message",message);// success 이름으로 boolean 타입의 값이 넘어온다
                    if(message.equals("ok"))
                    {
                        DataQueue.poll();
                        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                        while(!DataQueue.isEmpty())
                        {
                            if(RealTimeErrorCount >3 || RealTimeErrorFlag ==true)
                            {
                                break;
                            }
                            Response.Listener<String> ReresponseListner = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String Reresponse) {
                                    try
                                    {
                                        JSONObject rejsonResponse = new JSONObject(Reresponse);
                                        String message = rejsonResponse.getString("message");
                                        if(message.equals("ok"))
                                        {
                                            DataQueue.poll();
                                        }
                                        else
                                        {
                                            RealTimeErrorCount++;
                                        }
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            ArrayList<String> TempData = DataQueue.element();
                            RealDataTransferRequest RealtimedataqueueRequest = new RealDataTransferRequest(TempData,ReresponseListner,context);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                            RequestQueue queue = Volley.newRequestQueue(context);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                            queue.add(RealtimedataqueueRequest);
                        }
                        RealTimeErrorCount = 0;
                        RealTimeErrorFlag = false;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        RealDataTransferRequest RealtimedataRequest = new RealDataTransferRequest(DataList,reponseListener,context);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
        RequestQueue queue = Volley.newRequestQueue(context);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
        queue.add(RealtimedataRequest);
        RealTimeErrorFlag = false;
    }
}
