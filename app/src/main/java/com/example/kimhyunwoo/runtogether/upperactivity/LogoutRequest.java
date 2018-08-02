package com.example.kimhyunwoo.runtogether.upperactivity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kimhyunwoo.runtogether.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.kimhyunwoo.runtogether.ServerInfo.*;

public class LogoutRequest extends StringRequest {
    final static private String URL = serverURL + logoutURL;                          // URL 헤더파일로 묶어서 수정해야함.
    private Map<String,String> parameters;                                                          // Map 형식으로 데이터를 저장(JSON 이기에 Stirng2개)

    // Volley의 StringRequest를 선언받아서 사용해야 하는데 내가 사용할 값들과 부모의 인자값까지 사용해야함. 앞서 선언한 map 에 필요한 값들을 JSON형식의 String 2개값을 넣어서 보냄.
    public LogoutRequest(Response.Listener<String> listener, final Context context)
    {
        super(Request.Method.POST,URL,listener,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                dialog = builder.setMessage("Connect Error, Please Try again later")
                        .setNegativeButton("OK", null)
                        .create();
                dialog.show();
                return;

            }
        });
        parameters = new HashMap<>();
        JSONObject informationObject = new JSONObject();
        try{
            informationObject.put("token", UserInfo.getUserToken());
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        parameters.put("json",informationObject.toString());
        Log.v("parameters",parameters.toString());
    }



    public Map<String,String>getParams()
    {
        return parameters;
    }
}
