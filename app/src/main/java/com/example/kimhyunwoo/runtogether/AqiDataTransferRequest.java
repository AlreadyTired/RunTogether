package com.example.kimhyunwoo.runtogether;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.kimhyunwoo.runtogether.ServerInfo.*;

public class AqiDataTransferRequest extends StringRequest {
    final static private String URL = serverURL + AqiDataTransferURL;                          // URL 헤더파일로 묶어서 수정해야함.
    private Map<String,String> parameters;                                                          // Map 형식으로 데이터를 저장(JSON 이기에 Stirng2개)

    // Volley의 StringRequest를 선언받아서 사용해야 하는데 내가 사용할 값들과 부모의 인자값까지 사용해야함. 앞서 선언한 map 에 필요한 값들을 JSON형식의 String 2개값을 넣어서 보냄.
    public AqiDataTransferRequest(ArrayList<String> DataList, Response.Listener<String> listener, final Context context)
    {
        super(Request.Method.POST,URL,listener,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                dialog = builder.setMessage("Connect Error, Please Try again later")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AqiDataTansfer.AqiErrorFlag = true;
                            }
                        })
                        .create();
                dialog.show();
                return;
            }
        });
        parameters = new HashMap<>();
        JSONObject informationObject = new JSONObject();
        try{
            informationObject.put("coaqi",DataList.get(0));
            informationObject.put("so2aqi",DataList.get(1));
            informationObject.put("no2aqi",DataList.get(2));
            informationObject.put("o3aqi",DataList.get(3));
            informationObject.put("pm25aqi",DataList.get(4));
            informationObject.put("totalaqi",DataList.get(5));
            informationObject.put("timestamp",DataList.get(6));
            informationObject.put("token",UserInfo.getUserToken());
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        parameters.put("json",informationObject.toString());
    }

    public Map<String,String>getParams()
    {
        return parameters;
    }
}
