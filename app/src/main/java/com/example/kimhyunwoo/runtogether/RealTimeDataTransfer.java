package com.example.kimhyunwoo.runtogether;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.mainactivity.MainActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.FindPasswordActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.FindPasswordRequest;
import com.example.kimhyunwoo.runtogether.usermanagement.LoginActivity;

import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class RealTimeDataTransfer {
    private AlertDialog dialog;
    private static String CO = "",SO2= "",NO2= "",O3= "",PM25= "",Temp= "",HeartRate= "",Lang= "",Long= "",TimeStamp="";
    private static Queue<ArrayList> DataQueue = new LinkedList<>();
    public static boolean ErrorFlag = false;
    int ErrorCount = 0;

    public void setAirData(String CO,String SO2, String NO2,String O3,String PM25,String Temp,String TimeStamp)
    {
        this.CO = CO;
        this.SO2 = SO2;
        this.NO2 = NO2;
        this.O3 = O3;
        this.PM25 = PM25;
        this.Temp = Temp;
        this.TimeStamp = TimeStamp;
    }
    public void setHeartRate(String HeartRate)
    {
        this.HeartRate = HeartRate;
    }
    public void setGPS(String Lang,String Long)
    {
        this.Lang = Lang;
        this.Long = Long;
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
        DataList.add(Lang);
        DataList.add(Long);
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
                    String message = jsonResponse.getString("message");               // success 이름으로 boolean 타입의 값이 넘어온다
                    if(message.equals("ok"))
                    {
                        DataQueue.poll();
                        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                        while(!DataQueue.isEmpty())
                        {
                            if(ErrorCount>3 || ErrorFlag==true)
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
                                            ErrorCount++;
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
                        ErrorCount = 0;
                        ErrorFlag = false;
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
        ErrorFlag = false;
    }
}
