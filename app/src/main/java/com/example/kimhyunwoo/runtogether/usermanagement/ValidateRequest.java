package com.example.kimhyunwoo.runtogether.usermanagement;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.kimhyunwoo.runtogether.ServerInfo.*;

public class ValidateRequest extends StringRequest{
    final static private String URL = serverURL + validateURL;
    private Map<String,String> parameters;

    public ValidateRequest(String userEmail,Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.v("volley Error",error.getMessage().toString());
                    }
                });
        parameters = new HashMap<>();
        JSONObject informationObject = new JSONObject();
        try{
            informationObject.put("userEmail",userEmail);
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
