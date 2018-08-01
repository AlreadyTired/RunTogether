package com.example.kimhyunwoo.runtogether.usermanagement;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.kimhyunwoo.runtogether.ServerInfo.*;

public class RegisterRequest extends StringRequest{
    final static private String URL = serverURL+registerURL;
    private Map<String,String> parameters;

    public RegisterRequest(String userEmail,String userPassword, String userGender, String userNickname,String userAge,Response.Listener<String> listener){
        super(Request.Method.POST,URL,listener,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("volley Error",error.getMessage().toString());
            }
        });
        parameters = new HashMap<>();
        JSONObject informationObject = new JSONObject();
        try{
            informationObject.put("userEmail",userEmail);
            informationObject.put("userPassword",userPassword);
            informationObject.put("userGender",userGender);
            informationObject.put("userNickname",userNickname);
            informationObject.put("userAge",userAge);
        }catch(JSONException e)
        {
            e.printStackTrace();
        }

        parameters.put("json",informationObject.toString());
        Log.v("parameters",parameters.toString());
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}
