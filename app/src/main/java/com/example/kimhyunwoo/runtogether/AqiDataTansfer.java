package com.example.kimhyunwoo.runtogether;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.RealDataTransferRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AqiDataTansfer {
    private AlertDialog dialog;
    private static String COAqi="",SO2Aqi="",NO2Aqi="",O3Aqi="",PM25Aqi="",TotalAqi="",TimeStamp="";
    private static Queue<ArrayList> DataQueue = new LinkedList<>();
    public static boolean AqiErrorFlag = false;
    static int AqiErrorCount = 0;

    public static void setAqiData(String CoAqi,String So2Aqi,String No2Aqi,String o3Aqi,String Pm25Aqi,String TOTALAqi,String Timestamp)
    {
        COAqi = CoAqi;
        SO2Aqi = So2Aqi;
        NO2Aqi = No2Aqi;
        O3Aqi = o3Aqi;
        PM25Aqi = Pm25Aqi;
        TotalAqi = TOTALAqi;
        TimeStamp = Timestamp;
    }

    public void Request(final Context context)
    {
        ArrayList<String> DataList = new ArrayList<>();
        DataList.add(COAqi);
        DataList.add(SO2Aqi);
        DataList.add(NO2Aqi);
        DataList.add(O3Aqi);
        DataList.add(PM25Aqi);
        DataList.add(TotalAqi);
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
                    String message = jsonResponse.getString("message");               // success 이름으로 boolean 타입의 값이 넘어온다
                    if(message.equals("ok"))
                    {
                        DataQueue.poll();
                        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                        while(!DataQueue.isEmpty())
                        {
                            if(AqiErrorCount >3 || AqiErrorFlag ==true)
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
                                            AqiErrorCount++;
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
                        AqiErrorCount = 0;
                        AqiErrorFlag = false;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        AqiDataTransferRequest aqiDataTansfer = new AqiDataTransferRequest(DataList,reponseListener, context);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
        RequestQueue queue = Volley.newRequestQueue(context);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
        queue.add(aqiDataTansfer);
        AqiErrorFlag = false;
    }
}
