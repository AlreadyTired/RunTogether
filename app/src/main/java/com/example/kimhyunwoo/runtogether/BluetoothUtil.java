package com.example.kimhyunwoo.runtogether;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BluetoothUtil {
    public JSONObject airData;

    public String airDataJsonParsing(String data) throws JSONException {
        try {
            airData = new JSONObject(data);
        } catch (Exception e) {
            Log.e("[ERR] JSON", e.getMessage().toString());
            return null;
        }

        //  현재는 임시 데이터로 정의 됨
        String pm25 = airData.getString("PM25");
        String so2 = airData.getString("SO2");
        String jsonString = pm25 + "," + so2;

        return jsonString;
    }
}
