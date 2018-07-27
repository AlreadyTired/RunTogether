package com.example.kimhyunwoo.runtogether.usermanagement;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{
    final static private String URL = "http://100.64.12.80/UserRegister.php";
    private Map<String,String> parameters;

    public RegisterRequest(String userEmail,String userPassword, String userGender, String userNickname,String userAge,Response.Listener<String> listener){
        super(Request.Method.POST,URL,listener,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("volley Error",error.getMessage().toString());
            }
        });
        parameters = new HashMap<>();
        parameters.put("userEmail",userEmail);
        parameters.put("userPassword",userPassword);
        parameters.put("userGender",userGender);
        parameters.put("userNickname",userNickname);
        parameters.put("userAge",userAge);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}
